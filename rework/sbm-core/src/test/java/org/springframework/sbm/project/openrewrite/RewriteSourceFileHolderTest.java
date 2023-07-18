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
package org.springframework.sbm.project.openrewrite;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.tree.J;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class RewriteSourceFileHolderTest {

    public static final Path PROJECT_DIR = TestProjectContext.getDefaultProjectRoot();
    public static final String SOURCE_CODE = "package com.foo.bar; class Foo{}";

    private ProjectContext projectContext = TestProjectContext.buildProjectContext()
            .withJavaSource("src/main/java", SOURCE_CODE)
            .build();

    private RewriteSourceFileHolder<J.CompilationUnit> sut = projectContext
            .getProjectJavaSources()
            .list()
            .get(0)
            .getResource();

    @Test
    void replaceWithShouldMarkAsChanged_whenContentDiffers() {
        J.CompilationUnit newSourceFile = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java", "package com.foo.bar; class Bar{}")
                .build()
                .getProjectJavaSources()
                .list()
                .get(0)
                .getResource()
                .getSourceFile();

        sut.replaceWith(newSourceFile);

        assertThat(sut.hasChanges()).isTrue();
        assertThat(sut.getSourceFile()).isSameAs(newSourceFile);
    }

    @Test
    void replaceWithShouldNotMarkAsChanged_WhenContentEquals() {
        String sourceCode = "class Foo{}";
        J.CompilationUnit newSourceFile = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java", SOURCE_CODE)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0)
                .getResource()
                .getSourceFile();

        sut.replaceWith(newSourceFile);

        assertThat(sut.hasChanges()).isFalse();
        assertThat(sut.getSourceFile()).isSameAs(newSourceFile);
    }

    @Test
    void testSourcePath() {
        Path sourcePath = sut.getSourcePath();
        assertThat(sourcePath).isEqualTo(Path.of("src/main/java/com/foo/bar/Foo.java"));
    }

    @Test
    void testGetAbsolutePath() {
        Path absolutePath = sut.getAbsolutePath();
        assertThat(absolutePath).isEqualTo(PROJECT_DIR.resolve("src/main/java/com/foo/bar/Foo.java"));
    }

    @Test
    void testMoveTo_withRelativePath() {
        sut.moveTo(Path.of("foo/Bar.java"));
        assertThat(sut.getAbsolutePath()).isEqualTo(PROJECT_DIR.resolve("foo/Bar.java"));
        assertThat(sut.print()).isEqualTo(SOURCE_CODE);
        assertThat(sut.hasChanges()).isTrue();
    }

    @Test
    void testMoveTo_withAbsolutePath() {
        sut.moveTo(PROJECT_DIR.resolve("foo/Foo.java"));
        assertThat(sut.getAbsolutePath()).isEqualTo(PROJECT_DIR.resolve("foo/Foo.java"));
        assertThat(sut.print()).isEqualTo(SOURCE_CODE);
        assertThat(sut.hasChanges()).isTrue();
    }
}