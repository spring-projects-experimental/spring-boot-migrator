package org.openrewrite.java.spring.boot3;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.yaml.YamlParser;
import org.openrewrite.yaml.tree.Yaml;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpatePropertyYAMLTest {

    private final InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

    @Test
    void movePropertyTestSingle() {
        List<Result> result = runRecipe("""
                    spring:
                      data:
                        elasticsearch:
                          client:
                            reactive:
                              connection-timeout: 1000
                """.stripIndent());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo("""
                    spring:
                      elasticsearch.connection-timeout: 1000
                """.stripIndent());
    }

    private List<Result> runRecipe(@Language("yml") String source) {
        List<Yaml.Documents> document = new YamlParser().parse(source);
        String recipeName = "org.openrewrite.yaml.ChangePropertyKey";

        return RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(document, ctx);
    }
}
