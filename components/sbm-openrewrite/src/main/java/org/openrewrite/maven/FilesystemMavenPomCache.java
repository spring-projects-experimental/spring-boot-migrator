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
package org.openrewrite.maven;

import lombok.Value;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.cache.MavenPomCache;
import org.openrewrite.maven.tree.*;

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;

/**
 * A `MavenPomCache` that uses local filesystem, e.g. Maven repository, to store and retrieve pom files.
 *
 * The cache directory is optionally configurable. If not set local Maven repository is used.
 *
 * The `FilesystemPomCache` must be provided through `ExecutionContext`.
 *
 * [source,java]
 * ....
 * ExecutionContext executionContext = ...
 * MavenExecutionContextView.view(executionContext).setPomCache(new LocalMavenPomCache());
 * ....
 *
 * @author Fabian Krüger
 */
public class FilesystemMavenPomCache implements MavenPomCache {

    private final Cache<MetadataKey, Optional<MavenMetadata>> mavenMetadataCache;
    private Cache<ResolvedGroupArtifactVersion, Optional<Pom>> pomCache;
    private final Cache<MavenRepository, Optional<MavenRepository>> repositoryCache;
    private final Cache<ResolvedGroupArtifactVersion, ResolvedPom> dependencyCache;

    @Value
    public static class MetadataKey {
        URI repository;
        GroupArtifactVersion gav;
    }

    /**
     * Creates `FileSystemPomCache` using local Maven repository `${user.home}/.m2/repository` as cache dir.
     */
    public FilesystemMavenPomCache() {
        this(Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository"));
    }


    /**
     * Creates a `FileSystemPomCache` using the provided `cacheDir`.
     *
     * @throws IllegalArgumentException if cacheDir does not exist.
     */
    public FilesystemMavenPomCache(Path cacheDir) {
        if(!cacheDir.toFile().exists()) {
             throw new IllegalArgumentException("The given cache dir '%s' does not exist, Please provide an existing dir.".formatted(cacheDir));
        }
        pomCache = new FilesystemPomCache(cacheDir);
        mavenMetadataCache = new MavenMetadataCache();
        repositoryCache = new RepositoryCache();
        dependencyCache = new DependencyCache();
    }

    @Override
    public @Nullable ResolvedPom getResolvedDependencyPom(ResolvedGroupArtifactVersion dependency) {
        return dependencyCache.getIfPresent(dependency);
    }

    @Override
    public void putResolvedDependencyPom(ResolvedGroupArtifactVersion dependency, ResolvedPom resolved) {
        dependencyCache.put(dependency, resolved.deduplicate());
    }

    @Override
    public @Nullable Optional<MavenMetadata> getMavenMetadata(URI repo, GroupArtifactVersion gav) {
        return mavenMetadataCache.getIfPresent(new MetadataKey(repo, gav));
    }

    @Override
    public void putMavenMetadata(URI repo, GroupArtifactVersion gav, @Nullable MavenMetadata metadata) {
        mavenMetadataCache.put(new MetadataKey(repo, gav), Optional.ofNullable(metadata));
    }

    @Override
    public @Nullable Optional<Pom> getPom(ResolvedGroupArtifactVersion gav) throws MavenDownloadingException {
        return pomCache.getIfPresent(gav);
    }

    @Override
    public void putPom(ResolvedGroupArtifactVersion gav, @Nullable Pom pom) {
        pomCache.put(gav, Optional.ofNullable(pom));
    }

    @Override
    public @Nullable Optional<MavenRepository> getNormalizedRepository(MavenRepository repository) {
        return repositoryCache.getIfPresent(repository);
    }

    @Override
    public void putNormalizedRepository(MavenRepository repository, MavenRepository normalized) {
        repositoryCache.put(repository, Optional.ofNullable(normalized));
    }

    private class MavenMetadataCache implements Cache<MetadataKey, Optional<MavenMetadata>> {
        @Override
        public void put(MetadataKey key, Optional<MavenMetadata> value) {

        }

        @Override
        public Optional<MavenMetadata> getIfPresent(MetadataKey key) {
            return Optional.empty();
        }
    }

    private class RepositoryCache implements Cache<MavenRepository, Optional<MavenRepository>> {
        @Override
        public void put(MavenRepository key, Optional<MavenRepository> value) {

        }

        @Override
        public Optional<MavenRepository> getIfPresent(MavenRepository key) {
            return Optional.empty();
        }
    }

    private class DependencyCache implements Cache<ResolvedGroupArtifactVersion, ResolvedPom> {
        @Override
        public void put(ResolvedGroupArtifactVersion key, ResolvedPom value) {

        }

        @Override
        public ResolvedPom getIfPresent(ResolvedGroupArtifactVersion key) {
            return null;
        }
    }
}
