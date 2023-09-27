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
package org.springframework.sbm.parsers;

import lombok.Getter;
import org.apache.maven.model.Model;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.nio.file.Path;
import java.util.List;

/**
 * Helper class
 *
 * TODO: Make class independant to Maven and find better name.
 * TODO: Can this class be used to transport build information from Maven and Gradle to reuse functionality?
 *
 * @author Fabian Krüger
 */
public class SortedProjects {
    private final List<Resource> resources;
    @Getter
    private final List<SbmMavenProject> sortedProjects;
    @Getter
    private final List<String> activeProfiles;



    // FIXME: The relation between resource and project is brittle, if it's really needed we should validate in constructor
    public SortedProjects(List<Resource> given, List<SbmMavenProject> allProjects, List<String> activeProfiles) {
        this.resources = given;
        this.sortedProjects = allProjects;
        this.activeProfiles = activeProfiles;
    }

    public List<Resource> getResources() {
        return sortedProjects
                .stream()
                .map(SbmMavenProject::getPomFilePath)
                .map(m -> this.findResourceWithPath(m, resources))
                .toList();
    }

    private Resource findResourceWithPath(Path m, List<Resource> resources) {
        return resources.stream()
                .filter(r -> ResourceUtil.getPath(r).equals(m))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find a resource in the list of resources that matches the path of pom '%s'".formatted(m.toString())));
    }

    public Resource getMatchingBuildFileResource(SbmMavenProject pom) {
        return resources.stream()
                .filter(r -> ResourceUtil.getPath(r).toString().equals(pom.getPomFilePath().toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find a resource in the list of resources that matches the path of SbmMavenProject '%s'".formatted(pom.getPomFile().toString())));
    }

    private List<String> readActiveProfiles(Model topLevelModel) {
        return activeProfiles;
    }

    public SbmMavenProject getMavenProject(Resource r) {
        Path path = ResourceUtil.getPath(r);
        return sortedProjects.stream()
                .filter(p -> p.getFile().getPath().toString().equals(path.toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find SbmMavenProject for given resource '%s'".formatted(path)));
    }
}
