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

import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.Scope;
import org.springframework.sbm.scopes.ProjectMetadata;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Fabian Krüger
 */
public class ClasspathRegistry {
    private final ClasspathExtractor classpathExtractor;
    private final Map<Path, MavenResolutionResult> map;

    public ClasspathRegistry(ClasspathExtractor classpathExtractor) {
        this.map = new HashMap<>();
        this.classpathExtractor = classpathExtractor;
    }

    public void registerClasspaths(Map<Path, MavenResolutionResult> map) {
        this.map.putAll(map);
    }

    public void registerClasspath(Path buildFilePath, MavenResolutionResult mavenResolutionResult) {
        this.map.put(buildFilePath, mavenResolutionResult);
    }

    public void clear() {
        this.map.clear();
    }

    public Set<Path> getClasspath(Path buildFileProjectPath, Scope scope) {
        if(!map.containsKey(buildFileProjectPath)) {
            throw new IllegalArgumentException("The given pom path '%s' does not define a classpath.".formatted(buildFileProjectPath));
        }
        MavenResolutionResult mavenResolutionResult = map.get(buildFileProjectPath);
        return classpathExtractor.extractClasspath(mavenResolutionResult, scope);
    }
}
