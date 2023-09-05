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
package org.springframework.sbm.parsers;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.PathMatcher;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ConfigurationProperties with prefix {@code parser}.
 * Defaults coming from {@code META-INF/sbm-support-rewrite.properties}
 *
 * @author Fabian Krüger
 */
@Component
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "parser")
public class ParserProperties {

    /**
     * Whether to skip parsing maven pom files
     */
    private boolean skipMavenParsing = false;

    /**
     * Enable {@link org.openrewrite.maven.cache.RocksdbMavenPomCache} on 64-Bit system
     */
    private boolean pomCacheEnabled = false;

    /**
     * Directory used by {@link org.openrewrite.maven.cache.RocksdbMavenPomCache} when {@link ParserProperties#pomCacheEnabled} is {@code true}
     */
    private String pomCacheDirectory;

    /**
     * Comma-separated list of patterns used to create {@link PathMatcher}s
     * The pattern should not contain a leading {@code glob:}
     */
    private Set<String> plainTextMasks = new HashSet<>();

    /**
     * Project resources exceeding this threshold will not be parsed and provided as {@link org.openrewrite.quark.Quark}s
     */
    private int sizeThresholdMb = -1;

    /**
     * Whether only the current Maven module will be parsed
     */
    private boolean runPerSubmodule = false;

    /**
     * Whether the discovery should fail on invalid active recipes.
     * TODO: Move to 'discovery' prefix
     */
    private boolean failOnInvalidActiveRecipes = false;

    /**
     * Comma-separated list of active Maven profiles
     */
    private List<String> activeProfiles = List.of("default");

    /**
     * Comma-separated list of patterns used to create {@link PathMatcher}s to exclude paths from being parsed.
     */
    private Set<String> ignoredPathPatterns = new HashSet<>();

    public boolean isPomCacheEnabled() {
        return pomCacheEnabled;
    }

    public String getPomCacheDirectory() {
        return pomCacheDirectory;
    }

    public boolean isSkipMavenParsing() {
        return skipMavenParsing;
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
