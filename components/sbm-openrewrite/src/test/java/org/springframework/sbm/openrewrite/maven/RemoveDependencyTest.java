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

package org.springframework.sbm.openrewrite.maven;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.Result;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.RemoveDependency;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.tree.Xml;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("#7 deleted dependencies not reflected in marker, see https://rewriteoss.slack.com/archives/G01J94KRH70/p1651168478382839")
public class RemoveDependencyTest {
    @Test
    void deletePomTypeDependency() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>foo</groupId>\n" +
                        "    <artifactId>bar</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.apache.tomee</groupId>\n" +
                        "            <artifactId>openejb-core-hibernate</artifactId>\n" +
                        "            <version>8.0.5</version>\n" +
                        "            <type>pom</type>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        List<Xml.Document> mavens = MavenParser.builder().build().parse(pomXml);

        List<Result> run = new RemoveDependency("org.apache.tomee", "openejb-core-hibernate", null).run(mavens);

        System.out.println(run.get(0).getAfter().printAll());

        assertThat(run.get(0).getAfter().getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequestedDependencies()).isEmpty();
    }

    @Test
    void deleteTypeDependency() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>foo</groupId>\n" +
                        "    <artifactId>bar</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "           <groupId>org.junit.jupiter</groupId>\n" +
                        "           <artifactId>junit-jupiter-api</artifactId>\n" +
                        "           <version>5.7.0</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        List<Xml.Document> mavens = MavenParser.builder().build().parse(pomXml);

        assertThat(mavens.get(0).getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequestedDependencies()).hasSize(1);

        List<Result> run = new RemoveDependency("org.junit.jupiter", "junit-jupiter-api", null).run(mavens);

        assertThat(run.get(0).getAfter().getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequestedDependencies()).isEmpty();
    }

    @Test
    void deleteJarTypeDependency() {

        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>foo</groupId>\n" +
                        "    <artifactId>bar</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <dependencies>\n" +
                        "           <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter</artifactId>\n" +
                        "                <version>5.7.1</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        List<Xml.Document> mavens = MavenParser.builder().build().parse(pomXml);

        assertThat(mavens.get(0).getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequestedDependencies()).hasSize(1);

        List<Result> run = new RemoveDependency("org.junit.jupiter", "junit-jupiter", "test").run(mavens);

        System.out.println(run.get(0).getAfter().printAll());

        assertThat(run.get(0).getAfter().getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequestedDependencies()).isEmpty();

    }
}
