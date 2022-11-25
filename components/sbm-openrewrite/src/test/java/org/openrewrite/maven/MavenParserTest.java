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
package org.openrewrite.maven;

import org.assertj.core.api.Assertions;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.internal.RecipeRunException;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.Problem;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Fabian Krüger
 */
public class MavenParserTest {

    @Test
    void test_renameMe() {
        MavenParser mavenParser = MavenParser.builder().build();
        Parser.Input parserInput = new Parser.Input(
                             Path.of("moduleA/pom.xml"),
                             null,
                             () -> new ByteArrayInputStream(
                                    """
                                    <?xml version="1.0" encoding="UTF-8"?>
                                    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                        <modelVersion>4.0.0</modelVersion>
                                        <groupId>com.example</groupId>
                                        <artifactId>parent</artifactId>
                                        <version>0.1</version>
                                        <packaging>pom</packaging>
                                        <modules>
                                            <module>moduleA</module>
                                        </modules>
                                    </project>
                                    """
                                     .getBytes(StandardCharsets.UTF_8)),
                             !Files.exists(Path.of("moduleA/pom.xml"))
                );
        List<Xml.Document> newMavenFiles = mavenParser.parseInputs(List.of(parserInput), null, new InMemoryExecutionContext((t) -> t.printStackTrace()));
//        System.out.println(newMavenFiles.get(0).printAll());
    }

    @Test
    void parsePomsWithInvalidDeps() {
        @Language("xml")
        String parentPomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0.0</version>
                    <packaging>pom</packaging>
                    <modules>
                        <module>module1</module>
                    </modules>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>javax.validation</groupId>
                                <!-- It's just 'validation-api' -->
                                <artifactId>javax.validation-api</artifactId>
                                <version>2.0.0.Final</version>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                </project>
                """;

        @Language("xml")
        String module1PomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.example</groupId>
                        <artifactId>parent</artifactId>
                        <version>1.0.0</version>
                        <relativePath/>
                    </parent>
                    <artifactId>module1</artifactId>
                    <packaging>jar</packaging>

                    <dependencies>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>javax.validation-api</artifactId>
                        </dependency>
                    </dependencies>
                </project>
                """;
        MavenParser mavenParser = MavenParser.builder().build();
        Xml.Document parentPom = mavenParser.parse(parentPomXml).get(0);
        Optional<MavenResolutionResult> mavenResolutionResult = parentPom.getMarkers().findFirst(MavenResolutionResult.class);
        assertThat(mavenResolutionResult).isPresent();
        assertThatExceptionOfType(RecipeRunException.class)
                .isThrownBy(() -> mavenParser.parse(parentPomXml, module1PomXml))
                .describedAs("Maven visitors should not be visiting XML documents without a Maven marker");
    }

    @Test
    void parsePomFromTextWithoutMarkers() {
        Xml.Document sut = MavenParser.builder().build().parse(
                new InMemoryExecutionContext((e) -> e.printStackTrace()),
                  """
                  <?xml version="1.0" encoding="UTF-8"?>
                  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                      <modelVersion>4.0.0</modelVersion>
                      <groupId>com.example</groupId>
                      <artifactId>parent</artifactId>
                      <version>0.1</version>
                      <packaging>pom</packaging>
                      <properties>
                          <some-property>value1</some-property>
                      </properties>
                      <modules>
                          <module>moduleA</module>
                      </modules>
                  </project>
                  """
        ).get(0);
        assertThat(sut).isNotNull();
    }


    @Problem(description = "java.io.UncheckedIOException: Failed to parse pom", since = "7.18.2", fixedIn = "7.23.0")
    void testParsingPomWithEmptyDependenciesSection() {
        String pomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>foo-bar</artifactId>\n" +
                "    <version>0.1.0-SNAPSHOT</version>\n" +
                "    <dependencies></dependencies>\n" +
                "</project>";

        List<Xml.Document> parse = MavenParser.builder().build().parse(pomXml);
        assertThat(parse).isNotEmpty();
    }

    @Test
    void test(@TempDir Path tempDir) {
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
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.apache.tomee</groupId>\n" +
                        "            <artifactId>openejb-core-hibernate</artifactId>\n" +
                        "            <version>8.0.5</version>\n" +
                        "            <type>pom</type>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        Xml.Document document = MavenParser.builder().build().parse(pomXml).get(0);
        MavenResolutionResult r = document.getMarkers().findFirst(MavenResolutionResult.class).get();

        InMemoryExecutionContext executionContext = new InMemoryExecutionContext((t) -> System.out.println(t.getMessage()));
        MavenExecutionContextView ctx = MavenExecutionContextView.view(executionContext);
        ctx.setPomCache(new InMemoryMavenPomCache());
        List<ResolvedDependency> resolvedDependencies = r.getDependencies().get(Scope.Provided);
        assertThat(r.getDependencies()).hasSize(4);
        assertThat(resolvedDependencies).hasSize(81); // FIXME: #7 was 81 before ?!
    }

}
