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

import org.springframework.sbm.engine.commands.DescribeCommand;
import org.springframework.sbm.engine.recipe.Recipe;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class DescribeShellCommand {

    private final DescribeCommand describeCommand;
    private final DescribeCommandRenderer describeCommandRenderer;

    @ShellMethod(key = {"describe", "d"}, value = "Describe a given recipe.")
    public AttributedString describe(@ShellOption(help = "The name of the recipe.") String recipe) {
        final Recipe recipeResult = describeCommand.execute(recipe);
        return describeCommandRenderer.render(recipeResult);
    }
}
