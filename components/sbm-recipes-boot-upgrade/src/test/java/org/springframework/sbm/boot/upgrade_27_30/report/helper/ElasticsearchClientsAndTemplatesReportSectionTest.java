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

class ElasticsearchClientsAndTemplatesReportSectionTest {
    @Test
    @DisplayName("Elasticsearch Clients and Templates should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("org.springframework.data:spring-data-elasticsearch:4.4.6")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Elasticsearch Clients and Templates")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === Elasticsearch Clients and Templates
                                          
                                          ==== What Changed
                                          Support for Elasticsearch's high-level REST client has been removed.
                                          In its place, auto-configuration for Elasticsearch's new Java client has been introduced.
                                          Similarly, support for the Spring Data Elasticsearch templates that built on top of the high-level REST client has been removed.
                                          In its place, auto-configuration for the new templates that build upon the new Java client has been introduced.
                                          See https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.clients[the Elasticsearch section of the reference documentation] for further details.
                                          
                                          `ReactiveElasticsearchRestClientAutoConfiguration` has been renamed to `ReactiveElasticsearchClientAutoConfiguration` and has moved from `org.springframework.boot.autoconfigure.data.elasticsearch` to `org.springframework.boot.autoconfigure.elasticsearch`.
                                          Any auto-configuration exclusions or ordering should be updated accordingly.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `org\\.springframework\\.data\\:spring-data-elasticsearch\\:.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#elasticsearch-clients-and-templates[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/688[#688^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("Elasticsearch Clients and Templates should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Elasticsearch Clients and Templates")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
