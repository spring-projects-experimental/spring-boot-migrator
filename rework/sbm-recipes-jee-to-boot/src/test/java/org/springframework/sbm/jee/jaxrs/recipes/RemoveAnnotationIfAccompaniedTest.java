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
package org.springframework.sbm.jee.jaxrs.recipes;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class RemoveAnnotationIfAccompaniedTest {
    private final static String SPRING_VERSION = "5.3.13";

    @Test
    void givenBothAnnotationsArePresentOnTheFirstMethodParameter_thenTheAnnotationIsRemoved() {
        String sourceCode = """                
                import org.springframework.web.bind.annotation.RequestParam;
                import javax.ws.rs.DefaultValue;
                                
                class ControllerClass {
                    public String test(@DefaultValue("default-value") @RequestParam(value = "q") String searchString) {
                        return "Hello";
                    }
                }
                """;

        String expected = """
                import org.springframework.web.bind.annotation.RequestParam;
                                
                class ControllerClass {
                    public String test(@RequestParam(value = "q") String searchString) {
                        return "Hello";
                    }
                }
                """;

        testRemoveAnnotation(sourceCode, expected);
    }

    @Test
    void givenBothAnnotationsArePresentOnTheFirstMethodParameterInReverseOrder_thenTheAnnotationIsRemoved() {
        String sourceCode = """                
                import org.springframework.web.bind.annotation.RequestParam;
                import javax.ws.rs.DefaultValue;
                                
                class ControllerClass {
                    public String test(@RequestParam(value = "q") @DefaultValue("default-value") String searchString) {
                        return "Hello";
                    }
                }
                """;

        String expected = """
                import org.springframework.web.bind.annotation.RequestParam;
                                
                class ControllerClass {
                    public String test(@RequestParam(value = "q") String searchString) {
                        return "Hello";
                    }
                }
                """;

        testRemoveAnnotation(sourceCode, expected);
    }

    @Test
    void givenBothAnnotationsArePresentOnTheFirstMethodParameterAndPrecededByAnotherOne_thenTheAnnotationIsRemoved() {
        String sourceCode = """                
                import org.springframework.web.bind.annotation.RequestParam;
                import jakarta.validation.constraints.NotNull;
                import javax.ws.rs.DefaultValue;
                                
                class ControllerClass {
                    public String test(@NotNull @DefaultValue("default-value") @RequestParam(value = "q") String searchString) {
                        return "Hello";
                    }
                }
                """;

        String expected = """
                import org.springframework.web.bind.annotation.RequestParam;
                import jakarta.validation.constraints.NotNull;
                                
                class ControllerClass {
                    public String test(@NotNull @RequestParam(value = "q") String searchString) {
                        return "Hello";
                    }
                }
                """;

        testRemoveAnnotation(sourceCode, expected);
    }

    @Test
    void givenBothAnnotationsArePresentOnTheSecondMethodParameter_thenTheAnnotationIsRemoved() {
        String sourceCode = """                
                import org.springframework.web.bind.annotation.RequestParam;
                import javax.ws.rs.DefaultValue;
                                
                class ControllerClass {
                    public String test(@RequestParam String name, @DefaultValue("default-value") @RequestParam(value = "q") String searchString) {
                        return "Hello";
                    }
                }
                """;

        String expected = """
                import org.springframework.web.bind.annotation.RequestParam;
                                
                class ControllerClass {
                    public String test(@RequestParam String name, @RequestParam(value = "q") String searchString) {
                        return "Hello";
                    }
                }
                """;

        testRemoveAnnotation(sourceCode, expected);
    }

    @Test
    void givenBothAnnotationsArePresentButOnDifferentMethodParameters_thenNothingChanges() {
        String sourceCode = """                
                import org.springframework.web.bind.annotation.RequestHeader;
                import org.springframework.web.bind.annotation.RequestParam;
                import javax.ws.rs.DefaultValue;
                                
                class ControllerClass {
                    public String test(@RequestParam String name, @DefaultValue("default-value") @RequestHeader String myHeader) {
                        return "Hello";
                    }
                }
                """;

        testRemoveAnnotation(sourceCode, sourceCode);
    }

    @Test
    void givenOnlyAnnotationToRemoveIsPresent_thenNothingChanges() {
        String sourceCode = """                
                import org.springframework.web.bind.annotation.RequestHeader;
                import javax.ws.rs.DefaultValue;
                                
                class ControllerClass {
                    public String test(@DefaultValue("default-value") @RequestHeader String myHeader) {
                        return "Hello";
                    }
                }
                """;

        testRemoveAnnotation(sourceCode, sourceCode);
    }

    @Test
    void givenMethodWithoutParameters_thenNothingChanges() {
        String sourceCode = """                
                import org.springframework.web.bind.annotation.RequestHeader;
                import javax.ws.rs.DefaultValue;
                                
                class ControllerClass {
                    public String test() {
                        return "Hello";
                    }
                }
                """;

        testRemoveAnnotation(sourceCode, sourceCode);
    }

    private void testRemoveAnnotation(String sourceCode, String expected) {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies(
                        "jakarta.ws.rs:jakarta.ws.rs-api:2.1.6",
                        "jakarta.validation:jakarta.validation-api:2.0.2",
                        "org.springframework:spring-web:" + SPRING_VERSION
                )
                .build();

        RemoveAnnotationIfAccompanied sut = new RemoveAnnotationIfAccompanied(
                "javax.ws.rs.DefaultValue", "org.springframework.web.bind.annotation.RequestParam");

        JavaSource javaSource = projectContext.getProjectJavaSources().list().get(0);
        javaSource.apply(sut);

        assertThat(javaSource.print()).isEqualTo(expected);
    }
}