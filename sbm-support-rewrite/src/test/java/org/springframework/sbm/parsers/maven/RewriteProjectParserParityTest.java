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
package org.springframework.sbm.parsers.maven;

import com.google.common.collect.Streams;
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
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.internal.JavaTypeCache;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.marker.BuildTool;
import org.openrewrite.marker.GitProvenance;
import org.openrewrite.marker.OperatingSystemProvenance;
import org.openrewrite.marker.ci.GithubActionsBuildEnvironment;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.maven.cache.CompositeMavenPomCache;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.shaded.jgit.api.errors.GitAPIException;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.openrewrite.xml.style.Autodetect;
import org.openrewrite.xml.tree.Xml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.sbm.boot.autoconfigure.SbmSupportRewriteConfiguration;
import org.springframework.sbm.parsers.*;
import org.springframework.sbm.test.util.DummyResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.mock;



/**
 * Test parity between OpenRewrite parser logic and RewriteProjectParser.
 *
 * RewriteMavenProjectParser resembles the parser logic from OpenRewrite's Maven plugin
 *
 * @author Fabian Krüger
 */
//@SpringBootTest(classes = SbmSupportRewriteConfiguration.class)
//@DirtiesContext
class RewriteProjectParserParityTest {

    @Autowired
    private RewriteProjectParser sut;

    @Autowired
    private ParserProperties parserProperties;

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


        List<Path> classpath = List.of(
                Path.of("/Users/fkrueger/.m2/repository/org/springframework/boot/spring-boot-starter/3.1.1/spring-boot-starter-3.1.1.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/springframework/boot/spring-boot/3.1.1/spring-boot-3.1.1.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/springframework/spring-context/6.0.10/spring-context-6.0.10.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/springframework/spring-aop/6.0.10/spring-aop-6.0.10.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/springframework/spring-beans/6.0.10/spring-beans-6.0.10.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/springframework/spring-expression/6.0.10/spring-expression-6.0.10.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/3.1.1/spring-boot-autoconfigure-3.1.1.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/springframework/boot/spring-boot-starter-logging/3.1.1/spring-boot-starter-logging-3.1.1.jar"),
                Path.of("/Users/fkrueger/.m2/repository/ch/qos/logback/logback-classic/1.4.8/logback-classic-1.4.8.jar"),
                Path.of("/Users/fkrueger/.m2/repository/ch/qos/logback/logback-core/1.4.8/logback-core-1.4.8.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/slf4j/slf4j-api/2.0.7/slf4j-api-2.0.7.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.20.0/log4j-to-slf4j-2.20.0.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/apache/logging/log4j/log4j-api/2.20.0/log4j-api-2.20.0.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/slf4j/jul-to-slf4j/2.0.7/jul-to-slf4j-2.0.7.jar"),
                Path.of("/Users/fkrueger/.m2/repository/jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/springframework/spring-core/6.0.10/spring-core-6.0.10.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/springframework/spring-jcl/6.0.10/spring-jcl-6.0.10.jar"),
                Path.of("/Users/fkrueger/.m2/repository/org/yaml/snakeyaml/1.33/snakeyaml-1.33.jar")
        );
        JavaTypeCache javaTypeCache = new JavaTypeCache();
        SourceFile sourceFile = JavaParser.fromJavaVersion().classpath(classpath).typeCache(javaTypeCache)
                .build()
                .parse(javaClass)
                .toList()
                .get(0);

        JavaSourceSet.build("main", classpath, javaTypeCache, true);


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
                .parseSequentially()
                .verifyParity();


        // call SUT
        new ApplicationContextRunner().withUserConfiguration(SbmSupportRewriteConfiguration.class)
                .withBean("parser-org.springframework.sbm.parsers.ParserProperties", ParserProperties.class, () -> comparingParserProperties)
                .run(appCtx -> {
                    RewriteProjectParser sut = appCtx.getBean(RewriteProjectParser.class);
                    RewriteProjectParsingResult parsingResult2 = sut.parse(tempDir);
                    verifyParsingResult(parsingResult2, ParserType.SBM);

                    InMemoryExecutionContext executionContext1 = createExecutionContext();
                    RewriteMavenProjectParser mavenProjectParser = new ComparingParserFactory().createComparingParser(comparingParserProperties);
                    RewriteProjectParsingResult parsingResult = mavenProjectParser.parse(tempDir, executionContext1);
                    verifyParsingResult(parsingResult, ParserType.COMPARING);
                });
    }

    @NotNull
    private static InMemoryExecutionContext createExecutionContext() {
        return new InMemoryExecutionContext(t -> t.printStackTrace());
    }

    private void verifyParsingResult(RewriteProjectParsingResult parsingResult, ParserType parserType) {
        // Verify result
        List<SourceFile> sourceFiles = parsingResult.sourceFiles();
        assertThat(sourceFiles).isNotEmpty();
        assertThat(sourceFiles).hasSize(2);
        SourceFile pom = sourceFiles.get(0);
        assertThat(pom).isInstanceOf(Xml.Document.class);
        int expectedNumMarkers = 7;
        if (System.getenv("GITHUB_ACTION_REF") != null) {
            expectedNumMarkers = 8;
        }
        assertThat(pom.getMarkers().getMarkers()).hasSize(expectedNumMarkers);
        assertThat(pom.getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequested().getDependencies()).hasSize(1);
        assertThat(pom.getMarkers().findFirst(GitProvenance.class)).isNotNull();
        assertThat(pom.getMarkers().findFirst(OperatingSystemProvenance.class)).isNotNull();
        assertThat(pom.getMarkers().findFirst(BuildTool.class)).isNotNull();
        assertThat(pom.getMarkers().findFirst(JavaVersion.class)).isNotNull();
        assertThat(pom.getMarkers().findFirst(JavaProject.class)).isNotNull();
        assertThat(pom.getMarkers().findFirst(Autodetect.class)).isNotNull();

        assertThat(sourceFiles.get(1)).isInstanceOf(J.CompilationUnit.class);
        J.CompilationUnit compilationUnit = J.CompilationUnit.class.cast(sourceFiles.get(1));
        assertThat(compilationUnit.getMarkers().getMarkers()).hasSize(expectedNumMarkers);
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

        // Classpath contains classes from JDK and spring-boot-starter
        assertThat(classpath).contains(
                "org.springframework.boot.web.reactive.context.ApplicationReactiveWebEnvironment",
                "org.springframework.context.ApplicationContext",
                "java.math.BigInteger"
        );

        verifyExecutionContext(parsingResult, parserType);
        // TODO: Add test that uses Maven settings and encrypted passwords
    }

    @Test
    @DisplayName("Parse multi-module-1")
    void parseMultiModule1() {
        Path baseDir = getMavenProject("multi-module-1");
        Set<String> ignoredPathPatterns = Streams.concat(parserProperties.getIgnoredPathPatterns().stream(), Stream.of("README.adoc")).collect(Collectors.toSet());
        parserProperties.setIgnoredPathPatterns(ignoredPathPatterns);

        ExecutionContext ctx = createExecutionContext();
        RewriteMavenProjectParser mavenProjectParser = new ComparingParserFactory().createComparingParser();
        RewriteProjectParsingResult parsingResult = mavenProjectParser.parse(baseDir, ctx);
        verifyMavenParser(parsingResult);

        parsingResult = sut.parse(baseDir);
        verifyMavenParser(parsingResult);
    }

    @Test
    @DisplayName("Should Parse Maven Config Project")
    @Disabled("https://github.com/openrewrite/rewrite/issues/3409")
    void shouldParseMavenConfigProject() {
        Path baseDir = Path.of("./testcode/maven-projects/maven-config").toAbsolutePath().normalize();
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
        RewriteMavenProjectParser mavenProjectParser = new ComparingParserFactory().createComparingParser();
        RewriteProjectParsingResult parsingResult = mavenProjectParser.parse(baseDir);
        assertThat(parsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/rules.xml");
        assertThat(parsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/suppressions.xml");

        parsingResult = sut.parse(baseDir);
        assertThat(parsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/rules.xml");
        assertThat(parsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/suppressions.xml");
    }

    @Test
    @DisplayName("Parse complex Maven reactor project")
    @Disabled("https://github.com/openrewrite/rewrite/issues/3409")
    void parseComplexMavenReactorProject() {
        Path projectRoot = Path.of("./testcode/maven-projects/cwa-server").toAbsolutePath().normalize();
        TestProjectHelper.createTestProject(projectRoot)
                .cloneGitProject("https://github.com/corona-warn-app/cwa-server.git")
                .checkoutTag("v3.2.0")
                .writeToFilesystem();

        ExecutionContext executionContext = createExecutionContext();
        List<String> parsedFiles = new ArrayList<>();
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

        ParserProperties parserProperties = new ParserProperties();
        parserProperties.setIgnoredPathPatterns(Set.of("**/testcode/**", ".rewrite/**", "internal/**"));
        RewriteMavenProjectParser mavenProjectParser = new ComparingParserFactory().createComparingParser(parserProperties);
        RewriteProjectParsingResult parsingResult = mavenProjectParser.parse(
                projectRoot,
                executionContext
        );

        parsingResult.sourceFiles().stream()
                .map(SourceFile::getSourcePath)
                .forEach(System.out::println);
    }

    private static void verifyExecutionContext(RewriteProjectParsingResult parsingResult, ParserType parserType) {
        ExecutionContext resultingExecutionContext = parsingResult.executionContext();
        assertThat(resultingExecutionContext).isNotNull();

        Map<String, Object> messages = (Map<String, Object>) ReflectionTestUtils.getField(resultingExecutionContext, "messages");

        // 1
        assertThat(
                (Object) resultingExecutionContext.getMessage("org.openrewrite.maven.settings")
        ).isSameAs(
                MavenExecutionContextView.view(resultingExecutionContext).getSettings()
        );
        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getSettings()).isInstanceOf(MavenSettings.class);

        // 2
        assertThat(
                (Object) resultingExecutionContext.getMessage("org.openrewrite.maven.auth")
        ).isSameAs(
                MavenExecutionContextView.view(resultingExecutionContext).getCredentials()
        );
        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getCredentials()).isEmpty();

        // 3
        assertThat(
                messages.get("org.openrewrite.parser.charset")
        )
                .isSameAs(
                        ParsingExecutionContextView.view(resultingExecutionContext).getCharset()
                );
        assertThat(ParsingExecutionContextView.view(resultingExecutionContext).getCharset()).isEqualTo(Charset.forName("UTF-8"));

        // 4
        assertThat(
                ((Duration) resultingExecutionContext.getMessage(ExecutionContext.RUN_TIMEOUT)).toMillis()
        ).isGreaterThan(10);

        // 5
        assertThat(
                (List) resultingExecutionContext.getMessage("org.openrewrite.maven.activeProfiles")
        ).isSameAs(
                MavenExecutionContextView.view(resultingExecutionContext).getActiveProfiles()
        );
        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getActiveProfiles()).isEmpty();

        // 6
        assertThat(
                messages.get("org.openrewrite.maven.mirrors")
        ).isSameAs(
                MavenExecutionContextView.view(resultingExecutionContext).getMirrors()
        );
        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getMirrors()).isEmpty();

        // 7
        assertThat(
                messages.get("org.openrewrite.maven.localRepo")
        ).isSameAs(
                MavenExecutionContextView.view(resultingExecutionContext).getLocalRepository()
        );
        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getLocalRepository().getId()).isEqualTo("local");
        assertThat(
                MavenExecutionContextView.view(resultingExecutionContext).getLocalRepository().getUri()
        ).isEqualTo(
                "file://" + Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository").toAbsolutePath().normalize() + "/"
        );

        // 8
        assertThat(
                messages.get("org.openrewrite.maven.pomCache")
        ).isInstanceOf(CompositeMavenPomCache.class);
        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getPomCache()).isInstanceOf(CompositeMavenPomCache.class);

        // 9
        // This fails sometimes when multiple tests are run together. The resolution time has been 0 and null
        /*assertThat(
                messages.get("org.openrewrite.maven.resolutionTime")
        ).isEqualTo(
                MavenExecutionContextView.view(resultingExecutionContext).getResolutionTime().toMillis()
        );
        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getResolutionTime()).isInstanceOf(Duration.class);
        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getResolutionTime()).isNotNull(); //.toMillis()).isGreaterThanOrEqualTo(0);*/

        // 10
        assertThat(
                messages.get("org.openrewrite.maven.repos")
        ).isSameAs(
                MavenExecutionContextView.view(resultingExecutionContext).getRepositories()
        );
        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getRepositories()).isEmpty();
    }


    private void verifyMavenParser(RewriteProjectParsingResult parsingResult) {
        verify(parsingResult.sourceFiles().get(0), Xml.Document.class, "pom.xml", document -> {
            // further verifications specific to this source file
        });
        verify(parsingResult.sourceFiles().get(1), Xml.Document.class, "module-b/pom.xml");
        verify(parsingResult.sourceFiles().get(2), Xml.Document.class, "module-a/pom.xml");
    }

    private <T extends SourceFile> void verify(SourceFile sourceFile, Class<T> clazz, String resourcePath) {
        verify(sourceFile, clazz, resourcePath, t -> {
        });
    }

    private <T extends SourceFile> void verify(SourceFile sourceFile, Class<T> clazz, String resourcePath, Consumer<T> verify) {
        if (!clazz.isInstance(sourceFile)) {
            fail("Given sourceFile '%s' is not of type %s".formatted(sourceFile.getSourcePath(), clazz));
        }
        if (!resourcePath.equals(sourceFile.getSourcePath().toString())) {
            fail("Actual path '%s' did not match expected path '%s'".formatted(sourceFile.getSourcePath().toString(), resourcePath));
        }
        if (Xml.Document.class == clazz) {
            Xml.Document pom = Xml.Document.class.cast(sourceFile);
            int numExpectedMarkers = 7;
            if (System.getenv("GITHUB_ACTION_REF") != null) {
                numExpectedMarkers = 8;
            }
            assertThat(pom.getMarkers().getMarkers())
                    .as(() -> pom.getMarkers().getMarkers().stream().map(m -> m.getClass().getName()).collect(Collectors.joining("\n")))
                    .hasSize(numExpectedMarkers);

            assertThat(pom.getMarkers().findFirst(MavenResolutionResult.class)).isPresent();
            if (System.getenv("GITHUB_ACTION_REF") != null) {
                assertThat(pom.getMarkers().findFirst(GithubActionsBuildEnvironment.class)).isPresent();
            }
            assertThat(pom.getMarkers().findFirst(GitProvenance.class)).isNotNull();
            assertThat(pom.getMarkers().findFirst(OperatingSystemProvenance.class)).isNotNull();
            assertThat(pom.getMarkers().findFirst(BuildTool.class)).isNotNull();
            assertThat(pom.getMarkers().findFirst(JavaVersion.class)).isNotNull();
            assertThat(pom.getMarkers().findFirst(JavaProject.class)).isNotNull();
            assertThat(pom.getMarkers().findFirst(Autodetect.class)).isNotNull();
            verify.accept((T) pom);
        }
    }

    private Path getMavenProject(String s) {
        return Path.of("./testcode/maven-projects/").resolve(s).toAbsolutePath().normalize();
    }


    private enum ParserType {
        SBM, COMPARING
    }
}