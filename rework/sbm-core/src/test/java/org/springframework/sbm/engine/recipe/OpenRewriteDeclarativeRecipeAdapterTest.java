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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.sbm.engine.context.ProjectContext;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OpenRewriteDeclarativeRecipeAdapterTest {

    @Mock
    RewriteRecipeLoader rewriteRecipeLoader;

    @Mock
    RewriteRecipeRunner rewriteRecipeRunner;

    @InjectMocks
    OpenRewriteDeclarativeRecipeAdapter sut;

    @Test
    void testApply() {
        String recipeDeclaration = "name: some-recipe";
        sut.setOpenRewriteRecipe(recipeDeclaration);

        org.openrewrite.Recipe recipe = mock(org.openrewrite.Recipe.class);
        when(rewriteRecipeLoader.createRecipe(recipeDeclaration)).thenReturn(recipe);
        ProjectContext context = mock(ProjectContext.class);

        sut.apply(context);

        verify(rewriteRecipeRunner).run(context, recipe);
    }


}
