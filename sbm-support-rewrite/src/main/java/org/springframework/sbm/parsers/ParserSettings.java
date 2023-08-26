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
package org.springframework.sbm.parsers;

import lombok.*;
import org.apache.commons.logging.Log;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ConfigurationProperties with prefix {@code parser}.
 *
 * @author Fabian Krüger
 */
@Component
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "parser")
public class ParserSettings {

    private String loggerClass;
    /**
     * Defines if pom files get cached or if they are always downloaded.
     */
    private boolean pomCacheEnabled = true;
    /**
     *
     */
    private Path pomCacheDirectory;
    private boolean skipMavenParsing = false;
    private Set<String> exclusions = new HashSet<>();
    private Set<String> plainTextMasks = new HashSet<>();
    private int sizeThresholdMb = -1;
    private boolean runPerSubmodule = false;
    private boolean failOnInvalidActiveRecipes = false;
    private List<String> activeProfiles = List.of("default");
    private Set<String> ignoredPathPatterns = new HashSet<>();

    /**
     * @return fully qualified classname of the logger to use.
     */
    public String getLoggerClass() {
        return loggerClass;
    }

    public void setLoggerClass(String loggerClass) {
        this.loggerClass = loggerClass;
    }

    public boolean isPomCacheEnabled() {
        return pomCacheEnabled;
    }

    public Path getPomCacheDirectory() {
        return pomCacheDirectory;
    }

    public boolean isSkipMavenParsing() {
        return skipMavenParsing;
    }

    public Set<String> getExclusions() {
        return exclusions;
    }

    public Set<String> getPlainTextMasks() {
        return plainTextMasks;
    }

    public int getSizeThresholdMb() {
        return sizeThresholdMb;
    }

    public boolean isRunPerSubmodule() {
        return runPerSubmodule;
    }

    public boolean isFailOnInvalidActiveRecipes() {
        return failOnInvalidActiveRecipes;
    }

    public List<String> getActiveProfiles() {
        return activeProfiles;
    }

    public Set<String> getIgnoredPathPatterns() {
        return ignoredPathPatterns;
    }
}
