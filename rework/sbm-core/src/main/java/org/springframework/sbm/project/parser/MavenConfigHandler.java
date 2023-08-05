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

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class MavenConfigHandler {

    public void injectMavenConfigIntoSystemProperties(List<Resource> resources) {

        Optional<Resource> mavenConfig = resources.stream().filter(k -> "maven.config".equals(k.getFilename()))
                .findFirst();

        Map<String, String> mavenConfigMap = new HashMap<>();

        if (mavenConfig.isPresent()) {
            Properties properties = new Properties();
            try {
                properties.load(mavenConfig.get().getInputStream());

                properties.forEach((key, value) -> {

                    String varKey = key.toString();

                    if (varKey.startsWith("-D")) {
                        mavenConfigMap.put(varKey.replace("-D", ""), value.toString());
                    }
                });

            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        }

        mavenConfigMap.keySet().forEach(k -> System.setProperty(k, mavenConfigMap.get(k)));
    }
}
