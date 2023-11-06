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
package org.springframework.sbm.parsers;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junitpioneer.jupiter.Issue;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.shaded.jgit.api.errors.GitAPIException;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.springframework.sbm.parsers.maven.ComparingParserFactory;
import org.springframework.sbm.parsers.maven.RewriteMavenProjectParser;
import org.springframework.sbm.test.util.DummyResource;
import org.springframework.sbm.test.util.ParserParityTestHelper;
import org.springframework.sbm.test.util.TestProjectHelper;

import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;



/**
 * Test parity between OpenRewrite parser logic and RewriteProjectParser.
 *
 * RewriteMavenProjectParser resembles the parser logic from OpenRewrite's Maven plugin
 *
 * @author Fabian Krüger
 */
class RewriteProjectParserParityTest {

    @Test
    @DisplayName("Parsing Simplistic Maven Project ")
    void parsingSimplisticMavenProject(@TempDir Path tempDir) throws GitAPIException {
        @Language("xml")
        String pomXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.example</groupId>
                    <artifactId>root-project</artifactId>
                    <version>1.0.0</version>
                    <properties>
                        <maven.compiler.target>17</maven.compiler.target>
                        <maven.compiler.source>17</maven.compiler.source>
                        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                    </properties>
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
                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter</artifactId>
                            <version>3.1.1</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        @Language("java")
        String javaClass = """
                package com.example;
                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;
                                            
                @SpringBootApplication
                public class MyMain {
                    public static void main(String[] args){
                        SpringApplication.run(MyMain.class, args);
                    }
                }
                """;

        TestProjectHelper.createTestProject(tempDir)
                .withResources(
                    new DummyResource(tempDir.resolve("src/main/java/com/example/MyMain.java"), javaClass),
                    new DummyResource(tempDir.resolve("pom.xml"), pomXml)
                )
                .initializeGitRepo() // trigger creation of GIT related marker
                .writeToFilesystem();

        ParserProperties comparingParserProperties = new ParserProperties();
        Set<String> ignoredPathPatterns = Set.of("**/testcode/**", "testcode/**", ".rewrite-cache/**", "**/target/**", "**/.git/**");
        comparingParserProperties.setIgnoredPathPatterns(ignoredPathPatterns);
        comparingParserProperties.setPomCacheEnabled(true);

        ParserParityTestHelper
                .scanProjectDir(tempDir)
                .withParserProperties(comparingParserProperties)
                .verifyParity();
    }

    @NotNull
    private static InMemoryExecutionContext createExecutionContext() {
        return new InMemoryExecutionContext(t -> t.printStackTrace());
    }

    @Test
    @DisplayName("Parse multi-module-1")
    void parseMultiModule1() {
        Path baseDir = getMavenProject("multi-module-1");

        ParserParityTestHelper.scanProjectDir(baseDir)
                .verifyParity();
    }

    @Test
    @DisplayName("Should Parse Maven Config Project")
    @Disabled("https://github.com/openrewrite/rewrite/issues/3409")
    void shouldParseMavenConfigProject() {
        Path baseDir = Path.of("./testcode/maven-projects/maven-config").toAbsolutePath().normalize();
        ParserProperties parserProperties = new ParserProperties();
        parserProperties.setIgnoredPathPatterns(Set.of(".mvn"));
        RewriteMavenProjectParser mavenProjectParser = new ComparingParserFactory().createComparingParser();
        RewriteProjectParsingResult parsingResult = mavenProjectParser.parse(
                baseDir,
                new InMemoryExecutionContext(t -> fail(t.getMessage()))
        );
        assertThat(parsingResult.sourceFiles()).hasSize(2);
    }


    @Test
    @DisplayName("parseCheckstyle")
    @Issue("https://github.com/spring-projects-experimental/spring-boot-migrator/issues/875")
    void parseCheckstyle() {
        Path baseDir = getMavenProject("checkstyle");
        ParserParityTestHelper.scanProjectDir(baseDir)
                .parseSequentially()
                .verifyParity((comparingParsingResult, testedParsingResult) -> {
                    assertThat(comparingParsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/rules.xml");
                    assertThat(comparingParsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/suppressions.xml");
                    assertThat(testedParsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/rules.xml");
                    assertThat(testedParsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/suppressions.xml");
                });
    }

    @Test
    @DisplayName("Parse complex Maven reactor project")
    @Disabled("https://github.com/openrewrite/rewrite/issues/3409")
    void parseComplexMavenReactorProject() {
        Path projectRoot = Path.of("./testcode/maven-projects/cwa-server").toAbsolutePath().normalize();
        TestProjectHelper.createTestProject(projectRoot)
                .deleteDirIfExists()
                .cloneGitProject("https://github.com/corona-warn-app/cwa-server.git")
                .checkoutTag("v3.2.0")
                .writeToFilesystem();

        ParserProperties parserProperties = new ParserProperties();
        parserProperties.setIgnoredPathPatterns(Set.of(".rewrite/**", "internal/**"));

        List<String> parsedFiles = new ArrayList<>();
        ExecutionContext executionContext = createExecutionContext();
        ParsingExecutionContextView.view(executionContext).setParsingListener(new ParsingEventListener() {
            @Override
            public void parsed(Parser.Input input, SourceFile sourceFile) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                        .withLocale(Locale.US)
                        .withZone(ZoneId.systemDefault());
                String format = dateTimeFormatter.format(Instant.now());
                System.out.println("%s: Parsed file: %s".formatted(format, sourceFile.getSourcePath()));
                parsedFiles.add(sourceFile.getSourcePath().toString());
            }
        });

        ParserParityTestHelper.scanProjectDir(projectRoot)
                .parseSequentially()
                .withExecutionContextForComparingParser(executionContext)
                .withParserProperties(parserProperties)
                .verifyParity();
    }

    private Path getMavenProject(String s) {
        return Path.of("./testcode/maven-projects/").resolve(s).toAbsolutePath().normalize();
    }


    private enum ParserType {
        SBM, COMPARING
    }
}