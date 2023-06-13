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
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.maven.tree.*;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.xml.tree.Xml;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.sbm.build.impl.MavenBuildFileUtil;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.engine.events.*;
import org.springframework.sbm.scopes.ProjectMetadata;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Parse a Maven project on disk into a list of {@link org.openrewrite.SourceFile} including
 * Maven, Java, YAML, properties, and XML AST representations of sources and resources found.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MavenProjectParser {

    private final ResourceParser resourceParser;
    private final RewriteMavenParser mavenParser;
    private final MavenArtifactDownloader artifactDownloader;
    private final ApplicationEventPublisher eventPublisher;
    private final JavaProvenanceMarkerFactory javaProvenanceMarkerFactory;
    private final JavaParser javaParser;
    private final MavenConfigHandler mavenConfigHandler;
    private final ProjectMetadata projectMetadata;
    private final ExecutionContext executionContext;

    public List<SourceFile> parse(Path projectDirectory, List<Resource> resources) {
        projectMetadata.setMetadata("some metadata");
        MavenSettings mavenSettings = new MavenSettings(null, null, null, null, null);
        projectMetadata.setMavenSettings(mavenSettings);
        MavenExecutionContextView mavenExecutionContext = MavenExecutionContextView.view(executionContext);
        mavenExecutionContext.setMavenSettings(mavenSettings);


        mavenConfigHandler.injectMavenConfigIntoSystemProperties(resources);

        @Nullable BuildEnvironment buildEnvironment = null;
        GitProvenance gitProvenance = GitProvenance.fromProjectDirectory(projectDirectory, buildEnvironment);

        List<Resource> filteredMavenPoms = filterMavenPoms(resources);
        List<Parser.Input> inputs = filteredMavenPoms.stream().map(r -> new Parser.Input(getPath(r), () -> {
            eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(getPath(r)));
            InputStream is = getInputStream(r);
            return is;
        })).collect(Collectors.toList());

        eventPublisher.publishEvent(new StartedScanningProjectResourceSetEvent("Maven", inputs.size()));

        List<Xml.Document> mavens = mavenParser.parseInputs(inputs, projectDirectory, executionContext);
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
            List<Marker> javaProvenanceMarkers = javaProvenanceMarkerFactory.createJavaProvenanceMarkers(pomXml,
                                                                                                         projectDirectory,
                                                                                                         executionContext);
            // Add markers to pom
            Xml.Document mavenWithMarkers = addMarkers(pomXml, javaProvenanceMarkers);
            // Add pom to sources
            sourceFiles.add(mavenWithMarkers);

            // download pom dependencies, provided scope contains compile scope
            Path relativeModuleDir = mavenWithMarkers.getSourcePath().getParent();
            Path mavenProjectDirectory = projectDirectory;
            if (relativeModuleDir != null) {
                mavenProjectDirectory = projectDirectory.resolve(relativeModuleDir);
            }

            // --------
            // Main Java sources
            List<J.CompilationUnit> mainJavaSources = parseMainJavaSources(projectDirectory, resources,
                                                                           executionContext, javaParser,
                                                                           pomXml, mavenWithMarkers,
                                                                           mavenProjectDirectory,
                                                                           javaProvenanceMarkers);
            JavaSourceSet mainSourceSet = javaParser.getSourceSet(executionContext);
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

            // FIXME: mainSourceSetMarker and provenance marker must be added to all resources
            List<Resource> resourceList = resourceParser.filter(projectDirectory, mainResourcePaths, resources, relativeModuleDir);

            List<Marker> resourceMarker = new ArrayList(javaProvenanceMarkers);
            resourceMarker.add(mainSourceSet);
            if(gitProvenance != null) {
                resourceMarker.add(gitProvenance);
            }
            List<SourceFile> mainResources = resourceParser.parse(projectDirectory, resourceList, resourceMarker);
            sourceFiles.addAll(mainResources);

            // -------
            // Test Java sources
            ArrayList<Marker> markers = new ArrayList<>(javaProvenanceMarkers);
            markers.add(mainSourceSet);
            List<J.CompilationUnit> testJavaSources = parseTestJavaSources(projectDirectory, resources,
                                                                           executionContext, javaParser, pomXml, mavenWithMarkers, mavenProjectDirectory, markers);
            JavaSourceSet testSourceSet = javaParser.getSourceSet(executionContext);
            sourceFiles.addAll(testJavaSources);

            // --------
            // Test resources
            Set<Path> testResourcePaths = Set.of(
                    Path.of("src/test/resources"),
                    Path.of("src/test/webapp"),
                    Path.of("src/test/mule")
            );

            List<Resource> filteredResources = resourceParser.filter(projectDirectory, testResourcePaths, resources, relativeModuleDir);
            List<Marker> testResourceMarker = new ArrayList(javaProvenanceMarkers);
            testResourceMarker.add(testSourceSet);
            if(gitProvenance != null) {
                testResourceMarker.add(gitProvenance);
            }
            List<SourceFile> testResources = resourceParser.parse(projectDirectory, filteredResources, testResourceMarker);
            sourceFiles.addAll(testResources);
        }
        if(gitProvenance != null) {
            sourceFiles = ListUtils.map(sourceFiles, s -> s.withMarkers(s.getMarkers().addIfAbsent(gitProvenance)));
        }
        return sourceFiles;
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
        List<J.CompilationUnit> testCompilationUnits = javaParser.parseInputs(testJavaSourcesInput, projectDirectory,
                                                                              ctx);
        // FIXME: #7 JavaParser and adding markers is required when adding java sources and should go into dedicated component
        testCompilationUnits.forEach(cu -> cu.getMarkers().getMarkers().addAll(javaProvenanceMarkers));
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
        mainCompilationUnits.stream().forEach(cu -> cu.getMarkers().getMarkers().addAll(javaProvenanceMarkers));
        return mainCompilationUnits;
    }


    public static List<Resource> filterMavenPoms(List<Resource> resources) {
        return resources
                .stream()
                .filter(p -> getPath(p).getFileName().toString().equals("pom.xml") && !p.toString().contains("/src/"))
                .collect(Collectors.toList());
    }

    public List<Resource> getJavaSources(Path projectDir, List<Resource> resources, Xml.Document maven) {

        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "main", "java"));
        return resources
                .stream()
                .filter(r -> getPath(r).startsWith(inPath) && getPath(r).toString().endsWith(".java"))
                .collect(Collectors.toList());
    }

    public List<Resource> getTestJavaSources(Path projectDir, List<Resource> resources, Xml.Document maven) {
        Path inPath = projectDir.resolve(maven.getSourcePath()).getParent().resolve(Paths.get("src", "test", "java"));
        return resources
                .stream()
                .filter(r -> getPath(r).startsWith(inPath) && getPath(r).toString().endsWith(".java"))
                .collect(Collectors.toList());
    }

    private <S extends SourceFile> S addMarkers(S s, List<Marker> markers) {
        for (Marker marker : markers) {
            s = s.withMarkers(s.getMarkers().addIfAbsent(marker));
        }
        return s;
    }

    // TODO: #7 move into central place as downloading artifacts will also be required when dependencies are added to build file
    private List<Path> downloadArtifacts(List<ResolvedDependency> dependencies) {

        eventPublisher.publishEvent(new StartDownloadingDependenciesEvent(dependencies.size()));


        List<Path> paths = dependencies
                .stream()
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

            Set<Dependency> dependencies = mavenResolution
                    .getDependencies()
                    .values()
                    .stream()
                    .flatMap(d -> d.stream())
                    .map(d -> d.getRequested())
                    .collect(Collectors.toSet());

            for (Dependency dependency : dependencies) {
                for (Xml.Document test : mavens) {
                    MavenResolutionResult testMavenResolution = MavenBuildFileUtil.findMavenResolution(test).get();
                    if (testMavenResolution.getPom().getGroupId().equals(dependency.getGroupId()) && testMavenResolution
                            .getPom()
                            .getArtifactId()
                            .equals(dependency.getArtifactId())) {
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
        sorted.sort((d, e) -> d.getSourcePath().toString().compareTo(e.getSourcePath().toString()));
        if(log.isDebugEnabled()) {
            String collect = sorted.stream().map(Xml.Document::getSourcePath).map(Object::toString).collect(Collectors.joining(", "));
            log.debug("Sorted Maven files: \"%s\"".formatted(collect));
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
