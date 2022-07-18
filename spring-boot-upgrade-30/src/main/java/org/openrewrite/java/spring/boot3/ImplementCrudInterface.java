package org.openrewrite.java.spring.boot3;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.Comment;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.TextComment;
import org.openrewrite.marker.Markers;

import java.util.List;

public class ImplementCrudInterface extends Recipe {
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

                J.ClassDeclaration c = super.visitClassDeclaration(classDecl, executionContext);
                return c.withComments(List.of(new TextComment(false, "my comment", null, Markers.EMPTY)));
            }
        };
    }
}
