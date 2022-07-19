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
package org.springframework.sbm.boot.upgrade_27_30;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.test.RewriteTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaTestHelper {

    public void runAndVerifyNoChanges(
            Recipe recipe,
            List<String> dependsOn,
            @Language("java") String before
    ) {
        List<Result> result = runRecipe(recipe, dependsOn, before);
        assertThat(result).hasSize(0);
    }

    public void runAndVerify(
            Recipe recipe,
            List<String> dependsOn,
            @Language("java") String before,
            @Language("java") String after
            ) {
        List<Result> result = runRecipe(recipe, dependsOn, before);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(after);
    }

    @NotNull
    private List<Result> runRecipe(Recipe recipe, List<String> dependsOn, @Language("java") String before) {

        List<Throwable> errors = new ArrayList<>();
        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            ex.printStackTrace();
            errors.add(ex);
        });

        JavaParser parser = JavaParser
                .fromJavaVersion()
                .dependsOn(dependsOn.toArray(new String[0]))
                .build();

        List<J.CompilationUnit> cu = parser.parse(before);

        List<Result> result = recipe.run(cu, ctx);

        assertThat(errors).hasSize(0);
        return result;
    }
}
