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
import org.apache.maven.graph.GraphBuilder;
import org.apache.maven.model.Profile;
import org.apache.maven.model.building.Result;
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
 * @author Fabian Krüger
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RewriteMavenProjectParser {

    public static final String LOCAL_REPOSITORY = Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository").toString();
    public static final List<String> MAVEN_GOALS = List.of("clean", "test-compile");// "dependency:resolve";
    private final MavenPlexusContainerFactory mavenPlexusContainerFactory;


    public RewriteProjectParsingResult parse(Path baseDir) {
        ExecutionContext executionContext = new InMemoryExecutionContext();
        return parse(baseDir, executionContext);
    }

    public RewriteProjectParsingResult parse(Path baseDir, ExecutionContext executionContext) {
        boolean pomCacheEnabled = true;
        String pomCacheDirectory = "pom-cache";
        boolean skipMavenParsing = false;
        Collection<String> exclusions = Set.of();
        Collection<String> plainTextMasks = Set.of();
        int sizeThreshold = -1;
        boolean runPerSubmodule = false;

        return parse(baseDir, pomCacheEnabled, pomCacheDirectory, skipMavenParsing, exclusions, plainTextMasks, sizeThreshold, runPerSubmodule, executionContext);
    }

    /**
     * Parses a list of {@link Resource}s in given {@code baseDir} to OpenRewrite AST.
     * It uses default settings for configuration.
     * Use {@link #parse(Path, boolean, String, boolean, Collection, Collection, int, boolean, ExecutionContext)}
     * if you need to pass in different settings
     */
    @NotNull
    public RewriteProjectParsingResult parse(Path baseDir, boolean pomCacheEnabled, String pomCacheDirectory, boolean skipMavenParsing, Collection<String> exclusions, Collection<String> plainTextMasks, int sizeThreshold, boolean runPerSubmodule, ExecutionContext executionContext) {
        PlexusContainer plexusContainer = buildPlexusContainer(baseDir);
        AtomicReference<RewriteProjectParsingResult> parsingResult = new AtomicReference<>();
        runInMaven(baseDir, plexusContainer, session -> {
            List<MavenProject> mavenProjects = session.getAllProjects();
            MavenMojoProjectParser rewriteProjectParser = buildMavenMojoProjectParser(
                    baseDir,
                    mavenProjects,
                    pomCacheEnabled,
                    pomCacheDirectory,
                    skipMavenParsing,
                    exclusions,
                    plainTextMasks,
                    sizeThreshold,
                    runPerSubmodule,
                    plexusContainer,
                    session);
            List<NamedStyles> styles = List.of();
            try {
                GraphBuilder graphBuilder = plexusContainer.lookup(GraphBuilder.class);
                ProjectBuildingRequest request = session.getProjectBuildingRequest();
                Result<? extends ProjectDependencyGraph> build = graphBuilder.build(session);
                // ordered projects, this is how it could be done when File is acceptable
                // the alternative would be to build the graph programmatically ourselves
                List<MavenProject> allProjects = build.get().getAllProjects();
            } catch (ComponentLookupException e) {
                throw new RuntimeException(e);
            }
            List<SourceFile> sourceFiles = parseSourceFiles(rewriteProjectParser, mavenProjects, styles, executionContext);
            parsingResult.set(new RewriteProjectParsingResult(sourceFiles, executionContext));
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
        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        } catch (MojoExecutionException e) {
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

    private void runInMaven(Path baseDir, PlexusContainer plexusContainer, Consumer<MavenSession> sessionConsumer) {
        try {
            MavenExecutionRequest request = new DefaultMavenExecutionRequest();
            ArtifactRepositoryFactory repositoryFactory = plexusContainer.lookup(ArtifactRepositoryFactory.class);
            ArtifactRepository repository = new UserLocalArtifactRepository(repositoryFactory.createArtifactRepository("local", "file://" + LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null));// repositoryFactory.createArtifactRepository("local", "file://" + LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null); // new MavenArtifactRepository("local", "file://"+LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null);
            repository.setUrl("file://" + LOCAL_REPOSITORY);
            request.setBaseDirectory(baseDir.toFile());
            request.setLocalRepositoryPath(LOCAL_REPOSITORY);
            request.setActiveProfiles(List.of("default")); // TODO: make profile configurable
            // fixes the maven run when plugins depending on Java version are encountered.
            // This is the case for some transitive dependencies when running against the SBM code base itself.
            // In these cases the Java version could not be retrieved without this line
            request.setSystemProperties(System.getProperties());

            Profile profile = new Profile();
            profile.setId("default");
            request.setProfiles(List.of(profile));
            request.setDegreeOfConcurrency(1);
            request.setLoggingLevel(MavenExecutionRequest.LOGGING_LEVEL_DEBUG);
            request.setMultiModuleProjectDirectory(baseDir.toFile());
            request.setLocalRepository(repository);
            request.setGoals(MAVEN_GOALS);
            request.setPom(baseDir.resolve("pom.xml").toFile());
            request.setExecutionListener(new AbstractExecutionListener() {
                @Override
                public void mojoFailed(ExecutionEvent event) {
                    super.mojoFailed(event);
                    String mojo = event.getMojoExecution().getGroupId() + ":" + event.getMojoExecution().getArtifactId() + ":" + event.getMojoExecution().getGoal();
                    throw new RuntimeException("Exception while executing Maven Mojo: " + mojo, event.getException());
                }

                @Override
                public void sessionStarted(ExecutionEvent event) {
                    super.sessionStarted(event);
                }

                @Override
                public void mojoSucceeded(ExecutionEvent event) {
//                    log.info("Mojo succeeded: " + event.getMojoExecution().getGoal() + " in " + event.getMojoExecution().getLifecyclePhase() + " for " + event.getProject().getGroupId() + ":" + event.getProject().getArtifactId());
//
//                    if(event.getMojoExecution().getGoal().equals("testCompile") && event.getSession().getTopLevelProject().getArtifactId().equals(event.getProject().getArtifactId())) {
//                        log.info("Starting Maven session consumer");
//                        sessionConsumer.accept(event.getSession());
//                    }
                }

                @Override
                public void projectSucceeded(ExecutionEvent event) {
                    System.out.println("PROJECT SUCCEEDED: " + event.getProject().getName());
                    List<MavenProject> projects = event.getSession().getProjects();

                    if(event.getProject().getName().equals(projects.get(projects.size()-1).getArtifactId())) {
                        sessionConsumer.accept(event.getSession());
                    }
                }


                //                @Override
//                public void mojoSucceeded(ExecutionEvent event) {
//
//                    super.mojoSucceeded(event);
//                    try {
//                        event.getProject().getCompileClasspathElements().stream()
//                                .forEach(System.out::println);
//
//
//                    } catch (DependencyResolutionRequiredException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
            });
//            request.setLocalRepositoryPath(LOCAL_REPOSITORY);
            Maven maven = plexusContainer.lookup(Maven.class);
            MavenExecutionResult execute = maven.execute(request);
            if (execute.hasExceptions()) {
                System.out.println(execute.getExceptions().get(0).getMessage());
//                throw new RuntimeException(execute.getExceptions().get(0));
            }
        } catch (ComponentLookupException e) {
            throw new RuntimeException(e);
        }
    }

    private void runOpenRewriteParser(MavenSession session) {
        session.getProjects();
    }

    private PlexusContainer buildPlexusContainer(Path baseDir) {
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
