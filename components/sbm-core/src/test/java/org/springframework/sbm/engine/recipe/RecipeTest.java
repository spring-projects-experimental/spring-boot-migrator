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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.engine.context.ProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeTest {
    @Mock
    Action applicableAction;
    @Mock
    Action notApplicableAction;
    @Mock
    ProjectContext projectContext;

    @Test
    void shouldReturnOnlyApplicableActions() {
        when(applicableAction.isApplicable(any(ProjectContext.class))).thenReturn(true);
        when(notApplicableAction.isApplicable(any(ProjectContext.class))).thenReturn(false);
        Recipe testRecipe = new Recipe(
                "test recipe",
                List.of(applicableAction, notApplicableAction),
                Condition.TRUE,
                0);

        List<Action> actionList = testRecipe.apply(projectContext);

        assertThat(actionList).hasSize(1);
        assertThat(actionList).contains(applicableAction);
    }

    @Test
    void shouldReturnOnlyActionsWhichHaveSuccessfullyBeingApplied() {

        when(applicableAction.isApplicable(any(ProjectContext.class))).thenReturn(true);
        when(notApplicableAction.isApplicable(any(ProjectContext.class))).thenReturn(false);

        final Action successAction = mock(Action.class);
        final Action failAction = mock(Action.class);

        when(successAction.isApplicable(any(ProjectContext.class))).thenReturn(true);
        when(failAction.isApplicable(any(ProjectContext.class))).thenReturn(true);

        Recipe testRecipe = new Recipe(
                "test recipe",
                List.of(
                        applicableAction,
                        notApplicableAction,
                        successAction,
                        failAction
                ),
                Condition.TRUE,
                0);


        doThrow(new RuntimeException("")).when(failAction).applyWithStatusEvent(any());

        assertThrows(RuntimeException.class, () -> testRecipe.apply(projectContext));
    }
}
