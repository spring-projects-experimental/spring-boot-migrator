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

import lombok.extern.slf4j.Slf4j;
import org.openrewrite.marker.Marker;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@Slf4j
@Component
public class BuildFileParser {
    public Map<Resource, Xml.Document> parseBuildFiles(Stream<Resource> buildFileResources, Map<Resource, List<? extends Marker>> provenanceMarkers) {
        return null;
    }

    // TODO: filter out poms in test sources, src/test/...
    public Stream<Resource> retrieveSortedBuildFiles(List<Resource> resources) {
        return resources.stream()
                .filter(r -> "pom.xml".equals(ResourceUtil.getPath(r).toFile().getName()))
                .filter(r -> filterTestResources(r))
                .sorted((r1, r2) -> {

                    Path r1Path = ResourceUtil.getPath(r1);
                    ArrayList<String> r1PathParts = new ArrayList<String>();
                    r1Path.iterator().forEachRemaining(it -> r1PathParts.add(it.toString()));

                    Path r2Path = ResourceUtil.getPath(r2);
                    ArrayList<String> r2PathParts = new ArrayList<String>();
                    r2Path.iterator().forEachRemaining(it -> r2PathParts.add(it.toString()));
                    return Integer.compare(r1PathParts.size(), r2PathParts.size());
                });
    }

    private static boolean filterTestResources(Resource r) {
        String path = ResourceUtil.getPath(r).toString();
        boolean underTest = path.contains("src/test");
        if(underTest) {
            log.info("Ignore build file '%s' having 'src/test' in its path indicating it's a build file for tests.".formatted(path));
        }
        return !underTest;
    }
}
