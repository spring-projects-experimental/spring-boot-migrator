/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.boot.upgrade_27_30.config;

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

    @ParameterizedTest
    @MethodSource("provideYamlInputFiles")
    void runYamlTestsData(String inputFilePath) throws IOException {

        Pair<String, String> testData = ConfigRecipeTestHelper.provideIO(inputFilePath);
        List<Result> result = ConfigRecipeTestHelper.runRecipeOnYaml(testData.getLeft(),
                "org.openrewrite.java.spring.boot3.SpringBootPropertiesManual_2_7");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(testData.getRight());
    }

    @ParameterizedTest
    @MethodSource("providePropertiesInputFiles")
    void runPropertiesTestsDataSource(String inputFilePath) throws IOException {

        Pair<String, String> testData = ConfigRecipeTestHelper.provideIO(inputFilePath);
        List<Result> result = ConfigRecipeTestHelper.runRecipeOnProperties(testData.getLeft(),
                "org.openrewrite.java.spring.boot3.SpringBootPropertiesManual_2_7");

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
