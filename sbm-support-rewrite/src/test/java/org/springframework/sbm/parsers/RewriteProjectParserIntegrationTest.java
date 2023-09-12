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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.java.tree.J;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.sbm.boot.autoconfigure.ScannerConfiguration;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@SpringBootTest(classes = {ScannerConfiguration.class})
public class RewriteProjectParserIntegrationTest {

    @Autowired
    RewriteProjectParser sut;

    @Autowired
    ProjectScanner projectScanner;

    @Autowired
    ParserSettings parserSettings;

    private static List<ParsedResourceEvent> capturedEvents = new ArrayList<>();
    private static StartedParsingProjectEvent startedParsingEvent;
    private static FinishedParsingProjectEvent finishedParsingEvent;

    @Test
    @DisplayName("Should publish parsing events")
    @Disabled("FIXME https://github.com/spring-projects-experimental/spring-boot-migrator/issues/902")
    void shouldPublishParsingEvents() {
        // remove events recorded in other tests
        capturedEvents.clear();

        Path baseDir = Path.of("./testcode/maven-projects/multi-module-1");
        parserSettings.setIgnoredPathPatterns(Set.of("**/target/**", "**/*.adoc"));
        List<Resource> resources = projectScanner.scan(baseDir);
        ExecutionContext ctx = new InMemoryExecutionContext(t -> {throw new RuntimeException(t);});

        RewriteProjectParsingResult parsingResult = sut.parse(baseDir, resources, ctx);

        assertThat(capturedEvents).hasSize(3);
        assertThat(capturedEvents.get(0).sourceFile().getSourcePath().toString())
                .isEqualTo("pom.xml");
        // FIXME: Parsing order differs from RewriteMavenProjectParser but b should be parsed first as it has no dependencies
        // Reported to OR the order gets lost in a Set in MavenMojoProjectParser
        assertThat(capturedEvents.get(1).sourceFile().getSourcePath().toString())
                .isEqualTo("module-b/pom.xml");
        assertThat(capturedEvents.get(2).sourceFile().getSourcePath().toString())
                .isEqualTo("module-a/pom.xml");

        assertThat(startedParsingEvent).isNotNull();
        assertThat(startedParsingEvent.resources()).isSameAs(resources);
        assertThat(finishedParsingEvent).isNotNull();
        assertThat(finishedParsingEvent.sourceFiles()).isSameAs(parsingResult.sourceFiles());
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("testFailingProject")
    // FIXME: Succeeds with RewriteMavenProjectParser
    void testFailingProject() {
        Path baseDir = Path.of("./testcode/maven-projects/failing");
        RewriteProjectParsingResult parsingResult = sut.parse(baseDir);
        assertThat(parsingResult.sourceFiles().get(1)).isInstanceOf(J.CompilationUnit.class);
        J.CompilationUnit cu = (J.CompilationUnit) parsingResult.sourceFiles().get(1);
        assertThat(cu.getTypesInUse().getTypesInUse().stream().map(t -> t.toString()).anyMatch(t -> t.equals("javax.validation.constraints.Min"))).isTrue();
    }

    @TestConfiguration
    static class TestEventListener {


        @EventListener(ParsedResourceEvent.class)
        public void onEvent(ParsedResourceEvent event) {
            capturedEvents.add(event);
        }

        @EventListener(StartedParsingProjectEvent.class)
        public void onStartedParsingProjectEvent(StartedParsingProjectEvent event) {
            startedParsingEvent = event;
        }

        @EventListener(FinishedParsingProjectEvent.class)
        public void onFinishedParsingProjectEvent(FinishedParsingProjectEvent event) {
            finishedParsingEvent = event;
        }
    }

    @Test
    @DisplayName("parseCheckstyle")
    void parseCheckstyle() {
        Path baseDir = getMavenProject("checkstyle");
        List<Resource> resources = projectScanner.scan(baseDir);
        RewriteProjectParsingResult parsingResult = sut.parse(baseDir, resources, new InMemoryExecutionContext(t -> {throw new RuntimeException(t);}));
        assertThat(parsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/rules.xml");
        assertThat(parsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/suppressions.xml");
    }

    @Test
    @DisplayName("parse4Modules")
    void parse4Modules() {
        Path baseDir = getMavenProject("4-modules");
        List<Resource> resources = projectScanner.scan(baseDir);

        assertThat(resources).hasSize(4);

        RewriteProjectParsingResult parsingResult = sut.parse(baseDir, resources, new InMemoryExecutionContext(t -> {throw new RuntimeException(t);}));
        assertThat(parsingResult.sourceFiles()).hasSize(4);
    }

    private Path getMavenProject(String s) {
        return Path.of("./testcode/maven-projects/").resolve(s).toAbsolutePath().normalize();
    }


}
