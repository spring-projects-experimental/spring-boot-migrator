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
package org.springframework.sbm.boot.upgrade_27_30.openrewrite;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.tree.J;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.rewrite.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityManagerUsagesFinderTest {

    @Test
    void noResult() {
        @Language("java")
        String class1 =
                """
                public class Foo {}
                """;

        ProjectContext context = TestProjectContext.buildProjectContext().withJavaSource("src/main/java", class1).build();

        List<RewriteSourceFileHolder<J.CompilationUnit>> matches = context.getProjectJavaSources().find(new SecurityManagerUsagesFinder());

        assertThat(matches).isEmpty();
    }

    @Test
    @DisplayName("Finds imports of java.security.AccessControlException")
    void usageOfAccessControlException() {
        @Language("java")
        String class1 =
                """
                import java.security.AccessControlException;
                public class Foo {
                    void some() {
                        throw new AccessControlException();
                    }
                }
                """;

        ProjectContext context = TestProjectContext.buildProjectContext().withJavaSource("src/main/java", class1).build();

        List<RewriteSourceFileHolder<J.CompilationUnit>> matches = context.getProjectJavaSources().find(new SecurityManagerUsagesFinder());

        assertThat(matches).hasSize(1);
    }

    @Test
    @DisplayName("Finds usages of System.getSecurityManager()")
    void usageOfSystemGetSecurityManager() {
        @Language("java")
        String class1 =
                """
                public class Foo {
                    void some() {
                        System.getSecurityManager();
                    }
                }
                """;

        ProjectContext context = TestProjectContext.buildProjectContext().withJavaSource("src/main/java", class1).build();

        List<RewriteSourceFileHolder<J.CompilationUnit>> matches = context.getProjectJavaSources().find(new SecurityManagerUsagesFinder());

        assertThat(matches).hasSize(1);
    }

}