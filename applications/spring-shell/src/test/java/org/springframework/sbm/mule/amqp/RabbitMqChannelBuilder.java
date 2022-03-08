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

package org.springframework.sbm.mule.amqp;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class RabbitMqChannelBuilder {
    private static final String FIRST_QUEUE_NAME = "sbm-integration-queue-one";
    private static final String SECOND_QUEUE_NAME = "sbm-integration-queue-two";
    private static final ConnectionFactory CONNECTION_FACTORY = new ConnectionFactory();
    public static final String HOST = "localhost";

    public Channel initializeChannelAndQueues(int port) throws IOException, TimeoutException {
        Connection connection = CONNECTION_FACTORY.newConnection(
                Collections.singletonList(
                        new Address(HOST, port)
                ));
        Channel channel = connection.createChannel();
        arrangeQueues(channel);
        return channel;
    }

    private void arrangeQueues(Channel channel) throws IOException {
        channel.queueDeclare(FIRST_QUEUE_NAME, false, false, false, null);
        channel.queueDeclare(SECOND_QUEUE_NAME, false, false, false, null);
        String EXCHANGE = "sbm-integration-exchange";
        channel.exchangeDeclare(EXCHANGE, "direct");
        channel.queueBind(SECOND_QUEUE_NAME, EXCHANGE, SECOND_QUEUE_NAME);
    }
}