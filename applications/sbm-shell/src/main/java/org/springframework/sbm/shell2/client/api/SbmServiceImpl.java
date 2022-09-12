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
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.sbm.engine.recipe.*;
import org.springframework.sbm.shell2.client.stomp.StompSessionStore;
import org.springframework.sbm.shell2.client.events.UserInputRequestedEvent;
import org.springframework.sbm.shell2.client.events.*;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * @author Fabian Krüger
 */
@Service
@Deprecated
@RequiredArgsConstructor
public class SbmServiceImpl extends StompSessionHandlerAdapter implements SbmService {

    private static final String DESTINATION = "/sbm/scan";
    private final StompSessionStore sessionStore;

    private final ApplicationEventPublisher eventPublisher;
    private StompSession stompSession;


    @Override
    public void scan(Path projectRoot) {

        StompSession session = getStompSession();
        session.send("/sbm/scan", projectRoot.toString());
        session.send(DESTINATION, projectRoot.toString());
//
//        // TODO: remove simulation of scan
//        new Thread(() -> {
//            IntStream.range(0, 10).forEach(i-> {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                scanProgressUpdatedEventConsumer.accept(new ScanProgressUpdatedEvent(new ScanProgressUpdate()));
//            });
//            scanCompletedEventConsumer.accept(new ScanCompletedEvent(new ScanResult(
//                    List.of(
//                            Recipe.builder().name("recipe-1").build(),
//                            Recipe.builder().name("recipe-2").build())
//                    )
//                )
//            );
//        }).run();
    }

    private StompSession getStompSession() {
        try {
            WebSocketClient webSocketClient = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
            stompClient.setMessageConverter(new StringMessageConverter());
//        TaskScheduler taskScheduler;
//        stompClient.setTaskScheduler(taskScheduler); // for heartbeats
            String url = "ws://127.0.0.1:8080/endpoint";
            StompSessionHandler sessionHandler = new DeadEndStompSessionHandler();
            ListenableFuture<StompSession> connect = stompClient.connect(url, sessionHandler);
            stompSession = connect.get();
            return stompSession;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    public class MyStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe("/queue/recipes/applicable", new TransformAndPublishScanCompletedEvent(eventPublisher));
            session.subscribe("/queue/migration/logs", new StompClientSessionHandler.MigrationLogsHandler());
            session.subscribe("/queue/migration/result", new MigrationResultHandler());
        }
    }
    */

    @Override
    public void apply(String selectedRecipe,
                      Consumer<RecipeExecutionProgressUpdateEvent> recipeExecutionProgressUpdateEventConsumer,
                      Consumer<RecipeExecutionCompletedEvent> recipeExecutionCompletedEventConsumer,
                      Function<UserInputRequestedEvent, Answer> userInputRequestedEventConsumer) {
        // TODO: remove simulation of apply
        new Thread(() -> {
            IntStream.range(0, 10).forEach(i-> {
                try {
                    Thread.sleep(100);
                    if(i%3==0) {
                        Answer answer = userInputRequestedEventConsumer.apply(new UserInputRequestedEvent(
                                SelectOneQuestion
                                        .builder()
                                        .option(Option.of("A", "a"))
                                        .option(Option.of("B", "b"))
                                        .build()));
                        System.out.println("Thanks for answering: " + answer.userInput());
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                recipeExecutionProgressUpdateEventConsumer.accept(new RecipeExecutionProgressUpdateEvent());
            });
            recipeExecutionCompletedEventConsumer.accept(new RecipeExecutionCompletedEvent(new RecipeExecutionResult(selectedRecipe)));
        }).run();
    }

    private class ApplicableRecipeUpdateHandler  extends StompSessionHandlerAdapter {
        private Consumer<ScanResult> scanCompletedEventConsumer;

        public ApplicableRecipeUpdateHandler(Consumer<ScanResult> scanCompletedEventConsumer) {
            this.scanCompletedEventConsumer = scanCompletedEventConsumer;
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe("/queue/recipes/applicable", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return ScanResult.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    ScanResult scanResult = ScanResult.class.cast(payload);
                    scanCompletedEventConsumer.accept(scanResult);
                }
            });
        }
    }

    private class DeadEndStompSessionHandler extends StompSessionHandlerAdapter {
    }
}
