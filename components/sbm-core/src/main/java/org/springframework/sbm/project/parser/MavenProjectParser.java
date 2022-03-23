/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.project.parser;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.BuildTool;
import org.openrewrite.marker.GitProvenance;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.tree.Maven;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.text.PlainTextParser;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.xml.tree.Xml;
import org.openrewrite.yaml.YamlParser;
import org.openrewrite.yaml.tree.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.events.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.openrewrite.Tree.randomId;

/**
 * Parse a Maven project on disk into a list of {@link org.openrewrite.SourceFile} including
 * Maven, Java, YAML, properties, and XML AST representations of sources and resources found.
 */
public class MavenProjectParser {

    private static final Pattern mavenWrapperVersionPattern = Pattern.compile(".*apache-maven/(.*?)/.*");
    private static final Logger logger = LoggerFactory.getLogger(MavenProjectParser.class);

    private final MavenParser mavenParser;
    private final MavenArtifactDownloader artifactDownloader;
    private final JavaParser.Builder<?, ?> javaParserBuilder;
    private final ApplicationEventPublisher eventPublisher;
    private final ExecutionContext ctx;

    public MavenProjectParser(MavenArtifactDownloader artifactDownloader,
                              MavenParser.Builder mavenParserBuilder,
                              JavaParser.Builder<?, ?> javaParserBuilder,
                              ApplicationEventPublisher eventPublisher, ExecutionContext ctx) {
        this.mavenParser = mavenParserBuilder.build();
        this.artifactDownloader = artifactDownloader;
        this.javaParserBuilder = javaParserBuilder;
        this.eventPublisher = eventPublisher;
        this.ctx = ctx;
    }

    public List<SourceFile> parse(Path projectDirectory, List<Resource> resources) {
        GitProvenance gitProvenance = GitProvenance.fromProjectDirectory(projectDirectory);

        List<Resource> filteredMavenPoms = filterMavenPoms(resources);
        List<Parser.Input> inputs = filteredMavenPoms.stream()
                .map(r -> new Parser.Input(getPath(r),
                                () -> {
                                    eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(r)));
                                    InputStream is = getInputStream(r);
                                    return is;
                                }
                        )
                )
                .collect(Collectors.toList());

        // -1 sets the max to infinite as new pom files might be added to the collection during scan
        eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Maven", inputs.size()));
        List<Maven> mavens = mavenParser.parseInputs(inputs, projectDirectory, ctx);
        eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());

        mavens = sort(mavens);

        JavaParser javaParser = javaParserBuilder
                .build();

        logger.info("The order in which projects are being parsed is:");
        for (Maven maven : mavens) {
            logger.info("  {}:{}", maven.getModel().getGroupId(), maven.getModel().getArtifactId());
        }

        List<SourceFile> sourceFiles = new ArrayList<>();
        for (Maven maven : mavens) {
            List<Marker> projectProvenance = getJavaProvenance(maven, projectDirectory);
            sourceFiles.add(addProjectProvenance(maven, projectProvenance));

            List<Path> dependencies = downloadArtifacts(maven.getModel().getDependencies(Scope.Compile));
            JavaSourceSet mainProvenance = JavaSourceSet.build("main", dependencies, ctx);
            javaParser.setClasspath(dependencies);

            List<Resource> javaSources = getJavaSources(projectDirectory, resources, maven);

            List<Parser.Input> javaSourcesInput = javaSources.stream().map(js -> new Parser.Input(getPath(js), () -> {
                eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(js)));
                InputStream content = getInputStream(js);
                return content;
            })).collect(Collectors.toList());

            eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Java [main]: '" + maven.getModel().getArtifactId() + "'", javaSourcesInput.size()));
            List<J.CompilationUnit> compilationUnits = javaParser.parseInputs(javaSourcesInput, projectDirectory, ctx);
            eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());

            /*javaParser.parse(maven.getJavaSources(projectDirectory, ctx) javaSources, projectDirectory, ctx)*/
            sourceFiles.addAll(ListUtils.map(compilationUnits, addProvenance(projectProvenance, mainProvenance)));

            List<Path> testDependencies = downloadArtifacts(maven.getModel().getDependencies(Scope.Test));
            JavaSourceSet testProvenance = JavaSourceSet.build("test", testDependencies, ctx);
            javaParser.setClasspath(testDependencies);

            List<Resource> testJavaSources = getTestJavaSources(projectDirectory, resources, maven);
            List<Parser.Input> testJavaSourcesInput = testJavaSources.stream().map(js -> new Parser.Input(getPath(js), () -> {
                eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(js)));
                return getInputStream(js);
            })).collect(Collectors.toList());

            eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Java [test]: '" + maven.getModel().getArtifactId() + "'", testJavaSourcesInput.size()));
            List<J.CompilationUnit> testCompilationUnits = javaParser.parseInputs(testJavaSourcesInput, projectDirectory, ctx);
            eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());

            sourceFiles.addAll(ListUtils.map(testCompilationUnits, addProvenance(projectProvenance, testProvenance)));

            parseResources(getWebappResources(projectDirectory, resources, maven), projectDirectory, sourceFiles, projectProvenance, mainProvenance);
            parseResources(getMulesoftResources(projectDirectory, resources, maven), projectDirectory, sourceFiles, projectProvenance, mainProvenance);
            parseResources(getResources(projectDirectory, resources, maven), projectDirectory, sourceFiles, projectProvenance, mainProvenance);
            parseResources(getTestResources(projectDirectory, resources, maven), projectDirectory, sourceFiles, projectProvenance, testProvenance);
        }

        return ListUtils.map(sourceFiles, s -> s.withMarkers(s.getMarkers().addIfAbsent(gitProvenance)));
    }

    private InputStream getInputStream(Resource r) {
        try {
            return r.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getPath(Resource r) {
        try {
            return r.getFile().toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Resource> getWebappResources(Path projectDir, List<Resource> resources, Maven maven) {
        if (!"jar".equals(maven.getMavenModel().getPom().getPackaging()) && !"bundle".equals(maven.getMavenModel().getPom().getPackaging())) {
            return emptyList();
        }
        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "webapp"));
        return resources.stream()
                .filter(r -> getPath(r).startsWith(inPath) /* && Stream.of(".properties", ".xml", ".yml", ".yaml").anyMatch(fe -> r.getPath().toString().endsWith(fe))*/)
                .collect(Collectors.toList());
    }

    private List<Resource> getMulesoftResources(Path projectDir, List<Resource> resources, Maven maven) {
        Set mulePaths = Set.of(
            projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "app")),
            projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "mule")),
            projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "api"))
        );
//        Path appPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "app"));
//        Path mulePath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "mule"));
//        Path apiPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "api"));
        return resources.stream()
                .filter(r -> mulePaths.stream().anyMatch(appPath -> getPath(r).startsWith(appPath.toString())) /* && Stream.of(".properties", ".xml", ".yml", ".yaml").anyMatch(fe -> r.getPath().toString().endsWith(fe))*/)
                .collect(Collectors.toList());
    }


    public List<Resource> filterMavenPoms(List<Resource> resources) {
        return resources.stream()
                .filter(p -> getPath(p).getFileName().toString().equals("pom.xml") &&
                        !p.toString().contains("/src/"))
                .collect(Collectors.toList());
    }

    public List<Resource> getJavaSources(Path projectDir, List<Resource> resources, Maven maven) {
//        if (!"jar".equals(maven.getMavenModel().getPom().getPackaging()) && !"bundle".equals(maven.getMavenModel().getPom().getPackaging())) {
//            return emptyList();
//        }
        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "java"));
        return resources.stream()
                .filter(r -> getPath(r).startsWith(inPath) && getPath(r).toString().endsWith(".java"))
                .collect(Collectors.toList());
    }

    public List<Resource> getTestJavaSources(Path projectDir, List<Resource> resources, Maven maven) {
//        if (!"jar".equals(maven.getMavenModel().getPom().getPackaging()) && !"bundle".equals(maven.getMavenModel().getPom().getPackaging())) {
//            return emptyList();
//        }
        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "test", "java"));
        return resources.stream()
                .filter(r -> getPath(r).startsWith(inPath) && getPath(r).toString().endsWith(".java"))
                .collect(Collectors.toList());
    }


    public List<Resource> getResources(Path projectDir, List<Resource> resources, Maven maven) {
//        if (!"jar".equals(maven.getMavenModel().getPom().getPackaging()) && !"bundle".equals(maven.getMavenModel().getPom().getPackaging())) {
//            return emptyList();
//        }
        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "resources"));
        return resources.stream()
                .filter(r -> getPath(r).startsWith(inPath) /* && Stream.of(".properties", ".xml", ".yml", ".yaml").anyMatch(fe -> r.getPath().toString().endsWith(fe))*/)
                .collect(Collectors.toList());
    }

    public List<Resource> getTestResources(Path projectDir, List<Resource> resources, Maven maven) {
//        if (!"jar".equals(maven.getMavenModel().getPom().getPackaging()) && !"bundle".equals(maven.getMavenModel().getPom().getPackaging())) {
//            return emptyList();
//        }
        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "test", "resources"));
        return resources.stream()
                .filter(r -> getPath(r).startsWith(inPath) /*&& Stream.of(".properties", ".xml", ".yml", ".yaml").anyMatch(fe -> r.getPath().toString().endsWith(fe))*/)
                .collect(Collectors.toList());
    }

    private List<Resource> mapToResource(List<Path> testResources) {

        return testResources.stream()
                .map(p -> new FileSystemResource(p))
                .collect(Collectors.toList());
    }

    private List<Marker> getJavaProvenance(Maven maven, Path projectDirectory) {
        Pom mavenModel = maven.getModel();
        String javaRuntimeVersion = System.getProperty("java.runtime.version");
        String javaVendor = System.getProperty("java.vm.vendor");
        String sourceCompatibility = javaRuntimeVersion;
        String targetCompatibility = javaRuntimeVersion;
        String propertiesSourceCompatibility = mavenModel.getValue(mavenModel.getValue("maven.compiler.source"));
        if (propertiesSourceCompatibility != null) {
            sourceCompatibility = propertiesSourceCompatibility;
        }
        String propertiesTargetCompatibility = mavenModel.getValue(mavenModel.getValue("maven.compiler.target"));
        if (propertiesTargetCompatibility != null) {
            targetCompatibility = propertiesTargetCompatibility;
        }

        Path wrapperPropertiesPath = projectDirectory.resolve(".mvn/wrapper/maven-wrapper.properties");
        String mavenVersion = "3.6";
        if (Files.exists(wrapperPropertiesPath)) {
            try {
                Properties wrapperProperties = new Properties();
                wrapperProperties.load(new FileReader(wrapperPropertiesPath.toFile()));
                String distributionUrl = (String) wrapperProperties.get("distributionUrl");
                if (distributionUrl != null) {
                    Matcher wrapperVersionMatcher = mavenWrapperVersionPattern.matcher(distributionUrl);
                    if (wrapperVersionMatcher.matches()) {
                        mavenVersion = wrapperVersionMatcher.group(1);
                    }
                }
            } catch (IOException e) {
                ctx.getOnError().accept(e);
            }
        }

        return Arrays.asList(
                new BuildTool(randomId(), BuildTool.Type.Maven, mavenVersion),
                new JavaVersion(randomId(), javaRuntimeVersion, javaVendor, sourceCompatibility, targetCompatibility),
                new JavaProject(randomId(), mavenModel.getName(), new JavaProject.Publication(
                        mavenModel.getGroupId(),
                        mavenModel.getArtifactId(),
                        mavenModel.getVersion()
                ))
        );
    }

    private void parseResources(List<Resource> resources, Path projectDirectory, List<SourceFile> sourceFiles, List<Marker> projectProvenance, JavaSourceSet sourceSet) {
        XmlParser xmlParser = new XmlParser();

        List<Parser.Input> xmlFiles = resources.stream()
                .filter(p -> xmlParser.accept(getPath(p)))
                .map(r -> new Parser.Input(getPath(r), () -> {
                    eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(r)));
                    return getInputStream(r);
                }))
                .collect(Collectors.toList());

        eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Xml", xmlFiles.size()));

        List<Xml.Document> documents = xmlParser.parseInputs(
                // TODO: duplicates
                xmlFiles,
                projectDirectory,
                ctx
        );
        sourceFiles.addAll(ListUtils.map(documents, addProvenance(projectProvenance, sourceSet)));

        eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());



        YamlParser yamlParser = new YamlParser();
        List<Parser.Input> yamlFiles = resources.stream()
                .filter(p -> yamlParser.accept(getPath(p)))
                .map(r -> new Parser.Input(getPath(r), () -> {
                    eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(r)));
                    return getInputStream(r);
                }))
                .collect(Collectors.toList());

        eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Yaml", yamlFiles.size()));

        List<Yaml.Documents> yamls = yamlParser.parseInputs(
                yamlFiles,
                projectDirectory,
                ctx
        );
        sourceFiles.addAll(ListUtils.map(yamls, addProvenance(projectProvenance, sourceSet)));

        eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());



        PropertiesParser propertiesParser = new PropertiesParser();
        List<Parser.Input> propertiesFiles = resources.stream()
                .filter(p -> propertiesParser.accept(getPath(p)))
                .map(r -> new Parser.Input(getPath(r), () -> {
                    eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(r)));
                    return getInputStream(r);
                }))
                .collect(Collectors.toList());

        eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Properties", propertiesFiles.size()));

        List<org.openrewrite.properties.tree.Properties.File> properties = propertiesParser.parseInputs(
                propertiesFiles,
                projectDirectory,
                ctx
        );
        sourceFiles.addAll(ListUtils.map(properties, addProvenance(projectProvenance, sourceSet)));

        eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());



        eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("other files", propertiesFiles.size()));

        List<Parser.Input> otherFiles = resources.stream()
                .filter(p -> !xmlParser.accept(getPath(p)) && !yamlParser.accept(getPath(p)) && !propertiesParser.accept(getPath(p)))
                .map(r -> new Parser.Input(getPath(r), () -> {
                    eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(r)));
                    return getInputStream(r);
                }))
                .collect(Collectors.toList());

        List<org.openrewrite.text.PlainText> textfiles = new PlainTextParser().parseInputs(
                otherFiles,
                projectDirectory,
                ctx
        );
        sourceFiles.addAll(ListUtils.map(textfiles, addProvenance(projectProvenance, sourceSet)));

        eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());
    }

    private <S extends SourceFile> S addProjectProvenance(S s, List<Marker> projectProvenance) {
        for (Marker marker : projectProvenance) {
            s = s.withMarkers(s.getMarkers().addIfAbsent(marker));
        }
        return s;
    }

    private <S extends SourceFile> UnaryOperator<S> addProvenance(List<Marker> projectProvenance, JavaSourceSet sourceSet) {
        return s -> {
            s = addProjectProvenance(s, projectProvenance);
            s = s.withMarkers(s.getMarkers().addIfAbsent(sourceSet));
            return s;
        };
    }

    private List<Path> downloadArtifacts(Set<Pom.Dependency> dependencies) {

        eventPublisher.publishEvent(new StartDownloadingDependenciesEvent(dependencies.size()));

        List<Path> paths = dependencies.stream()
                .filter(d -> d.getRepository() != null)
                .peek(d -> eventPublisher.publishEvent(new StartDownloadingDependencyEvent(d)))
                .map(artifactDownloader::downloadArtifact)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        eventPublisher.publishEvent(new FinishedDownloadingDependencies());

        return paths;
    }

    public static List<Maven> sort(List<Maven> mavens) {
        // the value is the set of maven projects that depend on the key
        Map<Maven, Set<Maven>> byDependedOn = new HashMap<>();

        for (Maven maven : mavens) {
            byDependedOn.computeIfAbsent(maven, m -> new HashSet<>());
            for (Pom.Dependency dependency : maven.getModel().getDependencies()) {
                for (Maven test : mavens) {
                    if (test.getModel().getGroupId().equals(dependency.getGroupId()) &&
                            test.getModel().getArtifactId().equals(dependency.getArtifactId())) {
                        byDependedOn.computeIfAbsent(maven, m -> new HashSet<>()).add(test);
                    }
                }
            }
        }

        List<Maven> sorted = new ArrayList<>(mavens.size());
        next:
        while (!byDependedOn.isEmpty()) {
            for (Map.Entry<Maven, Set<Maven>> mavenAndDependencies : byDependedOn.entrySet()) {
                if (mavenAndDependencies.getValue().isEmpty()) {
                    Maven maven = mavenAndDependencies.getKey();
                    byDependedOn.remove(maven);
                    sorted.add(maven);
                    for (Set<Maven> dependencies : byDependedOn.values()) {
                        dependencies.remove(maven);
                    }
                    continue next;
                }
            }
        }

        return sorted;
    }
}
