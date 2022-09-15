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
package org.springframework.sbm.shell2.client.stomp;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabian Krüger
 */
//@Configuration
public class WsClientConfig implements WebSocketMessageBrokerConfigurer {

    @Bean
    public WebSocketStompClient webSocketStompClient(WebSocketClient webSocketClient,
                                                     StompSessionHandler stompSessionHandler) {
        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(webSocketClient);

        List<MessageConverter> converters = new ArrayList<MessageConverter>();
        converters.add(new MappingJackson2MessageConverter()); // used to handle json messages
        converters.add(new ByteArrayMessageConverter());
        converters.add(new StringMessageConverter()); // used to handle raw strings

        webSocketStompClient.setMessageConverter(new CompositeMessageConverter(converters));

//        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        TaskScheduler taskScheduler = new DefaultManagedTaskScheduler();
        webSocketStompClient.setTaskScheduler(taskScheduler);
        webSocketStompClient.setAutoStartup(true);
        webSocketStompClient.connect("ws://127.0.0.1:8080/endpoint", stompSessionHandler);
        return webSocketStompClient;
    }

    @Bean
    public WebSocketClient webSocketClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
//        transports.add(new RestTemplateXhrTransport());
        return new SockJsClient(transports);
    }

    @Bean
    public StompSessionHandler stompSessionHandler(StompSessionStore stompSessionStore, ApplicationEventPublisher eventPublisher) {
        return new StompClientSessionHandler(eventPublisher) {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                super.afterConnected(session, connectedHeaders);
                stompSessionStore.setStompSession(session);
            }
        };
    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
//        registry.addEndpoint("/endpoint");
//    }

}
