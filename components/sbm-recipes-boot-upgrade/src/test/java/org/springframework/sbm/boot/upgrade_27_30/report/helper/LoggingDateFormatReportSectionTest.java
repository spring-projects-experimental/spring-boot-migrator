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
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportTestSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

/**
 * @author Fabian Krüger
 */
public class LoggingDateFormatReportSectionTest {

    @Test
    void shouldRenderSectionWhenNoPropertiesExist() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withSpringBootParentOf("2.7.5")
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .addProjectResource("src/main/resources/application-myprofile.properties", "not.logging.pattern.dateformat=some-format")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Logging Date Format")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                        === Logging Date Format
                        
                        ==== What Changed
                        The default format for the date and time component of log messages for Logback and Log4j2 has changed to
                        align with the ISO-8601 standard. The new default format `yyyy-MM-dd’T’HH:mm:ss.SSSXXX` uses a `T` to
                        separate the date and time instead of a space character and adds the timezone offset to the end.
                        The `LOG_DATEFORMAT_PATTERN` environment variable or `logging.pattern.dateformat` property can be used to
                        restore the previous default value of `yyyy-MM-dd HH:mm:ss.SSS`.
                        
                        ==== Why is the application affected
                        The scan found no property `logging.pattern.dateformat`.
                        
                        ==== Remediation
                        Set `logging.pattern.dateformat=yyyy-MM-dd HH:mm:ss.SSS` to fall back to the previous log format.
                        
                        
                        """
                );
    }

    @Test
    void shouldRenderSectionWhenPropertyNotDefined() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withSpringBootParentOf("2.7.5")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Logging Date Format")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                        === Logging Date Format
                        
                        ==== What Changed
                        The default format for the date and time component of log messages for Logback and Log4j2 has changed to
                        align with the ISO-8601 standard. The new default format `yyyy-MM-dd’T’HH:mm:ss.SSSXXX` uses a `T` to
                        separate the date and time instead of a space character and adds the timezone offset to the end.
                        The `LOG_DATEFORMAT_PATTERN` environment variable or `logging.pattern.dateformat` property can be used to
                        restore the previous default value of `yyyy-MM-dd HH:mm:ss.SSS`.
                        
                        ==== Why is the application affected
                        The scan found no property `logging.pattern.dateformat`.
                        
                        ==== Remediation
                        Set `logging.pattern.dateformat=yyyy-MM-dd HH:mm:ss.SSS` to fall back to the previous log format.
                        
                        
                        """
                );
    }

    @Test
    void shouldNotRenderSectionWhenPropertyIsDefined() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .addProjectResource("src/main/resources/application-myprofile.properties", "logging.pattern.dateformat=some-format")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Logging Date Format")
                .fromProjectContext(context)
                .shouldNotRender();
    }

}
