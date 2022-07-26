package org.springframework.sbm.boot.upgrade_27_30;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class TestRecipe extends Recipe {
    final static Set<String> crudMethodNames = Set.of("save", "saveAll", "findById", "existsById", "findAllById", "count");

    @Override
    @NotNull
    public String getDisplayName() {
        return "Test";
    }

    @Override
    protected @Nullable TreeVisitor<?, ExecutionContext> getApplicableTest() {
        return super.getApplicableTest();
    }



    @Override
    @NotNull
    protected JavaIsoVisitor<ExecutionContext> getVisitor() {

        return new JavaIsoVisitor<>() {
            @Override
            @NotNull
            public J.ClassDeclaration visitClassDeclaration(@NotNull J.ClassDeclaration classDecl, @NotNull ExecutionContext executionContext) {
                return super.visitClassDeclaration(classDecl, executionContext);
            }

            @Override
            public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method, ExecutionContext executionContext) {
                boolean pagingAndSortingRepository = ((J.Identifier) method.getSelect()).getType().isAssignableFrom(Pattern.compile("org.springframework.data.repository.PagingAndSortingRepository"));
                if (pagingAndSortingRepository && crudMethodNames.contains(method.getSimpleName())) {
                    doNext(new CrudRepositoryExtension(
                            "org.springframework.data.repository.PagingAndSortingRepository",
                            "org.springframework.data.repository.CrudRepository"));
                }
                return super.visitMethodInvocation(method, executionContext);
            }
        };
    }
}
