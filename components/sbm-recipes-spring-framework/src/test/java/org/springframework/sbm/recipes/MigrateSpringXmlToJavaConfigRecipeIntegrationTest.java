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

import org.springframework.sbm.test.RecipeIntegrationTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrateSpringXmlToJavaConfigRecipeIntegrationTest {

    @Test
    @Tag("integration")
    @Disabled("FIXME: repair test")
    void migrate_standalone_spring_application() throws IOException {
        String recipeName = "migrate-spring-xml-to-java-config";
        String applicationDir = "standalone-spring-application";
        Path givenApplication = Path.of("./testcode/spring-xml-java-config/" + applicationDir + "/given");
        Path expectedResultDir = Path.of("./testcode/spring-xml-java-config/" + applicationDir + "/expected");

        // apply recipe
        RecipeIntegrationTestSupport
                .initializeProject(givenApplication, applicationDir)
                .andApplyRecipe(recipeName);

        // verify
        Path migrationResultDir = RecipeIntegrationTestSupport.getResultDir(applicationDir);

        // xml application context became java config
        assertThat(migrationResultDir.resolve("src/main/java/it/dontesta/spring/example/ApplicationContext.java"))
                .hasSameTextualContentAs(expectedResultDir.resolve("src/main/java/it/dontesta/spring/example/ApplicationContext.java"));

        // xml application context has been removed
        assertThat(migrationResultDir.resolve("src/main/resources/applicationContext.xml"))
                .doesNotExist();

        // database.properties merged into application.properties
        assertThat(migrationResultDir.resolve("src/main/resources/database.properties"))
                .doesNotExist();
        // application.properties now contains properties from database.properties
        Properties properties = new Properties();
        properties.load(new FileSystemResource(migrationResultDir.resolve("src/main/resources/application.properties")).getInputStream());
        assertThat(properties.getProperty("jdbc.driverClassName")).isEqualTo("org.h2.Driver");
        assertThat(properties.getProperty("jdbc.url")).isEqualTo("jdbc:h2:mem:test");
        assertThat(properties.getProperty("jdbc.username")).isEqualTo("sa");
        assertThat(properties.getProperty("jdbc.password")).isEqualTo("");
    }

    @Test
    @Disabled("FIXME: reactivate and add assertions")
    void migrate_spring_petclinic() throws IOException {
        String recipeName = "migrate-spring-xml-to-java-config";
        String applicationDir = "spring-petclinic";
        Path givenApplication = Path.of("./testcode/spring-xml-java-config/" + applicationDir + "/given");
        Path expectedResultDir = Path.of("./testcode/spring-xml-java-config/" + applicationDir + "/expected");

        // apply recipe
        RecipeIntegrationTestSupport
                .initializeProject(givenApplication, applicationDir)
                .andApplyRecipe(recipeName);
    }

    @Test
    @Disabled("see #117")
    void test_simple_spring_webapp() throws IOException {
        String recipeName = "migrate-spring-xml-to-java-config";
        String applicationDir = "simple-spring-webapp";
        Path givenApplication = Path.of("./testcode/spring-xml-java-config/" + applicationDir + "/given");
        Path expectedResultDir = Path.of("./testcode/spring-xml-java-config/" + applicationDir + "/expected");

        // apply recipe
        RecipeIntegrationTestSupport
                .initializeProject(givenApplication, applicationDir)
                .andApplyRecipe(recipeName);
    }

    @Test
    @Disabled("FIXME please!")
    void test_classloader_example() throws IOException {
        String recipeName = "migrate-spring-xml-to-java-config";
        String applicationDir = "spring-xml-app";
        Path givenApplication = Path.of("./testcode/spring-xml-java-config/" + applicationDir + "/given");
        Path expectedResultDir = Path.of("./testcode/spring-xml-java-config/" + applicationDir + "/expected");

        // apply recipe
        RecipeIntegrationTestSupport
                .initializeProject(givenApplication, applicationDir)
                .andApplyRecipe(recipeName);
    }

}
