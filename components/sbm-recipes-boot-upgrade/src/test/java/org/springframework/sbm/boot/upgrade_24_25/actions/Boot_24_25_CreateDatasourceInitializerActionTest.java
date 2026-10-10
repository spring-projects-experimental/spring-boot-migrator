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

import freemarker.template.Configuration;
import freemarker.template.Version;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.common.filter.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class Boot_24_25_CreateDatasourceInitializerActionTest {

    private static final Pattern VALUE_PLACEHOLDER_PATTERN = Pattern.compile("@Value\\(\"\\$\\{([^\"]+)}\"\\)");

    private final Boot_24_25_CreateDatasourceInitializerAction sut = new Boot_24_25_CreateDatasourceInitializerAction();
    private ProjectContext context;

    @BeforeEach
    void setUp() throws IOException {
        Configuration templateConfiguration = new Configuration(new Version("2.3.0"));
        templateConfiguration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
        ReflectionTestUtils.setField(sut, "configuration", templateConfiguration);

        context = TestProjectContext.buildProjectContext()
                .withJavaSources("package com.example.demo;\npublic class DemoApplication {}")
                .withProjectResource("src/main/resources/application.properties", """
                        spring.datasource.continue-on-error=false
                        spring.datasource.platform=h2
                        spring.datasource.schema=classpath:schema.sql
                        spring.datasource.schema-username=schema_user
                        spring.datasource.schema-password=schema_pw
                        """)
                .withProjectResource("src/main/resources/schema.sql", "CREATE TABLE TAG (ID BIGINT PRIMARY KEY);")
                .withProjectResource("src/main/resources/data.sql", "INSERT INTO TAG VALUES (1);")
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .build();
    }

    @Test
    void generatedConfigurationUsesPropertiesThatSurviveTheSqlInitPropertiesRecipe() {
        sut.apply(context);
        new Boot_24_25_SqlScriptDataSourceInitializationAction().apply(context);

        String generatedConfiguration = readGeneratedConfiguration();
        List<String> referencedProperties = referencedProperties(generatedConfiguration);

        assertThat(referencedProperties).isNotEmpty();
        SpringBootApplicationProperties properties = applicationProperties();
        referencedProperties.forEach(property ->
                assertThat(properties.getProperty(property))
                        .as("property '%s' used in DataSourceInitializerConfiguration", property)
                        .isPresent());
    }

    @Test
    void generatedConfigurationAndPropertiesUseSqlInitPropertyNames() {
        sut.apply(context);

        String generatedConfiguration = readGeneratedConfiguration();

        assertThat(generatedConfiguration).contains(
                "@Value(\"${spring.sql.init.schema-locations}\")",
                "@Value(\"${spring.sql.init.username}\")",
                "@Value(\"${spring.sql.init.password}\")");

        SpringBootApplicationProperties properties = applicationProperties();
        assertThat(properties.getProperty("spring.sql.init.schema-locations")).contains("classpath:schema.sql");
        assertThat(properties.getProperty("spring.sql.init.username")).contains("schema_user");
        assertThat(properties.getProperty("spring.sql.init.password")).contains("schema_pw");
        assertThat(properties.getProperty("spring.datasource.schema")).isEmpty();
        assertThat(properties.getProperty("spring.datasource.schema-username")).isEmpty();
        assertThat(properties.getProperty("spring.datasource.schema-password")).isEmpty();
    }

    @Test
    void sqlInitPropertiesRecipeMigratesSchemaCredentialsToSqlInitUsernameAndPassword() {
        sut.apply(context);
        new Boot_24_25_SqlScriptDataSourceInitializationAction().apply(context);

        SpringBootApplicationProperties properties = applicationProperties();
        assertThat(properties.getProperty("spring.sql.init.schema-locations")).contains("classpath:schema.sql");
        assertThat(properties.getProperty("spring.sql.init.username")).contains("schema_user");
        assertThat(properties.getProperty("spring.sql.init.password")).contains("schema_pw");
    }

    @Test
    void dataPasswordPropertyIsMigratedTogetherWithDataUsername() {
        ProjectContext projectContext = buildContext("""
                spring.datasource.data-username=data_user
                spring.datasource.data-password=data_pw
                """);
        sut.apply(projectContext);

        SpringBootApplicationProperties properties = propertiesOf(projectContext);
        assertThat(properties.getProperty("spring.sql.init.username")).contains("data_user");
        assertThat(properties.getProperty("spring.sql.init.password")).contains("data_pw");
        assertThat(properties.getProperty("spring.datasource.data-username")).isEmpty();
        assertThat(properties.getProperty("spring.datasource.data-password")).isEmpty();
    }

    @Test
    void conflictingSchemaAndDataCredentialsAreLeftUntouched() {
        ProjectContext projectContext = buildContext("""
                spring.datasource.schema-username=schema_user
                spring.datasource.schema-password=schema_pw
                spring.datasource.data-username=data_user
                spring.datasource.data-password=data_pw
                """);
        sut.apply(projectContext);

        SpringBootApplicationProperties properties = propertiesOf(projectContext);
        assertThat(properties.getProperty("spring.datasource.schema-username")).contains("schema_user");
        assertThat(properties.getProperty("spring.datasource.schema-password")).contains("schema_pw");
        assertThat(properties.getProperty("spring.datasource.data-username")).contains("data_user");
        assertThat(properties.getProperty("spring.datasource.data-password")).contains("data_pw");
        assertThat(properties.getProperty("spring.sql.init.username")).isEmpty();
        assertThat(properties.getProperty("spring.sql.init.password")).isEmpty();
    }

    private ProjectContext buildContext(String applicationPropertiesLines) {
        return TestProjectContext.buildProjectContext()
                .withJavaSources("package com.example.demo;\npublic class DemoApplication {}")
                .withProjectResource("src/main/resources/application.properties", applicationPropertiesLines)
                .withProjectResource("src/main/resources/schema.sql", "CREATE TABLE TAG (ID BIGINT PRIMARY KEY);")
                .withProjectResource("src/main/resources/data.sql", "INSERT INTO TAG VALUES (1);")
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .build();
    }

    private SpringBootApplicationProperties propertiesOf(ProjectContext projectContext) {
        return projectContext.search(new SpringBootApplicationPropertiesResourceListFilter()).get(0);
    }

    private String readGeneratedConfiguration() {
        List<ProjectResource> configurations = context.search(new PathPatternMatchingProjectResourceFinder("/**/DataSourceInitializerConfiguration.java"));
        assertThat(configurations).hasSize(1);
        return configurations.get(0).print();
    }

    private List<String> referencedProperties(String generatedConfiguration) {
        Matcher matcher = VALUE_PLACEHOLDER_PATTERN.matcher(generatedConfiguration);
        List<String> referencedProperties = new ArrayList<>();
        while (matcher.find()) {
            referencedProperties.add(matcher.group(1));
        }
        return referencedProperties;
    }

    private SpringBootApplicationProperties applicationProperties() {
        return context.search(new SpringBootApplicationPropertiesResourceListFilter()).get(0);
    }

}
