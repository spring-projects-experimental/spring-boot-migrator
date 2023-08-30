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
package org.springframework.sbm.jee.web.recipes;

import org.springframework.sbm.build.migration.actions.AddDependencies;
import org.springframework.sbm.build.migration.actions.RemoveDependenciesMatchingRegex;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.java.migration.actions.AddTypeAnnotationToTypeAnnotatedWith;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.springframework.sbm.test.RecipeTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MigrateAnnotatedServletRecipeTest {

    @Test
    void testMigrateAnnotatedServletRecipe() {

        Path recipePath = Path.of("recipes/migrate-annotated-servlets.yaml");

        testRecipe(recipePath, recipes -> {

            Optional<Recipe> recipe = recipes.getRecipeByName("migrate-annotated-servlets");

            assertThatRecipeExists(recipe);
            assertThatRecipeHasActions(recipe,
                    AddDependencies.class,
                    AddTypeAnnotationToTypeAnnotatedWith.class,
                    RemoveDependenciesMatchingRegex.class
            );

            AddDependencies addDeps = getAction(recipe, AddDependencies.class);
            assertThat(addDeps.getDependencies())
                    .allMatch(d ->
                            d.getGroupId().equals("org.springframework.boot") &&
                                    d.getArtifactId().equals("spring-boot-starter-web")
                    );

            AddTypeAnnotationToTypeAnnotatedWith addTypeAnnotationToTypeAnnotatedWith = getAction(recipe, AddTypeAnnotationToTypeAnnotatedWith.class);
            assertThat(addTypeAnnotationToTypeAnnotatedWith.getAnnotation()).isEqualTo("org.springframework.boot.web.servlet.ServletComponentScan");
            assertThat(addTypeAnnotationToTypeAnnotatedWith.getAnnotatedWith()).isEqualTo("org.springframework.boot.autoconfigure.SpringBootApplication");
        });
    }
}
