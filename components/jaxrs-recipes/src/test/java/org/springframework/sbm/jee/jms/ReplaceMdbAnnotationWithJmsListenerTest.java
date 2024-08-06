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
package org.springframework.sbm.jee.jms;

import org.springframework.sbm.test.JavaMigrationActionTestSupport;
import org.springframework.sbm.jee.jms.actions.ReplaceMdbAnnotationWithJmsListener;
import org.junit.jupiter.api.Test;

public class ReplaceMdbAnnotationWithJmsListenerTest {

    @Test
    void testReplaceMdbAnnotation() {

        String given =
                        """
                        import javax.ejb.ActivationConfigProperty;
                        import javax.ejb.MessageDriven;
                        import javax.jms.Message;
                        import javax.jms.MessageListener;
                        
                        @MessageDriven(activationConfig = {
                            @ActivationConfigProperty(propertyName = "destinationType",\s
                                propertyValue = "javax.jms.Queue"),
                            @ActivationConfigProperty(propertyName = "destinationLookup",\s
                                propertyValue = "java:app/jms/CargoHandledQueue")
                        })
                        public class CargoHandledConsumer implements MessageListener {
                        
                            @Override
                            public void onMessage(Message message) {
                            }
                        }
                        """;

        String expected =
                        """
                        import javax.jms.Message;
                        import org.springframework.jms.annotation.JmsListener;
                        import org.springframework.stereotype.Component;
                        
                        @Component
                        public class CargoHandledConsumer {
                        
                            @JmsListener(destination = "CargoHandledQueue")
                            public void onMessage(Message message) {
                            }
                        }
                        """;

        JavaMigrationActionTestSupport.verify(given, expected, new ReplaceMdbAnnotationWithJmsListener(),
                "javax:javaee-api:7.0",
                "org.springframework:spring-jms:4.1.7.RELEASE",
                "org.springframework:spring-context:4.1.7.RELEASE"); //, "org.springframework:spring-webmvc:4.1.7.RELEASE");
    }

}
