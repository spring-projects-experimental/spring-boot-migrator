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

import org.assertj.core.api.Assertions;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportTestSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

/**
 * @author Fabian Krüger
 */
public class SpringMVCAndWebFluxUrlMatchingChangesReportSectionTest {
    @Test
    void shouldRenderSection() {
        String restController1 =
                """
                package b.example;
                import org.springframework.web.bind.annotation.RestController;
                
                @RestController
                public class RestController1 {
                }
                """;

        String restController2 =
                """
                package a.example;
                import org.springframework.web.bind.annotation.RestController;
                
                @RestController
                public class RestController2 {
                }
                """;

        String anotherClass =
                """
                package com.example;
                public class AnotherClass {};
                """;

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework:spring-web:5.3.23")
                .addJavaSource("src/main/java", restController1)
                .addJavaSource("src/main/java", restController2)
                .addJavaSource("src/main/java", anotherClass)
                .build();

        @Language("adoc")
        String expectedOutput =
                """
                === Spring MVC and WebFlux URL matching changes
                Issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/522[#522^, role="ext-link"], Contributors: https://github.com/fabapp2[@fabapp2^, role="ext-link"]
                                                 
                ==== What Changed
                As of Spring Framework 6.0, the trailing slash matching configuration option has been deprecated and its default value set to `false`.
                This means that previously, the following controller would match both "GET /some/greeting" and "GET /some/greeting/":

                ```
                @RestController
                public class MyController {

                  @GetMapping("/some/greeting")
                  public String greeting {
                    return "Hello";
                  }

                }
                ```

                As of https://github.com/spring-projects/spring-framework/issues/28552[this Spring Framework change], "GET /some/greeting/" doesn't
                match anymore by default.

                Developers should instead configure explicit redirects/rewrites through a proxy, a Servlet/web filter, or even declare the additional route explicitly on the controller handler (like `@GetMapping("/some/greeting", "/some/greeting/")` for more targeted cases.

                Until your application fully adapts to this change, you can change the default with the following global configuration:

                ```
                @Configuration
                public class WebConfiguration implements WebMvcConfigurer {

                    @Override
                    public void configurePathMatch(PathMatchConfigurer configurer) {
                      configurer.setUseTrailingSlashMatch(true);
                    }
                                                 
                }
                ```
                                                 
                ==== Why is the application affected
                The scan found classes annotated with `@RestController` which could be affected by this change.
                * file://<PATH>/src/main/java/a/example/RestController2.java[`src/main/java/a/example/RestController2.java`]
                * file://<PATH>/src/main/java/b/example/RestController1.java[`src/main/java/b/example/RestController1.java`]
                                                 
                ==== Remediation
                You have different choices to remediate this change.
                                                 
                ===== Do Nothing
                If no clients expect a response for requests with a trailing `/` nothing needs to done.
                                                 
                ===== Configure explicit redirects/rewrites
                Configure explicit redirects/rewrites through a proxy, a Servlet/web filter.
                                                 
                ===== Add additional path mappings
                If only some endpoints should handle requests with a trailing `/`, another path can be provided to the
                existing request mapping.
                                                
                [source, java]
                ....
                @RequestMapping(value = {"/current/path", "current/path/"})
                                                
                @RequestMapping(path = {"/current/path", "current/path/"})
                                                
                @GetMapping(value = {"/current/path", "current/path/"})
                                                 
                @GetMapping(path = {"/current/path", "current/path/"})
                ...
                ....
                                                 
                * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/GetMapping.html[@GetMapping API]
                * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RequestMapping.html[@RequestMapping API]
                                                 
                ===== Reset behavior to prior 3.0
                If all rest controllers must serve requests with trailing `/`, a `PathMatchConfigurer` should be configured like so:
                                               
                [source, java]
                ....
                @Configuration
                public class WebConfiguration implements WebMvcConfigurer {
                                               
                  @Override
                  public void configurePathMatch(PathMatchConfigurer configurer) {
                    configurer.setUseTrailingSlashMatch(true);
                  }
                                               
                }
                ....
                
                
                """;

        SpringBootUpgradeReportTestSupport.generatedSection("Spring MVC and WebFlux URL matching changes")
                .fromProjectContext(context)
                .shouldRenderAs(expectedOutput);

    }
}
