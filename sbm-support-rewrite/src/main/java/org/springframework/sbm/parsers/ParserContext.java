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
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.util.List;

/**
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
public class ParserContext {

    @Getter
    private final List<Resource> resources;
    @Getter
    private final List<SbmMavenProject> sortedProjects;

    public List<String> getActiveProfiles() {
        // FIXME: Add support for Maven profiles
        return List.of("default");
    }

    public Resource getMatchingBuildFileResource(SbmMavenProject pom) {
        return resources.stream()
                .filter(r -> ResourceUtil.getPath(r).toString().equals(pom.getPomFilePath().toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find a resource in the list of resources that matches the path of SbmMavenProject '%s'".formatted(pom.getPomFile().toString())));
    }

    public List<Resource> getBuildFileResources() {
        return sortedProjects.stream()
                .map(p -> p.getPomFile())
                .toList();
    }
}
