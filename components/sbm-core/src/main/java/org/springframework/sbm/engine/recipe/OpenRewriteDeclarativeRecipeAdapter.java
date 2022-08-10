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

package org.springframework.sbm.engine.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.openrewrite.config.DeclarativeRecipe;
import org.openrewrite.config.YamlResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenRewriteDeclarativeRecipeAdapter extends AbstractAction {
    @Setter
    @Getter
    private String openRewriteRecipe;

    @JsonIgnore
    @Autowired
    private OpenRewriteRecipeRunner openRewriteRecipeRunner;

    @Override
    public boolean isApplicable(ProjectContext context) {
        return super.isApplicable(context);
    }

    @Override
    public void apply(ProjectContext context) {
        ByteArrayInputStream yamlInput = new ByteArrayInputStream(openRewriteRecipe.getBytes(StandardCharsets.UTF_8));
        URI source = URI.create("embedded-recipe");
        YamlResourceLoader yamlResourceLoader = new YamlResourceLoader(yamlInput, source, new Properties());
        Collection<Recipe> rewriteYamlRecipe = yamlResourceLoader.listRecipes();

        rewriteYamlRecipe.stream()
                .filter(DeclarativeRecipe.class::isInstance)
                .map(DeclarativeRecipe.class::cast)
                .forEach(this::initializeRecipe);

        if(rewriteYamlRecipe.size() != 1) {
            throw new RuntimeException(String.format("Ambiguous number of recipes found. Expected exactly one, found %s", rewriteYamlRecipe.size()));
        }
        Recipe recipe = rewriteYamlRecipe.iterator().next();
        openRewriteRecipeRunner.run(context, recipe);
    }

    private void initializeRecipe(DeclarativeRecipe recipe) {
        Method initialize = ReflectionUtils.findMethod(DeclarativeRecipe.class, "initialize", Collection.class);
        ReflectionUtils.makeAccessible(initialize);
        try {
            initialize.invoke(recipe, List.of(recipe));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
