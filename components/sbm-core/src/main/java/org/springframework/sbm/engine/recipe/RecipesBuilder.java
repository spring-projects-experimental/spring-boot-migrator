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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecipesBuilder {

    // TODO: Unify with Recipes to only have one list of recipes.
    @Deprecated(forRemoval = true)
    @Autowired(required = false)
    private List<Recipe> beanRecipes;

    private final RecipeParser recipeParser;

    private final RecipeLoader recipeLoader;

    private Recipes recipes;

    public Recipes buildRecipes() {
        if (recipes == null) {
            Resource[] files = recipeLoader.loadRecipes();

            if (log.isDebugEnabled()) {
                log.debug("Recipes loading...");
                for (Resource r : files) {
                    log.debug(r.toString());
                }
                log.debug("Recipes loading... DONE");
            }

            List<Recipe> recipeList = Arrays.stream(files)
                    .peek(f -> log.debug("loading Recipe " + f.toString()))
                    .flatMap(f -> Arrays.stream(recipeParser.parseRecipe(f)))
                    .collect(Collectors.toList());

            if (beanRecipes != null) {
                recipeList.addAll(beanRecipes);
            }

            recipes = new Recipes(recipeList);
        }
        return recipes;
    }
}
