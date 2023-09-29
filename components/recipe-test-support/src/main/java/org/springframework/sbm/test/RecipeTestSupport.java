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
package org.springframework.sbm.test;

import org.springframework.sbm.boot.autoconfigure.ScopeConfiguration;
import org.springframework.sbm.build.impl.MavenBuildFileRefactoringFactory;
import org.springframework.sbm.build.impl.MavenSettingsInitializer;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.context.RewriteJavaSearchActionDeserializer;
import org.springframework.sbm.engine.recipe.*;
import org.springframework.sbm.java.impl.RewriteJavaParser;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.ProjectResourceSetHolder;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.scopes.ExecutionScope;
import org.springframework.sbm.scopes.ScanScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.Validator;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Helper class for testing <code>Recipe</code> definitions in yaml files.
 */
public class RecipeTestSupport {

    private RecipeTestSupport() {
    }

    private static final Class<?>[] springBeans = {
            SbmApplicationProperties.class,
            RecipesBuilder.class,
            ResourceHelper.class,
            RecipeParser.class,
            RewriteJavaParser.class,
            CustomValidator.class,
            ValidatorConfiguration.class,
            YamlObjectMapperConfiguration.class,
            ResourceHelperDummy.class,
            ActionDeserializerRegistry.class,
            MultiModuleAwareActionDeserializer.class,
            DefaultActionDeserializer.class,
            RewriteJavaSearchActionDeserializer.class,
            RewriteRecipeLoader.class,
            MigrationResultProjectContextMerger.class,
            RewriteSourceFileWrapper.class,
            SbmRecipeLoader.class,
            BasePackageCalculator.class,
            ProjectContextHolder.class,
            RewriteMavenParser.class,
            MavenSettingsInitializer.class,
            MavenBuildFileRefactoringFactory.class,
            ProjectResourceSetHolder.class,
            ScopeConfiguration.class,
            ExecutionScope.class,
            ScanScope.class
    };


    /**
     * Helper to test <code>Recipe</code>s.
     *
     * @param recipeFile            Path to the recipe yaml file
     * @param consumer              Consumer called with the <code>Recipes</code> that contains the <code>Recipe</code>
     * @param additionalSpringBeans optional additional Spring components
     */
    public static void testRecipe(Path recipeFile, Consumer<Recipes> consumer, Class<?>... additionalSpringBeans) {
        List<Class<?>> allSpringBeans = new ArrayList<>();
        allSpringBeans.addAll(List.of(springBeans));
        allSpringBeans.addAll(List.of(additionalSpringBeans));

        SpringBeanProvider.run(context -> {
            context.start();
            RecipesBuilder recipesBuilder = context.getBean(RecipesBuilder.class);
            ResourceHelperDummy resourceHelperDummy = context.getBean(ResourceHelperDummy.class);
            resourceHelperDummy.setRecipe(recipeFile);
            Recipes recipes = recipesBuilder.buildRecipes();
            consumer.accept(recipes);
        }, allSpringBeans.toArray(new Class[]{}));
    }

    /**
     * Get the list of <code>Action</code> names in the same order as in the recipe.
     *
     * @param recipe
     */
    public static List<String> getActionNames(Recipe recipe) {
        return recipe.getActions().stream()
                .map(a -> a.getClass().getName())
                .collect(Collectors.toList());
    }

    /**
     * Assert that the <code>Optional<Recipe></code> is not empty.
     *
     * @param optionalRecipe
     */
    public static void assertThatRecipeExists(Optional<Recipe> optionalRecipe) {
        assertThat(optionalRecipe.isPresent()).isTrue();
    }


    /**
     * Assert that the given <code>Action</code>s are defined in this order in the <code>Recipe</code> under test.
     * Additionally it check that all given types implement <code>Action</code>.
     *
     * @param recipe  the <code>Recipe</code> to test
     * @param actions the <code>Action</code>s expected in this order
     */
    public static void assertThatRecipeHasActions(Optional<Recipe> recipe, Class... actions) {
        List<Class> classes = recipe.get().getActions().stream().map(Action::getClass).collect(Collectors.toList());
        assertThat(classes).containsExactly(actions);
        assertThat(classes).extracting(aClass -> aClass.isAssignableFrom(Action.class));
    }

    public static void assertThatRecipeHasCondition(Optional<Recipe> optionalRecipe, Class<?> conditionClass) {
        Recipe recipe = optionalRecipe.get();
        assertThat(recipe.getCondition().getClass()).isSameAs(conditionClass);
    }

    public static <T> T getAction(Optional<Recipe> recipe, Class<T> actionClass) {
        return (T) recipe.get().getActions()
                .stream()
                .filter(a -> a.getClass().equals(actionClass))
                .findFirst()
                .get();
    }

    public static <T> List<T> getActions(Optional<Recipe> recipe, Class<T> actionClass) {
        return (List<T>) recipe.get().getActions()
                .stream()
                .filter(a -> a.getClass().equals(actionClass))
                .collect(Collectors.toList());
    }

    public static void assertThatActionHasCondition(Action action, Class<? extends Condition> conditionClass) {
        assertThat(action.getCondition()).isInstanceOf(conditionClass);
    }

    public static <T extends Condition> T getConditionFor(Action action, Class<T> conditionClass) {
        return conditionClass.cast(action.getCondition());
    }

    public static <T> T getConditionFor(Optional<Recipe> recipe, Class<T> conditionClass) {
        return (T) recipe.get().getCondition();
    }

    @Configuration
    static class ValidatorConfiguration {
        @Bean
        Validator validator() {
            LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
            return localValidatorFactoryBean;
        }
    }

    @Component
    @Primary
    private static class ResourceHelperDummy extends ResourceHelper {

        private Path recipeFile;

        public ResourceHelperDummy(ResourceLoader resourceLoader) {
            super(null);
        }

        @Override
        public Resource[] loadResources(String pattern) {
            if(recipeFile != null) {
                return super.loadResources("classpath:" + recipeFile.toString());
            } else {
                 return super.loadResources(pattern);
            }
        }

        public void setRecipe(Path recipeFile) {
            this.recipeFile = recipeFile;
        }
    }
}
