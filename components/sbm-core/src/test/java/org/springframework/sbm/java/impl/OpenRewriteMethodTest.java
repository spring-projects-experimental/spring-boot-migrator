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
package org.springframework.sbm.java.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.java.api.*;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenRewriteMethodTest {

    @Test
    void testAddAnnotation() {
        String sourceCode =
                "import org.junit.jupiter.api.BeforeAll;\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "public class Foo {\n" +
                        "   @Test\n" +
                        "   @BeforeAll\n" +
                        "   void bar() {\n" +
                        "   }\n" +
                        "}";

        String expected =
                "import org.junit.jupiter.api.BeforeAll;\n" +
                        "import org.junit.jupiter.api.BeforeEach;\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "\n" +
                        "public class Foo {\n" +
                        "    @Test\n" +
                        "    @BeforeAll\n" +
                        "    @BeforeEach\n" +
                        "    void bar() {\n" +
                        "   }\n" +
                        "}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        javaSource.getTypes().stream()
                .map(Type::getMethods)
                .flatMap(List::stream)
                .forEach(m -> m.addAnnotation("\n@BeforeEach", "org.junit.jupiter.api.BeforeEach"));

        Assertions.assertThat(javaSource.print())
                .as(TestDiff.of(javaSource.print(), expected))
                .isEqualTo(expected);
    }

    @Test
    void testAddAnnotationWithParameter() {
        String sourceCode =
                "public class Foo {\n" +
                        "public void mytest() {}\n" +
                        "}";
        String expected =
                "import org.junit.jupiter.api.Order;\n" +
                        "\n" +
                        "public class Foo {\n" +
                        "    @Order(value = Integer.MAX_VALUE)\n" +
                        "    public void mytest() {}\n" +
                        "}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Method method = javaSource.getTypes().get(0).getMethods().get(0);

        method.addAnnotation("@Order(value=Integer.MAX_VALUE)", "org.junit.jupiter.api.Order", "java.lang.Integer");

        Assertions.assertThat(javaSource.print())
                .as(TestDiff.of(javaSource.print(), expected))
                .isEqualTo(expected);
    }

    @Test
    void testRemoveAnnotation() {
        String sourceCode =
                "import org.junit.jupiter.api.BeforeAll;\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "public class Foo {\n" +
                        "    @Test\n" +
                        "    @BeforeAll\n" +
                        "    void bar() {\n" +
                        "    }\n" +
                        "}";

        String expected =
                "import org.junit.jupiter.api.BeforeAll;\n" +
                        "public class Foo {\n" +
                        "    \n" +
                        "    @BeforeAll\n" +
                        "    void bar() {\n" +
                        "    }\n" +
                        "}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Method method = javaSource.getTypes().get(0).getMethods().get(0);

        method.removeAnnotation(method.getAnnotations().get(0));

        Assertions.assertThat(javaSource.print())
                .as(TestDiff.of(javaSource.print(), expected))
                .isEqualTo(expected);
    }

    @Test
    @Disabled("FIXME: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/200")
    void removeMethodAnnotationsFromDependency() {
        String given =
                "import javax.ejb.*;\n" +
                        "@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)\n" +
                        "public class TransactionalService {\n" +
                        "    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)\n" +
                        "    public void notSupported() {}\n" +
                        "}";

        String expected =
                "import javax.ejb.*;\n" +
                        "@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)\n" +
                        "public class TransactionalService {\n" +
                        "    \n" +
                        "    public void notSupported() {}\n" +
                        "}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2", "org.springframework.data:spring-data-jpa:2.6.1")
                .withJavaSources(given)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Annotation transactionAttribute = javaSource.getTypes().get(0).getMethods().get(0).getAnnotations().get(0);
        javaSource.getTypes().get(0).getMethods().get(0).removeAnnotation(transactionAttribute);

        assertThat(javaSource.print()).isEqualTo(expected);
    }

    @Test
    void testContainsAnnotation() {
        String sourceCode =
                "import org.junit.jupiter.api.BeforeAll;\n" +
                        "public class Foo {                     \n" +
                        "   @BeforeAll                          \n" +
                        "   void bar() {                        \n" +
                        "   }                                   \n" +
                        "}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Method method = javaSource.getTypes().get(0).getMethods().get(0);

        assertThat(method.containsAnnotation(Pattern.compile("org\\.junit\\.jupiter\\.api\\..*"))).isTrue();
        assertThat(method.containsAnnotation(Pattern.compile("org\\.junit\\.jupiter\\.api\\.Test"))).isFalse();
    }

    @Test
    void testImplicitDefaultMethod() {
        String sourceCode =
                "public class Foo {                     \n" +
                        "   void bar() {                        \n" +
                        "   }                                   \n" +
                        "}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Method method = javaSource.getTypes().get(0).getMethods().get(0);

        assertThat(method.getVisibility()).isEqualTo(Visibility.DEFAULT);
    }

    @Test
    void testOtherVisibilityMethod() {
        String sourceCode =
                "public class Foo {                     \n" +
                        "   public void foo1() {                        \n" +
                        "   }                                   \n" +
                        "   protected void foo2() {                        \n" +
                        "   }                                   \n" +
                        "   private void foo3() {                        \n" +
                        "   }                                   \n" +
                        "   default void foo4() {                        \n" +
                        "   }                                   \n" +
                        "}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Type type = javaSource.getTypes().get(0);

        assertThat(type.getMethods().get(0).getVisibility()).isEqualTo(Visibility.PUBLIC);
        assertThat(type.getMethods().get(1).getVisibility()).isEqualTo(Visibility.PROTECTED);
        assertThat(type.getMethods().get(2).getVisibility()).isEqualTo(Visibility.PRIVATE);
        assertThat(type.getMethods().get(3).getVisibility()).isEqualTo(Visibility.DEFAULT);
    }

}
