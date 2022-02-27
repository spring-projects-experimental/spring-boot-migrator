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

import javax.inject.Provider;

import org.jline.reader.LineReader;
import org.springframework.stereotype.Component;

import org.springframework.sbm.engine.recipe.UserInteractions;

@Component
public class ShellUserInteractions implements UserInteractions {

    private static final String YES_RESPONSE = "y";
    private static final String NO_RESPONSE = "n";

    private final Provider<LineReader> lineReader;

    ShellUserInteractions(Provider<LineReader> lineReader) {
        this.lineReader = lineReader;
    }

    @Override
    public boolean askUserYesOrNo(String question) {
        String input = "";
        while (!YES_RESPONSE.equalsIgnoreCase(input) && !NO_RESPONSE.equalsIgnoreCase(input)) {
            input = lineReader.get().readLine("\n" + question + " > ").trim();
        }
        return YES_RESPONSE.equalsIgnoreCase(input);
    }

    @Override
    public String askForInput(String question) {
         return lineReader.get().readLine("\n" + question + " > ").trim();
    }
}
