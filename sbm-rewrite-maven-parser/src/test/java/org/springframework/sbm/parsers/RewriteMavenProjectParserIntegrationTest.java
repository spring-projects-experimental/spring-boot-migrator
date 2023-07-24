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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.event.EventListener;
import org.springframework.sbm.parsers.events.ParsedResourceEvent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@SpringBootTest
public class RewriteMavenProjectParserIntegrationTest {

    @Autowired
    private RewriteMavenProjectParser sut;

    @Test
    @DisplayName("Should Publish Build Events")
    void shouldPublishBuildEvents() {

        Path baseDir = Path.of("./testcode/maven-projects/multi-module-1");
        RewriteProjectParsingResult parsingResult = sut.parse(baseDir);
        assertThat(capturedEvents).hasSize(3);
        assertThat(capturedEvents.get(0).sourceFile().getSourcePath().toString())
                .isEqualTo("pom.xml");
        assertThat(capturedEvents.get(1).sourceFile().getSourcePath().toString())
                .isEqualTo("module-a/pom.xml");
        assertThat(capturedEvents.get(2).sourceFile().getSourcePath().toString())
                .isEqualTo("module-b/pom.xml");
    }

    private static List<ParsedResourceEvent> capturedEvents = new ArrayList<>();

    @TestConfiguration
    static class TestEventListener {
        @EventListener(ParsedResourceEvent.class)
        public void onEvent(ParsedResourceEvent event) {
            capturedEvents.add(event);
        }
    }

}
