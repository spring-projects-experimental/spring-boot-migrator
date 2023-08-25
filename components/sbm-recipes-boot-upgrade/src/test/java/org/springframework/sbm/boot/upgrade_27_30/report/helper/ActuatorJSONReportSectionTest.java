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

class ActuatorJSONReportSectionTest {
    @Test
    @DisplayName("Actuator JSON should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-actuator:3.0.0")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Actuator JSON")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === Actuator JSON
                                          
                                          ==== What Changed
                                          Responses from the actuator endpoints shipped with Spring Boot now use an isolated `ObjectMapper` instance to ensure results are consistent.
                                          If you want to revert to the old behavior and use the application `ObjectMapper` you can set `management.endpoints.jackson.isolated-object-mapper` to `false`.
                                          
                                          If you have developed your own endpoints, you might want to ensure that responses implement the `OperationResponseBody` interface.
                                          This will ensure that the isolated `ObjectMapper` is considered when serializing the response as JSON.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `org\\.springframework\\.boot\\:spring-boot-actuator\\:.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#actuator-json[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/642[#642^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("Actuator JSON should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Actuator JSON")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
