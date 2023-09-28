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
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.tree.*;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.openrewrite.xml.tree.Xml;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.events.SuccessfullyParsedProjectEvent;
import org.springframework.sbm.parsers.events.StartedParsingProjectEvent;
import org.springframework.sbm.scopes.ScanScope;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Project parser parsing resources under a given {@link Path} to OpenRewrite abstract syntax tree.
 * The implementation aims to produce the exact same result as the build tool plugins provided by OpenRewrite.
 * The AST is provided as {@code List<}{@link SourceFile}{@code >}.
 *
 * <p>
 * This dummy code shows how the AST can be used to run OpenRewrite recipes:
 *
 * <pre>{@code
 *  Path projectBaseDir = ...
 *  RewriteProjectParser parser = ...
 *  RewriteRecipeDiscovery discovery = ...
 *  RewriteProjectParsingResult parsingResult = parser.parse(projectBaseDir);
 *  List<SourceFile> ast = parsingResult.sourceFiles();
 *  ExecutionContext ctx = parsingResult.executionContext();
 *  List<Recipe> recipes = discovery.discoverRecipes();
 *  RecipeRun recipeRun = recipes.get(0).run(ast, ctx);
 *  }
 * </pre>
 *
 * @author Fabian Krüger
 * @see org.springframework.sbm.recipes.RewriteRecipeDiscovery
 */
@Slf4j
@RequiredArgsConstructor
public class RewriteProjectParser {

    private final ProvenanceMarkerFactory provenanceMarkerFactory;
    private final BuildFileParser buildFileParser;
    private final SourceFileParser sourceFileParser;
    private final StyleDetector styleDetector;
    private final ParserProperties parserProperties;
    private final ParsingEventListener parsingEventListener;
    private final ApplicationEventPublisher eventPublisher;
    private final ScanScope scanScope;
    private final ConfigurableListableBeanFactory beanFactory;
    private final ProjectScanner scanner;
    private final ExecutionContext executionContext;
    private final MavenProjectAnalyzer mavenProjectAnalyzer;


    public RewriteProjectParsingResult parse(Path baseDir) {
        List<Resource> resources = scanner.scan(baseDir);
        return this.parse(baseDir, resources, executionContext);
    }

    /**
     * Parse given {@link Resource}s in {@code baseDir} to OpenRewrite AST representation.
     * <p>
     * extract all poms from list of resources
     * sort the list of poms
     * parse all poms
     * - create marker - generateProvenance()
     * Read java version from pom.xml, also checks maven-compiler-plugin settings.
     * Adds markers: BuildEnvironment, GitProvenance, BuildTool, OperatingSystemProvenance, JavaVersion, JavaProject
     * - Parse Maven files - parseMaven()
     * * parse source files
     * - listSourceFiles()
     * Extract source-encoding from poms
     * Create JavaParser with logging and styles
     * Parse resources
     * processMainSources()
     * processTestSources()
     */
    public RewriteProjectParsingResult parse(Path givenBaseDir, List<Resource> resources, ExecutionContext executionContext) {
        scanScope.clear(beanFactory);

        final Path baseDir = normalizePath(givenBaseDir);

        // FIXME: ... WARN 30694 --- [           main] .m.p.i.DeprecatedCoreExpressionValidator : Parameter 'local' is deprecated core expression; Avoid use of ArtifactRepository type. If you need access to local repository, switch to '${repositorySystemSession}' expression and get LRM from it instead.
        MavenExecutionContextView.view(executionContext).setLocalRepository(new MavenRepository("local", "file://" + Path.of(System.getProperty("user.home")).resolve(".m2/repository"), null, null, false, null, null, null));
        eventPublisher.publishEvent(new StartedParsingProjectEvent(resources));

        ParsingExecutionContextView.view(executionContext).setParsingListener(parsingEventListener);

        // TODO: "runPerSubmodule"
        // TODO: See ConfigurableRewriteMojo#getPlainTextMasks()
        // TODO: where to retrieve styles from? --> see AbstractRewriteMojo#getActiveStyles() & AbstractRewriteMojo#loadStyles()
        List<NamedStyles> styles = List.of();

        // Get the ordered list of projects
        ParserContext parserContext = mavenProjectAnalyzer.createParserContext(baseDir, resources);

        // SortedProjects makes downstream components independent of Maven classes
        // TODO: 945 Is SortedProjects still required?
//            List<Resource> sortedBuildFileResources = buildFileParser.filterAndSortBuildFiles(resources);

        // generate provenance
        Map<Path, List<Marker>> provenanceMarkers = provenanceMarkerFactory.generateProvenanceMarkers(baseDir, parserContext);

        // 127: parse build files
        Map<Path, Xml.Document> resourceToDocumentMap = buildFileParser.parseBuildFiles(baseDir, parserContext.getBuildFileResources(), parserContext.getActiveProfiles(), executionContext, parserProperties.isSkipMavenParsing(), provenanceMarkers);

        List<SourceFile> parsedAndSortedBuildFileDocuments = parserContext.getBuildFileResources().stream()
                .map(r -> resourceToDocumentMap.get(ResourceUtil.getPath(r)))
                .map(SourceFile.class::cast)
                // FIXME: 945 ugly hack
                .peek(sourceFile -> addSourceFileToModel(baseDir, parserContext.getSortedProjects(), sourceFile))
                .toList();

        log.trace("Start to parse %d source files in %d modules".formatted(resources.size() + resourceToDocumentMap.size(), resourceToDocumentMap.size()));
        List<SourceFile> list = sourceFileParser.parseOtherSourceFiles(baseDir, parserContext, resourceToDocumentMap, resources, provenanceMarkers, styles, executionContext);

//        List<SourceFile> sourceFilesWithoutPoms = sourceFilesStream.filter(sf -> resourceToDocumentMap.keySet().contains(baseDir.resolve(sf.getSourcePath()).toAbsolutePath().normalize())).toList();
        List<SourceFile> resultingList = new ArrayList<>(); // sourceFilesStream2.toList();
        resultingList.addAll(parsedAndSortedBuildFileDocuments);
        resultingList.addAll(list);
        List<SourceFile> sourceFiles = styleDetector.sourcesWithAutoDetectedStyles(resultingList.stream());

        eventPublisher.publishEvent(new SuccessfullyParsedProjectEvent(sourceFiles));

        return new RewriteProjectParsingResult(sourceFiles, executionContext);
//        });

    }

    @NotNull
    private static Path normalizePath(Path givenBaseDir) {
        if (!givenBaseDir.isAbsolute()) {
            givenBaseDir = givenBaseDir.toAbsolutePath().normalize();
        }
        final Path baseDir = givenBaseDir;
        return baseDir;
    }

    private static void addSourceFileToModel(Path baseDir, List<SbmMavenProject> sortedProjectsList, SourceFile s) {
        sortedProjectsList.stream()
                .filter(p -> ResourceUtil.getPath(p.getPomFile()).toString().equals(baseDir.resolve(s.getSourcePath()).toString()))
                .forEach(p -> p.setSourceFile(s));
    }
}
