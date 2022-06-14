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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.GitProvenance;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.ci.BuildEnvironment;
import org.openrewrite.maven.tree.*;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.xml.tree.Xml;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.sbm.build.impl.MavenBuildFileUtil;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.engine.events.*;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Parse a Maven project on disk into a list of {@link org.openrewrite.SourceFile} including
 * Maven, Java, YAML, properties, and XML AST representations of sources and resources found.
 */
@Slf4j
@Component
@RequiredArgsConstructor
// FIXME: #7 rename to ProjectParser
public class MavenProjectParser {

    private final ResourceParser resourceParser;
    private final RewriteMavenParser mavenParser;
    private final MavenArtifactDownloader artifactDownloader;
//    private final JavaParser.Builder<?, ?> javaParserBuilder;
    private final ApplicationEventPublisher eventPublisher;
    private final JavaProvenanceMarkerFactory javaProvenanceMarkerFactory;

    private final JavaParser javaParser;
//    public MavenProjectParser(ResourceParser resourceParser,
//                              MavenArtifactDownloader artifactDownloader,
//                              MavenParser.Builder mavenParserBuilder,
//                              JavaParser.Builder<?, ?> javaParserBuilder,
//                              ApplicationEventPublisher eventPublisher, JavaProvenanceMarkerFactory javaProvenanceMarkerFactory, ExecutionContext ctx) {
//        this.resourceParser = resourceParser;
//        this.mavenParser = mavenParserBuilder.build();
//        this.artifactDownloader = artifactDownloader;
////        this.javaParserBuilder = javaParserBuilder;
//        this.eventPublisher = eventPublisher;
//        this.javaProvenanceMarkerFactory = javaProvenanceMarkerFactory;
//    }

    public List<SourceFile> parse(Path projectDirectory, List<Resource> resources) {
        ExecutionContext ctx = new RewriteExecutionContext();
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

        if(log.isDebugEnabled()) {
            for (Xml.Document maven : mavens) {
                MavenResolutionResult mavenResolution = MavenBuildFileUtil.getMavenResolution(maven);
                log.debug("  {}:{}", mavenResolution.getPom().getGroupId(), mavenResolution.getPom().getArtifactId());
            }
        }

        List<SourceFile> sourceFiles = new ArrayList<>();
        for (Xml.Document pomXml : mavens) {
            // Create markers for pom
            List<Marker> javaProvenanceMarkers = javaProvenanceMarkerFactory.createJavaProvenanceMarkers(pomXml, projectDirectory, ctx);
            // Add markers to pom
            Xml.Document mavenWithMarkers = addMarkers(pomXml, javaProvenanceMarkers);
            // Add pom to sources
            sourceFiles.add(mavenWithMarkers);

            // download pom dependencies, provided scope contains compile scope
            Path relativeModuleDir = mavenWithMarkers.getSourcePath().getParent();
            Path mavenProjectDirectory = projectDirectory;
            if(relativeModuleDir != null) {
                mavenProjectDirectory = projectDirectory.resolve(relativeModuleDir);
            }

            // --------
            // Main Java sources
            List<J.CompilationUnit> mainJavaSources = parseMainJavaSources(projectDirectory, resources, ctx, javaParser, pomXml, mavenWithMarkers, mavenProjectDirectory, javaProvenanceMarkers);
            JavaSourceSet mainSourceSet = javaParser.getSourceSet(ctx);
            sourceFiles.addAll(mainJavaSources);
            // FIxME: cus already have sourceSetMarker, only provenance need to be added

            // FIXME: ALL JavaParser should share the same TypeCache

            //UnaryOperator<J.CompilationUnit> unaryOperator = addMarkers(mainSourceSet, javaProvenanceMarkers);
            //sourceFiles.addAll(ListUtils.map(mainCompilationUnits, unaryOperator));

            // --------
            // Main resources
            Set<Path> mainResourcePaths = Set.of(
                    Path.of("src/main/resources"),
                    Path.of("src/main/webapp"),
                    Path.of("src/main/mule")
            );

            // FIXME: mainSourceSetMarker and provenance marker needs to be a dde to all resources

            List<SourceFile> mainResources = resourceParser.parse(projectDirectory, mainResourcePaths, resources);
            sourceFiles.addAll(mainResources);

            // -------
            // Test Java sources
            List<J.CompilationUnit> testJavaSources = parseTestJavaSources(projectDirectory, resources, ctx, javaParser, pomXml, mavenWithMarkers, mavenProjectDirectory, javaProvenanceMarkers);
            JavaSourceSet testSourceSet = javaParser.getSourceSet(ctx);
            sourceFiles.addAll(testJavaSources);

            // --------
            // Test resources
            Set<Path> testResourcePaths = Set.of(
                    Path.of("src/test/resources"),
                    Path.of("src/test/webapp"),
                    Path.of("src/test/mule")
            );

            // FIXME: mainSourceSetMarker and provenance marker needs to be a dde to all resources

            List<SourceFile> testResources = resourceParser.parse(projectDirectory, testResourcePaths, resources);
            sourceFiles.addAll(testResources);

//
//            List<Marker> mainMarkers = new ArrayList<>(javaProvenanceMarkers);
//            mainMarkers.add(javaParser.getSourceSet(ctx));
//            sourceFiles.addAll(ListUtils.map(
//                    resourceParser.parse(
//                            projectDirectory,
//                            mainResourceFolder,
//                            ctx
//                    ),
//                    addProvenance(mainMarkers)
//            ));


            /*

            // --------
            // Test Java sources
            List<Resource> testJavaSources1 = getTestJavaSources(mavenProjectDirectory, resources, mavenResolution);

            JavaTypeCache typeCache = new JavaTypeCache();
            JavaSourceSet testJavaSourceSet = JavaSourceSet.build("test", dependencies,  typeCache, true);

            List<Parser.Input> testSourcesParserInput = testJavaSources1.stream().map(js -> new Parser.Input(getPath(js), () -> {
                eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(js)));
                InputStream content = getInputStream(js);
                return content;
            })).collect(Collectors.toList());

            List<J.CompilationUnit> testCompilationUnits = javaParser.parseInputs(testSourcesParserInput, mavenProjectDirectory, ctx);
            UnaryOperator<J.CompilationUnit> sourceFileUnaryOperator = addMarkers(testJavaSourceSet, javaProvenanceMarkers);
            sourceFiles.addAll(ListUtils.map(testCompilationUnits, sourceFileUnaryOperator));

            // -------

            JavaTypeCache typeCache = new JavaTypeCache();
            JavaSourceSet mainProvenance = JavaSourceSet.build("main", dependencies,  typeCache, true);

            List<Resource> javaSources = getJavaSources(projectDirectory, resources, pomXml);

            List<Parser.Input> javaSourcesInput = javaSources.stream().map(js -> new Parser.Input(getPath(js), () -> {
                eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(js)));
                InputStream content = getInputStream(js);
                return content;
            })).collect(Collectors.toList());

            eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Java [main]: '" + mavenResolution.getPom().getArtifactId() + "'", javaSourcesInput.size()));
            List<J.CompilationUnit> compilationUnits = javaParser.parseInputs(javaSourcesInput, projectDirectory, ctx);
            eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());

            javaParser.parse(pomXml.getJavaSources(projectDirectory, ctx) javaSources, projectDirectory, ctx)
            sourceFiles.addAll(ListUtils.map(compilationUnits, addProvenance(javaProvenanceMarkers, mainProvenance)));

            List<Path> testDependencies = downloadArtifacts(mavenResolution.getDependencies().get(Scope.Test));
            JavaSourceSet testProvenance = JavaSourceSet.build("test", testDependencies, typeCache, true);
            javaParser.setClasspath(testDependencies);

            List<Resource> testJavaSources = getTestJavaSources(projectDirectory, resources, pomXml);
            List<Parser.Input> testJavaSourcesInput = testJavaSources.stream().map(js -> new Parser.Input(getPath(js), () -> {
                eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(js)));
                return getInputStream(js);
            })).collect(Collectors.toList());

            eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Java [test]: '" + mavenResolution.getPom().getArtifactId() + "'", testJavaSourcesInput.size()));
            List<J.CompilationUnit> testCompilationUnits = javaParser.parseInputs(testJavaSourcesInput, projectDirectory, ctx);
            eventPublisher.publishEvent(new FinishedScanningProjectResourceSetEvent());

            sourceFiles.addAll(ListUtils.map(testCompilationUnits, addProvenance(javaProvenanceMarkers, testProvenance)));

            parseResources(getWebappResources(projectDirectory, resources, pomXml), projectDirectory, sourceFiles, javaProvenanceMarkers, mainProvenance);
            parseResources(getMulesoftResources(projectDirectory, resources, pomXml), projectDirectory, sourceFiles, javaProvenanceMarkers, mainProvenance);
            parseResources(getResources(projectDirectory, resources, pomXml), projectDirectory, sourceFiles, javaProvenanceMarkers, mainProvenance);
            parseResources(getTestResources(projectDirectory, resources, pomXml), projectDirectory, sourceFiles, javaProvenanceMarkers, testProvenance);

             */
        }

        return ListUtils.map(sourceFiles, s -> s.withMarkers(s.getMarkers().addIfAbsent(gitProvenance)));
    }

    private List<J.CompilationUnit> parseTestJavaSources(Path projectDirectory, List<Resource> resources, ExecutionContext ctx, JavaParser javaParser, Xml.Document pomXml, Xml.Document mavenWithMarkers, Path mavenProjectDirectory, List<Marker> javaProvenanceMarkers) {
        MavenResolutionResult mavenResolution = MavenBuildFileUtil.getMavenResolution(mavenWithMarkers);
        List<ResolvedDependency> resolvedDependencies = mavenResolution.getDependencies().get(Scope.Test);
        List<Path> dependencies = downloadArtifacts(resolvedDependencies);
        javaParser.setClasspath(dependencies);

        // --------
        // Main Java sources
        javaParser.setSourceSet("test");
        List<Resource> testJavaSources = getTestJavaSources(projectDirectory, resources, pomXml);
        List<Parser.Input> testJavaSourcesInput = testJavaSources.stream().map(js -> {
            Path jsPath = getPath(js);
            return new Parser.Input(jsPath, () -> {
                eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(jsPath));
                InputStream content = getInputStream(js);
                return content;
            });
        }).collect(Collectors.toList());
        List<J.CompilationUnit> testCompilationUnits = javaParser.parseInputs(testJavaSourcesInput, projectDirectory, ctx);
        // FIXME: #7 JavaParser and adding markers is required when adding java sources and should go into dedicated component
        testCompilationUnits.stream()
                .forEach(cu -> cu.getMarkers().getMarkers().addAll(javaProvenanceMarkers));
        return testCompilationUnits;
    }

    private List<J.CompilationUnit> parseMainJavaSources(Path projectDirectory, List<Resource> resources, ExecutionContext ctx, JavaParser javaParser, Xml.Document pomXml, Xml.Document mavenWithMarkers, Path mavenProjectDirectory, List<Marker> javaProvenanceMarkers) {
        MavenResolutionResult mavenResolution = MavenBuildFileUtil.getMavenResolution(mavenWithMarkers);
        List<ResolvedDependency> resolvedDependencies = mavenResolution.getDependencies().get(Scope.Provided);
        List<Path> dependencies = downloadArtifacts(resolvedDependencies);
        javaParser.setClasspath(dependencies);

        // --------
        // Main Java sources
        javaParser.setSourceSet("main");
        List<Resource> mainJavaSources = getJavaSources(projectDirectory, resources, pomXml);
        List<Parser.Input> mainJavaSourcesInput = mainJavaSources.stream().map(js -> {
            Path jsPath = getPath(js);
            return new Parser.Input(jsPath, () -> {
                eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(jsPath));
                InputStream content = getInputStream(js);
                return content;
            });
        }).collect(Collectors.toList());
        List<J.CompilationUnit> mainCompilationUnits = javaParser.parseInputs(mainJavaSourcesInput, projectDirectory, ctx);
        // FIXME: #7 JavaParser and adding markers is required when adding java sources and should go into dedicated component
        mainCompilationUnits.stream()
                .forEach(cu -> cu.getMarkers().getMarkers().addAll(javaProvenanceMarkers));
        return mainCompilationUnits;
    }

/*
    void createProjectProvenance(Path baseDir) {
        String javaRuntimeVersion = System.getProperty("java.runtime.version");
        String javaVendor = System.getProperty("java.vm.vendor");
        String sourceCompatibility = javaRuntimeVersion;
        String targetCompatibility = javaRuntimeVersion;

        String propertiesSourceCompatibility = (String) mavenProject.getProperties().get("maven.compiler.source");
        if (propertiesSourceCompatibility != null) {
            sourceCompatibility = propertiesSourceCompatibility;
        }
        String propertiesTargetCompatibility = (String) mavenProject.getProperties().get("maven.compiler.target");
        if (propertiesTargetCompatibility != null) {
            targetCompatibility = propertiesTargetCompatibility;
        }

        BuildEnvironment buildEnvironment = BuildEnvironment.build(System::getenv);
        return Stream.of(
                        buildEnvironment,
                        gitProvenance(baseDir, buildEnvironment),
                        new BuildTool(randomId(), BuildTool.Type.Maven, runtime.getMavenVersion()),
                        new JavaVersion(randomId(), javaRuntimeVersion, javaVendor, sourceCompatibility, targetCompatibility),
                        new JavaProject(randomId(), mavenProject.getName(), new JavaProject.Publication(
                                mavenProject.getGroupId(),
                                mavenProject.getArtifactId(),
                                mavenProject.getVersion()
                        )))
                .filter(Objects::nonNull)
                .collect(toList());
    }

 */

    @Nullable
    private GitProvenance gitProvenance(Path baseDir, @Nullable BuildEnvironment buildEnvironment) {
        try {
            return GitProvenance.fromProjectDirectory(baseDir, buildEnvironment);
        } catch (Exception e) {
            // Logging at a low level as this is unlikely to happen except in non-git projects, where it is expected
            log.debug("Unable to determine git provenance", e);
        }
        return null;
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
                .filter(r -> getPath(r).startsWith(inPath) /* && Stream.of(".properties", ".xml", ".yml", ".yaml").anyMatch(fe -> r.getPath().toString().endsWith(fe)))
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
/*
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

 */

    @Deprecated
    private <S extends SourceFile> UnaryOperator<S> addMarkers(JavaSourceSet sourceSet, List<Marker> projectProvenance) {
        return s -> {
            s = addMarkers(s, projectProvenance);
            s = s.withMarkers(s.getMarkers().addIfAbsent(sourceSet));
            return s;
        };
    }

    private <S extends SourceFile> S addMarkers(S s, List<Marker> markers) {
        for (Marker marker : markers) {
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

    // TODO: #7 move into central place as downloading artifacts will also be required when dependencies are added to build file
    private List<Path> downloadArtifacts(List<ResolvedDependency> dependencies) {

        eventPublisher.publishEvent(new StartDownloadingDependenciesEvent(dependencies.size()));


        List<Path> paths = dependencies.stream()
                .filter(d -> d.getRepository() != null)
                .peek(d -> eventPublisher.publishEvent(new StartDownloadingDependencyEvent(d.getRequested())))
//                .parallel()
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

    private static Path getPath(Resource r) {
        try {
            return r.getFile().toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private InputStream getInputStream(Resource r) {
        try {
            return r.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
