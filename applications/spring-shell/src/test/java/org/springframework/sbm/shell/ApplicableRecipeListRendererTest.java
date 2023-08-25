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
package org.springframework.sbm.shell;

import org.springframework.sbm.engine.recipe.Recipe;
import org.jline.utils.AttributedString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicableRecipeListRendererTest {

    @Mock
    private RecipeRenderer recipeRenderer;

    @InjectMocks
    private ApplicableRecipeListRenderer sut;

    @Test
    void testRendererWithApplicableRecipes() {
        AttributedString renderedRecipesList = new AttributedString("");
        List<Recipe> recipeList = Collections.emptyList();
        when(recipeRenderer.renderRecipesList("No applicable recipes found.", "Applicable recipes:", recipeList)).thenReturn(renderedRecipesList);
        AttributedString result = sut.render(recipeList);
        assertThat(result).isSameAs(renderedRecipesList);
    }

}