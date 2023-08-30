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
package org.springframework.sbm.java.util;

import org.springframework.sbm.java.api.JavaSource;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JavaSourceUtilTest {

    @Test
    void calculateBasePackageWithSingleJavaSource() {
        String package1 = "com.example.foo";

        List<JavaSource> javaSources = getJavaSources(package1);

        JavaSource javaSourceInBasePackage = JavaUtil.calculateBasePackage(javaSources);

        assertThat(javaSourceInBasePackage.getPackageName()).isEqualTo(package1);
    }

    @Test
    void calculateBasePackageWithTwoAmbiguousPackages() {
        String package1 = "com.example.foo";
        String package2 = "org.foo";
        String package3 = "org.foo.blah.blub";

        List<JavaSource> javaSources = getJavaSources(package1, package2, package3);

        Path curDir = Path.of(".").toAbsolutePath().normalize();
        assertThatThrownBy(() -> JavaUtil.calculateBasePackage(javaSources));
    }

    @Test
    void calculateBasePackageWithOneBasePackage() {
        String package1 = "com.example.foo";
        String package2 = "com.example.foo.a";
        String package3 = "com.example.foo";

        List<JavaSource> javaSources = getJavaSources(package1, package2, package3);

        JavaSource javaSourceLocation = JavaUtil.calculateBasePackage(javaSources);

        assertThat(javaSourceLocation.getPackageName()).isEqualTo(package1);
    }

    @Test
    void calculateBasePackageWithOneBasePackage2() {
        String package1 = "com.example.foo";
        String package2 = "com.example.foo";
        String package3 = "com.example.bar";

        List<JavaSource> javaSources = getJavaSources(package1, package2, package3);

        assertThatThrownBy(() -> JavaUtil.calculateBasePackage(javaSources));
    }

    @Test
    void calculateBasePackageWithEmptyList() {
        List<JavaSource> javaSources = List.of();
        assertThatThrownBy(() -> JavaUtil.calculateBasePackage(javaSources));
    }

    @Test
    void calculateBasePackageWithAmbiguousBasePackage() {
        String package1 = "com.example.foo";
        String package2 = "com.example.bar";
        String package3 = "com.example.foo.b";

        List<JavaSource> javaSources = getJavaSources(package1, package2, package3);

        Path curDir = Path.of(".").toAbsolutePath().normalize();
        assertThatThrownBy(() -> JavaUtil.calculateBasePackage(javaSources));
    }

    @NotNull
    private List<JavaSource> getJavaSources(String... packages) {
        Random random = new Random();
        return Stream.of(packages).map(p -> {
            JavaSource js1 = mock(JavaSource.class);
            when(js1.getPackageName()).thenReturn(p);
            when(js1.getSourceFolder()).thenReturn(Path.of("src/main/java").resolve(p.replace(".", "/")).resolve("SomeClass" + random.nextInt(100) + ".java").toAbsolutePath());
            when(js1.getAbsolutePath()).thenReturn(Path.of("src/main/java").resolve(p.replace(".", "/")).resolve("SomeClass" + random.nextInt(100) + ".java").toAbsolutePath());
            return js1;
        }).collect(Collectors.toList());
    }
}