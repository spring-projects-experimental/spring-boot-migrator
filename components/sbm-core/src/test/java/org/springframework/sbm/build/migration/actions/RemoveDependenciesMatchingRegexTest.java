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
package org.springframework.sbm.build.migration.actions;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class RemoveDependenciesMatchingRegexTest {

    @Test
    @Tag("integration")
    void apply() {
        String pomSource =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.superbiz.jsf</groupId>\n" +
                        "    <artifactId>jsf-managedBean-and-ejb</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>8.0.5-SNAPSHOT</version>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "           <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter</artifactId>\n" +
                        "                <version>5.7.1</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.junit.jupiter</groupId>\n" +
                        "            <artifactId>junit-jupiter</artifactId>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.junit.jupiter</groupId>\n" +
                        "            <artifactId>junit-jupiter-api</artifactId>\n" +
                        "            <version>5.6.3</version>\n" +
                        "            <scope>test</scope>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.mockito</groupId>\n" +
                        "            <artifactId>mockito-core</artifactId>\n" +
                        "            <version>3.7.7</version>\n" +
                        "            <scope>test</scope>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.apache.tomee</groupId>\n" +
                        "            <artifactId>openejb-core-hibernate</artifactId>\n" +
                        "            <version>8.0.5</version>\n" +
                        "            <type>pom</type>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>\n";

        String expectedResult =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.superbiz.jsf</groupId>\n" +
                        "    <artifactId>jsf-managedBean-and-ejb</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>8.0.5-SNAPSHOT</version>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "           <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter</artifactId>\n" +
                        "                <version>5.7.1</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "</project>\n";

        RemoveDependenciesMatchingRegex sut = new RemoveDependenciesMatchingRegex(new ArrayList<>());
        sut.setDependenciesRegex(List.of(
                "org\\.mockito\\:mockito-core\\:3\\..*",
                "org\\.junit\\.jupiter\\:(junit-jupiter|junit-juniper-api).*",
                "org\\.apache\\.tomee\\:openejb-core-hibernate\\:.*"
        ));

        OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomSource, expectedResult, sut);
    }
}