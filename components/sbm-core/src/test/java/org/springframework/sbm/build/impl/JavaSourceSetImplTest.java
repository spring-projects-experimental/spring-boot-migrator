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
package org.springframework.sbm.build.impl;

import org.springframework.sbm.build.api.JavaSourceSet;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.JavaSourceLocation;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class JavaSourceSetImplTest {

    @Test
    void testGetBasePackageShouldReturnDistinctRootPackageIfExists() {
        final String sourceCode1 =
                "package org.springframework.sbm.level1;   " +
                        "public class Class1 {          " +
                        "}                              " +
                        "";
        final String sourceCode2 =
                "package org.springframework.sbm.level1.level2;    " +
                        "public class Class2 {                  " +
                        "}                                      " +
                        "";

        JavaSourceSet sut = TestProjectContext.buildProjectContext()
                .withDummyRootBuildFile()
                .withJavaSources(sourceCode1, sourceCode2)
                .build()
                .getApplicationModules().getRootModule().getMainJavaSourceSet();

        JavaSourceLocation location = sut.getJavaSourceLocation();
        assertThat(location.getSourceFolder()).isEqualTo(TestProjectContext.getDefaultProjectRoot().resolve("src/main/java").normalize());
        assertThat(location.getPackageName()).isEqualTo("org.springframework.sbm.level1");
    }

    @Test
    void getBasePackageShouldReturnDefaultIfNoDistinctRootPackageExists() {
        final String sourceCode1 =
                "package org.springframework.sbm.level1;   " +
                        "public class Class1 {          " +
                        "}                              " +
                        "";
        final String sourceCode2 =
                "package org.springframework.sbm.anotherLevel1;    " +
                        "public class Class2 {                  " +
                        "}                                      " +
                        "";

        SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
        sbmApplicationProperties.setDefaultBasePackage("org.springframework.sbm");

        JavaSourceSet sut = TestProjectContext.buildProjectContext()
                .withDummyRootBuildFile()
                .withApplicationProperties(sbmApplicationProperties)
                .withJavaSources(sourceCode1, sourceCode2)
                .build()
                .getApplicationModules()
                .getRootModule()
                .getMainJavaSourceSet();

        JavaSourceLocation location = sut.getJavaSourceLocation();
        assertThat(location.getPackageName()).isEqualTo(sbmApplicationProperties.getDefaultBasePackage());
        assertThat(location.getSourceFolder()).isEqualTo(TestProjectContext.getDefaultProjectRoot().resolve("src/main/java"));
    }

    @Test
    void testGetBasePackageShouldConsiderLevelNotLength() {
        final String sourceCode1 =
                "package org.springframework.sbm.level1;   " +
                        "public class Class1 {}";
        final String sourceCode2 =
                "package org.springframework.sbmanotherLevel1;    " +
                        "public class Class2 {}";

        SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
        sbmApplicationProperties.setDefaultBasePackage("org.springframework.sbm");

        JavaSourceSet sut = TestProjectContext.buildProjectContext()
                .withDummyRootBuildFile()
                .withApplicationProperties(sbmApplicationProperties)
                .withJavaSources(sourceCode1, sourceCode2)
                .build()
                .getApplicationModules()
                .getRootModule()
                .getMainJavaSourceSet();

        JavaSourceLocation location = sut.getJavaSourceLocation();
        assertThat(location.getPackageName()).isEqualTo(sbmApplicationProperties.getDefaultBasePackage());
    }

    @Test
    void testGetBasePackageWithDefaultPackageAsRootShouldReturnDefaultPackage() {
        final String sourceCode1 =
                "public class Class1 {}";
        final String sourceCode2 =
                "package org.springframework.sbm.level1;    " +
                        "public class Class2 {}";

        JavaSourceSet sut = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode1, sourceCode2)
                .build()
                .getApplicationModules()
                .getRootModule()
                .getMainJavaSourceSet();

        JavaSourceLocation location = sut.getJavaSourceLocation();
        assertThat(location.getPackageName()).isEqualTo("");
    }

    @Test
    void testAddJavaSource() {
        final String sourceCode =
                "package org.springframework.sbm.level1;" +
                        "public class Class1 { }";

        JavaSourceSet sut = TestProjectContext.buildProjectContext()
                .build()
                .getApplicationModules()
                .getRootModule()
                .getMainJavaSourceSet();

        sut.addJavaSource(TestProjectContext.getDefaultProjectRoot(), Path.of("src/main/java"), sourceCode, "org.springframework.sbm.level1");

        Path expectedSourcePath = Path.of("src/main/java/org/springframework/sbm/level1/Class1.java");
        Path expectedAbsolutePath = TestProjectContext.getDefaultProjectRoot().resolve("src/main/java/org/springframework/sbm/level1/Class1.java");

        Assertions.assertThat(sut.list()).hasSize(1);
        JavaSource javaSource = sut.list().get(0);
        Assertions.assertThat(javaSource.getResource().hasChanges()).isTrue();
        Assertions.assertThat(javaSource.getResource().getSourcePath()).isEqualTo(expectedSourcePath);
        Assertions.assertThat(javaSource.getResource().getAbsolutePath()).isEqualTo(expectedAbsolutePath);
        Assertions.assertThat(javaSource.getResource().getAbsoluteProjectDir()).isEqualTo(TestProjectContext.getDefaultProjectRoot());
        Assertions.assertThat(javaSource.print()).isEqualTo(sourceCode);
    }


    @Test
    void testAddJavaSourceWithCompileErrors() {
        final String sourceCode =
                "package org.springframework.sbm.level2;  public class Cla ... ss2 { }";

        JavaSourceSet sut = TestProjectContext.buildProjectContext()
                .build()
                .getApplicationModules()
                .getRootModule()
                .getMainJavaSourceSet();

        sut.addJavaSource(TestProjectContext.getDefaultProjectRoot(), Path.of("src/main/java"), sourceCode, "org.springframework.sbm.level2");

        assertThat(sourceCode).isEqualTo(sut.list().get(0).print());

        Assertions.assertThat(sut.list()).hasSize(1);
        Assertions.assertThat(sut.list().get(0).getResource().hasChanges()).isTrue();
        Assertions.assertThat(sut.list().get(0).getResource().getSourcePath()).isEqualTo(Path.of("src/main/java/org/springframework/sbm/level2/Cla.java"));
        Assertions.assertThat(sut.list().get(0).getTypes().get(0).getSimpleName()).isEqualTo("Cla");
    }

}