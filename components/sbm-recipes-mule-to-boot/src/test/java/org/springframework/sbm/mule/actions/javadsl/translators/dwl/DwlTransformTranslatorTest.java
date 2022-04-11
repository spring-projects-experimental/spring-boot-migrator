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
package org.springframework.sbm.mule.actions.javadsl.translators.dwl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class DwlTransformTranslatorTest {

    @ParameterizedTest
    @CsvSource(value = {
            "MapClientRiskRatingResponseTransform,classpath:dwl/mapClientRiskRatingResponse.dwl",
            "MapclientriskratingresponseTransform,classpath:dwl/map-client-risk-rating-response.dwl",
            "MapclientriskratingresponseTransform,classpath:map client risk rating response.dwl",
            "MapclientriskratingresponseTransform,classpath:map client risk rating response"
    },
            delimiter = ',')
    void classNameSanitizer(String expected, String input) {
        assertEquals(
                expected,
                DwlTransformTranslator.sanitizeForClassName(input)
        );
    }
}
