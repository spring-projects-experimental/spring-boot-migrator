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
package org.springframework.sbm;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.sbm.wmq.WMQListener;
import org.springframework.sbm.wmq.WMQSender;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class BootifySimpleMuleAppIntegrationTest extends IntegrationTestBaseClass {

    private static final String FIRST_QUEUE_NAME = "sbm-integration-queue-one";
    private static final String SECOND_QUEUE_NAME = "sbm-integration-queue-two";
    private static final ConnectionFactory CONNECTION_FACTORY = new ConnectionFactory();
    private String messageContent;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    protected String getTestSubDir() {

        return "mule-app/spring-amqp-mule";

    }

    @BeforeEach
    public void setUp() {
        messageContent = "Integration test message " + UUID.randomUUID();
    }

    @Test
    @Tag("integration")
    public void springIntegrationWorks() throws IOException, TimeoutException, InterruptedException {
        intializeTestProject();
        scanProject();
        applyRecipe("initialize-spring-boot-migration");
        applyRecipe("migrate-mule-to-boot");

        executeMavenGoals(getTestDir(), "clean", "package", "spring-boot:build-image");

        RunningNetworkedContainer rabbitContainer = startDockerContainers(
                new NetworkedContainer(
                        "rabbitmq:3-management",
                        List.of(5672, 15672),
                        "amqphost"),
                null,
                Collections.emptyMap());

        Map<String, String> wmqMap = new HashMap<>();
        wmqMap.put("LICENSE", "accept");
        wmqMap.put("MQ_QMGR_NAME", "QM1");
        wmqMap.put("MQ_APP_PASSWORD", "passw0rd");
        RunningNetworkedContainer wmqContainer = startDockerContainers(
                new NetworkedContainer(
                        "ibmcom/mq",
                        List.of(1414, 9443),
                        "wmqhost"),
                rabbitContainer.getNetwork(),
                        wmqMap);

        try (Connection connection = CONNECTION_FACTORY.newConnection(
                Collections.singletonList(
                        new Address("localhost", rabbitContainer.getContainer().getMappedPort(5672))
                )); Channel channel = connection.createChannel()) {
            channel.queueDeclare(FIRST_QUEUE_NAME, false, false, false, null);

            channel.queueDeclare(SECOND_QUEUE_NAME, false, false, false, null);
            String EXCHANGE = "sbm-integration-exchange";
            channel.exchangeDeclare(EXCHANGE, "direct");
            channel.queueBind(SECOND_QUEUE_NAME, EXCHANGE, SECOND_QUEUE_NAME);

            String message = "{\"msgContent\": \"" + messageContent + "\"}";
            channel.basicPublish("", FIRST_QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent amqp message: '" + message + "'");

            RunningNetworkedContainer container = startDockerContainers(
                    new NetworkedContainer("hellomule-migrated:1.0-SNAPSHOT", List.of(9081), "spring"),
                    rabbitContainer.getNetwork(),
                    Collections.emptyMap());

            checkReceivedMessage(channel, message, Map.of("TestProperty", "TestPropertyValue"));
            checkSendHttpMessage(container.getContainer().getMappedPort(9081));
            checkInboundGatewayHttpMessage(container.getContainer().getMappedPort(9081));
            checkWMQMessage(wmqContainer);
        }
    }

    private void checkWMQMessage(RunningNetworkedContainer wmqContainer) throws InterruptedException {
        WMQSender wmqSender = new WMQSender();
        CountDownLatch latch = new CountDownLatch(1);
        WMQListener wmqListener = new WMQListener();
        int mappedPort = wmqContainer.getContainer().getMappedPort(1414);
        wmqListener.listenForMessage(mappedPort, "DEV.QUEUE.2", message -> {
            System.out.println(" [x] Received wmq message: '" + message + "'");
            latch.countDown();
        });
        wmqSender.sendMessage(mappedPort, "DEV.QUEUE.2", "Test WMQ message");
        boolean latchResult = latch.await(10000, TimeUnit.MILLISECONDS);
        assertThat(latchResult).isTrue();
    }

    private void checkInboundGatewayHttpMessage(int port) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/helloworld", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("{\"message\": \"Hello worldXXX\"}");
    }

    private void checkSendHttpMessage(int port) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/test", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void checkReceivedMessage(Channel channel, String expectedMessage, Map<String, String> expectedHeaders) throws IOException, InterruptedException {
        Receiver receiver = new Receiver(expectedMessage, expectedHeaders);
        channel.basicConsume(SECOND_QUEUE_NAME, true, receiver, consumerTag -> {
        });
        boolean latch = receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(latch).isTrue();
    }

    public static class Receiver implements DeliverCallback {
        private final String expectedMessage;
        private final Map<String, String> expectedHeaders;
        private final CountDownLatch latch = new CountDownLatch(1);

        public Receiver(String expectedMessage, Map<String, String> expectedHeaders) {
            this.expectedMessage = expectedMessage;
            this.expectedHeaders = expectedHeaders;
        }

        public CountDownLatch getLatch() {
            return latch;
        }

        @Override
        public void handle(String s, Delivery delivery) throws IOException {
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
}
