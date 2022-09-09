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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.sbm.shell2.api.*;
import org.springframework.shell.component.PathInput;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.nio.file.Path;

/**
 * @author Fabian Krüger
 */
@ShellComponent
@RequiredArgsConstructor
public class ScanCommand extends AbstractShellComponent {

    private final SbmShellContext shellContext;
    private final SbmService sbmService;

    private final ApplicationEventPublisher eventPublisher;

    private final ScanProgressRenderer scanProgressRenderer;
    private ApplyRecipeResultRenderer applyRecipeResultRenderer;

    @ShellMethod(key = "scan")
    public void scan() {
        PathInput component = new PathInput(getTerminal(), "Enter value");
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());
        PathInput.PathInputContext context = component.run(PathInput.PathInputContext.empty());
        Path projectRoot = context.getResultValue();
        shellContext.setScannedPath(projectRoot);
        startScanProcess(projectRoot);
    }

    private void startScanProcess(Path projectRoot) {
        scanProgressRenderer.startScan(projectRoot);
        sbmService.scan(projectRoot);
    }

    @EventListener(ScanUpdateEvent.class)
    public void onScanUpdate(ScanUpdateEvent event) {
        ScanUpdate scanUpdate = event.getUpdate();
        scanProgressRenderer.renderUpdate(scanUpdate);
        // render multi select with recipes

        System.out.println(event);

        // user selects recipe or cancel

        // fire event on select
        eventPublisher.publishEvent(new ApplicableRecipeSelectedEvent("selected-recipe"));
    }

    @EventListener(ScanFinishedEvent.class)
    public void onScanFinished(ScanFinishedEvent event) {
        ScanResult scanResult = event.scanResult();
        System.out.println(event.scanResult());
        scanProgressRenderer.renderResult(scanResult);
    }

    @EventListener(ApplicableRecipeSelectedEvent.class)
    public void onApplicableRecipeSelectedEvent(ApplicableRecipeSelectedEvent event) {
        // apply recipe
        String selectedRecipe = event.getSelectedRecipe();
        sbmService.apply(selectedRecipe);
        // Use ApplyRecipeProcessRenderer to render process (updates)
    }

    @EventListener(ApplyProgressUpdateEvent.class)
    public void onApplyProgressUpdateEvent(ApplyProgressUpdateEvent event) {

    }

    @EventListener(ApplyFinishedEvent.class)
    public void onApplyFinishedEvent(ApplyFinishedEvent event) {
        ApplyRecipeResult result = event.getApplyRecipeResult();
        applyRecipeResultRenderer.render(result);
        startScanProcess(shellContext.getScannedPath());
    }
}
