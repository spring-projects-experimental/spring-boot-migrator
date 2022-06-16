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
package org.springframework.sbm.support.openrewrite.maven;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openrewrite.Result;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.UpgradeParentVersion;
import org.openrewrite.xml.tree.Xml;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpgradeDependencyVersionTest {

    @Test
    @Tag("integration")
    void upgradeDependencyOfParent() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <parent>\n" +
                        "        <groupId>org.springframework.boot</groupId>\n" +
                        "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "        <version>2.4.12</version>\n" +
                        "        <relativePath/> <!-- lookup parent from repository -->\n" +
                        "    </parent>\n" +
                        "    <groupId>com.example</groupId>\n" +
                        "    <artifactId>spring-boot-24-to-25-example</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>spring-boot-2.4-to-2.5-example</name>\n" +
                        "    <description>spring-boot-2.4-to-2.5-example</description>\n" +
                        "    <properties>\n" +
                        "        <java.version>11</java.version>\n" +
                        "    </properties>\n" +
                        "</project>";

        Xml.Document maven = MavenParser.builder()
                .build()
                .parse(pomXml)
                .get(0);

        List<Result> results = new UpgradeParentVersion("org.springframework.boot", "spring-boot-starter-parent", "2.5.6", null).run(List.of(maven));

        assertThat(results.get(0).getAfter().printAll()).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <parent>\n" +
                "        <groupId>org.springframework.boot</groupId>\n" +
                "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                "        <version>2.5.6</version>\n" +
                "        <relativePath/> <!-- lookup parent from repository -->\n" +
                "    </parent>\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>spring-boot-24-to-25-example</artifactId>\n" +
                "    <version>0.0.1-SNAPSHOT</version>\n" +
                "    <name>spring-boot-2.4-to-2.5-example</name>\n" +
                "    <description>spring-boot-2.4-to-2.5-example</description>\n" +
                "    <properties>\n" +
                "        <java.version>11</java.version>\n" +
                "    </properties>\n" +
                "</project>");
    }
}
