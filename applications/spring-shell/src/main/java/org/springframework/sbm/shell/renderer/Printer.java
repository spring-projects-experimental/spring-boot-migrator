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

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Fabian Krüger
 */
@Component
public class Printer {
    private final AtomicReference<String> previousLine = new AtomicReference<>();

    public void println(String out) {
        System.out.println(out);
    }

    synchronized public void print(String text) {
        if(previousLine.get() != null && !previousLine.get().isEmpty()) {
            clearPreviousLine();
        }
        previousLine.set(text);
        System.out.print(text);
        System.out.flush();
    }

    synchronized public void printAndNewLine(String text) {
        print(text);
        println("");
        previousLine.set("");
    }

    private void clearPreviousLine() {
        System.out.print("\b".repeat(previousLine.get().length()));
        previousLine.set("");
    }
}
