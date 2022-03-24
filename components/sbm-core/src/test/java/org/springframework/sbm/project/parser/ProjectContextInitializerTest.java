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

import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.openrewrite.java.tree.J;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.properties.tree.Properties;
import org.openrewrite.text.PlainText;
import org.openrewrite.xml.tree.Xml;
import org.openrewrite.yaml.tree.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ResourceLoader;
import org.springframework.sbm.build.migration.MavenPomCacheProvider;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextFactory;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactoryImpl;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.*;
import org.springframework.sbm.xml.parser.RewriteXmlParser;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.sbm.project.parser.ResourceVerifierTestHelper.*;


@SpringBootTest(classes = {
        ProjectContextInitializer.class,
        BasePackageCalculator.class,
        ProjectContextFactory.class,
        RewriteMavenParserFactory.class,
        MavenPomCacheProvider.class,
        PathScanner.class,
        ApplicationProperties.class,
        RewriteXmlParser.class,
        ResourceHelper.class,
        ResourceLoader.class,
        GitSupport.class,
        ProjectResourceSetHolder.class,
        JavaRefactoringFactoryImpl.class,
        ProjectResourceWrapperRegistry.class
}, properties = {"sbm.gitSupportEnabled=false"})
class ProjectContextInitializerTest {

    private final Path projectDirectory = Path.of("./testcode").toAbsolutePath().normalize();

    @Autowired
    private ProjectContextInitializer sut;

    @BeforeEach
    void beforeEach() throws IOException {
        FileSystemUtils.deleteRecursively(projectDirectory.toAbsolutePath().resolve(".git"));
    }

    @AfterEach
    void afterEach() throws IOException {
        FileSystemUtils.deleteRecursively(projectDirectory.toAbsolutePath().resolve(".git"));
    }

    @Test
    @Tag("integration")
    void test() {

        assertThat(projectDirectory.toAbsolutePath().resolve(".git")).doesNotExist();

        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        RewriteExecutionContext executionContext = new RewriteExecutionContext(eventPublisher);
        ProjectContext projectContext = sut.initProjectContext(projectDirectory, executionContext);
        List<RewriteSourceFileHolder<? extends SourceFile>> projectResources = projectContext.getProjectResources().list();

        assertThat(projectDirectory.toAbsolutePath().resolve(".git")).exists();

        assertThat(projectResources).hasSize(18);

        verifyResource("testcode/pom.xml")
                .wrappedInstanceOf(Xml.Document.class)
                .havingMarkers(
                        mavenModelMarker("com.example:example-project-parent:1.0.0-SNAPSHOT",
                                List.of("com.example:module1:1.0.0-SNAPSHOT", "com.example:module2:1.0.0-SNAPSHOT"),
                                Map.of(
                                    Scope.Compile, List.of(),
                                    Scope.Provided, List.of(),
                                    Scope.Test, List.of(),
                                    Scope.Runtime, List.of()
                                )
                        ),
                        buildToolMarker("Maven", "3.6"), // TODO: does this work in all env (taken from .mvn)?
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:example-project-parent:1.0.0-SNAPSHOT"),
                        modulesMarker("com.example:module1:1.0.0-SNAPSHOT", "com.example:module2:1.0.0-SNAPSHOT"),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/pom.xml")
                .wrappedInstanceOf(Maven.class)
                .havingMarkers(
                        mavenModelMarker("com.example:module1:1.0.0-SNAPSHOT"),
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/java/com/example/SomeJavaClass.java")
                .wrappedInstanceOf(J.CompilationUnit.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/resources/schema.sql")
                .wrappedInstanceOf(PlainText.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/resources/some.xml")
                .wrappedInstanceOf(Xml.Document.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/resources/some.yaml")
                .wrappedInstanceOf(Yaml.Documents.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/resources/some.properties")
                .wrappedInstanceOf(Properties.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/resources/some.html")
                .wrappedInstanceOf(PlainText.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/resources/some.jsp")
                .wrappedInstanceOf(PlainText.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/resources/some.txt")
                .wrappedInstanceOf(PlainText.class)
                .havingMarkers(buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/resources/some.xhtml")
                .wrappedInstanceOf(Xml.Document.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/resources/some.xsd")
                .wrappedInstanceOf(Xml.Document.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/webapp/META-INF/some.wsdl")
                .wrappedInstanceOf(Xml.Document.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/webapp/META-INF/some.xsl")
                .wrappedInstanceOf(Xml.Document.class)
                .havingMarkers(buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module1/src/main/webapp/META-INF/some.xslt")
                .wrappedInstanceOf(Xml.Document.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module1:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("main", ""),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        // module2
        verifyResource("testcode/module2/pom.xml")
                .wrappedInstanceOf(Maven.class)
                .havingMarkers(
                        mavenModelMarker("com.example:module2:1.0.0-SNAPSHOT"),
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module2:1.0.0-SNAPSHOT"),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module2/src/test/java/com/example/FooTest.java")
                .wrappedInstanceOf(J.CompilationUnit.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module2:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("test", "java.awt.dnd.DragGestureRecognizer, java.nio.channels.ClosedByInterruptException, java.lang.management.ThreadMXBean"),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);

        verifyResource("testcode/module2/src/test/resources/test.whatever")
                .wrappedInstanceOf(PlainText.class)
                .havingMarkers(
                        buildToolMarker("Maven", "3.6"),
                        javaVersionMarker(11, "maven.compiler.source", "maven.compiler.target"),
                        javaProjectMarker(null, "com.example:module2:1.0.0-SNAPSHOT"),
                        javaSourceSetMarker("test", "java.awt.dnd.DragGestureRecognizer, java.nio.channels.ClosedByInterruptException, java.lang.management.ThreadMXBean"),
                        gitProvenanceMarker("master")
                )
                .isContainedIn(projectResources);
    }
}