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

class AutoconfigurationFilesReportSectionTest {
    @Test
    @DisplayName("Auto-configuration Files should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter:2.7.5")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Auto-configuration Files")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === Auto-configuration Files
                                          
                                          ==== What Changed
                                          Spring Boot 2.7 https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.7-Release-Notes#changes-to-auto-configuration[introduced] a new `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` file for registering auto-configurations, while maintaining backwards compatibility with registration in `spring.factories`.
                                          With this release, support for registering auto-configurations in `spring.factories` has been removed in favor of the imports file.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `org\\.springframework\\.boot\\:spring-boot-starter\\:.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#auto-configuration-files[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/654[#654^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("Auto-configuration Files should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Auto-configuration Files")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
