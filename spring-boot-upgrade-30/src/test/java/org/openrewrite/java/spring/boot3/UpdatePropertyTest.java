package org.openrewrite.java.spring.boot3;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openrewrite.Result;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePropertyTest {
    private static ConfigRecipeTestHelper configRecipeTestHelper = ConfigRecipeTestHelper
            .builder()
            .recipeName("org.openrewrite.java.spring.boot3.SpringBootPropertiesManual_2_7")
            .build();

    @ParameterizedTest
    @MethodSource("provideYamlInputFiles")
    void runYamlTestsData(String inputFilePath) throws IOException {

        Pair<String, String> testData = configRecipeTestHelper.provideIO(inputFilePath);
        List<Result> result = configRecipeTestHelper.runRecipeOnYaml(testData.getLeft());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
    }

    @ParameterizedTest
    @MethodSource("providePropertiesInputFiles")
    void runPropertiesTestsDataSource(String inputFilePath) throws IOException {

        Pair<String, String> testData = configRecipeTestHelper.provideIO(inputFilePath);
        List<Result> result = configRecipeTestHelper.runRecipeOnProperties(testData.getLeft());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
    }

    private static Stream<Arguments> providePropertiesInputFiles() throws URISyntaxException {

        return ConfigRecipeTestHelper.provideFiles("/props-to-update","properties");
    }

    private static Stream<Arguments> provideYamlInputFiles() throws URISyntaxException  {

        return ConfigRecipeTestHelper.provideFiles("/props-to-update", "yaml");
    }
}
