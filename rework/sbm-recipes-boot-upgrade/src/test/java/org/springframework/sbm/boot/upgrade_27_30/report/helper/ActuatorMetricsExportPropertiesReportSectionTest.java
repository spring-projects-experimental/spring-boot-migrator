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
package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportTestSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

class ActuatorMetricsExportPropertiesReportSectionTest {
    @Test
    @DisplayName("Actuator Metrics Export Properties should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("io.micrometer:micrometer-core:1.10.2")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Actuator Metrics Export Properties")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === Actuator Metrics Export Properties
                                          
                                          ==== What Changed
                                          We have moved the properties controlling the https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator.metrics[actuator metrics export]. 
                                          The old schema was `management.metrics.export.<product>`, the new one is `management.<product>.metrics.export` (Example: the prometheus properties moved from `management.metrics.export.prometheus` to `management.prometheus.metrics.export`). 
                                          If you are using the `spring-boot-properties-migrator`, you will get notified at startup. 
                                          
                                          See https://github.com/spring-projects/spring-boot/issues/30381[issue #30381] for details.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `io\\.micrometer\\:micrometer-.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#actuator-metrics-export-properties[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/626[#626^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("Actuator Metrics Export Properties should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Actuator Metrics Export Properties")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
