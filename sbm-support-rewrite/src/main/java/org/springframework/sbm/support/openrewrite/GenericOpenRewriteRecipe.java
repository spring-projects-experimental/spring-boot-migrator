/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.support.openrewrite;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class GenericOpenRewriteRecipe<V extends TreeVisitor<?, ExecutionContext>> extends Recipe {

    private final Supplier<V> visitorSupplier;
    private final String description;
    private final List<Recipe> recipes;

    public GenericOpenRewriteRecipe() {
        description = null;
        visitorSupplier = null;
    }

    public GenericOpenRewriteRecipe(String description, Supplier<V> visitor) {
        this.visitorSupplier = visitor;
        this.description = description;
        this.recipes = new ArrayList<>();
    }

    public GenericOpenRewriteRecipe() {
        description = null;
        visitorSupplier = null;
        this.recipes = new ArrayList<>();
    }

    public GenericOpenRewriteRecipe(Supplier<V> visitor) {
        this("Executing visitor %s".formatted(visitor.get().getClass()), visitor);
    }

    public <V extends TreeVisitor<?, ExecutionContext>> GenericOpenRewriteRecipe(String description, Recipe... recipes) {
        this.recipes = Arrays.stream(recipes).toList();
        this.description = description;
        this.visitorSupplier = null;
    }

    @Override
    public List<Recipe> getRecipeList() {
        return this.recipes;
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return visitorSupplier.get();
    }

    @Override
    public String getDisplayName() {
        return visitorSupplier != null ? visitorSupplier.get().getClass().getSimpleName() : "???";
    }

    @Override
    public String getDescription() {
        return description;
    }
}
