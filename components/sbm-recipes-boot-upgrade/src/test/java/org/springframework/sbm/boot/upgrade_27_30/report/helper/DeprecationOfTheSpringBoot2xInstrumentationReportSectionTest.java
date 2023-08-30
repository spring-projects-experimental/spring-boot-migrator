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

class DeprecationOfTheSpringBoot2xInstrumentationReportSectionTest {
    @Test
    @DisplayName("Deprecation of the Spring Boot 2.x instrumentation should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("io.micrometer:micrometer-core:1.10.2")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Deprecation of the Spring Boot 2.x instrumentation")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === Deprecation of the Spring Boot 2.x instrumentation
                                          
                                          ==== What Changed
                                          As a result of the integration with the Observation support, we are now deprecating the previous instrumentation.
                                          The filters, interceptors performing the actual instrumentation have been removed entirely, as entire classes of bugs could not be resolved and the risk of duplicate instrumentation was too high. For example, the `WebMvcMetricsFilter` has been deleted entirely and is effectively replaced by Spring Framework's `ServerHttpObservationFilter`.
                                          The corresponding `*TagProvider` `*TagContributor` and `*Tags` classes have been deprecated.
                                          They are not used by default anymore by the observation instrumentation.
                                          We are keeping them around during the deprecation phase so that developers can migrate their existing infrastructure to the new one.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `io\\.micrometer\\:micrometer-.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#deprecation-of-the-spring-boot-2.x-instrumentation[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/676[#676^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("Deprecation of the Spring Boot 2.x instrumentation should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Deprecation of the Spring Boot 2.x instrumentation")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
