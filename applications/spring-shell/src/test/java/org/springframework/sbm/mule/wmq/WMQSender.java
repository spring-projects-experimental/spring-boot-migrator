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
package org.springframework.sbm.mule.wmq;


import javax.jms.*;

public class WMQSender {

    public void sendMessage(int port, String queueName, String messageContent) throws JMSException {
        WMQFactory wmqFactory = new WMQFactory();
        ConnectionFactory cf = wmqFactory.createFactory(port);

        // Create JMS objects
        JMSContext context = cf.createContext();
        Destination destination = context.createQueue("queue:///" + queueName);

        TextMessage message = context.createTextMessage(messageContent);

        JMSProducer producer = context.createProducer();
        System.out.println(" [x] Sent wmq message: '" + message.getText() + "'");
        producer.send(destination, message);

        context.close();
    }
}
