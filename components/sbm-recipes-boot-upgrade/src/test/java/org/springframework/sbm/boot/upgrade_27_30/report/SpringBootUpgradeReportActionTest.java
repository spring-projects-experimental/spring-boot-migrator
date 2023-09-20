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
package org.springframework.sbm.boot.upgrade_27_30.report;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.parsers.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.test.RecipeIntegrationTestSupport;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class SpringBootUpgradeReportActionTest {

    @Test
    void renderReport() throws IOException {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .withProjectResource("src/main/resources/application.properties", "spring.data.foo=bar")
                .withProjectResource("src/main/resources/application-another.properties", "spring.data.here=there")
                .build();

        @Language("adoc")
        String expectedOutput =
                        """
                        [[index]]
                        = Spring Boot 3 Upgrade Report
                        Sandeep Nagaraj, 
                        Fabian Krüger
                        :source-highlighter: highlight.js
                        :highlightjs-languages: java
                        :linkcss:
                        :doctype: book
                        :idprefix:
                        :idseparator: -
                        :toc: left
                        :sectnumlevels: 2
                        :toclevels: 2
                        :tabsize: 4
                        :numbered:
                        :sectanchors:
                        :sectnums:
                        :hide-uri-scheme:
                        :docinfo: shared,private
                        :attribute-missing: warn
                        :chomp: default headers packages
                        :spring-boot-artifactory-repo: snapshot
                        :github-tag: main
                        :spring-boot-version: current
                        
                        == Introduction
                        [cols="1h,3"]
                        |===
                        | Scanned dir | `<PATH>`
                        | Revision | Scanned project not under Git
                        | Coordinate | `com.example:dummy-root:0.1.0-SNAPSHOT`
                        | Boot version | `2.7.3`
                        | Changes | 2
                        |===
                        
                        The application was scanned and matched against the changes listed in the
                        https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.5-Release-Notes[Spring Boot 2.5 Release Notes]
                        as well as from https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-5.x[Spring Framework 5.x Release Notes].
                        
                        The Relevant Changes section lists all potentially required changes to upgrade the scanned application to Spring Boot 2.5.6.
                        
                        NOTE: JDK 17 is required for Spring Boot 3
                        
                        == Relevant Changes
                        
                        This section lists the changes SBM found to be applicable to upgrade the scanned application to Spring Boot 3.0.0.
                        
                        === Changes to Data Properties
                        Issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/441[#441], Contributors: https://github.com/fabapp2[@fabapp2^, role="ext-link"]
                        
                        ==== What Changed
                        The data prefix has been reserved for Spring Data and any properties under the `spring.data` prefix imply that Spring
                        Data is required on the classpath.
                        
                        ==== Why is the application affected
                        The scan found properties with `spring.data` prefix but no dependency matching `org.springframework.data:.*`.
                        
                        * file://<PATH>/src/main/resources/application.properties[`src/main/resources/application.properties`]
                        ** `spring.data.foo`
                        * file://<PATH>/src/main/resources/application-another.properties[`src/main/resources/application-another.properties`]
                        ** `spring.data.here`
                        
                        ==== Remediation
                        Either add `spring-data` dependency, rename the property or remove it in case it's not required anymore.
                        
                        
                        
                        === Logging Date Format
                        Issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/489[#489], Contributors: https://github.com/fabapp2[@fabapp2^, role="ext-link"]
                        
                        ==== What Changed
                        The default format for the date and time component of log messages for Logback and Log4j2 has changed to
                        align with the ISO-8601 standard. The new default format `yyyy-MM-dd’T’HH:mm:ss.SSSXXX` uses a `T` to
                        separate the date and time instead of a space character and adds the timezone offset to the end.
                        The `LOG_DATEFORMAT_PATTERN` environment variable or `logging.pattern.dateformat` property can be used to
                        restore the previous default value of `yyyy-MM-dd HH:mm:ss.SSS`.
                        
                        ==== Why is the application affected
                        The scan found no property `logging.pattern.dateformat`.
                        
                        ==== Remediation
                        Set `logging.pattern.dateformat=yyyy-MM-dd HH:mm:ss.SSS` to fall back to the previous log format.
                        
                        
                        
                        
                        We want to say thank you to all Contributors:
                        
                        Generated by Spring Boot Migrator (experimental)
                        """;

        SpringBootUpgradeReportTestSupport.generatedReport()
                .fromProjectContext(context)
                .shouldRenderAs(expectedOutput, Map.of("PATH", Path.of(".").toAbsolutePath().resolve(TestProjectContext.getDefaultProjectRoot()).toString()));
    }

    @Test
    void verifyRenderedHtml(@TempDir Path tempDir) throws IOException {
        @Language("xml")
        String pomSource = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-parent</artifactId>
                        <version>2.7.3</version>
                        <relativePath/> <!-- lookup parent from repository -->
                    </parent>
                    <groupId>com.example</groupId>
                    <artifactId>spring-boot-27</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>spring-boot-2.7</name>
                    <description>spring-boot-2.7</description>
                    <properties>
                        <java.version>17</java.version>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter</artifactId>
                        </dependency>
                                                                      
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-test</artifactId>
                            <scope>test</scope>
                        </dependency>
                    </dependencies>
                                                                      
                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                            </plugin>
                        </plugins>
                    </build>
                                                                      
                </project>
                """;

        TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .withMavenRootBuildFileSource(pomSource)
                .withProjectResource("src/main/resources/application.properties", "spring.data.foo=bar")
                .withProjectResource("src/main/resources/application-another.properties", "spring.data.here=there")
                .serializeProjectContext(tempDir);

        RecipeIntegrationTestSupport.initializeProject(tempDir, "spring-upgrade-report")
                .andApplyRecipe("sbu30-report");

        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            Path curDir = Path.of(".").toAbsolutePath().normalize();
            final HtmlPage page = webClient.getPage("file://"+curDir+"/target/testcode/spring-upgrade-report/spring-boot-upgrade-report/report.html");

            final String pageAsText = page.asNormalizedText();
            List<String> h2Headers = getTextContentOfAllElements(page, "h2");
            List<String> h3Headers = getTextContentOfAllElements(page, "h3");

            // verify title and some elements to verify the HTML was rendered
            assertThat(pageAsText.contains("Spring Boot 3 Upgrade Report")).isTrue();
            assertThat(h2Headers).containsExactly(
                    "Introduction",
                    "Relevant Changes"
            );
            assertThat(h3Headers).anySatisfy(e -> e.matches("2\\.\\d Changes to Data Properties"));
            assertThat(h3Headers).anySatisfy(e -> e.matches("2\\.\\d Logging Date Format"));
        }
    }

    @NotNull
    private List<String> getTextContentOfAllElements(HtmlPage page, String tagName) {
        NodeList nodes = page.getElementsByTagName(tagName);
        List<String> elements = new ArrayList<>();
        for(int i=0; i < nodes.getLength(); i++) {
            elements.add(nodes.item(i).getTextContent());
        }
        return elements;
    }

}
