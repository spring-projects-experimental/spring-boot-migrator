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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.sbm.shell2.client.api.ScanResult;
import org.springframework.sbm.shell2.client.events.ScanCompletedEvent;

import java.lang.reflect.Type;

@Slf4j
@RequiredArgsConstructor
public class TransformAndPublishScanCompletedEvent implements StompFrameHandler {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ScanResult scanResult = ScanResult.class.cast(payload);
        log.debug(String.format("Received ScanCompletedEvent with %d applicable recipes", scanResult.applicableRecipes()));
        eventPublisher.publishEvent(new ScanCompletedEvent(scanResult));
    }
}