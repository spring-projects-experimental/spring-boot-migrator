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
package org.springframework.sbm.shell2;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.sbm.shell2.client.stomp.MigrationResultHandler;
import org.springframework.sbm.shell2.client.stomp.StompClientSessionHandler;
import org.springframework.sbm.shell2.client.stomp.StompSessionStore;
import org.springframework.sbm.shell2.client.stomp.WsClientConfig;

/**
 * @author Fabian Krüger
 */
@Disabled
@SpringBootTest(classes = {
        WsClientConfig.class, MigrationResultHandler.class, StompClientSessionHandler.class, StompSessionStore.class, WsClientConfig.class
})
public class SbmClientTest {

    @Autowired
    private StompSessionStore sessionStore;

    @Test
    void test_renameMe() {
        StompSession.Receiptable send = sessionStore.getStompSession().send("/sbm/scan", "yeah! :::");
        Assertions.assertThat(send.getReceiptId()).isNotNull();
    }
}
