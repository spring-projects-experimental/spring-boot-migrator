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
package org.springframework.sbm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.UserInteractions;
import org.springframework.sbm.parsers.RewriteExecutionContext;
import org.springframework.sbm.project.parser.ProjectContextInitializer;
import org.springframework.sbm.spring.migration.actions.InitDataSourceAfterJpaInitAction;
import org.springframework.sbm.test.ProjectContextFileSystemTestSupport;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SpringBoot23To24MigrationTest {

    @MockBean
    private UserInteractions ui;

    @Autowired
    private ProjectContextInitializer contextInitializer;

    @Autowired
    @Qualifier("migrateBoot23To24")
    private Recipe sut;


//    @Test
    @Disabled("use ProjectContextFileSystemTestSupport instead")
    public void recipeIsNotApplicableTo24App() throws IOException {
//        ProjectContext projectContext = useProject("boot-24-app").build();
//        assertThat(sut.isApplicable(projectContext)).isFalse();
    }

//    @Test
    @Disabled("use ProjectContextFileSystemTestSupport instead")
    public void recipeIsApplicableTo23App() throws IOException {
//        ProjectContext projectContext = useProject("boot-23-app").build();
//        assertThat(sut.isApplicable(projectContext)).isTrue();
    }

    @Test
    @Disabled("TODO: Make integration test")
    public void recipeUpdatesBootDependenciesAndParentVersion() throws IOException {

        String expectedPom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                + "	xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
                + "	<modelVersion>4.0.0</modelVersion>\n"
                + "	<parent>\n"
                + "		<groupId>org.springframework.boot</groupId>\n"
                + "		<artifactId>spring-boot-starter-parent</artifactId>\n"
                + "		<version>2.4.5</version>\n"
                + "		<relativePath/> <!-- lookup parent from repository -->\n"
                + "	</parent>\n"
                + "	<groupId>com.example</groupId>\n"
                + "	<artifactId>boot-23-app</artifactId>\n"
                + "	<version>0.0.1-SNAPSHOT</version>\n"
                + "	<name>boot-23-app</name>\n"
                + "	<description>Demo project for Spring Boot</description>\n"
                + "	<properties>\n"
                + "		<java.version>11</java.version>\n"
                + "	</properties>\n"
                + "	<dependencies>\n"
                + "		<dependency>\n"
                + "			<groupId>org.springframework.boot</groupId>\n"
                + "			<artifactId>spring-boot-starter-data-jpa</artifactId>\n"
                + "			<version>2.4.5</version>\n"
                + "		</dependency>\n" + "\n"
                + "		<dependency>\n"
                + "			<groupId>org.springframework.boot</groupId>\n"
                + "			<artifactId>spring-boot-starter-test</artifactId>\n"
                + "			<scope>test</scope>\n"
                + "			<version>2.4.5</version>\n"
                + "		</dependency>\n"
                + "	</dependencies>\n" + "\n"
                + "	<build>\n"
                + "		<plugins>\n"
                + "			<plugin>\n"
                + "				<groupId>org.springframework.boot</groupId>\n"
                + "				<artifactId>spring-boot-maven-plugin</artifactId>\n"
                + "			</plugin>\n"
                + "		</plugins>\n"
                + "	</build>\n"
                + "</project>\n";

    List<Resource> resources = List.of();

        ProjectContext projectContext = contextInitializer.initProjectContext(Path.of("./testcode/boot-23-app/given"), resources);
        projectContext.getApplicationModules().getRootModule().getMainResourceSet().addStringResource("src/main/resources/data.sql", "# Empty file");


        when(ui.askUserYesOrNo(InitDataSourceAfterJpaInitAction.QUESTION)).thenReturn(false);

//        SpringBootVersionMigrationConfig config = new SpringBootVersionMigrationConfig();
//        Recipe sut = config.migrateBoot23To24(ui);

        sut.apply(projectContext);

        assertThat(expectedPom).isEqualTo(projectContext.getBuildFile().print());

        SpringBootApplicationProperties applicationProperties = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter()).get(0);
        assertThat(applicationProperties.hasChanges()).isFalse();
        assertThat(applicationProperties.getProperty("spring.datasource.initialization-order")).isEmpty();

        verify(ui).askUserYesOrNo(InitDataSourceAfterJpaInitAction.QUESTION);
        verifyNoMoreInteractions(ui);
    }

//    @Test
    public void recipeJpaInitPropertyAdded() throws IOException {


        String expectedPom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                + "	xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
                + "	<modelVersion>4.0.0</modelVersion>\n"
                + "	<parent>\n"
                + "		<groupId>org.springframework.boot</groupId>\n"
                + "		<artifactId>spring-boot-starter-parent</artifactId>\n"
                + "		<version>2.4.5</version>\n"
                + "		<relativePath/> <!-- lookup parent from repository -->\n"
                + "	</parent>\n"
                + "	<groupId>com.example</groupId>\n"
                + "	<artifactId>boot-23-app</artifactId>\n"
                + "	<version>0.0.1-SNAPSHOT</version>\n"
                + "	<name>boot-23-app</name>\n"
                + "	<description>Demo project for Spring Boot</description>\n"
                + "	<properties>\n"
                + "		<java.version>11</java.version>\n"
                + "	</properties>\n"
                + "	<dependencies>\n"
                + "		<dependency>\n"
                + "			<groupId>org.springframework.boot</groupId>\n"
                + "			<artifactId>spring-boot-starter-data-jpa</artifactId>\n"
                + "			<version>2.4.5</version>\n"
                + "		</dependency>\n" + "\n"
                + "		<dependency>\n"
                + "			<groupId>org.springframework.boot</groupId>\n"
                + "			<artifactId>spring-boot-starter-test</artifactId>\n"
                + "			<scope>test</scope>\n"
                + "			<version>2.4.5</version>\n"
                + "		</dependency>\n"
                + "	</dependencies>\n" + "\n"
                + "	<build>\n"
                + "		<plugins>\n"
                + "			<plugin>\n"
                + "				<groupId>org.springframework.boot</groupId>\n"
                + "				<artifactId>spring-boot-maven-plugin</artifactId>\n"
                + "			</plugin>\n"
                + "		</plugins>\n"
                + "	</build>\n"
                + "\n"
                + "</project>\n";

        ProjectContext projectContext = ProjectContextFileSystemTestSupport.createProjectContextFromDir("boot-23-app");
        projectContext.getApplicationModules().getRootModule().getMainResourceSet().addStringResource("src/main/resources/data.sql", "# Empty file");

        UserInteractions ui = mock(UserInteractions.class);
        when(ui.askUserYesOrNo(InitDataSourceAfterJpaInitAction.QUESTION)).thenReturn(false);

        MigrateBoot23To24Recipe config = new MigrateBoot23To24Recipe();
        Recipe sut = config.migrateBoot23To24(ui);


        when(ui.askUserYesOrNo(InitDataSourceAfterJpaInitAction.QUESTION)).thenReturn(true);
        sut.apply(projectContext);
        assertEquals(
                expectedPom,
                projectContext.getBuildFile().print()
        );

        SpringBootApplicationProperties applicationProperties = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter()).get(0);
        assertThat(applicationProperties.getProperty("spring.datasource.initialization-order").get()).isEqualTo("after-jpa");

        verify(ui).askUserYesOrNo(InitDataSourceAfterJpaInitAction.QUESTION);
        verifyNoMoreInteractions(ui);
    }

//    @Test
    @Disabled("use ProjectContextFileSystemTestSupport instead")
    public void recipeJpaInitPropertyAddedForSchemaSql() throws IOException {
//        when(ui.askUserYesOrNo(InitDataSourceAfterJpaInitAction.QUESTION)).thenReturn(true);
//
//        ProjectContext projectContext = useProject("boot-23-app")
//                .withFile("src/main/resources/schema.sql", "# Empty file")
//                .build();
//
//        sut.apply(projectContext);
//
//        SpringBootApplicationProperties applicationProperties = projectContext.getFilteredResources(new SpringBootApplicationPropertiesResourceListFilter()).get(0);
//        assertThat(applicationProperties.getProperty("spring.datasource.initialization-order").get()).isEqualTo("after-jpa");
//
//        verify(ui).askUserYesOrNo(InitDataSourceAfterJpaInitAction.QUESTION);
//        verifyNoMoreInteractions(ui);
    }
//
//    private ProjectCustomizer useProject(String appName) throws IOException {
//    	return Utils.useProject(appName, pcBuilder);
//    }

}
