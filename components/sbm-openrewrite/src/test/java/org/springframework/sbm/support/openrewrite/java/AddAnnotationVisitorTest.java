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
package org.springframework.sbm.support.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.RecipeRun;
import org.openrewrite.java.tree.J;
import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AddAnnotationVisitorTest {

    @Test
    void visitClassDeclarationWithGenericRewriteRecipe() {
        String ejbDependency = "javax.ejb:javax.ejb-api:3.2";
        String javaCode = "public class Foo {}";

        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(javaCode);

        String annotationImport = "javax.ejb.Stateless";

        J target = compilationUnit.getClasses().get(0);
        String snippet = "@Stateless(name = \"test\")";

        AddAnnotationVisitor sut = new AddAnnotationVisitor(() -> OpenRewriteTestSupport.getJavaParser(ejbDependency), target, snippet, annotationImport);
        RecipeRun run = new GenericOpenRewriteRecipe<>(() -> sut).run(List.of(compilationUnit), new InMemoryExecutionContext());
        J.CompilationUnit afterVisit = (J.CompilationUnit) run.getResults().get(0).getAfter();

        assertThat(afterVisit.printAll()).isEqualTo(
                "import javax.ejb.Stateless;\n" +
                        "\n" +
                        snippet + "\n" +
                        "public class Foo {}"
        );

        J.Annotation annotation = afterVisit.getClasses().get(0).getLeadingAnnotations().get(0);
        assertThat(annotation.getAnnotationType().getType()).isNotNull();
    }

    @Test
    void visitMethodDeclaration() {
        String code = "public class Foo {\n" +
                "    void foo() {}\n" +
                "}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);
        AddAnnotationVisitor sut = new AddAnnotationVisitor(() -> OpenRewriteTestSupport.getJavaParser("org.junit.jupiter:junit-jupiter-api:5.7.1"), compilationUnit.getClasses().get(0).getBody().getStatements().get(0), "@Test", "org.junit.jupiter.api.Test");
        RecipeRun recipeRun = new GenericOpenRewriteRecipe<>(() -> sut).run(List.of(compilationUnit));
        assertThat(recipeRun.getResults()).isNotEmpty();
        assertThat(recipeRun.getResults().get(0).getAfter().printAll()).isEqualTo(
                "import org.junit.jupiter.api.Test;\n" +
                        "\n" +
                        "public class Foo {\n" +
                        "    @Test\n" +
                        "    void foo() {}\n" +
                        "}"
        );
    }

    @Test
    void visitVariableDeclarations() {
        String code =
                "public class Foo {\n" +
                        "    private int bar;\n" +
                        "}";

        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);

        AddAnnotationVisitor sut = new AddAnnotationVisitor(OpenRewriteTestSupport.getJavaParser(), compilationUnit.getClasses().get(0).getBody().getStatements().get(0), "@Deprecated", "java.lang.Deprecated");
        RecipeRun recipeRun = new GenericOpenRewriteRecipe<>(() -> sut).run(List.of(compilationUnit));
        assertThat(recipeRun.getResults()).isNotEmpty();
        assertThat(recipeRun.getResults().get(0).getAfter().printAll()).isEqualTo(
                        "public class Foo {\n" +
                        "    @Deprecated\n" +
                        "    private int bar;\n" +
                        "}"
        );
    }

}