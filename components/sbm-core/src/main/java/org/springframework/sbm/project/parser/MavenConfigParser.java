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

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class MavenConfigParser {

    public Map<String, String> parse(List<String> configLines) {
        if (configLines == null) {
            throw new IllegalArgumentException("Empty config Lines");
        }

        String combine = configLines.stream().collect(Collectors.joining("\n"));
        Map<String, String> mavenConfigMap = new HashMap<>();

        Properties p = new Properties();
        try {
            p.load(new ByteArrayInputStream(combine.getBytes(StandardCharsets.UTF_8)));

            p.entrySet().forEach(entry -> {

                String key = entry.getKey().toString();

                if (key.startsWith("-D")) {
                    mavenConfigMap.put(key.replace("-D", ""), entry.getValue().toString());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Unable to read maven config file", e);
        }


//        for(String line : configLines) {
//            String trimmedLine = line.trim();
//            if(trimmedLine.startsWith("-D")) {
//
//                String[] keyAndValue = trimmedLine.replace("-D", "").split("=");
//                if(keyAndValue.length == 2) {
//                    String key = keyAndValue[0].trim();
//                    String value = keyAndValue[1].trim();
//                    mavenConfigMap.put(key, value);
//                }
//            }
//        }

        return mavenConfigMap;
    }
}
