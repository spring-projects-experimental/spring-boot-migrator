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
package org.springframework.sbm.build.api;

import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.MavenDownloadingException;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.GroupArtifactVersion;
import org.openrewrite.maven.tree.MavenRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * This class holds all the dependencies included in a spring artifact
 */
public class SpringManagedDependencies {

    private static List<MavenRepository> SPRING_REPOSITORIES = List.of(
        new MavenRepository("spring-release", "https://repo.spring.io/release", "true", "false", true, null, null, null)
    );

    private List<org.openrewrite.maven.tree.Dependency> dependencies;
    private static Map<GroupArtifactVersion, SpringManagedDependencies> INSTANCES = new HashMap<>();

    public static SpringManagedDependencies by(String groupId, String artifact, String version, ExecutionContext executionContext){
        final GroupArtifactVersion groupArtifactVersion =
                new GroupArtifactVersion(groupId, artifact, version);

        INSTANCES.computeIfAbsent(groupArtifactVersion, gav ->  new SpringManagedDependencies(gav, executionContext));
        return INSTANCES.get(groupArtifactVersion);
    }

    private SpringManagedDependencies(GroupArtifactVersion groupArtifactVersion, ExecutionContext executionContext){
        try {
            dependencies = new MavenPomDownloader(Collections.emptyMap(), executionContext)
                    .download(groupArtifactVersion, null, null, SPRING_REPOSITORIES)
                    .getDependencies();
        } catch (MavenDownloadingException e) {
            throw new RuntimeException(e);
        }
    }

    public Stream<Dependency> stream(){
        return dependencies.stream()
                           .map(d -> Dependency.builder()
                                          .groupId(d.getGroupId())
                                          .artifactId(d.getArtifactId())
                                          .version(d.getVersion())
                                          .build()
                           );
    }


}
