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
package org.springframework.sbm.support.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;

import java.util.List;
import java.util.stream.Stream;

public class DependencyWithTypePom_SBM41 {

    @Test
    void typeOfDependencyShouldBeReflectedInMavenModel() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>foo</groupId>\n" +
                        "    <artifactId>bar</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>foobat</name>\n" +
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
                        "    </repositories>" +
                        "\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.apache.tomee</groupId>\n" +
                        "            <artifactId>openejb-core-hibernate</artifactId>\n" +
                        "            <version>8.0.5</version>\n" +
                        "            <type>pom</type>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        Stream<SourceFile> mavenList = MavenParser.builder()
                .build()
                .parse(pomXml);

        // FIXME: Test without assertions

//        Pom.Dependency theDependency = mavenList.get(0).getModel().getDependencies().iterator().next();
//        assertThat(theDependency.getGroupId()).isEqualTo("org.apache.tomee");
//        assertThat(theDependency.getArtifactId()).isEqualTo("openejb-core-hibernate");
//        assertThat(theDependency.getVersion()).isEqualTo("8.0.5");
//        assertThat(theDependency.getType()).isEqualTo("pom");
    }

}
