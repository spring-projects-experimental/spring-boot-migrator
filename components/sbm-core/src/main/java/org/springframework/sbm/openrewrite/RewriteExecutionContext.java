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
package org.springframework.sbm.openrewrite;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.java.JavaParsingException;
import org.openrewrite.maven.MavenDownloadingException;
import org.openrewrite.maven.internal.MavenParsingException;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.function.Consumer;

@Getter
@Slf4j
public class RewriteExecutionContext extends InMemoryExecutionContext {

    @Deprecated(forRemoval = true)
    private Optional<ApplicationEventPublisher> appEventPublisher;

    @Deprecated(forRemoval = true)
    public RewriteExecutionContext(ApplicationEventPublisher appEventPublisher) {
        super(createErrorHandler());
        this.appEventPublisher = Optional.of(appEventPublisher);
    }

    public RewriteExecutionContext() {
        super(createErrorHandler());
        this.appEventPublisher = Optional.empty();
    }

    @Deprecated
    public RewriteExecutionContext(Consumer<Throwable> exceptionHandler) {
        super(exceptionHandler);
    }

    private static Consumer<Throwable> createErrorHandler() {
        Consumer<Throwable> errorConsumer = (t) -> {
            if (t instanceof MavenParsingException) {
                log.warn(t.getMessage());
            } else if(t instanceof MavenDownloadingException) {
                log.warn(t.getMessage());
            } else if(t instanceof JavaParsingException) {
                if(t.getMessage().equals("Failed symbol entering or attribution")) {
                    throw new RuntimeException("This could be a broken jar. Activate logging on WARN level for 'org.openrewrite' might reveal more information.", t);
                }
            } else {
                log.error("Exception occured!", t);
            }
        };
        return errorConsumer;
    }

}
