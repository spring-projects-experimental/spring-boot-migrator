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
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.openrewrite.xml.tree.Xml;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.events.StartedParsingProjectEvent;
import org.springframework.sbm.parsers.events.SuccessfullyParsedProjectEvent;
import org.springframework.sbm.parsers.maven.BuildFileParser;
import org.springframework.sbm.parsers.maven.MavenProjectAnalyzer;
import org.springframework.sbm.parsers.maven.ProvenanceMarkerFactory;
import org.springframework.sbm.scopes.ScanScope;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Project parser parsing resources under a given {@link Path} to OpenRewrite Lossless Semantic Tree (LST).
 * The implementation aims to produce the exact same result as the
 * build tool plugins provided by OpenRewrite. The LST is provided as
 * {@code List<}{@link SourceFile}{@code >}.
 *
 * <p>
 * This dummy code shows how the AST can be used to run OpenRewrite recipes:
 *
 * <pre>{@code
 *  Path projectBaseDir = ...
 *  RewriteProjectParsingResult parsingResult = parser.parse(projectBaseDir);
 *  List<SourceFile> ast = parsingResult.sourceFiles();
 *  List<Recipe> recipes = discovery.discoverRecipes();
 *  RecipeRun recipeRun = recipes.get(0).run(ast, ctx);
 *  }
 * </pre>
 *
 * @author Fabian Krüger
 * @see org.springframework.sbm.recipes.RewriteRecipeDiscovery
 * @see <a href="https://docs.openrewrite.org/concepts-explanations/lossless-semantic-trees">LST</a>
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

	/**
	 * Parse the given {@code baseDir} to OpenRewrite AST.
	 */
	public RewriteProjectParsingResult parse(Path baseDir) {
		List<Resource> resources = scanner.scan(baseDir);
		return this.parse(baseDir, resources);
	}

	/**
	 * Parse given {@link Resource}s in {@code baseDir} to OpenRewrite AST representation.
	 */
	public RewriteProjectParsingResult parse(Path givenBaseDir, List<Resource> resources) {
		scanScope.clear(beanFactory);

		final Path baseDir = normalizePath(givenBaseDir);

		eventPublisher.publishEvent(new StartedParsingProjectEvent(resources));

		ParsingExecutionContextView.view(executionContext).setParsingListener(parsingEventListener);

		// TODO: "runPerSubmodule"
		// TODO: See ConfigurableRewriteMojo#getPlainTextMasks()
		// TODO: where to retrieve styles from? --> see
		// AbstractRewriteMojo#getActiveStyles() & AbstractRewriteMojo#loadStyles()
		List<NamedStyles> styles = List.of();

		// Get the ordered otherSourceFiles of projects
		ParserContext parserContext = mavenProjectAnalyzer.createParserContext(baseDir, resources);

		// generate provenance
		Map<Path, List<Marker>> provenanceMarkers = provenanceMarkerFactory.generateProvenanceMarkers(baseDir,
				parserContext);

		// 127: parse build files
		// TODO: 945 this map is only used to lookup module pom by path in
		// SourceFileParser. If possible provide the build file from ParserContext and
		// remove this map.
		List<Xml.Document> parsedBuildFiles = buildFileParser.parseBuildFiles(baseDir,
				parserContext.getBuildFileResources(), parserContext.getActiveProfiles(), executionContext,
				parserProperties.isSkipMavenParsing(), provenanceMarkers);
		parserContext.setParsedBuildFiles(parsedBuildFiles);

		log.trace("Start to parse %d source files in %d modules".formatted(resources.size() + parsedBuildFiles.size(),
				parsedBuildFiles.size()));
		List<SourceFile> otherSourceFiles = sourceFileParser.parseOtherSourceFiles(baseDir, parserContext, resources,
				provenanceMarkers, styles, executionContext);

		List<Xml.Document> sortedBuildFileDocuments = parserContext.getSortedBuildFileDocuments();

		List<SourceFile> resultingList = new ArrayList<>();
		resultingList.addAll(sortedBuildFileDocuments);
		resultingList.addAll(otherSourceFiles);
		List<SourceFile> sourceFiles = styleDetector.sourcesWithAutoDetectedStyles(resultingList.stream());

		eventPublisher.publishEvent(new SuccessfullyParsedProjectEvent(sourceFiles));

		return new RewriteProjectParsingResult(sourceFiles, executionContext);
	}

	@NotNull
	private static Path normalizePath(Path givenBaseDir) {
		if (!givenBaseDir.isAbsolute()) {
			givenBaseDir = givenBaseDir.toAbsolutePath().normalize();
		}
		final Path baseDir = givenBaseDir;
		return baseDir;
	}

}
