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
package org.springframework.sbm.java.refactoring;

import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.tree.J;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JavaGlobalRefactoringImpl implements JavaGlobalRefactoring {
    private ProjectResourceSet projectResourceSet;

    public JavaGlobalRefactoringImpl(ProjectResourceSet projectResourceSet) {
        this.projectResourceSet = projectResourceSet;
    }


    @Override
    public void refactor(JavaVisitor<ExecutionContext>... visitors) {
        List<J.CompilationUnit> compilationUnits = getAllCompilationUnits().stream().map(RewriteSourceFileHolder::getSourceFile).collect(Collectors.toList());
        Arrays.stream(visitors)
                .map(v -> new GenericOpenRewriteRecipe(() -> v))
                .map(r -> executeRecipe(compilationUnits, r))
                .forEach(r -> processResults(compilationUnits, r));
    }

    private List<RewriteSourceFileHolder<J.CompilationUnit>> getAllCompilationUnits() {
        return projectResourceSet.stream()
                .filter(rsfh -> J.CompilationUnit.class.isAssignableFrom(rsfh.getSourceFile().getClass()))
                .map(this::cast)
                .collect(Collectors.toList());
    }

    private RewriteSourceFileHolder<J.CompilationUnit> cast(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        if (J.CompilationUnit.class.isAssignableFrom(rewriteSourceFileHolder.getSourceFile().getClass())) {
            return (RewriteSourceFileHolder<J.CompilationUnit>) rewriteSourceFileHolder;
        } else {
            throw new ClassCastException(String.format("Given type '%s<%s>' is not of type %s<%s>",
                    rewriteSourceFileHolder.getSourceFile(),
                    rewriteSourceFileHolder.getSourceFile().getClass(),
                    RewriteSourceFileHolder.class.getSimpleName(),
                    J.CompilationUnit.class.getSimpleName()));
        }
    }


    @Override
    public void refactor(Recipe... recipes) {
        List<J.CompilationUnit> compilationUnits = getAllCompilationUnits().stream().map(r -> r.getSourceFile()).collect(Collectors.toList());
        for (Recipe recipe : recipes) {
            List<Result> results = executeRecipe(compilationUnits, recipe);
            processResults(compilationUnits, results);
        }
    }

    @Override
    public List<RewriteSourceFileHolder<J.CompilationUnit>> find(Recipe recipe) {
        return findInternal(getAllCompilationUnits(), recipe);
    }

    @NotNull
    protected List<RewriteSourceFileHolder<J.CompilationUnit>> findInternal(List<RewriteSourceFileHolder<J.CompilationUnit>> resourceWrappers, Recipe recipe) {
        List<J.CompilationUnit> compilationUnits = resourceWrappers.stream().map(RewriteSourceFileHolder::getSourceFile).collect(Collectors.toList());
        List<Result> results = executeRecipe(compilationUnits, recipe);
        return results.stream()
                .map(r -> r.getAfter())
                .filter(r -> J.CompilationUnit.class.isAssignableFrom(r.getClass()))
                .map(J.CompilationUnit.class::cast)
                .map(cu -> resourceWrappers.stream()
                        .filter(fh -> fh.getId().equals(cu.getId()))
                        .map(pr -> (RewriteSourceFileHolder<J.CompilationUnit>) pr)
                        .findAny().orElseThrow())
                .collect(Collectors.toList());
    }

    @Deprecated
    void processResults(List<J.CompilationUnit> compilationUnits, List<Result> changes) {
        if (!changes.isEmpty()) {
            changes.forEach(c -> processResult(c));
        }
    }

    void processResults(List<Result> changes) {
        if (!changes.isEmpty()) {
            changes.forEach(c -> processResult(c));
        }
    }

    private void processResult(Result result) {
        UUID id = result.getBefore().getId();

        RewriteSourceFileHolder<J.CompilationUnit> match = findRewriteSourceFileHolderHoldingCompilationUnitWithId(id);

        match.replaceWith((J.CompilationUnit) result.getAfter());
    }

    private RewriteSourceFileHolder<J.CompilationUnit> findRewriteSourceFileHolderHoldingCompilationUnitWithId(UUID id) {
        return projectResourceSet.stream()
                .filter(pr -> pr.getSourceFile().getId().equals(id))
                .filter(pr -> J.CompilationUnit.class.isAssignableFrom(pr.getSourceFile().getClass()))
                .map(pr -> (RewriteSourceFileHolder<J.CompilationUnit>) pr)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Not matching modification found"));
    }

    List<Result> executeRecipe(List<J.CompilationUnit> compilationUnits, Recipe recipe) {
        // FIXME #7 added RewriteExecutionContext here, remove again?
        List<Result> results = recipe.run(compilationUnits, new RewriteExecutionContext());
//         List<Result> results = recipe.run(compilationUnits, new RewriteExecutionContext(), new ForkJoinScheduler(new ForkJoinPool(1)), 10, 1);
        return results;
    }

//    List<Result> executeRecipe(List<RewriteSourceFileHolder<J.CompilationUnit>> modifiableCompilationUnits, Recipe recipe) {
//        List<J.CompilationUnit> cus = modifiableCompilationUnits.stream()
//                .map(RewriteSourceFileHolder::getRewriteResource)
//                .map(J.CompilationUnit.class::cast)
//                .collect(Collectors.toList());
//        List<Result> results = recipe.run(cus, new RewriteExecutionContext());
//        return results;
//    }
}
