package org.springframework.sbm.engine.recipe;

import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;
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

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RewriteRecipeRunner {
    @Autowired
    private RewriteMigrationResultMerger resultMerger;

    public void run(ProjectContext context, Recipe recipe) {
        List<? extends SourceFile> rewriteSourceFiles = context.search(new OpenRewriteSourceFilesFinder());
        List<Result> results = recipe.run(rewriteSourceFiles, new InMemoryExecutionContext(
                (t) -> {
                    throw new RuntimeException(t);
                }
        )).getResults();
        resultMerger.mergeResults(context, results);
    }

}
