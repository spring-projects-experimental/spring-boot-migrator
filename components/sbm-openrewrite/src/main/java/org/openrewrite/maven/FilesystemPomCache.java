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

import org.jetbrains.annotations.NotNull;
import org.openrewrite.maven.internal.RawPom;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.ResolvedGroupArtifactVersion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Cache implementation to store {@link Pom} objects as pom files in local Maven repository.
 *
 * @author Fabian Krüger
 */
class FilesystemPomCache implements Cache<ResolvedGroupArtifactVersion, Optional<Pom>> {

    private Path cacheDir;

    public FilesystemPomCache(Path cacheDir) {
        this.cacheDir = cacheDir;
    }

    @Override
    public void put(ResolvedGroupArtifactVersion key, Optional<Pom> optionalPom) {
        if (optionalPom.isPresent()) {
            Pom pom = optionalPom.get();
            Path path = Path.of(key.getGroupId()).resolve(key.getArtifactId()).resolve(key.getVersion());
            Path pomPath = cacheDir.resolve(path).resolve(pom.getSourcePath().getFileName());

            String pomXml = new PomSerializer().serialize(pom);

            Path pomDir = buildPomPath(key);
            String pomFile = buildPomFilename(key);

            if(!pomDir.toFile().exists()) {
                if(!pomDir.toFile().exists()) {
                    boolean dirCreated = pomDir.toFile().mkdirs();
                    if(!dirCreated) {
                        throw new RuntimeException("Could not create non-existent dir '%s'".formatted(pomDir));
                    }
                }
                try {
                    Files.writeString(pomDir.resolve(pomFile), pomXml);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @NotNull
    private String buildPomFilename(ResolvedGroupArtifactVersion key) {
        return key.getArtifactId() + "-" + key.getVersion() + (key.getDatedSnapshotVersion() != null ? key.getDatedSnapshotVersion() : "") + ".pom";
    }

    @NotNull
    private Path buildPomPath(ResolvedGroupArtifactVersion key) {
        return cacheDir
                .resolve(key.getGroupId().replace(".", "/"))
                .resolve(key.getArtifactId())
                .resolve(key.getVersion());
    }

    @Override
    public Optional<Pom> getIfPresent(ResolvedGroupArtifactVersion key) {

        Path pomDir = buildPomPath(key);
        String pomFilename = buildPomFilename(key);

        if(!pomDir.resolve(pomFilename).toFile().exists()) {
            return null; // Expected by OR code when no pom exists
        }

        try {
            String pomContent = Files.readString(pomDir.resolve(pomFilename));
            RawPom parse = RawPom.parse(new ByteArrayInputStream(pomContent.getBytes()), null);
            Pom pom = parse.toPom(pomDir.resolve(pomFilename), null);
            return Optional.of(pom);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
