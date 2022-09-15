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

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.sbm.engine.recipe.Answer;
import org.springframework.sbm.engine.recipe.Question;
import org.springframework.sbm.shell2.ScanProgressUpdate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Fabian Krüger
 */
@Slf4j
public class SbmClient {

    private static final String SCAN_DESTINATION = "/sbm/scan";
    private static final String APPLY_DESTINATION = "/sbm/apply";
    private final String HANDSHAKE_URL = "ws://127.0.0.1:8080/endpoint";

    private StompSession stompSession;
    private final StompSessionHandler stompSessionHandler;

    public SbmClient(StompSessionHandler stompSessionHandler) {
        this.stompSessionHandler = stompSessionHandler;
    }

    public CompletableFuture<ScanResult> scan(Path projectRoot) {
        StompSession session = getStompSession(stompSessionHandler);
        session.send(SCAN_DESTINATION, projectRoot.toString());
        // FIXME: this does not work, fix this or find another way to keep the console from printing `>`
        CompletableFuture<ScanResult> future = new CompletableFuture();
        return future;
    }

    public void apply(Path projectRootPath, String selectedRecipeName) {
        StompSession stompSession = getStompSession(stompSessionHandler);
        RecipeExecutionRequest recipeExecutionRequest = new RecipeExecutionRequest(projectRootPath, selectedRecipeName);
        stompSession.send(APPLY_DESTINATION, recipeExecutionRequest);
    }

    private StompSession getStompSession(StompSessionHandler sessionHandler) {
        if(stompSession == null || !stompSession.isConnected()) {
            try {
                WebSocketClient webSocketClient = new StandardWebSocketClient();
                WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
                stompClient.setMessageConverter(
                        new MappingJackson2MessageConverter()
//                        new CompositeMessageConverter(
//                            List.of(
//                                    new MappingJackson2MessageConverter(),
//                                    new ByteArrayMessageConverter(),
//                                    new StringMessageConverter()
//                            )
//                        )
                );
                TaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
                stompClient.setTaskScheduler(taskScheduler); // for heartbeats
                String url = HANDSHAKE_URL;
                ListenableFuture<StompSession> connect = stompClient.connect(url, sessionHandler);
                stompSession = connect.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return stompSession;
    }

    public static class SbmClientStompClientSessionHandler extends StompSessionHandlerAdapter  {

        private final Consumer<ScanProgressUpdate> scanProgressUpdateConsumer;
        private final Consumer<ScanResult> scanResultConsumer;
        private final Consumer<RecipeExecutionProgress> recipeExecutionProgressConsumer;
        private final Consumer<RecipeExecutionResult> recipeExecutionResultConsumer;
        private final Function<Question, Answer> questionConsumer;
        private StompSession session;

        public SbmClientStompClientSessionHandler(
                Consumer<ScanProgressUpdate> scanProgressUpdateConsumer,
                Consumer<ScanResult> scanResultConsumer,
                Consumer<RecipeExecutionProgress> recipeExecutionProgressConsumer,
                Consumer<RecipeExecutionResult> recipeExecutionResultConsumer,
                Function<Question, Answer> questionConsumer) {
            this.scanProgressUpdateConsumer = scanProgressUpdateConsumer;
            this.scanResultConsumer = scanResultConsumer;
            this.recipeExecutionProgressConsumer = recipeExecutionProgressConsumer;
            this.recipeExecutionResultConsumer = recipeExecutionResultConsumer;
            this.questionConsumer = questionConsumer;
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe("/queue/scan/result", new ScanResultFrameHandler(scanResultConsumer));
            session.subscribe("/queue/scan/progress", new ScanProgressUpdateHandler(scanProgressUpdateConsumer));
            session.subscribe("/queue/migration/progress", new RecipeExecutionProgressHandler(recipeExecutionProgressConsumer));
            session.subscribe("/queue/migration/result", new RecipeExecutionResultHandler(recipeExecutionResultConsumer));
            session.subscribe("/queue/question", new QuestionHandler(questionConsumer));
            this.session = session;
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            log.error("Exception while handling Websocket.", exception);
            //throw new RuntimeException("Exception while handling Websocket.", exception);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            log.error("Transport error while handling Websocket.", exception);
//            throw new RuntimeException("Transport error while handling Websocket.", exception);
        }

        abstract class GenericStompFrameHandler<T> implements StompFrameHandler {

            private final Class<T> type;
            private Consumer<T> consumer;

            public GenericStompFrameHandler(Class<T> type, Consumer<T> consumer) {
                this.type = type;
                this.consumer = consumer;
            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return this.type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                T data = type.cast(payload);
                consumer.accept(data);
            }
        }

        private class ScanResultFrameHandler extends SbmClientStompClientSessionHandler.GenericStompFrameHandler<ScanResult>{
            public ScanResultFrameHandler(Consumer<ScanResult> consumer) {
                super(ScanResult.class, consumer);
            }
        }
        private class ScanProgressUpdateHandler extends SbmClientStompClientSessionHandler.GenericStompFrameHandler<ScanProgressUpdate> {
            public ScanProgressUpdateHandler(Consumer<ScanProgressUpdate> consumer) {
                super(ScanProgressUpdate.class, consumer);
            }
        }
        private class RecipeExecutionProgressHandler extends SbmClientStompClientSessionHandler.GenericStompFrameHandler<RecipeExecutionProgress> {
            public RecipeExecutionProgressHandler(Consumer<RecipeExecutionProgress> consumer) {
                super(RecipeExecutionProgress.class, consumer);
            }
        }
        private class RecipeExecutionResultHandler extends GenericStompFrameHandler<RecipeExecutionResult> {
            public RecipeExecutionResultHandler(Consumer<RecipeExecutionResult> consumer) {
                super(RecipeExecutionResult.class, consumer);
            }
        }
        private class QuestionHandler implements StompFrameHandler {

            private final Function<Question, Answer> consumer;
            private Consumer<Answer> answerConsumer;

            public QuestionHandler(Function<Question, Answer> consumer) {
                this.consumer = consumer;
                this.answerConsumer = answerConsumer;
            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Question.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Question question = Question.class.cast(payload);
                Answer answer = consumer.apply(question);
                session.send("queue/question/answer", answer);
            }
        }
    }
}
