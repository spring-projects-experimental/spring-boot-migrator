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

import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.RecipeRun;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.RemoveUnusedImports;
import org.openrewrite.java.tree.J;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class RemoveUnusedImportsTest {
    @Test
    void removeUnusedImports() {
        String javaCode =
                "import org.springframework.transaction.annotation.Propagation;\n" +
                        "import org.springframework.transaction.annotation.Transactional;\n" +
                        "\n" +
                        "import javax.ejb.TransactionAttributeType;\n" +
                        "\n" +
                        "\n" +
                        "@Transactional(propagation = Propagation.REQUIRES_NEW)\n" +
                        "public class TransactionalService {\n" +
                        "   public void requiresNewFromType() {}\n" +
                        "\n" +
                        "    @Transactional(propagation = Propagation.NOT_SUPPORTED)\n" +
                        "    public void notSupported() {}\n" +
                        "}";

        String expected =
                "import org.springframework.transaction.annotation.Propagation;\n" +
                        "import org.springframework.transaction.annotation.Transactional;\n" +
                        "\n" +
                        "\n" +
                        "@Transactional(propagation = Propagation.REQUIRES_NEW)\n" +
                        "public class TransactionalService {\n" +
                        "   public void requiresNewFromType() {}\n" +
                        "\n" +
                        "    @Transactional(propagation = Propagation.NOT_SUPPORTED)\n" +
                        "    public void notSupported() {}\n" +
                        "}";

        List<SourceFile> compilationUnits = OpenRewriteTestSupport.createCompilationUnitsAsSourceFileFromStrings(List.of("javax.ejb:javax.ejb-api:3.2", "org.springframework.boot:spring-boot-starter-data-jpa:2.4.2"), javaCode);

        RemoveUnusedImports sut = new RemoveUnusedImports();
        RecipeRun run = sut.run(new InMemoryLargeSourceSet(compilationUnits), new InMemoryExecutionContext(t -> fail(t)));

        assertThat(run.getChangeset().getAllResults().get(0).getAfter().printAll()).isEqualTo(expected);

    }
}
