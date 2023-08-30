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
package org.springframework.sbm;

import org.openrewrite.maven.AddPluginDependency;
import org.openrewrite.maven.UpdateMavenModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.build.migration.conditions.AnyDeclaredDependencyExistMatchingRegex;
import org.springframework.sbm.build.migration.recipe.RemoveMavenPlugin;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.spring.migration.actions.OpenRewriteRecipeAdapterAction;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Configuration
public class OrRecipesConfig {


    public static Action createAction(org.openrewrite.Recipe r) {
        return new OpenRewriteRecipeAdapterAction(r);
    }

    public static Recipe createRecipe(String name, org.openrewrite.Recipe recipe, Condition condition) {
        return Recipe.builder()
                .name(name)
                .description(recipe.getDescription())
                .condition(condition)
                .actions(List.of(new OpenRewriteRecipeAdapterAction(recipe)))
                .build();
    }

    org.openrewrite.Recipe getRecipe(String clazz) {
        try {
            Class<?> aClass = Class.forName(clazz);
            return (org.openrewrite.Recipe) aClass.getConstructor(null).newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    Recipe recipeSpringBoot1To2Migration(RewriteRecipesRepository repo) {
        org.openrewrite.Recipe r = repo.getRecipe("org.openrewrite.java.spring.boot2.SpringBoot1To2Migration");
        
        r.doNext(new RemoveMavenPlugin("org.codehaus.mojo", "cobertura-maven-plugin"));
        r.doNext(new AddPluginDependency("ro.isdc.wro4j", "wro4j-maven-plugin", "org.mockito", "mockito-core", "${mockito.version}"));
        r.doNext(new GenericOpenRewriteRecipe<>(() -> new UpdateMavenModel<>()));

        AnyDeclaredDependencyExistMatchingRegex condition = new AnyDeclaredDependencyExistMatchingRegex();
        condition.setDependencies(List.of("org\\.springframework\\.boot:.*:1\\..*"));

        return createRecipe("upgrade-boot-1x-to-2x", r, condition);
    }
    
}
