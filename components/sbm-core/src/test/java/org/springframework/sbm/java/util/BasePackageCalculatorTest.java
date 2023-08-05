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
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BasePackageCalculatorTest {

    public static final String DEFAULT_PACKAGENAME = "the.default.packagename";
    private BasePackageCalculator sut;
    @BeforeEach
    void beforeEach() {
        SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
        sbmApplicationProperties.setDefaultBasePackage(DEFAULT_PACKAGENAME);
        sut = new BasePackageCalculator(sbmApplicationProperties);
    }


    @Test
    void calculateBasePackageWithSingleJavaSource() {
        String package1 = "com.example.foo";

        List<JavaSource> javaSources = getJavaSources(package1);

        String javaSourceInBasePackage = sut.calculateBasePackage(javaSources);

        assertThat(javaSourceInBasePackage).isEqualTo(package1);
    }

    @Test
    void calculateBasePackageWithTwoAmbiguousPackages() {
        String package1 = "com.example.foo";
        String package2 = "org.foo";
        String package3 = "org.foo.blah.blub";

        List<JavaSource> javaSources = getJavaSources(package1, package2, package3);

        assertThat(sut.calculateBasePackage(javaSources)).isEqualTo("");
    }

    @Test
    void calculateBasePackageWithOneBasePackage() {
        String package1 = "com.example.foo";
        String package2 = "com.example.foo.a";
        String package3 = "com.example.foo";

        List<JavaSource> javaSources = getJavaSources(package1, package2, package3);

        String javaSourceLocation = sut.calculateBasePackage(javaSources);

        assertThat(javaSourceLocation).isEqualTo(package1);
    }

    @Test
    void calculateBasePackageWithOneBasePackage2() {
        String package1 = "com.example.foo";
        String package2 = "com.example.foo";
        String package3 = "com.example.bar";
        List<JavaSource> javaSources = getJavaSources(package1, package2, package3);

        assertThat(sut.calculateBasePackage(javaSources)).isEqualTo("com.example");
    }

    @Test
    void calculateBasePackageWithOneBasePackage3() {
        String package1 = "com.acme.some.A.C";
        String package2 = "com.acme.other.B";

        List<JavaSource> javaSources = getJavaSources(package1, package2);

        assertThat(sut.calculateBasePackage(javaSources)).isEqualTo("com.acme");
    }

    @Test
    void calculateBasePackageWithSameBasePackages() {
        String package1 = "com.acme.some.B";
        String package2 = "com.acme.some.B";

        List<JavaSource> javaSources = getJavaSources(package1, package2);

        assertThat(sut.calculateBasePackage(javaSources)).isEqualTo("com.acme.some.B");
    }

    @Test
    void calculateBasePackageWithEmptyList() {
        List<JavaSource> javaSources = List.of();
        assertThat(sut.calculateBasePackage(javaSources)).isEqualTo(DEFAULT_PACKAGENAME);
    }

    @Test
    void calculateBasePackageWithAmbiguousBasePackage() {
        String package1 = "com.example.foo";
        String package2 = "com.example.bar";
        String package3 = "com.example.foo.b";

        List<JavaSource> javaSources = getJavaSources(package1, package2, package3);

        Path curDir = Path.of(".").toAbsolutePath().normalize();
        assertThat(sut.calculateBasePackage(javaSources)).isEqualTo("com.example");
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