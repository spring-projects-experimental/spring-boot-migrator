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
package org.springframework.sbm.sccs;

import org.springframework.sbm.test.RecipeIntegrationTestSupport;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrateToSpringCloudConfigServerIntegrationTest {

    String applicationDir = "sccs-client";
    private String applicationConfigDir = "sccs-client-config";

    @BeforeEach
    void beforeEach() throws IOException {
        FileUtils.deleteDirectory(Path.of("./target/testcode/").toFile());
    }

    @Test
    void recipeTest() throws IOException {

        Path given = Path.of("./testcode").resolve(applicationDir).resolve("given");

        RecipeIntegrationTestSupport.initializeProject(given, applicationDir)
                .andApplyRecipe("cn-spring-cloud-config-server");


        // verify migrated project
        Path migratedProjectDir = RecipeIntegrationTestSupport.getResultDir(applicationDir);

        // application-foo.properties removed from migrated project
        assertThat(migratedProjectDir.resolve("src/main/resources/application-foo.properties")).doesNotExist();

        // application.properties has connection property to config server
        assertThat(migratedProjectDir.resolve("src/main/resources/application.properties"))
                .usingCharset(StandardCharsets.UTF_8).hasContent(
                "spring.application.name=sccs-client\n" +
                        "spring.profiles.active=default,foo\n" +
                        "property1=yes\n" +
                        "property2=no\n" +
                        "spring.config.import=optional:configserver:http://localhost:8888"
        );

        // dependencies for spring cloud config server were added
        // dependencyManagement contains spring cloud dependencies
        assertThat(migratedProjectDir.resolve("pom.xml")).hasSameTextualContentAs(Path.of("testcode/sccs-client/expected/pom.xml"));

        // verify config project created and initialized
        Path migratedProjectConfigDir = RecipeIntegrationTestSupport.getResultDir(applicationConfigDir);
        // config project was created
        assertThat(migratedProjectConfigDir).exists();
        // config project contains application.properties and application-foo.properties
        assertThat(migratedProjectConfigDir.resolve("application.properties")).exists();
        assertThat(migratedProjectConfigDir.resolve("application.properties")).hasContent(
                "spring.application.name=sccs-client\n" +
                        "spring.profiles.active=default,foo\n" +
                        "property1=yes\n" +
                        "property2=no"
        );
        // git initialized for config project and property files were committed
        assertThat(migratedProjectConfigDir.resolve("application-foo.properties")).exists();
        assertThat(migratedProjectConfigDir.resolve("application-foo.properties")).hasContent(
                "property2=foobar"
        );

    }
}
