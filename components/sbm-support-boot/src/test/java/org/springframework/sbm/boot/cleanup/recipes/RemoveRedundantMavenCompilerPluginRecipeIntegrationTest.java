package org.springframework.sbm.boot.cleanup.recipes;

import org.junit.jupiter.api.Test;

import org.springframework.sbm.test.RecipeIntegrationTestSupport;

public class RemoveRedundantMavenCompilerPluginRecipeIntegrationTest {


	@Test
	void removeRedundantMavenCompilerPlugin() {
		String applicationDir = "simple-boot";
		RecipeIntegrationTestSupport.initializeProject(applicationDir)
				.andApplyRecipeComparingWithExpected("remove-redundant-maven-compiler-plugin");
	}
}
