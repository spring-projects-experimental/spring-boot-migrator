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
package org.example.app;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeRun;
import org.openrewrite.SourceFile;
import org.openrewrite.config.RecipeDescriptor;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.ProjectScanner;
import org.springframework.sbm.parsers.RewriteProjectParser;
import org.springframework.sbm.parsers.RewriteProjectParsingResult;
import org.springframework.sbm.recipes.RewriteRecipeDiscovery;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ListApplicableRecipesExampleApplication implements ApplicationRunner {

	private final RewriteRecipeDiscovery discovery;

	private final ProjectScanner scanner;

	private final RewriteProjectParser parser;

	private final ExecutionContext executionContext;

	public ListApplicableRecipesExampleApplication(RewriteRecipeDiscovery discovery, ProjectScanner scanner,
			RewriteProjectParser parser, ExecutionContext executionContext) {
		this.discovery = discovery;
		this.scanner = scanner;
		this.parser = parser;
		this.executionContext = executionContext;
	}

	public static void main(String[] args) {
		SpringApplication.run(ListApplicableRecipesExampleApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		Path baseDir = Path.of(args.getNonOptionArgs().get(0));
		List<String> recipeNames = Arrays.asList(args.getNonOptionArgs().get(1).split(","));
		List<Recipe> recipes = discovery.discoverRecipes();
		List<Recipe> matchingRecipes = recipes.stream().filter(r -> recipeNames.contains(r.getName())).toList();
		List<Resource> resources = scanner.scan(baseDir);
		// parse
		RewriteProjectParsingResult parsingResult = parser.parse(baseDir, resources);
		List<SourceFile> sourceFiles = parsingResult.sourceFiles();
		matchingRecipes.forEach(recipe -> {
			RecipeRun recipeRun = recipe.run(new InMemoryLargeSourceSet(sourceFiles), executionContext);
			recipeRun.getChangeset().getAllResults().forEach(result -> {
				result.getRecipeDescriptorsThatMadeChanges().forEach(rd -> {
					printResourceDescriptor(0, rd);
				});
			});
		});
	}

	private static void printResourceDescriptor(int indent, RecipeDescriptor rd) {
		String recipeThatWouldMakeChanges = rd.getName();
		System.out.println(" ".repeat(indent) + recipeThatWouldMakeChanges);
		rd.getRecipeList().forEach(re -> {
			printResourceDescriptor(indent + 1, re);
		});
	}

}
