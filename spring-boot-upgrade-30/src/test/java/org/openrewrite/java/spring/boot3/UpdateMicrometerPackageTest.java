package org.openrewrite.java.spring.boot3;

import org.junit.jupiter.api.Test;
import org.openrewrite.Result;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.test.RewriteTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateMicrometerPackageTest {

    @Test
    void shouldUpdatePackage() {
        JavaParser javaParser = JavaParser.fromJavaVersion().build();
        String javaDependsOn = """
                package io.micrometer.core.instrument.binder;
                public class Abc {
                }""".stripIndent();
        String javaCode = """
                package a;
                import io.micrometer.core.instrument.binder.*;
                class A {
                    Abc method() {
                        return null;
                    }
                }""".stripIndent();
        List<J.CompilationUnit> parse = javaParser.parse(javaDependsOn, javaCode);
        String recipeName = "org.openrewrite.java.AddMavenRepository";
        List<Result> results = RewriteTest.fromRuntimeClasspath(recipeName)
                .run(parse);
        assertThat(results).hasSize(1);
    }
}
