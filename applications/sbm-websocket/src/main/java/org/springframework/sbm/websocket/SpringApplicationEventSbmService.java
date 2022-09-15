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
package org.springframework.sbm.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.events.UserInputRequestedEvent;
import org.springframework.sbm.engine.recipe.Question;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabian Krüger
 */
@Service
@Profile("default")
public class SpringApplicationEventSbmService extends SbmService {
    private final ApplicationEventPublisher eventPublisher;

    public SpringApplicationEventSbmService(ScanCommand scanCommand, ApplicableRecipeListCommand applicableRecipeListCommand, ProjectContextHolder projectContextHolder, ApplyCommand applyCommand, ApplicationEventPublisher eventPublisher) {
        super(scanCommand, applicableRecipeListCommand, projectContextHolder, applyCommand);
        this.eventPublisher = eventPublisher;
    }

}
