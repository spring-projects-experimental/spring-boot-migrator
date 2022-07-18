package org.openrewrite.java.spring.boot3;

import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.test.RewriteTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SomeTest {

    @Test
    public void te() {

        InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);
        JavaParser parser = JavaParser
                .fromJavaVersion()
                .build();

        List<J.CompilationUnit> cu = parser.parse("""
                package test;
                public class A {
                }
                """);


        String recipeName = "org.boot3.Crud";

        List<Result> result = RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(cu, ctx);


        assertThat(result).hasSize(1);
    }
}
