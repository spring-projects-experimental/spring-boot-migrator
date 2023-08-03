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

import lombok.RequiredArgsConstructor;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.graph.GraphBuilder;
import org.apache.maven.model.building.Result;
import org.apache.maven.project.MavenProject;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Uses Mavens {@link GraphBuilder} to build the graph of Maven projects from the list of {@link Resource}s.
 * <p>
 * Internally a Maven application context is created and an instance of {@link GraphBuilder} is retrieved from the container.
 * The {@link GraphBuilder} is then  class requires the provided resources to exist on filesystem.
 * <p>
 * TODO: Check if GraphBuilder uses to active profiles
 *
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
class MavenBuildFileGraph implements BuildFileGraph {

    private final MavenPlexusContainer mavenPlexusContainer;

    @Override
    public SortedProjects build(List<Resource> resources, MavenSession mavenSession) {
        GraphBuilder graphBuilder = mavenPlexusContainer.lookup(GraphBuilder.class);
        Result<? extends ProjectDependencyGraph> result = graphBuilder.build(mavenSession);
        List<MavenProject> allProjects = result.get().getSortedProjects();
        List<String> defaultProfiles = List.of("default");
        return new SortedProjects(resources, allProjects, defaultProfiles);
    }

}
