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
package org.springframework.sbm.project.parser;

import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.cache.ReadOnlyLocalMavenArtifactCache;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

//@Component
//@RequiredArgsConstructor
@Deprecated
public class RewriteMavenParserFactory {

//    private final MavenPomCacheProvider mavenPomCacheProvider;
//    private final ApplicationEventPublisher eventPublisher;
//    private JavaProvenanceMarkerFactory javaProvenanceMarkerFactory;
//
//    private final ResourceParser resourceParser;

    MavenProjectParser createRewriteMavenParser(Path absoluteProjectDir, RewriteExecutionContext rewriteExecutionContext) {
        Consumer<Throwable> onError = rewriteExecutionContext.getOnError();
        MavenArtifactDownloader downloader = new MavenArtifactDownloader(
                ReadOnlyLocalMavenArtifactCache.mavenLocal().orElse(
                        new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".rewrite", "cache", "artifacts"))
                ),
                null,
                onError
        );
        // rewriteExecutionContext.getMavenPomCache();

        MavenParser.Builder mavenParserBuilder = MavenParser.builder()
                .mavenConfig(absoluteProjectDir.resolve(".mvn/maven.config"));

//        MavenProjectParser mavenProjectParser = new MavenProjectParser(
//                resourceParser,
//                downloader,
//                mavenParserBuilder,
//                JavaParser.fromJavaVersion(),
//                eventPublisher,
//                javaProvenanceMarkerFactory,
//                rewriteExecutionContext
//        );
//        return mavenProjectParser;
        // FIXME #7
        return null;
    }

//    private MavenPomCache getPomCache() {
//        return mavenPomCacheProvider.getPomCache();
//    }


}
