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

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.internal.JavaTypeCache;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.BuildTool;
import org.openrewrite.marker.GitProvenance;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.ci.BuildEnvironment;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.tree.*;
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
import org.springframework.sbm.build.impl.MavenBuildFileUtil;
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
    private static final Logger logger = LoggerFactory.getLogger(MavenProjectParser.class);

    private final MavenParser mavenParser;
    private final MavenArtifactDownloader artifactDownloader;
    private final JavaParser.Builder<?, ?> javaParserBuilder;
    private final ApplicationEventPublisher eventPublisher;
    private final JavaProvenanceMarkerFactory javaProvenanceMarkerFactory;
    private final ExecutionContext ctx;

    public MavenProjectParser(MavenArtifactDownloader artifactDownloader,
                              MavenParser.Builder mavenParserBuilder,
                              JavaParser.Builder<?, ?> javaParserBuilder,
                              ApplicationEventPublisher eventPublisher, JavaProvenanceMarkerFactory javaProvenanceMarkerFactory, ExecutionContext ctx) {
        this.mavenParser = mavenParserBuilder.build();
        this.artifactDownloader = artifactDownloader;
        this.javaParserBuilder = javaParserBuilder;
        this.eventPublisher = eventPublisher;
        this.javaProvenanceMarkerFactory = javaProvenanceMarkerFactory;
        this.ctx = ctx;
    }

    public List<SourceFile> parse(Path projectDirectory, List<Resource> resources) {
        @Nullable BuildEnvironment buildEnvironment = null;
        GitProvenance gitProvenance = GitProvenance.fromProjectDirectory(projectDirectory, buildEnvironment);

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

        eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Maven", inputs.size()));
        List<Xml.Document> mavens = mavenParser.parseInputs(inputs, projectDirectory, ctx);
        eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());

        mavens = sort(mavens);

        JavaParser javaParser = javaParserBuilder
                .build();

        for (Xml.Document maven : mavens) {
            MavenResolutionResult mavenResolution = MavenBuildFileUtil.getMavenResolution(maven);
            logger.info("  {}:{}", mavenResolution.getPom().getGroupId(), mavenResolution.getPom().getArtifactId());
        }

        List<SourceFile> sourceFiles = new ArrayList<>();
        for (Xml.Document maven : mavens) {
            List<Marker> javaProvenanceMarkers = javaProvenanceMarkerFactory.getJavaProvenanceMarkers(maven, projectDirectory, ctx);

            sourceFiles.add(addProjectProvenance(maven, javaProvenanceMarkers));
            MavenResolutionResult mavenResolution = MavenBuildFileUtil.getMavenResolution(maven);
            List<ResolvedDependency> resolvedDependencies = mavenResolution.getDependencies().get(Scope.Provided);
            List<Path> dependencies = downloadArtifacts(resolvedDependencies);

            javaParser.setSourceSet("main");
            javaParser.setClasspath(dependencies);

            Path mavenProjectDirectory = normalizeSourcePath(projectDirectory, maven.getSourcePath()).getParent();
/*
            List<Resource> testJavaSources1 = getTestJavaSources(mavenProjectDirectory, mavenResolution);
            sourceFiles.addAll(ListUtils.map(javaParser.parse(testJavaSources1, projectDirectory, ctx),
                    addProvenance(javaProvenanceMarkers)));

            JavaTypeCache typeCache = new JavaTypeCache();
            JavaSourceSet mainProvenance = JavaSourceSet.build("main", dependencies,  typeCache, true);

            List<Resource> javaSources = getJavaSources(projectDirectory, resources, maven);

            List<Parser.Input> javaSourcesInput = javaSources.stream().map(js -> new Parser.Input(getPath(js), () -> {
                eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(js)));
                InputStream content = getInputStream(js);
                return content;
            })).collect(Collectors.toList());

            eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Java [main]: '" + mavenResolution.getPom().getArtifactId() + "'", javaSourcesInput.size()));
            List<J.CompilationUnit> compilationUnits = javaParser.parseInputs(javaSourcesInput, projectDirectory, ctx);
            eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());

            javaParser.parse(maven.getJavaSources(projectDirectory, ctx) javaSources, projectDirectory, ctx)
            sourceFiles.addAll(ListUtils.map(compilationUnits, addProvenance(javaProvenanceMarkers, mainProvenance)));

            List<Path> testDependencies = downloadArtifacts(mavenResolution.getDependencies().get(Scope.Test));
            JavaSourceSet testProvenance = JavaSourceSet.build("test", testDependencies, typeCache, true);
            javaParser.setClasspath(testDependencies);

            List<Resource> testJavaSources = getTestJavaSources(projectDirectory, resources, maven);
            List<Parser.Input> testJavaSourcesInput = testJavaSources.stream().map(js -> new Parser.Input(getPath(js), () -> {
                eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(js)));
                return getInputStream(js);
            })).collect(Collectors.toList());

            eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Java [test]: '" + mavenResolution.getPom().getArtifactId() + "'", testJavaSourcesInput.size()));
            List<J.CompilationUnit> testCompilationUnits = javaParser.parseInputs(testJavaSourcesInput, projectDirectory, ctx);
            eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());

            sourceFiles.addAll(ListUtils.map(testCompilationUnits, addProvenance(javaProvenanceMarkers, testProvenance)));

            parseResources(getWebappResources(projectDirectory, resources, maven), projectDirectory, sourceFiles, javaProvenanceMarkers, mainProvenance);
            parseResources(getMulesoftResources(projectDirectory, resources, maven), projectDirectory, sourceFiles, javaProvenanceMarkers, mainProvenance);
            parseResources(getResources(projectDirectory, resources, maven), projectDirectory, sourceFiles, javaProvenanceMarkers, mainProvenance);
            parseResources(getTestResources(projectDirectory, resources, maven), projectDirectory, sourceFiles, javaProvenanceMarkers, testProvenance);
            */
        }

        return ListUtils.map(sourceFiles, s -> s.withMarkers(s.getMarkers().addIfAbsent(gitProvenance)));
    }

    private Path normalizeSourcePath(Path projectDirectory, Path sourcePath) {
        return null;
    }

    private List<Resource> getWebappResources(Path projectDir, List<Resource> resources, Xml.Document maven) {
        MavenResolutionResult mavenResolution = MavenBuildFileUtil.findMavenResolution(maven).get();
        if (!"jar".equals(mavenResolution.getPom().getPackaging()) && !"bundle".equals(mavenResolution.getPom().getPackaging())) {
            return emptyList();
        }
        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "webapp"));
        return resources.stream()
                .filter(r -> getPath(r).startsWith(inPath) /* && Stream.of(".properties", ".xml", ".yml", ".yaml").anyMatch(fe -> r.getPath().toString().endsWith(fe))*/)
                .collect(Collectors.toList());
    }

    private List<Resource> getMulesoftResources(Path projectDir, List<Resource> resources, Xml.Document maven) {
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


    public static List<Resource> filterMavenPoms(List<Resource> resources) {
        return resources.stream()
                .filter(p -> getPath(p).getFileName().toString().equals("pom.xml") &&
                        !p.toString().contains("/src/"))
                .collect(Collectors.toList());
    }

    public List<Resource> getJavaSources(Path projectDir, List<Resource> resources, Xml.Document maven) {
//        if (!"jar".equals(maven.getMavenModel().getPom().getPackaging()) && !"bundle".equals(maven.getMavenModel().getPom().getPackaging())) {
//            return emptyList();
//        }
        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "java"));
        return resources.stream()
                .filter(r -> getPath(r).startsWith(inPath) && getPath(r).toString().endsWith(".java"))
                .collect(Collectors.toList());
    }

    public List<Resource> getTestJavaSources(Path projectDir, List<Resource> resources, Xml.Document maven) {
//        if (!"jar".equals(maven.getMavenModel().getPom().getPackaging()) && !"bundle".equals(maven.getMavenModel().getPom().getPackaging())) {
//            return emptyList();
//        }
        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "test", "java"));
        return resources.stream()
                .filter(r -> getPath(r).startsWith(inPath) && getPath(r).toString().endsWith(".java"))
                .collect(Collectors.toList());
    }


    public List<Resource> getResources(Path projectDir, List<Resource> resources, Xml.Document maven) {
//        if (!"jar".equals(maven.getMavenModel().getPom().getPackaging()) && !"bundle".equals(maven.getMavenModel().getPom().getPackaging())) {
//            return emptyList();
//        }
        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "resources"));
        return resources.stream()
                .filter(r -> getPath(r).startsWith(inPath) /* && Stream.of(".properties", ".xml", ".yml", ".yaml").anyMatch(fe -> r.getPath().toString().endsWith(fe))*/)
                .collect(Collectors.toList());
    }

    public List<Resource> getTestResources(Path projectDir, List<Resource> resources, Xml.Document maven) {
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

    @Deprecated
    private <S extends SourceFile> UnaryOperator<S> addProvenance(List<Marker> projectProvenance, JavaSourceSet sourceSet) {
        return s -> {
            s = addProjectProvenance(s, projectProvenance);
            s = s.withMarkers(s.getMarkers().addIfAbsent(sourceSet));
            return s;
        };
    }

    private <S extends SourceFile> S addProjectProvenance(S s, List<Marker> projectProvenance) {
        for (Marker marker : projectProvenance) {
            s = s.withMarkers(s.getMarkers().addIfAbsent(marker));
        }
        return s;
    }

    /*
    private <S extends SourceFile> UnaryOperator<S> addProvenance(List<Marker> projectProvenance) {
        return s -> {
            s = addProjectProvenance(s, projectProvenance);
            s = s.withMarkers(s.getMarkers().addIfAbsent(sourceSet));
            return s;
        };
    }
     */

    private List<Path> downloadArtifacts(List<ResolvedDependency> dependencies) {

        eventPublisher.publishEvent(new StartDownloadingDependenciesEvent(dependencies.size()));

        List<Path> paths = dependencies.stream()
                .filter(d -> d.getRepository() != null)
                .peek(d -> eventPublisher.publishEvent(new StartDownloadingDependencyEvent(d.getRequested())))
                .map(artifactDownloader::downloadArtifact)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        eventPublisher.publishEvent(new FinishedDownloadingDependencies());

        return paths;
    }

    public static List<Xml.Document> sort(List<Xml.Document> mavens) {
        // the value is the set of maven projects that depend on the key
        Map<Xml.Document, Set<Xml.Document>> byDependedOn = new HashMap<>();

        for (Xml.Document maven : mavens) {
            MavenResolutionResult mavenResolution = MavenBuildFileUtil.findMavenResolution(maven).get();
            byDependedOn.computeIfAbsent(maven, m -> new HashSet<>());

            Set<Dependency> dependencies = mavenResolution.getDependencies().values().stream()
                    .flatMap(d -> d.stream())
                    .map(d -> d.getRequested())
                    .collect(Collectors.toSet());

            for (Dependency dependency : dependencies) {
                for (Xml.Document test : mavens) {
                    MavenResolutionResult testMavenResolution = MavenBuildFileUtil.findMavenResolution(test).get();
                    if (testMavenResolution.getPom().getGroupId().equals(dependency.getGroupId()) &&
                            testMavenResolution.getPom().getArtifactId().equals(dependency.getArtifactId())) {
                        byDependedOn.computeIfAbsent(maven, m -> new HashSet<>()).add(test);
                    }
                }
            }
        }

        List<Xml.Document> sorted = new ArrayList<>(mavens.size());
        next:
        while (!byDependedOn.isEmpty()) {
            for (Map.Entry<Xml.Document, Set<Xml.Document>> mavenAndDependencies : byDependedOn.entrySet()) {
                if (mavenAndDependencies.getValue().isEmpty()) {
                    Xml.Document maven = mavenAndDependencies.getKey();
                    byDependedOn.remove(maven);
                    sorted.add(maven);
                    for (Set<Xml.Document> dependencies : byDependedOn.values()) {
                        dependencies.remove(maven);
                    }
                    continue next;
                }
            }
        }

        return sorted;
    }


    private InputStream getInputStream(Resource r) {
        try {
            return r.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: duplicate of PathScanner.getPath(), move to helper
    private static Path getPath(Resource r) {
        try {
            return r.getFile().toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
