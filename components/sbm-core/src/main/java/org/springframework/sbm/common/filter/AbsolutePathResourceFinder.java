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
package org.springframework.sbm.common.filter;

import lombok.RequiredArgsConstructor;
import org.openrewrite.SourceFile;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;

import java.nio.file.Path;
import java.util.Optional;

@RequiredArgsConstructor
public class AbsolutePathResourceFinder implements ProjectResourceFinder<Optional<RewriteSourceFileHolder<? extends SourceFile>>> {

    private final Path absoluteResourcePath;

    @Override
    public Optional<RewriteSourceFileHolder<? extends SourceFile>> apply(ProjectResourceSet projectResourceSet) {
        if (absoluteResourcePath == null || ! absoluteResourcePath.isAbsolute()) {
            throw new IllegalArgumentException("Given path '"+absoluteResourcePath+"' is not absolute");
        }
        Path searchForPath = absoluteResourcePath.normalize();
        return projectResourceSet
                .stream()
                .filter(r -> searchForPath.equals(r.getAbsolutePath()))
                .findFirst();

    }
}
