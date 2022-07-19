package org.openrewrite.java.spring.boot3;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
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
