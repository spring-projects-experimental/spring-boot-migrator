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
import org.openrewrite.style.NamedStyles;
import org.openrewrite.xml.tree.Xml;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;
import org.sonatype.plexus.components.cipher.PlexusCipherException;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SourceFileParser {

    private final MavenModelReader modelReader;
    private final ParserSettings parserSettings;
    private final MavenMojoProjectParserFactory mavenMojoProjectParserFactory;

    public Stream<SourceFile> parseOtherSourceFiles(Path baseDir, Map<Resource, Xml.Document> parsedBuildFiles, List<Resource> resources, Map<Resource, List<Marker>> provenanceMarkers, List<NamedStyles> styles, ExecutionContext executionContext) {
        parsedBuildFiles.keySet().forEach(r -> {
            Xml.Document moduleBuildFile = parsedBuildFiles.get(r);
            List<Marker> markers = provenanceMarkers.get(r);
            List<SourceFile> parsedSourceFiles = parseModuleSourceFiles(resources, moduleBuildFile, markers, styles, executionContext, baseDir);
        });

        return null;
    }

    /**
     * {@link org.openrewrite.maven.MavenMojoProjectParser#listSourceFiles(MavenProject, Xml.Document, List, List, ExecutionContext)}
     */
    private List<SourceFile> parseModuleSourceFiles(List<Resource> resources, Xml.Document moduleBuildFile, List<Marker> provenanceMarkers, List<NamedStyles> styles, ExecutionContext executionContext, Path baseDir) {
        List<SourceFile> sourceFiles = new ArrayList<>();
        // 146:149: get source encoding from maven
        // TDOD:
        //String s = moduleBuildFile.getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getProperties().get("project.build.sourceEncoding");
//        if (mavenSourceEncoding != null) {
//            ParsingExecutionContextView.view(ctx).setCharset(Charset.forName(mavenSourceEncoding.toString()));
//        }

        // 150:153
        JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder = JavaParser.fromJavaVersion()
                .styles(styles)
                .logCompilationWarningsAndErrors(false);
        Set<Path> pathsToOtherModules = pathsToOtherMavenProjects(resources, moduleBuildFile);
        ResourceParser rp = new ResourceParser(
                baseDir,
                new Slf4jToMavenLoggerAdapter(log),
                parserSettings.getExclusions(),
                parserSettings.getPlainTextMasks(),
                parserSettings.getSizeThresholdMb(),
                pathsToOtherModules,
                javaParserBuilder.clone()
        );

        // 155:156: parse main and test sources
        Set<Path> alreadyParsed = Set.of();
        List<SourceFile> mainSources = parseMainSources(baseDir, moduleBuildFile, javaParserBuilder.clone(), rp, provenanceMarkers, alreadyParsed, executionContext);
        List<SourceFile> testSources = parseTestSources(baseDir, moduleBuildFile, javaParserBuilder.clone(), rp, provenanceMarkers, alreadyParsed, executionContext);

        // 157:169
        sourceFiles = mergeAndFilterExcluded(baseDir, parserSettings.getExclusions(), mainSources, testSources);

        // 171:175
        Stream<SourceFile> parsedResourceFiles = rp.parse(baseDir.resolve(moduleBuildFile.getSourcePath()).resolve("src/main/resources"), alreadyParsed )
                .map(addProvenance(baseDir, provenanceMarkers, null));



        return sourceFiles;
    }

    /**
     * {@link MavenMojoProjectParser#addProvenance(Path, List, Collection)}
     */
    private <T extends SourceFile> UnaryOperator<T> addProvenance(Path baseDir, List<Marker> provenance, @Nullable Collection<Path> generatedSources) {
        MavenMojoProjectParser mavenMojoProjectParser = createMavenMojoProjectParser(baseDir);
        Method method = ReflectionUtils.findMethod(MavenMojoProjectParser.class, "addProvenance");
        if(method == null) {
            throw new IllegalStateException("Could not find method '%s' on %s while trying to call it.".formatted("addProvenance", MavenMojoProjectParser.class.getName()));
        }
        Object result = ReflectionUtils.invokeMethod(method, mavenMojoProjectParser, baseDir, provenance, generatedSources);
        return (UnaryOperator<T>) result;
    }

    private List<SourceFile> mergeAndFilterExcluded(Path baseDir, Set<String> exclusions, List<SourceFile> mainSources, List<SourceFile> testSources) {
        List<PathMatcher> pathMatchers = exclusions.stream()
                .map(pattern -> baseDir.getFileSystem().getPathMatcher("glob:" + pattern))
                .toList();
        return Stream.concat(mainSources.stream(), testSources.stream())
                .filter(s -> isNotExcluded(baseDir, pathMatchers, s))
                .toList();
    }

    private static boolean isNotExcluded(Path baseDir, List<PathMatcher> exclusions, SourceFile s) {
        return exclusions.stream()
                .noneMatch(pm -> pm.matches(baseDir.resolve(s.getSourcePath()).toAbsolutePath().normalize()));
    }

    private List<SourceFile> parseTestSources(Path baseDir, Xml.Document moduleBuildFile, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, ResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext) {
        return invokeProcessMethod(baseDir, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, "processTestSources");
    }

    /**
     * {@link MavenMojoProjectParser#processMainSources(MavenProject, JavaParser.Builder, ResourceParser, List, Set, ExecutionContext)}
     */
    private List<SourceFile> parseMainSources(Path baseDir, Xml.Document moduleBuildFile, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, ResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext) {
        // MavenMojoProjectParser#processMainSources(..) takes MavenProject
        // it reads from it:
        // - mavenProject.getBuild().getDirectory()
        // - mavenProject.getBuild().getSourceDirectory()
        // - mavenProject.getCompileClasspathElements() --> The classpath of the given project/module
        // - mavenProject.getBasedir().toPath()
        return invokeProcessMethod(baseDir, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, "processMainSources");
    }

    @NotNull
    private List<SourceFile> invokeProcessMethod(Path baseDir, Xml.Document moduleBuildFile, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, ResourceParser rp, List<? extends Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext, String methodName) {
        MavenMojoProjectParser mavenMojoProjectParser = createMavenMojoProjectParser(baseDir);
        Method method = ReflectionUtils.findMethod(MavenMojoProjectParser.class, methodName);
        if(method == null) {
            throw new IllegalStateException("Could not find method '%s' on %s while trying to call it.".formatted(methodName, MavenMojoProjectParser.class.getName()));
        }
        Object result = ReflectionUtils.invokeMethod(method, mavenMojoProjectParser,
//                    MavenProject mavenProject,
                new MavenProject() {
                    @Override
                    public Build getBuild() {
                        return new Build() {
                            // mavenProject.getBuild().getDirectory()
                            @Override
                            public String getDirectory() {
                                // FIXME:
                                return moduleBuildFile.getSourcePath().getParent().resolve("target").toAbsolutePath().normalize().toString();
                            }

                            // mavenProject.getBuild().getSourceDirectory()
                            @Override
                            public String getSourceDirectory() {
                                // FIXME:
                                return "src/main/java";
                            }
                        };
                    }

                    // mavenProject.getCompileClasspathElements()
                    @Override
                    public List<String> getCompileClasspathElements() {
                        // FIXME:
                        return List.of();
                    }

                    @Override
                    public List<String> getTestClasspathElements() {
                        // FIXME:
                        return List.of();
                    }

                    // mavenProject.getBasedir().toPath()
                    @Override
                    public File getBasedir() {
                        return Path.of("...").toFile();
                    }
                },
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
            List<SourceFile> sourceFiles = (List<SourceFile>) result;
            return sourceFiles;
        } else {
            throw new RuntimeException("Could not cast result returned from MavenMojoParser#methodName to Stream<SourceFile>.");
        }
    }

    private MavenMojoProjectParser createMavenMojoProjectParser(Path baseDir) {
        try {
            return mavenMojoProjectParserFactory.create(baseDir, new DefaultRuntimeInformation(), new DefaultSettingsDecrypter(new DefaultSecDispatcher(new DefaultPlexusCipher())));
        } catch (PlexusCipherException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * private Set<Path> pathsToOtherMavenProjects(MavenProject mavenProject) {
     * return mavenSession.getProjects().stream()
     * .filter(o -> o != mavenProject)
     * .map(o -> o.getBasedir().toPath())
     * .collect(Collectors.toSet());
     * }
     */
    private Set<Path> pathsToOtherMavenProjects(List<Resource> resources, Xml.Document moduleBuildFile) {
        // filter build files
        // create relative paths to all other build files
        // return result
        return Set.of();
    }
}
