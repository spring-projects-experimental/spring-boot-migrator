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

import org.openrewrite.SourceFile;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class AbsolutePathResourcesFinder implements ProjectResourceFinder<List<RewriteSourceFileHolder<? extends SourceFile>>> {

    private final Set<Path> absoluteResourcePaths;
    public AbsolutePathResourcesFinder(List<Path> absoluteResourcePaths) {
        this(new HashSet<>(absoluteResourcePaths));
    }
    public AbsolutePathResourcesFinder(Path... absoluteResourcePath) {
        this(Arrays.asList(absoluteResourcePath));
    }
    public AbsolutePathResourcesFinder(Path absoluteResourcePath) {
        this(Set.of(absoluteResourcePath));
    }
    public AbsolutePathResourcesFinder(Set<Path> absoluteResourcePaths) {
        String invalidPaths = absoluteResourcePaths.stream()
                .filter(absoluteResourcePath -> absoluteResourcePath == null || !absoluteResourcePath.isAbsolute())
                .map(p -> {
                    if(p == null) {
                        return "null";
                    } else  {
                        return p.toString();
                    }
                })
                .collect(Collectors.joining("', '"));

        if(invalidPaths != null) {
            throw new IllegalArgumentException("Given paths '"+ invalidPaths +"' were not absolute");
        }

        this.absoluteResourcePaths = absoluteResourcePaths.stream().map(Path::normalize).collect(Collectors.toSet());
    }

    @Override
    public List<RewriteSourceFileHolder<? extends SourceFile>> apply(ProjectResourceSet projectResourceSet) {
        return projectResourceSet
                .stream()
                .filter(r -> absoluteResourcePaths.contains(r.getAbsolutePath()))
                .collect(Collectors.toList());
    }
}
