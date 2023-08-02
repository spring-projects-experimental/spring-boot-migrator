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
package org.springframework.sbm.parsers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class MavenBuildFileGraphTest {
    @Test
    @DisplayName("Should Create Correct BuildPath")
    void shouldCreateCorrectBuildPath() {
        MavenExecutionRequestFactory requestFactory = new MavenExecutionRequestFactory(new MavenConfigFileParser());
        PlexusContainerProvider containerProvider = new PlexusContainerProvider();
        MavenExecutor mavenExecutor = new MavenExecutor(requestFactory, containerProvider);
        Path baseDir = Path.of("./testcode/maven-projects/multi-module-1").toAbsolutePath().normalize();
        ProjectScanner scanner = new ProjectScanner(new FileSystemResourceLoader());
        List<Resource> resources = scanner.scan(baseDir, Set.of());
        mavenExecutor.onProjectSucceededEvent(baseDir, List.of("clean", "install"), event -> {
            MavenBuildFileGraph sut = new MavenBuildFileGraph(containerProvider);
            List<Resource> build = sut.build(resources, event.getSession()).getOrdered();

            assertThat(ResourceUtil.getPath(build.get(0)).toString())
                    .isEqualTo(baseDir.resolve("pom.xml").toString());

            assertThat(ResourceUtil.getPath(build.get(1)).toString())
                    .isEqualTo(baseDir.resolve("module-b/pom.xml").toString());

            assertThat(ResourceUtil.getPath(build.get(2)).toString())
                    .isEqualTo(baseDir.resolve("module-a/pom.xml").toString());
        });
    }


}