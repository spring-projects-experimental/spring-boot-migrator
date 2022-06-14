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
package org.springframework.sbm.mule.actions.javadsl.translators.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
class QueryParameter {
    private String query = "";
    private List<String> muleExpressions = new ArrayList<>();
}

public class DBCommons {
    public static String escapeDoubleQuotes(String str) {
        return str.replace("\"", "\\\"");
    }

    private static final String regexPattern = "#\\[(.+?)]";
    private static final Pattern pattern = Pattern.compile(regexPattern);

    public static QueryParameter parseQueryParameter(String input) {

        if (input == null) {
            return new QueryParameter();
        }

        Matcher m = pattern.matcher(input);

        List<String> muleExpressions = new ArrayList<>();

        while (m.find()) {
            muleExpressions.add(m.group(1));
        }

        return new QueryParameter(input
                .replaceAll(regexPattern, "?")
                .replace("'?'", "?")
                , muleExpressions);
    }
}
