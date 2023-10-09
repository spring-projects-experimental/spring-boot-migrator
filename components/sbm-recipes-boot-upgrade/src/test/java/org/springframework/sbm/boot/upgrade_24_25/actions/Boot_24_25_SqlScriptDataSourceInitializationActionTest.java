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
package org.springframework.sbm.boot.upgrade_24_25.actions;

import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.parsers.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Boot_24_25_SqlScriptDataSourceInitializationActionTest {

    @Test
    void apply_withAllPropertiesInOneFile() {
        String applicationPropertiesLines =
                "spring.datasource.continue-on-error=spring.sql.init.continue-on-error\n" +
                "spring.datasource.data=spring.sql.init.data-locations\n" +
                "spring.datasource.sql-script-encoding=spring.sql.init.encoding\n" +
                "spring.datasource.initialization-mode=spring.sql.init.mode\n" +
                "spring.datasource.platform=spring.sql.init.platform\n" +
                "spring.datasource.schema=spring.sql.init.schema-locations\n" +
                "spring.datasource.separator=spring.sql.init.separator";

        ProjectContext projectContext = getProjectContextSinglePropertiesFile(applicationPropertiesLines);

        Boot_24_25_SqlScriptDataSourceInitializationAction sut = new Boot_24_25_SqlScriptDataSourceInitializationAction();
        sut.apply(projectContext);

        List<SpringBootApplicationProperties> filteredResources = projectContext.search(new SpringBootApplicationPropertiesResourceListFinder());
        SpringBootApplicationProperties properties = filteredResources.get(0);

        assertThat(properties.getProperty("spring.sql.init.continue-on-error").get()).isEqualTo("spring.sql.init.continue-on-error");
        assertThat(properties.getProperty("spring.sql.init.data-locations").get()).isEqualTo("spring.sql.init.data-locations");
        assertThat(properties.getProperty("spring.sql.init.encoding").get()).isEqualTo("spring.sql.init.encoding");
        assertThat(properties.getProperty("spring.sql.init.mode").get()).isEqualTo("spring.sql.init.mode");
        assertThat(properties.getProperty("spring.sql.init.platform").get()).isEqualTo("spring.sql.init.platform");
        assertThat(properties.getProperty("spring.sql.init.schema-locations").get()).isEqualTo("spring.sql.init.schema-locations");
        assertThat(properties.getProperty("spring.sql.init.separator").get()).isEqualTo("spring.sql.init.separator");
    }

    @Test
    void apply_withPropertiesInTwoFiles() {
        String applicationPropertiesLines =
                "spring.datasource.continue-on-error=spring.sql.init.continue-on-error\n" +
                "spring.datasource.separator=spring.sql.init.separator";

        ProjectContext projectContext = getProjectContextTwoPropertiesFile(applicationPropertiesLines);

        Boot_24_25_SqlScriptDataSourceInitializationAction sut = new Boot_24_25_SqlScriptDataSourceInitializationAction();
        sut.apply(projectContext);

        SpringBootApplicationProperties properties = projectContext.search(new SpringBootApplicationPropertiesResourceListFinder()).get(0);

        assertThat(properties.getProperty("spring.sql.init.continue-on-error").get()).isEqualTo("spring.sql.init.continue-on-error");
        assertThat(properties.getProperty("spring.sql.init.separator").get()).isEqualTo("spring.sql.init.separator");

        SpringBootApplicationProperties properties2 = projectContext.search(new SpringBootApplicationPropertiesResourceListFinder()).get(1);
        assertThat(properties2.getProperty("spring.sql.init.continue-on-error").get()).isEqualTo("spring.sql.init.continue-on-error");
        assertThat(properties2.getProperty("spring.sql.init.separator").get()).isEqualTo("spring.sql.init.separator");
    }

    @Test
    void apply_withOnlySchemaCredentials() {
        String applicationPropertiesLines =
                "spring.datasource.schema-password=spring.sql.init.password\n" +
                "spring.datasource.schema-username=spring.sql.init.username";

        ProjectContext projectContext = getProjectContextSinglePropertiesFile(applicationPropertiesLines);

        Boot_24_25_SqlScriptDataSourceInitializationAction sut = new Boot_24_25_SqlScriptDataSourceInitializationAction();
        sut.apply(projectContext);

        SpringBootApplicationProperties properties = projectContext.search(new SpringBootApplicationPropertiesResourceListFinder()).get(0);

        assertThat(properties.getProperty("spring.sql.init.password").get()).isEqualTo("spring.sql.init.password");
        assertThat(properties.getProperty("spring.sql.init.username").get()).isEqualTo("spring.sql.init.username");
    }

    @Test
    void apply_withOnlyDataCredentials() {
        String applicationPropertiesLines =
                "spring.datasource.data-password=spring.sql.init.password\n" +
                "spring.datasource.data-username=spring.sql.init.username";

        ProjectContext projectContext = getProjectContextSinglePropertiesFile(applicationPropertiesLines);

        Boot_24_25_SqlScriptDataSourceInitializationAction sut = new Boot_24_25_SqlScriptDataSourceInitializationAction();
        sut.apply(projectContext);

        SpringBootApplicationProperties properties = projectContext.search(new SpringBootApplicationPropertiesResourceListFinder()).get(0);

        assertThat(properties.getProperty("spring.sql.init.password").get()).isEqualTo("spring.sql.init.password");
        assertThat(properties.getProperty("spring.sql.init.username").get()).isEqualTo("spring.sql.init.username");
    }

    @Test
    void apply_withSchemaAndDateCredentialsButSameName() {
        String applicationPropertiesLines =
                "spring.datasource.data-password=spring.sql.init.password\n" +
                "spring.datasource.data-username=spring.sql.init.username\n" +
                "spring.datasource.schema-password=spring.sql.init.password\n" +
                "spring.datasource.schema-username=spring.sql.init.username";

        ProjectContext projectContext = getProjectContextSinglePropertiesFile(applicationPropertiesLines);

        Boot_24_25_SqlScriptDataSourceInitializationAction sut = new Boot_24_25_SqlScriptDataSourceInitializationAction();
        sut.apply(projectContext);

        SpringBootApplicationProperties properties = projectContext.search(new SpringBootApplicationPropertiesResourceListFinder()).get(0);

        assertThat(properties.getProperty("spring.sql.init.password").get()).isEqualTo("spring.sql.init.password");
        assertThat(properties.getProperty("spring.sql.init.username").get()).isEqualTo("spring.sql.init.username");
    }

    @Test
    void apply_withSchemaAndDateCredentialsButDifferentNames() {
        String applicationPropertiesLines =
                "spring.datasource.data-password=spring.sql.init.password\n" +
                "spring.datasource.data-username=NAME_1\n" +
                "spring.datasource.schema-password=spring.sql.init.password\n" +
                "spring.datasource.schema-username=NAME_2";

        ProjectContext projectContext = getProjectContextSinglePropertiesFile(applicationPropertiesLines);

        Boot_24_25_SqlScriptDataSourceInitializationAction sut = new Boot_24_25_SqlScriptDataSourceInitializationAction();
        sut.apply(projectContext);

        SpringBootApplicationProperties properties = projectContext.search(new SpringBootApplicationPropertiesResourceListFinder()).get(0);

        assertThat(properties.getProperty("spring.datasource.data-password").get()).isEqualTo("spring.sql.init.password");
        assertThat(properties.getProperty("spring.datasource.data-username").get()).isEqualTo("NAME_1");
        assertThat(properties.getProperty("spring.datasource.schema-password").get()).isEqualTo("spring.sql.init.password");
        assertThat(properties.getProperty("spring.datasource.schema-username").get()).isEqualTo("NAME_2");
    }

    @Test
    void apply_withSchemaAndDateCredentialsButDifferentNamesInTwoFiles() {
        String applicationPropertiesLines1 =
                "spring.datasource.data-password=spring.sql.init.password\n" +
                        "spring.datasource.data-username=NAME_1";

        String applicationPropertiesLines2 =
                        "spring.datasource.schema-password=spring.sql.init.password\n" +
                        "spring.datasource.schema-username=NAME_2";


        ProjectContext projectContext = getProjectContextTwoPropertiesFile(applicationPropertiesLines1, applicationPropertiesLines2);

        Boot_24_25_SqlScriptDataSourceInitializationAction sut = new Boot_24_25_SqlScriptDataSourceInitializationAction();
        sut.apply(projectContext);

        SpringBootApplicationProperties properties = projectContext.search(new SpringBootApplicationPropertiesResourceListFinder()).get(0);
        assertThat(properties.getProperty("spring.datasource.data-password").get()).isEqualTo("spring.sql.init.password");
        assertThat(properties.getProperty("spring.datasource.data-username").get()).isEqualTo("NAME_1");
        SpringBootApplicationProperties properties2 = projectContext.search(new SpringBootApplicationPropertiesResourceListFinder()).get(1);
        assertThat(properties2.getProperty("spring.datasource.schema-password").get()).isEqualTo("spring.sql.init.password");
        assertThat(properties2.getProperty("spring.datasource.schema-username").get()).isEqualTo("NAME_2");
    }

    private ProjectContext getProjectContextTwoPropertiesFile(String applicationPropertiesLines1, String applicationPropertiesLines2) {
        Path rootDirectory = Path.of("./dummy");
        String applicationPropertiesPath = "src/main/resources/application.properties";
        String applicationPropertiesPathTest = "src/main/resources/application-test.properties";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectRoot(rootDirectory)
                .withProjectResource(applicationPropertiesPath, applicationPropertiesLines1)
                .withProjectResource(applicationPropertiesPathTest, applicationPropertiesLines2)
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .build();

        return projectContext;
    }

    private ProjectContext getProjectContextSinglePropertiesFile(String applicationPropertiesLines) {
        Path rootDirectory = Path.of("./dummy");
        String applicationPropertiesPath = "src/main/resources/application.properties";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectRoot(rootDirectory)
                .withProjectResource(applicationPropertiesPath, applicationPropertiesLines)
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .build();

        return projectContext;
    }

    private ProjectContext getProjectContextTwoPropertiesFile(String applicationPropertiesLines) {
        Path rootDirectory = Path.of("./dummy");
        String applicationPropertiesPath = "src/main/resources/application.properties";
        String applicationPropertiesPathTest = "src/main/resources/application-test.properties";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectRoot(rootDirectory)
                .withProjectResource(applicationPropertiesPath, applicationPropertiesLines)
                .withProjectResource(applicationPropertiesPathTest, applicationPropertiesLines)
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .build();

        return projectContext;
    }

}