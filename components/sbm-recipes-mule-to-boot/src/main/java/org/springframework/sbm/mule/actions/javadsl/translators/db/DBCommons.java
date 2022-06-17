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
import org.mulesoft.schema.mule.db.AdvancedDbMessageProcessorType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
class QueryWithParameters {
    private String query = "";
    private List<String> muleExpressions = new ArrayList<>();
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class QueryFunctionParameter {
    private String query = "";
    private String arguments = "";
}

public class DBCommons {
    public static String escapeDoubleQuotes(String str) {
        return str.replace("\"", "\\\"");
    }

    private static final String regexPattern = "#\\[(.+?)]";
    private static final Pattern pattern = Pattern.compile(regexPattern);

    public static QueryWithParameters parseQueryParameter(String input) {

        if (input == null) {
            return new QueryWithParameters();
        }

        Matcher m = pattern.matcher(input);

        List<String> muleExpressions = new ArrayList<>();

        while (m.find()) {
            muleExpressions.add(m.group(1));
        }

        return new QueryWithParameters(input
                .replaceAll(regexPattern, "?")
                .replace("'?'", "?")
                , muleExpressions);
    }

    public static QueryFunctionParameter extractQueryAndParameters(AdvancedDbMessageProcessorType component) {

        String query = component.getDynamicQuery() == null ? component.getParameterizedQuery()
                : component.getDynamicQuery();
        QueryWithParameters queryWithParameters = DBCommons.parseQueryParameter(query);
        String argumentTemplate = "                                p.getFirst(\"%s\") /* TODO: Translate #[%s] to java expression*/";
        String arguments = queryWithParameters
                .getMuleExpressions()
                .stream()
                .map(muleExpression -> String.format(argumentTemplate, muleExpression, muleExpression))
                .collect(Collectors.joining(",\n"));

        if (!arguments.isEmpty()) {
            arguments = ",\n" + arguments + "\n";
        }

        return new QueryFunctionParameter(queryWithParameters.getQuery(), arguments);
    }
}
