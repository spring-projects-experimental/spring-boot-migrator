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
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junitpioneer.jupiter.Issue;
import org.mockito.Mockito;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
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
import org.openrewrite.maven.cache.*;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.cache.MavenArtifactCache;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.shaded.jgit.api.Git;
import org.openrewrite.shaded.jgit.api.errors.GitAPIException;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.openrewrite.xml.style.Autodetect;
import org.openrewrite.xml.tree.Xml;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.events.RewriteParsingEventListenerAdapter;
import org.springframework.sbm.scopes.ScanScope;
import org.springframework.sbm.test.util.DummyResource;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.mock;

/**
 * @author Fabian Krüger
 */
class RewriteMavenProjectParserTest {


    MavenExecutionRequestFactory requestFactory = new MavenExecutionRequestFactory(
            new MavenConfigFileParser()
    );
    MavenPlexusContainer plexusContainer = new MavenPlexusContainer();
    private ParserProperties parserProperties = new ParserProperties();
    private RewriteMavenProjectParser sut;
    private ConfigurableListableBeanFactory beanFactory;
    private ScanScope scanScope;

    @BeforeEach
    void beforeEach() {
        beanFactory = mock(ConfigurableListableBeanFactory.class);
        scanScope = mock(ScanScope.class);
        sut = new RewriteMavenProjectParser(
                plexusContainer,
                new RewriteParsingEventListenerAdapter(mock(ApplicationEventPublisher.class)),
                new MavenExecutor(requestFactory, plexusContainer),
                new MavenMojoProjectParserFactory(parserProperties),
                scanScope,
                beanFactory,
                new InMemoryExecutionContext(t -> {throw new RuntimeException(t);})
        );
    }

    @Test
    @DisplayName("Parsing Simplistic Maven Project ")
    void parsingSimplisticMavenProject(@TempDir Path tempDir) throws GitAPIException {
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

        // init GIT repo to trigger adding of GitProvenance marker
        Git.init().setDirectory(tempDir.toFile()).call();
        List<Resource> resources = List.of(
                new DummyResource(tempDir.resolve("src/main/java/com/example/MyMain.java"), javaClass),
                new DummyResource(tempDir.resolve("pom.xml"), pomXml)
        );
        ResourceUtil.write(tempDir, resources);

        parserProperties.setIgnoredPathPatterns(Set.of("**/testcode/**", "testcode/**", ".rewrite-cache/**"));
        parserProperties.setPomCacheEnabled(true); // org.openrewrite.maven.pomCache will be CompositeMavenPomCache

        // call SUT
        RewriteProjectParsingResult parsingResult = sut.parse(
                tempDir,
                new InMemoryExecutionContext(t -> t.printStackTrace())
        );

        // Verify result
        List<SourceFile> sourceFiles = parsingResult.sourceFiles();
        assertThat(sourceFiles).isNotEmpty();
        assertThat(sourceFiles).hasSize(2);
        SourceFile pom = sourceFiles.get(0);
        assertThat(pom).isInstanceOf(Xml.Document.class);
        int expectedNumMarkers = 7;
        if(System.getenv("GITHUB_ACTION_REF") != null) {
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

        verifyExecutionContext(parsingResult);

        Mockito.verify(scanScope).clear(beanFactory);

        // TODO: Add test that uses Maven settings and encrypted passwords
    }

    @Test
    @DisplayName("Should Parse Maven Config Project")
    @Disabled("https://github.com/openrewrite/rewrite/issues/3409")
    void shouldParseMavenConfigProject() {
        Path baseDir = Path.of("./testcode/maven-projects/maven-config").toAbsolutePath().normalize();
        parserProperties.setIgnoredPathPatterns(Set.of(".mvn"));
        RewriteProjectParsingResult parsingResult = sut.parse(
                baseDir,
                new InMemoryExecutionContext(t -> fail(t.getMessage()))
        );
        assertThat(parsingResult.sourceFiles()).hasSize(2);
    }

    @Test
    @DisplayName("Parse multi-module-1")
    void parseMultiModule1_withIntegratedParser() {
        ExecutionContext ctx = new InMemoryExecutionContext(t -> t.printStackTrace());
        Path baseDir = getMavenProject("multi-module-1");
        parserProperties.setIgnoredPathPatterns(Set.of("README.adoc"));
        RewriteProjectParsingResult parsingResult = sut.parse(
                baseDir,
                ctx);
        verifyMavenParser(parsingResult);
    }

    @Test
    void parseMultiModule1_WithCustomParser() {
        Path baseDir = getMavenProject("multi-module-1");
        ExecutionContext ctx;
        ctx = new InMemoryExecutionContext(t -> t.printStackTrace());
        MavenMojoProjectParserFactory mavenMojoProjectParserFactory = new MavenMojoProjectParserFactory(parserProperties);
        MavenArtifactCache mavenArtifactCache = new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".m2", "repository"));
        @Nullable MavenSettings mavenSettings = null;
        Consumer<Throwable> onError = (t) -> {throw new RuntimeException(t);};
        ModuleParser moduleParser = new ModuleParser();

        JavaParserBuilder javaParserBuilder = new JavaParserBuilder();
        RewriteProjectParser rpp = new RewriteProjectParser(
                new ProvenanceMarkerFactory(new MavenProvenanceMarkerFactory()),
                new BuildFileParser(),
                new SourceFileParser(parserProperties, moduleParser),
                new StyleDetector(),
                parserProperties,
                mock(ParsingEventListener.class),
                mock(ApplicationEventPublisher.class),
                scanScope,
                beanFactory,
                new ProjectScanner(new DefaultResourceLoader(), parserProperties),
                ctx,
                new MavenProjectAnalyzer(mock(RewriteMavenArtifactDownloader.class))
        );

        Set<String> ignoredPatters = Set.of();
        ProjectScanner projectScanner = new ProjectScanner(new FileSystemResourceLoader(), parserProperties);
        List<Resource> resources = projectScanner.scan(baseDir);
        RewriteProjectParsingResult parsingResult1 = rpp.parse(baseDir, resources, ctx);

        verifyMavenParser(parsingResult1);
        Mockito.verify(scanScope).clear(beanFactory);
    }

    @Test
    @DisplayName("Parse complex Maven reactor project")
    @Disabled("https://github.com/openrewrite/rewrite/issues/3409")
    void parseComplexMavenReactorProject() {
        String target = "./testcode/maven-projects/cwa-server";
        cloneProject("https://github.com/corona-warn-app/cwa-server.git", target, "v3.2.0");
        Path projectRoot = Path.of(target).toAbsolutePath().normalize(); // SBM root
        RewriteMavenProjectParser projectParser = sut;
        ExecutionContext executionContext = new InMemoryExecutionContext(t -> t.printStackTrace());
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

        parserProperties.setIgnoredPathPatterns(Set.of("**/testcode/**", ".rewrite/**", "internal/**"));
        RewriteProjectParsingResult parsingResult = projectParser.parse(
                projectRoot,
                executionContext
        );

        parsingResult.sourceFiles().stream()
                .map(SourceFile::getSourcePath)
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("parseCheckstyle")
    @Issue("https://github.com/spring-projects-experimental/spring-boot-migrator/issues/875")
    void parseCheckstyle() {
        Path baseDir = getMavenProject("checkstyle");
        RewriteProjectParsingResult parsingResult = sut.parse(baseDir);
        assertThat(parsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/rules.xml");
        assertThat(parsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/suppressions.xml");
    }

    private static void verifyExecutionContext(RewriteProjectParsingResult parsingResult) {
        ExecutionContext resultingExecutionContext = parsingResult.executionContext();
        assertThat(resultingExecutionContext).isNotNull();

        Map<String, Object> messages = (Map<String, Object>) ReflectionTestUtils.getField(resultingExecutionContext, "messages");
//        assertThat(messages).hasSize(10);

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
                "file://" + Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository").toAbsolutePath().normalize().toString() + "/"
        );

        // 8
        assertThat(
                messages.get("org.openrewrite.maven.pomCache")
        ).isInstanceOf(CompositeMavenPomCache.class);
        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getPomCache()).isInstanceOf(CompositeMavenPomCache.class);
//        assertThat(MavenExecutionContextView.view(resultingExecutionContext).getPomCache()).isInstanceOf(CompositeMavenPomCache.class);

        // 9
        // FIXME:   This fails sometimes when multiple tests are run together. The resolution time has been 0 and null
        //          The test succeeds with a useful resolution time when the test runs in isolation.
        //          The property is set by MavenArtifactDownloader (I think) and an unresolvable pom might ne the reason.
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

    private void cloneProject(String url, String target, String tag) {
        File directory = Path.of(target).toFile();
        if (directory.exists()) {
            return;
        }
        try {
            Git git = Git.cloneRepository()
                    .setDirectory(directory)
                    .setURI(url)
                    .call();

            git.checkout()
                    .setName("refs/tags/" + tag)
                    .call();

        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
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
            if(System.getenv("GITHUB_ACTION_REF") != null) {
                numExpectedMarkers = 8;
            }
            assertThat(pom.getMarkers().getMarkers())
                    .as(() -> pom.getMarkers().getMarkers().stream().map(m -> m.getClass().getName()).collect(Collectors.joining("\n")))
                    .hasSize(numExpectedMarkers);

            assertThat(pom.getMarkers().findFirst(MavenResolutionResult.class)).isPresent();
            if(System.getenv("GITHUB_ACTION_REF") != null) {
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

}