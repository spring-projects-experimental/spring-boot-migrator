package org.springframework.sbm.boot.upgrade_27_30;

import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.yaml.YamlParser;
import org.openrewrite.yaml.tree.Yaml;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyTest {
    @Test
    public void temp() {
        String source = """
                some:
                  other: "prop"
                spring:
                  activemq:
                    broker-url: http://google.com
                    close-timeout: 13
                    in-memory: true
                    non-blocking-redelivery: true
                    password: password
                    send-timeout: 11
                    user: user
                    packages:
                      trust-all: true
                      trusted: true
                    pool:
                      block-if-full: true
                      block-if-full-timeout: true
                      enabled: true
                      idle-timeout: 12
                      max-connections: 200
                      max-sessions-per-connection: 300
                      time-between-expiration-check: 22
                      use-anonymous-producers: true
                """;

        InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);
        List<Yaml.Documents> document = new YamlParser().parse(source);

        List<Result> result = RewriteTest
                .fromRuntimeClasspath("org.openrewrite.java.spring.boot3.SpringBootPropertiesManual_2_7_Removed")
                .run(document, ctx);

        assertThat(result).hasSize(1);
    }
}
