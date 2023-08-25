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

class RunningYourApplicationInTheMavenProcessReportSectionTest {
    @Test
    @DisplayName("Running Your Application in the Maven Process should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter:3.0.0")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Running Your Application in the Maven Process")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === Running Your Application in the Maven Process
                                          
                                          ==== What Changed
                                          The `fork` attribute of `spring-boot:run` and `spring-boot:start` that was deprecated in Spring Boot 2.7 has been removed.
                                          Hence, we should replace with `process-exec-maven-plugin`.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `org\\.springframework\\.boot\\:spring-boot-starter\\:.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          Add the process-exec-maven-plugin in pom.xml under plugin section, for more details, please go through the link https://github.com/bazaarvoice/maven-process-plugin[Maven Process Plugin^, role="ext-link"]. +
                                          

                                          """);
    }

    @Test
    @DisplayName("Running Your Application in the Maven Process should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Running Your Application in the Maven Process")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
