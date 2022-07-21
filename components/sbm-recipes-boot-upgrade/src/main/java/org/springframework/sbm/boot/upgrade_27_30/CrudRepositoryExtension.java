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
package org.springframework.sbm.boot.upgrade_27_30;


import org.jetbrains.annotations.NotNull;
import org.openrewrite.*;
import org.openrewrite.internal.lang.NonNullApi;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.springframework.sbm.boot.upgrade_27_30.helperrecipe.ImplementTypedInterface;

import java.util.List;
import java.util.Optional;

public class CrudRepositoryExtension extends Recipe {
    public static final String PAGING_AND_SORTING_REPOSITORY = "org.springframework.data.repository.PagingAndSortingRepository";
    public static final String CRUD_REPOSITORY = "org.springframework.data.repository.CrudRepository";

    @Override
    @NotNull
    public String getDisplayName() {
        return "Extends CrudRepository for Interfaces that extends PagingAndSortingRepository";
    }

    @Override
    protected @Nullable TreeVisitor<?, ExecutionContext> getApplicableTest() {
        return new JavaIsoVisitor<>() {
            @Override
            @NotNull
            public J.ClassDeclaration visitClassDeclaration(@NotNull J.ClassDeclaration classDecl, @NotNull ExecutionContext executionContext) {
                return doesItExtendPagingAndSorting(classDecl) ? applyThisRecipe(classDecl) : ceaseVisit(classDecl);
            }

            private boolean doesItExtendPagingAndSorting(J.ClassDeclaration classDecl) {
                if (classDecl.getType() == null) {
                    return false;
                }
                return classDecl.getType().getInterfaces().stream()
                        .anyMatch(impl -> impl.getFullyQualifiedName().equals(PAGING_AND_SORTING_REPOSITORY));
            }

            private J.ClassDeclaration ceaseVisit(J.ClassDeclaration classDecl) {
                return classDecl;
            }

            @NotNull
            private J.ClassDeclaration applyThisRecipe(J.ClassDeclaration classDecl) {
                return classDecl.withMarkers(classDecl.getMarkers().searchResult());
            }
        };
    }

    @Override
    @NotNull
    protected JavaIsoVisitor<ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<>() {
            @Override
            @NotNull
            public J.ClassDeclaration visitClassDeclaration(@NotNull J.ClassDeclaration classDecl, @NotNull ExecutionContext executionContext) {

                Optional<JavaType.FullyQualified> pagingInterface = getExtendPagingAndSorting(classDecl);
                if (pagingInterface.isEmpty()) {
                    return classDecl;
                }
                List<JavaType> typeParameters = pagingInterface.get().getTypeParameters();
                doAfterVisit(new ImplementTypedInterface<>(classDecl, CRUD_REPOSITORY, typeParameters));
                return classDecl;
            }

            private Optional<JavaType.FullyQualified> getExtendPagingAndSorting(J.ClassDeclaration classDecl) {
                if (classDecl.getType() == null) {
                    return Optional.empty();
                }
                return classDecl.getType().getInterfaces().stream()
                        .filter(impl -> impl.getFullyQualifiedName().equals(PAGING_AND_SORTING_REPOSITORY))
                        .findAny();
            }
        };

    }
}
