package com.example.recipes;

import org.openrewrite.Recipe;

public class DummyRecipe extends Recipe {

    @Override
    public java.lang.String getDisplayName() {
        return "Dummy Recipe";
    }

    @Override
    public java.lang.String getDescription() {
        return "Some dummy recipe for tests";
    }
}