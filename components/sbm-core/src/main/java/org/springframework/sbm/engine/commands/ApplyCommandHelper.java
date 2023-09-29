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

import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.git.ProjectSyncVerifier;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.RecipesBuilder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextSerializer;
import org.springframework.sbm.project.resource.finder.DeletedResourcePathStringFilter;
import org.springframework.sbm.project.resource.finder.ModifiedResourcePathStringFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Deprecated(forRemoval = true)
@Component
@RequiredArgsConstructor
public class ApplyCommandHelper {

    private final RecipesBuilder recipesBuilder;

    private final ProjectContextSerializer contextSerializer;

    private final ProjectSyncVerifier projectSyncVerifier;

    private final GitSupport gitSupport;


    Recipe applyRecipe(ProjectContext context, String recipeName) {

        Recipe recipe = recipesBuilder.buildRecipes().getRecipeByName(recipeName)
                .orElseThrow(() -> new IllegalArgumentException("Recipe with name '" + recipeName + "' could not be found"));

        // verify that project sources are in sync with in memory representation
        projectSyncVerifier.rescanWhenProjectIsOutOfSyncAndGitAvailable(context);

        recipe.apply(context);

        // verify that project sources didn't change while running recipe
        projectSyncVerifier.verifyProjectIsInSyncWhenGitAvailable(context);

        List<String> modifiedResources = context.search(new ModifiedResourcePathStringFilter());

        List<String> deletedResources = context.search(new DeletedResourcePathStringFilter());

        contextSerializer.writeChanges(context);

        gitSupport.commitWhenGitAvailable(context, recipeName, modifiedResources, deletedResources);

        return recipe;
    }

}
