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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;

import java.util.List;

@Slf4j
public class OpenRewriteRecipeAdapterAction extends AbstractAction {

    private final Recipe recipe;

    @JsonIgnore
    @Autowired
    private RewriteMigrationResultMerger resultMerger;
    @JsonIgnore
    @Autowired
    private ExecutionContext executionContext;

    @Override
    public boolean isApplicable(ProjectContext context) {
        return true;
        // FIXME: use getApplicableTest and getSingleSourceApplicableTest to calculate
        /*Method getApplicableTest = ReflectionUtils.findMethod(Recipe.class, "getApplicableTest");
        ReflectionUtils.makeAccessible(getApplicableTest);
        try {
            TreeVisitor<?, ExecutionContext> visitor = (TreeVisitor<?, ExecutionContext>) getApplicableTest.invoke(recipe);
            if(visitor == null) {
                return true;
            } else {
                List<SourceFile> search = context.search(new OpenRewriteSourceFilesFinder());
                return visitor.visit(search, new InMemoryExecutionContext());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }*/
    }

    public OpenRewriteRecipeAdapterAction(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String getDescription() {
        return recipe.getDescription().isEmpty() ? recipe.getDisplayName() : recipe.getDescription();
    }
    
    @Override
    public void apply(ProjectContext context) {
        List<? extends SourceFile> rewriteSourceFiles = context.search(new OpenRewriteSourceFilesFinder());
        InMemoryLargeSourceSet largeSourceSet = new InMemoryLargeSourceSet(rewriteSourceFiles.stream().map(SourceFile.class::cast).toList());
        List<Result> results = recipe.run(largeSourceSet, executionContext).getChangeset().getAllResults();
        resultMerger.mergeResults(context, results);
    }


}