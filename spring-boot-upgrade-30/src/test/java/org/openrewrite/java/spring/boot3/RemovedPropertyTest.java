package org.openrewrite.java.spring.boot3;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openrewrite.Result;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class RemovedPropertyTest extends ConfigRecipeTest {

    @Override
    String getRecipeName() {
        return "org.openrewrite.java.spring.boot3.SpringBootPropertiesManual_2_7_Removed";
    }

    @ParameterizedTest
    @MethodSource("provideYamlInputFiles")
    public void removeYaml(String inputFilePath) throws IOException {
        Pair<String, String> testData = provideIO(inputFilePath);
        List<Result> result = runRecipeOnYaml(testData.getLeft());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
    }

    @ParameterizedTest
    @MethodSource("providePropertiesInputFiles")
    public void removeProperties(String inputFilePath) throws IOException {
        Pair<String, String> testData = provideIO(inputFilePath);
        List<Result> result = runRecipeOnProperties(testData.getLeft());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
    }

    private static Stream<Arguments> providePropertiesInputFiles() throws URISyntaxException  {

        return provideFiles("/props-to-delete","properties");
    }

    private static Stream<Arguments> provideYamlInputFiles() throws URISyntaxException  {

        return provideFiles("/props-to-delete", "yaml");
    }
}
