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

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportTestSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

public class ConstructorBindingReportSectionTest {

    @Test
    public void reportMigrationSuggestionsWhenConstructorBindingUsageIsFound() {
        @Language("java")
        String javaClassWithConstructorBinding = """
                package com.example;
                                                
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.boot.context.properties.ConfigurationProperties;
                import org.springframework.boot.context.properties.ConstructorBinding;
                                                
                @ConfigurationProperties(prefix = "mail")
                @ConstructorBinding
                public class ConfigProperties {
                    private String hostName;
                    private int port;
                    private String from;
                                                
                    public ConfigProperties(String hostName, int port, String from) {
                        this.hostName = hostName;
                        this.port = port;
                        this.from = from;
                    }
                                                
                    public String getHostName() {
                        return hostName;
                    }
                                                
                    public int getPort() {
                        return port;
                    }
                                                
                    public String getFrom() {
                        return from;
                    }
                }
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java", javaClassWithConstructorBinding)
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot:2.7.1")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Constructor Binding")
                .fromProjectContext(context)
                .shouldRenderAs(
                   """
                    === Constructor Binding
                                                    
                    ==== What Changed
                    When using constructor bound @ConfigurationProperties the @ConstructorBinding annotation
                    is no longer required if the class has a single parameterized constructor.
                    If you have more than one constructor, you'll still need to use `@ConstructorBinding`
                    to tell Spring Boot which one to use.
                                                    
                    For most users, this updated logic will allow for simpler `@ConfigurationProperties`
                    classes. If, however, you have a `@ConfigurationProperties` and you want to inject
                    beans into the constructor rather than binding it, you'll now need to add an
                    `@Autowired` annotation.
                                                    
                    ==== Why is the application affected
                    We found usage of `@ConstructorBinding` in following files:

                    * <PATH>/src/main/java/com/example/ConfigProperties.java
                                                    
                    ==== Remediation
                    Remove `@ConstructorBinding` if it matches the criteria, please refer issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/166[#166]
                    for more information
                                       
                    * https://github.com/spring-projects-experimental/spring-boot-migrator/issues/166[Issue 166]                                                           
                    """
                );
    }

    @Test
    public void shouldNotReportConstructorBindingSuggestionWhenNothingIsFound() {
        @Language("java")
        String javaClassWithConstructorBinding = """
                package com.example;
                                                
                public class A { }
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java/com/example/A.java", javaClassWithConstructorBinding)
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot:2.7.1")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Constructor Binding")
                .fromProjectContext(context)
                .shouldNotRender();
    }
}