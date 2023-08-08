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
package org.springframework.sbm.java.impl;

import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Member;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenRewriteMemberTest {

    @Test
    void testHasAnnotation() {
        String sourceCode =
                """
                import javax.validation.constraints.Min;
                class AnnotatedClass {
                   private int var1;
                
                   @Min(1)
                   private int var2;
                }
                """;

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax.validation:validation-api:2.0.1.Final")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Member sut1 = javaSource.getTypes().get(0).getMembers().get(0);
        Member sut2 = javaSource.getTypes().get(0).getMembers().get(1);

        Assertions.assertThat(sut1.getAnnotation("javax.validation.constraints.Min")).isNull();
        Assertions.assertThat(sut2.getAnnotation("javax.validation.constraints.Min")).isNotNull();
    }

    @Test
    void testHasAnnotationWithDependency() {
        String sourceCode =
                "import javax.ejb.EJB;            \n" +
                        "class AnnotatedClass {           \n" +
                        "   @EJB                          \n" +
                        "   Object ejb;                   \n" +
                        "}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Member sut1 = javaSource.getTypes().get(0).getMembers().get(0);

        Assertions.assertThat(sut1.getAnnotation("javax.ejb.EJB")).isNotNull();
    }

    @Test
    void testAddMemberAnnotation() {
        final String sourceCode =
                "package com.foo;\n" +
                        "public class Class1 {\n" +
                        "   private String var1;\n" +
                        "   private String var2;\n" +
                        "}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax.validation:validation-api:2.0.0.Final")
                .withJavaSources(sourceCode)
                .build();

        JavaSource javaSource = projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.foo.Class1").get();
        javaSource.getTypes().get(0).getMembers().get(0).addAnnotation("javax.validation.constraints.Min");

        assertThat(javaSource.getImports()).hasSize(1);
        assertThat(javaSource.hasImportStartingWith("javax.validation.constraints.Min")).isTrue();
        assertThat(javaSource.getTypes().get(0).getMembers().get(0).getAnnotation("javax.validation.constraints.Min")).isNotNull();
        assertThat(javaSource.getTypes().get(0).getMembers().get(1).getAnnotation("javax.validation.constraints.Min")).isNull();
    }

    @Test
    void testMemberName() {
        final String sourceCode =
                "" +
                        "public class Class1 {              " +
                        "   private String var1;            " +
                        "   private String var2;            " +
                        "   private String var3, var4;      " +
                        "}                                  " +
                        "";

        JavaSource javaSource = TestProjectContext.buildProjectContext().withJavaSources(sourceCode).build().getProjectJavaSources().list().get(0);

        Assertions.assertThat(javaSource.getTypes().get(0).getMembers().get(0).getName()).isEqualTo("var1");
        Assertions.assertThat(javaSource.getTypes().get(0).getMembers().get(1).getName()).isEqualTo("var2");
        Assertions.assertThat(javaSource.getTypes().get(0).getMembers().get(2).getName()).isEqualTo("var3");
        Assertions.assertThat(javaSource.getTypes().get(0).getMembers().get(3).getName()).isEqualTo("var4");
    }

    @Test
    void testMemberTypeFqName() {
        final String sourceCode =
                "import java.util.List;\n" +
                        "public class Class1 {              " +
                        "   private String var1;            " +
                        "   private int var2;            " +
                        "   private List<String> var3;      " +
                        "}                                  " +
                        "";

        JavaSource javaSource = TestProjectContext.buildProjectContext().withJavaSources(sourceCode).build().getProjectJavaSources().list().get(0);

        Assertions.assertThat(javaSource.getTypes().get(0).getMembers().get(0).getTypeFqName()).isEqualTo("java.lang.String");
        Assertions.assertThat(javaSource.getTypes().get(0).getMembers().get(1).getTypeFqName()).isEqualTo("int");
        Assertions.assertThat(javaSource.getTypes().get(0).getMembers().get(2).getTypeFqName()).isEqualTo("java.util.List");
    }
}
