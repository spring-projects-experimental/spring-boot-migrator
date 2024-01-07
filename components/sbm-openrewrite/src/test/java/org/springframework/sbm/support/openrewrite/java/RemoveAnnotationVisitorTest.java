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
package org.springframework.sbm.support.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.RecipeRun;
import org.openrewrite.Result;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.tree.J;
import org.springframework.rewrite.support.openrewrite.GenericOpenRewriteRecipe;
import org.springframework.sbm.java.OpenRewriteTestSupport;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class RemoveAnnotationVisitorTest {

    @Test
    void removeAnnotationOnTypeLevel() {
        String javaSource =
                "import org.junit.jupiter.api.extension.ExtendWith;\n" +
                        "import org.mockito.junit.jupiter.MockitoExtension;\n" +
                        "import org.junit.jupiter.api.Disabled;\n" +
                        "@ExtendWith(MockitoExtension.class)\n" +
                        "@Disabled\n" +
                        "public class Foo {" +
                        "}";

        String expected =
                "import org.mockito.junit.jupiter.MockitoExtension;\n" +
                        "import org.junit.jupiter.api.Disabled;\n" +
                        "\n" +
                        "@Disabled\n" +
                        "public class Foo {" +
                        "}";

        J.CompilationUnit cu = OpenRewriteTestSupport.createCompilationUnitsFromStrings(List.of("org.junit.jupiter:junit-jupiter-api:5.7.0"), javaSource).get(0);
        RemoveAnnotationVisitor sut = new RemoveAnnotationVisitor(cu.getClasses().get(0), "org.junit.jupiter.api.extension.ExtendWith");

        OpenRewriteTestSupport.verifyChange(sut, cu, expected);
    }

    @Test
    void removeAnnotationOnMethodLevel() {
        String given =
                "import javax.ejb.*;\n" +
                        "@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)\n" +
                        "public class TransactionalService {\n" +
                        "   public void requiresNewFromType() {}\n" +
                        "   @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)\n" +
                        "   public void notSupported() {}\n" +
                        "   @TransactionAttribute(TransactionAttributeType.MANDATORY)\n" +
                        "   public void mandatory() {}\n" +
                        "}";

        String expected =
                "import javax.ejb.*;\n" +
                        "@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)\n" +
                        "public class TransactionalService {\n" +
                        "   public void requiresNewFromType() {}\n" +
                        "   \n" +
                        "   public void notSupported() {}\n" +
                        "   \n" +
                        "   public void mandatory() {}\n" +
                        "}";

        J.CompilationUnit cu = OpenRewriteTestSupport.createCompilationUnitsFromStrings(List.of("javax.ejb:javax.ejb-api:3.2"), given).get(0);

        List<J.MethodDeclaration> methodDeclarationList = cu.getClasses().get(0).getBody().getStatements().stream()
                .filter(J.MethodDeclaration.class::isInstance)
                .map(J.MethodDeclaration.class::cast)
                .collect(Collectors.toList());

        J.CompilationUnit result = cu;
        for (J.MethodDeclaration md : methodDeclarationList) {
            RemoveAnnotationVisitor sut1 = new RemoveAnnotationVisitor(md, "javax.ejb.TransactionAttribute");
            RecipeRun run = new GenericOpenRewriteRecipe(() -> sut1).run(new InMemoryLargeSourceSet(List.of(result)), new InMemoryExecutionContext(t -> fail(t)));
            List<Result> allResults = run.getChangeset().getAllResults();
            if (!allResults.isEmpty()) {
                result = (J.CompilationUnit) allResults.get(0).getAfter();
            }
        }

        assertThat(result.print()).isEqualTo(expected);
    }

    @Test
    void removeAnnotationOnMemberLevel() {
        String given =
                "import javax.ejb.*;\n" +
                        "@EJB\n" +
                        "public class TransactionalService {\n" +
                        "   @EJB\n" +
                        "   private Object that;\n" +
                        "   private Object other;\n" +
                        "   @EJB\n" +
                        "   public void setEJB(Object some) {}\n" +
                        "}";

        String expected =
                "import javax.ejb.*;\n" +
                        "@EJB\n" +
                        "public class TransactionalService {\n" +
                        "   \n" +
                        "   private Object that;\n" +
                        "   private Object other;\n" +
                        "   @EJB\n" +
                        "   public void setEJB(Object some) {}\n" +
                        "}";

        J.CompilationUnit cu = OpenRewriteTestSupport.createCompilationUnitsFromStrings(List.of("javax.ejb:javax.ejb-api:3.2"), given).get(0);

        List<J.VariableDeclarations> variableDeclarations = cu.getClasses().get(0).getBody().getStatements().stream()
                .filter(J.VariableDeclarations.class::isInstance)
                .map(J.VariableDeclarations.class::cast)
                .collect(Collectors.toList());

        J.CompilationUnit result = cu;
        for (J.VariableDeclarations vd : variableDeclarations) {
            RemoveAnnotationVisitor sut1 = new RemoveAnnotationVisitor(vd, "javax.ejb.EJB");
            RecipeRun run = new GenericOpenRewriteRecipe(() -> sut1).run(new InMemoryLargeSourceSet(List.of(result)), new InMemoryExecutionContext(t -> fail(t)));
            if (!run.getChangeset().getAllResults().isEmpty()) {
                result = (J.CompilationUnit) run.getChangeset().getAllResults().get(0).getAfter();
            }
        }

        assertThat(result.print()).isEqualTo(expected);
    }
}