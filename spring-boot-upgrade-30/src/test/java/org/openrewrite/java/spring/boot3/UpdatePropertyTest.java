package org.openrewrite.java.spring.boot3;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.assertj.core.api.Assertions.in;

public class UpdatePropertyTest {

    private final InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

    @ParameterizedTest
    @ValueSource(strings = {
            "/spring-3_0-config-refactor-yaml-data.txt",
            "/spring-3_0-config-refactor-yaml-datasource.txt",
            "/spring-3_0-config-refactor-yaml-elasticsearch.txt",
            "/spring-3_0-config-refactor-yaml-misc.txt"})
    void runYamlTestsData(String inputFilePath) throws IOException {

        Pair<String, String> testData = provideIO(inputFilePath);
        List<Result> result = runRecipeOnYml(testData.getLeft());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/spring-3_0-config-refactor-properties-data.txt",
            "/spring-3_0-config-refactor-properties-datasource.txt",
            "/spring-3_0-config-refactor-properties-elasticsearch.txt",
            "/spring-3_0-config-refactor-properties-misc.txt"})
    void runYamlTestsDataSource(String inputFilePath) throws IOException {

        Pair<String, String> testData = provideIO(inputFilePath);
        List<Result> result = runRecipeOnProperties(testData.getLeft());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
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

    private static Pair<String, String> provideIO(String inputFilePath) throws IOException {
        InputStream data =
                UpdatePropertyTest.class.getResourceAsStream(inputFilePath);

        if (data == null) {
            throw new RuntimeException("unable to read: "+ inputFilePath);
        }

        String fileContent = new String(data.readAllBytes());
        String[] k = fileContent.split("expected:.*\n");

        return new ImmutablePair<>(k[0].replaceAll("input:.*\n", ""), k[1]);
    }
}
