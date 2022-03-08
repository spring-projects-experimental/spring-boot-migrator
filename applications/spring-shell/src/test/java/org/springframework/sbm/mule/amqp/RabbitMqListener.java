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

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class RabbitMqListener implements DeliverCallback {
        private final String expectedMessage;
        private final Map<String, String> expectedHeaders;
        private final CountDownLatch latch = new CountDownLatch(1);

        public RabbitMqListener(String expectedMessage, Map<String, String> expectedHeaders) {
            this.expectedMessage = expectedMessage;
            this.expectedHeaders = expectedHeaders;
        }

        public CountDownLatch getLatch() {
            return latch;
        }

        @Override
        public void handle(String s, Delivery delivery) {
            String receivedMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Map<String, Object> receivedHeader = delivery.getProperties().getHeaders();

            boolean headersMatch = expectedHeaders.entrySet().stream()
                    .allMatch(
                            p -> receivedHeader.get(p.getKey()).toString().equals(p.getValue())
                    );
            System.out.println(" [x] Received ampq message: '" + receivedMessage + "'");
            System.out.println(" [x] Does amqp header match? : " + headersMatch);
            if (receivedMessage.equals(expectedMessage) && headersMatch) {
                latch.countDown();
            }
        }
}
