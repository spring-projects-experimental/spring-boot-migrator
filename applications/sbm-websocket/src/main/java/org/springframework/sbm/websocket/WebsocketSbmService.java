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

import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
@Profile("websocket")
public class WebsocketSbmService extends SbmService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebsocketSbmService(ScanCommand scanCommand, ApplicableRecipeListCommand applicableRecipeListCommand, ProjectContextHolder projectContextHolder, ApplyCommand applyCommand, SimpMessagingTemplate messagingTemplate) {
        super(scanCommand, applicableRecipeListCommand, projectContextHolder, applyCommand);
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Listen to {@code UserInputRequestedEvent}s and forward them to websocket destination {@code 'queue/question'}.
     */
    @EventListener(UserInputRequestedEvent.class)
    void onUserInputRequestedEvent(UserInputRequestedEvent event) {
        Question question = event.getQuestion();
        Message<Question> message = MessageBuilder.withPayload(question).build();

        List<MessageConverter> converters = new ArrayList<MessageConverter>();
        converters.add(new MappingJackson2MessageConverter()); // used to handle json messages
        converters.add(new ByteArrayMessageConverter());
        converters.add(new StringMessageConverter()); // used to handle raw strings

        CompositeMessageConverter converter = new CompositeMessageConverter(converters);

        messagingTemplate.send("/queue/question", message);
        messagingTemplate.setMessageConverter(converter);
    }

}
