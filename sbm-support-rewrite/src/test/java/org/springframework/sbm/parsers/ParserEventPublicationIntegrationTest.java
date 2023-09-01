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
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.events.SuccessfullyParsedProjectEvent;
import org.springframework.sbm.parsers.events.ParsedResourceEvent;
import org.springframework.sbm.parsers.events.StartedParsingProjectEvent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@SpringBootTest
public class ParserEventPublicationIntegrationTest {

    @Autowired
    RewriteProjectParser sut;

    @Autowired
    ProjectScanner projectScanner;

    @Autowired
    ParserSettings parserSettings;

    private static List<ParsedResourceEvent> capturedEvents = new ArrayList<>();
    private static StartedParsingProjectEvent startedParsingEvent;
    private static SuccessfullyParsedProjectEvent finishedParsingEvent;

    @Test
    @DisplayName("Should publish parsing events")
    void shouldPublishParsingEvents() {
        Path baseDir = Path.of("./testcode/maven-projects/multi-module-events");
        parserSettings.setIgnoredPathPatterns(Set.of("{**/target,target}", "**.adoc"));
        List<Resource> resources = projectScanner.scan(baseDir);
        ExecutionContext ctx = new InMemoryExecutionContext(t -> {throw new RuntimeException(t);});

        RewriteProjectParsingResult parsingResult = sut.parse(baseDir, resources, ctx);

        assertThat(capturedEvents).hasSize(4);

        assertThat(capturedEvents.get(0).sourceFile().getSourcePath().toString())
                .isEqualTo("pom.xml");
        assertThat(capturedEvents.get(1).sourceFile().getSourcePath().toString())
                .isEqualTo("module-b/pom.xml");
        assertThat(capturedEvents.get(2).sourceFile().getSourcePath().toString())
                .isEqualTo("module-a/pom.xml");
        assertThat(capturedEvents.get(3).sourceFile().getSourcePath().toString())
                .isEqualTo("module-a/src/main/java/com/acme/SomeClass.java");
        // ResourceParser not firing events
        // TODO: reactivate after https://github.com/openrewrite/rewrite-maven-plugin/issues/622
//        assertThat(capturedEvents.get(4).sourceFile().getSourcePath().toString())
//                .isEqualTo("module-a/src/test/resources/application.yaml");

        assertThat(startedParsingEvent).isNotNull();
        assertThat(startedParsingEvent.resources()).isSameAs(resources);
        assertThat(finishedParsingEvent).isNotNull();
        assertThat(finishedParsingEvent.sourceFiles()).isSameAs(parsingResult.sourceFiles());
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

        @EventListener(SuccessfullyParsedProjectEvent.class)
        public void onFinishedParsingProjectEvent(SuccessfullyParsedProjectEvent event) {
            finishedParsingEvent = event;
        }
    }

}
