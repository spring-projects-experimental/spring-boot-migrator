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

import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.recipe.Recipe;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplyShellCommandTest {

    @Mock
    private ApplyCommand applyCommand;

    @Mock
    private ApplyCommandRenderer applyCommandRenderer;

    @Mock
    private ProjectContextHolder projectContextHolder;

    @Mock
    private ApplicableRecipeListCommand applicableRecipeListCommand;

    @Mock
    private ApplicableRecipeListRenderer applicableRecipeListRenderer;

    @InjectMocks
    private ApplyShellCommand sut;

    @Test
    void testApply() {
        String recipeName = "recipe-1";
        AttributedString applyOutput = new AttributedString("applyOutput");
        List<Recipe> applicableRecipes = List.of();
        AttributedString applicableRecipesOutput = new AttributedString("applicableRecipesOutput");

        // TODO: test printing of header

        ProjectContext projectContext = mock(ProjectContext.class);
        when(projectContextHolder.getProjectContext()).thenReturn(projectContext);
        when(applyCommand.execute(projectContext, recipeName)).thenReturn(List.of());
        when(applyCommandRenderer.render(recipeName, List.of())).thenReturn(applyOutput);
        when(applicableRecipeListCommand.execute(projectContext)).thenReturn(applicableRecipes);
        when(applicableRecipeListRenderer.render(applicableRecipes)).thenReturn(applicableRecipesOutput);

        AttributedString result = sut.apply(recipeName);

        assertThat(result).isEqualTo(new AttributedStringBuilder()
                .append(applyOutput)
                .append(System.lineSeparator())
                .append(applicableRecipesOutput)
                .toAttributedString());
    }
}
