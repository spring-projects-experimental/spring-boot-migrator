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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OpenRewriteNamedRecipeAdapterTest {
    @Mock
    RewriteRecipeLoader rewriteRecipeLoader;

    @Mock
    RewriteRecipeRunner rewriteRecipeRunner;

    @InjectMocks
    OpenRewriteNamedRecipeAdapter sut;

    @Test
    void testApply() {
        String recipeName = "name";
        sut.setOpenRewriteRecipeName(recipeName);

        org.openrewrite.Recipe recipe = mock(org.openrewrite.Recipe.class);
        when(rewriteRecipeLoader.loadRewriteRecipe(recipeName)).thenReturn(recipe);
        ProjectContext context = mock(ProjectContext.class);

        sut.apply(context);

        verify(rewriteRecipeRunner).run(context, recipe);
    }

}
