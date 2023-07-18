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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test ApplicationModule.searchTestJava")
public class Module_searchMainJava_Test {

    @Nested
    @DisplayName("Project")
    class SingleModuleProject {

        @Language("xml")
        String singlePom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.acme</groupId>
                    <artifactId>application</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                </project>
                """;

        private TestProjectContext.Builder builder;

        @BeforeEach
        void beforeEach() {
            builder = TestProjectContext.buildProjectContext().withMavenBuildFileSource("pom.xml", singlePom);
        }

        @Test
        @DisplayName("with no classes in src/main/java provides empty ProjectResourceSet to search method")
        void withNoClassesInSrcMainJava_providesEmptyProjectResources() {

            ProjectContext context = builder.build();

            AtomicBoolean wasCalled = new AtomicBoolean(false);
            verifySearchMain(context, projectResourceSet -> {
                assertThat(projectResourceSet.list()).isEmpty();
            }, "");
        }

        @Test
        @DisplayName("with classes in src/main/java provides ProjectResourceSet with classes from src/main/java")
        void withClassesInSrcMainJavaProvidesProjectResourcesWithMainClasses() {

            ProjectContext context = TestProjectContext
                    .buildProjectContext()
                    .withMavenBuildFileSource("pom.xml", singlePom)
                    .withJavaSource("src/main/java", "public class SomeClass{}")
                    .build();

            verifySearchMain(context, projectResourceSet -> {
                assertThat(projectResourceSet.list()).hasSize(1);
                assertThat(projectResourceSet.get(0).getSourcePath().toString()).isEqualTo("src/main/java/SomeClass.java");
                assertThat(projectResourceSet.get(0).print()).isEqualTo("public class SomeClass{}");
            }, "");
        }

        @Test
        @DisplayName("with classes in src/main/java and src/test/java provides ProjectResourceSet with classes from src/main/java")
        void withClassesInTestAndMain_providesClassesFromMain() {
            ProjectContext context = builder
                    .withJavaSource("src/main/java", "public class SomeClass{}")
                    .withJavaSource("src/test/java", "public class SomeClassTest{}")
                    .build();

            verifySearchMain(context, projectResourceSet -> {
                assertThat(projectResourceSet.list()).hasSize(1);
                assertThat(projectResourceSet.get(0).getSourcePath().toString()).isEqualTo("src/main/java/SomeClass.java");
                assertThat(projectResourceSet.get(0).print()).isEqualTo("public class SomeClass{}");
            }, "");
        }

        @Test
        @DisplayName("with classes in src/test/java provides empty ProjectResourceSet to search method")
        void withClassesInSrcTestJava_providesEmptyProjectResources() {

            ProjectContext context = builder
                    .withProjectResource("src/test/java/SomeClass.java", "public class SomeClass{}")
                    .build();

            verifySearchMain(context, projectResourceSet -> assertThat(projectResourceSet.list()).isEmpty(), "");
        }
    }

    @Nested
    @DisplayName("Multi-module project")
    class MultiMavenModuleProject {
        @Language("xml")
        String parentPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.acme</groupId>
                    <artifactId>parent</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <packaging>pom</packaging>
                    <modules>
                        <module>application</module>
                        <module>component</module>
                    </modules>
                </project>
                """;

        @Language("xml")
        String componentPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.0.1-SNAPSHOT</version>
                        <relativePath>../</relativePath>
                    </parent>
                    <artifactId>component</artifactId>
                </project>
                """;

        @Language("xml")
        String applicationPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.0.1-SNAPSHOT</version>
                        <relativePath>../</relativePath>
                    </parent>
                    <artifactId>application</artifactId>
                    <dependencies>
                        <dependency>
                            <groupId>com.acme</groupId>
                            <artifactId>component</artifactId>
                            <version>0.0.1-SNAPSHOT</version>
                        </dependency>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>2.0.1.Final</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        private TestProjectContext.Builder builder;

        @BeforeEach
        void beforeEach() {
            builder = TestProjectContext
                    .buildProjectContext()
                    .withMavenBuildFileSource("pom.xml", parentPom)
                    .withMavenBuildFileSource("application/pom.xml", applicationPom)
                    .withMavenBuildFileSource("component/pom.xml", componentPom);
        }


        @Test
        @DisplayName("with no classes provides empty ProjectResourceSet to search method")
        void withNoClasses_providesEmptyProjectResources() {
            ProjectContext context = builder.build();
            verifySearchMain(context, (projectResourceSet) -> assertThat(projectResourceSet.list()).isEmpty(),
                             "application");
        }

        @Test
        @DisplayName("with classes in src/test/java of other module provides empty ProjectResourceSet to search method")
        void withClassesInOtherModules_providesEmptyProjectResources() {

            ProjectContext context = builder
                    .withJavaSource("component/src/main/java", "public class SomeClass{}")
                    .build();

            verifySearchMain(context, (projectResourceSet) -> assertThat(projectResourceSet.list()).isEmpty(),
                             "application");
        }

        @Test
        @DisplayName("with classes in src/test/java and src/main/java provides ProjectResourceSet with classes from src/main/java")
        void withClassesInMainAndTest_providesClassesFromSrcMainJava() {

            ProjectContext context = builder
                    .withJavaSource("application/src/main/java", "public class SomeClass{}")
                    .withJavaSource("application/src/test/java", "public class SomeClassTest{}")
                    .build();

            verifySearchMain(context,
                             (projectResourceSet) -> {
                                assertThat(projectResourceSet.list()).hasSize(1);
                                assertThat(projectResourceSet.list().get(0).getSourcePath().toString()).isEqualTo("application/src/main/java/SomeClass.java");
                                assertThat(projectResourceSet.list().get(0).print()).isEqualTo("public class SomeClass{}");
                            },
                             "application");
        }

        @Test
        @DisplayName("with classes in src/main/java provides ProjectResourceSet with classes from stc/main/java")
        void withClassesInMain_providesClassesFromSrcMainJava() {

            ProjectContext context = builder
                    .withJavaSource("application/src/main/java", "public class SomeClass{}")
                    .build();

            verifySearchMain(context,
                             projectResourceSet -> {
                                assertThat(projectResourceSet.list()).hasSize(1);
                                assertThat(projectResourceSet.list().get(0).getSourcePath().toString()).isEqualTo("application/src/main/java/SomeClass.java");
                                assertThat(projectResourceSet.list().get(0).print()).isEqualTo("public class SomeClass{}");
                            },
                             "application");
        }
    }

    private void verifySearchMain(ProjectContext context, Consumer<ProjectResourceSet> projectResourceSetConsumer, String modulePath) {
        AtomicBoolean wasCalled = new AtomicBoolean(false);
        searchTest(context, wasCalled, projectResourceSetConsumer, modulePath);
        assertThat(wasCalled).isTrue();
    }

    private void searchTest(ProjectContext context, AtomicBoolean wasCalled, Consumer<ProjectResourceSet> f, String modulePath) {
        context
                .getApplicationModules()
                .getModule(Path.of(modulePath))
                .searchMainJava(
                        ((ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>>) projectResourceSet -> {
                            f.accept(projectResourceSet);
                            wasCalled.set(true);
                            return null;
                        }));
    }

}
