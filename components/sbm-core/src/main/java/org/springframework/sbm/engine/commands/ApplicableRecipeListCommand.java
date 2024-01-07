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
package org.springframework.sbm.engine.commands;

import org.springframework.rewrite.scopes.ExecutionScope;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.ApplicableRecipesListHolder;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.Recipes;
import org.springframework.sbm.engine.recipe.RecipesBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicableRecipeListCommand extends AbstractCommand<List<Recipe>> {

    private static final String COMMAND_NAME = "applicableRecipes";
    private final RecipesBuilder recipesBuilder;
    private final ApplicableRecipesListHolder applicableRecipesListHolder;

    protected ApplicableRecipeListCommand(RecipesBuilder recipesBuilder, ExecutionScope executionScope, ApplicableRecipesListHolder applicableRecipesListHolder) {
        super(COMMAND_NAME);
        this.recipesBuilder = recipesBuilder;
        this.applicableRecipesListHolder = applicableRecipesListHolder;
    }

    public List<Recipe> execute(ProjectContext projectContext) {
        return getApplicableRecipes(projectContext);
    }

    private List<Recipe> getApplicableRecipes(ProjectContext context) {
        applicableRecipesListHolder.clear();
        Recipes recipes = recipesBuilder.buildRecipes();
        List<Recipe> applicable = recipes.getApplicable(context);
        applicableRecipesListHolder.setRecipes(applicable);
        return applicable;
    }

    @Override
    @Deprecated
    // FIXME: Refactor: inheriting AbstractCommand forces this method!
    public List<Recipe> execute(String... arguments) {
//        // FIXME: This call creates a new ProjectResourceSet which is not correct.
        return null;
    }
}
