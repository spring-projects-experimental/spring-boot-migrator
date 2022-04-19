/*
 * Copyright 2021 - 2022 the original author or authors.
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
package org.springframework.sbm.jee.jsf.recipes;

import org.springframework.sbm.test.RecipeTestSupport;
import org.springframework.sbm.build.migration.actions.AddMavenPlugin;
import org.springframework.sbm.build.migration.actions.RemoveDependenciesMatchingRegex;
import org.springframework.sbm.common.migration.actions.MoveFilesAction;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.jee.jsf.actions.AddJoinfacesDependencies;
import org.springframework.sbm.jee.jsf.conditions.IsMigrateJsf2ToSpringBootApplicableCondition;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrateJsf2xToSpringBootRecipeTest {

    @Test
    void recipe() {
        RecipeTestSupport.testRecipe(Path.of("recipes/migrate-jsf-2.x-to-spring-boot.yaml"), recipes -> {

            Optional<Recipe> recipe = recipes.getRecipeByName("migrate-jsf-2.x-to-spring-boot");
            RecipeTestSupport.assertThatRecipeExists(recipe);

            RecipeTestSupport.assertThatRecipeHasCondition(recipe, IsMigrateJsf2ToSpringBootApplicableCondition.class);

            RecipeTestSupport.assertThatRecipeHasActions(recipe,
                    AddMavenPlugin.class,
                    RemoveDependenciesMatchingRegex.class,
                    AddJoinfacesDependencies.class,
                    RemoveDependenciesMatchingRegex.class,
                    MoveFilesAction.class,
                    MoveFilesAction.class,
                    MoveFilesAction.class,
                    MoveFilesAction.class
            );

            AddMavenPlugin addMavenPlugin = RecipeTestSupport.getAction(recipe, AddMavenPlugin.class);
            assertThat(addMavenPlugin.getPlugin().getGroupId()).isEqualTo("org.joinfaces");
            assertThat(addMavenPlugin.getPlugin().getArtifactId()).isEqualTo("joinfaces-maven-plugin");
            assertThat(addMavenPlugin.getPlugin().getExecutions()).hasSize(1);
            assertThat(addMavenPlugin.getPlugin().getExecutions().get(0).getGoals()).containsExactly("classpath-scan");
        });
    }
}
