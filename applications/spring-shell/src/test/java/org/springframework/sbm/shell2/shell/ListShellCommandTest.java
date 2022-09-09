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

import org.springframework.sbm.engine.commands.ListCommand;
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
class ListShellCommandTest {
    @Mock
    ListCommand listCommand;

    @Mock
    ListCommandRenderer listCommandRenderer;

    @InjectMocks
    ListShellCommand sut;

    @Test
    void testList() {
        List<Recipe> recipesList = List.of(mock(Recipe.class), mock(Recipe.class));
        when(listCommand.execute()).thenReturn(recipesList);

        AttributedString attributedString = new AttributedStringBuilder().toAttributedString();
        when(listCommandRenderer.render(recipesList)).thenReturn(attributedString);

        AttributedString result = sut.list();

        assertThat(result).isEqualTo(attributedString);
    }
}