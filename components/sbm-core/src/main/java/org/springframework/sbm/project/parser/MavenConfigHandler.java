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
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class MavenConfigHandler {

    public void injectMavenConfigIntoSystemProperties(Path projectDirectory) {

        Map<String, String> mavenConfigMap = new HashMap<>();
        Path mavenConfigPath = projectDirectory.resolve(".mvn/maven.config");
        if (mavenConfigPath.toFile().exists()) {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(mavenConfigPath.toFile()));

                properties.entrySet().forEach(entry -> {

                    String key = entry.getKey().toString();

                    if (key.startsWith("-D")) {
                        mavenConfigMap.put(key.replace("-D", ""), entry.getValue().toString());
                    }
                });

            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        }

        mavenConfigMap.keySet().forEach(k -> System.setProperty(k, mavenConfigMap.get(k)));
    }
}
