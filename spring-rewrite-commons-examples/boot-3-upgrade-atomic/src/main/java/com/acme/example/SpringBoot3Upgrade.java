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
package com.acme.example;

import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.rewrite.parsers.RewriteProjectParser;
import org.springframework.rewrite.parsers.RewriteProjectParsingResult;
import org.springframework.rewrite.project.resource.ProjectResourceSet;
import org.springframework.rewrite.project.resource.ProjectResourceSetFactory;
import org.springframework.rewrite.project.resource.ProjectResourceSetSerializer;
import org.springframework.rewrite.recipes.RewriteRecipeDiscovery;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * @author Fabian Krüger
 */
@SpringBootApplication
public class SpringBoot3Upgrade implements CommandLineRunner {

	public static final String RECIPE_NAME = "org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_1";

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot3Upgrade.class, args);
	}

	private final RewriteProjectParser parser;

	private final RewriteRecipeDiscovery discovery;

	private final ProjectResourceSetSerializer serializer;

	private final ProjectResourceSetFactory projectResourceSetFactory;

	public SpringBoot3Upgrade(RewriteProjectParser parser, RewriteRecipeDiscovery discovery,
			ProjectResourceSetSerializer serializer, ProjectResourceSetFactory projectResourceSetFactory) {
		this.parser = parser;
		this.discovery = discovery;
		this.serializer = serializer;
		this.projectResourceSetFactory = projectResourceSetFactory;
	}

	@Override
	public void run(String... args) {
		Path baseDir;
		if (args.length == 0) {
			throw new IllegalArgumentException("A path must be provided");
		}
		else {
			String pathStr = args[0];
			baseDir = Path.of(pathStr);
		}

		// parse
		RewriteProjectParsingResult parsingResult = parser.parse(baseDir);
		List<SourceFile> sourceFiles = parsingResult.sourceFiles();
		ProjectResourceSet projectResourceSet = projectResourceSetFactory.create(baseDir, sourceFiles);

		// discover
		List<Recipe> recipes = discovery.discoverRecipes();
		Optional<Recipe> recipe = recipes.stream().filter(r -> RECIPE_NAME.equals(r.getName())).findFirst();

		// apply
		recipe.ifPresent((Recipe r) -> {
			projectResourceSet.apply(r);
			serializer.writeChanges(projectResourceSet);
		});
	}

}
