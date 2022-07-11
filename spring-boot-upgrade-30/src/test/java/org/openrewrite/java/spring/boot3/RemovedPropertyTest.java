package org.openrewrite.java.spring.boot3;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openrewrite.Result;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RemovedPropertyTest extends ConfigRecipeTest {

    @Override
    String getRecipeName() {
        return "org.openrewrite.java.spring.boot3.SpringBootPropertiesManual_2_7_Removed";
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/props-to-delete/spring-3_0-config-remove-yaml-management.txt",
            "/props-to-delete/spring-3_0-config-remove-yaml-activemq.txt",
            "/props-to-delete/spring-3_0-config-remove-yaml-artemis.txt"
    })
    public void removeYaml(String inputFilePath) throws IOException {
        Pair<String, String> testData = provideIO(inputFilePath);
        List<Result> result = runRecipeOnYaml(testData.getLeft());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/props-to-delete/spring-3_0-config-remove-properties-management.txt",
            "/props-to-delete/spring-3_0-config-remove-properties-activemq.txt",
            "/props-to-delete/spring-3_0-config-remove-properties-artemis.txt"
    })
    public void removeProperties(String inputFilePath) throws IOException {
        Pair<String, String> testData = provideIO(inputFilePath);
        List<Result> result = runRecipeOnProperties(testData.getLeft());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
    }
}
