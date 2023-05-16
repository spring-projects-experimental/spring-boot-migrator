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

import org.springframework.sbm.engine.recipe.Answer;
import org.springframework.sbm.engine.recipe.Question;
import org.springframework.sbm.shell2.client.api.*;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
@ShellComponent
public class ApplyRecipeFlow extends AbstractShellComponent {

    private final SbmShellContext shellContext;
    private final ScanProgressRenderer scanProgressRenderer;
    private final ApplyRecipeResultRenderer applyRecipeResultRenderer;
    private final UserInputScanner userInputScanner;
    private final UserInputRequester userInputRequester;
    private final SbmClient sbmClient;

    public ApplyRecipeFlow(SbmShellContext shellContext,
                           ScanProgressRenderer scanProgressRenderer,
                           ApplyRecipeResultRenderer applyRecipeResultRenderer,
                           UserInputScanner userInputScanner,
                           UserInputRequester userInputRequester,
                           SbmClientFactory sbmClientFactory) {
        this.shellContext = shellContext;
        this.scanProgressRenderer = scanProgressRenderer;
        this.applyRecipeResultRenderer = applyRecipeResultRenderer;
        this.userInputScanner = userInputScanner;
        this.userInputRequester = userInputRequester;
        this.sbmClient = sbmClientFactory.createWebsocketClient(
                (scanUpdate) -> this.handleScanProgressUpdate(scanUpdate),
                (scanResult) -> this.handleScanCompletedEvent(scanResult),
                (recipeProgress) -> this.handleRecipeExecutionProgressUpdate(recipeProgress),
                (recipeResult) -> this.handleRecipeExecutionResult(recipeResult),
                (question) -> this.handleUserInputRequested(question));
    }

    /**
     * Starts a scan
     */
    @ShellMethod(key = "scan")
    public void scanCommand() {
        Path projectRoot = userInputScanner.askForPath("Enter path to project");
        shellContext.setScannedPath(projectRoot);
        CompletableFuture<ScanResult> scan = sbmClient.scan(projectRoot);
        scanProgressRenderer.startScan(projectRoot);
        try {
            scan.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyRecipe(Path projectRootPath, String selectedRecipeName) {
        sbmClient.apply(projectRootPath, selectedRecipeName);
//        sbmService.apply(selectedRecipeName,
//                         (e) -> this.handleRecipeExecutionProgressUpdate(e),
//                         (e) -> this.handleRecipeExecutionResult(e),
//                         (e) -> this.handleUserInputRequested(e));
    }

    //    private void startScanProcess(Path projectRoot) {
//
//        sbmService.scan(projectRoot,
//                        (e) -> this.handleScanProgressUpdateEvent(e),
//                        (e) -> this.handleScanCompletedEvent(e));

//    }

    public void handleScanProgressUpdate(ScanProgressUpdate progressUpdate) {
        scanProgressRenderer.renderUpdate(progressUpdate);
    }

    /**
     * Handle {@code ScanCompletedEvent}s and ask user to select the recipe to apply.
     */
    public void handleScanCompletedEvent(ScanResult scanResult) {
        List<SelectorItem<String>> items = scanResult.getApplicableRecipes().stream()
                                            .map(r -> SelectorItem.of(r.getName(), r.getName()))
                                            .collect(Collectors.toList());

        scanProgressRenderer.renderResult(scanResult);

        if(!items.isEmpty()) {
            String selectedRecipe = userInputScanner.askForSingleSelection(items);
            applyRecipe(scanResult.getScannedDir(), selectedRecipe);
        }
    }

    private Answer handleUserInputRequested(Question question) {
        return userInputRequester.ask(question);
    }

    void handleRecipeExecutionProgressUpdate(RecipeExecutionProgress e) {
        applyRecipeResultRenderer.renderProgress(e);
    }

    void handleRecipeExecutionResult(RecipeExecutionResult result) {
        applyRecipeResultRenderer.render(result);
        Path scannedPath = shellContext.getScannedPath();
        sbmClient.scan(scannedPath);
        scanProgressRenderer.startScan(scannedPath);
    }
}
