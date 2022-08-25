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

import org.junit.jupiter.api.Test;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class Module_getModuleResources_Test {
    @Test
    void singleModuleProject() {
        String rootPom = PomBuilder
                .buiildPom("com.example:parent:1.0")
                .type("jar")
                .withModules("module1", "module2")
                .build();

        String javaClass = """
                package com.example;
                public class SomeClass {}
                """;
        String testjavaClass = """
                package com.example;
                public class SomeClassTest {}
                """;
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withMavenBuildFileSource("pom.xml", rootPom)
                .addJavaSource("src/main/java", javaClass)
                .addProjectResource("src/main/resources/resource-found.txt", "")
                .addJavaSource("src/test/java", testjavaClass)
                .addProjectResource("src/test/resources/test-resource-found.txt", "")
                .addProjectResource("not-in-source-set.txt", "")
                .build();

        Module root = context.getApplicationModules().getModule("root");
        Path rootPath = TestProjectContext.getDefaultProjectRoot();
        assertThat(root.contains(rootPath.resolve("pom.xml"))).isTrue();
        assertThat(root.contains(rootPath.resolve("src/main/java/com/example/SomeClass.java"))).isTrue();
        assertThat(root.contains(rootPath.resolve("src/test/java/com/example/SomeClassTest.java"))).isTrue();
        assertThat(root.contains(rootPath.resolve("src/main/resources/resource-found.txt"))).isTrue();
        assertThat(root.contains(rootPath.resolve("src/test/resources/test-resource-found.txt"))).isTrue();
        assertThat(root.contains(rootPath.resolve("not-in-source-set.txt"))).isFalse();
    }

    @Test
    void multiModuleProject() {
        String rootPom = PomBuilder
                .buiildPom("com.example:parent:1.0")
                .type("pom")
                .withModules("module1", "module2")
                .build();

        String module1Pom = PomBuilder
                .buiildPom("com.example:parent:1.0", "module1")
                .dependencies("com.example:module2:1.0")
                .build();

        String module2Pom = PomBuilder.buiildPom("com.example:parent:1.0", "module2").build();

        String javaClass = """
                package com.example;
                public class SomeClass {}
                """;
        String testjavaClass = """
                package com.example;
                public class SomeClassTest {}
                """;
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withMavenBuildFileSource("module1/pom.xml", module1Pom)
                .addJavaSource("module1/src/test/java", testjavaClass)
                .addProjectResource("module1/src/test/resources/test-resource-found.txt", "")

                .withMavenBuildFileSource("module2/pom.xml", module2Pom)
                .addJavaSource("module2/src/main/java", javaClass)
                .addProjectResource("module2/src/main/resources/resource-found.txt", "")

                .withMavenBuildFileSource("pom.xml", rootPom)
                .addProjectResource("not-in-source-set.txt", "")


                .build();

        Module root = context.getApplicationModules().getModule("root");
        Path rootPath = TestProjectContext.getDefaultProjectRoot();
        assertThat(root.contains(rootPath.resolve("pom.xml"))).isTrue();
        assertThat(root.contains(rootPath.resolve("src/main/java/com/example/SomeClass.java"))).isFalse();

        Module module1 = context.getApplicationModules().getModule("module2");

        assertThat(module1.contains(rootPath.resolve("module2//main/java/com/example/SomeClass.java"))).isFalse();
        assertThat(
                module1.contains(rootPath.resolve("module2/src/test/java/com/example/SomeClassTest.java"))).isFalse();
        assertThat(module1.contains(rootPath.resolve("module2/src/main/resources/resource-found.txt"))).isTrue();
        assertThat(root.contains(rootPath.resolve("src/test/resources/test-resource-found.txt"))).isFalse();
        assertThat(root.contains(rootPath.resolve("not-in-source-set.txt"))).isFalse();
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForRelativePath() {
        ProjectContext context = TestProjectContext.buildProjectContext().build();
        Path relativePath = Path.of(".");
        assertThatIllegalArgumentException().isThrownBy(() -> {
            context.getApplicationModules().getModule("root").contains(relativePath);
        });
    }

}