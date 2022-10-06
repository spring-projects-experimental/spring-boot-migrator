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

package org.springframework.sbm.project.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MavenConfigParser {
    public Map<String, String> parse(List<String> mavenConfigs) {
        if (mavenConfigs == null) {
            return new HashMap<>();
        }

        Pattern envVarPattern = Pattern.compile("-D.*=.*");
        return mavenConfigs
                .stream()
                .map(k -> Arrays.stream(k.split("\n")).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .filter(k -> envVarPattern.matcher(k).find())
                .map(k -> k.replace("-D", ""))
                .collect(Collectors.toMap(k -> k.split("=")[0], k -> k.split("=")[1]));
    }
}
