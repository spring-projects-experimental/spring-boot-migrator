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

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.sbm.engine.events.UserInputRequestedEvent;
import org.springframework.sbm.engine.recipe.Answer;
import org.springframework.sbm.engine.recipe.Question;
import org.springframework.sbm.engine.recipe.UserInputProvidedEvent;
import org.springframework.sbm.shell2.ScanProgressUpdate;
import org.springframework.sbm.websocket.SpringApplicationEventSbmService;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
public class SbmClientFactory {

    private final SpringApplicationEventSbmService sbmService;
    private final ApplicationEventPublisher eventPublisher;
    private Function<Question, Answer> questionConsumer;

    public SbmClient createWebsocketClient(Consumer<ScanProgressUpdate> scanProgressUpdateConsumer,
                                           Consumer<ScanResult> scanResultConsumer,
                                           Consumer<RecipeExecutionProgress> recipeExecutionProgressConsumer,
                                           Consumer<RecipeExecutionResult> recipeExecutionResultConsumer,
                                           Function<Question, Answer> questionConsumer) {

        SbmWebsocketClient.SbmClientStompClientSessionHandler stompSessionHandler = new SbmWebsocketClient.SbmClientStompClientSessionHandler(
                scanProgressUpdateConsumer,
                scanResultConsumer,
                recipeExecutionProgressConsumer,
                recipeExecutionResultConsumer,
                questionConsumer);

        SbmClient sbmClient = new SbmWebsocketClient(stompSessionHandler);

        return sbmClient;
    }

    public SbmClient createSpringApplicationEventClient(Consumer<ScanProgressUpdate> scanProgressUpdateConsumer,
                                                        Consumer<ScanResult> scanResultConsumer,
                                                        Consumer<RecipeExecutionProgress> recipeExecutionProgressConsumer,
                                                        Consumer<RecipeExecutionResult> recipeExecutionResultConsumer,
                                                        Function<Question, Answer> questionConsumer) {

        SbmClient sbmClient = new SpringApplicationEventClient(
                scanProgressUpdateConsumer,
                scanResultConsumer,
                recipeExecutionProgressConsumer,
                recipeExecutionResultConsumer,
                questionConsumer,
                sbmService,
                eventPublisher);
        this.questionConsumer = questionConsumer;
        return sbmClient;
    }

    // Just here because SbmClient is not a Spring bean (yet)
    @EventListener(UserInputRequestedEvent.class)
    void onUserInputRequestedEvent(UserInputRequestedEvent event) {
        Question question = event.getQuestion();
        Answer answer = this.questionConsumer.apply(question);
        eventPublisher.publishEvent(new UserInputProvidedEvent(answer));
    }
}
