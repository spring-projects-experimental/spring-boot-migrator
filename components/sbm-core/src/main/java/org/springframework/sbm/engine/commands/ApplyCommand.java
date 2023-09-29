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

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.sbm.project.resource.finder.DeletedResourcePathStringFilter;
import org.springframework.sbm.project.resource.finder.ModifiedResourcePathStringFilter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextSerializer;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.git.ProjectSyncVerifier;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.RecipesBuilder;
import org.springframework.sbm.scopes.ExecutionScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplyCommand extends AbstractCommand<Recipe> {

    private final RecipesBuilder recipesBuilder;

    private final ProjectContextSerializer contextSerializer;

    private final ProjectSyncVerifier projectSyncVerifier;

    private final GitSupport gitSupport;
    private final ConfigurableListableBeanFactory beanFactory;
    private final ExecutionScope executionScope;

    public ApplyCommand(
            RecipesBuilder recipesBuilder,
            ProjectContextSerializer contextSerializer,
            ProjectSyncVerifier projectSyncVerifier,
            GitSupport gitSupport,
            ConfigurableListableBeanFactory beanFactory, ExecutionScope executionScope) {
        super("apply");
        this.recipesBuilder = recipesBuilder;
        this.contextSerializer = contextSerializer;
        this.projectSyncVerifier = projectSyncVerifier;
        this.gitSupport = gitSupport;
        this.beanFactory = beanFactory;
        this.executionScope = executionScope;
    }

    public List<Action> execute(ProjectContext projectContext, String recipeName) {
        try {
            Recipe recipe = recipesBuilder.buildRecipes().getRecipeByName(recipeName)
                    .orElseThrow(() -> new IllegalArgumentException("Recipe with name '" + recipeName + "' could not be found"));

            // verify that project sources are in sync with in memory representation
            projectSyncVerifier.rescanWhenProjectIsOutOfSyncAndGitAvailable(projectContext);

            List<Action> appliedActions = recipe.apply(projectContext);

            // verify that project sources didn't change while running recipe
            projectSyncVerifier.verifyProjectIsInSyncWhenGitAvailable(projectContext);

            List<String> modifiedResources = projectContext.search(new ModifiedResourcePathStringFilter());

            List<String> deletedResources = projectContext.search(new DeletedResourcePathStringFilter());

            contextSerializer.writeChanges(projectContext);

            gitSupport.commitWhenGitAvailable(projectContext, recipeName, modifiedResources, deletedResources);

            return appliedActions;
        } finally {
            executionScope.clear(beanFactory);
        }
    }

    @Override
    @Deprecated
    public Recipe execute(String... arguments) {
        throw new UnsupportedOperationException("This method will be removed.");
    }
}
