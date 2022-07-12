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

import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.sbm.mule.amqp.RabbitMqChannelBuilder;
import org.springframework.sbm.mule.amqp.RabbitMqListener;
import org.springframework.sbm.mule.wmq.JmsListener;
import org.springframework.sbm.mule.wmq.JmsSender;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.Network;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class BootifySimpleMuleAppIntegrationTest extends IntegrationTestBaseClass {

    private static final String FIRST_QUEUE_NAME = "sbm-integration-queue-one";
    private static final String SECOND_QUEUE_NAME = "sbm-integration-queue-two";
    private String messageContent;
    private final RestTemplate restTemplate = new RestTemplate();
    private static RunningNetworkedContainer rabbitMqContainer;

    @Override
    protected String getTestSubDir() {

        return "mule-app/spring-amqp-mule";

    }

    @BeforeEach
    public void setUp() {
        messageContent = "Integration test message " + UUID.randomUUID();
    }

    @BeforeAll
    public static void beforeAll() {
        IntegrationTestBaseClass.beforeAll();
        // start RabbitMQ
        rabbitMqContainer = startDockerContainer(
                new NetworkedContainer(
                        "rabbitmq:3-management",
                        List.of(5672, 15672),
                        "amqphost"),
                null,
                Collections.emptyMap());
        if(!rabbitMqContainer.getContainer().isRunning()) {
            throw new RuntimeException("RabbitMQ container could not be started");
        }
    }

    @AfterAll
    public static void afterAll() {
        if(rabbitMqContainer != null && rabbitMqContainer.getContainer() != null) {
            rabbitMqContainer.getContainer().stop();
        }
    }

    @Test
    @Tag("integration")
    public void  t0_springIntegrationWorks() throws IOException, TimeoutException, InterruptedException {
        intializeTestProject();
        scanProject();
        applyRecipe("initialize-spring-boot-migration");
        applyRecipe("migrate-mule-to-boot");

        executeMavenGoals(getTestDir(), "clean", "package", "spring-boot:build-image");


        int amqpPort = rabbitMqContainer.getContainer().getMappedPort(5672);
        Channel ampqChannel = new RabbitMqChannelBuilder().initializeChannelAndQueues(amqpPort);

        RunningNetworkedContainer container = startDockerContainer(
                new NetworkedContainer("hellomule-migrated:1.0-SNAPSHOT", List.of(9081), "spring"),
                rabbitMqContainer.getNetwork(),
                Collections.emptyMap());

        checkSendHttpMessage(container.getContainer().getMappedPort(9081));
        checkInboundGatewayHttpMessage(container.getContainer().getMappedPort(9081));
        checkRabbitMqIntegration(ampqChannel);
        checkDbIntegration(container.getContainer().getMappedPort(9081));
    }

    @Test
    @Tag("integration")
    @DisabledIfSystemProperty(named= "os.arch", matches = "aarch64", disabledReason = "imbcom/mq image not supported with Apple Silicon")
    void t1_testWebsphereMqMigration() throws JMSException, InterruptedException {
        checkWmqIntegration(rabbitMqContainer.getNetwork());
    }

    private void checkRabbitMqIntegration(Channel amqpChannel)
            throws IOException, InterruptedException {

        String message = "{\"msgContent\": \"" + messageContent + "\"}";
        amqpChannel.basicPublish("", FIRST_QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent amqp message: '" + message + "'");

        RabbitMqListener receiver = new RabbitMqListener(message, Map.of("TestProperty", "TestPropertyValue"));
        amqpChannel.basicConsume(SECOND_QUEUE_NAME, true, receiver, consumerTag -> {
        });
        boolean latch = receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(latch).isTrue();
    }

    private RunningNetworkedContainer startJmsContainer(Network rabbitContainerNetwork) {
        Map<String, String> wmqMap = new HashMap<>();
        wmqMap.put("LICENSE", "accept");
        wmqMap.put("MQ_QMGR_NAME", "QM1");
        wmqMap.put("MQ_APP_PASSWORD", "passw0rd");

        return startDockerContainer(new NetworkedContainer("ibmcom/mq",
                        List.of(1414, 9443),
                        "wmqhost"
                ),
                rabbitContainerNetwork,
                wmqMap);
    }

    private void checkWmqIntegration(Network rabbitMqNetwork) throws InterruptedException, JMSException {
        RunningNetworkedContainer jmsContainer = startJmsContainer(rabbitMqNetwork);
        JmsSender jmsSender = new JmsSender();
        CountDownLatch latch = new CountDownLatch(1);
        JmsListener wmqListener = new JmsListener();
        int mappedPort = jmsContainer.getContainer().getMappedPort(1414);
        wmqListener.listenForMessage(mappedPort, "DEV.QUEUE.2", message -> {
            System.out.println(" [x] Received wmq message: '" + message + "'");
            latch.countDown();
        });
        jmsSender.sendMessage(mappedPort, "DEV.QUEUE.1", "Test WMQ message");
        boolean latchResult = latch.await(1000000, TimeUnit.MILLISECONDS);
        assertThat(latchResult).isTrue();
    }

    private void checkDbIntegration(int port) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/db", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("{\"ID\":1,\"USERNAME\":\"TestUser\",\"PASSWORD\":\"secret\"");
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
}
