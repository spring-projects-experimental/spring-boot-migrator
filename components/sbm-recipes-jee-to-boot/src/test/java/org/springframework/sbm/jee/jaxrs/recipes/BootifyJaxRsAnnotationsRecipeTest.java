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
package org.springframework.sbm.jee.jaxrs.recipes;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.build.migration.actions.AddDependencies;
import org.springframework.sbm.engine.recipe.OpenRewriteDeclarativeRecipeAdapter;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.java.JavaRecipeAction;
import org.springframework.sbm.java.migration.actions.ReplaceTypeAction;
import org.springframework.sbm.jee.jaxrs.MigrateJaxRsRecipe;
import org.springframework.sbm.jee.jaxrs.actions.ConvertJaxRsAnnotations;
import org.springframework.sbm.test.RecipeTestSupport;

import java.util.Optional;

public class BootifyJaxRsAnnotationsRecipeTest {

    @Test
    void test() {

        Recipe jaxRsRecipe = new MigrateJaxRsRecipe().jaxRs(null);
        Optional<Recipe> recipe = Optional.of(jaxRsRecipe);
        RecipeTestSupport.assertThatRecipeExists(recipe);
        RecipeTestSupport.assertThatRecipeHasActions(recipe,
                AddDependencies.class,
                ConvertJaxRsAnnotations.class,
                ReplaceTypeAction.class,
                ReplaceTypeAction.class,
                ReplaceTypeAction.class,
                JavaRecipeAction.class,
                JavaRecipeAction.class,
                JavaRecipeAction.class,
                JavaRecipeAction.class,
                JavaRecipeAction.class,
                JavaRecipeAction.class,
                OpenRewriteDeclarativeRecipeAdapter.class);
    }
}
