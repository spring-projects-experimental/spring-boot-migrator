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
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.tree.Xml;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
public class ClasspathRegistryFactory {

    private final ClasspathExtractor classpathExtractor;

    public ClasspathRegistry create(Path baseDir, List<Xml.Document> parsedBuildFiles) {
        Map<Path, MavenResolutionResult> map = new HashMap<>();
        parsedBuildFiles.stream()
                .forEach(buildFile -> {
                    Path pomPath = baseDir.resolve(buildFile.getSourcePath()).toAbsolutePath().normalize();
                    MavenResolutionResult marker = buildFile.getMarkers().findFirst(MavenResolutionResult.class).get();
                    map.put(pomPath, marker);
                });

        return new ClasspathRegistry(classpathExtractor);
    }
}
