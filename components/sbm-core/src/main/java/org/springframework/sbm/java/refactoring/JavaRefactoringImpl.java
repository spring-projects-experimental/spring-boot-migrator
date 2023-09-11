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
package org.springframework.sbm.java.refactoring;

import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.openrewrite.GenericOpenRewriteRecipe;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.tree.J;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JavaRefactoringImpl extends JavaGlobalRefactoringImpl implements JavaRefactoring {

    @Deprecated
    public JavaRefactoringImpl(ProjectResourceSet projectResources, ExecutionContext executionContext) {
        super(projectResources, executionContext);
    }

    @Deprecated
    public JavaRefactoringImpl(ProjectResourceSet projectResourceSet, J.CompilationUnit compilationUnit, ExecutionContext executionContext) {
        super(projectResourceSet, executionContext);
    }

    @Deprecated
    public JavaRefactoringImpl(ProjectResourceSet projectResourceSet, RewriteSourceFileHolder<J.CompilationUnit> rewriteSourceFileHolder, ExecutionContext executionContext) {
        super(projectResourceSet, executionContext);
    }

    @Override
    public void refactor(RewriteSourceFileHolder<J.CompilationUnit> resourceWrapper, JavaVisitor<ExecutionContext>... visitors) {
        Recipe recipe = createRecipeChainFromVisitors(visitors);
        List<J.CompilationUnit> compilationUnits = List.of(resourceWrapper.getSourceFile());
        runRecipe(compilationUnits, recipe);
    }

    @Override
    public void refactor(RewriteSourceFileHolder<J.CompilationUnit> resourceWrapper, Recipe... recipes) {
        Recipe recipe = chainRecipes(List.of(recipes));
        List<J.CompilationUnit> compilationUnits = List.of(resourceWrapper.getSourceFile());
        runRecipe(compilationUnits, recipe);
    }

    @Override
    public List<RewriteSourceFileHolder<J.CompilationUnit>> find(RewriteSourceFileHolder<J.CompilationUnit> resourceWrapper, Recipe recipe) {
        return super.findInternal(List.of(resourceWrapper), recipe);
    }

    private void runRecipe(List<J.CompilationUnit> compilationUnits, Recipe recipe) {
        List<Result> results = executeRecipe(compilationUnits, recipe);
        processResults(results);
    }

    private Recipe createRecipeChainFromVisitors(JavaVisitor<ExecutionContext>[] visitors) {
        Recipe recipe = null;
        List<Recipe> recipes = Arrays.stream(visitors)
                .map(v -> new GenericOpenRewriteRecipe(() -> v))
                .collect(Collectors.toList());
        recipe = chainRecipes(recipes);
        return recipe;
    }

    private Recipe chainRecipes(List<Recipe> recipes) {
        Recipe recipe = null;
        if (!recipes.isEmpty()) {
            recipe = recipes.get(0);
            for (int i = 1; i < recipes.size(); i++) {
                recipe.doNext(recipes.get(i));
            }
        }
        return recipe;
    }


}
