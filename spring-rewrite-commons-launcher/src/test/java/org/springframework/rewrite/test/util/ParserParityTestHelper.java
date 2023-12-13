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
package org.springframework.rewrite.test.util;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.Markers;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.style.Style;
import org.springframework.rewrite.parsers.ParserProperties;
import org.springframework.rewrite.parsers.RewriteProjectParsingResult;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class ParserParityTestHelper {

	private final Path baseDir;

	private ParserProperties parserProperties = new ParserProperties();

	private boolean isParallelParse = true;

	private ExecutionContext executionContext;

	private ParserParityTestHelper(Path baseDir) {
		this.baseDir = baseDir;
	}

	public static ParserParityTestHelper scanProjectDir(Path baseDir) {
		ParserParityTestHelper helper = new ParserParityTestHelper(baseDir);
		return helper;
	}

	/**
	 * Sequentially parse given project using tested parser and then comparing parser. The
	 * parsers are executed in parallel by default.
	 */
	public ParserParityTestHelper parseSequentially() {
		this.isParallelParse = false;
		return this;
	}

	public ParserParityTestHelper withParserProperties(ParserProperties parserProperties) {
		this.parserProperties = parserProperties;
		return this;
	}

	public ParserParityTestHelper withExecutionContextForComparingParser(ExecutionContext executionContext) {
		this.executionContext = executionContext;
		return this;
	}

	/**
	 * Use this method when no additional assertions required.
	 */
	public void verifyParity() {
		verifyParity((expectedParsingResult, actualParsingResult) -> {
			// nothing extra to verify
		});
	}

	/**
	 * Use this method if additional assertions are required.
	 */
	public void verifyParity(CustomParserResultParityChecker customParserResultParityChecker) {
		RewriteProjectParsingResult expectedParserResult = null;
		RewriteProjectParsingResult actualParserResult = null;

		ParserExecutionHelper parserExecutionHelper = new ParserExecutionHelper();
		if (isParallelParse) {
			ParallelParsingResult result = parserExecutionHelper.parseParallel(baseDir, parserProperties,
					executionContext);
			expectedParserResult = result.expectedParsingResult();
			actualParserResult = result.actualParsingResult();
		}
		else {
			actualParserResult = parserExecutionHelper.parseWithRewriteProjectParser(baseDir, parserProperties);
			expectedParserResult = parserExecutionHelper.parseWithComparingParser(baseDir, parserProperties,
					executionContext);
		}

		DefaultParserResultParityChecker.verifyParserResultParity(baseDir, expectedParserResult, actualParserResult);

		// additional checks
		customParserResultParityChecker.accept(actualParserResult, expectedParserResult);
	}

	public interface CustomParserResultParityChecker
			extends BiConsumer<RewriteProjectParsingResult, RewriteProjectParsingResult> {

		@Override
		void accept(RewriteProjectParsingResult expectedParsingResult, RewriteProjectParsingResult actualParsingResult);

	}

	@RequiredArgsConstructor
	private class DefaultParserResultParityChecker {

		public static void verifyParserResultParity(Path baseDir, RewriteProjectParsingResult expectedParserResult,
				RewriteProjectParsingResult actualParserResult) {
			verifyEqualNumberOfParsedResources(expectedParserResult, actualParserResult);
			verifyEqualResourcePaths(baseDir, expectedParserResult, actualParserResult);
			RewriteMarkerParityVerifier.verifyEqualMarkers(expectedParserResult, actualParserResult);
		}

		private static void verifyEqualResourcePaths(Path baseDir, RewriteProjectParsingResult expectedParserResult,
				RewriteProjectParsingResult actualParserResult) {
			List<String> expectedResultPaths = expectedParserResult.sourceFiles()
				.stream()
				.map(sf -> baseDir.resolve(sf.getSourcePath()).toAbsolutePath().normalize().toString())
				.toList();
			List<String> actualResultPaths = actualParserResult.sourceFiles()
				.stream()
				.map(sf -> baseDir.resolve(sf.getSourcePath()).toAbsolutePath().normalize().toString())
				.toList();
			assertThat(actualResultPaths).containsExactlyInAnyOrder(expectedResultPaths.toArray(String[]::new));
		}

		private static void verifyEqualNumberOfParsedResources(RewriteProjectParsingResult expectedParserResult,
				RewriteProjectParsingResult actualParserResult) {
			assertThat(actualParserResult.sourceFiles().size())
				.as(renderErrorMessage(expectedParserResult, actualParserResult))
				.isEqualTo(expectedParserResult.sourceFiles().size());
		}

		private static String renderErrorMessage(RewriteProjectParsingResult expectedParserResult,
				RewriteProjectParsingResult actualParserResult) {
			List<SourceFile> collect = new ArrayList<>();
			if (expectedParserResult.sourceFiles().size() > actualParserResult.sourceFiles().size()) {
				collect = expectedParserResult.sourceFiles()
					.stream()
					.filter(element -> !actualParserResult.sourceFiles().contains(element))
					.collect(Collectors.toList());
			}
			else {
				collect = actualParserResult.sourceFiles()
					.stream()
					.filter(element -> !expectedParserResult.sourceFiles().contains(element))
					.collect(Collectors.toList());
			}

			return "ComparingParserResult had %d sourceFiles whereas TestedParserResult had %d sourceFiles. Files were %s"
				.formatted(expectedParserResult.sourceFiles().size(), actualParserResult.sourceFiles().size(), collect);
		}

	}

	private static class RewriteMarkerParityVerifier {

		static void verifyEqualMarkers(RewriteProjectParsingResult expectedParserResult,
				RewriteProjectParsingResult actualParserResult) {
			List<SourceFile> expectedSourceFiles = expectedParserResult.sourceFiles();
			List<SourceFile> actualSourceFiles = actualParserResult.sourceFiles();

			// bring to same order
			expectedSourceFiles.sort(Comparator.comparing(SourceFile::getSourcePath));
			actualSourceFiles.sort(Comparator.comparing(SourceFile::getSourcePath));

			// Compare and verify markers of all source files
			for (SourceFile curExpectedSourceFile : expectedSourceFiles) {
				int index = expectedSourceFiles.indexOf(curExpectedSourceFile);
				SourceFile curGivenSourceFile = actualSourceFiles.get(index);
				verifyEqualSourceFileMarkers(curExpectedSourceFile, curGivenSourceFile);
			}
		}

		static void verifyEqualSourceFileMarkers(SourceFile curExpectedSourceFile, SourceFile curGivenSourceFile) {
			Markers expectedMarkers = curExpectedSourceFile.getMarkers();
			List<Marker> expectedMarkersList = expectedMarkers.getMarkers();
			Markers givenMarkers = curGivenSourceFile.getMarkers();
			List<Marker> actualMarkersList = givenMarkers.getMarkers();

			assertThat(actualMarkersList.size()).isEqualTo(expectedMarkersList.size());

			SoftAssertions softAssertions = new SoftAssertions();

			actualMarkersList.sort(Comparator.comparing(o -> o.getClass().getName()));
			expectedMarkersList.sort(Comparator.comparing(o -> o.getClass().getName()));

			expectedMarkersList.forEach(expectedMarker -> {
				int i = expectedMarkersList.indexOf(expectedMarker);
				Marker actualMarker = actualMarkersList.get(i);

				assertThat(actualMarker).isInstanceOf(expectedMarker.getClass());

				if (MavenResolutionResult.class.isInstance(actualMarker)) {
					MavenResolutionResult expected = (MavenResolutionResult) expectedMarker;
					MavenResolutionResult actual = (MavenResolutionResult) actualMarker;
					compareMavenResolutionResultMarker(softAssertions, expected, actual);
				}
				else {
					compareMarker(softAssertions, expectedMarker, actualMarker);
				}

			});

			softAssertions.assertAll();

			if (curExpectedSourceFile.getMarkers().findFirst(JavaSourceSet.class).isPresent()) {
				// Tested parser must have JavaSourceSet marker when comparing parser has
				// it
				assertThat(givenMarkers.findFirst(JavaSourceSet.class)).isPresent();

				// assert classpath equality
				List<String> expectedClasspath = expectedMarkers.findFirst(JavaSourceSet.class)
					.get()
					.getClasspath()
					.stream()
					.map(JavaType.FullyQualified::getFullyQualifiedName)
					.toList();
				List<String> actualClasspath = givenMarkers.findFirst(JavaSourceSet.class)
					.get()
					.getClasspath()
					.stream()
					.map(JavaType.FullyQualified::getFullyQualifiedName)
					.toList();

				assertThat(actualClasspath.size()).isEqualTo(expectedClasspath.size());

				assertThat(expectedClasspath).withFailMessage(() -> {
					List<String> additionalElementsInExpectedClasspath = expectedClasspath.stream()
						.filter(element -> !actualClasspath.contains(element))
						.collect(Collectors.toList());

					if (!additionalElementsInExpectedClasspath.isEmpty()) {
						return "Classpath of comparing and tested parser differ: comparing classpath contains additional entries: %s"
							.formatted(additionalElementsInExpectedClasspath);
					}

					List<String> additionalElementsInActualClasspath = actualClasspath.stream()
						.filter(element -> !expectedClasspath.contains(element))
						.collect(Collectors.toList());

					if (!additionalElementsInActualClasspath.isEmpty()) {
						return "Classpath of comparing and tested parser differ: tested classpath contains additional entries: %s"
							.formatted(additionalElementsInActualClasspath);
					}

					throw new IllegalStateException("Something went terribly wrong...");
				}).containsExactlyInAnyOrder(actualClasspath.toArray(String[]::new));
			}
		}

		static void compareMavenResolutionResultMarker(SoftAssertions softAssertions, MavenResolutionResult expected,
				MavenResolutionResult actual) {
			softAssertions.assertThat(actual)
				.usingRecursiveComparison()
				.withEqualsForFieldsMatchingRegexes(customRepositoryEquals("mavenSettings.localRepository"),
						"mavenSettings.localRepository", ".*\\.repository", "mavenSettings.mavenLocal.uri")
				.ignoringFields("modules", // checked further down
						"dependencies", // checked further down
						"parent.modules" // TODO:
											// https://github.com/spring-projects-experimental/spring-boot-migrator/issues/991
				)
				.ignoringFieldsOfTypes(UUID.class)
				.isEqualTo(expected);

			// verify modules
			verifyEqualModulesInMavenResolutionResult(softAssertions, expected, actual);

			// verify dependencies
			verifyEqualDependencies(softAssertions, expected, actual);
		}

		private static void verifyEqualDependencies(SoftAssertions softAssertions, MavenResolutionResult expected,
				MavenResolutionResult actual) {
			Set<Scope> keys = expected.getDependencies().keySet();
			keys.forEach(k -> {
				List<ResolvedDependency> expectedDependencies = expected.getDependencies().get(k);
				List<ResolvedDependency> actualDependencies = actual.getDependencies().get(k);

				// same order
				expectedDependencies.sort(Comparator.comparing(o -> o.getGav().toString()));
				actualDependencies.sort(Comparator.comparing(o -> o.getGav().toString()));

				softAssertions.assertThat(actualDependencies)
					.usingRecursiveComparison()
					.withEqualsForFieldsMatchingRegexes(customRepositoryEquals(".*\\.repository"), ".*\\.repository")
					.ignoringFieldsOfTypes(UUID.class)
					.isEqualTo(expectedDependencies);
			});
		}

		private static void verifyEqualModulesInMavenResolutionResult(SoftAssertions softAssertions,
				MavenResolutionResult expected, MavenResolutionResult actual) {
			List<MavenResolutionResult> expectedModules = expected.getModules();
			List<MavenResolutionResult> actualModules = actual.getModules();
			// bring modules in same order
			expectedModules.sort(Comparator.comparing(o -> o.getPom().getGav().toString()));
			actualModules.sort(Comparator.comparing(o -> o.getPom().getGav().toString()));
			// test modules
			expectedModules.forEach(cm -> {
				MavenResolutionResult actualMavenResolutionResult = actualModules.get(expectedModules.indexOf(cm));
				compareMavenResolutionResultMarker(softAssertions, cm, actualMavenResolutionResult);
			});
		}

		/**
		 * Custom equals comparing fields names with 'repository' URI. This is required
		 * because the repository URI can be 'file:host' or 'file//host' which is
		 * effectively the same. But the strict comparison fails. This custom equals
		 * method can be used instead. <pre>
		 * .withEqualsForFieldsMatchingRegexes(
		 *                  customRepositoryEquals(),
		 *                  ".*\\.repository"
		 * )
		 * </pre>
		 */
		@NotNull
		private static BiPredicate<Object, Object> customRepositoryEquals(String s) {
			// System.out.println(s);
			return (Object actual, Object expected) -> {
				// field null?
				if (actual == null) {
					if (expected == null) {
						return true;
					}
					return false;
				}
				// normal equals?
				boolean equals = actual.equals(expected);
				if (equals) {
					return true;
				}
				// Compare Repository URI
				if (actual.getClass() == actual.getClass()) {
					if (actual instanceof URI) {
						URI f1 = (URI) actual;
						URI f2 = (URI) expected;
						return equals ? true
								: f1.getScheme().equals(f2.getScheme()) && f1.getHost().equals(f2.getHost())
										&& f1.getPath().equals(f2.getPath())
										&& f1.getFragment().equals(f2.getFragment());
					}
					else if (actual instanceof String) {
						try {
							URI f1 = new URI((String) actual);
							URI f2 = new URI((String) expected);
							return f1.getScheme() == null ? (f2.getScheme() == null ? true : false)
									: f1.getScheme().equals(f2.getScheme())
											&& (f1.getHost() == null ? (f2.getHost() == null ? true : false)
													: f1.getHost().equals(f2.getHost()))
											&& f1.getPath().equals(f2.getPath()) && f1.getFragment() == null
													? (f2.getFragment() == null ? true : false)
													: f1.getFragment().equals(f2.getFragment());
						}
						catch (URISyntaxException e) {
							throw new RuntimeException(e);
						}
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}

			};
		}

		static void compareMarker(SoftAssertions softAssertions, Marker expectedMarker, Marker actualMarker) {
			softAssertions.assertThat(actualMarker)
				.usingRecursiveComparison()
				.withStrictTypeChecking()
				.ignoringCollectionOrder()
				.withEqualsForFields(equalsClasspath(), "classpath")
				.ignoringFields(
						// FIXME:
						// https://github.com/spring-projects-experimental/spring-boot-migrator/issues/982
						"styles")
				.ignoringFieldsOfTypes(UUID.class,
						// FIXME:
						// https://github.com/spring-projects-experimental/spring-boot-migrator/issues/982
						Style.class)
				.isEqualTo(expectedMarker);
		}

		private static BiPredicate<?, ?> equalsClasspath() {
			return (List<JavaType.FullyQualified> c1, List<JavaType.FullyQualified> c2) -> {
				List<String> c1Sorted = c1.stream()
					.map(JavaType.FullyQualified::getFullyQualifiedName)
					.sorted()
					.toList();
				List<String> c2Sorted = c2.stream()
					.map(JavaType.FullyQualified::getFullyQualifiedName)
					.sorted()
					.toList();
				return c1Sorted.equals(c2Sorted);
			};
		}

	}

}
