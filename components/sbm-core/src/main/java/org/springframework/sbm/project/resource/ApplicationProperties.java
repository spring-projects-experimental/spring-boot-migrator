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
package org.springframework.sbm.project.resource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "sbm")
public class ApplicationProperties {
    private boolean gitSupportEnabled;
    private String defaultBasePackage;
    private boolean writeInMavenLocal;
    private boolean  javaParserLoggingCompilationWarningsAndErrors;
    private List<String> ignoredPathsPatterns = new ArrayList<>();

    public void setIgnoredPathsPatterns(List<String> patterns) {
        List<String> absolutePatterns = patterns.stream()
                .filter(pattern -> pattern.startsWith("/"))
                .collect(Collectors.toList());

        if( ! absolutePatterns.isEmpty()) {
            throw new IllegalArgumentException("Found absolute ignore paths patterns defined in sbm.ignoredPathsPatterns. Patterns must be relative and not start with '/'. Invalid patterns found: ['" + absolutePatterns.stream().collect(Collectors.joining("', '")) + "'].");
        }

        this.ignoredPathsPatterns = patterns;
    }

}
