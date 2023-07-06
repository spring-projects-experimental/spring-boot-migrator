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
package org.springframework.sbm.parsers;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaParserExecutionContextView;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.marker.BuildTool;
import org.openrewrite.marker.GitProvenance;
import org.openrewrite.marker.OperatingSystemProvenance;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.shaded.jgit.api.Git;
import org.openrewrite.shaded.jgit.api.errors.GitAPIException;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.openrewrite.xml.style.Autodetect;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

/**
 * @author Fabian Krüger
 */
class RewriteMavenProjectParserTest {

    @Test
    @DisplayName("Parsing Simplistic Maven Project ")
    void parsingSimplisticMavenProject(@TempDir Path tempDir) throws IOException, GitAPIException {
        @Language("xml")
        String pomXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.example</groupId>
                    <artifactId>root-project</artifactId>
                    <version>1.0.0</version>
                    <properties>
                        <maven.compiler.target>17</maven.compiler.target>
                        <maven.compiler.source>17</maven.compiler.source>
                        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                    </properties>
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

        // init GIT repo to trigger adding of GitProvenance marker
        Git.init().setDirectory(tempDir.toFile()).call();

        // Init Maven project as text fixture
        Path pathToFile = tempDir.resolve("src/main/java/com/example/MyMain.java");
        Files.createDirectories(pathToFile.getParent());
        Files.createFile(pathToFile);
        Files.writeString(pathToFile, javaClass);
        Files.writeString(tempDir.resolve("pom.xml"), pomXml);

        RewriteMavenProjectParser sut = new RewriteMavenProjectParser();

        // call SUT
        RewriteProjectParsingResult parsingResult = sut.parse(
                tempDir,
                true,
                tempDir.toString(),
                false,
                Set.of("**/testcode/**", "testcode/**", ".rewrite-cache/**"),
                Set.of(),
                -1,
                false,
                new InMemoryExecutionContext(t -> t.printStackTrace()));

        // Verify result
        List<SourceFile> sourceFiles = parsingResult.sourceFiles();
        assertThat(sourceFiles).isNotEmpty();
        assertThat(sourceFiles).hasSize(2);
        SourceFile pom = sourceFiles.get(0);
        assertThat(pom).isInstanceOf(Xml.Document.class);
        assertThat(pom.getMarkers().getMarkers()).hasSize(3);
        assertThat(pom.getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequested().getDependencies()).hasSize(1);
        assertThat(pom.getMarkers().findFirst(JavaProject.class)).isNotNull();
        assertThat(pom.getMarkers().findFirst(Autodetect.class)).isNotNull();

        assertThat(sourceFiles.get(1)).isInstanceOf(J.CompilationUnit.class);
        J.CompilationUnit compilationUnit = J.CompilationUnit.class.cast(sourceFiles.get(1));
        assertThat(compilationUnit.getMarkers().getMarkers()).hasSize(7);
        assertThat(compilationUnit.getMarkers().findFirst(GitProvenance.class)).isNotNull();
        assertThat(compilationUnit.getMarkers().findFirst(OperatingSystemProvenance.class)).isNotNull();
        assertThat(compilationUnit.getMarkers().findFirst(BuildTool.class)).isNotNull();
        assertThat(compilationUnit.getMarkers().findFirst(JavaVersion.class)).isNotNull();
        assertThat(compilationUnit.getMarkers().findFirst(JavaProject.class)).isNotNull();
        assertThat(compilationUnit.getMarkers().findFirst(JavaSourceSet.class)).isNotNull();
        assertThat(compilationUnit.getMarkers().findFirst(Autodetect.class)).isNotNull();
        List<JavaType> typeInUse = new ArrayList<>();
        typeInUse.addAll(compilationUnit.getTypesInUse().getTypesInUse());
        assertThat(typeInUse).hasSize(7);
        List<String> fqnTypesInUse = typeInUse.stream()
                .filter(JavaType.class::isInstance)
                .map(JavaType.class::cast)
                .map(JavaType::toString)
                .toList();
        assertThat(fqnTypesInUse).contains("org.springframework.boot.autoconfigure.SpringBootApplication");
        assertThat(fqnTypesInUse).contains("void");
        assertThat(fqnTypesInUse).contains("java.lang.String");
        assertThat(fqnTypesInUse).contains("java.lang.Class<com.example.MyMain>");
        assertThat(fqnTypesInUse).contains("org.springframework.boot.SpringApplication");
        assertThat(fqnTypesInUse).contains("com.example.MyMain");
        assertThat(fqnTypesInUse).contains("org.springframework.boot.autoconfigure.SpringBootApplication");

        List<String> classpath = compilationUnit.getMarkers().findFirst(JavaSourceSet.class).get().getClasspath()
                .stream()
                .map(JavaType.FullyQualified::getFullyQualifiedName)
                .toList();

        // Classpath contains classes from JDK and spring-boot-starter and transitive dependencies, currently 6710
        assertThat(classpath).hasSize(6859);

        ExecutionContext resultingExecutionContext = parsingResult.executionContext();
        assertThat(resultingExecutionContext).isNotNull();
        Map<String, Object> messages = (Map<String, Object>) ReflectionTestUtils.getField(resultingExecutionContext, "messages");
        assertThat(messages).hasSize(8);
        MavenExecutionContextView contextView = MavenExecutionContextView.view(resultingExecutionContext);
        assertThat(contextView.getSettings()).isNotNull();  // TODO: verify settings
        assertThat(ParsingExecutionContextView.view(resultingExecutionContext).getCharset()).isEqualTo(Charset.forName("UTF-8"));

        // TODO: Add test that uses Maven settings and encrypted passwords
    }
    
    @Test
    @DisplayName("Parse complex Maven reactor project")
    void parseComplexMavenReactorProject() {
        Path projectRoot = Path.of("./..").toAbsolutePath().normalize(); // SBM root
        RewriteMavenProjectParser projectParser = new RewriteMavenProjectParser();
        ExecutionContext executionContext = new InMemoryExecutionContext(t -> t.printStackTrace());
        List<String> parsedFiles = new ArrayList<>();
        ParsingExecutionContextView.view(executionContext).setParsingListener((Parser.Input input, SourceFile sourceFile) -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.US)
                    .withZone(ZoneId.systemDefault());
            String format = dateTimeFormatter.format(Instant.now());
            System.out.println("%s: Parsed file: %s".formatted(format, sourceFile.getSourcePath()));
            parsedFiles.add(sourceFile.getSourcePath().toString());
        });
        RewriteProjectParsingResult parsingResult = projectParser.parse(projectRoot, true, "pomCache", false, List.of("**/testcode/**", ".rewrite/**", "internal/**"), List.of("*.txt"), -1, false, executionContext);

        parsingResult.sourceFiles().stream()
                .map(SourceFile::getSourcePath)
                .forEach(System.out::println);
    }

}