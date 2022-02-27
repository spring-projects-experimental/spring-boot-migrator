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
package org.springframework.sbm.common.filter;

import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PathMatchingProjectResourceFilter implements ProjectResourceFinder<List<ProjectResource>> {

    /**
     * Ant-style path patterns to filter resources.
     */
    private final List<String> matchingPatterns;

    private final AntPathMatcher matcher = new AntPathMatcher();

    public PathMatchingProjectResourceFilter(List<String> matchingPatterns) {
        this.matchingPatterns = matchingPatterns;
    }

    public PathMatchingProjectResourceFilter(String... matchingPatterns) {
        this(Arrays.asList(matchingPatterns));
    }

    private boolean filterResources(ProjectResource projectResource) {
        return matchingPatterns.stream()
                .anyMatch(pattern -> matcher.match(pattern, projectResource.getAbsolutePath().toString()));
    }

    @Override
    public List<ProjectResource> apply(ProjectResourceSet projectResourceSet) {
        return projectResourceSet
                .stream()
                .filter(this::filterResources)
                .collect(Collectors.toList());
    }
}
