package org.springframework.sbm.project.parser;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openrewrite.SourceFile;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.events.StartedScanningProjectResourceEvent;
import org.springframework.sbm.project.TestDummyResource;
import org.springframework.sbm.properties.parser.RewritePropertiesParser;
import org.springframework.sbm.xml.parser.RewriteXmlParser;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class ResourceParserTest {

    private ResourceParser sut;
    private ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
    private Path baseDir = Path.of("some-base-dir").toAbsolutePath();
    private Path resourceDirPath = Path.of("src/main/resources");
    private Set<Path> resourcePaths = Set.of(resourceDirPath);

    @BeforeEach
    void beforeEach() {
        sut = new ResourceParser(
                new RewriteJsonParser(),
                new RewriteXmlParser(),
                new RewriteYamlParser(),
                new RewritePropertiesParser(),
                new RewritePlainTextParser(),
                new ResourceParser.ResourceFilter(),
                eventPublisher
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
    void test(String filename, String content, String className) throws ClassNotFoundException {
        List<Resource> resources = getResourceAsList(filename, content);
        List<SourceFile> parsedResources = sut.parse(baseDir, resourcePaths, resources);
        assertCorrectParsing(filename, content, Class.forName(className), parsedResources);
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