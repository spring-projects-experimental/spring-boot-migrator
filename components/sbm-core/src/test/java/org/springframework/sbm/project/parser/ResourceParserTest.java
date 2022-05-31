package org.springframework.sbm.project.parser;

import io.github.resilience4j.core.EventPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.openrewrite.SourceFile;
import org.openrewrite.json.JsonParser;
import org.openrewrite.json.tree.Json;
import org.openrewrite.json.tree.JsonValue;
import org.openrewrite.json.tree.Space;
import org.openrewrite.marker.Markers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.events.StartedScanningProjectResourceEvent;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.TestDummyResource;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class ResourceParserTest {

    @Test
    void shouldParseJsonResourcesWithJsonParser() {
        JsonParser jsonParser = new RewriteJsonParser();
        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        ResourceParser sut = new ResourceParser(jsonParser, eventPublisher);

        Path baseDir = Path.of("some-base-dir").toAbsolutePath();
        Path sourcePath = Path.of("src/main/resources/some.json");
        Set<Path> resourcePaths = Set.of(Path.of("src/main/resources"));
        Path resourcePath = baseDir.resolve(sourcePath);
        String jsonContent = "{}";
        List<Resource> resources = List.of(new TestDummyResource(resourcePath, jsonContent));

        List<SourceFile> parse = sut.parse(baseDir, resourcePaths, resources);

        // parser event was published
        ArgumentCaptor<StartedScanningProjectResourceEvent> argumentCaptor = ArgumentCaptor.forClass(StartedScanningProjectResourceEvent.class);
        verify(eventPublisher).publishEvent(argumentCaptor.capture());
        // parser event has sourcePath
        assertThat(argumentCaptor.getValue().getPath()).isEqualTo(sourcePath);
        // Json document with json content  was parsed
        assertThat(parse.get(0)).isInstanceOf(Json.Document.class);
        assertThat(parse.get(0).printAll()).isEqualTo(jsonContent);
    }

    @Test
    void shouldParseXmlResourcesWithXmlParser() {

    }

    @Test
    void shouldParseEveryResourceTypeOnlyOnce() {

    }
}