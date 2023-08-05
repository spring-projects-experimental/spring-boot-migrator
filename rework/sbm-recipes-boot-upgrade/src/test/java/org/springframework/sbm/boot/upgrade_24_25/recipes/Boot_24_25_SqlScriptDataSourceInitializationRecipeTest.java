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
package org.springframework.sbm.boot.upgrade_24_25.recipes;

import org.junit.jupiter.api.Disabled;
import org.springframework.sbm.test.RecipeIntegrationTestSupport;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class Boot_24_25_SqlScriptDataSourceInitializationRecipeTest {

    @Test
    @Tag("integration")
    @Disabled("FIXME: #7 flaky test, fix later")
    void testRecipe() throws IOException {
        String applicationDir = "spring-boot-2.4-to-2.5-example";
        Path from = Path.of("./testcode").resolve(applicationDir).resolve("given");
        RecipeIntegrationTestSupport.initializeProject(from, applicationDir)
                .andApplyRecipe("boot-2.4-2.5-sql-init-properties");

        Path resultDir = RecipeIntegrationTestSupport.getResultDir(applicationDir).resolve("src/main/resources/application.properties");
        assertThat(resultDir.toFile()).hasContent(
                "spring.jpa.hibernate.ddl-auto=none\n" +
                "spring.h2.console.enabled=true\n" +
                "spring.jackson.serialization.FAIL_ON_EMPTY_BEANS=false\n" +
                "spring.sql.init.continue-on-error=false\n" +
                "spring.sql.init.platform=h2\n" +
                "spring.datasource.schema-username=schema_user\n" +
                "spring.datasource.schema-password=schema_pw\n" +
                "spring.datasource.data-username=data_user\n" +
                "spring.datasource.data-password=data_pw\n"
        );
    }
}
