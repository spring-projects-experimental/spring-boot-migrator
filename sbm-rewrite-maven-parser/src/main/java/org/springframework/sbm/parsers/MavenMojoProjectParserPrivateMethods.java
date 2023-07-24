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
import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.apache.maven.rtinfo.internal.DefaultRuntimeInformation;
import org.apache.maven.settings.crypto.DefaultSettingsDecrypter;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaParser;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.openrewrite.maven.ResourceParser;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.xml.tree.Xml;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;
import org.sonatype.plexus.components.cipher.PlexusCipherException;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
public class MavenMojoProjectParserPrivateMethods {

    private final MavenMojoProjectParserFactory mavenMojoProjectParserFactory;
    private final MavenArtifactDownloader artifactDownloader;

    public List<SourceFile> processMainSources(Path baseDir, Xml.Document moduleBuildFile, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, ResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext) {
        String methodName = "processMainSources";
        MavenMojoProjectParser mavenMojoProjectParser = createMavenMojoProjectParser(baseDir);
        Method method = ReflectionUtils.findMethod(
                MavenMojoProjectParser.class,
                methodName,
                MavenProject.class,
                JavaParser.Builder.class,
                ResourceParser.class,
                List.class,
                Set.class,
                ExecutionContext.class);
        ReflectionUtils.makeAccessible(method);
        if (method == null) {
            throw new IllegalStateException("Could not find method '%s' on %s while trying to call it.".formatted(methodName, MavenMojoProjectParser.class.getName()));
        }
        MavenProject mavenProject = new MavenProject() {
            @Override
            public Build getBuild() {
                return new Build() {
                    // mavenProject.getBuild().getDirectory()
                    @Override
                    public String getDirectory() {
                        Path modulePath = getModulePath();
                        Path sourceDirectory = modulePath.resolve("target");
                        return sourceDirectory.toString();
                    }

                    // mavenProject.getBuild().getSourceDirectory()

                    @Override
                    public String getSourceDirectory() {
                        Path modulePath = getModulePath();
                        Path sourceDirectory = modulePath.resolve("src/main/java").toAbsolutePath().normalize();
                        return sourceDirectory.toString();
                    }

                    @Override
                    public String getTestSourceDirectory() {
                        Path modulePath = getModulePath();
                        Path sourceDirectory = modulePath.resolve("src/test/java").toAbsolutePath().normalize();
                        return sourceDirectory.toString();
                    }

                    @NotNull
                    private Path getModulePath() {
                        Path moduleDir = moduleBuildFile.getSourcePath().getParent();
                        if (moduleDir == null) {
                            moduleDir = Path.of("");
                        }
                        Path resolve = baseDir.resolve(moduleDir).toAbsolutePath().normalize();
                        return resolve;
                    }
                };
            }

            // mavenProject.getCompileClasspathElements()
            @Override
            public List<String> getCompileClasspathElements() {
                MavenResolutionResult mavenResolution = moduleBuildFile.getMarkers().findFirst(MavenResolutionResult.class).get();
                List<ResolvedDependency> resolvedDependencies = mavenResolution.getDependencies().get(Scope.Provided);
                List<String> dependencies = downloadArtifacts(resolvedDependencies).stream()
                        .map(Path::toAbsolutePath)
                        .map(Path::toString)
                        .toList();

                // FIXME: provide paths to jars here
                return dependencies;
            }

            @Override
            public List<String> getTestClasspathElements() {
                MavenResolutionResult mavenResolution = moduleBuildFile.getMarkers().findFirst(MavenResolutionResult.class).get();
                List<ResolvedDependency> resolvedDependencies = mavenResolution.getDependencies().get(Scope.Test);
                List<String> dependencies = downloadArtifacts(resolvedDependencies).stream()
                        .map(Path::toAbsolutePath)
                        .map(Path::toString)
                        .toList();

                // FIXME: provide paths to jars here
                return dependencies;
            }

            // mavenProject.getBasedir().toPath()
            @Override
            public File getBasedir() {
                return Path.of("...").toFile();
            }
        };

        Object result = ReflectionUtils.invokeMethod(method, mavenMojoProjectParser,
//                    MavenProject mavenProject,
                mavenProject,
//                    JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder,
                javaParserBuilder,
//                    ResourceParser resourceParser,
                rp,
//                    List<Marker> projectProvenance,
                provenanceMarkers,
//                    Set<Path> alreadyParsed,
                alreadyParsed,
//                    ExecutionContext
                executionContext
        );
        if (result instanceof Stream) {
            List<SourceFile> sourceFiles = ((Stream<SourceFile>) result).toList();
            return sourceFiles;
        } else {
            throw new RuntimeException("Could not cast result returned from MavenMojoParser#methodName to Stream<SourceFile>.");
        }
    }

    /**
     * {@link MavenMojoProjectParser#addProvenance(Path, List, Collection)}
     */
    public <T extends SourceFile> UnaryOperator<T> addProvenance(Path baseDir, List<Marker> provenance, @Nullable Collection<Path> generatedSources) {
        MavenMojoProjectParser mavenMojoProjectParser = createMavenMojoProjectParser(baseDir);
        Method method = ReflectionUtils.findMethod(MavenMojoProjectParser.class, "addProvenance", Path.class, List.class, Collection.class);
        ReflectionUtils.makeAccessible(method);
        if(method == null) {
            throw new IllegalStateException("Could not find method '%s' on %s while trying to call it.".formatted("addProvenance", MavenMojoProjectParser.class.getName()));
        }
        Object result = ReflectionUtils.invokeMethod(method, mavenMojoProjectParser, baseDir, provenance, generatedSources);
        return (UnaryOperator<T>) result;
    }

    private MavenMojoProjectParser createMavenMojoProjectParser(Path baseDir) {
        try {
            return mavenMojoProjectParserFactory.create(baseDir, new DefaultRuntimeInformation(), new DefaultSettingsDecrypter(new DefaultSecDispatcher(new DefaultPlexusCipher())));
        } catch (PlexusCipherException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Path> downloadArtifacts(List<ResolvedDependency> dependencies) {

//        eventPublisher.publishEvent(new StartDownloadingDependenciesEvent(dependencies.size()));


        List<Path> paths = dependencies
                .stream()
                .filter(d -> d.getRepository() != null)
//                .peek(d -> eventPublisher.publishEvent(new StartDownloadingDependencyEvent(d.getRequested())))
//                .parallel()
                .map(artifactDownloader::downloadArtifact)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

//        eventPublisher.publishEvent(new FinishedDownloadingDependencies());

        return paths;
    }
}
