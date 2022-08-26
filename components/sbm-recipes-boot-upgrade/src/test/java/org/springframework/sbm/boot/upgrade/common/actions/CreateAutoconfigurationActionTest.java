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
package org.springframework.sbm.boot.upgrade.common.actions;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.common.filter.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateAutoconfigurationActionTest {

    private ProjectContext context;
    private CreateAutoconfigurationAction action;

    private final static String NEW_AUTO_CONFIG_FILE = "/**/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports";
    private final static String EXISTING_SPRING_FACTORIES_FILE = "/**/src/main/resources/META-INF/spring.factories";

    @BeforeEach
    public void setup() {
        context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        """
                                hello.world=something
                                org.springframework.boot.autoconfigure.EnableAutoConfiguration=XYZ
                                """
                )
                .build();
        action = new CreateAutoconfigurationAction();
    }

    @Test
    public void autoConfigurationImportsIsGenerated() {
        action.apply(context);

        assertThat(getFileAsProjectResource(NEW_AUTO_CONFIG_FILE)).hasSize(1);
    }

    @Test
    public void autoConfigurationImportsContent() {
        action.apply(context);

        String content = getNewAutoConfigFileContents();

        assertThat(content).isEqualTo("XYZ");
    }

    @Test
    public void itDeletesSourceWhenMovedToNewFile() {
        action.apply(context);

        String content = getSpringFactoryFile();

        assertThat(content).isEqualTo("""
                hello.world=something
                """);
    }

    @Test
    public void shouldMoveMultipleProperties() {
        context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        """
                                hello.world=something
                                org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
                                XYZ,\
                                ABC,\
                                DEF
                                """
                )
                .build();

        action.apply(context);

        String content = getNewAutoConfigFileContents();

        assertThat(content).isEqualTo("""
                XYZ
                ABC
                DEF"""
        );
    }

    @Test
    public void multiMavenModule() {

        context = setupMultiMavenSpringModule();

        action.apply(context);

        List<ProjectResource> projectResources = getAutoConfigFileAsProjectResource();

        assertThat(projectResources).hasSize(1);
        assertSpringConfigFileContentsInProject("spring-app");
    }

    @Test
    public void moduleInsideModuleMavenSetup() {
        context = moduleInModuleProjectContext();
        action.apply(context);
        List<ProjectResource> projectResources = getAutoConfigFileAsProjectResource();
        assertThat(projectResources).hasSize(1);

        assertSpringConfigFileContentsInProject("app/spring-app");
    }

    private void assertSpringConfigFileContentsInProject(String project) {
        List<ProjectResource> content = getAutoConfigFileAsProjectResource();
        assertThat(content).hasSize(1);
        assertThat(content.get(0).getAbsolutePath().toString())
                .isEqualTo(TestProjectContext.getDefaultProjectRoot()
                        + "/" + project + "/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports");

        List<ProjectResource> oldFile = getFileAsProjectResource(EXISTING_SPRING_FACTORIES_FILE);
        assertThat(oldFile).hasSize(1);
        assertThat(oldFile.get(0).getAbsolutePath().toString())
                .isEqualTo(TestProjectContext.getDefaultProjectRoot() +
                        "/" + project + "/src/main/resources/META-INF/spring.factories");

        assertThat(oldFile.get(0).print()).isEqualTo("");
    }

    private ProjectContext moduleInModuleProjectContext() {
        return TestProjectContext.
                buildProjectContext()
                .addProjectResource("pom.xml",
                        """
                                <?xml version="1.0" encoding="UTF-8"?>
                                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                                    <modelVersion>4.0.0</modelVersion>
                                    <modules>
                                        <module>app</module>
                                    </modules>
                                    <groupId>com.example</groupId>
                                    <artifactId>root</artifactId>
                                    <version>v1</version>
                                    <name>root</name>
                                </project>
                                """)
                .addProjectResource("app/pom.xml",
                        """
                                <?xml version="1.0" encoding="UTF-8"?>
                                                                
                                <project xmlns="http://maven.apache.org/POM/4.0.0"
                                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                    <parent>
                                        <artifactId>root</artifactId>
                                        <groupId>com.example</groupId>
                                        <version>v1</version>
                                    </parent>
                                    <modelVersion>4.0.0</modelVersion>
                                                                
                                    <artifactId>app</artifactId>
                                    <modules>
                                        <module>spring-app</module>
                                    </modules>
                                </project>
                                """)
                .addProjectResource("app/spring-app/pom.xml",
                        """
                                <?xml version="1.0" encoding="UTF-8"?>
                                                                
                                <project xmlns="http://maven.apache.org/POM/4.0.0"
                                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                    <parent>
                                        <artifactId>app</artifactId>
                                        <groupId>com.example</groupId>
                                        <version>v1</version>
                                    </parent>
                                    <modelVersion>4.0.0</modelVersion>
                                                                
                                    <artifactId>spring-app</artifactId>
                                </project>
                                """)
                .addProjectResource("app/spring-app/src/main/resources/META-INF/spring.factories",
                        """
                                org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.hello.GreetingConfig
                                """)
                .build();
    }

    private ProjectContext setupMultiMavenSpringModule() {
        return TestProjectContext.
                buildProjectContext()
                .addProjectResource("pom.xml",
                        """
                                <?xml version="1.0" encoding="UTF-8"?>
                                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                                    <modelVersion>4.0.0</modelVersion>
                                    <packaging>pom</packaging>
                                    <modules>
                                        <module>spring-app</module>
                                    </modules>
                                    <groupId>com.example</groupId>
                                    <artifactId>root</artifactId>
                                    <version>v1</version>
                                    <name>root</name>
                                </project>
                                """)
                .addProjectResource("spring-app/pom.xml",
                        """
                                <?xml version="1.0" encoding="UTF-8"?>
                                                                
                                <project xmlns="http://maven.apache.org/POM/4.0.0"
                                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                    <parent>
                                        <artifactId>root</artifactId>
                                        <groupId>com.example</groupId>
                                        <version>v1</version>
                                    </parent>
                                    <modelVersion>4.0.0</modelVersion>
                                                                
                                    <artifactId>spring-app</artifactId>
                                </project>
                                                                
                                """)
                .addProjectResource("spring-app/src/main/resources/META-INF//spring.factories",
                        """
                                org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.hello.GreetingConfig
                                """)
                .build();
    }

    private String getNewAutoConfigFileContents() {
        return getAutoConfigFileAsProjectResource().get(0).print();
    }

    private List<ProjectResource> getAutoConfigFileAsProjectResource() {
        return getFileAsProjectResource(
                "/**/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports"
        );
    }

    private List<ProjectResource> getFileAsProjectResource(String path) {
        return context.search(new PathPatternMatchingProjectResourceFinder(path));
    }

    private String getSpringFactoryFile() {

        return getFileAsProjectResource(EXISTING_SPRING_FACTORIES_FILE).get(0).print();
    }
}
