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
package org.springframework.sbm.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationModules_getTopmostModulesTest {

    @Nested
    public class GivenSingleModuleProjectWithExternalParentPom {
        private static final String projectPom =
                        """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <parent>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-starter-parent</artifactId>
                                <version>2.5.6</version>
                            </parent>
                            <groupId>com.example.sbm</groupId>
                            <artifactId>parent</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                            <modelVersion>4.0.0</modelVersion>
                            <packaging>jar</packaging>
                        </project>
                        """;

        private ProjectContext projectContext;

        @BeforeEach
        void beforeEach() {
            projectContext = TestProjectContext.buildProjectContext()
                    .withProjectResource("pom.xml", projectPom)
                    .build();
        }

        @Test
        void whenGetTopmostApplicationModulesThenChildModuleShouldBeReturned() {
            List<Module> topmostModules = projectContext.getApplicationModules().getTopmostApplicationModules();
            assertThat(topmostModules).hasSize(1);
            assertThat(topmostModules.get(0).getBuildFile().getSourcePath().toString()).isEqualTo("pom.xml");
        }
    }

    @Nested
    public class GivenTwoModuleProject {
        private static final String parentPom =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <groupId>com.example.sbm</groupId>\n" +
                        "    <artifactId>parent</artifactId>\n" +
                        "    <version>0.1.0-SNAPSHOT</version>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <packaging>pom</packaging>\n" +
                        "    <modules>\n" +
                        "        <module>module1</module>\n" +
                        "    </modules>" +
                        "</project>\n";

        private static final String childModule =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <parent>\n" +
                        "        <groupId>com.example.sbm</groupId>\n" +
                        "        <artifactId>parent</artifactId>\n" +
                        "        <version>0.1.0-SNAPSHOT</version>\n" +
                        "        <relativePath>../pom.xml</relativePath>\n" +
                        "    </parent>\n" +
                        "    <artifactId>module2</artifactId>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "</project>\n";

        private ProjectContext projectContext;

        @BeforeEach
        void beforeEach() {
            projectContext = TestProjectContext.buildProjectContext()
                    .withProjectResource("pom.xml", parentPom)
                    .withProjectResource("module1/pom.xml", childModule)
                    .build();
        }

        @Test
        void whenGetTopmostApplicationModulesThenChildModuleShouldBeReturned() {
            List<Module> topmostModules = projectContext.getApplicationModules().getTopmostApplicationModules();
            assertThat(topmostModules).hasSize(1);
            assertThat(topmostModules.get(0).getBuildFile().getSourcePath()).isEqualTo(Path.of("module1/pom.xml"));
        }
    }

    @Nested
    public class GivenThreeModuleProjectWithOneTopmostModule {
        private static final String parentPom =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <groupId>com.example.sbm</groupId>\n" +
                        "    <artifactId>parent</artifactId>\n" +
                        "    <version>0.1.0-SNAPSHOT</version>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <packaging>pom</packaging>\n" +
                        "    <modules>\n" +
                        "        <module>module1</module>\n" +
                        "        <module>module2</module>\n" +
                        "    </modules>" +
                        "</project>\n";

        private static final String childPom1 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <parent>\n" +
                        "        <groupId>com.example.sbm</groupId>\n" +
                        "        <artifactId>parent</artifactId>\n" +
                        "        <version>0.1.0-SNAPSHOT</version>\n" +
                        "        <relativePath>../pom.xml</relativePath>\n" +
                        "    </parent>\n" +
                        "    <artifactId>module1</artifactId>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>com.example.sbm</groupId>\n" +
                        "            <artifactId>module2</artifactId>\n" +
                        "            <version>0.1.0-SNAPSHOT</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>\n";

        private static final String childPom2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <parent>\n" +
                        "        <groupId>com.example.sbm</groupId>\n" +
                        "        <artifactId>parent</artifactId>\n" +
                        "        <version>0.1.0-SNAPSHOT</version>\n" +
                        "        <relativePath>../pom.xml</relativePath>\n" +
                        "    </parent>\n" +
                        "    <artifactId>module2</artifactId>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "</project>\n";

        private ProjectContext projectContext;

        @BeforeEach
        void beforeEach() {
            projectContext = TestProjectContext.buildProjectContext()
                    .withProjectResource("pom.xml", parentPom)
                    .withProjectResource("module1/pom.xml", childPom1)
                    .withProjectResource("module2/pom.xml", childPom2)
                    .build();
        }

        @Test
        void whenGetTopmostApplicationModulesThenChildPom1ShouldBeReturned() {
            List<Module> topmostModules = projectContext.getApplicationModules().getTopmostApplicationModules();
            assertThat(topmostModules).hasSize(1);
            assertThat(
                    topmostModules.get(0).getBuildFile().getSourcePath().toString()).isEqualTo(Path.of("module1/pom.xml").toString());
        }
    }

    @Nested
    public class GivenFourModuleProjectWithTwoTopmostModules {
        private static final String parentPom =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <groupId>com.example.sbm</groupId>\n" +
                        "    <artifactId>parent</artifactId>\n" +
                        "    <version>0.1.0-SNAPSHOT</version>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <packaging>pom</packaging>\n" +
                        "    <modules>\n" +
                        "        <module>module1</module>\n" +
                        "        <module>module2</module>\n" +
                        "        <module>module3</module>\n" +
                        "    </modules>" +
                        "</project>\n";

        private static final String childPom1 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <parent>\n" +
                        "        <groupId>com.example.sbm</groupId>\n" +
                        "        <artifactId>parent</artifactId>\n" +
                        "        <version>0.1.0-SNAPSHOT</version>\n" +
                        "        <relativePath>../pom.xml</relativePath>\n" +
                        "    </parent>\n" +
                        "    <artifactId>module1</artifactId>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>com.example.sbm</groupId>\n" +
                        "            <artifactId>module2</artifactId>\n" +
                        "            <version>0.1.0-SNAPSHOT</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>\n";

        private static final String childPom2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <parent>\n" +
                        "        <groupId>com.example.sbm</groupId>\n" +
                        "        <artifactId>parent</artifactId>\n" +
                        "        <version>0.1.0-SNAPSHOT</version>\n" +
                        "        <relativePath>../pom.xml</relativePath>\n" +
                        "    </parent>\n" +
                        "    <artifactId>module2</artifactId>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "</project>\n";

        private static final String childPom3 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <parent>\n" +
                        "        <groupId>com.example.sbm</groupId>\n" +
                        "        <artifactId>parent</artifactId>\n" +
                        "        <version>0.1.0-SNAPSHOT</version>\n" +
                        "        <relativePath>../pom.xml</relativePath>\n" +
                        "    </parent>\n" +
                        "    <artifactId>module3</artifactId>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>com.example.sbm</groupId>\n" +
                        "            <artifactId>module2</artifactId>\n" +
                        "            <version>0.1.0-SNAPSHOT</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>\n";

        private ProjectContext projectContext;

        @BeforeEach
        void beforeEach() {
            projectContext = TestProjectContext.buildProjectContext()
                    .withProjectResource("pom.xml", parentPom)
                    .withProjectResource("module1/pom.xml", childPom1)
                    .withProjectResource("module2/pom.xml", childPom2)
                    .withProjectResource("module3/pom.xml", childPom3)
                    .build();
        }

        @Test
        void whenGetTopmostApplicationModulesThenPom1AndPom3ShouldBeReturned() {
            List<Module> topmostModules = projectContext.getApplicationModules().getTopmostApplicationModules();
            assertThat(topmostModules).hasSize(2);
            List<String> topmostApplicationModulePaths = topmostModules
                    .stream().map(m -> m.getBuildFile().getSourcePath().toString()).collect(Collectors.toList());
            assertThat(topmostApplicationModulePaths).contains(Path.of("module1/pom.xml").toString(), Path.of("module3/pom.xml").toString());
        }
    }
}