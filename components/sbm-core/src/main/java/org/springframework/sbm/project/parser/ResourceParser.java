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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.hcl.HclParser;
import org.openrewrite.json.JsonParser;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.Markers;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.protobuf.ProtoParser;
import org.openrewrite.text.PlainTextParser;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.xml.tree.Xml;
import org.openrewrite.yaml.YamlParser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.events.StartedScanningProjectResourceEvent;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceParser {

    private final JsonParser jsonParser;
    private final XmlParser xmlParser;
    private final YamlParser yamlParser;
    private final PropertiesParser propertiesParser;
    private final PlainTextParser plainTextParser;
    private final ResourceFilter resourceFilter;
    private final ApplicationEventPublisher eventPublisher;

    List<Resource> filter(Path projectDirectory, Set<Path> resourcePaths, List<Resource> resources, Path relativeModuleDir) {
        Path comparingPath = relativeModuleDir != null ? projectDirectory.resolve(relativeModuleDir) : projectDirectory;
        List<Resource> relevantResources = resourceFilter.filter(resources, comparingPath, resourcePaths);
        return relevantResources;
    }

    private List<Parser.Input> createParserInputs(List<Resource> relevantResources) {
        return relevantResources.stream().map(js -> {
            Path jsPath = getPath(js);
            return new Parser.Input(jsPath, () -> {
                InputStream content = getInputStream(js);
                return content;
            });
        }).collect(Collectors.toList());
    }



    // TODO: duplicate of PathScanner.getPath(), move to helper
    private static Path getPath(Resource r) {
        try {
            return r.getFile().toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getInputStream(Resource r) {
        try {
            return r.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SourceFile> parse(Path baseDir, List<Resource> relevantResources, List<Marker> markers) {
        List<Parser.Input> parserInputs = createParserInputs(relevantResources);

        HashMap<Parser<? extends SourceFile>, List<Parser.Input>> parserAndParserInputMappings = new LinkedHashMap();
        parserAndParserInputMappings.put(jsonParser, new ArrayList<>());
        parserAndParserInputMappings.put(xmlParser, new ArrayList<>());
        parserAndParserInputMappings.put(yamlParser, new ArrayList<>());
        parserAndParserInputMappings.put(propertiesParser, new ArrayList<>());
        parserAndParserInputMappings.put(new ProtoParser(), new ArrayList<>());
        parserAndParserInputMappings.put(HclParser.builder().build(), new ArrayList<>());
        parserAndParserInputMappings.put(plainTextParser, new ArrayList<>());

        parserInputs.forEach(r -> {
            Parser parser = parserAndParserInputMappings.keySet().stream()
                    .filter(p -> p.accept(r))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Could not find matching parser for " + r.getPath()));

            parserAndParserInputMappings.get(parser).add(r);
        });

        ParsingExecutionContextView ctx = ParsingExecutionContextView.view(new RewriteExecutionContext(eventPublisher));
        ctx.setParsingListener((input, sourceFile) -> eventPublisher.publishEvent(new StartedScanningProjectResourceEvent(sourceFile.getSourcePath())));

        return parserAndParserInputMappings.entrySet().stream()
                .map(e -> e.getKey().parseInputs(e.getValue(), baseDir, ctx))
                .flatMap(List::stream)
                .map(e -> addMarkers(e, markers))
                .collect(Collectors.toList());

    }

    private SourceFile addMarkers(SourceFile e, List<Marker> markers) {
        return e.withMarkers(Markers.build(markers));
    }

    @Component
    public static class ResourceFilter {
        private List<Resource> filter(List<Resource> resources, Path moduleDir, Set<Path> searchDirs) {
            return resources.stream()
                    .filter(r -> searchDirs.stream().anyMatch(dir -> moduleDir.relativize(getPath(r)).startsWith(dir)))
                    .collect(Collectors.toList());
        }
    }

    // TODO: Filter resources exceeding size threshold
	/*
	long fileSize = attrs.size();
        if ((sizeThresholdMb > 0 && fileSize > sizeThresholdMb * 1024L * 1024L)) {
            alreadyParsed.add(path);
            log.info("Skipping parsing " + path + " as its size + " + fileSize / (1024L * 1024L) +
                    "Mb exceeds size threshold " + sizeThresholdMb + "Mb");
            return false;
        }
	 */
}




