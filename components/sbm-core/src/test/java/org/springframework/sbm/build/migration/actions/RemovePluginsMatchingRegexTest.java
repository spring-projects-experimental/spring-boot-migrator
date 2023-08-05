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
package org.springframework.sbm.build.migration.actions;

import org.springframework.sbm.engine.recipe.Action;
import org.junit.jupiter.api.Test;

import java.util.List;

class RemovePluginsMatchingRegexTest {

    @Test
    void apply() {
        String given =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.mule.examples</groupId>\n" +
                        "    <artifactId>hello-world</artifactId>\n" +
                        "    <version>2.1.5-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <name>hello-world</name>\n" +
                        "\n" +
                        "    <properties>\n" +
                        "        <mule.maven.plugin.version>3.5.3</mule.maven.plugin.version>\n" +
                        "        <munit.version>2.2.4</munit.version>\n" +
                        "        <app.runtime>4.1.5</app.runtime>\n" +
                        "        <http.connector.version>1.5.4</http.connector.version>\n" +
                        "        <sockets.connector.version>1.1.5</sockets.connector.version>\n" +
                        "        <maven.compiler.target>11</maven.compiler.target>\n" +
                        "        <maven.compiler.source>11</maven.compiler.source>\n" +
                        "    </properties>\n" +
                        "    <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.springframework.boot</groupId>\n" +
                        "                <artifactId>spring-boot-maven-plugin</artifactId>\n" +
                        "                <version>${spring-boot.version}</version>\n" +
                        "                <executions>\n" +
                        "                    <execution>\n" +
                        "                        <configuration>\n" +
                        "                            <mainClass>org.springframework.sbm.SpringShellApplication</mainClass>\n" +
                        "                        </configuration>\n" +
                        "                        <goals>\n" +
                        "                            <goal>repackage</goal>\n" +
                        "                        </goals>\n" +
                        "                    </execution>\n" +
                        "                </executions>\n" +
                        "            </plugin>" +
                        "            <plugin>\n" +
                        "                <groupId>org.mule.tools.maven</groupId>\n" +
                        "                <artifactId>mule-maven-plugin</artifactId>\n" +
                        "                <version>${mule.maven.plugin.version}</version>\n" +
                        "                <extensions>true</extensions>\n" +
                        "                <configuration>\n" +
                        "                    <classifier>mule-application-example</classifier>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>com.mulesoft.munit.tools</groupId>\n" +
                        "                <artifactId>munit-maven-plugin</artifactId>\n" +
                        "                <version>${munit.version}</version>\n" +
                        "                <executions>\n" +
                        "                    <execution>\n" +
                        "                        <id>test</id>\n" +
                        "                        <phase>test</phase>\n" +
                        "                        <goals>\n" +
                        "                            <goal>test</goal>\n" +
                        "                            <goal>coverage-report</goal>\n" +
                        "                        </goals>\n" +
                        "                    </execution>\n" +
                        "                </executions>\n" +
                        "                <configuration>\n" +
                        "                    <coverage>\n" +
                        "                        <runCoverage>true</runCoverage>\n" +
                        "                        <formats>\n" +
                        "                            <format>html</format>\n" +
                        "                        </formats>\n" +
                        "                    </coverage>\n" +
                        "                    <runtimeVersion>${app.runtime}</runtimeVersion>\n" +
                        "                    <dynamicPorts>\n" +
                        "                        <dynamicPort>http.port</dynamicPort>\n" +
                        "                    </dynamicPorts>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>\n";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.mule.examples</groupId>\n" +
                        "    <artifactId>hello-world</artifactId>\n" +
                        "    <version>2.1.5-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <name>hello-world</name>\n" +
                        "\n" +
                        "    <properties>\n" +
                        "        <mule.maven.plugin.version>3.5.3</mule.maven.plugin.version>\n" +
                        "        <munit.version>2.2.4</munit.version>\n" +
                        "        <app.runtime>4.1.5</app.runtime>\n" +
                        "        <http.connector.version>1.5.4</http.connector.version>\n" +
                        "        <sockets.connector.version>1.1.5</sockets.connector.version>\n" +
                        "        <maven.compiler.target>11</maven.compiler.target>\n" +
                        "        <maven.compiler.source>11</maven.compiler.source>\n" +
                        "    </properties>\n" +
                        "    <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.springframework.boot</groupId>\n" +
                        "                <artifactId>spring-boot-maven-plugin</artifactId>\n" +
                        "                <version>${spring-boot.version}</version>\n" +
                        "                <executions>\n" +
                        "                    <execution>\n" +
                        "                        <configuration>\n" +
                        "                            <mainClass>org.springframework.sbm.SpringShellApplication</mainClass>\n" +
                        "                        </configuration>\n" +
                        "                        <goals>\n" +
                        "                            <goal>repackage</goal>\n" +
                        "                        </goals>\n" +
                        "                    </execution>\n" +
                        "                </executions>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>\n";

        List<String> regex = List.of("com\\.mulesoft\\.munit\\.tools\\:.*", "org\\.mule\\.tools\\..*");
        Action sut = new RemovePluginsMatchingRegex(regex);
        OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(given, expected, sut);
    }
}