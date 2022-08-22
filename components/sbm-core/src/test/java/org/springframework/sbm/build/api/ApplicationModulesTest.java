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

package org.springframework.sbm.build.api;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationModulesTest {

    private static ApplicationModules sut;
    @Language("xml")
    private static final String PARENT_POM = """
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
    private static final String APPLICATION_POM = """
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
    private static final String COMPONENT_POM = """
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

    @BeforeAll
    static void beforeAll() {
        sut = TestProjectContext
                .buildProjectContext()
                .withMavenRootBuildFileSource(PARENT_POM)
                .withMavenBuildFileSource("module1/pom.xml", APPLICATION_POM)
                .withMavenBuildFileSource("module2/pom.xml", COMPONENT_POM)
                .build()
                .getApplicationModules();
    }

    @Test
    void shouldBeRecognizedAsMultiModuleProject() {
        assertThat(sut.isSingleModuleApplication()).isFalse();
        assertThat(sut.list()).hasSize(3);
    }

    @Test
    void rootModule() {
        ApplicationModule rootModule = sut.getRootModule();
        assertThat(rootModule.getModulePath()).isEqualTo(Path.of(""));
        assertThat(rootModule.getBuildFile().getCoordinates()).isEqualTo("org.example:parent:1.0-SNAPSHOT");
//        assertThat(rootModule.getModules().get(0).getBuildFile().getCoordinates()).isEqualTo("org.example:module1:1.0-SNAPSHOT");
//        assertThat(rootModule.getModules().get(1).getBuildFile().getCoordinates()).isEqualTo("org.example:module2:1.0-SNAPSHOT");
        assertThat(rootModule.getDeclaredModules()).hasSize(2);
        assertThat(rootModule.getDeclaredModules().get(0)).isEqualTo("org.example:module1:1.0-SNAPSHOT");
        assertThat(rootModule.getDeclaredModules().get(1)).isEqualTo("org.example:module2:1.0-SNAPSHOT");
    }

    @Test
    void getModule() {
        ApplicationModule parentModule = sut.findModule("org.example:parent:1.0-SNAPSHOT").get();
        assertThat(parentModule.getBuildFile().getCoordinates()).isEqualTo("org.example:parent:1.0-SNAPSHOT");

        ApplicationModule module1 = sut.findModule("org.example:module1:1.0-SNAPSHOT").get();
        assertThat(module1.getBuildFile().getCoordinates()).isEqualTo("org.example:module1:1.0-SNAPSHOT");

        ApplicationModule module2 = sut.findModule("org.example:module2:1.0-SNAPSHOT").get();
        assertThat(module2.getBuildFile().getCoordinates()).isEqualTo("org.example:module2:1.0-SNAPSHOT");
    }

    @Test
    void applicationModule() {
        assertThat(sut.getTopmostApplicationModules()).hasSize(1);
        ApplicationModule applicationModule = sut.getTopmostApplicationModules().get(0);
        assertThat(applicationModule.getModulePath()).isEqualTo(Path.of("module1"));
        assertThat(applicationModule.getBuildFile().getCoordinates()).isEqualTo("org.example:module1:1.0-SNAPSHOT");
    }

    @Test
    void componentModule() {
        assertThat(sut.getComponentModules()).hasSize(1);
        ApplicationModule applicationModule = sut.getTopmostApplicationModules().get(0);
        assertThat(applicationModule.getModulePath()).isEqualTo(Path.of("module1"));
        assertThat(applicationModule.getBuildFile().getCoordinates()).isEqualTo("org.example:module1:1.0-SNAPSHOT");
    }

    // TODO: add test for getTopmostApplicationModules with packaging != jar

}