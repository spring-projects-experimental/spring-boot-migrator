package org.openrewrite.java.spring.boot3;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.test.RewriteTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaTestHelper {

    public void runAndVerify(
            String recipeName,
            List<String> dependsOn,
            @Language("java") String before
    ) {
        List<Result> result = runRecipe(recipeName, dependsOn, before);
        assertThat(result).hasSize(0);
    }

    public void runAndVerify(
            String recipeName,
            List<String> dependsOn,
            @Language("java") String before,
            @Language("java") String after
            ) {
        List<Result> result = runRecipe(recipeName, dependsOn, before);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(after);
    }

    @NotNull
    private List<Result> runRecipe(String recipeName, List<String> dependsOn, @Language("java") String before) {

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

        List<Result> result = RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(cu, ctx);

        assertThat(errors).hasSize(0);
        return result;
    }
}
