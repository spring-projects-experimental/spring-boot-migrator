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
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.springframework.sbm.project.RewriteSourceFileWrapper;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
public class ProjectResourceSetFactory {
    private final RewriteMigrationResultMerger rewriteMigrationResultMerger;
    private final RewriteSourceFileWrapper sourceFileWrapper;
    private final ExecutionContext executionContext;


    public ProjectResourceSet create(Path baseDir, List<SourceFile> sourceFiles) {
        List<RewriteSourceFileHolder<? extends SourceFile>> rewriteSourceFileHolders = sourceFileWrapper.wrapRewriteSourceFiles(baseDir, sourceFiles);
        return createFromSourceFileHolders(rewriteSourceFileHolders);
    }

    public ProjectResourceSet createFromSourceFileHolders(List<RewriteSourceFileHolder<? extends SourceFile>> rewriteSourceFileHolders) {
        return new ProjectResourceSet(rewriteSourceFileHolders, executionContext, rewriteMigrationResultMerger);
    }
}
