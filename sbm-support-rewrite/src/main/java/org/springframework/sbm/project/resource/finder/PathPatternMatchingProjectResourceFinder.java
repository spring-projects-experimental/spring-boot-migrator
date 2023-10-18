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
package org.springframework.sbm.project.resource.finder;

import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.utils.OsAgnosticPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PathPatternMatchingProjectResourceFinder implements ProjectResourceFinder<List<ProjectResource>> {

    /**
     * Ant-style path patterns to filter resources.
     */
    private final List<String> matchingPatterns;

    private final PathMatcher matcher = new OsAgnosticPathMatcher();

    public PathPatternMatchingProjectResourceFinder(List<String> matchingPatterns) {
        validateMatchingPatterns(matchingPatterns);
        this.matchingPatterns = matchingPatterns;
    }

    public PathPatternMatchingProjectResourceFinder(String... matchingPatterns) {
        this(Arrays.asList(matchingPatterns));
    }

    private void validateMatchingPatterns(List<String> matchingPatterns) {
        for(String pattern : matchingPatterns) {
            if( ! matcher.isPattern(pattern)) {
                throw new RuntimeException("The provided pattern '"+pattern+"' is invalid. Please check AntPathMatcher javadoc for examples of valid patterns.");
            }
        }
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
