package org.springframework.sbm.boot.upgrade_27_30;


import org.jetbrains.annotations.NotNull;
import org.openrewrite.*;
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
    public String getDisplayName() {
        return "Extends CrudRepository for Interfaces that extends PagingAndSortingRepository";
    }

    @Override
    protected @Nullable TreeVisitor<?, ExecutionContext> getApplicableTest() {
        return new JavaIsoVisitor<>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext executionContext) {
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
    protected JavaIsoVisitor<ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext executionContext) {

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
