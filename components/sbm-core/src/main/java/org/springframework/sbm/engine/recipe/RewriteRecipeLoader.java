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
package org.springframework.sbm.engine.recipe;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.config.DeclarativeRecipe;
import org.openrewrite.config.Environment;
import org.openrewrite.config.YamlResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//
@Component
public class RewriteRecipeLoader implements RecipeLoader {
    @Override
    public List<Recipe> loadRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        // returns empty list for now. Otherwise, all rewrite recipes would be in the list of applicable recipes.
        return recipeList;
    }

    public org.openrewrite.Recipe loadRewriteRecipe(String recipeName) {
        List<org.openrewrite.Recipe> recipes = loadRewriteRecipes();
        return recipes
                .stream()
                .filter(r -> r.getName().equals(recipeName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Could not find OpenRewrite recipe with name '%s'", recipeName)));
    }

    @NotNull
    private List<org.openrewrite.Recipe> loadRewriteRecipes() {
        Environment environment = Environment.builder()
                .scanRuntimeClasspath()
                .build();
        return environment.listRecipes().stream().collect(Collectors.toList());
    }

    public org.openrewrite.Recipe createRecipe(String openRewriteRecipeDeclaration) {
        ByteArrayInputStream yamlInput = new ByteArrayInputStream(openRewriteRecipeDeclaration.getBytes(
                StandardCharsets.UTF_8));
        URI source = URI.create("embedded-recipe");
        YamlResourceLoader yamlResourceLoader = new YamlResourceLoader(yamlInput, source, new Properties());
        Collection<org.openrewrite.Recipe> rewriteYamlRecipe = yamlResourceLoader.listRecipes();

        rewriteYamlRecipe.stream()
                .filter(DeclarativeRecipe.class::isInstance)
                .map(DeclarativeRecipe.class::cast)
                .forEach(this::initializeRecipe);

        if(rewriteYamlRecipe.size() != 1) {
            throw new RuntimeException(String.format("Ambiguous number of recipes found. Expected exactly one, found %s", rewriteYamlRecipe.size()));
        }
        org.openrewrite.Recipe recipe = rewriteYamlRecipe.iterator().next();
        return recipe;
    }

    private void initializeRecipe(DeclarativeRecipe recipe) {
        Method initialize = ReflectionUtils.findMethod(DeclarativeRecipe.class, "initialize", Collection.class);
        ReflectionUtils.makeAccessible(initialize);
        try {
            initialize.invoke(recipe, List.of(recipe));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
