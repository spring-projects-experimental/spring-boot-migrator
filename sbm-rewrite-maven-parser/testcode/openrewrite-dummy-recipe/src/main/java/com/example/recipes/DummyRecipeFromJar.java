package com.example.recipes;

import org.openrewrite.Recipe;

public class DummyRecipeFromJar extends Recipe {

    @Override
    public java.lang.String getDisplayName() {
        return "Packaged Dummy Recipe from Jar";
    }

    @Override
    public java.lang.String getDescription() {
        return "Some Packaged Dummy Recipe from Jar";
    }
}