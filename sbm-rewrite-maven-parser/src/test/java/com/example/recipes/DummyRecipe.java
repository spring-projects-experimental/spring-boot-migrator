package com.example.recipes;

import org.openrewrite.Recipe;

public class DummyRecipe extends Recipe {

    @Override
    public String getDisplayName() {
        return "Dummy Recipe in Java";
    }

    @Override
    public String getDescription() {
        return "Some dummy recipe for tests";
    }
}