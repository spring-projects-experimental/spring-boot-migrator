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
package org.springframework.sbm.project.buildfile;

import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiModulePlayground {

    @Nested
    @DisplayName("Given two poms and one Java source")
    public class GivenTwoModuleReactorBuild {

        private static final String rootPom =
                """
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <artifactId>parent</artifactId>
                            <groupId>com.example.foo</groupId>
                            <version>0.1.0-SNAPSHOT</version>
                            <modelVersion>4.0.0</modelVersion>
                            <packaging>pom</packaging>
                            <properties>
                                <maven.compiler.target>1.8</maven.compiler.target>
                                <maven.compiler.source>1.8</maven.compiler.source>
                            </properties>
                            <modules>
                                <module>module1</module>
                            </modules>
                        </project>
                        """;

        private static final String childPom =
                """
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <parent>
                                <artifactId>parent</artifactId>
                                <groupId>com.example.foo</groupId>
                                <version>0.1.0-SNAPSHOT</version>
                                <relativePath>../pom.xml</relativePath>
                            </parent>
                            <artifactId>child</artifactId>
                            <modelVersion>4.0.0</modelVersion>
                            <properties>
                                <maven.compiler.target>1.8</maven.compiler.target>
                                <maven.compiler.source>1.8</maven.compiler.source>
                            </properties>
                        </project>
                        """;

        private static final String javaCode = "public class Foo {}";

        private final ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(rootPom)
                .withProjectResource("module1/pom.xml", childPom)
                .withJavaSourceInModule("module1/src/main/java/Foo.java", javaCode)
                .build();

        @Test
        @DisplayName("When getApplicationModules() gets called then two modules should be found")
        void allApplicationModulesShouldBeFound() {
            assertThat(context.getApplicationModules().stream()).hasSize(2);
            assertThat(context.getApplicationModules().getModule(Path.of("")).getBuildFile().print()).isEqualTo(rootPom);
            assertThat(context.getApplicationModules().getModule(Path.of("module1")).getBuildFile().print()).isEqualTo(childPom);
        }

        @Test
        @DisplayName("When getRootModule() is called it must return the topmost module.")
        void testGetRootModule() {
            assertThat(context.getApplicationModules().getRootModule().getBuildFile().print()).isEqualTo(rootPom);
        }

        @Test
        @DisplayName("When getMainJavaSourceSet() is called on root module it must be empty.")
        void rootModuleHasEmptyJavaSourceSet() {
            assertThat(context.getApplicationModules().getModule(Path.of("")).getMainJavaSourceSet().list()).isEmpty();
        }

        @Test
        @DisplayName("When getModules() is called on root module it must contain the child modules.")
        void testGetModulesOnRootModule() {
            Module module = context.getApplicationModules().getModule(Path.of(""));
            assertThat(context.getApplicationModules().getModules(module)).hasSize(1);
            assertThat(context.getApplicationModules().getModules(module).get(0).getBuildFile().print()).isEqualTo(childPom);
        }

        @Test
        @DisplayName("When getMainJavaSourceSet() is called on child module it must contain Java source.")
        void childModuleHasJavaSourceSetWithJavaSource() {
            assertThat(context.getApplicationModules().getModule(Path.of("module1")).getMainJavaSourceSet().list()).hasSize(1);
            assertThat(context.getApplicationModules().getModule(Path.of("module1")).getMainJavaSourceSet().list().get(0).print()).isEqualTo(javaCode);
        }
    }


}
