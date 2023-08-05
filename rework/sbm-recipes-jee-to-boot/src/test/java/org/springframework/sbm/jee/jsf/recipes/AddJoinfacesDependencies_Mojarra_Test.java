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
package org.springframework.sbm.jee.jsf.recipes;

import org.springframework.sbm.build.migration.actions.OpenRewriteMavenBuildFileTestSupport;
import org.springframework.sbm.jee.jsf.actions.AddJoinfacesDependencies;
import org.junit.jupiter.api.Test;

class AddJoinfacesDependencies_Mojarra_Test {

    @Test
    void apply() {
        String givenPom =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.superbiz.jsf</groupId>\n" +
                        "    <artifactId>jsf-managedBean-and-ejb</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>8.0.5-SNAPSHOT</version>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.glassfish</groupId>\n" +
                        "            <artifactId>javax.faces</artifactId>\n" +
                        "            <version>2.2.13</version>\n" +
                        "            <type>jar</type>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>\n";

        String expectedPom =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.superbiz.jsf</groupId>\n" +
                        "    <artifactId>jsf-managedBean-and-ejb</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>8.0.5-SNAPSHOT</version>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.glassfish</groupId>\n" +
                        "            <artifactId>javax.faces</artifactId>\n" +
                        "            <version>2.2.13</version>\n" +
                        "            <type>jar</type>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.joinfaces</groupId>\n" +
                        "            <artifactId>jsf-spring-boot-starter</artifactId>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.joinfaces</groupId>\n" +
                        "                <artifactId>joinfaces-dependencies</artifactId>\n" +
                        "                <version>4.4.10</version>\n" +
                        "                <type>pom</type>\n" +
                        "                <scope>import</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "</project>\n";

        AddJoinfacesDependencies sut = new AddJoinfacesDependencies();

        OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(givenPom, expectedPom, sut);
    }
}