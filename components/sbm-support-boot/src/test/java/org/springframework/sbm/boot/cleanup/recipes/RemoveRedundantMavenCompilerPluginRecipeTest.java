package org.springframework.sbm.boot.cleanup.recipes;

import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.sbm.boot.cleanup.actions.RemoveRedundantMavenCompilerPlugin;
import org.springframework.sbm.boot.cleanup.actions.RemoveRedundantMavenCompilerPluginProperties;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.test.RecipeTestSupport;

import static org.springframework.sbm.test.RecipeTestSupport.testRecipe;

public class RemoveRedundantMavenCompilerPluginRecipeTest {

	@Test
	void initializeSpringBootMigrationRecipe() {
		testRecipe(Path.of("recipes/remove-redundant-maven-compiler-plugin.yaml"), recipes -> {

			Optional<Recipe> recipe = recipes.getRecipeByName("remove-redundant-maven-compiler-plugin");

			RecipeTestSupport.assertThatRecipeHasActions(recipe, RemoveRedundantMavenCompilerPluginProperties.class,
					RemoveRedundantMavenCompilerPlugin.class);
		}, freemarker.template.Configuration.class);
	}

}
