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

package org.springframework.sbm.engine.recipe;

import org.openrewrite.config.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//
// @Component
public class RewriteRecipeLoader implements RecipeLoader {
    @Override
    public List<Recipe> loadRecipes() {
        List<Recipe> recipeList = new ArrayList<>();

        Environment environment = Environment.builder()
                .scanRuntimeClasspath()
                .build();

        environment.listRecipes().forEach(r -> {
            Recipe recipe = new Recipe("Rewrite: " + r.getDisplayName(), r.getDescription(), Condition.TRUE, List.of(new OpenRewriteRecipeAdapterAction(r)));
            recipeList.add(recipe);
        });

        return recipeList;
    }

    public org.openrewrite.Recipe loadRewriteRecipe(String recipeName) {
        Environment environment = Environment.builder()
                .scanRuntimeClasspath()
                .build();

        return environment.listRecipes().stream()
                .filter(r -> r.getName().equals(recipeName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Could not find OpenRewrite recipe with name '%s'", recipeName)));
    }
}