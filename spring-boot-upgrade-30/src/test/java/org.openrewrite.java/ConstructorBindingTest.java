package org.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.java.tree.J;
import org.openrewrite.test.RewriteTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Brain dump
 * TODO:
 *  * Source can be 11 and destination must be 17 separate test for that ?
 * */
public class ConstructorBindingTest {

    @Test
    void newGeneratedClassRemovesConstructorBinding() {

        InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

        String source = "" +
                "package com.example.config.demo;\n" +
                "\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.boot.context.properties.ConfigurationProperties;\n" +
                "import org.springframework.boot.context.properties.ConstructorBinding;\n" +

                "@ConfigurationProperties(prefix = \"mail\")\n" +
                "@ConstructorBinding\n" +
                "public class ConfigProperties {\n" +
                "    private String hostName;\n" +
                "    private int port;\n" +
                "    private String from;\n" +
                "\n" +
                "    public ConfigProperties(String hostName, int port, String from) {\n" +
                "        this.hostName = hostName;\n" +
                "        this.port = port;\n" +
                "        this.from = from;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public String getHostName() {\n" +
                "        return hostName;\n" +
                "    }\n" +
                "\n" +
                "    public int getPort() {\n" +
                "        return port;\n" +
                "    }\n" +
                "\n" +
                "    public String getFrom() {\n" +
                "        return from;\n" +
                "    }\n" +
                "}";

        List<J.CompilationUnit> parse = Java17Parser.builder()
                .classpath("spring-boot")
                .build().parse(ctx, source);

        String recipeName = "org.openrewrite.java.spring.boot3.data.java.constructorbinding";

        List<Result> result = RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(parse, ctx);

        assertThat(result).hasSize(1);
    }
}
