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

import lombok.RequiredArgsConstructor;
import org.openrewrite.SourceFile;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RewriteSourceFileHolderListFinder<T extends SourceFile> implements ProjectResourceFinder<List<RewriteSourceFileHolder<T>>> {

    private final Class<T> wrappedType;

    @Override
    public List<RewriteSourceFileHolder<T>> apply(ProjectResourceSet projectResourceSet) {
        return projectResourceSet.stream()
                .filter(r -> wrappedType.isAssignableFrom(r.getSourceFile().getClass()))
                .map(this::cast)
                .collect(Collectors.toList());
    }

    private RewriteSourceFileHolder<T> cast(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        return (RewriteSourceFileHolder<T>) rewriteSourceFileHolder;
    }
}
