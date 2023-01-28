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
package org.springframework.sbm.shell;

/**
 * Utility methods to convert exceptions into other types of exceptions, status
 * objects etc.
 *
 * @author Kris De Volder
 */
public class ExceptionUtil {

    public static Throwable getDeepestCause(Throwable e) {
        Throwable cause = e;
        Throwable parent = e.getCause();
        while (parent != null && parent != e) {
            cause = parent;
            parent = cause.getCause();
        }
        return cause;
    }

    public static String getMessage(Throwable e) {
        // The message of nested exception is usually more interesting than the
        // one on top.
        Throwable cause = getDeepestCause(e);
        String errorType = cause.getClass().getSimpleName();
        String msg = cause.getMessage();
        return errorType + ": " + msg;
    }

}
