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
package org.springframework.sbm.project.parser;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.openrewrite.java.tree.J;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.properties.tree.Properties;
import org.openrewrite.text.PlainText;
import org.openrewrite.xml.tree.Xml;
import org.openrewrite.yaml.tree.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.sbm.build.impl.MavenSettingsInitializer;
import org.springframework.sbm.build.impl.RewriteMavenArtifactDownloader;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.build.migration.MavenPomCacheProvider;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextFactory;
import org.springframework.sbm.engine.context.ProjectRootPathResolver;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.precondition.PreconditionVerifier;
import org.springframework.sbm.java.impl.RewriteJavaParser;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactoryImpl;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.*;
import org.springframework.sbm.properties.parser.RewritePropertiesParser;
import org.springframework.sbm.scopeplayground.ExecutionScope;
import org.springframework.sbm.scopeplayground.ProjectMetadata;
import org.springframework.sbm.scopeplayground.ScanScope;
import org.springframework.sbm.scopeplayground.ScopeConfiguration;
import org.springframework.sbm.xml.parser.RewriteXmlParser;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.sbm.project.parser.ResourceVerifierTestHelper.*;

@SpringBootTest(classes = {
        ProjectContextInitializer.class,
        RewriteMavenArtifactDownloader.class,
        JavaProvenanceMarkerFactory.class,
        BasePackageCalculator.class,
        BasePackageCalculator.class,
        ProjectRootPathResolver.class,
        PreconditionVerifier.class,
        ProjectContextFactory.class,
        MavenPomCacheProvider.class,
        SbmApplicationProperties.class,
        PathScanner.class,
        RewriteJavaParser.class,
        RewritePlainTextParser.class,
        RewriteYamlParser.class,
        RewriteJsonParser.class,
        ResourceParser.class,
        RewritePropertiesParser.class,
        MavenProjectParser.class,
        RewriteMavenParser.class,
        MavenSettingsInitializer.class,
        RewriteXmlParser.class,
        ResourceHelper.class,
        ResourceLoader.class,
        GitSupport.class,
        ScanCommand.class,
        ProjectResourceSetHolder.class,
        JavaRefactoringFactoryImpl.class,
        ProjectResourceWrapperRegistry.class,
        RewriteSourceFileWrapper.class,
        MavenConfigHandler.class,
        ScopeConfiguration.class,
        ScanScope.class,
        ExecutionScope.class
}, properties = {"sbm.gitSupportEnabled=false"})
class ProjectContextInitializerTest {

    public static final String VERSION_PATTERN = System.getProperty("java.version");
    public static final int NUM_TYPES = 1919;
    private final Path projectDirectory = Path.of("./testcode/path-scanner").toAbsolutePath().normalize();

    @Autowired
    private ProjectContextInitializer sut;

    @Autowired
    private ScanCommand scanCommand;

    @BeforeEach
    void beforeEach() throws IOException {
        Path repo = projectDirectory.toAbsolutePath().resolve(".git");
        FileSystemUtils.deleteRecursively(repo);
        GitSupport.initGit(repo.toFile());
    }

    @AfterEach
    void afterEach() throws IOException {
        FileSystemUtils.deleteRecursively(projectDirectory.toAbsolutePath().resolve(".git"));
    }

    // TODO: Proof of incorrect MavenResolutionResult for
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
        List<Xml.Document> parsedPomFiles = mavenParser.parse(parentPom, module1Pom, module2Pom);
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
    @Tag("integration")
    void test() {

        final String defaultBranchName = GitSupport.getBranchName(new File("./testcode/path-scanner")).get();

        List<Resource> resources = scanCommand.scanProjectRoot(projectDirectory.toString());
        ProjectContext projectContext = sut.initProjectContext(projectDirectory, resources);
        List<RewriteSourceFileHolder<? extends SourceFile>> projectResources = projectContext.getProjectResources().list();

        //assertThat(projectDirectory.toAbsolutePath().resolve(".git")).exists();

        assertThat(projectResources).hasSize(19);

        verifyResource("testcode/pom.xml").wrapsInstanceOf(Xml.Document.class);
        //verifyIgnored(projectResources, "testcode/path-scanner/.git");

        verifyResource("testcode/path-scanner/pom.xml")
                .wrapsInstanceOf(Xml.Document.class)
                .havingMarkers(
                        mavenResolutionResult(null, "com.example:example-project-parent:1.0.0-SNAPSHOT",
                                List.of(
                                        "com.example:module1:1.0.0-SNAPSHOT",
                                        "com.example:module2:1.0.0-SNAPSHOT"),
                                noDependencies()
                        ),
                        buildToolMarker("Maven", "3.6"), // TODO: does this work in all env (taken from .mvn)?
                        javaVersionMarker(VERSION_PATTERN, "17", "11"),
                        javaProjectMarker(null, "com.example:example-project-parent:1.0.0-SNAPSHOT"),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/pom.xml")
                .wrapsInstanceOf(Xml.Document.class)
                .havingMarkers(
                        mavenResolutionResult(
                                "com.example:example-project-parent:1.0.0-SNAPSHOT",
                                "com.example:module1:1.0.0-SNAPSHOT",
                                List.of(),
                                Map.of(
                                        Scope.Provided, List.of(),
                                        Scope.Compile, List.of(),
                                        Scope.Runtime, List.of(),
                                        Scope.Test, List.of("org.jetbrains:annotations:23.0.0")
                                )),
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/java/com/example/SomeJavaClass.java")
                .wrapsInstanceOf(J.CompilationUnit.class)
                .havingMarkers(
                        javaSourceSetMarker("main", ""),
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/resources/schema.sql")
                .wrapsInstanceOf(PlainText.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                 .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/resources/some.xml")
                .wrapsInstanceOf(Xml.Document.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/resources/some.yaml")
                .wrapsInstanceOf(Yaml.Documents.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/resources/some.properties")
                .wrapsInstanceOf(Properties.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/resources/some.html")
                .wrapsInstanceOf(PlainText.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/resources/some.jsp")
                .wrapsInstanceOf(PlainText.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/resources/some.txt")
                .wrapsInstanceOf(PlainText.class)
                .havingMarkers(buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/resources/some.xhtml")
                .wrapsInstanceOf(Xml.Document.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/resources/some.xsd")
                .wrapsInstanceOf(Xml.Document.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/webapp/META-INF/some.wsdl")
                .wrapsInstanceOf(Xml.Document.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/webapp/META-INF/some.xsl")
                .wrapsInstanceOf(Xml.Document.class)
                .havingMarkers(buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module1/src/main/webapp/META-INF/some.xslt")
                .wrapsInstanceOf(Xml.Document.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", NUM_TYPES),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        // module2
        verifyResource("testcode/path-scanner/module2/pom.xml")
                .wrapsInstanceOf(Xml.Document.class)
                .havingMarkers(
                        mavenResolutionResult(
                                "com.example:example-project-parent:1.0.0-SNAPSHOT",
                                "com.example:module2:1.0.0-SNAPSHOT",
                                List.of(),
                                Map.of(
                                        Scope.Provided, List.of(
                                                "org.openjfx:javafx-swing:11.0.2",
                                                "org.openjfx:javafx-swing:11.0.2",
                                                "org.openjfx:javafx-graphics:11.0.2",
                                                "org.openjfx:javafx-graphics:11.0.2",
                                                "org.openjfx:javafx-base:11.0.2",
                                                "org.openjfx:javafx-base:11.0.2"
                                        ),
                                        Scope.Compile, List.of(
                                                "org.openjfx:javafx-swing:11.0.2",
                                                "org.openjfx:javafx-swing:11.0.2",
                                                "org.openjfx:javafx-graphics:11.0.2",
                                                "org.openjfx:javafx-graphics:11.0.2",
                                                "org.openjfx:javafx-base:11.0.2",
                                                "org.openjfx:javafx-base:11.0.2"
                                        ),
                                        Scope.Runtime, List.of(
                                                "org.openjfx:javafx-swing:11.0.2",
                                                "org.openjfx:javafx-swing:11.0.2",
                                                "org.openjfx:javafx-graphics:11.0.2",
                                                "org.openjfx:javafx-graphics:11.0.2",
                                                "org.openjfx:javafx-base:11.0.2",
                                                "org.openjfx:javafx-base:11.0.2"
                                        ),
                                        Scope.Test, List.of(
                                                "org.openjfx:javafx-swing:11.0.2",
                                                "org.openjfx:javafx-swing:11.0.2",
                                                "org.openjfx:javafx-graphics:11.0.2",
                                                "org.openjfx:javafx-graphics:11.0.2",
                                                "org.openjfx:javafx-base:11.0.2",
                                                "org.openjfx:javafx-base:11.0.2"
                                        )
                                )
                        ),
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module2:1.0.0-SNAPSHOT"),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module2/src/test/java/com/example/FooTest.java")
                .wrapsInstanceOf(J.CompilationUnit.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module2:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", "java.awt.dnd.DragGestureRecognizer, java.nio.channels.ClosedByInterruptException, java.lang.management.ThreadMXBean"),
                        javaSourceSetMarker("test", "java.awt.dnd.DragGestureRecognizer, java.nio.channels.ClosedByInterruptException, java.lang.management.ThreadMXBean"),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/path-scanner/module2/src/test/resources/test.whatever")
                .wrapsInstanceOf(PlainText.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(VERSION_PATTERN, "11", "11"),
                        javaProjectMarker(null, "com.example:module2:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("test", ""),
                        gitProvenanceMarker(defaultBranchName)
                )
                .isContainedIn(projectResources);
    }

    private void verifyIgnored(List<RewriteSourceFileHolder<? extends SourceFile>> projectResources, String s) {
        assertThat(Path.of(s).toFile()).exists();
        assertThat(projectResources.stream().noneMatch(r -> s.equals(r.getAbsolutePath().toString()))).isTrue();
    }

    @NotNull
    private Map<? extends Scope, ? extends List<String>> noDependencies() {
        return Map.of(
                Scope.Compile, List.of(),
                Scope.Provided, List.of(),
                Scope.Test, List.of(),
                Scope.Runtime, List.of()
        );
    }
}



