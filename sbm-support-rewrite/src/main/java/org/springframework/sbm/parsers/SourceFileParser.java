/*
 * Copyright 2021 - 2023 the original author or authors.
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
import org.apache.maven.project.MavenProject;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaParser;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.openrewrite.maven.ResourceParser;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SourceFileParser {

    private final ParserSettings parserSettings;
    private final MavenMojoProjectParserPrivateMethods mavenMojoProjectParserPrivateMethods;
    private final JavaParserBuilder javaParserBuilderHolder;

    public Stream<SourceFile> parseOtherSourceFiles(
            Path baseDir,
            SortedProjects mavenProject,
            Map<Path, Xml.Document> pathToDocumentMap,
            List<Resource> resources,
            Map<Path, List<Marker>> provenanceMarkers,
            List<NamedStyles> styles,
            ExecutionContext executionContext) {

        List<SourceFile> parsedSourceFiles = new ArrayList<>();

        mavenProject.getSortedProjects().forEach(currentMavenProject -> {
            Resource moduleBuildFileResource = mavenProject.getMatchingBuildFileResource(currentMavenProject);
            Xml.Document moduleBuildFile = pathToDocumentMap.get(ResourceUtil.getPath(moduleBuildFileResource));
            List<Marker> markers = provenanceMarkers.get(ResourceUtil.getPath(moduleBuildFileResource));
            if(markers == null || markers.isEmpty()) {
                log.warn("Could not find provenance markers for resource '%s'".formatted(mavenProject.getMatchingBuildFileResource(currentMavenProject)));
            }
            List<SourceFile> sourceFiles = parseModuleSourceFiles(resources, currentMavenProject, moduleBuildFile, markers, styles, executionContext, baseDir);
            parsedSourceFiles.addAll(sourceFiles);
        });

        return parsedSourceFiles.stream();
    }

    /**
     * {@link org.openrewrite.maven.MavenMojoProjectParser#listSourceFiles(MavenProject, Xml.Document, List, List, ExecutionContext)}
     */
    private List<SourceFile> parseModuleSourceFiles(List<Resource> resources, MavenProject mavenProject, Xml.Document moduleBuildFile, List<Marker> provenanceMarkers, List<NamedStyles> styles, ExecutionContext executionContext, Path baseDir) {
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

        javaParserBuilderHolder.setDelegate(javaParserBuilder);

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
        Set<Path> alreadyParsed = new HashSet<>();
        List<SourceFile> mainSources = parseMainSources(baseDir, mavenProject, moduleBuildFile, javaParserBuilder.clone(), rp, provenanceMarkers, alreadyParsed, executionContext);
        List<SourceFile> testSources = parseTestSources(baseDir, mavenProject, moduleBuildFile, javaParserBuilder.clone(), rp, provenanceMarkers, alreadyParsed, executionContext);

        // 171:175
        Stream<SourceFile> parsedResourceFiles = rp.parse(baseDir.resolve(moduleBuildFile.getSourcePath()).getParent(), alreadyParsed )
                // FIXME: handle generated sources
                .map(mavenMojoProjectParserPrivateMethods.addProvenance(baseDir, provenanceMarkers, null));

        // 157:169
        List<SourceFile> resourceSourceFiles = mergeAndFilterExcluded(baseDir, parserSettings.getExclusions(), mainSources, testSources);
        List<SourceFile> resourceFilesList = parsedResourceFiles.toList();
        sourceFiles.addAll(resourceFilesList);
        sourceFiles.addAll(resourceSourceFiles);

        return sourceFiles;
    }


    private List<SourceFile> mergeAndFilterExcluded(Path baseDir, Set<String> exclusions, List<SourceFile> mainSources, List<SourceFile> testSources) {
        List<PathMatcher> pathMatchers = exclusions.stream()
                .map(pattern -> baseDir.getFileSystem().getPathMatcher("glob:" + pattern))
                .toList();
        if(pathMatchers.isEmpty()) {
            return Stream.concat(mainSources.stream(), testSources.stream()).toList();
        }
        return new ArrayList<>(Stream.concat(mainSources.stream(), testSources.stream())
                .filter(s -> isNotExcluded(baseDir, pathMatchers, s))
                .toList());
    }

    private static boolean isNotExcluded(Path baseDir, List<PathMatcher> exclusions, SourceFile s) {
        return exclusions.stream()
                .noneMatch(pm -> pm.matches(baseDir.resolve(s.getSourcePath()).toAbsolutePath().normalize()));
    }

    private List<SourceFile> parseTestSources(Path baseDir, MavenProject mavenProject, Xml.Document moduleBuildFile, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, ResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext) {
        return mavenMojoProjectParserPrivateMethods.processTestSources(baseDir, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, mavenProject);
    }

    /**
     * {@link MavenMojoProjectParser#processMainSources(MavenProject, JavaParser.Builder, ResourceParser, List, Set, ExecutionContext)}
     */
    private List<SourceFile> parseMainSources(Path baseDir, MavenProject mavenProject, Xml.Document moduleBuildFile, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, ResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext) {
        // MavenMojoProjectParser#processMainSources(..) takes MavenProject
        // it reads from it:
        // - mavenProject.getBuild().getDirectory()
        // - mavenProject.getBuild().getSourceDirectory()
        // - mavenProject.getCompileClasspathElements() --> The classpath of the given project/module
        // - mavenProject.getBasedir().toPath()
        return mavenMojoProjectParserPrivateMethods.processMainSources(baseDir, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, mavenProject);
//        return invokeProcessMethod(baseDir, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, "processMainSources");
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
