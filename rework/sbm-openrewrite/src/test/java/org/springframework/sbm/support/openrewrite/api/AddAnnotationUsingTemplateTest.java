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
package org.springframework.sbm.support.openrewrite.api;

import org.openrewrite.SourceFile;
import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.J;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AddAnnotationUsingTemplateTest {

    @Test
    void addAnnotationUsingTemplate() {
        String javaCode =
                "public class SomeClass {}";

        Stream<SourceFile> compilationUnit = JavaParser.fromJavaVersion()
                .build()
                .parse(javaCode);

        JavaIsoVisitor<Object> javaIsoVisitor = new JavaIsoVisitor<>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, Object o) {
                J.ClassDeclaration cd = super.visitClassDeclaration(classDecl, o);
                JavaTemplate template = JavaTemplate.builder("@Disabled")
                        .imports("org.junit.jupiter.api.Disabled")
                        .javaParser(JavaParser.fromJavaVersion())
                        .build();
                maybeAddImport("org.junit.jupiter.api.Disabled");
                return template.apply(getCursor().getParent(), cd.getCoordinates().addAnnotation((a1, a2) -> 0));
            }
        };

        @Nullable J classDeclaration = javaIsoVisitor.visit(compilationUnit.toList().get(0), new InMemoryExecutionContext((t) -> t.printStackTrace()));

        assertThat(classDeclaration.print()).isEqualTo(
                "@Disabled\n" +
                        "public class SomeClass {}"
        );
    }

    @Test
    @Disabled("Playground test")
    void replaceAnnotationTest() {
        String javaCode =
                "import javax.ejb.*;\n" +
                        "@TransactionManagement(TransactionManagementType.CONTAINER)\n" +
                        "@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)\n" +
                        "public class TransactionalService {\n" +
                        "   public void requiresNewFromType() {}\n" +
                        "   @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)\n" +
                        "   public void supports() {}\n" +
                        "}";

        JavaParser.Builder javaParser = OpenRewriteTestSupport.getJavaParser("org.junit.jupiter:junit-jupiter-api:5.7.1", "javax.ejb:javax.ejb-api:3.2", "org.springframework.boot:spring-boot-starter-data-jpa:2.4.2");

        Stream<SourceFile> compilationUnits = javaParser.build().parse(javaCode);

        JavaIsoVisitor<ExecutionContext> javaIsoVisitor = new JavaIsoVisitor<>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext executionContext) {
                J.ClassDeclaration cd = super.visitClassDeclaration(classDecl, executionContext);
                JavaTemplate template = JavaTemplate.builder("@Disabled")
                        .imports("org.junit.jupiter.api.Disabled")
                        .javaParser(javaParser)
                        .build();
                J.ClassDeclaration j = template.apply(getCursor().getParent(), cd.getCoordinates().addAnnotation((a1, a2) -> 0));
                maybeAddImport("org.junit.jupiter.api.Disabled");
                return j;
            }
        };

        J.CompilationUnit c = (J.CompilationUnit) javaIsoVisitor.visit(compilationUnits.toList().get(0), new InMemoryExecutionContext((t) -> new RuntimeException(t)));

        System.out.println(c.printAll());
    }

}
