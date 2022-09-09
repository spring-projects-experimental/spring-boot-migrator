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
package org.springframework.sbm.shell2.shell;

import lombok.Setter;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.context.event.EventListener;
import org.springframework.sbm.engine.events.*;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.shell2.shell.renderer.Printer;
import org.springframework.sbm.shell2.shell.renderer.RecipeProgressRenderer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Renders progress information during the runtime of a recipe.
 *
 * {@code RecipeProgressRenderer} handles the rendering of action progress.
 * A {@code ScheduledExecutorService} is used to periodically call the {@link RecipeProgressRenderer#render()} method
 * which renders a process as loading to provide visual feedback to users during a potentially long-running Action.
 *
 * Logging to console will be disabled and log messages to info, warn and error level are redirected to the matching
 * {@link RecipeProgressRenderer#logError(String)},
 * {@link RecipeProgressRenderer#logWarning(String)} or
 * {@link RecipeProgressRenderer#logMessage(String)}
 * method while there's a process running.
 */
@Component
public class ApplyCommandRenderer {

    private ScheduledExecutorService scheduledExecutorService;
    private final RecipeProgressRenderer recipeProgressRenderer = new RecipeProgressRenderer(new Printer());

    /**
     * The delay in ms to start polling {@link RecipeProgressRenderer#render()}.
     */
    @Setter
    private int initialDelay = 50;

    /**
     * The interval in ms to poll {@link RecipeProgressRenderer#render()}.
     */
    @Setter
    private int delay = 250;

    public AttributedString render(String recipeName, List<Action> actions) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.append("\n");
        builder.style(AttributedStyle.DEFAULT.italicDefault());
        builder.append(recipeName);
        builder.append(" successfully applied the following actions:\n")
                .append(actions.stream()
                        .map(a -> "  (x) " + getDescriptionWithIndent(a))
                        .collect(Collectors.joining("\n")))
                .append("\n");
        return builder.toAttributedString();
    }

    private String getDescriptionWithIndent(Action a) {
        if(a == null || a.getDescription() == null) return "";

        String[] lines = a.getDescription().split("\\r?\\n");
        List<String> strings = Arrays.asList(lines);
        String collect = "";
        if(lines.length > 1) {
            collect = strings.subList(1, strings.size()).stream()
                    .map(l -> String.format("        %s", l))
                    .collect(Collectors.joining("\n"));
        }
        return strings.get(0) + (collect.isEmpty() ? "" : "\n" + collect + "\n");
    }

    @EventListener
    public void onActionStarted(ActionStartedEvent e) {
        if(scheduledExecutorService != null && ! scheduledExecutorService.isTerminated()) {
            scheduledExecutorService.shutdown();
        }
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(() -> recipeProgressRenderer.render(), initialDelay, delay, TimeUnit.MILLISECONDS);
        recipeProgressRenderer.startProcess(e.getDescription());
    }

    @EventListener
    public void onActionProcessStarted(ActionProcessStartedEvent e) {
        recipeProgressRenderer.startProcess(e.getDescription());
    }

    @EventListener
    public void onActionLog(ActionLogEvent e) {
        recipeProgressRenderer.logMessage(e.getMesssage());
    }

    @EventListener
    public void onActionProcessFinished(ActionProcessFinishedEvent e) {
        recipeProgressRenderer.finishProcess();
        scheduledExecutorService.shutdownNow();
    }

    @EventListener
    public void onActionFinished(ActionFinishedEvent e) {
        recipeProgressRenderer.finishAction();
        stopExecutorService();
    }

    @EventListener
    public void onActionFailedEvent(ActionFailedEvent e) {
        recipeProgressRenderer.failProcess();
        scheduledExecutorService.shutdownNow();
        stopExecutorService();
    }

    private void stopExecutorService() {
        try {
            scheduledExecutorService.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}