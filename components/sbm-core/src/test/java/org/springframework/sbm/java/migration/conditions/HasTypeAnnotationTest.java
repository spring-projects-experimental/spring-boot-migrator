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
package org.springframework.sbm.java.migration.conditions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HasTypeAnnotationTest {

    @ParameterizedTest
    @CsvSource({
            "org.springframework.context.annotation.Configuration, true",
            "org.springframework.context.annotation.DependsOn, false",
    })
    void testIsApplicableTRUE(String searched, boolean expected) {
        String sourceCode =
                """
                import org.springframework.context.annotation.Configuration;
                @Configuration
                class AnnotatedClass {
                }
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("org.springframework:spring-context:6.0.1")
                .build();

        HasTypeAnnotation sut = new HasTypeAnnotation();
        sut.setAnnotation(searched);
        assertThat(sut.evaluate(context)).isEqualTo(expected);
    }
}
