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

class HttptraceEndpointRenamedToHttpexchangesReportSectionTest {
    @Test
    @DisplayName("'httptrace' Endpoint Renamed to 'httpexchanges' should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-actuator:3.0.0")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("'httptrace' Endpoint Renamed to 'httpexchanges'")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === 'httptrace' Endpoint Renamed to 'httpexchanges'
                                          
                                          ==== What Changed
                                          The `httptrace` endpoint and related infrastructure records and provides access to information about recent HTTP request-response exchanges.
                                          Following the introduction of support for https://micrometer.io/docs/tracing[Micrometer Tracing], the name `httptrace` may cause confusion.
                                          To reduce this possible confusion the endpoint has been renamed to `httpexchanges`.
                                          The contents of the endpoint's response has also been affected by this renaming.
                                          Please refer to the {actuator-api}/#httpexchanges[Actuator API documentation] for further details.
                                          
                                          Related infrastructure classes have also been renamed.
                                          For example, `HttpTraceRepository` is now named `HttpExchangeRepository` and can be found in the `org.springframework.boot.actuate.web.exchanges` package.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `org\\.springframework\\.boot\\:spring-boot-actuator\\:.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#-httptrace--endpoint-renamed-to--httpexchanges-[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/632[#632^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("'httptrace' Endpoint Renamed to 'httpexchanges' should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("'httptrace' Endpoint Renamed to 'httpexchanges'")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
