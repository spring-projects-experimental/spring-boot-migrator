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
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RewriteRecipeRunner {
    private final RewriteMigrationResultMerger resultMerger;
    private final ExecutionContext executionContext;

    // FIXME: Make this a method 'apply(org.openrewrite.Recipe)' on ProjectContext, see https://github.com/spring-projects-experimental/spring-boot-migrator/issues/803
    public void run(ProjectContext context, Recipe recipe) {
        List<? extends SourceFile> rewriteSourceFiles = context.search(new OpenRewriteSourceFilesFinder());
        List<Result> results = recipe.run(new InMemoryLargeSourceSet(rewriteSourceFiles.stream().map(SourceFile.class::cast).toList()), executionContext).getChangeset().getAllResults();
        resultMerger.mergeResults(context, results);
    }

}
