package org.springframework.sbm.engine.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextSerializer;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.git.ProjectSyncVerifier;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.Recipes;
import org.springframework.sbm.engine.recipe.RecipesBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplyCommandTest {
    @Mock
    private RecipesBuilder recipesBuilder;
    @Mock
    private ProjectContextSerializer contextSerializer;
    @Mock
    private ProjectSyncVerifier projectSyncVerifier;
    @Mock
    private GitSupport gitSupport;
    @Mock
    Recipes recipes;
    @Mock
    private Recipe recipe;
    @Mock
    ProjectContext projectContext;
    @Mock
    Action action1;
    @Mock
    Action action2;

    @InjectMocks
    ApplyCommand applyCommand;

    @Test
    void shouldReturnActionList() {
        when(recipesBuilder.buildRecipes()).thenReturn(recipes);
        when(recipes.getRecipeByName("testRecipe")).thenReturn(Optional.of(recipe));
        when(recipe.apply(projectContext)).thenReturn(List.of(action1, action2));
        List<Action> actions = applyCommand.execute(projectContext, "testRecipe");

        assertThat(actions).hasSize(2);
        assertThat(actions).contains(action1).contains(action2);
    }
}
