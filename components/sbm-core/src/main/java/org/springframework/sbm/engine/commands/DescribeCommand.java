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
package org.springframework.sbm.engine.commands;

import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.RecipesBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DescribeCommand extends AbstractCommand<Recipe> {

    private final RecipesBuilder recipesBuilder;

    public DescribeCommand(RecipesBuilder recipesBuilder) {
        super("describe");
        this.recipesBuilder = recipesBuilder;
    }

    @Override
    public Recipe execute(String... arguments) {
        if (arguments == null || arguments.length == 0) {
            throw new IllegalArgumentException("Describe command needs recipe name to be provided");
        } else {
            final Optional<Recipe> recipe = recipesBuilder.buildRecipes().getRecipeByName(arguments[0]);
            if (!recipe.isPresent()) {
                throw new IllegalArgumentException("Recipe with name '" + arguments[0] + "' could not be found");
            }
            return recipe.get();
        }
    }
}
