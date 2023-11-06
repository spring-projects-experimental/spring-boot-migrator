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
package org.springframework.sbm.parsers.maven;

import lombok.extern.slf4j.Slf4j;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.ipc.http.HttpSender;
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.maven.cache.MavenArtifactCache;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;

import java.util.function.Consumer;

/**
 * Wrapper for OpenRewrite {@link MavenArtifactDownloader}.
 *
 * @author fkrueger
 */
@Slf4j
public class RewriteMavenArtifactDownloader extends MavenArtifactDownloader {

    public RewriteMavenArtifactDownloader(MavenArtifactCache mavenArtifactCache, @Nullable MavenSettings mavenSettings, Consumer<Throwable> onError) {
        super(mavenArtifactCache, mavenSettings, onError);
    }

    public RewriteMavenArtifactDownloader(MavenArtifactCache mavenArtifactCache, @Nullable MavenSettings settings, HttpSender httpSender, Consumer<Throwable> onError) {
        super(mavenArtifactCache, settings, httpSender, onError);
    }
}
