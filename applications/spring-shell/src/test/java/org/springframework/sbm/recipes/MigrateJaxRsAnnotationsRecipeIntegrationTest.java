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
package org.springframework.sbm.recipes;

import org.springframework.sbm.IntegrationTestBaseClass;
import org.springframework.sbm.testhelper.common.utils.TestDiff;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrateJaxRsAnnotationsRecipeIntegrationTest extends IntegrationTestBaseClass {

    @Override
    protected String getTestSubDir() {
        return "bootify-jaxrs";
    }

    private final String expectedJavaSource =
            """
                    package com.example.jee.app;

                    import org.springframework.http.MediaType;
                    import org.springframework.web.bind.annotation.*;

                    @RestController
                    @RequestMapping(value = "/")
                    public class PersonController {

                        @RequestMapping(value = "/json/{name}", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
                        public String getHelloWorldJSON(@PathVariable("name") String name) throws Exception {
                            System.out.println("name: " + name);
                            return "{\\"Hello\\":\\"" + name + "\\"";
                        }

                        @RequestMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
                        public String getAllPersons(@RequestParam(required = false, value = "q") String searchBy, @RequestParam(required = false, defaultValue = "0", value = "page") int page) throws Exception {
                            return "{\\"message\\":\\"No person here...\\"";
                        }


                        @RequestMapping(value = "/xml/{name}", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.POST)
                        public String getHelloWorldXML(@PathVariable("name") String name) throws Exception {
                            System.out.println("name: " + name);
                            return "<xml>Hello "+name+"</xml>";
                        }

                    }
                    """;

    private final String expectedPomSource =
            """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                        <modelVersion>4.0.0</modelVersion>
                        <groupId>org.springframework.sbm.examples</groupId>
                        <artifactId>migrate-jax-rs</artifactId>
                        <packaging>jar</packaging>
                        <version>0.0.1-SNAPSHOT</version>
                        <properties>
                            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                        </properties>
                        <build>
                            <plugins>
                                <plugin>
                                    <groupId>org.apache.maven.plugins</groupId>
                                    <artifactId>maven-compiler-plugin</artifactId>
                                    <version>3.5.1</version>
                                    <configuration>
                                        <source>1.8</source>
                                        <target>1.8</target>
                                    </configuration>
                                </plugin>
                            </plugins>
                        </build>
                        <dependencies>
                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-starter-web</artifactId>
                                <version>2.3.4.RELEASE</version>
                            </dependency>
                            <dependency>
                                <groupId>org.jboss.spec.javax.ws.rs</groupId>
                                <artifactId>jboss-jaxrs-api_2.1_spec</artifactId>
                                <version>1.0.1.Final</version>
                                <scope>runtime</scope>
                            </dependency>
                        </dependencies>
                        <repositories>
                            <repository>
                                <id>jcenter</id>
                                <name>jcenter</name>
                                <url>https://jcenter.bintray.com</url>
                            </repository>
                            <repository>
                                <id>mavencentral</id>
                                <name>mavencentral</name>
                                <url>https://repo.maven.apache.org/maven2</url>
                            </repository>
                        </repositories>
                    </project>
                    """;


    @Test
    @Tag("integration")
    void happyPath() {
        intializeTestProject();
        super.scanProject();
        super.applyRecipe("migrate-jax-rs");

        String javaFile = super.loadJavaFile("com.example.jee.app", "PersonController");
        assertThat(javaFile)
                .as(TestDiff.of(javaFile, expectedJavaSource))
                .isEqualTo(expectedJavaSource);

        String pomSource = super.loadFile(Path.of("pom.xml"));
        assertThat(pomSource)
                .as(TestDiff.of(pomSource, expectedPomSource))
                .isEqualTo(expectedPomSource);
    }
}
