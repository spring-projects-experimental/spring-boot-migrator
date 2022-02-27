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
package org.springframework.sbm.jee.ejb.recipes;

import org.springframework.sbm.common.migration.conditions.FileMatchingAntPathExist;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.jee.ejb.actions.MigrateEjbDeploymentDescriptor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.springframework.sbm.test.RecipeTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;

public class EjbJarDeploymentDescriptorTest {

    @Test
    void ejbJarMigrationTest() {
        Optional<Recipe> recipe = Optional.of(new MigrateEjbJarDeploymentDescriptorRecipe().ejbJarDeploymentDescriptor());

        assertThatRecipeExists(recipe);

        assertThatRecipeHasActions(recipe,
                MigrateEjbDeploymentDescriptor.class
        );

        MigrateEjbDeploymentDescriptor migrateEjbDeploymentDescriptor = getAction(recipe, MigrateEjbDeploymentDescriptor.class);
        assertThatActionHasCondition(migrateEjbDeploymentDescriptor, FileMatchingAntPathExist.class);
        FileMatchingAntPathExist fileMatchingAntPathExist = getConditionFor(migrateEjbDeploymentDescriptor, FileMatchingAntPathExist.class);
        assertThat(fileMatchingAntPathExist.getPattern()).isEqualTo("/**/ejb-jar.xml");
    }

}
