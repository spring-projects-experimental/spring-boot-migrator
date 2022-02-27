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
package org.springframework.sbm.java.migration.actions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AddTypeAnnotationToTypeAnnotatedWithTest {

    @Test
    void testApplyAction() {
        String sourceCode =
                "" +
                        "import org.junit.jupiter.api.extension.ExtendWith; \n" +
                        "import org.junit.jupiter.api.Test; \n" +
                        "                                   \n" +
                        "@ExtendWith(MockitoExtension.class) \n" +
                        "@Test\n" +
                        "class AnnotatedClass {             \n" +
                        "}                                  \n";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .build();

        AddTypeAnnotationToTypeAnnotatedWith sut = new AddTypeAnnotationToTypeAnnotatedWith();
        sut.setAnnotation("org.junit.jupiter.api.Test");
        sut.setAnnotatedWith("org.junit.jupiter.api.extension.ExtendWith");

        sut.apply(projectContext);

        Assertions.assertThat(projectContext.getProjectJavaSources().list().get(0).hasImportStartingWith("org.junit.jupiter.api.Test")).isTrue();
        Assertions.assertThat(projectContext.getProjectJavaSources().list().get(0).getTypes().get(0).hasAnnotation("org.junit.jupiter.api.Test")).isTrue();
    }
}
