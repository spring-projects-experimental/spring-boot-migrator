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
package org.springframework.sbm.parsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.Maven;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.execution.*;
import org.apache.maven.graph.DefaultProjectDependencyGraph;
import org.apache.maven.model.Profile;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.*;
import org.apache.maven.repository.UserLocalArtifactRepository;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.codehaus.plexus.*;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.Tree;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.java.tree.JavaSourceFile;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Parses a given {@link Path} to a Open Rewrite's AST representation {@code List<}{@link SourceFile}{@code >}.
 *
 * @author Fabian Krüger
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RewriteMavenProjectParser {


    public static final Collection<String> EXCLUSIONS = Set.of("**/.DS_Store", ".DS_Store");
    private final MavenPlexusContainerFactory mavenPlexusContainerFactory;
    private final MavenExecutionRequestFactory mavenExecutionRequestFactory;
    private final ParsingEventListener parsingListener;

    /**
     * Parses a list of {@link Resource}s in given {@code baseDir} to OpenRewrite AST.
     * It uses default settings for configuration.
     * Use {@link #parse(Path, boolean, String, boolean, Collection, Collection, int, boolean, ExecutionContext)}
     * if you need to pass in different settings
     */
    public RewriteProjectParsingResult parse(Path baseDir) {
        ExecutionContext executionContext = new InMemoryExecutionContext(t -> t.printStackTrace());
        ParsingExecutionContextView.view(executionContext).setParsingListener(parsingListener);
        return parse(baseDir, executionContext);
    }

    public RewriteProjectParsingResult parse(Path baseDir, ExecutionContext executionContext) {
        boolean pomCacheEnabled = true;
        String pomCacheDirectory = "pom-cache";
        boolean skipMavenParsing = false;
        Collection<String> plainTextMasks = Set.of();
        int sizeThreshold = -1;
        boolean runPerSubmodule = false;

        return parse(baseDir, pomCacheEnabled, pomCacheDirectory, skipMavenParsing, EXCLUSIONS, plainTextMasks, sizeThreshold, runPerSubmodule, executionContext);
    }


    @NotNull
    public RewriteProjectParsingResult parse(Path baseDir, boolean pomCacheEnabled, String pomCacheDirectory, boolean skipMavenParsing, Collection<String> exclusions, Collection<String> plainTextMasks, int sizeThreshold, boolean runPerSubmodule, ExecutionContext executionContext) {
        Collection<String> allExclusions = new ArrayList<>();
        allExclusions.addAll(EXCLUSIONS);
        allExclusions.addAll(exclusions);

        PlexusContainer plexusContainer = createPlexusContainer(baseDir);
        AtomicReference<RewriteProjectParsingResult> parsingResult = new AtomicReference<>();
        new MavenRunner().runAfterMavenGoals(plexusContainer, baseDir, List.of("clean", "package"), event -> {
            List<MavenProject> projects = event.getSession().getProjects();

            if (event.getProject().getName().equals(projects.get(projects.size() - 1).getArtifactId())) {
                MavenSession session = event.getSession();
                List<MavenProject> mavenProjects = session.getAllProjects();
                MavenMojoProjectParser rewriteProjectParser = buildMavenMojoProjectParser(
                        baseDir,
                        mavenProjects,
                        pomCacheEnabled,
                        pomCacheDirectory,
                        skipMavenParsing,
                        allExclusions,
                        plainTextMasks,
                        sizeThreshold,
                        runPerSubmodule,
                        plexusContainer,
                        session);
                List<NamedStyles> styles = List.of();
                List<SourceFile> sourceFiles = parseSourceFiles(rewriteProjectParser, mavenProjects, styles, executionContext);
                parsingResult.set(new RewriteProjectParsingResult(sourceFiles, executionContext));
            }
        });

        return parsingResult.get();
    }

    private List<SourceFile> parseSourceFiles(MavenMojoProjectParser rewriteProjectParser, List<MavenProject> mavenProjects, List<NamedStyles> styles, ExecutionContext executionContext) {
        try {
            Stream<SourceFile> sourceFileStream = rewriteProjectParser.listSourceFiles(
                    mavenProjects.get(mavenProjects.size() - 1), // FIXME: Order and access to root module
                    styles,
                    executionContext);
            return sourcesWithAutoDetectedStyles(sourceFileStream);
        } catch (DependencyResolutionRequiredException | MojoExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private MavenMojoProjectParser buildMavenMojoProjectParser(
            Path baseDir,
            List<MavenProject> mavenProjects,
            boolean pomCacheEnabled,
            String pomCacheDirectory,
            boolean skipMavenParsing,
            Collection<String> exclusions,
            Collection<String> plainTextMasks,
            int sizeThresholdMb,
            boolean runPerSubmodule,
            PlexusContainer plexusContainer, MavenSession session) {
        try {
            Log logger = new SystemStreamLog(); // plexusContainer.lookup(Log.class);//new DefaultLog(new ConsoleLogger());
            RuntimeInformation runtimeInformation = plexusContainer.lookup(RuntimeInformation.class);//new DefaultRuntimeInformation();
            ProjectDependencyGraph projectDependencyGraph = new DefaultProjectDependencyGraph(mavenProjects);
            SettingsDecrypter decrypter = plexusContainer.lookup(SettingsDecrypter.class);

            MavenMojoProjectParser sut = new MavenMojoProjectParser(
                    logger,
                    baseDir,
                    pomCacheEnabled,
                    pomCacheDirectory,
                    runtimeInformation,
                    skipMavenParsing,
                    exclusions,
                    plainTextMasks,
                    sizeThresholdMb,
                    session,
                    decrypter,
                    runPerSubmodule);

            return sut;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class MavenRunner {

        void runAfterMavenGoals(PlexusContainer plexusContainer, Path baseDir, List<String> goals, Consumer<ExecutionEvent> eventConsumer) {
            try {
                MavenExecutionRequest request = mavenExecutionRequestFactory.createMavenExecutionRequest(plexusContainer, baseDir);
                request.setExecutionListener(new AbstractExecutionListener() {
                    @Override
                    public void mojoFailed(ExecutionEvent event) {
                        super.mojoFailed(event);
                        String mojo = event.getMojoExecution().getGroupId() + ":" + event.getMojoExecution().getArtifactId() + ":" + event.getMojoExecution().getGoal();
                        throw new RuntimeException("Exception while executing Maven Mojo: " + mojo, event.getException());
                    }

                    @Override
                    public void projectSucceeded(ExecutionEvent event) {
                        eventConsumer.accept(event);
                    }
                });
                Maven maven = plexusContainer.lookup(Maven.class);
                MavenExecutionResult execute = maven.execute(request);
                if (execute.hasExceptions()) {
                    System.out.println(execute.getExceptions().get(0).getMessage());
                }
            } catch (ComponentLookupException e) {
                throw new RuntimeException(e);
            }
        }
        void runAfterMavenGoals(Path baseDir, List<String> goals, Consumer<ExecutionEvent> eventConsumer) {
            PlexusContainer plexusContainer = createPlexusContainer(baseDir);
            runAfterMavenGoals(plexusContainer, baseDir, goals, eventConsumer);
        }
    }

    private void runOpenRewriteParser(MavenSession session) {
        session.getProjects();
    }

    private PlexusContainer createPlexusContainer(Path baseDir) {
        return mavenPlexusContainerFactory.create(baseDir);
    }
//
//    private ProjectBuildingResult buildProject(ProjectBuilder projectBuilder, Path baseDir) {
//        try {
//            Path pomXmlFile = baseDir.resolve("pom.xml");
//            DefaultProjectBuildingRequest request = new DefaultProjectBuildingRequest();
//            RepositorySystemSession repositorySystemSession = new DefaultRepositorySystemSession();
//            request.setRepositorySession(repositorySystemSession);
//            ProjectBuildingResult buildingResult = null;
//            buildingResult = projectBuilder.build(
//                    pomXmlFile.toFile(),
//                    request
//            );
//            return buildingResult;
//        } catch (ProjectBuildingException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    private ProjectBuilder buildProjectBuilder(PlexusContainer plexusContainer) {
//        try {
//            ClassLoader mavenClassLoader = getClass().getClassLoader();
//            return plexusContainer.lookup(ProjectBuilder.class);
//        } catch (ComponentLookupException e) {
//            throw new RuntimeException("Could not lookup an instance of ProjectBuilder from Plexus container.", e);
//        }
//
//    }

    // copied from OpenRewrite for now, TODO: remove and reuse
    List<SourceFile> sourcesWithAutoDetectedStyles(Stream<SourceFile> sourceFiles) {
        org.openrewrite.java.style.Autodetect.Detector javaDetector = org.openrewrite.java.style.Autodetect.detector();
        org.openrewrite.xml.style.Autodetect.Detector xmlDetector = org.openrewrite.xml.style.Autodetect.detector();
        List<SourceFile> sourceFileList = sourceFiles
                .peek(javaDetector::sample)
                .peek(xmlDetector::sample)
                .collect(toList());

        Map<Class<? extends Tree>, NamedStyles> stylesByType = new HashMap<>();
        stylesByType.put(JavaSourceFile.class, javaDetector.build());
        stylesByType.put(Xml.Document.class, xmlDetector.build());

        return ListUtils.map(sourceFileList, applyAutodetectedStyle(stylesByType));
    }

    // copied from OpenRewrite for now, TODO: remove and reuse
    UnaryOperator<SourceFile> applyAutodetectedStyle(Map<Class<? extends Tree>, NamedStyles> stylesByType) {
        return before -> {
            for (Map.Entry<Class<? extends Tree>, NamedStyles> styleTypeEntry : stylesByType.entrySet()) {
                if (styleTypeEntry.getKey().isAssignableFrom(before.getClass())) {
                    before = before.withMarkers(before.getMarkers().add(styleTypeEntry.getValue()));
                }
            }
            return before;
        };
    }

}
