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
package org.springframework.sbm.build.migration.actions;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;

import static org.assertj.core.api.Assertions.assertThat;

class AddMavenDependencyManagementActionTest {

    @Test
    void shouldAddToRootPomInMultiModuleProject() {
        @Language("xml")
        final String PARENT_POM = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <packaging>pom</packaging>
                    <properties>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>
                    <modules>
                        <module>module1</module>
                        <module>module2</module>
                    </modules>
                </project>
                """;
        @Language("xml")
        final String APPLICATION_POM = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>org.example</groupId>
                        <artifactId>parent</artifactId>
                        <version>1.0-SNAPSHOT</version>
                        <relativePath>../pom.xml</relativePath>
                    </parent>
                    <artifactId>module1</artifactId>
                    <properties>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>org.example</groupId>
                            <artifactId>module2</artifactId>
                            <version>${project.version}</version>
                        </dependency>
                    </dependencies>
                </project>
                """;
        @Language("xml")
        final String COMPONENT_POM = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>org.example</groupId>
                        <artifactId>parent</artifactId>
                        <version>1.0-SNAPSHOT</version>
                        <relativePath>../pom.xml</relativePath>
                    </parent>
                    <artifactId>module2</artifactId>
                    <properties>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>
                </project>
                """;

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withMavenRootBuildFileSource(PARENT_POM)
                .withMavenBuildFileSource("module1/pom.xml", APPLICATION_POM)
                .withMavenBuildFileSource("module2/pom.xml", COMPONENT_POM)
                .build();

        AddMavenDependencyManagementAction sut = new AddMavenDependencyManagementAction();
        sut.setGroupId("org.springframework.boot");
        sut.setArtifactId("spring-boot-dependencies");
        sut.setVersion("2.7.1");
        sut.setDependencyType("pom");
        sut.setScope("import");
        sut.apply(context);

        @Language("xml")
        String expectedParentPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <packaging>pom</packaging>
                    <properties>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>
                    <modules>
                        <module>module1</module>
                        <module>module2</module>
                    </modules>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-dependencies</artifactId>
                                <version>2.7.1</version>
                                <type>pom</type>
                                <scope>import</scope>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                </project>
                """;
        assertThat(context.getApplicationModules().getRootModule().getBuildFile().print()).isEqualToIgnoringNewLines(expectedParentPom);
        assertThat(context.getApplicationModules().getRootModule().getBuildFile().getRequestedDependencyManagement()).hasSize(1);
        assertThat(context.getApplicationModules().findModule("org.example:module1:1.0-SNAPSHOT").get().getBuildFile().print()).isEqualToIgnoringNewLines(APPLICATION_POM);
        assertThat(context.getApplicationModules().findModule("org.example:module2:1.0-SNAPSHOT").get().getBuildFile().print()).isEqualToIgnoringNewLines(COMPONENT_POM);
    }


    @Test
    void shouldAddToRootPomInSingleModuleProject() {

        String pomSource =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.springframework.sbm.examples</groupId>\n" +
                        "    <artifactId>artifact-id</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "</project>\n";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.springframework.sbm.examples</groupId>\n" +
                        "    <artifactId>artifact-id</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>groupId</groupId>\n" +
                        "                <artifactId>artifactId</artifactId>\n" +
                        "                <version>version</version>\n" +
                        "                <type>jar</type>\n" +
                        "                <scope>compile</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "</project>\n";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomSource)
                .build();

        AddMavenDependencyManagementAction sut = new AddMavenDependencyManagementAction();
        sut.setArtifactId("artifactId");
        sut.setGroupId("groupId");
        sut.setVersion("version");
        sut.setScope("compile");
        sut.setDependencyType("jar");

        sut.apply(projectContext);

        BuildFile buildFile = projectContext.getBuildFile();

        assertThat(buildFile.print())
                .as(TestDiff.of(buildFile.print(), expected))
                .isEqualTo(expected);
    }
}
