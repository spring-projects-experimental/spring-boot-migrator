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

import org.intellij.lang.annotations.Language;
import org.openrewrite.RecipeRun;
import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openrewrite.java.tree.J;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RemoveAnnotationVisitorTest {

    @Test
    void removeAnnotationOnTypeLevel() {
        @Language("java")
        String javaSource = """
                import org.junit.jupiter.api.extension.ExtendWith;
                import org.mockito.junit.jupiter.MockitoExtension;
                import org.junit.jupiter.api.Disabled;
                 
                @ExtendWith(MockitoExtension.class)
                @Disabled
                public class Foo {}
                """;

        @Language("java")
        String expected = """
                import org.mockito.junit.jupiter.MockitoExtension;
                import org.junit.jupiter.api.Disabled;
                
                
                @Disabled
                public class Foo {}
                """;

        J.CompilationUnit cu = OpenRewriteTestSupport.createCompilationUnitsFromStrings(List.of("org.junit.jupiter:junit-jupiter-api:5.7.0"), javaSource).get(0);
        RemoveAnnotationVisitor sut = new RemoveAnnotationVisitor(cu.getClasses().get(0), "org.junit.jupiter.api.extension.ExtendWith");

        OpenRewriteTestSupport.verifyChange(sut, cu, expected);
    }

    @Test
    void removeAnnotationOnMethodLevel() {
        @Language("java")
        String given = """
                import javax.ejb.*;
                
                @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
                public class TransactionalService {
                
                    public void requiresNewFromType() {}
                
                    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
                    public void notSupported() {}
                                
                    @TransactionAttribute(TransactionAttributeType.MANDATORY)
                    public void mandatory() {}
                }
                """;

        @Language("java")
        String expected = """
                import javax.ejb.*;
                
                @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
                public class TransactionalService {
                
                    public void requiresNewFromType() {}
                
                   \s
                    public void notSupported() {}
                
                   \s
                    public void mandatory() {}
                }
                """;

        J.CompilationUnit cu = OpenRewriteTestSupport.createCompilationUnitsFromStrings(List.of("javax.ejb:javax.ejb-api:3.2"), given).get(0);

        List<J.MethodDeclaration> methodDeclarationList = cu.getClasses().get(0).getBody().getStatements().stream()
                .filter(J.MethodDeclaration.class::isInstance)
                .map(J.MethodDeclaration.class::cast)
                .toList();

        J.CompilationUnit result = cu;
        for (J.MethodDeclaration md : methodDeclarationList) {
            RemoveAnnotationVisitor sut1 = new RemoveAnnotationVisitor(md, "javax.ejb.TransactionAttribute");
            RecipeRun run = new GenericOpenRewriteRecipe<>(() -> sut1).run(List.of(result));
            if (!run.getResults().isEmpty()) {
                result = (J.CompilationUnit) run.getResults().get(0).getAfter();
            }
        }

        assertThat(result.print()).isEqualToNormalizingNewlines(expected);
    }

    @Test
    void removeAnnotationOnMemberLevel() {
        @Language("java")
        String given = """
                import javax.ejb.EJB;
                
                @EJB
                public class TransactionalService {
                
                    @EJB
                    private Object that;
                
                    private Object other;
                
                    @EJB
                    public void setEJB(Object some) {}
                }
                """;

        @Language("java")
        String expected = """
                import javax.ejb.EJB;
                
                @EJB
                public class TransactionalService {
                
                   \s
                    private Object that;
                
                    private Object other;
                
                    @EJB
                    public void setEJB(Object some) {}
                }
                """;

        J.CompilationUnit cu = OpenRewriteTestSupport.createCompilationUnitsFromStrings(List.of("javax.ejb:javax.ejb-api:3.2"), given).get(0);

        List<J.VariableDeclarations> variableDeclarations = cu.getClasses().get(0).getBody().getStatements().stream()
                .filter(J.VariableDeclarations.class::isInstance)
                .map(J.VariableDeclarations.class::cast)
                .toList();

        J.CompilationUnit result = cu;
        for (J.VariableDeclarations vd : variableDeclarations) {
            RemoveAnnotationVisitor sut1 = new RemoveAnnotationVisitor(vd, "javax.ejb.EJB");
            RecipeRun run = new GenericOpenRewriteRecipe<>(() -> sut1).run(List.of(result));
            if (!run.getResults().isEmpty()) {
                result = (J.CompilationUnit) run.getResults().get(0).getAfter();
            }
        }

        assertThat(result.print()).isEqualToNormalizingNewlines(expected);
    }
}