/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.boot.upgrade.comon.conditions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.sbm.boot.upgrade.common.conditions.BootHasAutoconfigurationCondition;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class BootHasAutoconfigurationConditionTest {

    private static final String content = "org.springframework.boot.autoconfigure.EnableAutoConfiguration=XYZ";

    @ParameterizedTest
    @CsvSource(value = {
            "src/main/resources/META-INF/spring.factories," + content + ",true",
            "src/main/resources/META-INF/META-INF/spring.factories," + content + ",false",
            "src/main/resources/META-INF/spring.factories,Hello World,false",
            "src/main/resources/META-INF/spring.factories,Hello World org.springframework.boot.autoconfigure.EnableAutoConfiguration,false",
    }, delimiter = ',')
    void conditionTests(String filePath, String fileContent, boolean expectation) {

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withProjectResource(
                        filePath,
                        fileContent
                )
                .build();

        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
        assertThat(condition.evaluate(context)).isEqualTo(expectation);
    }

    @Test
    void itCanDoMultiLine() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        """
                                org.springframework.boot.autoconfigure.EnableAutoConfiguration=XTZ\
                                abc
                                """
                )
                .build();

        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
        assertThat(condition.evaluate(context)).isTrue();
    }
}
