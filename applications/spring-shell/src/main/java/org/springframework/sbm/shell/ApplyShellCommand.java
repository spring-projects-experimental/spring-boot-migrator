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
package org.springframework.sbm.shell;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.ApplicableRecipesListHolder;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.shell.Availability;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.*;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Integer.parseUnsignedInt;

@ShellComponent
@RequiredArgsConstructor
public class ApplyShellCommand {

    private final ApplyCommand applyCommand;
    private final ApplicableRecipeListCommand applicableRecipeListCommand;
    private final ApplicableRecipeListRenderer applicableRecipeListRenderer;
    private final ProjectContextHolder projectContextHolder;
    private final ApplyCommandRenderer applyCommandRenderer;
    private final ApplicableRecipesListHolder applicableRecipeListHolder;

    @ShellMethod(key = {"apply", "a"}, value = "Apply a given recipe to the target application.")
    @ShellMethodAvailability("availabilityCheck")
    public AttributedString apply(@ShellOption(arity = 1, valueProvider = ApplyRecipeValueProvider.class,
            help = "The number of the recipe to apply.") String recipe) {
        // allow passing in number of position or name of recipe (meh)
        String recipeName = recipe;
        try {
            int recipeIndex = parseUnsignedInt(recipe);
            int realIndex = recipeIndex - 1;
            Recipe matchingRecipe = applicableRecipeListHolder.getRecipeByIndex(realIndex);
            recipeName = matchingRecipe.getName();
        } catch (NumberFormatException nfe) {

        }
        AttributedStringBuilder header = buildHeader(recipeName);
        System.out.println(header.toAnsi());

        ProjectContext projectContext = projectContextHolder.getProjectContext();
        List<Action> appliedActions = applyCommand.execute(projectContext, recipeName);
        AttributedString applyCommandOutput = applyCommandRenderer.render(recipeName, appliedActions);

        List<Recipe> applicableRecipes = applicableRecipeListCommand.execute(projectContext);
        AttributedString applicableRecipesOutput = applicableRecipeListRenderer.render(applicableRecipes);

        return new AttributedStringBuilder()
                .append(applyCommandOutput)
                .append(System.lineSeparator())
                .append(applicableRecipesOutput)
                .toAttributedString();
    }

    @NotNull
    private AttributedStringBuilder buildHeader(String recipeName) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.append("Applying recipe ");
        builder.style(AttributedStyle.DEFAULT.boldDefault());
        builder.append("'")
                .append(recipeName)
                .append("'");
        return builder;
    }

    public Availability availabilityCheck() {
        if (projectContextHolder.getProjectContext() != null) {
            return Availability.available();
        } else {
            return Availability.unavailable("You need to scan first");
        }
    }
}

@Component
@RequiredArgsConstructor
class ApplyRecipeValueProvider implements ValueProvider {

    private final ProjectContextHolder projectContextHolder;
    private final ApplicableRecipeListCommand applicableRecipeListCommand;

    @Override
    public List<CompletionProposal> complete(CompletionContext completionContext) {
        ProjectContext projectContext = projectContextHolder.getProjectContext();
        List<Recipe> applicableRecipes = applicableRecipeListCommand.execute(projectContext);

        return applicableRecipes.stream()
                .map(Recipe::getName)
                .map(CompletionProposal::new)
                .toList();
    }

}
