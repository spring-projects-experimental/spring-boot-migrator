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
package org.springframework.sbm.jee.jpa.recipes;

import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.migration.actions.AddDependencies;
import org.springframework.sbm.build.migration.actions.RemoveDependenciesMatchingRegex;
import org.springframework.sbm.build.migration.conditions.NoDependencyExistMatchingRegex;
import org.springframework.sbm.common.migration.actions.DeleteFileMatchingPattern;
import org.springframework.sbm.common.migration.conditions.FileExist;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.java.migration.actions.AddTypeAnnotationToTypeAnnotatedWith;
import org.springframework.sbm.java.migration.conditions.HasImportStartingWith;
import org.springframework.sbm.jee.jpa.actions.MigrateEclipseLinkToSpringBoot;
import org.springframework.sbm.jee.jpa.actions.MigratePersistenceXmlToApplicationPropertiesAction;
import freemarker.template.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.jee.jpa.actions.RenameUnitNameOfPersistenceContextAnnotationsToDefault;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.springframework.sbm.test.RecipeTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MigrateJpaToSpringBootRecipeTest {

    @Test
    void migrateJpaToSpringBootRecipe() {
        testRecipe(Path.of("recipes/migrate-jpa-to-spring-boot.yaml"), recipes -> {

            Optional<Recipe> recipe = recipes.getRecipeByName("migrate-jpa-to-spring-boot");

            // Recipe
            assertThatRecipeExists(recipe);
            assertThatRecipeHasActions(recipe,
                    MigratePersistenceXmlToApplicationPropertiesAction.class,
                    AddDependencies.class,
                    AddDependencies.class,
                    RemoveDependenciesMatchingRegex.class,
                    AddTypeAnnotationToTypeAnnotatedWith.class,
                    MigrateEclipseLinkToSpringBoot.class,
                    DeleteFileMatchingPattern.class,
                    RenameUnitNameOfPersistenceContextAnnotationsToDefault.class
            );
            assertThatRecipeHasCondition(recipe, FileExist.class);

            // Action: migrate persistence.xml to Spring application.properties
            MigratePersistenceXmlToApplicationPropertiesAction applicationPropertiesAction = getAction(recipe, MigratePersistenceXmlToApplicationPropertiesAction.class);
            assertThatActionHasCondition(applicationPropertiesAction, HasImportStartingWith.class);
            HasImportStartingWith startingWith = getConditionFor(applicationPropertiesAction, HasImportStartingWith.class);
            assertThat(startingWith.getValue()).isEqualTo("javax.persistence");

            // Action: add spring boot starter data jpa dependencies
            List<AddDependencies> adDependenciesActions = getActions(recipe, AddDependencies.class);


            AddDependencies addSpringDataJpaDependencies = adDependenciesActions.get(0);
            assertThat(addSpringDataJpaDependencies.getDependencies()).containsExactly(Dependency.builder().groupId("org.springframework.boot").artifactId("spring-boot-starter-data-jpa").version("managed").build());
            assertThatActionHasCondition(addSpringDataJpaDependencies, NoDependencyExistMatchingRegex.class);
            NoDependencyExistMatchingRegex noDependencyExist = getConditionFor(addSpringDataJpaDependencies, NoDependencyExistMatchingRegex.class);
            assertThat(noDependencyExist.getDependencies()).containsExactlyInAnyOrder("org\\.springframework\\.boot\\:spring-boot-starter-data-jpa\\:.*");

            // add h2 dependency
            AddDependencies addH2Dependency = adDependenciesActions.get(1);
            assertThat(addH2Dependency.getDependencies()).containsExactly(Dependency.builder().groupId("com.h2database").artifactId("h2").version("managed").scope("runtime").build());
            assertThatActionHasCondition(addH2Dependency, NoDependencyExistMatchingRegex.class);
            NoDependencyExistMatchingRegex noH2DependencyExist = getConditionFor(addH2Dependency, NoDependencyExistMatchingRegex.class);
            assertThat(noH2DependencyExist.getDependencies()).containsExactlyInAnyOrder("com.h2database:h2");


            // Action AddTypeAnnotationToTypeAnnotatedWith
            AddTypeAnnotationToTypeAnnotatedWith action = getAction(recipe, AddTypeAnnotationToTypeAnnotatedWith.class);
            assertThat(action.getAnnotatedWith()).isEqualTo("org.springframework.stereotype.Service");
            assertThat(action.getAnnotation()).isEqualTo("org.springframework.transaction.annotation.Transactional");

        }, Configuration.class);
    }
}
