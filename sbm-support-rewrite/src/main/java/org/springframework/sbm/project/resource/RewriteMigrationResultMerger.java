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
package org.springframework.sbm.project.resource;

import lombok.RequiredArgsConstructor;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.finder.AbsolutePathResourceFinder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RewriteMigrationResultMerger {

    private final RewriteSourceFileWrapper surceFileWrapper;

    public void mergeResults(ProjectResourceSet resourceSet, List<Result> results) {
        results.forEach(result -> {
            SourceFile after = result.getAfter();
            SourceFile before = result.getBefore();
            if (after == null) {
                handleDeleted(resourceSet, before);
            } else if (before == null) {
                handleAdded(resourceSet, after);
            } else {
                handleModified(resourceSet, after);
            }
        });
    }

    private void handleDeleted(ProjectResourceSet resourceSet, SourceFile before) {
        Path path = resourceSet.list().get(0).getAbsoluteProjectDir().resolve(before.getSourcePath());
        Optional<RewriteSourceFileHolder<? extends SourceFile>> match = new AbsolutePathResourceFinder(path).apply(resourceSet);
        match.get().delete();
    }

    private void handleAdded(ProjectResourceSet resourceSet, SourceFile after) {
        RewriteSourceFileHolder<? extends SourceFile> modifiableProjectResource = surceFileWrapper.wrapRewriteSourceFiles(resourceSet.list().get(0).getAbsoluteProjectDir(), List.of(after)).get(0);
        resourceSet.add(modifiableProjectResource);
    }

    private void handleModified(ProjectResourceSet resourceSet, SourceFile after) {
        Path absoluteProjectDir = resourceSet.list().get(0).getAbsoluteProjectDir();
        Path resolve = absoluteProjectDir.resolve(after.getSourcePath());
        Optional<RewriteSourceFileHolder<? extends SourceFile>> modifiedResource = new AbsolutePathResourceFinder(resolve).apply(resourceSet);
        if(modifiedResource.isEmpty()) {
            throw new IllegalStateException("Could not find resource matching path '%s'".formatted(resolve));
        }
        // TODO: handle situations where resource is not rewriteSourceFileHolder -> use predicates for known types to reuse, alternatively using the ProjectContextBuiltEvent might help
        replaceWrappedResource(modifiedResource.get(), after);
    }

    private <T extends SourceFile> void replaceWrappedResource(RewriteSourceFileHolder<T> resource, SourceFile r) {
        Class<? extends SourceFile> type = resource.getType();
        resource.replaceWith((T) type.cast(r));
    }
}
