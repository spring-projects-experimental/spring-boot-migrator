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
package org.springframework.sbm.parsers;

import org.apache.maven.plugin.logging.Log;
import org.slf4j.Logger;

/**
 * @author Fabian Krüger
 */
public class Slf4jToMavenLoggerAdapter implements Log {
    private final Logger log;

    public Slf4jToMavenLoggerAdapter(Logger log) {
        this.log = log;
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(CharSequence charSequence) {
        log.debug(charSequence.toString());
    }

    @Override
    public void debug(CharSequence charSequence, Throwable throwable) {
        log.debug(charSequence.toString(), throwable);
    }

    @Override
    public void debug(Throwable throwable) {
        log.debug("", throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(CharSequence charSequence) {
        log.info(charSequence.toString());
    }

    @Override
    public void info(CharSequence charSequence, Throwable throwable) {
        log.info(charSequence.toString(), throwable);
    }

    @Override
    public void info(Throwable throwable) {
        log.info("", throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(CharSequence charSequence) {
        log.warn(charSequence.toString());
    }

    @Override
    public void warn(CharSequence charSequence, Throwable throwable) {
        log.warn(charSequence.toString(), throwable);
    }

    @Override
    public void warn(Throwable throwable) {
        log.warn("", throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(CharSequence charSequence) {
        log.error(charSequence.toString());
    }

    @Override
    public void error(CharSequence charSequence, Throwable throwable) {
        log.error(charSequence.toString(), throwable);
    }

    @Override
    public void error(Throwable throwable) {
        log.error("", throwable);
    }
}
