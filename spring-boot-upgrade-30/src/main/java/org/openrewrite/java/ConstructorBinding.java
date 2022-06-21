package org.openrewrite.java;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.isolated.ReloadableJava17ParserVisitor;
import org.openrewrite.java.tree.J;

public class ConstructorBinding extends Recipe {
    @Override
    public String getDisplayName() {
        return "fdskjhgkfjdhgf fkjdh kdhfjklsdhf l;";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new RemoveAnnotationVisitor(new AnnotationMatcher("ConstructorBinding"));
    }
}
