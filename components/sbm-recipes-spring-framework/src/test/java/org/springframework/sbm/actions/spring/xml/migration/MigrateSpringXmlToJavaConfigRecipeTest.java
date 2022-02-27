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
package org.springframework.sbm.actions.spring.xml.migration;

import org.springframework.sbm.build.migration.actions.AddDependencies;
import org.springframework.sbm.build.migration.actions.RemoveDependenciesMatchingRegex;
import org.springframework.sbm.test.RecipeTestSupport;
import org.springframework.sbm.conditions.xml.XmlFileContaining;
import org.springframework.sbm.engine.recipe.Recipe;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrateSpringXmlToJavaConfigRecipeTest {

    @Test
    // TODO: add mvn clean install as actions or remove commented out code
    void recipe() {
        RecipeTestSupport.testRecipe(Path.of("recipes/migrate-spring-xml-to-java-config.yaml"), recipes -> {
            Optional<Recipe> recipe = recipes.getRecipeByName("migrate-spring-xml-to-java-config");
            RecipeTestSupport.assertThatRecipeExists(recipe);

            RecipeTestSupport.assertThatRecipeHasActions(recipe,
                    MigrateXmlToJavaConfigurationAction.class,
                    AddDependencies.class,
                    RemoveDependenciesMatchingRegex.class
            );
            MigrateXmlToJavaConfigurationAction action = RecipeTestSupport.getAction(recipe, MigrateXmlToJavaConfigurationAction.class);
            assertThat(action.getDescription()).isEqualTo("Migrate xml bean configuration to Java bean configuration.");
            RecipeTestSupport.assertThatActionHasCondition(action, XmlFileContaining.class);
            XmlFileContaining xmlFileContaining = RecipeTestSupport.getConditionFor(action, XmlFileContaining.class);
            assertThat(xmlFileContaining.getValue()).isEqualTo("www.springframework.org/schema");
        },
                // TODO: retrieve beans always from one(!) location
                MigrationContextFactory.class,
                MigrateXmlToJavaConfigurationActionHelper.class,
                Helper.class,
                GenericBeanHandler.class,
                ListBeanHandler.class,
                XmlToJavaConfigurationMigration.class,
                BeanPropertySetterStatementFactory.class,
                BeanConstructorCallFactory.class,
                BeanMethodFactory.class
        );
    }
}
