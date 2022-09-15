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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.sbm.engine.recipe.Answer;
import org.springframework.sbm.engine.recipe.Question;
import org.springframework.sbm.shell2.client.api.*;
import org.springframework.sbm.shell2.client.events.UserInputRequestedEvent;
import org.springframework.sbm.shell2.client.events.RecipeExecutionCompletedEvent;
import org.springframework.sbm.shell2.client.events.RecipeExecutionProgressUpdateEvent;
import org.springframework.shell.component.support.SelectorItem;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Fabian Krüger
 */
@ExtendWith(MockitoExtension.class)
class ApplyRecipeFlowTest {

    @Mock
    private SbmShellContext shellContext;
    @Mock
    private ScanProgressRenderer scanProgressRenderer;
    @Mock
    private ApplyRecipeResultRenderer applicableRecipeResultRenderer;
    @Mock
    private SbmClient sbmClient;
    @Mock
    private SbmClientFactory sbmClientFactory;
    @Mock
    private UserInputScanner userInputScanner;
    @Mock
    private UserInputRequester userInputRequester;
    @InjectMocks
    private ApplyRecipeFlow sut;

    @BeforeEach
    void beforeEach() {
        ArgumentCaptor<Consumer<ScanProgressUpdate>> scanProgressCaptor = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Consumer<ScanResult>> scanResultCaptor = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Consumer<RecipeExecutionProgress>> recipeProgressCaptor = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Consumer<RecipeExecutionResult>> recipeResultCaptor = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Function<Question, Answer>> questionCaptor = ArgumentCaptor.forClass(Function.class);
        when(sbmClientFactory.createWebsocketClient(scanProgressCaptor.capture(), scanResultCaptor.capture(), recipeProgressCaptor.capture(), recipeResultCaptor.capture(), questionCaptor.capture())).thenReturn(sbmClient);
        sut = new ApplyRecipeFlow(shellContext, scanProgressRenderer, applicableRecipeResultRenderer, userInputScanner, userInputRequester, sbmClientFactory);
    }

    @Test
    void scan() {
        Path rootPath = Path.of("some-application");

        // user will enter path
        when(userInputScanner.askForPath("Enter path to project")).thenReturn(rootPath);
        CompletableFuture<ScanResult> future = mock(CompletableFuture.class);
        when(sbmClient.scan(rootPath)).thenReturn(future);

        // start scan
        sut.scanCommand();

        // render
        verify(scanProgressRenderer).startScan(rootPath);

        // rootPath set in shell context
        ArgumentCaptor<Path> pathAc = ArgumentCaptor.forClass(Path.class);
        verify(shellContext).setScannedPath(pathAc.capture());
        assertThat(pathAc.getValue()).isEqualTo(rootPath);
    }

    @Test
    void handleScanProgressUpdateEvent() {
        ScanProgressUpdate update = new ScanProgressUpdate();
        sut.handleScanProgressUpdate(update);
        ArgumentCaptor<ScanProgressUpdate> ac = ArgumentCaptor.forClass(ScanProgressUpdate.class);
        verify(scanProgressRenderer).renderUpdate(ac.capture());
        assertThat(ac.getValue()).isSameAs(update);
    }

    @Test
    void handleScanResultWithApplicableRecipes() {
        // scan returns two recipes
        List<Recipe> applicableRecipes = List.of(Recipe.builder().name("r1").build(), Recipe.builder().name("r2").build());
        ScanResult scanResult = new ScanResult(applicableRecipes, Path.of("some-path"), 100);
        // user selects r1
        when(userInputScanner.askForSingleSelection(any())).thenReturn("r1");

        sut.handleScanCompletedEvent(scanResult);

        // result rendered
        verify(scanProgressRenderer).renderResult(scanResult);
        // recipes provided
        ArgumentCaptor<List<SelectorItem<String>>> ac = ArgumentCaptor.forClass(List.class);
        verify(userInputScanner).askForSingleSelection(ac.capture());
        assertThat(ac.getValue().get(0).getItem()).isEqualTo("r1");
        assertThat(ac.getValue().get(1).getItem()).isEqualTo("r2");
        // apply selected recipe
        ArgumentCaptor<Consumer<RecipeExecutionProgressUpdateEvent>> recipeExecutionProgressUpdateEvent = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Consumer<RecipeExecutionCompletedEvent>> recipeExecutionCompletedEvent = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Function<UserInputRequestedEvent, Answer>> userInputRequestedEvent = ArgumentCaptor.forClass(Function.class);
        verify(sbmClient).apply(any(Path.class), ArgumentMatchers.eq("r1"));
    }

    @Test
    void handleScanCompleteEventWithNoRecipes() {
        // scan returns no recipes
        List<Recipe> applicableRecipes = List.of();
        ScanResult scanResult = new ScanResult(applicableRecipes, Path.of("some-path"), 100);
        sut.handleScanCompletedEvent(scanResult);

        // result rendered
        verify(scanProgressRenderer).renderResult(scanResult);
        // User not asked to select recipe
        verify(userInputScanner, never()).askForSingleSelection(any());
        // No recipe applied
        verify(sbmClient, never()).apply(any(), any());
    }

    @Test
    void handleRecipeExecutionProgressUpdateEvent() {
        RecipeExecutionProgress p = new RecipeExecutionProgress();
        sut.handleRecipeExecutionProgressUpdate(p);
        verify(applicableRecipeResultRenderer).renderProgress(p);
    }

    @Test
    void handleRecipeExecutionCompletedEvent() {
        RecipeExecutionResult result = new RecipeExecutionResult("recipe-name");
        Path rootPath = Path.of("scanned-application");

        when(shellContext.getScannedPath()).thenReturn(rootPath);

        sut.handleRecipeExecutionResult(result);

        verify(sbmClient).scan(rootPath);
        verify(applicableRecipeResultRenderer).render(result);
    }
}