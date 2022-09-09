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
package org.springframework.sbm.shell2.shell.renderer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Fabian Krüger
 */
public class ProgressRendererLogbackLogAdapter {
    private final Appender<ILoggingEvent> logAppender;
    private static final Set<Appender<ILoggingEvent>> detachedAppenders = new HashSet<>();
    private final LoggerContext logCtx;

    private Level logLevel;

    public ProgressRendererLogbackLogAdapter(Map<Level, Consumer<String>> consumerMap) {
        logCtx = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = logCtx.getLogger("ROOT");
        if(logger.getAppender("console") != null) {
            detachedAppenders.add(logger.getAppender("console"));
        }
        if(logger.getAppender("CONSOLE") != null) {
            detachedAppenders.add(logger.getAppender("CONSOLE"));
        }
        if(logger.getAppender("CONSOLE") != null) {
            detachedAppenders.add(logger.getAppender("CONSOLE"));
            logger.detachAppender("CONSOLE");
        }
        if(logger.getAppender("stdout") != null) {
            detachedAppenders.add(logger.getAppender("stdout"));
        }
        logAppender = new ErrorLogAppender(consumerMap);

    }

    public void start() {
        if(logAppender.isStarted()) return;
        Logger logger = logCtx.getLogger("ROOT");
        detachedAppenders.forEach(a -> logger.detachAppender(a));
        logger.addAppender(logAppender);
        logAppender.setContext(logCtx);
        logAppender.start();
    }

    public void stop() {
        if(!logAppender.isStarted()) return;
        logAppender.stop();
        LoggerContext logCtx = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = logCtx.getLogger("ROOT");
        detachedAppenders.forEach(a -> {
            if(!root.isAttached(a)) {
                root.addAppender(a);
            }
        });
        root.detachAppender(logAppender);
    }

    public class ErrorLogAppender extends AppenderBase<ILoggingEvent> {
        private final Map<Level, Consumer<String>> consumerMap;

        public ErrorLogAppender(Map<Level, Consumer<String>> consumerMap) {
            this.consumerMap = consumerMap;
        }

        @Override
        protected void append(ILoggingEvent iLoggingEvent) {
            if(consumerMap.containsKey(iLoggingEvent.getLevel())) {
                consumerMap.get(iLoggingEvent.getLevel()).accept(iLoggingEvent.getMessage());
            }
        }
    }

}
