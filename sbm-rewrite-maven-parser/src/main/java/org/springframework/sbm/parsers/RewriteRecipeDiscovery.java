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
package org.springframework.sbm.parsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.openrewrite.Recipe;
import org.openrewrite.Validated;
import org.openrewrite.config.Environment;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.AbstractRewriteMojo;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private final MavenProjectFactory mavenProjectFactory;

    public List<Recipe> discoverFilteredRecipes(Resource rootPom, List<String> activeRecipes) {
        if (activeRecipes.isEmpty()) {
            log.warn("No active recipes were provided.");
            return emptyList();
        }

        List<Recipe> recipes = new ArrayList<>();

        MavenProject mavenProject = mavenProjectFactory.createMavenProject(rootPom);
        AbstractRewriteMojoHelper helper = new AbstractRewriteMojoHelper(mavenProject);
        Path repositoryRoot = helper.repositoryRoot();

        Environment env = helper.environment(getClass().getClassLoader());
        Recipe recipe = env.activateRecipes(activeRecipes);

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
            try {
                return super.environment(recipeClassLoader);
            } catch (MojoExecutionException e) {
                throw new RuntimeException(e);
            }
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
