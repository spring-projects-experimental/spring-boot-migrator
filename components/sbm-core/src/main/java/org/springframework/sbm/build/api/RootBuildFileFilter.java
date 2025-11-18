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
package org.springframework.sbm.build.api;

import org.jetbrains.annotations.NotNull;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RootBuildFileFilter implements ProjectResourceFinder<BuildFile> {

    @Override
    public BuildFile apply(@NotNull ProjectResourceSet projectResourceSet) {
        // collect all build files (pom.xml, etc.)
        List<BuildFile> buildFiles = projectResourceSet.stream()
                .filter(pr -> BuildFile.class.isAssignableFrom(pr.getClass()))
                .map(BuildFile.class::cast)
                .collect(Collectors.toList());

        if (buildFiles.isEmpty()) {
            throw new RootBuildFileNotFoundException("Could not find any BuildFile in project.");
        }

        // 1st try: existing logic – respect explicit isRootBuildFile flag
        return buildFiles.stream()
                .filter(BuildFile::isRootBuildFile)
                .findFirst()
                // 2nd try (fallback): no explicit root → choose the build file
                // whose source path is closest to the project root (smallest depth)
                .orElseGet(() -> buildFiles.stream()
                        .min(Comparator.comparingInt(bf -> pathDepth(bf.getSourcePath())))
                        .orElseThrow(() -> new RootBuildFileNotFoundException("Could not find BuildFile for root module.")));
    }

    private int pathDepth(Path path) {
        // defensive: null check, though OpenRewrite usually always has a path
        return (path == null) ? Integer.MAX_VALUE : path.getNameCount();
    }
}