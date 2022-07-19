package org.openrewrite.java.spring.boot3;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.*;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import java.util.List;

public class CrudRepositoryExtension extends Recipe {
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
                String className = classDecl.getImplements().get(0).getType().toString();
                return className.equals("org.springframework.data.repository.PagingAndSortingRepository");
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

                List<JavaType.FullyQualified> interfaces = classDecl.getType().getInterfaces();
                List<JavaType> typeParameters = interfaces.get(0).getTypeParameters();
                doAfterVisit(new ImplementTypedInterface(classDecl, "org.springframework.data.repository.CrudRepository", typeParameters));
                return classDecl;
            }
        };
    }
}
