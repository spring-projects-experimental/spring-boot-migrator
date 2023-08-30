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
package org.springframework.sbm.mule.actions.javadsl.translators.common;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExpressionLanguageTranslator {

    private Pattern p = Pattern.compile(
            "#\\[" +    // #[ - start of Mule EL
                    "(" +       // start group 1
                    "[^#\\[]" + // no #[ inside the EL
                    "[a-zA-Z0-9\\.\\_\\(\\)]*" +  // all chars, numbers, ., _, ( and ) allowed
                    ")" +       // end group
                    "\\]"       // end of Mule EL
    );

    public String translate(String message) {
        StringBuffer springEl = new StringBuffer();
        message = message.replaceAll("\"", "\\\\\"");
        Matcher matcher = p.matcher(message);
        while (matcher.find()) {
            String replacement = Matcher.quoteReplacement(matcher.group(1));
            replacement = "\\${" + replacement + "}";
            matcher.appendReplacement(springEl, replacement);
        }
        String s = matcher.appendTail(springEl).toString();
        return s;
    }
}
