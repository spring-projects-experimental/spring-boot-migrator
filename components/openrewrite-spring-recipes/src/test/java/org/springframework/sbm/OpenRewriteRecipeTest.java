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
package org.springframework.sbm;

import org.springframework.sbm.engine.recipe.UserInteractions;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.engine.recipe.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class OpenRewriteRecipeTest {

    @SpringBootApplication
    public static class TestApp {
    }

    @Autowired
    RewriteRecipesRepository rewriteRecipesRepo;

    @MockBean
    UserInteractions ui;

    @Test
    void retrieveOpenRewriteRecipeFromClasspath() {
        try {
            Class<?> aClass = Class.forName("org.openrewrite.java.format.AutoFormat");
            assertThat(aClass).isNotNull();

            org.openrewrite.Recipe recipe = (org.openrewrite.Recipe) aClass.getConstructor(null).newInstance();
            assertThat(recipe).isNotNull();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getClasspathJars() {
        String[] property = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        Arrays.stream(property).forEach(System.out::println);
    }

    @Test
    void recipesRepo() {
        assertThat(rewriteRecipesRepo.getRecipe("org.openrewrite.java.format.AutoFormat")).isNotNull();
    }

    @Test
    void mockRecipeWithTrue() {
        ProjectContext projectContext = mock(ProjectContext.class);
        Recipe recipe = mockedOrRecipe(Condition.TRUE);
        assertThat(recipe.isApplicable(projectContext)).isTrue();
    }

    @Test
    void mockRecipeWithFalse() {
        ProjectContext projectContext = mock(ProjectContext.class);
        Recipe recipe = mockedOrRecipe(Condition.FALSE);
        assertThat(recipe.isApplicable(projectContext)).isFalse();
    }

    private Recipe mockedOrRecipe(Condition c) {
        return OrRecipesConfig.createRecipe("the-name", mock(org.openrewrite.Recipe.class), c);
    }

}
