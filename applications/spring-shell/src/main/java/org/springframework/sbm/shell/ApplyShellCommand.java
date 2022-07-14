
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

import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.recipe.Recipe;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class ApplyShellCommand {

    private final ApplyCommand applyCommand;
    private final ApplicableRecipeListCommand applicableRecipeListCommand;
    private final ApplicableRecipeListRenderer applicableRecipeListRenderer;
    private final ProjectContextHolder projectContextHolder;
    private final ApplyCommandRenderer applyCommandRenderer;

    @ShellMethod(key = {"apply", "a"}, value = "Apply a given recipe to the target application.")
    @ShellMethodAvailability("availabilityCheck")
    public AttributedString apply(@ShellOption(help = "The name of the recipe to apply.") String recipeName) {
        AttributedStringBuilder header = buildHeader(recipeName);
        System.out.println(header.toAnsi());

        ProjectContext projectContext = projectContextHolder.getProjectContext();
        Recipe recipe = applyCommand.execute(projectContext, recipeName);
        AttributedString applyCommandOutput = applyCommandRenderer.render(recipe);

        List<Recipe> applicableRecipes = applicableRecipeListCommand.execute(projectContext);
        AttributedString applicableRecipesOutput = applicableRecipeListRenderer.render(applicableRecipes);

        AttributedString output = new AttributedStringBuilder().append(applyCommandOutput).append(System.lineSeparator()).append(applicableRecipesOutput).toAttributedString();

        return output;
    }

    @NotNull
    private AttributedStringBuilder buildHeader(String recipeName) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.append("Applying recipe ");
        builder.style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("yellow")));
        builder.append("'" + recipeName + "'");
        return builder;
    }

    public Availability availabilityCheck() {
        if(projectContextHolder.getProjectContext() != null) {
            return Availability.available();
        } else {
            return Availability.unavailable("You need to scan first");
        }
    }
}
