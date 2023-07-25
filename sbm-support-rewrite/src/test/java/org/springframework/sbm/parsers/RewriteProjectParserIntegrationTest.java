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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.sbm.RewriteParserConfig;
import org.springframework.sbm.parsers.events.FinishedParsingProjectEvent;
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
public class RewriteProjectParserIntegrationTest {

    @Autowired
    RewriteProjectParser sut;

    @Autowired
    ProjectScanner projectScanner;

    private static List<ParsedResourceEvent> capturedEvents = new ArrayList<>();
    private static StartedParsingProjectEvent startedParsingEvent;
    private static FinishedParsingProjectEvent finishedParsingEvent;

    @Test
    @DisplayName("Should publish parsing events")
    void shouldPublishParsingEvents() {
        Path baseDir = Path.of("./testcode/maven-projects/multi-module-1");
        List<Resource> resources = projectScanner.scan(baseDir, Set.of());
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

}
