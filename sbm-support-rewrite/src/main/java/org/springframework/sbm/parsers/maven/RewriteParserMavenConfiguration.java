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

import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.cache.MavenArtifactCache;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.sbm.boot.autoconfigure.ScopeConfiguration;
import org.springframework.sbm.scopes.ProjectMetadata;

import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * @author Fabian Krüger
 */
@AutoConfiguration
@Import({ScopeConfiguration.class})
public class RewriteParserMavenConfiguration {

    @Bean
    MavenProvenanceMarkerFactory mavenProvenanceMarkerFactory() {
        return new MavenProvenanceMarkerFactory();
    }

    @Bean
    BuildFileParser buildFileParser(MavenSettingsInitializer mavenSettingsInitializer) {
        return new BuildFileParser(mavenSettingsInitializer);
    }

    @Bean
    RewriteMavenArtifactDownloader artifactDownloader(MavenArtifactCache mavenArtifactCache, ProjectMetadata projectMetadata, Consumer<Throwable> artifactDownloaderErrorConsumer) {
        return new RewriteMavenArtifactDownloader(mavenArtifactCache, projectMetadata.getMavenSettings(), artifactDownloaderErrorConsumer);
    }

    @Bean
    @ConditionalOnMissingBean(MavenArtifactCache.class)
    MavenArtifactCache mavenArtifactCache() {
        return new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".m2", "repository")).orElse(
                new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".rewrite", "cache", "artifacts"))
        );
    }

    @Bean
    MavenSettingsInitializer mavenSettingsInitializer(ExecutionContext executionContext, ProjectMetadata projectMetadata) {
        return new MavenSettingsInitializer(executionContext, projectMetadata);
    }
}
