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

import org.apache.maven.Maven;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.execution.*;
import org.apache.maven.graph.DefaultProjectDependencyGraph;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.*;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.codehaus.plexus.*;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystemSession;
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

import java.net.URL;
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
public class RewriteMavenProjectParser {

    public static final String LOCAL_REPOSITORY = Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository").toString();


    /**
     * Parses a list of {@link Resource}s in given {@code baseDir} to OpenRewrite AST.
     * It uses default settings for configuration.
     * Use {@link #parse(Path, boolean, String, boolean, Collection, Collection, int, boolean)}
     * if you need to pass in different settings
     */
    public RewriteProjectParsingResult parse(Path baseDir) {
        boolean pomCacheEnabled = true;
        String pomCacheDirectory = "pom-cache";
        boolean skipMavenParsing = false;
        Collection<String> exclusions = Set.of();
        Collection<String> plainTextMasks = Set.of();
        int sizeThreshold = -1;
        boolean runPerSubmodule = false;

        return parse(baseDir, pomCacheEnabled, pomCacheDirectory, skipMavenParsing, exclusions, plainTextMasks, sizeThreshold, runPerSubmodule);
    }

    @NotNull
    public RewriteProjectParsingResult parse(Path baseDir, boolean pomCacheEnabled, String pomCacheDirectory, boolean skipMavenParsing, Collection<String> exclusions, Collection<String> plainTextMasks, int sizeThreshold, boolean runPerSubmodule) {
        PlexusContainer plexusContainer = buildPlexusContainer(baseDir);
//        ProjectBuilder projectBuilder = buildProjectBuilder(plexusContainer);
//        ProjectBuildingResult buildingResult = buildProject(projectBuilder, baseDir);

        AtomicReference<RewriteProjectParsingResult> parsingResult = new AtomicReference<>();

        runMaven(baseDir, plexusContainer, session -> {
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
            ExecutionContext executionContext = new InMemoryExecutionContext();
            List<SourceFile> sourceFiles = parseSourceFiles(rewriteProjectParser, mavenProjects, styles, executionContext);
            parsingResult.set(new RewriteProjectParsingResult(sourceFiles, executionContext));
        });
        return parsingResult.get();
    }

    private List<SourceFile> parseSourceFiles(MavenMojoProjectParser rewriteProjectParser, List<MavenProject> mavenProjects, List<NamedStyles> styles, ExecutionContext executionContext) {
        try {
            Stream<SourceFile> sourceFileStream = rewriteProjectParser.listSourceFiles(
                    mavenProjects.get(0),
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

    private void runMaven(Path baseDir, PlexusContainer plexusContainer, Consumer<MavenSession> sessionConsumer) {
        try {
            MavenExecutionRequest request = new DefaultMavenExecutionRequest();
            ArtifactRepositoryFactory repositoryFactory = null;
            repositoryFactory = plexusContainer.lookup(ArtifactRepositoryFactory.class);
            ArtifactRepository repository = repositoryFactory.createArtifactRepository("local", "file://"+LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null); // new MavenArtifactRepository("local", "file://"+LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null);
            request.setLocalRepository(repository);
            request.setGoals(List.of("dependency:resolve"));
            request.setPom(baseDir.resolve("pom.xml").toFile());
            request.setExecutionListener(new AbstractExecutionListener() {
//                @Override
//                public void sessionStarted(ExecutionEvent event) {
//                    super.sessionStarted(event);
//                    // debug here to see how the sesion and the project looks like
//                    try {
//                        event.getProject().getCompileClasspathElements().stream()
//                                .forEach(System.out::println);
//                    } catch (DependencyResolutionRequiredException e) {
//
//
//                    }
//                }
//
//                @Override
//                public void mojoStarted(ExecutionEvent event) {
//                    super.mojoStarted(event);
//                    System.out.println(event.getMojoExecution().getGroupId() + ":" + event.getMojoExecution().getArtifactId());
//                }

                @Override
                public void mojoSucceeded(ExecutionEvent event) {
                    try {
                        event.getProject().getCompileClasspathElements().stream()
                                .forEach(System.out::println);

                        sessionConsumer.accept(event.getSession());

                    } catch (DependencyResolutionRequiredException e) {
                        throw new RuntimeException(e);
                    }
                    super.mojoSucceeded(event);
                }
            });
//            request.setLocalRepositoryPath(LOCAL_REPOSITORY);
            Maven maven = plexusContainer.lookup(Maven.class);
            MavenExecutionResult execute = maven.execute(request);
            if (execute.hasExceptions()) {
                System.out.println(execute.getExceptions().get(0).getMessage());
                throw new RuntimeException(execute.getExceptions().get(0));
            }
        } catch (ComponentLookupException e) {
            throw new RuntimeException(e);
        }
    }

    private void runOpenRewriteParser(MavenSession session) {
        session.getProjects();
    }

    private PlexusContainer buildPlexusContainer(Path baseDir) {
        try {
            ClassLoader parent = null;
            boolean isContainerAutoWiring = false;
            String containerClassPathScanning = "on";
            String containerComponentVisibility = null;
            URL overridingComponentsXml = null; //getClass().getClassLoader().getResource("META-INF/**/components.xml");

            ContainerConfiguration configuration = new DefaultContainerConfiguration();
            configuration.setAutoWiring(isContainerAutoWiring)
                    .setClassPathScanning(containerClassPathScanning)
                    .setComponentVisibility(containerComponentVisibility)
                    .setContainerConfigurationURL(overridingComponentsXml);

            // inspired from https://github.com/jenkinsci/lib-jenkins-maven-embedder/blob/master/src/main/java/hudson/maven/MavenEmbedderUtils.java#L141
            ClassWorld classWorld = new ClassWorld();
            ClassRealm classRealm = new ClassRealm(classWorld, "maven", getClass().getClassLoader());
            classRealm.setParentRealm(new ClassRealm(classWorld, "maven-parent",
                    parent == null ? Thread.currentThread().getContextClassLoader()
                            : parent));
            configuration.setRealm(classRealm);

            configuration.setClassWorld(classWorld);
            return new DefaultPlexusContainer(configuration);
        } catch (PlexusContainerException e) {
            throw new RuntimeException(e);
        }
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
