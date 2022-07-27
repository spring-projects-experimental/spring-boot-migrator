package org.springframework.sbm.boot.upgrade_27_30;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.java.ImplementInterface;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.search.FindMethods;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.MethodCall;
import org.openrewrite.java.tree.TypeUtils;

import java.util.*;

public class ConditionTestRecipe extends Recipe {
    @Override
    public String getDisplayName() {
        return "MyConditionRecipe";
    }

    @Override
    protected List<SourceFile> visit(List<SourceFile> before, ExecutionContext ctx) {
        List<JavaType> types = new ArrayList<>();
        for (SourceFile sourceFile : before) {
            if (sourceFile instanceof J) {
                J j = (J) sourceFile;
                for (J found : find(j, "org.springframework.data.repository.PagingAndSortingRepository *(..)")) {
                    JavaType.Method methodType = ((MethodCall) found).getMethodType();
                    if (methodType != null) {
                        types.add(methodType.getDeclaringType());
                    }
                }
            }
        }

        return ListUtils.map(before, sourceFile -> (SourceFile) new JavaIsoVisitor<Integer>() {

            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, Integer p) {
                for (JavaType type : types) {
                    if (TypeUtils.isAssignableTo(type, classDecl.getType())) {
                        Optional<JavaType.FullyQualified> pagingInterface = getExtendPagingAndSorting(classDecl);
                        if (pagingInterface.isEmpty()) {
                            return classDecl;
                        }
                        List<JavaType> typeParameters = pagingInterface.get().getTypeParameters();
                        doAfterVisit(new ImplementTypedInterface<>(classDecl, "org.springframework.data.repository.CrudRepository", typeParameters));
                        return classDecl;
                    }
                }
                return super.visitClassDeclaration(classDecl, p);
            }
        }.visit(sourceFile, 0));
    }

    private Optional<JavaType.FullyQualified> getExtendPagingAndSorting(J.ClassDeclaration classDecl) {
        if (classDecl.getType() == null) {
            return Optional.empty();
        }
        return classDecl.getType().getInterfaces().stream()
                .filter(impl -> impl.getFullyQualifiedName().equals("org.springframework.data.repository.PagingAndSortingRepository"))
                .findAny();
    }

    public static Set<J> find(J j, String methodPattern) {
        final MethodMatcher methodMatcher = new MethodMatcher(methodPattern, true);
        JavaIsoVisitor<Set<J>> findVisitor = new JavaIsoVisitor<Set<J>>() {
            public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method, Set<J> ms) {
                if (methodMatcher.matches(method)) {
                    ms.add(method);
                }

                return super.visitMethodInvocation(method, ms);
            }

            public J.MemberReference visitMemberReference(J.MemberReference memberRef, Set<J> ms) {
                if (methodMatcher.matches(memberRef.getMethodType())) {
                    ms.add(memberRef);
                }

                return super.visitMemberReference(memberRef, ms);
            }
        };
        Set<J> ms = new HashSet();
        findVisitor.visit(j, ms);
        return ms;
    }
}
