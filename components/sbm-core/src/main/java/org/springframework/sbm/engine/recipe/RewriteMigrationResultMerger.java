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

package org.springframework.sbm.engine.recipe;

import lombok.RequiredArgsConstructor;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.common.filter.AbsolutePathResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RewriteMigrationResultMerger {

    private final RewriteSourceFileWrapper surceFileWrapper;
    public void mergeResults(ProjectContext context, List<Result> results) {
        // TODO: handle added
        results.forEach(result -> {
            SourceFile after = result.getAfter();
            SourceFile before = result.getBefore();
            if (after == null) {
                handleDeleted(context, before);
            } else if (before == null) {
                handleAdded(context, after);
            } else {
                handleModified(context, after);
            }
        });
    }

    private void handleDeleted(ProjectContext context, SourceFile before) {
        Path path = context.getProjectRootDirectory().resolve(before.getSourcePath());
        RewriteSourceFileHolder<? extends SourceFile> filteredResources = context.search(new AbsolutePathResourceFinder(path)).get();
        filteredResources.delete();
    }

    private void handleModified(ProjectContext context, SourceFile after) {
        Path path = context.getProjectRootDirectory().resolve(after.getSourcePath());
        RewriteSourceFileHolder<? extends SourceFile> filteredResources = context.search(new AbsolutePathResourceFinder(path)).get();
        // TODO: handle situations where resource is not rewriteSourceFileHolder -> use predicates for known types to reuse, alternatively using the ProjectContextBuiltEvent might help
        replaceWrappedResource(filteredResources, after);
    }

    private void handleAdded(ProjectContext context, SourceFile after) {
        RewriteSourceFileHolder<? extends SourceFile> modifiableProjectResource = surceFileWrapper.wrapRewriteSourceFiles(context.getProjectRootDirectory(), List.of(after)).get(0);
        context.getProjectResources().add(modifiableProjectResource);
    }

    private <T extends SourceFile> void replaceWrappedResource(RewriteSourceFileHolder<T> resource, SourceFile r) {
        Class<? extends SourceFile> type = resource.getType();
        resource.replaceWith((T) type.cast(r));
    }
}
