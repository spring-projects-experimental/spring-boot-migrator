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
import org.junit.jupiter.api.DisplayName;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportTestSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

class YamlJsonParserHasBeenRemovedReportSectionTest {
    @Test
    @DisplayName("YamlJsonParser Has Been Removed should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter:3.0.0")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("YamlJsonParser Has Been Removed")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === YamlJsonParser Has Been Removed
                                          
                                          ==== What Changed
                                          `YamlJsonParser` has been removed as SnakeYAML's JSON parsing was inconsistent with the other parser implementations.
                                          In the unlikely event that you were using `YamlJsonParser` directly, please migrate to one of the other `JsonParser` implementations.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `org\\.springframework\\.boot\\:spring-boot-starter\\:.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#yamljsonparser-has-been-removed[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/644[#644^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("YamlJsonParser Has Been Removed should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("YamlJsonParser Has Been Removed")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
