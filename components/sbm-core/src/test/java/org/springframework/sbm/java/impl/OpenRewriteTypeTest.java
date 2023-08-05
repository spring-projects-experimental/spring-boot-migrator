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

import org.springframework.sbm.GitHubIssue;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class OpenRewriteTypeTest {

    @Test
    void testHasAnnotation() {
        String sourceCode =
                "" +
                        "import org.junit.jupiter.api.Test; \n" +
                        "                                   \n" +
                        "@Test                              \n" +
                        "class AnnotatedClass {             \n" +
                        "}                                  \n" +
                        "                                   \n" +
                        "class NotAnnotatedClass {          \n" +
                        "}                                  \n" +
                        "";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        List<? extends Type> types = javaSource.getTypes();

        assertThat(types.get(0).hasAnnotation("org.junit.jupiter.api.Test")).isTrue();
        assertThat(types.get(1).hasAnnotation("org.junit.jupiter.api.Test")).isFalse();
    }

    @Test
    void testAddAnnotation() {
        final String sourceCode =
                "import org.junit.jupiter.api.Test;\n" +
                        "@Test\n" +
                        "public class Class1 {}\n" +
                        "@Test\n" +
                        "public class Class2 {}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);
        Type type = javaSource.getTypes().get(0);

        String annotation = "org.junit.jupiter.api.BeforeEach";
        type.addAnnotation(annotation);

        Assertions.assertThat(javaSource.getTypes().get(0).hasAnnotation(annotation)).isTrue();
        Assertions.assertThat(javaSource.getTypes().get(1).hasAnnotation(annotation)).isFalse();
    }

    @Test
    void testAddAnnotationFromSnippet() {

        final String sourceCode =
                "import org.junit.jupiter.api.Test;\n" +
                        "@Test\n" +
                        "public class Class1 {}\n" +
                        "@Test\n" +
                        "public class Class2 {}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Type type = javaSource.getTypes().get(0);

        String oldAnnotation = "org.junit.jupiter.api.Test";
        String newAnnotation = "org.junit.jupiter.api.BeforeEach";
        type.addAnnotation("@BeforeEach", newAnnotation);

        assertThat(javaSource.print()).isEqualTo(
                "import org.junit.jupiter.api.BeforeEach;\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "\n" +
                        "@Test\n" +
                        "@BeforeEach\n" +
                        "public class Class1 {}\n" +
                        "@Test\n" +
                        "public class Class2 {}"
        );

        Assertions.assertThat(javaSource.getTypes().get(0).hasAnnotation(oldAnnotation)).isTrue();
        Assertions.assertThat(javaSource.getTypes().get(0).hasAnnotation(newAnnotation)).isTrue();
        Assertions.assertThat(javaSource.getTypes().get(1).hasAnnotation(oldAnnotation)).isTrue();
        Assertions.assertThat(javaSource.getTypes().get(1).hasAnnotation(newAnnotation)).isFalse();
    }


//	@Test
//	void tmpTestgetFllyQualifiedName() {
//		String sourceCode =
//				"package com.foo;\n" +
////				"import javax.ejb.Stateless;\n" +
////				"@Stateless(name = \"test\")\n" +
//				"class ControllerClass {}";
//		JavaSource javaSource = TestProjectContext.buildProjectContext()
//				.withDummyRootBuildFile()
//				.withClasspath("javax.ejb:javax.ejb-api:3.2")
//				.addJavaSource(sourceCode)
//				.build()
//				.getAllJavaSources().get(0);
//
//		J.CompilationUnit cu = javaSource.getResource().getRewriteResource();
//
//		AddAnnotationVisitor addAnnotationVisitor = new AddAnnotationVisitor(
//				cu.getClasses().get(0), "@Stateless(name = \"test\")", "javax.ejb.Stateless");
//
//
//		ExecutionContext executionContext = new RewriteExecutionContext();
//		executionContext.putMessage("java-parser", javaParser);
//		List<Result> run = new GenericOpenRewriteRecipe(addAnnotationVisitor).run(List.of(cu), executionContext);
//
//		J.CompilationUnit after = (J.CompilationUnit)run.get(0).getAfter();
//		JavaSource javaSource2 = TestProjectContext.buildProjectContext()
////				.withDummyRootBuildFile()
//				.withClasspath("javax.ejb:javax.ejb-api:3.2")
//				.addJavaSource(after.print())
//				.build().getAllJavaSources().get(0);
//		System.out.println(TypeUtils.asFullyQualified(javaSource2.getResource().getRewriteResource().getClasses().get(0).getLeadingAnnotations().get(0).getAnnotationType().getType()));
//
//
//		J.Annotation annotation = ((J.CompilationUnit) run.get(0).getAfter()).getClasses().get(0).getLeadingAnnotations().get(0);
//
//		System.out.println(TypeUtils.asFullyQualified(annotation.getAnnotationType().getType()));
//
//
//
//		Type type = javaSource.getTypes().get(0);
//
//
//
//
//
//		System.out.println(run.get(0).getAfter().print());
//
//		type.addAnnotation("@Stateless(name = \"test\")", "javax.ejb.Stateless");
//		J.CompilationUnit compilationUnit = javaSource.getResource().getRewriteResource();
//		System.out.println(compilationUnit.print());
//
////		RewriteJavaParser rewriteJavaParser = new RewriteJavaParser();
////		rewriteJavaParser.setClasspath(List.of(Path.of("/Users/fkrueger/.m2/repository/javax/ejb/javax.ejb-api/3.2/javax.ejb-api-3.2.jar")));
////		J.CompilationUnit compilationUnit = rewriteJavaParser.parse(sourceCode).get(0);
//		System.out.println(compilationUnit.getClasses().get(0).getType().getFullyQualifiedName());
//		System.out.println(TypeUtils.asFullyQualified(((JavaType)compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0).getAnnotationType().getType())).getFullyQualifiedName());
//	}

    @Test
    void testAddAnnotationWithParams() {
        String sourceCode =
                "class ControllerClass {}";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2")
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);
        Type type = javaSource.getTypes().get(0);

        type.addAnnotation("@Stateless(name = \"test\")", "javax.ejb.Stateless");

        Assertions.assertThat(type.getAnnotations()).hasSize(1);
        Assertions.assertThat(type.getAnnotations().get(0).getFullyQualifiedName()).isEqualTo("javax.ejb.Stateless");
        Assertions.assertThat(type.getAnnotations().get(0).getAttribute("name").printAssignmentValue()).isEqualTo("test");
    }

    @Test
    void testRemoveAnnotation() {

        final String sourceCode =
                "import org.junit.jupiter.api.Test;\n" +
                        "@Test\n" +
                        "public class Class1 {}\n" +
                        "@Test\n" +
                        "public class Class2 {}";

        String removeAnnotation = "org.junit.jupiter.api.Test";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Type class1 = javaSource.getTypes().get(0);
        Type class2 = javaSource.getTypes().get(1);

        class1.removeAnnotation(removeAnnotation);

        Assertions.assertThat(class1.hasAnnotation(removeAnnotation)).isFalse();
        Assertions.assertThat(class2.hasAnnotation(removeAnnotation)).isTrue();
    }

    @Test
    void shouldRemoveAnnotationWithParams() {
        String sourceCode =
                "import org.junit.jupiter.api.Tag;\n" +
                        "@Tag(\"blah\") public class Foo {}";

        String removeAnnotation = "org.junit.jupiter.api.Tag";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);
        Type type = javaSource.getTypes().get(0);

        Assertions.assertThat(type.getAnnotations()).hasSize(1);

        type.removeAnnotation(removeAnnotation);

        Assertions.assertThat(type.getAnnotations()).isEmpty();
    }

    @Test
    void testAddMethod() {
        String template =
                "@Bean\n" +
                "IntegrationFlow http_routeFlow() {\n" +
                "return IntegrationFlows.from(Http.inboundChannelAdapter(\"/test\")).handle((p, h) -> p)\n" +
                ".log(LoggingHandler.Level.INFO)\n" +
                ".get();\n" +
                "}\n";

        Set<String> requiredImports = Set.of("org.springframework.integration.transformer.ObjectToStringTransformer",
                "org.springframework.context.annotation.Configuration",
                "org.springframework.integration.amqp.dsl.Amqp",
                "org.springframework.integration.handler.LoggingHandler",
                "org.springframework.integration.dsl.IntegrationFlow",
                "org.springframework.integration.dsl.IntegrationFlows",
                "org.springframework.context.annotation.Bean",
                "org.springframework.integration.http.dsl.Http");

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.integration:spring-integration-http:5.4.4")
                .withJavaSource("src/main/java/Config.java", "public class Config {}")
                .build();

        Type type = context.getProjectJavaSources().list().get(0).getTypes().get(0);
        type.addMethod(template, requiredImports);

        System.out.println(context.getProjectJavaSources().list().get(0).print());
        assertThat(context.getProjectJavaSources().list().get(0).print()).isEqualTo(
                "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                        "import org.springframework.integration.handler.LoggingHandler;\n" +
                        "import org.springframework.integration.http.dsl.Http;\n" +
                        "\n" +
                        "public class Config {\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow http_routeFlow() {\n" +
                        "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/test\")).handle((p, h) -> p)\n" +
                        "                .log(LoggingHandler.Level.INFO)\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "}"
        );
    }

    @Test
    void testAddMethod2() {
        String template =
                "@Bean\n" +
                        "IntegrationFlow http_routeFlow() {\n" +
                        "return IntegrationFlows.from(Http.inboundChannelAdapter(\"/test\")).handle((p, h) -> p)\n" +
                        ".log(LoggingHandler.Level.INFO)\n" +
                        ".get();\n" +
                        "}\n";

        Set<String> requiredImports = Set.of("org.springframework.integration.transformer.ObjectToStringTransformer",
                "org.springframework.context.annotation.Configuration",
                "org.springframework.integration.amqp.dsl.Amqp",
                "org.springframework.integration.handler.LoggingHandler",
                "org.springframework.integration.dsl.IntegrationFlow",
                "org.springframework.integration.dsl.IntegrationFlows",
                "org.springframework.context.annotation.Bean",
                "org.springframework.integration.http.dsl.Http");

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.boot:spring-boot-starter-web:2.5.5")
                .withJavaSource("src/main/java/Config.java", "public class Config {}")
                .build();

        long before = System.currentTimeMillis();

        context.getBuildFile().addDependency(Dependency.builder()
                        .groupId("org.springframework.integration")
                        .artifactId("spring-integration-http")
                        .version("5.4.4")
                .build());

        long timeSpent = System.currentTimeMillis() - before;
        System.out.println("Adding a dependency took: " + (timeSpent/1000) + " sec.");

        Type type = context.getProjectJavaSources().list().get(0).getTypes().get(0);
        type.addMethod(template, requiredImports);

        assertThat(context.getProjectJavaSources().list().get(0).print()).isEqualTo(
                "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                        "import org.springframework.integration.handler.LoggingHandler;\n" +
                        "import org.springframework.integration.http.dsl.Http;\n" +
                        "\n" +
                        "public class Config {\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow http_routeFlow() {\n" +
                        "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/test\")).handle((p, h) -> p)\n" +
                        "                .log(LoggingHandler.Level.INFO)\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "}"
        );
    }

    @Test
    void testRemoveSomeImplements() {
        final String sourceCode =
                "import java.util.Iterator;\n"
                        + "\n"
                        + "public class Subclass<T> implements Iterator<T>, Comparable<T> {\n"
                        + "\n"
                        + "	public boolean hasNext() {\n"
                        + "		return false;\n"
                        + "	}\n"
                        + "\n"
                        + "	public T next() {\n"
                        + "		return null;\n"
                        + "	}\n"
                        + "\n"
                        + "	public int compareTo(Object o) {\n"
                        + "		return 0;\n"
                        + "	}\n"
                        + "\n"
                        + "}\n"
                        + "";

        final String expected =
                "public class Subclass<T> implements Comparable<T> {\n"
                        + "\n"
                        + "	public boolean hasNext() {\n"
                        + "		return false;\n"
                        + "	}\n"
                        + "\n"
                        + "	public T next() {\n"
                        + "		return null;\n"
                        + "	}\n"
                        + "\n"
                        + "	public int compareTo(Object o) {\n"
                        + "		return 0;\n"
                        + "	}\n"
                        + "\n"
                        + "}\n"
                        + "";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        javaSource.getTypes().get(0).removeImplements("java.util.Iterator");

        Assertions.assertThat(javaSource.print())
                .as(TestDiff.of(javaSource.print(), expected))
                .isEqualTo(expected);
    }

    @Test
    void testRemoveAllImplements() {
        final String sourceCode =
                "import java.util.Iterator;\n"
                        + "\n"
                        + "public class Subclass<T> implements Iterator<T>, Comparable<T> {\n"
                        + "\n"
                        + "	public boolean hasNext() {\n"
                        + "		return false;\n"
                        + "	}\n"
                        + "\n"
                        + "	public T next() {\n"
                        + "		return null;\n"
                        + "	}\n"
                        + "\n"
                        + "	public int compareTo(Object o) {\n"
                        + "		return 0;\n"
                        + "	}\n"
                        + "\n"
                        + "}\n"
                        + "";

        final String expected =
                "public class Subclass<T> {\n"
                        + "\n"
                        + "	public boolean hasNext() {\n"
                        + "		return false;\n"
                        + "	}\n"
                        + "\n"
                        + "	public T next() {\n"
                        + "		return null;\n"
                        + "	}\n"
                        + "\n"
                        + "	public int compareTo(Object o) {\n"
                        + "		return 0;\n"
                        + "	}\n"
                        + "\n"
                        + "}\n"
                        + "";

        JavaSource javaSource = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        javaSource.getTypes().get(0).removeImplements("java.util.Iterator", "java.lang.Comparable");

        Assertions.assertThat(javaSource.print())
                .as(TestDiff.of(javaSource.print(), expected))
                .isEqualTo(expected);
    }

    @Test
    void testGetImplements() {
        String theInterface =
                "package example.foo;\n" +
                        "public interface TheInterface {}";

        String theOtherInterface =
                "package example.bar;\n" +
                        "public interface TheOtherInterface {}";

        String theClass =
                "package example.baz;\n" +
                        "import example.foo.TheInterface;\n" +
                        "import example.bar.TheOtherInterface;\n" +
                        "public class TheClass implements TheInterface, TheOtherInterface {}";


        Type theClassType = TestProjectContext.buildProjectContext()
                .withJavaSources(theInterface, theOtherInterface, theClass)
                .build()
                .getProjectJavaSources()
                .list()
                .get(2)
                .getTypes().get(0);

        assertThat(theClassType.getImplements()).hasSize(2);
        assertThat(theClassType.getImplements().get(0).getFullyQualifiedName()).isEqualTo("example.foo.TheInterface");
    }

    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/239")
    @Test
    void testGetImplements2() {

        String theClass =
                "import javax.jws.WebService;\n" +
                        "import java.rmi.Remote;\n" +
                        "import java.rmi.RemoteException;\n" +
                        "\n" +
                        "@WebService(targetNamespace = \"http://superbiz.org/wsdl\")\n" +
                        "public interface CalculatorWs extends Remote {\n" +
                        "\n" +
                        "    public int sum(int add1, int add2);\n" +
                        "\n" +
                        "    public int multiply(int mul1, int mul2);\n" +
                        "}";

        List<JavaSource> javaSources = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.apache.tomee.bom:tomee-plus-api:8.0.7")
                .withJavaSources(theClass)
                .build()
                .getProjectJavaSources()
                .list();

        Type theClassType = javaSources.get(0).getTypes().get(0);

        assertThat(theClassType.getImplements()).hasSize(1);
        assertThat(theClassType.getImplements().get(0)).isNotNull();
        assertThat(theClassType.getImplements().get(0)).isExactlyInstanceOf(CompiledType.class);
        assertThat(theClassType.getImplements().get(0).getFullyQualifiedName()).isEqualTo("java.rmi.Remote");
    }

}
