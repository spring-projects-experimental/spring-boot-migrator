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
package org.springframework.sbm.engine.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.ActionDeserializer;
import org.springframework.sbm.search.recipe.actions.OpenRewriteJavaSearchAction;
import org.openrewrite.Recipe;
import org.openrewrite.config.YamlResourceLoader;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class RewriteJavaSearchActionDeserializer implements ActionDeserializer {// extends StdDeserializer<FrameworkSupportAction> {

    protected final ObjectMapper yamlObjectMapper;
    protected final AutowireCapableBeanFactory beanFactory;

    @Override
    public Action deserialize(ObjectMapper tolerantObjectMapper, Class<? extends Action> actionClass, JsonNode node, AutowireCapableBeanFactory parser) {
        JsonNode rewriteRecipeDefinition = node.get("rewriteRecipeDefinition");
        if (rewriteRecipeDefinition == null) {
            throw new RuntimeException("Could not find attribute 'rewriteRecipeDefinition' in YAML for Action '" + actionClass + "'.");
        }
        YamlResourceLoader yamlResourceLoader = new YamlResourceLoader(new ByteArrayInputStream(rewriteRecipeDefinition.toString().getBytes(StandardCharsets.UTF_8)), URI.create("recipe.yaml"), new Properties());
        Collection<Recipe> recipes = yamlResourceLoader.listRecipes();
        Action action = yamlObjectMapper.convertValue(node, actionClass);
        OpenRewriteJavaSearchAction search = (OpenRewriteJavaSearchAction) action;
        search.setRewriteRecipe(recipes.iterator().next());
        return search;
    }

    @Override
    public boolean canHandle(Class<? extends Action> actionClass) {
        return OpenRewriteJavaSearchAction.class.isAssignableFrom(actionClass);
    }
}
