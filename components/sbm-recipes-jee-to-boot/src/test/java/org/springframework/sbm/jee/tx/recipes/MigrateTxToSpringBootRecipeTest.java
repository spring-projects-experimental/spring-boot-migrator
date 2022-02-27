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
package org.springframework.sbm.jee.tx.recipes;

import org.springframework.sbm.test.RecipeTestSupport;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.java.migration.conditions.HasAnyTypeReference;
import org.springframework.sbm.java.migration.conditions.HasImportStartingWith;
import org.springframework.sbm.jee.tx.actions.MigrateJeeTransactionsToSpringBootAction;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrateTxToSpringBootRecipeTest {

    @Test
    void testRecipe() {
        RecipeTestSupport.testRecipe(Path.of("classpath:/recipes/migrate-tx-to-spring-boot.yaml"), recipes -> {
            Optional<Recipe> recipe = recipes.getRecipeByName("migrate-tx-to-spring-boot");
            RecipeTestSupport.assertThatRecipeExists(recipe);
            RecipeTestSupport.assertThatRecipeHasCondition(recipe, HasImportStartingWith.class);
            HasImportStartingWith recipeCondition = RecipeTestSupport.getConditionFor(recipe, HasImportStartingWith.class);
            assertThat(recipeCondition.getValue()).isEqualTo("javax.ejb.TransactionAttribute");

            RecipeTestSupport.assertThatRecipeHasActions(recipe, MigrateJeeTransactionsToSpringBootAction.class);
            MigrateJeeTransactionsToSpringBootAction action = RecipeTestSupport.getAction(recipe, MigrateJeeTransactionsToSpringBootAction.class);
            HasAnyTypeReference actionCondition = RecipeTestSupport.getConditionFor(action, HasAnyTypeReference.class);
            assertThat(actionCondition.getFqTypeNames()).contains(
                    "javax.ejb.TransactionAttribute",
                    "javax.ejb.TransactionAttributeType",
                    "javax.ejb.TransactionManagement",
                    "javax.ejb.TransactionManagementType"
            );
        });
    }
}
