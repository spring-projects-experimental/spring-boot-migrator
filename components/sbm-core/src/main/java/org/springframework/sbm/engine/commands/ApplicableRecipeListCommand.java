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

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectRootPathResolver;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.Recipes;
import org.springframework.sbm.engine.recipe.RecipesBuilder;
import org.springframework.sbm.project.RewriteExecutionContextFactory;
import org.springframework.sbm.project.parser.ProjectContextInitializer;
import org.springframework.sbm.scopeplayground.ExecutionScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicableRecipeListCommand extends AbstractCommand<List<Recipe>> {

    private static final String COMMAND_NAME = "applicableRecipes";
    private final ProjectRootPathResolver projectRootPathResolver;
    private final RecipesBuilder recipesBuilder;
    private final ProjectContextInitializer projectContextBuilder;

    private final ConfigurableListableBeanFactory beanFactory;

    private final ExecutionScope executionScope;

    protected ApplicableRecipeListCommand(ProjectRootPathResolver projectRootPathResolver, RecipesBuilder recipesBuilder, ProjectContextInitializer projectContextBuilder, ConfigurableListableBeanFactory beanFactory, ExecutionScope executionScope,
                                          RewriteExecutionContextFactory rewriteExecutionContextFactory) {
        super(COMMAND_NAME);
        this.projectRootPathResolver = projectRootPathResolver;
        this.recipesBuilder = recipesBuilder;
        this.projectContextBuilder = projectContextBuilder;
        this.beanFactory = beanFactory;
        this.executionScope = executionScope;
        this.rewriteExecutionContextFactory = rewriteExecutionContextFactory;
    }

    private RewriteExecutionContextFactory rewriteExecutionContextFactory;

    public List<Recipe> execute(ProjectContext projectContext) {
        return getApplicableRecipes(projectContext);
    }

    private List<Recipe> getApplicableRecipes(ProjectContext context) {
        Recipes recipes = recipesBuilder.buildRecipes();
        return recipes.getApplicable(context);
    }

    @Override
    @Deprecated
    // FIXME: Refactor: inheriting AbstractCommand forces this method!
    public List<Recipe> execute(String... arguments) {
//        Path projectRoot = projectRootPathResolver.getProjectRootOrDefault(arguments[0]);
//        // FIXME: This call creates a new ProjectResourceSet which is not correct.
//        ProjectContext context = projectContextBuilder.initProjectContext(projectRoot, new RewriteExecutionContext());
//        return getApplicableRecipes(context);
        return null;
    }
}
