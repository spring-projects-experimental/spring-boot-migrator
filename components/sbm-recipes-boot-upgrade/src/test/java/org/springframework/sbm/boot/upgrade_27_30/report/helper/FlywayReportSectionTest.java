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

class FlywayReportSectionTest {
    @Test
    @DisplayName("Flyway should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("org.flywaydb:flyway-core:9.7.0")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Flyway")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === Flyway
                                          
                                          ==== What Changed
                                          Spring Boot 3.0 uses Flyway 9.0 by default. Please see the Flyway https://flywaydb.org/documentation/learnmore/releaseNotes#9.0.0[release notes] and https://flywaydb.org/blog/version-9-is-coming-what-developers-need-to-know[blog post] to learn how this may affect your application.
                                          
                                          `FlywayConfigurationCustomizer` beans are now called to customize the `FluentConfiguration` after any `Callback` and `JavaMigration` beans have been added to the configuration.
                                          An application that defines `Callback` and `JavaMigration` beans and adds callbacks and Java migrations using a customizer may have to be updated to ensure that the intended callbacks and Java migrations are used.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `org\\.flywaydb\\:flyway-core\\:.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#flyway[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/648[#648^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("Flyway should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Flyway")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
