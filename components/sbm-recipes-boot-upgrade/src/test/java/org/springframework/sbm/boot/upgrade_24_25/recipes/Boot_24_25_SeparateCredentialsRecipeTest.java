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
package org.springframework.sbm.boot.upgrade_24_25.recipes;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.test.RecipeIntegrationTestSupport;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class Boot_24_25_SeparateCredentialsRecipeTest {

    @Test
    void test() throws IOException {
        String applicationDir = "spring-boot-2.4-to-2.5-example";
        Path from = Path.of("./testcode").resolve(applicationDir).resolve("given");
        RecipeIntegrationTestSupport.initializeProject(from, applicationDir)
                .andApplyRecipe("boot-2.4-2.5-datasource-initializer");

        Path datasourceInitializer = RecipeIntegrationTestSupport.getResultDir(applicationDir).resolve("src/main/java/com/example/springboot24to25example/DataSourceInitializerConfiguration.java");
        Path applicationProperties = RecipeIntegrationTestSupport.getResultDir(applicationDir).resolve("src/main/resources/application.properties");

        String expectedApplicationProperties =
                "spring.jpa.hibernate.ddl-auto=none\n" +
                "spring.h2.console.enabled=true\n" +
                "spring.jackson.serialization.FAIL_ON_EMPTY_BEANS=false\n" +
                "spring.datasource.continue-on-error=false\n" +
                "spring.datasource.platform=h2\n" +
                "spring.datasource.schema-username=schema_user\n" +
                "spring.datasource.schema-password=schema_pw\n" +
                "spring.sql.init.username=data_user\n" +
                "spring.sql.init.password=data_pw\n";

        String expectedDatasourceInitializer =
                "package com.example.springboot24to25example;\n" +
                        "\n" +
                        "import org.springframework.beans.factory.annotation.Value;\n" +
                        "import org.springframework.boot.jdbc.DataSourceBuilder;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "import org.springframework.core.io.FileSystemResource;\n" +
                        "import org.springframework.jdbc.datasource.init.DataSourceInitializer;\n" +
                        "import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;\n" +
                        "\n" +
                        "import javax.sql.DataSource;\n" +
                        "\n" +
                        "@Configuration\n" +
                        "public class DataSourceInitializerConfiguration {\n" +
                        "\n" +
                        "    @Value(\"${spring.datasource.schema}\")\n" +
                        "    private String ddlSqlFilename;\n" +
                        "\n" +
                        "    @Value(\"${spring.datasource.schema-username}\")\n" +
                        "    private String ddlDbUsername;\n" +
                        "\n" +
                        "    @Value(\"${spring.datasource.schema-password}\")\n" +
                        "    private String ddlDbPassword;\n" +
                        "\n" +
                        "    @Bean\n" +
                        "    public DataSourceInitializer dataSourceInitializer() {\n" +
                        "        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();\n" +
                        "        DataSource ddlDataSource = dataSourceBuilder\n" +
                        "        .username(ddlDbUsername)\n" +
                        "        .password(ddlDbPassword)\n" +
                        "        .build();\n" +
                        "\n" +
                        "        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();\n" +
                        "        databasePopulator.addScript(new FileSystemResource(ddlSqlFilename));\n" +
                        "\n" +
                        "        DataSourceInitializer initializer = new DataSourceInitializer();\n" +
                        "        initializer.setDataSource(ddlDataSource);\n" +
                        "        initializer.setDatabasePopulator(databasePopulator);\n" +
                        "        initializer.afterPropertiesSet();\n" +
                        "        return initializer;\n" +
                        "    }\n" +
                        "\n" +
                        "}";

        assertThat(applicationProperties).hasContent(expectedApplicationProperties);
        assertThat(datasourceInitializer).hasContent(expectedDatasourceInitializer);
    }

}