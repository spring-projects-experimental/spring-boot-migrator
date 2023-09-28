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
package org.springframework.sbm.build.api;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.project.resource.finder.ProjectResourceFinder;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test ApplicationModule.searchMainResources")
public class Module_searchMainResources_Test {

    @Nested
    @DisplayName("Project")
    class SingleModuleProject {

        @Language("xml")
        String singlePom =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.acme</groupId>
                    <artifactId>parent</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                </project>
                """;

        private TestProjectContext.Builder builder;

        @BeforeEach
        void beforeEach() {
            builder = TestProjectContext
                    .buildProjectContext()
                    .withMavenBuildFileSource("pom.xml", singlePom);
        }

        @Test
        @DisplayName("with no resources in src/main/resources provides empty ProjectResourceSet to search method")
        void withNoResourcesInSrcMainResources_providesEmptyProjectResources() {

            ProjectContext context = builder.build();

            AtomicBoolean wasCalled = new AtomicBoolean(false);

            context.getApplicationModules().list().get(0).searchMainResources(((ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>>) projectResourceSet -> {
                assertThat(projectResourceSet.list()).isEmpty();
                wasCalled.set(true);
                return null;
            }) );

            assertThat(wasCalled).isTrue();
        }

        @Test
        @DisplayName("with resources in src/main/resources provides ProjectResourceSet with resources from src/main/resources")
        void withResourcesInSrcMainResourcesProvidesProjectResourcesWithMainResources() {

            ProjectContext context = TestProjectContext
                    .buildProjectContext()
                    .withMavenBuildFileSource("pom.xml", singlePom)
                    .withProjectResource("src/main/resources/some-resource.txt", "the content")
                    .build();

            AtomicBoolean wasCalled = new AtomicBoolean(false);

            context.getApplicationModules().list().get(0).searchMainResources(((ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>>) projectResourceSet -> {
                assertThat(projectResourceSet.list()).hasSize(1);
                assertThat(projectResourceSet.get(0).getSourcePath().toString()).isEqualTo("src/main/resources/some-resource.txt");
                assertThat(projectResourceSet.get(0).print()).isEqualTo("the content");
                wasCalled.set(true);
                return null;
            }) );

            assertThat(wasCalled).isTrue();
        }

        @Test
        @DisplayName("with resources in src/test/resources and src/main/resources provides ProjectResourceSet with resources from src/main/resources")
        void withResourcesInTestAndMain_providesResourcesFromMain() {
            ProjectContext context = builder
                    .withProjectResource("src/main/resources/some-resource.txt", "the content")
                    .withProjectResource("src/test/resources/some-resource.txt", "the test content")
                    .build();

            AtomicBoolean wasCalled = new AtomicBoolean(false);

            context.getApplicationModules().list().get(0).searchMainResources(((ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>>) projectResourceSet -> {
                assertThat(projectResourceSet.list()).hasSize(1);
                assertThat(projectResourceSet.get(0).getSourcePath().toString()).isEqualTo("src/main/resources/some-resource.txt");
                assertThat(projectResourceSet.get(0).print()).isEqualTo("the content");
                wasCalled.set(true);
                return null;
            }) );

            assertThat(wasCalled).isTrue();
        }

        @Test
        @DisplayName("with resources in src/test/resources provides empty ProjectResourceSet to search method")
        void withResourcesInSrcTestResources_providesEmptyProjectResources() {

            ProjectContext context = builder
                    .withProjectResource("src/test/resources/some-resource.txt", "the content")
                    .build();

            AtomicBoolean wasCalled = new AtomicBoolean(false);

            context.getApplicationModules().list().get(0).searchMainResources(((ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>>) projectResourceSet -> {
                assertThat(projectResourceSet.list()).isEmpty();
                wasCalled.set(true);
                return null;
            }) );

            assertThat(wasCalled).isTrue();
        }
    }

    @Nested
    @DisplayName("Multi-module project")
    class MultiMavenModuleProject {
        @Language("xml")
        String parentPom =
                """
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
        String componentPom =
                """
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
        String applicationPom =
                """
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
        @DisplayName("with no resources provides empty ProjectResourceSet to search method")
        void withNoResources_providesEmptyProjectResources() {

            ProjectContext context = builder.build();

            AtomicBoolean wasCalled = new AtomicBoolean(false);

            context.getApplicationModules().getModule(Path.of("application")).searchMainResources(((ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>>) projectResourceSet -> {
                assertThat(projectResourceSet.list()).isEmpty();
                wasCalled.set(true);
                return null;
            }) );

            assertThat(wasCalled).isTrue();
        }

        @Test
        @DisplayName("with resources in src/main/resources of other module provides empty ProjectResourceSet to search method")
        void withResourcesInOtherModules_providesEmptyProjectResources() {

            ProjectContext context = builder
                    .withProjectResource("component/src/main/resources/some-resource.txt", "")
                    .build();

            AtomicBoolean wasCalled = new AtomicBoolean(false);

            context.getApplicationModules().getModule(Path.of("application")).searchMainResources(((ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>>) projectResourceSet -> {
                assertThat(projectResourceSet.list()).isEmpty();
                wasCalled.set(true);
                return null;
            }) );

            assertThat(wasCalled).isTrue();
        }


        @Test
        @DisplayName("with resources in src/test/resources and src/main/resources provides ProjectResourceSet with resources from src/main/resources")
        void withResourcesInMainAndTest_providesProjectResourcesFromSrcMainResources() {

            ProjectContext context = builder
                    .withProjectResource("application/src/main/resources/some-resource.txt", "the content")
                    .withProjectResource("application/src/test/resources/some-resource.txt", "the test content")
                    .build();

            AtomicBoolean wasCalled = new AtomicBoolean(false);

            context.getApplicationModules().getModule(Path.of("application")).searchMainResources(((ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>>) projectResourceSet -> {
                assertThat(projectResourceSet.list()).hasSize(1);
                assertThat(projectResourceSet.list().get(0).getSourcePath().toString()).isEqualTo("application/src/main/resources/some-resource.txt");
                assertThat(projectResourceSet.list().get(0).print()).isEqualTo("the content");
                wasCalled.set(true);
                return null;
            }) );

            assertThat(wasCalled).isTrue();
        }

        @Test
        @DisplayName("with resources in src/main/resources provides ProjectResourceSet with resources from stc/main/resources")
        void withResourcesInMain_providesProjectResourcesFromSrcMainResources() {

            ProjectContext context = builder
                    .withProjectResource("application/src/main/resources/some-resource.txt", "the content")
                    .build();

            AtomicBoolean wasCalled = new AtomicBoolean(false);

            context.getApplicationModules().getModule(Path.of("application")).searchMainResources(((ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>>) projectResourceSet -> {
                assertThat(projectResourceSet.list()).hasSize(1);
                assertThat(projectResourceSet.list().get(0).getSourcePath().toString()).isEqualTo("application/src/main/resources/some-resource.txt");
                assertThat(projectResourceSet.list().get(0).print()).isEqualTo("the content");
                wasCalled.set(true);
                return null;
            }) );

            assertThat(wasCalled).isTrue();
        }
    }

}
