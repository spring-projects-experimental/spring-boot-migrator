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
package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class SpringMVCAndWebFluxUrlMatchingChangesHelperTest {
    @Test
    void findsMatches() {
        @Language("java")
        String restController1 =
                """
                package b.example;
                import org.springframework.web.bind.annotation.RestController;
                
                @RestController
                public class RestController1 {
                }
                """;

        @Language("java")
        String restController2 =
                """
                package a.example;
                import org.springframework.web.bind.annotation.RestController;
                
                @RestController
                public class RestController2 {
                }
                """;

        @Language("java")
        String anotherClass =
                """
                package com.example;
                public class AnotherClass {};
                """;

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withSpringBootParentOf("2.7.5")
                .withBuildFileHavingDependencies("org.springframework:spring-web:5.3.23")
                .withJavaSource("src/main/java", restController1)
                .withJavaSource("src/main/java", restController2)
                .withJavaSource("src/main/java", anotherClass)
                .build();

        SpringMVCAndWebFluxUrlMatchingChangesHelper sut = new SpringMVCAndWebFluxUrlMatchingChangesHelper();
        boolean evaluate = sut.evaluate(context);

        assertThat(evaluate).isTrue();
        // has restControllers sorted by path
        List<JavaSource> restCotrollers = sut.getData().get("restControllers");
        assertThat(restCotrollers).hasSize(2);
        assertThat(restCotrollers).containsExactly(
                context.getProjectJavaSources().list().get(1),
                context.getProjectJavaSources().list().get(0)
        );
    }

    @Test
    void findNoMatches() {
        @Language("java")
        String anotherClass =
                """
                package com.example;
                public class AnotherClass {};
                """;

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework:spring-web:5.3.23")
                .withJavaSource("src/main/java", anotherClass)
                .build();

        SpringMVCAndWebFluxUrlMatchingChangesHelper sut = new SpringMVCAndWebFluxUrlMatchingChangesHelper();
        boolean evaluate = sut.evaluate(context);

        assertThat(evaluate).isFalse();
        assertThat(sut.getData().get("restControllers")).isEmpty();
    }
}