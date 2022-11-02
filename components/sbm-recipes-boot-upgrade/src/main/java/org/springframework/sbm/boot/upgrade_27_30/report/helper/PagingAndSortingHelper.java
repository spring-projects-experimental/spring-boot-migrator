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

package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSection;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PagingAndSortingHelper implements SpringBootUpgradeReportSection.Helper<List<String>> {
    private List<String> pagingAndSortingRepo;
    private List<String> reactivePagingAndSortingRepo;
    private List<String> rxJavaSortingRepo;

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        //CrudRepositoryExtension
        List<RewriteSourceFileHolder<J.CompilationUnit>> pagingAndSortingFileHolders =
                context.getProjectJavaSources().find(pagingAndSortingFinders("org.springframework.data.repository.PagingAndSortingRepository"));
        List<RewriteSourceFileHolder<J.CompilationUnit>> reactiveSortingFileHolders =
                context.getProjectJavaSources().find(pagingAndSortingFinders("org.springframework.data.repository.reactive.ReactiveSortingRepository"));
        List<RewriteSourceFileHolder<J.CompilationUnit>> rxJavaSortingFileHolders =
                context.getProjectJavaSources().find(pagingAndSortingFinders("org.springframework.data.repository.reactive.RxJava3SortingRepository"));

        pagingAndSortingRepo = pagingAndSortingFileHolders
                .stream()
                .map(k -> k.getAbsolutePath().toString()).toList();

        reactivePagingAndSortingRepo = reactiveSortingFileHolders.stream()
                .map(k -> k.getAbsolutePath().toString()).collect(Collectors.toList());

        rxJavaSortingRepo = rxJavaSortingFileHolders.stream()
                .map(k -> k.getAbsolutePath().toString()).collect(Collectors.toList());

        return !pagingAndSortingFileHolders.isEmpty()
                || !reactiveSortingFileHolders.isEmpty()
                || !rxJavaSortingFileHolders.isEmpty();
    }

    @NotNull
    private GenericOpenRewriteRecipe<JavaIsoVisitor<ExecutionContext>> pagingAndSortingFinders(String clazz) {
        return new GenericOpenRewriteRecipe<>(() -> new JavaIsoVisitor<>() {
            @Override
            @NotNull
            public J.ClassDeclaration visitClassDeclaration(@NotNull J.ClassDeclaration classDecl, @NotNull ExecutionContext executionContext) {
                return doesItExtendPagingAndSorting(classDecl) ? applyThisRecipe(classDecl) : ceaseVisit(classDecl);
            }

            private boolean doesItExtendPagingAndSorting(J.ClassDeclaration classDecl) {
                if (classDecl.getImplements() == null) {
                    return false;
                }
                return classDecl.getType().getInterfaces().stream()
                        .anyMatch(impl -> impl.getFullyQualifiedName().equals(clazz));
            }

            private J.ClassDeclaration ceaseVisit(J.ClassDeclaration classDecl) {
                return classDecl;
            }

            @NotNull
            private J.ClassDeclaration applyThisRecipe(J.ClassDeclaration classDecl) {
                return classDecl.withMarkers(classDecl.getMarkers().searchResult());
            }
        });
    }

    @Override
    public Map<String, List<String>> getData(ProjectContext context) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("pagingAndSortingRepos", pagingAndSortingRepo);
        map.put("reactivePagingAndSortingRepos", reactivePagingAndSortingRepo);
        map.put("rxJavaSortingRepos", rxJavaSortingRepo);

        return map;
    }
}
