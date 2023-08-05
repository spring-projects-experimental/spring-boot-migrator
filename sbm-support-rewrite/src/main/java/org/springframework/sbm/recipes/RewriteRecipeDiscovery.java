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
package org.springframework.sbm.recipes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.openrewrite.Recipe;
import org.openrewrite.Validated;
import org.openrewrite.config.ClasspathScanningLoader;
import org.openrewrite.config.Environment;
import org.openrewrite.config.RecipeDescriptor;
import org.openrewrite.config.ResourceLoader;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.AbstractRewriteMojo;
import org.springframework.sbm.parsers.InvalidRecipesException;
import org.springframework.sbm.parsers.ParserSettings;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * @author Fabian Krüger
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RewriteRecipeDiscovery {

    private final ParserSettings parserSettings;
    /**
     *
     */
    public List<Recipe> discoverRecipes() {
        ClasspathScanningLoader resourceLoader = new ClasspathScanningLoader(new Properties(), new String[]{});
        return Environment.builder()
                .load(resourceLoader)
                .build()
                .listRecipes();
    }

    public List<Recipe> discoverFilteredRecipes(List<String> activeRecipes, Properties properties, String[] acceptPackages, ClasspathScanningLoader classpathScanningLoader) {
        if (activeRecipes.isEmpty()) {
            log.warn("No active recipes were provided.");
            return emptyList();
        }

        List<Recipe> recipes = new ArrayList<>();

        Environment environment = Environment.builder(properties)
                .load(classpathScanningLoader)
                .build();

        Recipe recipe = environment.activateRecipes(activeRecipes);

        if (recipe.getRecipeList().isEmpty()) {
            log.warn("No recipes were activated. None of the provided 'activeRecipes' matched any of the applicable recipes.");
            return emptyList();
        }

        Collection<Validated<Object>> validated = recipe.validateAll();
        List<Validated.Invalid<Object>> failedValidations = validated.stream().map(Validated::failures)
                .flatMap(Collection::stream).collect(toList());
        if (!failedValidations.isEmpty()) {
            failedValidations.forEach(failedValidation -> log.error(
                    "Recipe validation error in " + failedValidation.getProperty() + ": " +
                            failedValidation.getMessage(), failedValidation.getException()));
            if (parserSettings.isFailOnInvalidActiveRecipes()) {
                throw new InvalidRecipesException("Recipe validation errors detected as part of one or more activeRecipe(s). Please check error logs.");
            } else {
                log.error("Recipe validation errors detected as part of one or more activeRecipe(s). Execution will continue regardless.");
            }
        }

        recipes.add(recipe);

        return recipes;
    }

    public List<Recipe> discoverFilteredRecipes(List<String> activeRecipes, MavenProject mavenProject) {
        if (activeRecipes.isEmpty()) {
            log.warn("No active recipes were provided.");
            return emptyList();
        }

        List<Recipe> recipes = new ArrayList<>();

        AbstractRewriteMojoHelper helper = new AbstractRewriteMojoHelper(mavenProject);

        Environment env =helper.environment(getClass().getClassLoader());
        Recipe recipe = env.activateAll();
//        Recipe recipe = env.activateRecipes(activeRecipes);

        if (recipe.getRecipeList().isEmpty()) {
            log.warn("No recipes were activated. None of the provided 'activeRecipes' matched any of the applicable recipes.");
            return emptyList();
        }

        Collection<Validated<Object>> validated = recipe.validateAll();
        List<Validated.Invalid<Object>> failedValidations = validated.stream().map(Validated::failures)
                .flatMap(Collection::stream).collect(toList());
        if (!failedValidations.isEmpty()) {
            failedValidations.forEach(failedValidation -> log.error(
                    "Recipe validation error in " + failedValidation.getProperty() + ": " +
                            failedValidation.getMessage(), failedValidation.getException()));
            if (parserSettings.isFailOnInvalidActiveRecipes()) {
                throw new InvalidRecipesException("Recipe validation errors detected as part of one or more activeRecipe(s). Please check error logs.");
            } else {
                log.error("Recipe validation errors detected as part of one or more activeRecipe(s). Execution will continue regardless.");
            }
        }

        return recipes;
    }

    public RecipeDescriptor findRecipeDescriptor(String anotherDummyRecipe) {
        ResourceLoader resourceLoader = new ClasspathScanningLoader(new Properties(), new String[]{"io.example"});
        Environment environment = Environment.builder()
                .load(resourceLoader)
                .build();

        Collection<RecipeDescriptor> recipeDescriptors = environment.listRecipeDescriptors();
        RecipeDescriptor descriptor = recipeDescriptors.stream()
                .filter(rd -> "AnotherDummyRecipe".equals(rd.getDisplayName()))
                .findFirst()
                .get();
        return descriptor;
    }

    public List<Recipe> findRecipesByTags(String tag) {
        ResourceLoader resourceLoader = new ClasspathScanningLoader(new Properties(), new String[]{});
        Environment environment = Environment.builder()
                .load(resourceLoader)
                .build();

        List<Recipe> recipes = environment.listRecipes()
                .stream()
                .filter(r -> r.getTags().contains(tag))
                .toList();

        return recipes;
    }


    class AbstractRewriteMojoHelper extends AbstractRewriteMojo {

        public AbstractRewriteMojoHelper(MavenProject mavenProject) {
            super.project = mavenProject;
        }

        @Override
        public void execute() throws MojoExecutionException, MojoFailureException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Environment environment(@Nullable ClassLoader recipeClassLoader) {
            Environment.Builder env = Environment.builder(this.project.getProperties());
            if (recipeClassLoader == null) {
                env.scanRuntimeClasspath(new String[0]).scanUserHome();
            } else {
                env.load(new ClasspathScanningLoader(this.project.getProperties(), recipeClassLoader));
            }


            /*env.load(new ResourceLoader() {
                @Override
                public Collection<Recipe> listRecipes() {
                    return List.of();
                }

                @Override
                public Collection<RecipeDescriptor> listRecipeDescriptors() {
                    return List.of();
                }

                @Override
                public Collection<NamedStyles> listStyles() {
                    return List.of();
                }

                @Override
                public Collection<CategoryDescriptor> listCategoryDescriptors() {
                    return List.of();
                }

                @Override
                public Map<String, List<Contributor>> listContributors() {
                    return Map.of();
                }

                @Override
                public Map<String, List<RecipeExample>> listRecipeExamples() {
                    return Map.of();
                }
            });*/
            return env.build();
        }

        @Override
        protected Environment environment() throws MojoExecutionException {
            return super.environment();
        }

        @Override
        public Path repositoryRoot() {
            return super.repositoryRoot();
        }
    }
}
