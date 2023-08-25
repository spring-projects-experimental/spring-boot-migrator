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

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HasMemberAnnotationTest {

    @Test
    void testIsApplicableTRUE() {
        String sourceCode =
                "" +
                        "import org.junit.jupiter.api.Test; " +
                        "                                   " +
                        "class AnnotatedClass {             " +
                        "   @Test                           " +
                        "   private int var1;" +
                        "}                                  " +
                        "";

        ProjectContext context = TestProjectContext.buildProjectContext()
                        .withJavaSources(sourceCode)
                        .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                        .build();

        HasMemberAnnotation sut = new HasMemberAnnotation();
        sut.setAnnotation("org.junit.jupiter.api.Test");

        assertThat(sut.evaluate(context)).isTrue();
    }

    @Test
    void testIsApplicableFALSE() {
        String sourceCode =
                """
                import javax.validation.constraints.Min;
                class AnnotatedClass {
                   @Min(1)
                   private int var1;
                }
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("javax.validation:validation-api:2.0.1.Final")
                .build();

        HasMemberAnnotation sut = new HasMemberAnnotation();
        sut.setAnnotation("javax.validation.constraints.Max");

        assertThat(sut.evaluate(context)).isFalse();
    }
}
