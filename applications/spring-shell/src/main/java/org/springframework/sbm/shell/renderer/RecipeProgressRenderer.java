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
package org.springframework.sbm.shell.renderer;

import ch.qos.logback.classic.Level;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Renders progress for Actions executed by a Recipe to stdout.
 * <p>
 * A stack can either contain a Log message or a Process.
 * Log messages are shown once and processes have a start and an end and are rendered as running in between using a
 * very simple loader.
 * The element on top of the stack is the displayed to the user.
 * the render() method should be called by a scheduled thread to trigger the rewriting for a process in the stack.
 * This will render a loader and signalize a running process to the user.
 * <p>
 * Messages send to console by Logback (info, warn and error level) are rerouted to methods in this class.
 * The rerouting is started with the first started process and stopped when all processes finished or failed.
 *
 * @author Fabian Krüger
 * @author Modified by Mahendra Rao(bsmahi)
 */
@Component
public class RecipeProgressRenderer {

    // FIXME: deactivated for now. The isolated test succeeds but fails when ran with other integration tests.
    // The log re-routing does not work as expected then.
//    private final ProgressRendererLogbackLogAdapter errorLogAdapter = initLogbackLogAdapter();;
    private final Deque<ProgressStep> stepsDeque = new ConcurrentLinkedDeque<>();
    private final Printer printer;
    private final Indent indent;

    public RecipeProgressRenderer(Printer printer) {
        this.printer = printer;
        indent = new Indent();
    }

    @NotNull
    private ProgressRendererLogbackLogAdapter initLogbackLogAdapter() {
        final ProgressRendererLogbackLogAdapter errorLogAdapter;
        errorLogAdapter = new ProgressRendererLogbackLogAdapter(Map.of(
                // route logback logs of certain level to their render methods
                Level.ERROR, this::logError,
                Level.INFO, this::logMessage,
                Level.WARN, this::logWarning
        ));
        return errorLogAdapter;
    }

//    @PreDestroy
//    void preDestroy() {
//        errorLogAdapter.stop();
//    }

    public synchronized void startProcess(String description) {
//        errorLogAdapter.start();
        if (!stepsDeque.isEmpty()) {
            if (isLogging(stepsDeque)) {
                stepsDeque.pop();
            } else if (!((ProcessStep) stepsDeque.peek()).isPaused()) {
                pauseCurrent();
            }
            indent.increase();
        }
        ProcessStep processStep = new ProcessStep(description, indent);
        String start = processStep.start();
        printer.print(start);
        stepsDeque.push(processStep);
    }

    public synchronized void finishProcess() {
        if (!stepsDeque.isEmpty()) {
            if (isLogging(stepsDeque)) {
                stepsDeque.pop();
            }
        }
        if (!stepsDeque.isEmpty()) {
            finish();
        }
        if (stepsDeque.isEmpty()) {
//            errorLogAdapter.stop();
        }
    }

    public synchronized void logMessage(String logMessage) {
        LogStep e = new LogStep(logMessage, indent);
        if (!stepsDeque.isEmpty() && isLogging(stepsDeque)) {
            stepsDeque.pop();
        } else if (!stepsDeque.isEmpty() && !((ProcessStep) stepsDeque.peek()).isPaused()) {
            pauseCurrent();
        }
        String out = e.render();
        printer.println(out);
        stepsDeque.push(e);
    }

    public synchronized void finishAction() {
        while (!stepsDeque.isEmpty()) {
            finish();
        }
        stepsDeque.clear();
//        errorLogAdapter.stop();
    }


    public synchronized void failProcess() {
        if (!stepsDeque.isEmpty()) {
            if (stepsDeque.peek().getClass().isAssignableFrom(ProcessStep.class)) {
                String fail = ((ProcessStep) stepsDeque.pop()).fail();
                printer.printAndNewLine(fail);
            }
        }
        stepsDeque.clear();
//        errorLogAdapter.stop();
    }

    public synchronized void render() {
        if (!stepsDeque.isEmpty()) {
            if (LogStep.class.isAssignableFrom(stepsDeque.peek().getClass())) {
//            stepsDeque.pop();
//            stepsDeque.peek().resume();
                return;
            } else if (!((ProcessStep) stepsDeque.peek()).isPaused()) {
                String render = stepsDeque.peek().render();
                printer.print(render);
            }
        }
    }

    private void finish() {
        String finish = stepsDeque.pop().finish();
        printer.printAndNewLine(finish);
        if (!stepsDeque.isEmpty()) {
            indent.decrease();
        }
    }

    private void pauseCurrent() {
        if (stepsDeque.peek().getClass().isAssignableFrom(ProcessStep.class)) {
            String pause = ((ProcessStep) stepsDeque.peek()).pause();
            printer.printAndNewLine(pause);
        }
    }

    private boolean isLogging(Deque<ProgressStep> stepsDeque) {
        if (stepsDeque.isEmpty()) return false;
        return LogStep.class.isAssignableFrom(stepsDeque.peek().getClass());
    }

    public synchronized void logError(String message) {
        LogStep e = new LogStep(message, indent);
        if (!stepsDeque.isEmpty() && isLogging(stepsDeque)) {
            stepsDeque.pop();
        } else if (!stepsDeque.isEmpty() && !((ProcessStep) stepsDeque.peek()).isPaused()) {
            pauseCurrent();
        }
        String out = e.renderError();
        printer.println(out);
        stepsDeque.push(e);
    }

    public synchronized void logWarning(String message) {
        LogStep e = new LogStep(message, indent);
        if (!stepsDeque.isEmpty() && isLogging(stepsDeque)) {
            stepsDeque.pop();
        } else if (!stepsDeque.isEmpty() && !((ProcessStep) stepsDeque.peek()).isPaused()) {
            pauseCurrent();
        }
        String out = e.renderWarning();
        printer.println(out);
        stepsDeque.push(e);
    }


}
