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

import org.openrewrite.ExecutionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.core.NamedThreadLocal;
import org.springframework.sbm.common.filter.DeletedResourcePathStringFilter;
import org.springframework.sbm.common.filter.ModifiedResourcePathStringFilter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextSerializer;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.git.ProjectSyncVerifier;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.RecipesBuilder;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.scopeplayground.RecipeRuntimeScope;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ApplyCommand extends AbstractCommand<Recipe> {

    private final RecipesBuilder recipesBuilder;

    private final ProjectContextSerializer contextSerializer;

    private final ProjectSyncVerifier projectSyncVerifier;

    private final GitSupport gitSupport;
    private final ConfigurableBeanFactory beanFactory;

    public ApplyCommand(
            RecipesBuilder recipesBuilder,
            ProjectContextSerializer contextSerializer,
            ProjectSyncVerifier projectSyncVerifier,
            GitSupport gitSupport,
            AbstractBeanFactory beanFactory) {
        super("apply");
        this.recipesBuilder = recipesBuilder;
        this.contextSerializer = contextSerializer;
        this.projectSyncVerifier = projectSyncVerifier;
        this.gitSupport = gitSupport;
        this.beanFactory = beanFactory;
    }

    public List<Action> execute(ProjectContext projectContext, String recipeName) {
        try {
            // initialize the(!) ExecutionContext
            // It will be available through DI in all objects involved while this method runs (scoped to recipe run)
            beanFactory.destroyScopedBean("scopedTarget.executionContext");
            ExecutionContext execution = beanFactory.getBean(ExecutionContext.class);
//            System.out.println("Context in execute: " + execution.getMessage("contextId"));
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
//            beanFactory.getRegisteredScope("recipeScope").remove("executionContext");

            beanFactory.destroyScopedBean("scopedTarget.executionContext");
//            beanFactory.destroyScopedBean("executionContext");

//            System.out.println(beanFactory.getRegisteredScope("recipeScope"));
//            RecipeRuntimeScope recipeScope = (RecipeRuntimeScope) beanFactory.getRegisteredScope("recipeScope");
//            Field threadScope = ReflectionUtils.findField(RecipeRuntimeScope.class, "threadScope", ThreadLocal.class);
//            ReflectionUtils.makeAccessible(threadScope);
//            Object threadScope2 = ReflectionUtils.getField(threadScope, "threadScope");
//            HashMap threadScope1 = (HashMap) ((NamedThreadLocal) threadScope2).get();

//            ((ExecutionContext)((Map)((NamedThreadLocal)recipeScope.threadScope).get()).get("scopedTarget.executionContext")).getMessage("id");
        }
    }

    @Override
    @Deprecated
    public Recipe execute(String... arguments) {
        return null;
    }
}
