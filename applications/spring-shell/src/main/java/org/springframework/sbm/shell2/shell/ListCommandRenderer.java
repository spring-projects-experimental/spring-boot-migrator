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
package org.springframework.sbm.shell2.shell;

import org.springframework.sbm.engine.recipe.Recipe;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListCommandRenderer {

    private final RecipeRenderer recipeRenderer;

    public AttributedString render(List<Recipe> foundRecipes) {
        return recipeRenderer.renderRecipesList("No recipes found.", "Found these recipes:", foundRecipes);
    }

    private void addTitle(AttributedStringBuilder builder, String title) {
        builder.style(AttributedStyle.DEFAULT.bold());
        builder.append(title);
    }
}
