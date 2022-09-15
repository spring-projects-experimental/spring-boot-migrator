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
package org.springframework.sbm.shell2.client.api;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.sbm.engine.events.UserInputRequestedEvent;
import org.springframework.sbm.engine.recipe.Answer;
import org.springframework.sbm.engine.recipe.Question;
import org.springframework.sbm.engine.recipe.UserInputProvidedEvent;
import org.springframework.sbm.shell2.ScanProgressUpdate;
import org.springframework.sbm.websocket.SbmService;
import org.springframework.sbm.websocket.SpringApplicationEventSbmService;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
public class SpringApplicationEventClient implements SbmClient {
    private final Consumer<ScanProgressUpdate> scanProgressUpdateConsumer;
    private final Consumer<ScanResult> scanResultConsumer;
    private final Consumer<RecipeExecutionProgress> recipeExecutionProgressConsumer;
    private final Consumer<RecipeExecutionResult> recipeExecutionResultConsumer;
    private final Function<Question, Answer> questionConsumer;
    private final SbmService sbmService;

    private ApplicationEventPublisher eventPublisher;

    public SpringApplicationEventClient(
            Consumer<ScanProgressUpdate> scanProgressUpdateConsumer,
            Consumer<ScanResult> scanResultConsumer,
            Consumer<RecipeExecutionProgress> recipeExecutionProgressConsumer,
            Consumer<RecipeExecutionResult> recipeExecutionResultConsumer,
            Function<Question, Answer> questionConsumer, SpringApplicationEventSbmService sbmService, ApplicationEventPublisher eventPublisher) {
        this.scanProgressUpdateConsumer = scanProgressUpdateConsumer;
        this.scanResultConsumer = scanResultConsumer;
        this.recipeExecutionProgressConsumer = recipeExecutionProgressConsumer;
        this.recipeExecutionResultConsumer = recipeExecutionResultConsumer;
        this.questionConsumer = questionConsumer;
        this.sbmService = sbmService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public CompletableFuture<ScanResult> scan(Path projectRoot) {
        org.springframework.sbm.websocket.ScanResult scan = sbmService.scan(projectRoot);
        CompletableFuture<ScanResult> scanResultCompletableFuture = new CompletableFuture<>();
        ScanResult scanResultClientModel = new ScanResult(
                scan.getApplicableRecipes().stream()
                        .map(r -> new Recipe(r.getName(), r.getActions().stream()
                                .map(a -> Action.builder().name(a.getDescription()).build())
                                .collect(Collectors.toList())
                             )
                        )
                        .collect(Collectors.toList()), scan.getScannedDir(), scan.getTimeElapsed());
        this.scanResultConsumer.accept(scanResultClientModel);
        scanResultCompletableFuture.complete(scanResultClientModel);
        return scanResultCompletableFuture;
    }

    @Override
    public void apply(Path projectRootPath, String selectedRecipeName) {
        org.springframework.sbm.websocket.RecipeExecutionResult result = sbmService.apply(selectedRecipeName);
        RecipeExecutionResult recipeExecutionResult = new RecipeExecutionResult(selectedRecipeName);
        this.recipeExecutionResultConsumer.accept(recipeExecutionResult);
    }

}
