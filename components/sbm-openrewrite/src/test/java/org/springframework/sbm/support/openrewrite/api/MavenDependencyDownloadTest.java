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
package org.springframework.sbm.support.openrewrite.api;

import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.maven.MavenDownloadingException;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.cache.RocksdbMavenPomCache;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.GroupArtifactVersion;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.ResolvedPom;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.Problem;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MavenDependencyDownloadTest {

    @Problem(
            description = "Fails with containingPom being null",
            since = "7.18.2",
            fixedIn = "7.35.0"
    )
    void downloadDependencies(@TempDir Path tempDir) throws MavenDownloadingException {
        InMemoryExecutionContext executionContext = new InMemoryExecutionContext((t) -> System.out.println(t.getMessage()));
        MavenExecutionContextView ctx = MavenExecutionContextView.view(executionContext);
        ctx.setPomCache(new RocksdbMavenPomCache(tempDir.resolve("rewrite-cache")));

        HashMap<Path, Pom> projectPoms = new HashMap<>();
        MavenPomDownloader mavenPomDownloader = new MavenPomDownloader(projectPoms, ctx);
        GroupArtifactVersion gav = new GroupArtifactVersion("com.h2database", "h2", "1.4.200");
        String relativePath = "";
        ResolvedPom containingPom = null;
        Pom download = mavenPomDownloader.download(gav, relativePath, containingPom, List.of());
        assertThat(download).isNotNull();
        assertThat(download.getSourcePath().toString()).isEqualTo("com.h2database/h2/1.4.200");
    }

}
