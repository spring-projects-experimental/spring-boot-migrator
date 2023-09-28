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
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaParser;
import org.openrewrite.marker.Marker;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@Slf4j

@RequiredArgsConstructor
public class SourceFileParser {

    private final ParserProperties parserProperties;
    private final HelperWithoutAGoodName mavenMojoProjectParserPrivateMethods;

    public List<SourceFile> parseOtherSourceFiles(
            Path baseDir,
            ParserContext parserContext,
            Map<Path, Xml.Document> pathToDocumentMap,
            List<Resource> resources,
            Map<Path, List<Marker>> provenanceMarkers,
            List<NamedStyles> styles,
            ExecutionContext executionContext) {

        Set<SourceFile> parsedSourceFiles = new LinkedHashSet<>();

        parserContext.getSortedProjects().forEach(currentMavenProject -> {
            Resource moduleBuildFileResource = parserContext.getMatchingBuildFileResource(currentMavenProject);
            Xml.Document moduleBuildFile = pathToDocumentMap.get(ResourceUtil.getPath(moduleBuildFileResource));
            List<Marker> markers = provenanceMarkers.get(ResourceUtil.getPath(moduleBuildFileResource));
            if(markers == null || markers.isEmpty()) {
                log.warn("Could not find provenance markers for resource '%s'".formatted(parserContext.getMatchingBuildFileResource(currentMavenProject)));
            }
            List<SourceFile> sourceFiles = parseModuleSourceFiles(resources, currentMavenProject, moduleBuildFile, markers, styles, executionContext, baseDir);
            parsedSourceFiles.addAll(sourceFiles);
        });

        return new ArrayList<>(parsedSourceFiles);
    }

    private List<SourceFile> parseModuleSourceFiles(
            List<Resource> resources,
            SbmMavenProject currentProject,
            Xml.Document moduleBuildFile,
            List<Marker> provenanceMarkers,
            List<NamedStyles> styles,
            ExecutionContext executionContext,
            Path baseDir)
    {

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

        Path buildFilePath = currentProject.getBasedir().resolve(moduleBuildFile.getSourcePath());
        log.info("Parsing module " + buildFilePath);
        // these paths will be ignored by ResourceParser
        Set<Path> skipResourceScanDirs = pathsToOtherMavenProjects(currentProject, buildFilePath);
        // FIXME: Why is skipResourceScanDirs required at all? Shouldn't the module know it's resources
        RewriteResourceParser rp = new RewriteResourceParser(
                baseDir,
                parserProperties.getIgnoredPathPatterns(),
                parserProperties.getPlainTextMasks(),
                parserProperties.getSizeThresholdMb(),
                skipResourceScanDirs,
                javaParserBuilder.clone(),
                executionContext);

        // 155:156: parse main and test sources
        Set<Path> alreadyParsed = new HashSet<>();
        Path moduleBuildFilePath = baseDir.resolve(moduleBuildFile.getSourcePath());
        alreadyParsed.add(moduleBuildFilePath);
        alreadyParsed.addAll(skipResourceScanDirs);
        List<SourceFile> mainSources = parseMainSources(baseDir, currentProject, moduleBuildFile, resources, javaParserBuilder.clone(), rp, provenanceMarkers, alreadyParsed, executionContext);
        List<SourceFile> testSources = parseTestSources(baseDir, currentProject, moduleBuildFile, javaParserBuilder.clone(), rp, provenanceMarkers, alreadyParsed, executionContext, resources);
        // Collect the dirs of modules parsed in previous steps

        // parse other project resources
        Stream<SourceFile> parsedResourceFiles = rp.parse(moduleBuildFilePath.getParent(), resources, alreadyParsed)
                // FIXME: handle generated sources
                .map(mavenMojoProjectParserPrivateMethods.addProvenance(baseDir, provenanceMarkers, null));
        // 157:169
        List<SourceFile> mainAndTestSources = mergeAndFilterExcluded(baseDir, parserProperties.getIgnoredPathPatterns(), mainSources, testSources);
        List<SourceFile> resourceFilesList = parsedResourceFiles.toList();
        sourceFiles.addAll(mainAndTestSources);
        sourceFiles.addAll(resourceFilesList);

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

    private List<SourceFile> parseTestSources(Path baseDir, SbmMavenProject sbmMavenProject, Xml.Document moduleBuildFile, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, RewriteResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext, List<Resource> resources) {
        return mavenMojoProjectParserPrivateMethods.processTestSources(baseDir, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, sbmMavenProject, resources);
    }

    /**
     */
    private List<SourceFile> parseMainSources(Path baseDir, SbmMavenProject sbmMavenProject, Xml.Document moduleBuildFile, List<Resource> resources, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, RewriteResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext) {
        // MavenMojoProjectParser#processMainSources(..) takes SbmMavenProject
        // it reads from it:
        // - sbmMavenProject.getBuild().getDirectory()
        // - sbmMavenProject.getBuild().getSourceDirectory()
        // - sbmMavenProject.getCompileClasspathElements() --> The classpath of the given project/module
        // - sbmMavenProject.getBasedir().toPath()
        return mavenMojoProjectParserPrivateMethods.processMainSources(baseDir, resources, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, sbmMavenProject);
//        return invokeProcessMethod(baseDir, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, "processMainSources");
    }






    /**
     * private Set<Path> pathsToOtherMavenProjects(SbmMavenProject sbmMavenProject) {
     * return mavenSession.getProjects().stream()
     * .filter(o -> o != sbmMavenProject)
     * .map(o -> o.getBasedir().toPath())
     * .collect(Collectors.toSet());
     * }
     */
    private Set<Path> pathsToOtherMavenProjects(SbmMavenProject sbmMavenProject, Path moduleBuildFile) {
        return sbmMavenProject.getCollectedProjects().stream()
                .filter(p -> !p.getFile().toPath().toString().equals(moduleBuildFile.toString()))
                .map(p -> p.getFile().toPath().getParent())
                .collect(Collectors.toSet());
    }
}
