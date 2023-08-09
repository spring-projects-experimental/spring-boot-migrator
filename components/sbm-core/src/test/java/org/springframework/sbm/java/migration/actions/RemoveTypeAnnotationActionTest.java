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
package org.springframework.sbm.java.migration.actions;

import org.springframework.sbm.test.JavaMigrationActionTestSupport;
import org.junit.jupiter.api.Test;

import java.util.List;

class RemoveTypeAnnotationActionTest {

    @Test
    void removeTypeAnnotation() {
        String source =
                "import org.junit.jupiter.api.Disabled;\n" +
                        "@Disabled\n" +
                        "public class DisabledTest {}";

        String expected = "public class DisabledTest {}";

        RemoveTypeAnnotationAction sut = new RemoveTypeAnnotationAction();
        sut.setAnnotation("org.junit.jupiter.api.Disabled");

        JavaMigrationActionTestSupport.verify(source, expected, sut, "org.junit.jupiter:junit-jupiter-api:5.7.0");
    }

    @Test
    void removeMultipleTypeAnnotation() {
        String annotation =
                """
                package com.acme;
                import java.lang.annotation.Repeatable;
                @Repeatable(MultiAnnotations.class)
                public @interface MultiAnnotation {
                    
                }             
                """;

        String multiAnnotation =
                """
                package com.acme;
                public @interface MultiAnnotations {
                    MultiAnnotation[] value();
                }
                """;

        String source =
                """
                import com.acme.MultiAnnotation;
                @MultiAnnotation
                @MultiAnnotation
                @MultiAnnotation
                public class DisabledTest {}
                """;

        String expected =
                """
                public class DisabledTest {}
                """;

        RemoveTypeAnnotationAction sut = new RemoveTypeAnnotationAction();
        sut.setAnnotation("com.acme.MultiAnnotation");

        JavaMigrationActionTestSupport.verify(List.of(multiAnnotation, annotation, source), List.of(multiAnnotation, annotation, expected), sut);
    }

}