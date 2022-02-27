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

import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.RecipeAutomation;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipeRenderer {

    public static final String MANUAL_EMOJI = "\uD83D\uDCAA";

    public static final String AUTOMATED_EMOJI = "\uD83E\uDD16";

    public AttributedString renderRecipesList(String noRecipesTitle, String title, List<Recipe> foundRecipes) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        if (foundRecipes.isEmpty()) {
            builder.append(noRecipesTitle);
        } else {

            AttributedString emojiMapping = renderEmojiMapping();
            builder.append(emojiMapping);
            foundRecipes.forEach(recipe -> this.buildRecipePresentation(builder, recipe));
            AttributedString titleString = renderTitle(title);
            builder.append(titleString);
            builder.append("\n\n");
        }
        return builder.toAttributedString();
    }

    public AttributedString renderEmojiMapping() {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.append(RecipeRenderer.AUTOMATED_EMOJI + "    = 'automated recipe'\n");
        builder.append(RecipeRenderer.MANUAL_EMOJI + " " + RecipeRenderer.AUTOMATED_EMOJI + " = 'partially automated recipe'\n");
        builder.append(RecipeRenderer.MANUAL_EMOJI + "    = 'manual recipe'\n\n");
        return builder.toAttributedString();
    }

    public AttributedStringBuilder buildRecipePresentation(AttributedStringBuilder builder, Recipe recipe) {
        builder.style(AttributedStyle.DEFAULT);
        builder.append("  - ");
        builder.style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("yellow")));
        builder.append(recipe.getName());
        builder.style(AttributedStyle.DEFAULT);
        builder.append(" [" + getAutomationEmoji(recipe.getAutomationInfo()) + "]");
        builder.append("\n     -> " + recipe.getDescription());
        builder.append("\n");
        return builder;
    }

    private AttributedString renderTitle(String title) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.style(AttributedStyle.DEFAULT.bold());
        builder.append(title);
        return builder.toAttributedString();
    }

    private String getAutomationEmoji(RecipeAutomation recipeAutomation) {
        switch (recipeAutomation) {
            case AUTOMATED:
                return AUTOMATED_EMOJI;
            case PARTIALLY_AUTOMATED:
                return MANUAL_EMOJI + " " + AUTOMATED_EMOJI;
            default:
                return MANUAL_EMOJI;
        }
    }
}
