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
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
public class ClasspathExtractor {
    private final MavenArtifactDownloader rewriteMavenArtifactDownloader;

    public Set<Path> extractClasspath(MavenResolutionResult pom, Scope scope) {
        List<ResolvedDependency> resolvedDependencies = pom.getDependencies().get(scope);
        if (resolvedDependencies != null) {
            return resolvedDependencies
                    // FIXME: 945 - deal with dependencies to projects in reactor
                    //
                    .stream()
                    .filter(rd -> rd.getRepository() != null)
                    .map(rd -> rewriteMavenArtifactDownloader.downloadArtifact(rd))
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }
}
