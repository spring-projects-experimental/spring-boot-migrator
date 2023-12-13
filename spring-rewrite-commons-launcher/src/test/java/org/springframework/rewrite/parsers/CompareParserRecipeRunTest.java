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
package org.springframework.rewrite.parsers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeRun;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.Markers;
import org.openrewrite.marker.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.rewrite.boot.autoconfigure.SbmSupportRewriteConfiguration;
import org.springframework.rewrite.parsers.maven.RewriteMavenProjectParser;
import org.springframework.rewrite.parsers.maven.SbmTestConfiguration;
import org.springframework.rewrite.test.util.ParallelParsingResult;
import org.springframework.rewrite.test.util.ParserExecutionHelper;
import org.springframework.rewrite.test.util.TestProjectHelper;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@SpringBootTest(classes = { SbmSupportRewriteConfiguration.class, SbmTestConfiguration.class })
public class CompareParserRecipeRunTest {

	@Autowired
	RewriteProjectParser sut;

	@Autowired
	RewriteMavenProjectParser comparingParser;

	@Autowired
	private ExecutionContext executionContext;

	@Test
	@DisplayName("Running a recipe with RewriteMavenParser should yield the same result as with RewriteProjectParser")
	void runningARecipeWithRewriteMavenParserYieldsTheSameResultAsWithRewriteProjectParser() {
		Path baseDir = TestProjectHelper.getMavenProject("parser-recipe-run");
		ParallelParsingResult parallelParsingResult = new ParserExecutionHelper().parseParallel(baseDir);
		RewriteProjectParsingResult sutParsingResult = parallelParsingResult.actualParsingResult();
		RewriteProjectParsingResult compParsingResult = parallelParsingResult.expectedParsingResult();

		AtomicInteger counter = new AtomicInteger(0);

		Recipe recipe = new Recipe() {
			@Override
			public String getDisplayName() {
				return "Dummy recipe for test";
			}

			@Override
			public String getDescription() {
				return getDisplayName();
			}

			@Override
			public TreeVisitor<?, ExecutionContext> getVisitor() {
				return new JavaIsoVisitor<>() {
					@Override
					public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl,
							ExecutionContext executionContext) {
						J.ClassDeclaration cd = super.visitClassDeclaration(classDecl, executionContext);
						if (cd.getType().getFullyQualifiedName().equals("com.example.app.My")) {
							Markers markers = cd.getMarkers();
							markers = markers.addIfAbsent(new SearchResult(UUID.randomUUID(), "Another visit"));
							// This triggers the result
							cd = cd.withMarkers(markers);
							counter.incrementAndGet();
						}

						return cd;
					}
				};
			}
		};
		// Run the Comparing Parser reusing OpenRewrite code
		RecipeRun compRecipeRun = recipe.run(new InMemoryLargeSourceSet(compParsingResult.sourceFiles()),
				executionContext);
		assertThat(counter.get()).isEqualTo(2);
		assertThat(compRecipeRun.getChangeset().getAllResults()).hasSize(1);

		// Run Parser independent from Maven
		counter.setRelease(0);
		RecipeRun sutRecipeRun = recipe.run(new InMemoryLargeSourceSet(sutParsingResult.sourceFiles()),
				executionContext);
		assertThat(counter.get()).isEqualTo(2); // differs, should be 2
		assertThat(sutRecipeRun.getChangeset().getAllResults()).hasSize(1); // is 0
	}

}
