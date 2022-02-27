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
package org.springframework.sbm.shell.renderer;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@Disabled("See #524")
public class ProgressRendererLogbackLogAdapterTest {


    private Logger log;
    private PrintStream initialOut;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void beforeEach() {
        log = LoggerFactory.getLogger(getClass());
        initialOut = System.out;
        outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        System.setOut(out);
    }

    @Test
    void logAdapter() throws InterruptedException, IOException {

        // logs to console
        log.info("some info log");
        outContent.flush();
        assertThat(outContent.toString()).endsWith(" [main] INFO org.springframework.sbm.shell.renderer.ProgressRendererLogbackLogAdapterTest - some info log\n");

        // reroute console logging and capture messages
        CountDownLatch countDownLatch = new CountDownLatch(3);
        List<String> capturedMessges = new ArrayList<>();
        ProgressRendererLogbackLogAdapter sut = new ProgressRendererLogbackLogAdapter(
                Map.of(
                        Level.ERROR, (errorMessage) -> {
                            capturedMessges.add(errorMessage);
                            countDownLatch.countDown();
                        },
                        Level.WARN, (warnMessage) -> {
                            capturedMessges.add(warnMessage);
                            countDownLatch.countDown();
                        },
                        Level.INFO, (infoMessage) -> {
                            capturedMessges.add(infoMessage);
                            countDownLatch.countDown();
                        }
                ));
        sut.start();

        // send rerouted messages
        log.error("error message"); // satisfies assert in lambda
        log.warn("warn message");
        log.info("info message");

        countDownLatch.await(100, TimeUnit.MILLISECONDS);
        assertThat(capturedMessges).containsExactly("error message", "warn message", "info message");

        // reset logging to console
        sut.stop();
        log.warn("info 1");
        assertThat(outContent.toString()).endsWith("[main] WARN org.springframework.sbm.shell.renderer.ProgressRendererLogbackLogAdapterTest - info 1\n");

        log.info("info 2");
        assertThat(outContent.toString()).contains("[main] INFO org.springframework.sbm.shell.renderer.ProgressRendererLogbackLogAdapterTest - info 2");
    }


     @AfterEach
     void afterEach() {
         System.setOut(initialOut);
     }
}
