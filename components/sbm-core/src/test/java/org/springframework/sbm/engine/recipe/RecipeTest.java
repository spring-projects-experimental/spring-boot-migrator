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
