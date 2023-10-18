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
package org.springframework.sbm.parsers.maven;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaParser;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.*;
import org.springframework.sbm.utils.ResourceUtil;

import java.nio.file.FileSystems;
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
public class MavenModuleParser {

    private final ParserProperties parserProperties;
    private final ModuleParser mavenMojoProjectParserPrivateMethods;
    private final ClasspathExtractor classpathExtractor;
    private final ExecutionContext executionContext;

    public List<SourceFile> parseModuleSourceFiles(
            List<Resource> resources,
            MavenProject currentProject,
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
        List<SourceFile> mainSources = parseMainSources(baseDir, currentProject, resources, javaParserBuilder.clone(), rp, provenanceMarkers, alreadyParsed, executionContext);
        List<SourceFile> testSources = parseTestSources(baseDir, currentProject, javaParserBuilder.clone(), rp, provenanceMarkers, alreadyParsed, executionContext, resources);
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

    private List<SourceFile> parseTestSources(Path baseDir, MavenProject mavenProject, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, RewriteResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext, List<Resource> resources) {
        return mavenMojoProjectParserPrivateMethods.processTestSources(baseDir, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, mavenProject, resources);
    }

    /**
     */
    private List<SourceFile> parseMainSources(Path baseDir, MavenProject mavenProject, List<Resource> resources, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, RewriteResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext) {
        // MavenMojoProjectParser#processMainSources(..) takes MavenProject
        // it reads from it:
        // - mavenProject.getBuild().getDirectory()
        // - mavenProject.getBuild().getSourceDirectory()
        // - mavenProject.getCompileClasspathElements() --> The classpath of the given project/module
        // - mavenProject.getBasedir().toPath()
        return mavenMojoProjectParserPrivateMethods.processMainSources(baseDir, resources, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, mavenProject);
//        return invokeProcessMethod(baseDir, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, "processMainSources");
    }

    private Set<Path> pathsToOtherMavenProjects(MavenProject mavenProject, Path moduleBuildFile) {
        return mavenProject.getCollectedProjects().stream()
                .filter(p -> !p.getFile().toPath().toString().equals(moduleBuildFile.toString()))
                .map(p -> p.getFile().toPath().getParent())
                .collect(Collectors.toSet());
    }

    //-------------

    public ModuleParsingResult parseModule(Path baseDir, String modulePathSegment, Collection<Path> classpath, Collection<byte[]> classBytesClasspath, List<Resource> resources) {
        resources = filterModuleResources(baseDir, modulePathSegment, resources);
        List<SourceFile> parsedSources = new ArrayList<>();
        List<SourceFile> mainSources = parseMainSources(baseDir, classpath, classBytesClasspath, resources);
        parsedSources.addAll(mainSources);
        List<SourceFile> testSources = parseTestSources(resources, classpath, classBytesClasspath, resources);
        parsedSources.addAll(testSources);
        List<SourceFile> otherResources = parseOtherResources(resources);
        parsedSources.addAll(otherResources);
        ModuleParsingResult moduleParsingResult = new ModuleParsingResult(parsedSources);
        return moduleParsingResult;
    }

    private List<SourceFile> parseOtherResources(List<Resource> resources) {
        return new ArrayList<>();
    }

    private List<SourceFile> parseTestSources(List<Resource> resources, Collection<Path> classpath, Collection<byte[]> classBytesClasspath, List<Resource> resources1) {
        return new ArrayList<>();
    }

    private List<SourceFile> parseMainSources(Path baseDir, Collection<Path> classpath, Collection<byte[]> classBytesClasspath, List<Resource> resources) {
        return new ArrayList<>();
    }

    private List<Resource> filterModuleResources(Path baseDir, String modulePathSegment, List<Resource> resources) {
        String pattern = "glob:" + baseDir.resolve(modulePathSegment).normalize().toString() + "/**";
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(pattern);
        return resources.stream()
                .filter(r -> pathMatcher.matches(ResourceUtil.getPath(r)))
                .toList();
    }

    public ModuleParsingResult parseLeafModule(Path baseDir, List<Resource> resources, MavenResolutionResult result) {
        resources = filterModuleResources(baseDir, "", resources);
        List<SourceFile> parsedSources = new ArrayList<>();
        Set<Path> compileCP = classpathExtractor.extractClasspath(result, Scope.Compile);
        List<SourceFile> mainSources = parseMainSources(baseDir, compileCP, resources);
        parsedSources.addAll(mainSources);
//        List<Path> testCP = classpathExtractor.extractClasspath(result, Scope.Test);
//        List<SourceFile> testSources = parseTestSources(resources, testCP, resources);
//        parsedSources.addAll(testSources);
        List<SourceFile> otherResources = parseOtherResources(resources);
        parsedSources.addAll(otherResources);
        ModuleParsingResult moduleParsingResult = new ModuleParsingResult(parsedSources);
        return moduleParsingResult;
    }

//    private List<SourceFile> parseTestSources(List<Resource> resources, List<Path> testCP, List<Resource> moduleResources) {
//        JavaParser javaParser = JavaParser.fromJavaVersion().classpath(testCP).build();
//        List<Parser.Input> mainJavaSourcesParserInputs = moduleResources.stream()
//                .filter(this::isMainJavaSource)
//                .map(r ->  new Parser.Input(ResourceUtil.getPath(r), () -> ResourceUtil.getInputStream(r)))
//                .toList();
//        return javaParser.parseInputs(mainJavaSourcesParserInputs, null, executionContext).toList();
//    }

    private boolean isMainJavaSource(Resource resource) {
        Path path = ResourceUtil.getPath(resource);
        return FileSystems.getDefault().getPathMatcher("glob:**/src/main/java/**").matches(path);
    }

    private List<SourceFile> parseMainSources(Path baseDir, Set<Path> compileCP, List<Resource> moduleResources) {
        JavaParser javaParser = JavaParser.fromJavaVersion().classpath(compileCP).build();
        List<Parser.Input> mainJavaSourcesParserInputs = moduleResources.stream()
                .filter(this::isMainJavaSource)
                .map(r ->  new Parser.Input(ResourceUtil.getPath(r), () -> ResourceUtil.getInputStream(r)))
                .toList();
        return javaParser.parseInputs(mainJavaSourcesParserInputs, null, executionContext)
                .map(js -> (SourceFile)js.withMarkers(js.getMarkers().addIfAbsent(new JavaParserMarker(UUID.randomUUID(), javaParser))))
                .toList();
    }
}
