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
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.cache.ReadOnlyLocalMavenArtifactCache;
import org.openrewrite.maven.internal.MavenParsingException;
import org.openrewrite.maven.tree.*;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class DependencyHelper {

    public List<Path> downloadArtifacts(Consumer<Throwable> errorHandler, Set<ResolvedDependency> dependencies) {
// TODO: #111
        String userDirStr = System.getProperty("user.home");
        Path m2Repository = Path.of(userDirStr).resolve(".m2/repository");
        MavenArtifactDownloader artifactDownloader = new MavenArtifactDownloader(
                        ReadOnlyLocalMavenArtifactCache.mavenLocal().orElse(
                            new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".rewrite", "cache", "artifacts"))
                ),
//                new LocalMavenArtifactCache(userDirStr, ".m2", "repository")),
                null,
                errorHandler
        );
        return dependencies.stream()
                .filter(d -> d.getRepository() != null)
                .map(artifactDownloader::downloadArtifact)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Path> downloadArtifacts(Set<ResolvedDependency> dependencies) {
        return downloadArtifacts(createErrorHandler(), dependencies);
    }

    private static Consumer<Throwable> createErrorHandler() {
        Consumer<Throwable> errorConsumer = (t) -> {
            if (t instanceof MavenParsingException) {
                log.error(t.getMessage());
            } else {
                t.printStackTrace();
            }
        };
        return errorConsumer;
    }

    public Set<Dependency> mapCoordinatesToDependencies(List<String> coordinates) {
        Set<Dependency> dependencies = new LinkedHashSet<>();
        coordinates.forEach(c -> {

            String[] parts = c.split(":");

            if (parts.length != 3) throw new IllegalArgumentException("Given coordinate is invalid '" + c + "'");

            String groupId = parts[0];
            String artifactId = parts[1];
            String version = parts[2];

            MavenRepository mavenRepository = new MavenRepository(
                    "jcenter",
                    "https://jcenter.bintray.com",
                    true,
                    true,
                    true,
                    null,
                    null
            );

            @Nullable String classifier = null;
            @Nullable String type = null;
            String scope = Scope.Compile.name();
            List<GroupArtifact> exclusions = new ArrayList<>();
            boolean optional = false;

            Dependency dependency = new Dependency(
                    new GroupArtifactVersion(groupId, artifactId, version),
                    classifier,
                    type,
                    scope,
                    exclusions,
                    optional
            );

            dependencies.add(dependency);
        });
        return dependencies;
    }

    public Optional<Path> downloadArtifact(ResolvedDependency d) {
        List<Path> paths = this.downloadArtifacts(Set.of(d));
        if (paths == null || paths.isEmpty()) {
            log.warn(String.format("The dependency '%s:%s:%s' could not be downloaded.", d.getGroupId(), d.getArtifactId(), d.getVersion()));
            return Optional.empty();
        }
        return Optional.of(paths.get(0));
    }
}
