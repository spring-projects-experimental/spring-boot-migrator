package org.springframework.sbm.engine.commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectRootPathResolver;
import org.springframework.sbm.engine.recipe.ApplicableRecipesListHolder;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.Recipes;
import org.springframework.sbm.engine.recipe.RecipesBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Fabian Krüger
 */
@ExtendWith(MockitoExtension.class)
class ApplicableRecipeListCommandTest {
    @Mock
    ProjectRootPathResolver projectRootPathResolver;

    @Mock
    RecipesBuilder recipesBuilder;

    @Mock
    ApplicableRecipesListHolder applicableRecipesListHolder;

    @InjectMocks
    ApplicableRecipeListCommand sut;

    @Test
    @DisplayName("Sequence of Calls")
    void sequenceOfCallsInExecute() {
        Recipes recipes = mock(Recipes.class);
        ProjectContext context = mock(ProjectContext.class);
        when(recipesBuilder.buildRecipes()).thenReturn(recipes);
        List<Recipe> applicableRecipes = new ArrayList<>();
        when(recipes.getApplicable(context)).thenReturn(applicableRecipes);

        List<Recipe> returnedListOfRecipes = sut.execute(context);

        verify(applicableRecipesListHolder).setRecipes(applicableRecipes);
        verify(applicableRecipesListHolder).clear();

        assertThat(returnedListOfRecipes).isSameAs(applicableRecipes);
    }
}