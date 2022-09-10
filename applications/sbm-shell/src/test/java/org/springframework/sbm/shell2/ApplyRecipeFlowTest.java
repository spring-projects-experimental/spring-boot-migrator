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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.shell2.server.api.RecipeExecutionResult;
import org.springframework.sbm.shell2.server.api.SbmService;
import org.springframework.sbm.shell2.server.events.RecipeExecutionCompletedEvent;
import org.springframework.sbm.shell2.server.events.RecipeExecutionProgressUpdateEvent;
import org.springframework.sbm.shell2.server.events.ScanCompletedEvent;
import org.springframework.sbm.shell2.server.events.ScanProgressUpdatedEvent;
import org.springframework.shell.component.support.SelectorItem;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

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
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private ScanProgressRenderer scanProgressRenderer;
    @Mock
    private ApplyRecipeResultRenderer applicableRecipeResultRenderer;
    @Mock
    private SbmService sbmService;
    @Mock
    private UserInputScanner userInputScanner;
    @InjectMocks
    private ApplyRecipeFlow sut;

    @BeforeEach
    void beforeEach() {
        sut = new ApplyRecipeFlow(shellContext, sbmService, scanProgressRenderer, applicableRecipeResultRenderer, userInputScanner);
    }

    @Test
    void scan() {
        Path rootPath = Path.of("some-application");

        // user will enter path
        when(userInputScanner.askForPath()).thenReturn(rootPath);

        // start scan
        sut.scanCommand();

        // rootPath set in shell context
        ArgumentCaptor<Path> pathAc = ArgumentCaptor.forClass(Path.class);
        verify(shellContext).setScannedPath(pathAc.capture());
        assertThat(pathAc.getValue()).isEqualTo(rootPath);
        // render
        verify(scanProgressRenderer).startScan(rootPath);
        // scan was started
        ArgumentCaptor<Consumer<ScanProgressUpdatedEvent>> ac = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Consumer<ScanCompletedEvent>> ac2 = ArgumentCaptor.forClass(Consumer.class);
        verify(sbmService).scan(ArgumentMatchers.eq(rootPath), ac.capture(), ac2.capture());
        Consumer<ScanProgressUpdatedEvent> value = ac.getValue();
    }

    @Test
    void handleScanProgressUpdateEvent() {
        ScanProgressUpdate update = new ScanProgressUpdate();
        sut.handleScanProgressUpdateEvent(new ScanProgressUpdatedEvent(update));
        ArgumentCaptor<ScanProgressUpdate> ac = ArgumentCaptor.forClass(ScanProgressUpdate.class);
        verify(scanProgressRenderer).renderUpdate(ac.capture());
        assertThat(ac.getValue()).isSameAs(update);
    }

    @Test
    void handleScanCompleteEventWithApplicableRecipes() {
        // scan returns two recipes
        List<Recipe> applicableRecipes = List.of(Recipe.builder().name("r1").build(), Recipe.builder().name("r2").build());
        ScanResult scanResult = new ScanResult(applicableRecipes);
        ScanCompletedEvent scanCompletedEvent = new ScanCompletedEvent(scanResult);
        // user selects r1
        when(userInputScanner.askForSingleSelection(any())).thenReturn("r1");

        sut.handleScanCompletedEvent(scanCompletedEvent);

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
        verify(sbmService).apply(ArgumentMatchers.eq("r1"), recipeExecutionProgressUpdateEvent.capture(), recipeExecutionCompletedEvent.capture());
        recipeExecutionCompletedEvent.getValue();
    }

    @Test
    void handleScanCompleteEventWithNoRecipes() {
        // scan returns no recipes
        List<Recipe> applicableRecipes = List.of();
        ScanResult scanResult = new ScanResult(applicableRecipes);
        ScanCompletedEvent scanCompletedEvent = new ScanCompletedEvent(scanResult);
        sut.handleScanCompletedEvent(scanCompletedEvent);

        // result rendered
        verify(scanProgressRenderer).renderResult(scanResult);
        // User not asked to select recipe
        verify(userInputScanner, never()).askForSingleSelection(any());
        // No recipe applied
        verify(sbmService, never()).apply(any(), any(), any());
    }

    @Test
    void handleRecipeExecutionProgressUpdateEvent() {
        RecipeExecutionProgressUpdateEvent e = new RecipeExecutionProgressUpdateEvent();
        sut.handleRecipeExecutionProgressUpdateEvent(e);
        verify(applicableRecipeResultRenderer).renderProgress(e);
    }

    @Test
    void handleRecipeExecutionCompletedEvent() {
        RecipeExecutionResult result = new RecipeExecutionResult("recipe-name");
        Path rootPath = Path.of("scanned-application");
        when(shellContext.getScannedPath()).thenReturn(rootPath);

        sut.handleRecipeExecutionCompletedEvent(new RecipeExecutionCompletedEvent(result));

        verify(applicableRecipeResultRenderer).render(result);
        // render
        verify(scanProgressRenderer).startScan(rootPath);
        // scan was started
        verify(sbmService).scan(ArgumentMatchers.eq(rootPath), any(), any());
    }
}