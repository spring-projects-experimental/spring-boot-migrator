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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.marker.Marker;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.maven.MavenModuleParser;

import java.nio.file.Path;
import java.util.*;

/**
 * @author Fabian Krüger
 */
@Slf4j

@RequiredArgsConstructor
public class SourceFileParser {

    private final MavenModuleParser moduleParser;

    public List<SourceFile> parseOtherSourceFiles(
            Path baseDir,
            ParserContext parserContext,
            List<Resource> resources,
            Map<Path, List<Marker>> provenanceMarkers,
            List<NamedStyles> styles,
            ExecutionContext executionContext
    ) {

        Set<SourceFile> parsedSourceFiles = new LinkedHashSet<>();

        parserContext.getSortedProjects().forEach(currentMavenProject -> {
            Xml.Document moduleBuildFile = currentMavenProject.getSourceFile();
            List<Marker> markers = provenanceMarkers.get(currentMavenProject.getPomFilePath());
            if(markers == null || markers.isEmpty()) {
                log.warn("Could not find provenance markers for resource '%s'".formatted(parserContext.getMatchingBuildFileResource(currentMavenProject)));
            }
            List<SourceFile> sourceFiles = moduleParser.parseModuleSourceFiles(resources, currentMavenProject, moduleBuildFile, markers, styles, executionContext, baseDir);
            parsedSourceFiles.addAll(sourceFiles);
        });

        return new ArrayList<>(parsedSourceFiles);
    }
}
