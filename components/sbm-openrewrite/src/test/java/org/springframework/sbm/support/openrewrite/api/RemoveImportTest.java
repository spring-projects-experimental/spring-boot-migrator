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
package org.springframework.sbm.support.openrewrite.api;

import org.intellij.lang.annotations.Language;
import org.openrewrite.RecipeRun;
import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;
import org.springframework.sbm.support.openrewrite.java.RemoveAnnotationVisitor;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.tree.J;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RemoveImportTest {

    @Test
        // Shows that the import to TransactionAttributeType is not removed when @TransactionAttribute is removed
    void failing() {
        @Language("java")
        String source = """
                import org.springframework.transaction.annotation.Propagation;
                import org.springframework.transaction.annotation.Transactional;
                
                import javax.ejb.TransactionAttributeType;
                import javax.ejb.TransactionAttribute;
                
                @TransactionAttribute(TransactionAttributeType.NEVER)
                public class TransactionalService {
                    public void requiresNewFromType() {}
                
                
                    @Transactional(propagation = Propagation.NOT_SUPPORTED)
                    public void notSupported() {}
                }""";

        final J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(source, "javax.ejb:javax.ejb-api:3.2", "org.springframework.boot:spring-boot-starter-data-jpa:2.4.2");

        RecipeRun results = new GenericOpenRewriteRecipe<>(() -> new RemoveAnnotationVisitor(compilationUnit.getClasses().get(0), "javax.ejb.TransactionAttribute")).run(List.of(compilationUnit));
        J.CompilationUnit compilationUnit1 = (J.CompilationUnit) results.getResults().get(0).getAfter();

        @Language("java")
        String expected = """
                import org.springframework.transaction.annotation.Propagation;
                import org.springframework.transaction.annotation.Transactional;
                
                
                public class TransactionalService {
                    public void requiresNewFromType() {}
                
                              
                    @Transactional(propagation = Propagation.NOT_SUPPORTED)
                    public void notSupported() {}
                }""";

        assertThat(compilationUnit1.printAll()).isEqualToNormalizingNewlines(expected);
    }
}
