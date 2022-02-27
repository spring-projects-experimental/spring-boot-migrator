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

import org.springframework.sbm.Problem;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.internal.RawMaven;
import org.openrewrite.maven.tree.MavenRepository;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MavenDependencyDownloadTest {

    @Problem(
            description = "Some dependencies cannot be downloaded because the resolved resource path is wrong",
            version = "7.16.3"
    )
    void downloadDependencies() {
        InMemoryExecutionContext executionContext = new InMemoryExecutionContext((t) -> System.out.println(t.getMessage()));
        MavenPomDownloader mavenPomDownloader = new MavenPomDownloader(new InMemoryMavenPomCache(), new HashMap<>(), executionContext);
        RawMaven download = mavenPomDownloader.download("com.h2database", "h2", "1.4.200", null, null, List.of(new MavenRepository("maven", URI.create("https://repo.maven.apache.org/maven2"), true, false, null, null)), executionContext);
        assertThat(download).isNull();
    }
}
