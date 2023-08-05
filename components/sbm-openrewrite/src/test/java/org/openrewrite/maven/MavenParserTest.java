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
package org.openrewrite.maven;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.*;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.tree.*;
import org.openrewrite.tree.ParseError;
import org.springframework.sbm.GitHubIssue;
import org.springframework.sbm.Problem;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Fabian Krüger
 */
public class MavenParserTest {

    // TODO: Proof of incorrect MavenResolutionResult for
    @GitHubIssue("https://github.com/openrewrite/rewrite/issues/2601")
    @Test
    void mavenParserAddsMavenResolutionResultMarkerWithDuplicateDependencies() {
        final String parentPom = """
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <artifactId>example-project-parent</artifactId>
                            <groupId>com.example</groupId>
                            <version>1.0.0-SNAPSHOT</version>
                            <modelVersion>4.0.0</modelVersion>
                            <packaging>pom</packaging>
                        
                            <properties>
                                <maven.compiler.source>17</maven.compiler.source>
                                <maven.compiler.target>11</maven.compiler.target>
                            </properties>
                        
                            <modules>
                                <module>module1</module>
                                <module>module2</module>
                            </modules>
                        
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

        final String module1Pom = """
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <parent>
                        <artifactId>example-project-parent</artifactId>
                        <groupId>com.example</groupId>
                        <version>1.0.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module1</artifactId>
                    <modelVersion>4.0.0</modelVersion>
                
                    <properties>
                        <maven.compiler.source>11</maven.compiler.source>
                        <maven.compiler.target>11</maven.compiler.target>
                    </properties>
                
                    <dependencies>
                        <dependency>
                            <groupId>org.jetbrains</groupId>
                            <artifactId>annotations</artifactId>
                            <version>23.0.0</version>
                            <scope>test</scope>
                        </dependency>
                    </dependencies>
                
                </project>                
                """;

        final String module2Pom = """
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <parent>
                        <artifactId>example-project-parent</artifactId>
                        <groupId>com.example</groupId>
                        <version>1.0.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module2</artifactId>
                    <modelVersion>4.0.0</modelVersion>
                
                    <properties>
                        <maven.compiler.source>11</maven.compiler.source>
                        <maven.compiler.target>11</maven.compiler.target>
                    </properties>
                
                    <dependencies>
                        <dependency>
                            <groupId>org.openjfx</groupId>
                            <artifactId>javafx-swing</artifactId>
                            <version>11.0.2</version>
                        </dependency>
                    </dependencies>
                
                </project>
                """;

        MavenParser mavenParser = MavenParser.builder().build();
        Stream<SourceFile> parsedPomFilesStream = mavenParser.parse(parentPom, module1Pom, module2Pom);
        List<SourceFile> parsedPomFiles = parsedPomFilesStream.toList();
        MavenResolutionResult parentPomMarker = parsedPomFiles.get(0).getMarkers().findFirst(MavenResolutionResult.class).get();
        assertThat(parentPomMarker.getDependencies().get(Scope.Provided)).isEmpty();
        assertThat(parentPomMarker.getDependencies().get(Scope.Runtime)).isEmpty();
        assertThat(parentPomMarker.getDependencies().get(Scope.Compile)).isEmpty();
        assertThat(parentPomMarker.getDependencies().get(Scope.Test)).isEmpty();

        MavenResolutionResult module1PomMarker = parsedPomFiles.get(1).getMarkers().findFirst(MavenResolutionResult.class).get();
        assertThat(module1PomMarker.getDependencies().get(Scope.Provided)).isEmpty();
        assertThat(module1PomMarker.getDependencies().get(Scope.Runtime)).isEmpty();
        assertThat(module1PomMarker.getDependencies().get(Scope.Compile)).isEmpty();
        assertThat(module1PomMarker.getDependencies().get(Scope.Test)).isNotEmpty();
        assertThat(module1PomMarker.getDependencies().get(Scope.Test).get(0).getGav().toString()).isEqualTo("org.jetbrains:annotations:23.0.0");

        MavenResolutionResult module2PomMarker = parsedPomFiles.get(2).getMarkers().findFirst(MavenResolutionResult.class).get();
        // expected
//        assertThat(module2PomMarker.getDependencies().get(Scope.Provided)).hasSize(2);
//        assertThat(module2PomMarker.getDependencies().get(Scope.Runtime)).hasSize(2);
//        assertThat(module2PomMarker.getDependencies().get(Scope.Compile)).hasSize(2);
//        assertThat(module2PomMarker.getDependencies().get(Scope.Test)).hasSize(2);

        // actual
        assertThat(module2PomMarker.getDependencies().get(Scope.Provided)).hasSize(6);
        assertThat(module2PomMarker.getDependencies().get(Scope.Provided).get(0).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        assertThat(module2PomMarker.getDependencies().get(Scope.Provided).get(0).getDependencies()).hasSize(2);
        // "org.openjfx:javafx-swing:11.0.2" depends on "org.openjfx:javafx-swing:11.0.2" ?
        assertThat(module2PomMarker.getDependencies().get(Scope.Provided).get(0).getDependencies().get(0).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        assertThat(module2PomMarker.getDependencies().get(Scope.Provided).get(0).getDependencies().get(1).getGav().toString()).isEqualTo("org.openjfx:javafx-graphics:11.0.2");
        // "org.openjfx:javafx-swing:11.0.2" twice
        assertThat(module2PomMarker.getDependencies().get(Scope.Provided).get(1).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        // also without dependencies ?
        assertThat(module2PomMarker.getDependencies().get(Scope.Provided).get(1).getDependencies()).isEmpty();

        assertThat(module2PomMarker.getDependencies().get(Scope.Runtime)).hasSize(6);
        assertThat(module2PomMarker.getDependencies().get(Scope.Runtime).get(0).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        assertThat(module2PomMarker.getDependencies().get(Scope.Runtime).get(0).getDependencies()).hasSize(2);
        // "org.openjfx:javafx-swing:11.0.2" depends on "org.openjfx:javafx-swing:11.0.2" ?
        assertThat(module2PomMarker.getDependencies().get(Scope.Runtime).get(0).getDependencies().get(0).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        assertThat(module2PomMarker.getDependencies().get(Scope.Runtime).get(0).getDependencies().get(1).getGav().toString()).isEqualTo("org.openjfx:javafx-graphics:11.0.2");
        // "org.openjfx:javafx-swing:11.0.2" twice
        assertThat(module2PomMarker.getDependencies().get(Scope.Runtime).get(1).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        // also without dependencies ?
        assertThat(module2PomMarker.getDependencies().get(Scope.Runtime).get(1).getDependencies()).isEmpty();

        assertThat(module2PomMarker.getDependencies().get(Scope.Compile)).hasSize(6);
        assertThat(module2PomMarker.getDependencies().get(Scope.Compile).get(0).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        assertThat(module2PomMarker.getDependencies().get(Scope.Compile).get(0).getDependencies()).hasSize(2);
        // "org.openjfx:javafx-swing:11.0.2" depends on "org.openjfx:javafx-swing:11.0.2" ?
        assertThat(module2PomMarker.getDependencies().get(Scope.Compile).get(0).getDependencies().get(0).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        assertThat(module2PomMarker.getDependencies().get(Scope.Compile).get(0).getDependencies().get(1).getGav().toString()).isEqualTo("org.openjfx:javafx-graphics:11.0.2");
        // "org.openjfx:javafx-swing:11.0.2" twice
        assertThat(module2PomMarker.getDependencies().get(Scope.Compile).get(1).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        // also without dependencies ?
        assertThat(module2PomMarker.getDependencies().get(Scope.Compile).get(1).getDependencies()).isEmpty();

        assertThat(module2PomMarker.getDependencies().get(Scope.Test)).hasSize(6);
        assertThat(module2PomMarker.getDependencies().get(Scope.Test).get(0).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        assertThat(module2PomMarker.getDependencies().get(Scope.Test).get(0).getDependencies()).hasSize(2);
        // "org.openjfx:javafx-swing:11.0.2" depends on "org.openjfx:javafx-swing:11.0.2" ?
        assertThat(module2PomMarker.getDependencies().get(Scope.Test).get(0).getDependencies().get(0).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        assertThat(module2PomMarker.getDependencies().get(Scope.Test).get(0).getDependencies().get(1).getGav().toString()).isEqualTo("org.openjfx:javafx-graphics:11.0.2");
        // "org.openjfx:javafx-swing:11.0.2" twice
        assertThat(module2PomMarker.getDependencies().get(Scope.Test).get(1).getGav().toString()).isEqualTo("org.openjfx:javafx-swing:11.0.2");
        // also without dependencies ?
        assertThat(module2PomMarker.getDependencies().get(Scope.Test).get(1).getDependencies()).isEmpty();
    }

    @Test
    void newParsingShouldRefreshModel() {
        SourceFile document = MavenParser.builder().build().parse("""
                                                                            <?xml version="1.0" encoding="UTF-8"?>
                                                                            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                                                                    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                                                                                <modelVersion>4.0.0</modelVersion>
                                                                                <groupId>org.springframework.sbm.examples</groupId>
                                                                                <artifactId>artifact-id</artifactId>
                                                                                <packaging>jar</packaging>
                                                                                <version>0.0.1-SNAPSHOT</version>
                                                                            </project>
                                                                            """).toList().get(0);

        assertThat(document.getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getDependencyManagement()).isEmpty();

        SourceFile document1 = MavenParser.builder().build().parse("""
                                                                             <?xml version="1.0" encoding="UTF-8"?>
                                                                             <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                                                                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                                                                                 <modelVersion>4.0.0</modelVersion>
                                                                                 <groupId>org.springframework.sbm.examples</groupId>
                                                                                 <artifactId>artifact-id</artifactId>
                                                                                 <packaging>pom</packaging>
                                                                                 <version>0.0.1-SNAPSHOT</version>
                                                                                 <dependencyManagement>
                                                                                     <dependencies>
                                                                                         <dependency>
                                                                                             <groupId>groupId</groupId>
                                                                                             <artifactId>artifactId</artifactId>
                                                                                             <version>version</version>
                                                                                             <type>jar</type>
                                                                                             <scope>compile</scope>
                                                                                         </dependency>
                                                                                     </dependencies>
                                                                                 </dependencyManagement>
                                                                             </project>
                                                                             """).toList().get(0);

        assertThat(document1.getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getDependencyManagement()).isNotEmpty();
    }

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
        Stream<SourceFile> newMavenFiles = mavenParser.parseInputs(List.of(parserInput), null, new InMemoryExecutionContext((t) -> t.printStackTrace()));
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
        MavenParser mavenParser = MavenParser.builder()
                .build();

        ExecutionContext ctx = new InMemoryExecutionContext(t -> {
            throw new RuntimeException(t);
        });


        MavenExecutionContextView.view(ctx).setResolutionListener(new Listener());

        // parent can be parsed
        SourceFile parentPom = mavenParser.parse(ctx, parentPomXml).toList().get(0);
        Optional<MavenResolutionResult> mavenResolutionResult = parentPom.getMarkers().findFirst(MavenResolutionResult.class);
        assertThat(mavenResolutionResult).isPresent();

        // parent with module1 fails, but requires the listener to handle this case
        assertThatExceptionOfType(RewriteMavenDownloadingException.class)
                .isThrownBy(() -> mavenParser.parse(ctx, parentPomXml, module1PomXml))
                .describedAs("Maven visitors should not be visiting XML documents without a Maven marker");
    }

    @Test
    void parsePomFromTextWithoutMarkers() {
        SourceFile sut = MavenParser.builder().build().parse(
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
        ).findFirst().get();
        assertThat(sut).isNotNull();
    }


    @Problem(description = "java.io.UncheckedIOException: Failed to parse pom", since = "7.18.2", fixedIn = "7.23.0")
    void testParsingPomWithEmptyDependenciesSection() {
        @Language("xml")
        String pomXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>foo-bar</artifactId>
                    <version>0.1.0-SNAPSHOT</version>
                    <dependencies></dependencies>
                </project>
                """;

        List<SourceFile> parse = MavenParser.builder().build().parse(pomXml).toList();
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

        SourceFile document = MavenParser.builder().build().parse(pomXml).findFirst().get();
        MavenResolutionResult r = document.getMarkers().findFirst(MavenResolutionResult.class).get();

        InMemoryExecutionContext executionContext = new InMemoryExecutionContext((t) -> System.out.println(t.getMessage()));
        MavenExecutionContextView ctx = MavenExecutionContextView.view(executionContext);
        ctx.setPomCache(new InMemoryMavenPomCache());
        List<ResolvedDependency> resolvedDependencies = r.getDependencies().get(Scope.Provided);
        assertThat(r.getDependencies()).hasSize(4);
        assertThat(resolvedDependencies).hasSize(81); // FIXME: #7 was 81 before ?!
    }

    // FIXME: Exception Handling with
    private class Listener implements ResolutionEventListener {
            @Override
            public void clear() {

            }

            @Override
            public void downloadError(GroupArtifactVersion gav, Pom containing) {
                throw new RewriteMavenDownloadingException("Failed to download dependency: %s".formatted(gav.toString()), null, gav);
            }

            @Override
            public void parent(Pom parent, Pom containing) {

            }

            @Override
            public void dependency(Scope scope, ResolvedDependency resolvedDependency, ResolvedPom containing) {

            }

            @Override
            public void bomImport(ResolvedGroupArtifactVersion gav, Pom containing) {

            }

            @Override
            public void property(String key, String value, Pom containing) {

            }

            @Override
            public void dependencyManagement(ManagedDependency dependencyManagement, Pom containing) {

        }
    }
}
