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

import lombok.extern.slf4j.Slf4j;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.cache.ReadOnlyLocalMavenArtifactCache;
import org.openrewrite.maven.internal.MavenParsingException;
import org.openrewrite.maven.tree.MavenRepository;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class DependencyHelper {

    public List<Path> downloadArtifacts(Consumer<Throwable> errorHandler, Set<Pom.Dependency> dependencies) {
        MavenArtifactDownloader artifactDownloader = new MavenArtifactDownloader(
                ReadOnlyLocalMavenArtifactCache.mavenLocal().orElse(
                        new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".rewrite", "cache", "artifacts"))
                ),
                null,
                errorHandler
        );
        return dependencies.stream()
                .filter(d -> d.getRepository() != null)
                .map(artifactDownloader::downloadArtifact)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Path> downloadArtifacts(Set<Pom.Dependency> dependencies) {
        return downloadArtifacts(createErrorHandler(), dependencies);
    }

    private static Consumer<Throwable> createErrorHandler() {
        Consumer<Throwable> errorConsumer = (t) -> {
            if (t instanceof MavenParsingException) {
                log.error(t.getMessage());
            } else {
                throw new RuntimeException(t);
            }
        };
        return errorConsumer;
    }

    public Set<Pom.Dependency> mapCoordinatesToDependencies(List<String> coordinates) {
        Set<Pom.Dependency> dependencies = new HashSet<>();
        coordinates.forEach(c -> {

            String[] parts = c.split(":");

            if (parts.length != 3) throw new IllegalArgumentException("Given coordinate is invalid '" + c + "'");

            String groupId = parts[0];
            String artifactId = parts[1];
            String version = parts[2];

            MavenRepository mavenRepository = new MavenRepository(
                    "jcenter",
                    URI.create("https://jcenter.bintray.com"),
                    true,
                    true,
                    true,
                    null,
                    null
            );
            Pom model = Pom.build(groupId, artifactId, version, null);
            Pom.Dependency dependency = new Pom.Dependency(mavenRepository, Scope.Compile, null, null, false, model, version, Set.of());
            dependencies.add(dependency);
        });
        return dependencies;
    }

    public Optional<Path> downloadArtifact(Pom.Dependency d) {
        List<Path> paths = this.downloadArtifacts(Set.of(d));
        if (paths == null || paths.isEmpty()) {
            log.error(String.format("The dependency '%s:%s:%s' of packaging '%s' could not be downloaded.", d.getGroupId(), d.getArtifactId(), d.getVersion(), d.getModel().getPackaging()));
            return Optional.empty();
        }
        return Optional.of(paths.get(0));
    }
}
