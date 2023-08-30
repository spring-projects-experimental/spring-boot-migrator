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
package org.springframework.sbm.engine.recipe;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.sbm.project.resource.ResourceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RecipeParser {

    private final YAMLMapper yamlObjectMapper;

    private final CustomValidator customValidator;

    private final ResourceHelper resourceHelper;

    public Recipe[] parseRecipe(Resource recipeDefinition) {
        try {
            String resourceString = resourceHelper.getResourceAsString(recipeDefinition);
            return parseRecipe(resourceString);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read recipes from resource '" + recipeDefinition.getFilename() + "'", e);
        }
    }

    public Recipe[] parseRecipe(String recipesStr) throws IOException {
        Recipe[] recipes;
        recipes = yamlObjectMapper.readValue(recipesStr, Recipe[].class);
        Arrays.stream(recipes)
                .forEach(customValidator::validate);
        return recipes;
    }

}
