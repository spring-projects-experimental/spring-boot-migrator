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

class R2DBC10ReportSectionTest {
    @Test
    @DisplayName("R2DBC 1.0 should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("org.springframework.data:spring-data-r2dbc:1.5.6")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("R2DBC 1.0")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === R2DBC 1.0
                                          
                                          ==== What Changed
                                          Spring Boot 3.0 uses R2DBC 1.0 by default.
                                          With the 1.0 release, R2DBC no longer publishes a bill of materials (bom) which has affected Spring Boot's dependency management.
                                          The `r2dbc-bom.version` can no longer be used to override R2DBC's version.
                                          In its place, several new properties for the individual and separately versioned modules are now available:
                                          
                                          * `oracle-r2dbc.version` (`com.oracle.database.r2dbc:oracle-r2dbc`)
                                          * `r2dbc-h2.version` (`io.r2dc:r2dbc-h2`)
                                          * `r2dbc-pool.version` (`io.r2dc:r2dbc-pool`)
                                          * `r2dbc-postgres.version` (`io.r2dc:r2dbc-postgres`)
                                          * `r2dbc-proxy.version` (`io.r2dc:r2dbc-proxy`)
                                          * `r2dbc-spi.version` (`io.r2dc:r2dbc-spi`)
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `org\\.springframework\\.data\\:spring-data-r2dbc\\:.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#r2dbc-1.0[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/668[#668^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("R2DBC 1.0 should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("R2DBC 1.0")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
