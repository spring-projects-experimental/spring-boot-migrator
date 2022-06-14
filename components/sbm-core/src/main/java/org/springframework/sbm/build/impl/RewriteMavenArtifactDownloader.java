package org.springframework.sbm.build.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.ipc.http.HttpSender;
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.cache.MavenArtifactCache;
import org.openrewrite.maven.cache.ReadOnlyLocalMavenArtifactCache;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.function.Consumer;

@Slf4j
@Component
public class RewriteMavenArtifactDownloader extends MavenArtifactDownloader {

    // TODO: #7 make artifactCache configurable
    public RewriteMavenArtifactDownloader() {
        super(
                new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".m2", "repository")).orElse(
                        new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".rewrite", "cache", "artifacts"))
                ),
                null,
                (t) -> log.error("Error while downloading dependencies", t)
        );

//        super(new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".m2", "repository")),
//                null,
//                (t) -> log.error("Error while downloading dependencies", t));
    }

    public RewriteMavenArtifactDownloader(MavenArtifactCache mavenArtifactCache, @Nullable MavenSettings settings, HttpSender httpSender, Consumer<Throwable> onError) {
        super(mavenArtifactCache, settings, httpSender, onError);
    }
}
