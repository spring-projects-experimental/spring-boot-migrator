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

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test ApplicationModule.searchTestJava")
public class ApplicationModule_searchTestJava_Test {

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
        @DisplayName("with no classes in src/test/java provides empty ProjectResourceSet to search method")
        void withNoClassesInSrcTestJava_providesEmptyProjectResources() {

            ProjectContext context = builder.build();

            AtomicBoolean wasCalled = new AtomicBoolean(false);
            verifySearchTest(context, projectResourceSet -> {
                assertThat(projectResourceSet.list()).isEmpty();
            }, "");
        }

        @Test
        @DisplayName("with classes in src/test/java provides ProjectResourceSet with classes from src/test/java")
        void withClassesInSrcTestJavaProvidesProjectResourcesWithTestClasses() {

            ProjectContext context = TestProjectContext
                    .buildProjectContext()
                    .withMavenBuildFileSource("pom.xml", singlePom)
                    .addJavaSource("src/test/java", "public class SomeClass{}")
                    .build();

            verifySearchTest(context, projectResourceSet -> {
                assertThat(projectResourceSet.list()).hasSize(1);
                assertThat(projectResourceSet.get(0).getSourcePath().toString()).isEqualTo("src/test/java/SomeClass.java");
                assertThat(projectResourceSet.get(0).print()).isEqualTo("public class SomeClass{}");
            }, "");
        }

        @Test
        @DisplayName("with classes in src/main/java and src/test/java provides ProjectResourceSet with classes from src/test/java")
        void withClassesInTestAndMain_providesClassesFromTest() {
            ProjectContext context = builder
                    .addJavaSource("src/main/java", "public class SomeClass{}")
                    .addJavaSource("src/test/java", "public class SomeClassTest{}")
                    .build();

            verifySearchTest(context, projectResourceSet -> {
                assertThat(projectResourceSet.list()).hasSize(1);
                assertThat(projectResourceSet.get(0).getSourcePath().toString()).isEqualTo("src/test/java/SomeClassTest.java");
                assertThat(projectResourceSet.get(0).print()).isEqualTo("public class SomeClassTest{}");
            }, "");
        }

        @Test
        @DisplayName("with classes in src/main/java provides empty ProjectResourceSet to search method")
        void withClassesInSrcMainJava_providesEmptyProjectResources() {

            ProjectContext context = builder
                    .addProjectResource("src/main/java/SomeClass.java", "public class SomeClass{}")
                    .build();

            verifySearchTest(context, projectResourceSet -> assertThat(projectResourceSet.list()).isEmpty(), "");
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
            verifySearchTest(context, (projectResourceSet) -> assertThat(projectResourceSet.list()).isEmpty(),
                             "application");
        }

        @Test
        @DisplayName("with classes in src/test/java of other module provides empty ProjectResourceSet to search method")
        void withClassesInOtherModules_providesEmptyProjectResources() {

            ProjectContext context = builder
                    .addJavaSource("component/src/test/java", "public class SomeClass{}")
                    .build();

            verifySearchTest(context, (projectResourceSet) -> assertThat(projectResourceSet.list()).isEmpty(),
                             "application");
        }

        @Test
        @DisplayName("with classes in src/test/java and src/main/java provides ProjectResourceSet with classes from src/test/java")
        void withResourcesInMainAndTest_providesProjectResourcesFromSrcMainResources() {

            ProjectContext context = builder
                    .addJavaSource("application/src/main/java", "public class SomeClass{}")
                    .addJavaSource("application/src/test/java", "public class SomeClassTest{}")
                    .build();

            verifySearchTest(context,
                             (projectResourceSet) -> {
                                assertThat(projectResourceSet.list()).hasSize(1);
                                assertThat(projectResourceSet.list().get(0).getSourcePath().toString()).isEqualTo("application/src/test/java/SomeClassTest.java");
                                assertThat(projectResourceSet.list().get(0).print()).isEqualTo("public class SomeClassTest{}");
                            },
                            "application");
        }

        @Test
        @DisplayName("with classes in src/test/java provides ProjectResourceSet with classes from stc/test/java")
        void withClassesInTest_providesClassesFromSrcTestJava() {

            ProjectContext context = builder
                    .addJavaSource("application/src/test/java", "public class SomeClassTest{}")
                    .build();

            verifySearchTest(context,
                             projectResourceSet -> {
                                assertThat(projectResourceSet.list()).hasSize(1);
                                assertThat(projectResourceSet.list().get(0).getSourcePath().toString()).isEqualTo(
                                        "application/src/test/java/SomeClassTest.java");
                                assertThat(projectResourceSet.list().get(0).print()).isEqualTo("public class SomeClassTest{}");
                            },
                             "application");
        }
    }

    private void verifySearchTest(ProjectContext context, Consumer<ProjectResourceSet> projectResourceSetConsumer, String modulePath) {
        AtomicBoolean wasCalled = new AtomicBoolean(false);
        searchTest(context, wasCalled, projectResourceSetConsumer, modulePath);
        assertThat(wasCalled).isTrue();
    }

    private void searchTest(ProjectContext context, AtomicBoolean wasCalled, Consumer<ProjectResourceSet> f, String modulePath) {
        context
                .getApplicationModules()
                .getModule(modulePath)
                .searchTestJava(
                        ((ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>>) projectResourceSet -> {
                            f.accept(projectResourceSet);
                            wasCalled.set(true);
                            return null;
                        }));
    }

}
