package org.springframework.sbm.engine.commands;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.SbmRecipeLoader;
import org.springframework.sbm.project.resource.TestProjectContext;

import javax.validation.Validator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = TestConfig.class)
class ApplyCommandTest {

    @Autowired
    private ApplyCommand target;

    /*Needed to load project context successfully*/
    @MockBean
    private Validator validator;

    @MockBean
    private SbmRecipeLoader recipeLoader;

    @Test
    public void someActionsAreNotApplicable () {

        Action applicableAction = mock(Action.class);
        Action nonApplicableAction = mock(Action.class);

        List<Recipe> recipes = List.of(Recipe.builder()
                        .name("hello")
                        .actions(List.of(applicableAction, nonApplicableAction)).build());

        when(recipeLoader.loadRecipes()).thenReturn(recipes);

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(
                        """
                                package hello;
                                class Temp {
                                }
                                """
                )
                .withDummyRootBuildFile()
                .build();

        when(applicableAction.isApplicable(projectContext)).thenReturn(true);
        when(nonApplicableAction.isApplicable(projectContext)).thenReturn(false);

        List<Action> appliedActions = target.execute(projectContext, "hello");

        assertThat(appliedActions).hasSize(1);
        assertThat(appliedActions.get(0)).isEqualTo(applicableAction);
    }
}
