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

import com.example.recipes.DummyRecipe;
import io.example.recipes.AnotherDummyRecipe;
import org.assertj.core.data.Index;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.openrewrite.Recipe;
import org.openrewrite.config.*;
import org.springframework.rewrite.recipes.RewriteRecipeDiscovery;
import org.springframework.rewrite.test.util.OpenRewriteDummyRecipeInstaller;

import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@DisabledIfEnvironmentVariable(named = "GITHUB_ACTION_REF", matches = ".*")
class RewriteRecipeDiscoveryTest {

	@BeforeAll
	public static void beforeAll() {
		String mvnHome = System.getenv("MAVEN_HOME");

		if (mvnHome == null) {
			mvnHome = System.getenv("M2_HOME");
		}

		if (mvnHome == null) {
			System.err.println("You must set $MAVEN_HOME on your system for the integration test to run.");
			throw new RuntimeException();
		}

		System.setProperty("maven.home", mvnHome);

		OpenRewriteDummyRecipeInstaller recipeInstaller = new OpenRewriteDummyRecipeInstaller();
		recipeInstaller.installRecipe();
	}

	@Test
	@DisplayName("Should Discover Dummy Recipes")
	void shouldDiscoverDummyRecipes() {
		RewriteRecipeDiscovery recipeDiscovery = buildRecipeDiscovery();
		String[] acceptPackages = {};
		ClasspathScanningLoader classpathScanningLoader = new ClasspathScanningLoader(new Properties(), acceptPackages);
		List<Recipe> dummyRecipe = recipeDiscovery.discoverFilteredRecipes(List.of("com.example.recipes.DummyRecipe"),
				new Properties(), acceptPackages, classpathScanningLoader);
		assertThat(dummyRecipe).isNotNull();
		assertThat(dummyRecipe).isNotEmpty();
	}

	@Test
	@DisplayName("ProvidingAcceptedPackagesShouldOnlyShowRecipesWithMatchingPackage")
	void providingAcceptedPackagesShouldOnlyShowRecipesWithMatchingPackage() {
		ClasspathScanningLoader resourceLoader1 = new ClasspathScanningLoader(new Properties(),
				new String[] { "com.example" });
		Collection<Recipe> recipes = resourceLoader1.listRecipes();
		assertThat(recipes).anyMatch(r -> com.example.recipes.DummyRecipe.class == r.getClass());
		assertThat(recipes).noneMatch(r -> io.example.recipes.AnotherDummyRecipe.class == r.getClass());

		ClasspathScanningLoader resourceLoader2 = new ClasspathScanningLoader(new Properties(),
				new String[] { "io.example" });
		Collection<Recipe> recipes2 = resourceLoader2.listRecipes();
		assertThat(recipes2).noneMatch(r -> com.example.recipes.DummyRecipe.class == r.getClass());
		assertThat(recipes2).anyMatch(r -> io.example.recipes.AnotherDummyRecipe.class == r.getClass());

		ClasspathScanningLoader resourceLoader3 = new ClasspathScanningLoader(new Properties(),
				new String[] { "io.example", "com.example" });
		Collection<Recipe> recipes3 = resourceLoader3.listRecipes();
		assertThat(recipes3).anyMatch(r -> com.example.recipes.DummyRecipe.class == r.getClass());
		assertThat(recipes3).anyMatch(r -> io.example.recipes.AnotherDummyRecipe.class == r.getClass());
	}

	@Test
	@DisplayName("Discover all available recipes by default")
	void discoverAllAvailableRecipesByDefault() {
		RewriteRecipeDiscovery sut = buildRecipeDiscovery();
		List<Recipe> recipes = sut.discoverRecipes();
		assertThat(recipes).anyMatch(r -> r.getClass() == DummyRecipe.class);
		assertThat(recipes).anyMatch(r -> r.getClass() == AnotherDummyRecipe.class);
		assertThat(recipes).anyMatch(r -> "com.example.SomeDummyRecipeInYaml".equals(r.getName()));
	}

	@Test
	@DisplayName("Load OpenRewrite Recipes")
	void loadOpenRewriteRecipes() {
		ClasspathScanningLoader resourceLoader = new ClasspathScanningLoader(new Properties(),
				new String[] { "com.example" });
		Collection<Recipe> recipes1 = resourceLoader.listRecipes();
		assertThat(recipes1).map(Object::getClass).map(Class::getName).contains(DummyRecipe.class.getName());

		List<Recipe> recipes = Environment.builder().load(resourceLoader).build().listRecipes();

		assertThat(recipes).isNotEmpty();

		assertThat(recipes).map(Recipe::getName).contains(DummyRecipe.class.getName());

		assertThat(recipes).map(Recipe::getName).contains("com.example.SomeDummyRecipeInYaml");
	}

	@Test
	@DisplayName("Load Recipe From Classpath")
	void loadRecipeFromClasspath2() {
		String[] acceptPackages = {}; // "com.example"
		ClasspathScanningLoader loader = new ClasspathScanningLoader(new Properties(), acceptPackages);
		String path = System.getProperty("user.home");
		Path jarPath = Path
			.of(path + "/.m2/repository/org/openrewrite/recipe/rewrite-spring/4.36.0/rewrite-spring-4.36.0.jar");

		ClasspathScanningLoader classpathScanningLoader = new ClasspathScanningLoader(jarPath, new Properties(),
				Set.of(loader), getClass().getClassLoader());
		Environment environment = Environment.builder().load(loader, Set.of(classpathScanningLoader)).build();
		List<Recipe> recipes = environment.listRecipes();
		assertThat(recipes).hasSizeGreaterThan(1);
		// found the Java recipe under src/test/java/com/example/recipes/DummyRecipe.jav
		Optional<Recipe> recipe = getRecipeByDisplayName(recipes, "DummyRecipe");
		assertThat(recipe).isNotEmpty();
		Optional<Recipe> customJavaFromYaml = getRecipeByName(recipes, "com.example.SomeDummyRecipeInYaml");
		assertThat(customJavaFromYaml).isNotEmpty();
		assertThat(customJavaFromYaml.get()).isInstanceOf(DeclarativeRecipe.class);
	}

	@Test
	@DisplayName("Should Find Recipes By Tag")
	void shouldFindRecipesByTag() {
		String tag = "Java";
		RewriteRecipeDiscovery sut = buildRecipeDiscovery();
		List<Recipe> recipes = sut.findRecipesByTags(tag);

		assertThat(recipes).hasSize(1);
		assertThat(getRecipeByName(recipes, "io.example.recipes.AnotherDummyRecipe")).isNotNull();
	}

	@Test
	@Disabled("WIP: No assertions")
	@DisplayName("Should Find Recipe Categories")
	void shouldFindRecipeCategories() {
		ResourceLoader resourceLoader = new ClasspathScanningLoader(new Properties(), new String[] {});
		Collection<CategoryDescriptor> categoryDescriptors = resourceLoader.listCategoryDescriptors();

		categoryDescriptors.stream().forEach(cd -> {
			System.out.println("Tags: " + cd.getTags());
			System.out.println("Description: " + cd.getDescription());
			System.out.println("DisplayName: " + cd.getDisplayName());
			System.out.println("Priority: " + cd.getPriority());
			System.out.println("PackageName: " + cd.getPackageName());
			System.out.println("------------------------------------------------");
		});

	}

	@Test
	@DisplayName("Should Find RecipeDescriptor By Name")
	void shouldFindRecipeDescriptorByName() {

		RewriteRecipeDiscovery sut = buildRecipeDiscovery();

		RecipeDescriptor descriptor = sut.findRecipeDescriptor("AnotherDummyRecipe");

		assertThat(descriptor).isNotNull();
		assertThat(descriptor.getName()).isEqualTo("io.example.recipes.AnotherDummyRecipe");
		assertThat(descriptor.getDisplayName()).isEqualTo("AnotherDummyRecipe");
		assertThat(descriptor.getDescription()).isEqualTo("Description of AnotherDummyRecipe");

		assertThat(descriptor.getContributors()).satisfies(contributor -> {
			assertThat(contributor.getName()).isEqualTo("Fabian Krüger");
			assertThat(contributor.getEmail()).isEqualTo("some@email.com");
			assertThat(contributor.getLineCount()).isEqualTo(1);
		}, Index.atIndex(0));
		assertThat(descriptor.getContributors()).satisfies(contributor -> {
			assertThat(contributor.getName()).isEqualTo("Mike Wazowski");
			assertThat(contributor.getEmail()).isEqualTo("mike@monsterag.com");
			assertThat(contributor.getLineCount()).isEqualTo(1000);
		}, Index.atIndex(1));

		assertThat(descriptor.getMaintainers()).satisfies(maintainer -> {
			assertThat(maintainer.getMaintainer()).isEqualTo("Spring");
			assertThat(maintainer.getLogo()).isNull();
		}, Index.atIndex(0));
		assertThat(descriptor.getMaintainers()).satisfies(maintainer -> {
			assertThat(maintainer.getMaintainer()).isEqualTo("SBM");
			assertThat(maintainer.getLogo()).isNull();
		}, Index.atIndex(1));
	}

	@Test
	@DisplayName("Load Recipe From JAR")
	@Disabled("Still fiddling")
	void loadRecipeFromJar() {
		OpenRewriteDummyRecipeInstaller recipeInstaller = new OpenRewriteDummyRecipeInstaller();
		recipeInstaller.installRecipe();

		String[] acceptPackages = { "com", "org.springframework" };

		Path jarPath = Path.of(System.getProperty("user.home"))
			.resolve(".m2")
			.resolve(
					"repository/org/springframework/sbm/openrewrite-dummy-recipe/1.0-SNAPSHOT/openrewrite-dummy-recipe-1.0-SNAPSHOT.jar");
		assertThat(jarPath.toFile()).exists();
		ClasspathScanningLoader scanningLoader = new ClasspathScanningLoader(new Properties(), acceptPackages);
		ClasspathScanningLoader classpathScanningLoader = new ClasspathScanningLoader(jarPath, new Properties(),
				Set.of(scanningLoader), getClass().getClassLoader());

		Environment environment = Environment.builder().load(classpathScanningLoader).build();
		List<Recipe> recipes = environment.listRecipes();
		assertThat(recipes).hasSizeGreaterThan(2);

		// found the Java recipe under src/test/java/com/example/recipes/DummyRecipe.java
		Optional<Recipe> recipe = getRecipeByDisplayName(recipes, "Dummy Recipe in Java");
		assertThat(recipe).isNotEmpty();

		// found the declarative recipe under
		// src/test/resources/META-INF/rewrite/dummy-recipe-in-yaml.yaml
		Optional<Recipe> customJavaFromYaml = getRecipeByName(recipes, "com.example.SomeDummyRecipeInYaml");
		assertThat(customJavaFromYaml).isNotEmpty();
		assertThat(customJavaFromYaml.get()).isInstanceOf(DeclarativeRecipe.class);
	}

	private Optional<Recipe> getRecipeByName(List<Recipe> recipes, String s) {
		return recipes.stream().filter(r -> r.getName().equals(s)).findFirst();
	}

	@NotNull
	private static Optional<Recipe> getRecipeByDisplayName(List<Recipe> recipes, String recipeDisplayName) {
		return recipes.stream().filter(r -> {
			return r.getDisplayName().equals(recipeDisplayName);
		}).findFirst();
	}

	@NotNull
	private static RewriteRecipeDiscovery buildRecipeDiscovery() {
		return new RewriteRecipeDiscovery(new ParserProperties());
	}

}