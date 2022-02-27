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

import org.springframework.sbm.engine.context.ProjectContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Recipes {

    private final List<Recipe> recipesList;

    public Recipes(List<Recipe> recipesList) {
        if (recipesList != null) {
            recipesList.sort((o1, o2) -> Integer.valueOf(o2.getOrder() == null ? 0 : o2.getOrder()).compareTo(o1.getOrder() == null ? 0 : o1.getOrder()));
            this.recipesList = Collections.unmodifiableList(recipesList);
        } else {
            this.recipesList = Collections.emptyList();
        }
    }

    public int size() {
        return recipesList.size();
    }

    public String toString() {
        return recipesList.stream()
                .map(r -> r.toString("* "))
                .collect(Collectors.joining("\n"));
    }

    public List<Recipe> getApplicable(ProjectContext context) {
        List<Recipe> applicableRecipes = recipesList.stream()
                .filter(r -> r.isApplicable(context))
                .collect(Collectors.toList());
        return applicableRecipes;
    }

    public Optional<Recipe> getRecipeByName(String recipeName) {
        return recipesList.stream()
                .filter(r -> r.getName().equals(recipeName))
                .findAny();
    }

    public List<Recipe> getAll() {
        return recipesList;
    }
}
