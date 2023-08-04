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
package org.springframework.sbm.project.parser;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.marker.BuildTool;
import org.openrewrite.marker.Marker;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.build.impl.MavenSettingsInitializer;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JavaProvenanceMarkerFactoryTest {

    @Test
    void test() {
        JavaProvenanceMarkerFactory sut = new JavaProvenanceMarkerFactory();

        String pomXmlSource =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>module1</artifactId>\n" +
                "    <version>2.3.7</version>\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <name>project-name</name>" +
                "    <properties>\n" +
                "        <maven.compiler.source>17</maven.compiler.source>\n" +
                "        <maven.compiler.target>11</maven.compiler.target>\n" +
                "    </properties>\n" +
                "\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.jetbrains</groupId>\n" +
                "            <artifactId>annotations</artifactId>\n" +
                "            <version>23.0.0</version>\n" +
                "            <scope>test</scope>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "\n" +
                "</project>";

        Path projectDirectory = Path.of("./faked-project-dir/pom.xml");
        Xml.Document maven = new RewriteMavenParser(new MavenSettingsInitializer(), new RewriteExecutionContext()).parse(pomXmlSource).toList().get(0).withSourcePath(Path.of("pom.xml"));

        List<Marker> javaProvenanceMarkers = sut.createJavaProvenanceMarkers(maven, projectDirectory, new RewriteExecutionContext());

        assertThat(javaProvenanceMarkers).hasSize(3);

        Marker javaVersionMarker = extractMarker(javaProvenanceMarkers, JavaVersion.class);

        String property = System.getProperty("java.version");
        String javaVersion = property.contains(".") ? property.split("\\.")[0] : property;
        ResourceVerifierTestHelper.javaVersionMarker(javaVersion, "17", "11").assertMarker(maven, javaVersionMarker);

        Marker buildToolMarker = extractMarker(javaProvenanceMarkers, BuildTool.class);
        ResourceVerifierTestHelper.buildToolMarker("Maven", "3.6").assertMarker(maven, buildToolMarker);

        Marker javaProjectMarker = extractMarker(javaProvenanceMarkers, JavaProject.class);
        ResourceVerifierTestHelper.javaProjectMarker("project-name", "com.example:module1:2.3.7").assertMarker(maven, javaProjectMarker);
    }

    @NotNull
    private Marker extractMarker(List<Marker> javaProvenanceMarkers, Class<? extends Marker> javaVersionClass) {
        return javaProvenanceMarkers.stream().filter(javaVersionClass::isInstance).findFirst().get();
    }

}