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
package org.springframework.sbm.project.parser;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.text.PlainText;
import org.openrewrite.text.PlainTextParser;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.rewrite.parser.RewriteExecutionContext;
import org.springframework.sbm.engine.events.StartedScanningProjectResourceEvent;
import org.springframework.sbm.project.TestDummyResource;
import org.springframework.sbm.properties.parser.RewritePropertiesParser;
import org.springframework.sbm.xml.parser.RewriteXmlParser;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class ResourceParserTest {

    private ResourceParser sut;
    private ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
    private Path baseDir = Path.of("some-base-dir").toAbsolutePath();
    private Path resourceDirPath = Path.of("src/main/resources");
    private Set<Path> resourcePaths = Set.of(resourceDirPath);
    private ExecutionContext executionContext = new RewriteExecutionContext();

    @BeforeEach
    void beforeEach() {
        sut = new ResourceParser(
                new RewriteJsonParser(),
                new RewriteXmlParser(),
                new RewriteYamlParser(),
                new RewritePropertiesParser(),
                new RewritePlainTextParser(),
                new ResourceParser.ResourceFilter(),
                eventPublisher,
                executionContext
                );
    }

    @ParameterizedTest
    @CsvSource({
            "some.json,{},org.openrewrite.json.tree.Json$Document",
            "some.xml,<xml/>,org.openrewrite.xml.tree.Xml$Document",
            "some.yaml,foo:bar,org.openrewrite.yaml.tree.Yaml$Documents",
            "some.yml,foo2:bar2,org.openrewrite.yaml.tree.Yaml$Documents",
            "some.properties,a=b,org.openrewrite.properties.tree.Properties$File",
            "some.xml,<xml/>,org.openrewrite.xml.tree.Xml$Document",
            "some.yaml2,foo:bar,org.openrewrite.text.PlainText",
    })
    void picksMatchingParser(String filename, String content, String className) throws ClassNotFoundException {
        List<Resource> resources = getResourceAsList(filename, content);
        List<SourceFile> parsedResources = sut.parse(baseDir, resources, new ArrayList<>());
        assertCorrectParsing(filename, content, Class.forName(className), parsedResources);
    }

    // TODO: If this test fails RewritePlainTextParser.parseInputs() can be removed because PlainTextParser then publishes parser events
    @Test
    void originalPlainTextParserSholdPublishParserEvents() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ExecutionContext ctx = new RewriteExecutionContext();
        AtomicReference<Parser.Input> parsedInput = new AtomicReference<>();
        AtomicReference<SourceFile> parsedSourceFile = new AtomicReference<>();
        ParsingExecutionContextView.view(ctx).setParsingListener(new ParsingEventListener() {
            @Override
            public void parsed(Parser.Input input, SourceFile sourceFile) {
                parsedInput.set(input);
                parsedSourceFile.set(sourceFile);
                latch.countDown();
            }
        });
        List<Resource> resources = getResourceAsList("some-file-parsed-by-plaintext.txt", "content");

        PlainTextParser sut = new PlainTextParser();
        Path filePath = Path.of("some-file-parsed-by-plaintext.txt");
        String fileContent = "";
        Parser.Input pi = new Parser.Input(filePath, () -> new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
        List<PlainText> parsedResources = sut.parseInputs(List.of(pi), null, ctx).map(PlainText.class::cast).toList();
        latch.await(50, TimeUnit.MILLISECONDS);
        assertThat(parsedInput.get()).isSameAs(pi);
        assertThat(parsedSourceFile.get()).isSameAs(parsedResources.get(0));
    }

    @NotNull
    private List<Resource> getResourceAsList(String filename, String jsonContent) {
        Path sourcePath = resourceDirPath.resolve(filename);
        Path absolutePath = baseDir.resolve(sourcePath);
        List<Resource> resources = List.of(new TestDummyResource(absolutePath, jsonContent));
        return resources;
    }

    private void assertCorrectParsing(String filename, String content, Class<?> expectedType, List<SourceFile> parsedResources) {
        // parser event was published
        ArgumentCaptor<StartedScanningProjectResourceEvent> argumentCaptor = ArgumentCaptor.forClass(StartedScanningProjectResourceEvent.class);
        verify(eventPublisher).publishEvent(argumentCaptor.capture());
        // parser event has sourcePath
        assertThat(argumentCaptor.getValue().getPath()).isEqualTo(resourceDirPath.resolve(filename));
        assertThat(parsedResources.get(0)).isInstanceOf(expectedType);
        assertThat(parsedResources.get(0).printAll()).isEqualTo(content);
        assertThat(parsedResources.get(0)).isInstanceOf(expectedType);
        assertThat(parsedResources.get(0).printAll()).isEqualTo(content);
    }

}