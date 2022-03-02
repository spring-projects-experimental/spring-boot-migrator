package org.springframework.sbm.java.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.sbm.java.util.Helper.getClassFromFullyQualifiedReference;

class HelperTest {
    @ParameterizedTest
    @CsvSource(value = {
            "org.springframework.integration.dsl.IntegrationFlow, IntegrationFlow",
            "null,''",
            "IntegrationFlow,IntegrationFlow"
    }, nullValues = "null")
    void shouldValidateGettingClassFromFullyQualifiedReference(String input, String output) {
        assertThat(getClassFromFullyQualifiedReference(input)).isEqualTo(output);
    }
}
