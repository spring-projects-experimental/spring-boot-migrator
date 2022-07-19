package org.openrewrite.java.spring.boot3;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.java.ImplementInterface;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TextComment;
import org.openrewrite.marker.Markers;

import java.util.ArrayList;
import java.util.List;

public class CrudRepositoryExtension extends Recipe {
    @Override
    public String getDisplayName() {
        return "ldsfhgkjfdhgkjfdhg";
    }

    @Override
    protected JavaIsoVisitor<ExecutionContext> getVisitor() {
        System.out.println("HELLIO");
        return new JavaIsoVisitor<ExecutionContext>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext executionContext) {
                List<JavaType> typeParameters = classDecl.getType().getInterfaces().get(0).getTypeParameters();
                doAfterVisit(new ImplementTypedInterface(classDecl, "org.springframework.data.repository.CrudRepository", typeParameters));
                return classDecl;
            }
        };
    }
}
