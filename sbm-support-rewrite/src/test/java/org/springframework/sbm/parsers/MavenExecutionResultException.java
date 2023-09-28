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

import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exception thrown if {@link MavenExecutor} had errors during execution.
 *
 * @author Fabian Krüger
 */
public class MavenExecutionResultException extends RuntimeException {

    @Getter
    private final List<Throwable> exceptions;

    public MavenExecutionResultException(String message, List<Throwable> exceptions) {
        super(buildMessage(message, exceptions));
        this.exceptions = exceptions;
    }

    private static String buildMessage(String message, List<Throwable> exceptions) {
        return message + "\n" + exceptions.stream().map(t -> ExceptionUtils.getStackTrace(t)).collect(Collectors.joining("\n"));
    }
}
