/*
 * Copyright 2021 - 2022 the original author or authors.
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

import org.springframework.sbm.common.filter.DeletedResourcePathStringFilter;
import org.springframework.sbm.common.filter.ModifiedResourcePathStringFilter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextSerializer;
import org.springframework.sbm.engine.context.ProjectRootPathResolver;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.git.ProjectSyncVerifier;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.RecipesBuilder;
import org.springframework.sbm.project.parser.ProjectContextInitializer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplyCommand extends AbstractCommand<Recipe> {

    private final ProjectContextInitializer projectContextBuilder;

    private final ApplyCommandHelper applyCommandHelper;

    private final ProjectRootPathResolver projectRootPathResolver;

    private final RecipesBuilder recipesBuilder;

    private final ProjectContextSerializer contextSerializer;

    private final ProjectSyncVerifier projectSyncVerifier;

    private final GitSupport gitSupport;

    public ApplyCommand(
            ProjectContextInitializer projectContextBuilder,
            ApplyCommandHelper applyCommandHelper,
            ProjectRootPathResolver projectRootPathResolver, RecipesBuilder recipesBuilder, ProjectContextSerializer contextSerializer, ProjectSyncVerifier projectSyncVerifier, GitSupport gitSupport) {
        super("apply");
        this.projectContextBuilder = projectContextBuilder;
        this.applyCommandHelper = applyCommandHelper;
        this.projectRootPathResolver = projectRootPathResolver;
        this.recipesBuilder = recipesBuilder;
        this.contextSerializer = contextSerializer;
        this.projectSyncVerifier = projectSyncVerifier;
        this.gitSupport = gitSupport;
    }

    public List<Action> execute(ProjectContext projectContext, String recipeName) {
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
    }

    @Override
    @Deprecated
    public Recipe execute(String... arguments) {
//        if (arguments == null || arguments.length < 2) {
//            throw new IllegalArgumentException("Apply command needs project path (as first) and recipe name (as second) to be provided");
//        } else {
//            Path projectRoot = projectRootPathResolver.getProjectRootOrDefault(arguments[0]);
//            // FIXME: This triggers a new scan which shouldn't be required. Retrieve current ProjectContext from...? and use it here.
//            ProjectContext context = projectContextBuilder.initProjectContext(projectRoot, new RewriteExecutionContext());
//            return applyCommandHelper.applyRecipe(context, arguments[1]);
//        }
        return null;
    }

}
