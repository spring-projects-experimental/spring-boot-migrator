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

import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.migration.actions.AddDependencies;
import org.springframework.sbm.build.migration.actions.RemoveDependenciesMatchingRegex;
import org.springframework.sbm.build.migration.conditions.NoDependencyExistMatchingRegex;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.java.migration.actions.AddTypeAnnotationToTypeAnnotatedWith;
import org.springframework.sbm.java.migration.actions.ReplaceTypeAction;
import org.springframework.sbm.java.migration.conditions.HasAnnotation;
import org.springframework.sbm.java.migration.conditions.HasImportStartingWith;
import org.springframework.sbm.java.migration.conditions.HasMemberAnnotation;
import org.springframework.sbm.java.migration.conditions.HasTypeAnnotation;
import org.springframework.sbm.jee.ejb.actions.MigrateEjbAnnotations;
import org.springframework.sbm.jee.ejb.actions.MigrateLocalStatelessSessionBeans;
import org.springframework.sbm.jee.ejb.conditions.NoTransactionalAnnotationPresentOnTypeAnnotatedWith;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.springframework.sbm.test.RecipeTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MigrateStatelessEjbRecipeTest {

    @Test
    void migrateStatelessEjbRecipe() {
        testRecipe(Path.of("recipes/migrate-stateless-ejb.yaml"), recipes -> {
            Optional<Recipe> recipe = recipes.getRecipeByName("migrate-stateless-ejb");

            assertThatRecipeExists(recipe);

            assertThatRecipeHasCondition(recipe, HasImportStartingWith.class);

            assertThatRecipeHasActions(recipe,
                    MigrateEjbAnnotations.class,
                    ReplaceTypeAction.class,
                    ReplaceTypeAction.class,
                    MigrateLocalStatelessSessionBeans.class,
                    RemoveDependenciesMatchingRegex.class,
                    AddDependencies.class,
                    AddTypeAnnotationToTypeAnnotatedWith.class);

            MigrateEjbAnnotations migrateEjbAnnotations = getAction(recipe, MigrateEjbAnnotations.class);
            HasAnnotation hasAnnotation = getConditionFor(migrateEjbAnnotations, HasAnnotation.class);
            assertThat(hasAnnotation.getAnnotation()).isEqualTo("javax.ejb.EJB");

            List<ReplaceTypeAction> replaceTypeActions = getActions(recipe, ReplaceTypeAction.class);

            assertThatActionHasCondition(replaceTypeActions.get(0), HasMemberAnnotation.class);
            HasMemberAnnotation condition2 = getConditionFor(replaceTypeActions.get(0), HasMemberAnnotation.class);
            assertThat(condition2.getAnnotation()).isEqualTo("javax.inject.Inject");
            assertThat(replaceTypeActions.get(0).getAnnotation()).isEqualTo("javax.inject.Inject");
            assertThat(replaceTypeActions.get(0).getWithAnnotation()).isEqualTo("org.springframework.beans.factory.annotation.Autowired");

            assertThatActionHasCondition(replaceTypeActions.get(1), HasTypeAnnotation.class);
            HasTypeAnnotation condition4 = getConditionFor(replaceTypeActions.get(1), HasTypeAnnotation.class);
            assertThat(condition4.getAnnotation()).isEqualTo("javax.ejb.Startup");
            assertThat(replaceTypeActions.get(1).getAnnotation()).isEqualTo("javax.ejb.Startup");
            assertThat(replaceTypeActions.get(1).getWithAnnotation()).isEqualTo("org.springframework.stereotype.Service");

            MigrateLocalStatelessSessionBeans migrateLocalStatelessSessionBeans = getAction(recipe, MigrateLocalStatelessSessionBeans.class);
            assertThatActionHasCondition(migrateLocalStatelessSessionBeans, HasTypeAnnotation.class);
            HasTypeAnnotation condition6 = getConditionFor(migrateLocalStatelessSessionBeans, HasTypeAnnotation.class);
            assertThat(condition6.getAnnotation()).isEqualTo("javax.ejb.Stateless");

            RemoveDependenciesMatchingRegex removeDependenciesAction = getAction(recipe, RemoveDependenciesMatchingRegex.class);
            assertThat(removeDependenciesAction.getDependenciesRegex()).containsExactlyInAnyOrder(
                    "javax\\:javaee-api.*",
                    "javax\\.ejb\\:javax\\.ejb-api\\:.*",
                    "org\\.jboss\\.spec\\.javax\\.ejb\\:jboss-ejb-api_3.*");

            AddDependencies addDependencies = getAction(recipe, AddDependencies.class);
            assertThat(addDependencies.getDependencies()).containsExactly(Dependency.builder().groupId("org.springframework.boot").artifactId("spring-boot-starter-data-jpa").version("managed").build());
            NoDependencyExistMatchingRegex noDependencyExistMatchingRegex = getConditionFor(addDependencies, NoDependencyExistMatchingRegex.class);
            assertThat(noDependencyExistMatchingRegex.getDependencies()).containsExactly("org.springframework.boot:spring-boot-starter-data-jpa");

            AddTypeAnnotationToTypeAnnotatedWith addTransactionalAnnotationToTypeAnnotatedWith = getAction(recipe, AddTypeAnnotationToTypeAnnotatedWith.class);
            assertThat(addTransactionalAnnotationToTypeAnnotatedWith.getAnnotatedWith()).isEqualTo("org.springframework.stereotype.Service");
            assertThat(addTransactionalAnnotationToTypeAnnotatedWith.getAnnotation()).isEqualTo("org.springframework.transaction.annotation.Transactional");
            assertThat(addTransactionalAnnotationToTypeAnnotatedWith.isAddAnnotationIfExists()).isFalse();
            assertThatActionHasCondition(addTransactionalAnnotationToTypeAnnotatedWith, NoTransactionalAnnotationPresentOnTypeAnnotatedWith.class);
            NoTransactionalAnnotationPresentOnTypeAnnotatedWith hasServiceAnnotation = getConditionFor(addTransactionalAnnotationToTypeAnnotatedWith, NoTransactionalAnnotationPresentOnTypeAnnotatedWith.class);
            assertThat(hasServiceAnnotation.getTypeAnnotatedWith()).isEqualTo("org.springframework.stereotype.Service");
        });
    }
}