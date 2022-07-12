package org.openrewrite.java.spring.boot3;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.properties.tree.Properties;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.yaml.YamlParser;
import org.openrewrite.yaml.tree.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePropertyTest {

    private final InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

    @ParameterizedTest
    @MethodSource("provideYamlIO")
    void runYamlTests(String input, String expected) {

        List<Result> result = runRecipeOnYml(input);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("providePropIO")
    void runPropertiesTest(String input, String expected) {

        List<Result> result = runRecipeOnProperties(input);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(expected);
    }

    private List<Result> runRecipeOnProperties(@Language("properties") String source) {
        List<Properties.File> document = new PropertiesParser().parse(source);
        String recipeName = "org.openrewrite.java.spring.boot3.SpringBootPropertiesManual_2_7";

        return RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(document, ctx);
    }


    private List<Result> runRecipeOnYml(@Language("yml") String source) {
        List<Yaml.Documents> document = new YamlParser().parse(source);
        String recipeName = "org.openrewrite.java.spring.boot3.SpringBootPropertiesManual_2_7";

        return RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(document, ctx);
    }

    private static Stream<Arguments> provideYamlIO() throws IOException {
        InputStream data =
                UpdatePropertyTest.class.getResourceAsStream("/spring-3_0-config-refactor-yaml.txt");

        if (data == null) {
            throw new RuntimeException("unable to read /spring-3_0-config-refactor-yaml.txt file");
        }

        String fileContent = new String(data.readAllBytes());
        String[] tests = fileContent.split("--- end of test ---");

        return Arrays.stream(tests)
                .map(test -> {
                    String[] k = test.split("expected:.*\n");
                    return Arguments.of(k[0].replaceAll("input:.*\n", ""), k[1]);
                });
    }

    private static Stream<Arguments> providePropIO() throws IOException {
        String inputFile = "/spring-3_0-config-refactor-properties.txt";
        InputStream data =
                UpdatePropertyTest.class.getResourceAsStream(inputFile);

        if (data == null) {
            throw new RuntimeException(inputFile);
        }

        String fileContent = new String(data.readAllBytes());
        String[] tests = fileContent.split("--- end of test ---");

        return Arrays.stream(tests)
                .map(test -> {
                    String[] k = test.split("expected:.*\n");
                    return Arguments.of(k[0].replaceAll("input:.*\n", ""), k[1]);
                });
    }
}
