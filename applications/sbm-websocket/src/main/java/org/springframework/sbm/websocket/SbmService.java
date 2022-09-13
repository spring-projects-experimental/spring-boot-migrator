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
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.events.UserInputRequestedEvent;
import org.springframework.sbm.engine.precondition.PreconditionVerificationResult;
import org.springframework.sbm.engine.recipe.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Fabian Krüger
 */
@Service
@RequiredArgsConstructor
public class SbmService {

    private final ScanCommand scanCommand;
    private final ApplicableRecipeListCommand applicableRecipeListCommand;
    private final ProjectContextHolder projectContextHolder;
    private final ApplyCommand applyCommand;

    private final SimpMessagingTemplate messagingTemplate;

    public ScanResult scan(Path projectRoot) {
        List<Resource> resources = scanCommand.scanProjectRoot(projectRoot);
        PreconditionVerificationResult result = scanCommand.checkPreconditions(projectRoot.toString(), resources);
        // TODO: Add error support to scan
        if (!result.hasError()) {
            ProjectContext projectContext = scanCommand.execute(projectRoot.toString());
            projectContextHolder.setProjectContext(projectContext);
            List<Recipe> recipes = applicableRecipeListCommand.execute(projectContext);
            return new ScanResult(recipes, projectRoot, 100);
        }
        return new ScanResult(result, projectRoot, 100);
    }

    public RecipeExecutionResult apply(String recipeName) {
        ProjectContext projectContext = projectContextHolder.getProjectContext();
        if(projectContext != null) {
            List<Action> appliedActions = applyCommand.execute(projectContext, recipeName);
            return new RecipeExecutionResult(appliedActions);
        }
        return new RecipeExecutionResult(List.of());
    }

    /**
     * Listen to {@code UserInputRequestedEvent}s and forward them to websocket destination {@code 'queue/question'}.
     */
    @EventListener(UserInputRequestedEvent.class)
    void onUserInputRequestedEvent(UserInputRequestedEvent event) {
        Question question = event.getQuestion();
        Message<Question> message = MessageBuilder.withPayload(question).build();
        messagingTemplate.send("/queue/question", message);
    }

}
