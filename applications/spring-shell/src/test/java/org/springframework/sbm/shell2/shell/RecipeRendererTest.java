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
import org.springframework.sbm.engine.recipe.RecipeAutomation;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeRendererTest {

    RecipeRenderer sut = new RecipeRenderer();

    @Test
    void shouldRenderRecipeWithManualEmoji() {

        Recipe recipe = mock(Recipe.class);
        String recipeName = "recipe-1";
        String recipeDescription = "the description";

        when(recipe.getName()).thenReturn(recipeName);
        when(recipe.getDescription()).thenReturn(recipeDescription);
        when(recipe.getAutomationInfo()).thenReturn(RecipeAutomation.MANUAL);


        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.style(AttributedStyle.DEFAULT);
        builder.append("  - ");
        builder.style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("yellow")));
        builder.append(recipe.getName());
        builder.style(AttributedStyle.DEFAULT);
        builder.append(" [" + RecipeRenderer.MANUAL_EMOJI + "]");
        builder.append("\n     -> " + recipe.getDescription());
        builder.append("\n");

                AttributedStringBuilder builder2 = new AttributedStringBuilder();
        assertThat(sut.buildRecipePresentation(builder2, recipe).toAttributedString())
                .isEqualTo(builder.toAttributedString());
    }

    @Test
    void shouldRenderRecipeWithAutomatedEmoji() {

        Recipe recipe = mock(Recipe.class);
        String recipeName = "recipe-1";
        String recipeDescription = "the description";

        when(recipe.getName()).thenReturn(recipeName);
        when(recipe.getDescription()).thenReturn(recipeDescription);
        when(recipe.getAutomationInfo()).thenReturn(RecipeAutomation.AUTOMATED);

        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.style(AttributedStyle.DEFAULT);
        builder.append("  - ");
        builder.style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("yellow")));
        builder.append(recipe.getName());
        builder.style(AttributedStyle.DEFAULT);
        builder.append(" [" + RecipeRenderer.AUTOMATED_EMOJI + "]");
        builder.append("\n     -> " + recipe.getDescription());
        builder.append("\n");

        AttributedStringBuilder builder2 = new AttributedStringBuilder();
        assertThat(sut.buildRecipePresentation(builder2, recipe).toAttributedString())
                .isEqualTo(builder.toAttributedString());
    }

    @Test
    void shouldRenderRecipeWithPartiallyAutomatedEmoji() {

        Recipe recipe = mock(Recipe.class);
        String recipeName = "recipe-1";
        String recipeDescription = "the description";

        when(recipe.getName()).thenReturn(recipeName);
        when(recipe.getDescription()).thenReturn(recipeDescription);
        when(recipe.getAutomationInfo()).thenReturn(RecipeAutomation.PARTIALLY_AUTOMATED);

        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.style(AttributedStyle.DEFAULT);
        builder.append("  - ");
        builder.style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("yellow")));
        builder.append(recipe.getName());
        builder.style(AttributedStyle.DEFAULT);
        builder.append(" [" + RecipeRenderer.MANUAL_EMOJI + " " + RecipeRenderer.AUTOMATED_EMOJI + "]");
        builder.append("\n     -> " + recipe.getDescription());
        builder.append("\n");

        AttributedStringBuilder builder2 = new AttributedStringBuilder();
        assertThat(sut.buildRecipePresentation(builder2, recipe).toAttributedString())
                .isEqualTo(builder.toAttributedString());
    }

    @Test
    void shouldRenderListOfRecipesWhenNotEmpty() {
        RecipeRenderer sut = spy(RecipeRenderer.class);

        AttributedString emojiMapping = AttributedString.fromAnsi("emoji mapping");
        String title = "title";
        AttributedString recipe1Name = AttributedString.fromAnsi("recipe1");
        AttributedString recipe2Name = AttributedString.fromAnsi("recipe2");
        Recipe recipe1 = new Recipe(recipe1Name.toString(), Collections.emptyList());
        Recipe recipe2 = new Recipe(recipe2Name.toString(), Collections.emptyList());

        AttributedStringBuilder builder = new AttributedStringBuilder();

        when(sut.renderEmojiMapping()).thenReturn(emojiMapping);
        sut.buildRecipePresentation(builder, recipe1);
        sut.buildRecipePresentation(builder, recipe2);


        AttributedString attributedString = sut.renderRecipesList("", title, List.of(recipe1, recipe2));

        assertThat(attributedString.toString()).contains(emojiMapping.toString(), title, recipe1Name, recipe2Name);
    }

    @Test
    void shouldRenderMessageWhenListOfRecipesEmpty() {
        RecipeRenderer sut = new RecipeRenderer();
        String noRecipesTitle = "";
        AttributedString attributedString = sut.renderRecipesList(noRecipesTitle, "", Collections.emptyList());
        assertThat(attributedString.toString()).contains(noRecipesTitle);
    }
}