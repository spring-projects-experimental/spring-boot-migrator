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
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusContainer;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.Tree;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.java.tree.JavaSourceFile;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.openrewrite.xml.tree.Xml;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.RewriteProjectParsingResult;
import org.springframework.sbm.scopes.ScanScope;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Parses a given {@link Path} to a Open Rewrite's AST representation
 * {@code List<}{@link SourceFile}{@code >}.
 *
 * @author Fabian Krüger
 */
@Slf4j
@RequiredArgsConstructor
public class RewriteMavenProjectParser {

	private final MavenPlexusContainer mavenPlexusContainer;

	private final ParsingEventListener parsingListener;

	private final MavenExecutor mavenRunner;

	private final MavenMojoProjectParserFactory mavenMojoProjectParserFactory;

	private final ScanScope scanScope;

	private final ConfigurableListableBeanFactory beanFactory;

	private final ExecutionContext executionContext;

	/**
	 * Parses a list of {@link Resource}s in given {@code baseDir} to OpenRewrite AST. It
	 * uses default settings for configuration.
	 */
	public RewriteProjectParsingResult parse(Path baseDir) {
		ParsingExecutionContextView.view(executionContext).setParsingListener(parsingListener);
		return parse(baseDir, executionContext);
	}

	@NotNull
	public RewriteProjectParsingResult parse(Path baseDir, ExecutionContext executionContext) {
		final Path absoluteBaseDir = getAbsolutePath(baseDir);
		PlexusContainer plexusContainer = mavenPlexusContainer.get();
		RewriteProjectParsingResult parsingResult = parseInternal(absoluteBaseDir, executionContext, plexusContainer);
		return parsingResult;
	}

	private RewriteProjectParsingResult parseInternal(Path baseDir, ExecutionContext executionContext,
			PlexusContainer plexusContainer) {
		clearScanScopedBeans();

		AtomicReference<RewriteProjectParsingResult> parsingResult = new AtomicReference<>();
		mavenRunner.onProjectSucceededEvent(baseDir, List.of("clean", "package"), event -> {
			try {
				MavenSession session = event.getSession();
				List<MavenProject> mavenProjects = session.getAllProjects();
				MavenMojoProjectParser rewriteProjectParser = mavenMojoProjectParserFactory.create(baseDir,
						mavenProjects, plexusContainer, session);
				List<NamedStyles> styles = List.of();
				List<SourceFile> sourceFiles = parseSourceFiles(rewriteProjectParser, mavenProjects, styles,
						executionContext);
				parsingResult.set(new RewriteProjectParsingResult(sourceFiles, executionContext));
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return parsingResult.get();
	}

	private void clearScanScopedBeans() {
		scanScope.clear(beanFactory);
	}

	private List<SourceFile> parseSourceFiles(MavenMojoProjectParser rewriteProjectParser,
			List<MavenProject> mavenProjects, List<NamedStyles> styles, ExecutionContext executionContext) {
		try {
			Stream<SourceFile> sourceFileStream = rewriteProjectParser.listSourceFiles(
					mavenProjects.get(mavenProjects.size() - 1), // FIXME: Order and
																	// access to root
																	// module
					styles, executionContext);
			return sourcesWithAutoDetectedStyles(sourceFileStream);
		}
		catch (DependencyResolutionRequiredException | MojoExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	@NotNull
	private static Path getAbsolutePath(Path baseDir) {
		if (!baseDir.isAbsolute()) {
			baseDir = baseDir.toAbsolutePath().normalize();
		}
		return baseDir;
	}

	// copied from OpenRewrite for now, TODO: remove and reuse
	List<SourceFile> sourcesWithAutoDetectedStyles(Stream<SourceFile> sourceFiles) {
		org.openrewrite.java.style.Autodetect.Detector javaDetector = org.openrewrite.java.style.Autodetect.detector();
		org.openrewrite.xml.style.Autodetect.Detector xmlDetector = org.openrewrite.xml.style.Autodetect.detector();
		List<SourceFile> sourceFileList = sourceFiles.peek(javaDetector::sample)
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
