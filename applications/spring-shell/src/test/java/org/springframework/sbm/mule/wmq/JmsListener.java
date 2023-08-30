/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.mule.wmq;

import javax.jms.*;
import java.util.function.Consumer;

public class JmsListener {

    public void listenForMessage(int port, String queueName, final Consumer<String> messageConsumer) throws JMSException {
        WmqFactory wmqFactory = new WmqFactory();
        ConnectionFactory cf = wmqFactory.createFactory(port);

        // Create JMS objects
        JMSContext context = cf.createContext();
        Destination destination = context.createQueue("queue:///" + queueName);

        // autoclosable
        JMSConsumer consumer = context.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    messageConsumer.accept(message.getBody(String.class));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    } // end main()
}
