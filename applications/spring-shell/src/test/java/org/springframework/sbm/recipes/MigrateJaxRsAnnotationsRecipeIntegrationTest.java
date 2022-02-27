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
            "package com.example.jee.app;\n" +
                    "\n" +
                    "import org.springframework.http.MediaType;\n" +
                    "import org.springframework.web.bind.annotation.PathVariable;\n" +
                    "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                    "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                    "import org.springframework.web.bind.annotation.RestController;\n" +
                    "\n" +
                    "@RestController\n" +
                    "@RequestMapping(value = \"/\")\n" +
                    "public class PersonController {\n" +
                    "\n" +
                    "    @RequestMapping(value = \"/json/{name}\", produces = \"application/json\", consumes = \"application/json\", method = RequestMethod.POST)\n" +
                    "    public String getHelloWorldJSON(@PathVariable(\"name\") String name) throws Exception {\n" +
                    "        System.out.println(\"name: \" + name);\n" +
                    "        return \"{\\\"Hello\\\":\\\"\" + name + \"\\\"\";\n" +
                    "    }\n" +
                    "\n" +
                    "    @RequestMapping(value = \"/json\", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)\n" +
                    "    public String getAllPersons() throws Exception {\n" +
                    "        return \"{\\\"message\\\":\\\"No person here...\\\"\";\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "    @RequestMapping(value = \"/xml/{name}\", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.POST)\n" +
                    "    public String getHelloWorldXML(@PathVariable(\"name\") String name) throws Exception {\n" +
                    "        System.out.println(\"name: \" + name);\n" +
                    "        return \"<xml>Hello \"+name+\"</xml>\";\n" +
                    "    }\n" +
                    "\n" +
                    "}\n";

    private final String expectedPomSource =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                    "    <modelVersion>4.0.0</modelVersion>\n" +
                    "    <groupId>org.springframework.sbm.examples</groupId>\n" +
                    "    <artifactId>migrate-jax-rs</artifactId>\n" +
                    "    <packaging>jar</packaging>\n" +
                    "    <version>0.0.1-SNAPSHOT</version>\n" +
                    "    <properties>\n" +
                    "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                    "    </properties>\n" +
                    "    <build>\n" +
                    "        <plugins>\n" +
                    "            <plugin>\n" +
                    "                <groupId>org.apache.maven.plugins</groupId>\n" +
                    "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                    "                <version>3.5.1</version>\n" +
                    "                <configuration>\n" +
                    "                    <source>1.8</source>\n" +
                    "                    <target>1.8</target>\n" +
                    "                </configuration>\n" +
                    "            </plugin>\n" +
                    "        </plugins>\n" +
                    "    </build>\n" +
                    "    <dependencies>\n" +
                    "        <dependency>\n" +
                    "            <groupId>org.springframework.boot</groupId>\n" +
                    "            <artifactId>spring-boot-starter-web</artifactId>\n" +
                    "            <version>2.3.4.RELEASE</version>\n" +
                    "        </dependency>\n" +
                    "        <dependency>\n" +
                    "            <groupId>org.jboss.spec.javax.ws.rs</groupId>\n" +
                    "            <artifactId>jboss-jaxrs-api_2.1_spec</artifactId>\n" +
                    "            <version>1.0.1.Final</version>\n" +
                    "            <scope>runtime</scope>\n" +
                    "        </dependency>\n" +
                    "    </dependencies>\n" +
                    "    <repositories>\n" +
                    "        <repository>\n" +
                    "            <id>jcenter</id>\n" +
                    "            <name>jcenter</name>\n" +
                    "            <url>https://jcenter.bintray.com</url>\n" +
                    "        </repository>\n" +
                    "        <repository>\n" +
                    "            <id>mavencentral</id>\n" +
                    "            <name>mavencentral</name>\n" +
                    "            <url>https://repo.maven.apache.org/maven2</url>\n" +
                    "        </repository>\n" +
                    "    </repositories>\n" +
                    "</project>\n";


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
