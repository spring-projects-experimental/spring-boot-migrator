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
package org.springframework.sbm.shell2;

import lombok.RequiredArgsConstructor;
import org.springframework.sbm.engine.recipe.Answer;
import org.springframework.sbm.engine.recipe.Question;
import org.springframework.sbm.shell2.client.events.UserInputRequestedEvent;
import org.springframework.sbm.shell2.client.api.RecipeExecutionResult;
import org.springframework.sbm.shell2.client.api.SbmService;
import org.springframework.sbm.shell2.client.events.*;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
@ShellComponent
@RequiredArgsConstructor
public class ApplyRecipeFlow extends AbstractShellComponent {

    private final SbmShellContext shellContext;
    private final SbmService sbmService;
    private final ScanProgressRenderer scanProgressRenderer;
    private final ApplyRecipeResultRenderer applyRecipeResultRenderer;
    private final UserInputScanner userInputScanner;
    private final UserInputRequester userInputRequester;

    /**
     * Starts a scan
     */
    @ShellMethod(key = "scan")
    public void scanCommand() {
        Path projectRoot = userInputScanner.askForPath("Enter path to project");
        shellContext.setScannedPath(projectRoot);
        startScanProcess(projectRoot);
    }

    private void startScanProcess(Path projectRoot) {
        scanProgressRenderer.startScan(projectRoot);
        sbmService.scan(projectRoot,
                        (e) -> this.handleScanProgressUpdateEvent(e),
                        (e) -> this.handleScanCompletedEvent(e));
    }

    public void handleScanProgressUpdateEvent(ScanProgressUpdatedEvent event) {
        ScanProgressUpdate scanProgressUpdate = event.update();
        scanProgressRenderer.renderUpdate(scanProgressUpdate);
    }

    /**
     * Handle {@code ScanCompletedEvent}s and ask user to select the recipe to apply.
     */
    public void handleScanCompletedEvent(ScanCompletedEvent scanCompletedEvent) {
        List<SelectorItem<String>> items = scanCompletedEvent.scanResult().applicableRecipes().stream()
                                            .map(r -> SelectorItem.of(r.getName(), r.getName()))
                                            .collect(Collectors.toList());

        scanProgressRenderer.renderResult(scanCompletedEvent.scanResult());

        if(!items.isEmpty()) {
            String selectedRecipe = userInputScanner.askForSingleSelection(items);
            applyRecipe(selectedRecipe);
        }
    }

    private void applyRecipe(String selectedRecipeName) {
        sbmService.apply(selectedRecipeName,
                         (e) -> this.handleRecipeExecutionProgressUpdateEvent(e),
                         (e) -> this.handleRecipeExecutionCompletedEvent(e),
                         (e) -> this.handleUserInputRequestedEvent(e));
    }

    private Answer handleUserInputRequestedEvent(UserInputRequestedEvent e) {
        Question question = e.question();
        return userInputRequester.ask(question);
    }

    void handleRecipeExecutionProgressUpdateEvent(RecipeExecutionProgressUpdateEvent e) {
        applyRecipeResultRenderer.renderProgress(e);
    }

    void handleRecipeExecutionCompletedEvent(RecipeExecutionCompletedEvent event) {
        RecipeExecutionResult result = event.getResult();
        applyRecipeResultRenderer.render(result);
        startScanProcess(shellContext.getScannedPath());
    }
}
