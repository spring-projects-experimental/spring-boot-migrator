package org.openrewrite.java.spring.boot3;

import org.apache.commons.lang3.tuple.Pair;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.properties.tree.Properties;
import org.openrewrite.test.RewriteTest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePropertyTest extends ConfigRecipeTest {

    private final InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

    @ParameterizedTest
    @ValueSource(strings = {
            "/props-to-update/spring-3_0-config-refactor-yaml-data.txt",
            "/props-to-update/spring-3_0-config-refactor-yaml-datasource.txt",
            "/props-to-update/spring-3_0-config-refactor-yaml-elasticsearch.txt",
            "/props-to-update/spring-3_0-config-refactor-yaml-misc.txt"})
    void runYamlTestsData(String inputFilePath) throws IOException {

        Pair<String, String> testData = provideIO(inputFilePath);
        List<Result> result = runRecipeOnYaml(testData.getLeft());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/props-to-update/spring-3_0-config-refactor-properties-data.txt",
            "/props-to-update/spring-3_0-config-refactor-properties-datasource.txt",
            "/props-to-update/spring-3_0-config-refactor-properties-elasticsearch.txt",
            "/props-to-update/spring-3_0-config-refactor-properties-misc.txt"})
    void runPropertiesTestsDataSource(String inputFilePath) throws IOException {

        Pair<String, String> testData = provideIO(inputFilePath);
        List<Result> result = runRecipeOnProperties(testData.getLeft());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
    }

    @Override
    String getRecipeName() {
        return "org.openrewrite.java.spring.boot3.SpringBootPropertiesManual_2_7";
    }
}
