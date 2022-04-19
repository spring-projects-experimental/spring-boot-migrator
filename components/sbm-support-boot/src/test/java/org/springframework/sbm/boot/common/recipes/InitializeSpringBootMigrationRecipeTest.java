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
package org.springframework.sbm.boot.common.recipes;

import org.springframework.sbm.boot.common.actions.AddSpringBootContextTestClassAction;
import org.springframework.sbm.boot.common.actions.AddSpringBootMainClassAction;
import org.springframework.sbm.boot.properties.actions.AddSpringBootApplicationPropertiesAction;
import org.springframework.sbm.build.migration.actions.*;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.test.RecipeTestSupport;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.springframework.sbm.test.RecipeTestSupport.testRecipe;

public class InitializeSpringBootMigrationRecipeTest {
    @Test
    void initializeSpringBootMigrationRecipe() {
        testRecipe(Path.of("recipes/initialize-spring-boot-migration.yaml"), recipes -> {

            Optional<Recipe> recipe = recipes.getRecipeByName("initialize-spring-boot-migration");

            RecipeTestSupport.assertThatRecipeHasActions(recipe,
                    AddMinimalPomXml.class,
                    AddMavenDependencyManagementAction.class,
                    AddDependencies.class,
                    RemoveDependenciesMatchingRegex.class,
                    AddMavenPlugin.class,
                    AddSpringBootApplicationPropertiesAction.class,
                    AddSpringBootMainClassAction.class,
                    AddSpringBootContextTestClassAction.class,
                    BuildPackaging.class
            );

        }, freemarker.template.Configuration.class);
    }
}
