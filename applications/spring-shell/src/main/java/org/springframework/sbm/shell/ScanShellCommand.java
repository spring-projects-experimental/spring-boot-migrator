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
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.precondition.PreconditionVerificationResult;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.*;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class ScanShellCommand {

    private final ScanCommand scanCommand;

    private final ApplicableRecipeListRenderer applicableRecipeListRenderer;

    private final ApplicableRecipeListCommand applicableRecipeListCommand;

    private final ProjectContextHolder contextHolder;
    private final PreconditionVerificationRenderer preconditionVerificationRenderer;
    private final ScanCommandHeaderRenderer scanCommandHeaderRenderer;
    private final ConsolePrinter consolePrinter;

    @ShellMethod(key = {"scan", "s"},
            value = "Scans the target project directory and get the list of applicable recipes.")
    public String scan(
            @ShellOption(defaultValue = ".", help = "The root directory of the target application.",
                    valueProvider = ScanValueProvider.class)
                    //@Pattern(regexp = "")
                    String projectRoot) {


        List<Resource> resources = scanCommand.scanProjectRoot(projectRoot);
        String scanCommandHeader = scanCommandHeaderRenderer.renderHeader(projectRoot);
        PreconditionVerificationResult result = scanCommand.checkPreconditions(projectRoot, resources);
        String renderedPreconditionCheckResults = preconditionVerificationRenderer.renderPreconditionCheckResults(result);
        AttributedStringBuilder stringBuilder = new AttributedStringBuilder();
        String output = stringBuilder
                .append(scanCommandHeader)
                .ansiAppend(renderedPreconditionCheckResults)
                .toAttributedString().toAnsi();

        consolePrinter.println(output);

        stringBuilder = new AttributedStringBuilder();
        if (!result.hasError()) {
            ProjectContext projectContext = scanCommand.execute(projectRoot);
            contextHolder.setProjectContext(projectContext);
            List<Recipe> recipes = applicableRecipeListCommand.execute(projectContext);
            AttributedString recipeList = applicableRecipeListRenderer.render(recipes);
            stringBuilder.append(recipeList);
        }

        return stringBuilder.toAttributedString().toAnsi();
    }

}

/**
 * Auto complete argument to scan method for easier navigation.
 * Based on org.springframework.shell.standard.FileValueProvider.
 *
 * @author Tim te Beek
 */
class ScanValueProvider extends ValueProviderSupport { // extend to only complete for exact type matches

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext,
            String[] hints) {
        String input = completionContext.currentWordUpToCursor();
        int lastSlash = input.lastIndexOf(File.separatorChar);
        Path dir = lastSlash > -1 ? Paths.get(input.substring(0, lastSlash + 1)) : Paths.get("");
        String prefix = input.substring(lastSlash + 1, input.length());

        try {
            return Files.find(dir, 1,
                    (p, a) -> p.getFileName() != null && p.getFileName().toString().startsWith(prefix), FOLLOW_LINKS)
                    .map(p -> new CompletionProposal(p.toString())).collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}