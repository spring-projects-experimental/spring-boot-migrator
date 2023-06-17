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
package org.springframework.sbm.shell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipeRenderer {

    public static final String MANUAL_EMOJI = "\uD83E\uDDE0";

    public static final String AUTOMATED_EMOJI = "\uD83E\uDD16";

    public AttributedString renderRecipesList(String noRecipesTitle, String title, List<Recipe> foundRecipes) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        if (foundRecipes.isEmpty()) {
            builder.append(noRecipesTitle);
        } else {
            AttributedString titleString = renderTitle(title);
            builder.append(titleString);

            foundRecipes.forEach(recipe -> this.buildRecipePresentation(foundRecipes.indexOf(recipe), builder, recipe));

            builder.append("\n");
            builder.append("Run command '> apply <recipe-number>' to apply a recipe.");
            builder.append("\n");
        }
        return builder.toAttributedString();
    }

    public AttributedStringBuilder buildRecipePresentation(int index, AttributedStringBuilder builder, Recipe recipe) {
        builder.style(AttributedStyle.BOLD);//.foreground(Colors.rgbColor("yellow")));
        builder.append("  ").append(Integer.toString(index + 1)).append(") ");
        builder.append(recipe.getName());
        builder.style(AttributedStyle.DEFAULT);
        builder.append("\n     -> ").append(recipe.getDescription());
        builder.append("\n");
        return builder;
    }

    private AttributedString renderTitle(String title) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.style(AttributedStyle.DEFAULT.bold());
        builder.append("\n");
        builder.append(title);
        builder.append("\n\n");
        return builder.toAttributedString();
    }
}
