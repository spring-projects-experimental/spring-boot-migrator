package com.example.recipes;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Tree;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;

public class DummyRecipeFromJar extends Recipe {
    @Override
    public java.lang.String getDisplayName() {
        return "PackagedDummyRecipeFromJar";
    }

    @Override
    public java.lang.String getDescription() {
        return "Some Packaged Dummy Recipe from Jar";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new TreeVisitor<Tree, ExecutionContext>() {
            @Nullable
            @Override
            public Tree visit(@Nullable Tree tree, ExecutionContext executionContext) {
                return super.visit(tree, executionContext);
            }
        };
    }
}