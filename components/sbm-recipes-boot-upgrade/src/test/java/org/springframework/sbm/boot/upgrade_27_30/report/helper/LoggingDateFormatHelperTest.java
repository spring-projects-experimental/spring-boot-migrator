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
package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.properties.api.PropertiesSource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class LoggingDateFormatHelperTest {

    @Test
    void isApplicableWithExistingPropertiesFile() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withSpringBootParentOf("2.7.5")
                .addRegistrar(
                        new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .withProjectResource("src/main/resources/application-myprofile.properties", "not.logging.pattern.dateformat=value")
                .build();

        LoggingDateFormatHelper sut = new LoggingDateFormatHelper();

        boolean evaluate = sut.evaluate(context);
        assertThat(evaluate).isTrue();
        Map<String, List<? extends PropertiesSource>> data = sut.getData();
        assertThat(data).hasSize(1);
        assertThat(data.get("properties")).isEmpty();
    }

    @Test
    void isApplicableWithoutExistingPropertiesFile() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withSpringBootParentOf("2.7.5")
                .build();

        LoggingDateFormatHelper sut = new LoggingDateFormatHelper();

        boolean evaluate = sut.evaluate(context);
        assertThat(evaluate).isTrue();
        Map<String, List<? extends PropertiesSource>> data = sut.getData();
        assertThat(data).hasSize(1);
        assertThat(data.get("properties")).isEmpty();
    }

    @Test
    void isNotApplicableWithExistingPropertiesFileContainingRelevantProperty() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .withProjectResource("src/main/resources/application-myprofile.properties", "logging.pattern.dateformat=some-format")
                .build();

        LoggingDateFormatHelper sut = new LoggingDateFormatHelper();

        boolean evaluate = sut.evaluate(context);
        assertThat(evaluate).isFalse();
    }

}
