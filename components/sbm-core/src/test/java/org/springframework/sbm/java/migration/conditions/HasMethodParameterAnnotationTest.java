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

class HasMethodParameterAnnotationTest {

    @Test
    void testIsApplicableTRUE() {
        String sourceCode =
                "" +
                        "import javax.ws.rs.PathParam; " +
                        "                                   " +
                        "class AnnotatedClass {             " +
                        "    public foo(@PathParam(\"name\") String name) {\n" +
                        "    }\n" +
                        "}";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("javax.ws.rs:javax.ws.rs-api:2.1.1")
                .build();

        HasAnnotation sut = new HasAnnotation();
        sut.setAnnotation("@javax.ws.rs.PathParam");

        assertThat(sut.evaluate(context)).isTrue();
    }

    @Test
    void testIsApplicableFALSE() {
        String sourceCode =
                "" +
                        "import org.junit.jupiter.api.BeforeEach; " +
                        "                                   " +
                        "@BeforeEach                        " +
                        "class AnnotatedClass {             " +
                        "}                                  " +
                        "";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .build();

        HasTypeAnnotation sut = new HasTypeAnnotation();
        sut.setAnnotation("org.junit.jupiter.api.Test");

        assertThat(sut.evaluate(context)).isFalse();
    }
}
