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
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportTestSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;


public class JohnzonDependencyReportSectionTest {

    @Test
    public void shouldNotShowJsonBSection() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withDummyRootBuildFile()
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("JSON-B")
                .fromProjectContext(context)
                .shouldNotRender();
    }

    @Test
    public void rendersBannerSupportInformation() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.apache.johnzon:johnzon-core:1.2.11")
                .build();


        SpringBootUpgradeReportTestSupport
                .generatedSection("JSON-B")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                                === JSON-B
                                                                
                                ==== What Changed
                                Dependency management for Apache Johnzon has been removed in favor of Eclipse Yasson.
                                A Jakarta EE 10-compatible version of Apache Johnzon can be used with Spring Boot 3, but you will now have to specify a version in your dependency declaration.
                                                                
                                ==== Why is the application affected
                                This application uses johnzon-core from org.apache.johnzon. Spring Boot 3.0 does not provide dependency support for this library,
                                so an explicit version number must now be provided in the POM for this dependency.
                                                                
                                ==== Remediation
                                This recipe will add an explicit version number to the org.apache.johnzon:johnzon-core dependency in the POM.
                                                                
                                                                
                                """);
    }

    @Test
    public void helperWorksWithoutExplicitVersion() {

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <parent>
                               <groupId>org.springframework.boot</groupId>
                               <artifactId>spring-boot-starter-parent</artifactId>
                               <version>2.7.1</version>
                               <relativePath/>
                             </parent>
                            <groupId>com.example</groupId>
                            <artifactId>dummy-root</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                            <packaging>pom</packaging>
                            <dependencies>
                                <dependency>
                                    <groupId>org.apache.johnzon</groupId>
                                    <artifactId>johnzon-core</artifactId>
                                </dependency>
                            </dependencies>
                                                
                        </project>
                        """)
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("JSON-B")
                .fromProjectContext(context);
    }
}
