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

class GitCommitIDMavenPluginReportSectionTest {
    @Test
    @DisplayName("Git Commit ID Maven Plugin should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter:3.0.0")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Git Commit ID Maven Plugin")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === Git Commit ID Maven Plugin
                                          
                                          ==== What Changed
                                          The Git Commit ID Maven Plugin has been updated to version 5 where its coordinates have changed.
                                          The previous coordinates were `pl.project13.maven:git-commit-id-plugin`.
                                          The new coordinates are `io.github.git-commit-id:git-commit-id-maven-plugin`.
                                          Any `<plugin>` declaration in your `pom.xml` file should be updated accordingly.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `org\\.springframework\\.boot\\:spring-boot-starter\\:.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#git-commit-id-maven-plugin[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/660[#660^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("Git Commit ID Maven Plugin should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Git Commit ID Maven Plugin")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
