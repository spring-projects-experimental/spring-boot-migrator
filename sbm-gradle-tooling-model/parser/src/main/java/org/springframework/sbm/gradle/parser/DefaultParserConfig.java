/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.gradle.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultParserConfig implements ParseConfig {

    private final List<String> exclusions = new ArrayList<>();
    private final List<String> plainTextMasks = new ArrayList<>();

    private int sizeThresholdMb = 10;

    @Override
    public List<String> getPlainTextMasks() {
        if (plainTextMasks.isEmpty()) {
            plainTextMasks.addAll(Arrays.asList(
                    "**gradlew",
                    "**META-INF/services/**",
                    "**/META-INF/spring.factories",
                    "**/META-INF/spring/**",
                    "**.gitignore",
                    "**.gitattributes",
                    "**.java-version",
                    "**.sdkmanrc",
                    "**.sh",
                    "**.bash",
                    "**.bat",
                    "**.ksh",
                    "**.txt",
                    "**.jsp",
                    "**.sql",
                    "**Dockerfile",
                    "**Jenkinsfile",
                    "**.kts"
            ));
        }
        return plainTextMasks;
    }

    @Override
    public List<String> getExclusions() {
        return exclusions;
    }

    @Override
    public int getSizeThresholdMb() {
        return sizeThresholdMb;
    }

}
