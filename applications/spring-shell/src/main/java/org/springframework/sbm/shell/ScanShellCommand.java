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
import org.jline.utils.Colors;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.precondition.PreconditionVerificationResult;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class ScanShellCommand {

	private final ScanCommand scanCommand;

	private final ApplicableRecipeListRenderer applicableRecipeListRenderer;

	private final ApplicableRecipeListCommand applicableRecipeListCommand;

	private final ProjectContextHolder contextHolder;
    private final PreconditionVerificationRenderer preconditionVerificationRenderer;

    @ShellMethod(key = { "scan", "s" },
			value = "Scans the target project directory and get the list of applicable recipes.")
	public AttributedString scan(@ShellOption(defaultValue = ".",
			help = "The root directory of the target application.") String projectRoot) {

		AttributedStringBuilder stringBuilder = buildHeader(projectRoot);

		List<Resource> resources = scanCommand.scanProjectRoot(projectRoot);
		PreconditionVerificationResult result = scanCommand.checkPreconditions(projectRoot, resources);
        String renderedPreconditionCheckResults = preconditionVerificationRenderer.renderPreconditionCheckResults(result);
        stringBuilder.append(renderedPreconditionCheckResults);

		if ( ! result.hasError()) {
			System.out.println(stringBuilder.toAnsi());
			stringBuilder = new AttributedStringBuilder();
            ProjectContext projectContext = scanCommand.execute(projectRoot);
            contextHolder.setProjectContext(projectContext);
            List<Recipe> recipes = applicableRecipeListCommand.execute(projectContext);
            AttributedString recipeList = applicableRecipeListRenderer.render(recipes);
            stringBuilder.append(recipeList);
        }

        return stringBuilder.toAttributedString();
	}

	@NotNull
	private AttributedStringBuilder buildHeader(String projectRoot) {
		AttributedStringBuilder builder = new AttributedStringBuilder();
		builder.append("\n");
		builder.style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("green")));
		builder.append("scanning '" + projectRoot + "'");
		return builder;
	}

}
