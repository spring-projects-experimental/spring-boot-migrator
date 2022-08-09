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
package org.springframework.sbm.engine.recipe;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.events.*;

public interface Action {
    String getDescription();

    String getDetailedDescription();

    Condition getCondition();

    void apply(ProjectContext context);

    default void applyWithStatusEvent(ProjectContext context) {
        ApplicationEventPublisher eventPublisher = getEventPublisher();
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new ActionStartedEvent(getDescription()));
        }
        try {
            apply(context);
        } catch(Exception e) {
            String message = "'" + this.getDescription() + "' failed: " + e.getMessage();
            if (eventPublisher != null) {
                eventPublisher.publishEvent(new ActionFailedEvent(message));
            }
            throw new ActionFailedException(message, e);
        }
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new ActionFinishedEvent(getDescription()));
        }
    }

    ApplicationEventPublisher getEventPublisher();

    boolean isAutomated();

    default boolean isApplicable(ProjectContext context) {
        return getCondition().evaluate(context);
    }

    /**
     * Informs the user about a long running process.
     * Every started process must be ended with {@code #endProcess}.
     */
    default void startProcess(String name) {
        getEventPublisher().publishEvent(new ActionProcessStartedEvent(name));
    }

    /**
     * Ends a previously started process.
     */
    default void endProcess() {
        getEventPublisher().publishEvent(new ActionProcessFinishedEvent());
    }

    /**
     * Logs an event to the user as part of the currently active process.
     */
    default void logEvent(String s) {
        getEventPublisher().publishEvent(new ActionLogEvent(s));
    }
}
