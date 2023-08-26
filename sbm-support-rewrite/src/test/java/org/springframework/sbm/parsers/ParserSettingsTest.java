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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Krüger
 */
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = ParserSettings.class)
@TestPropertySource("classpath:parser-settings-test.properties")
class ParserSettingsTest {

    @Nested
    public class ValidProperties {

        @Autowired
        ParserSettings parserSettings;

        @Test
        @DisplayName("valid LoggerClass")
        void validLoggerClass() {
            assertThat(parserSettings.getLoggerClass()).isEqualTo("com.acme.LoggerClass");
        }

        @Test
        @DisplayName("valid Pom Cache enabled")
        void validPomCacheEnabled() {
            assertThat(parserSettings.isPomCacheEnabled()).isTrue();
        }

        @Test
        @DisplayName("valid pomCacheDirectory")
        void validPomCacheDirectory() {
            assertThat(parserSettings.getPomCacheDirectory()).isEqualTo(Path.of("some/dir"));
        }

        @Test
        @DisplayName("valid skipMavenParsing")
        void validSkipMavenParsing() {
            assertThat(parserSettings.isSkipMavenParsing()).isTrue();
        }

        @Test
        @DisplayName("valid exclusions")
        void validExclusions() {
            assertThat(parserSettings.getExclusions()).containsExactlyInAnyOrder("foo", "bar/*");
        }

        @Test
        @DisplayName("valid plainTextMasks")
        void validPlainTextMasks() {
            assertThat(parserSettings.getPlainTextMasks()).containsExactlyInAnyOrder("*.txt", "*.md");
        }

        @Test
        @DisplayName("valid sizeThresholdMb")
        void validSizeThresholdMb() {
            assertThat(parserSettings.getSizeThresholdMb()).isEqualTo(10);
        }

        @Test
        @DisplayName("valid runPerSubmodule")
        void validRunPerSubmodule() {
            assertThat(parserSettings.isRunPerSubmodule()).isTrue();
        }

        @Test
        @DisplayName("valid failOnInvalidActiveRecipes")
        void validFailOnInvalidActiveRecipes() {
            assertThat(parserSettings.isFailOnInvalidActiveRecipes()).isTrue();
        }

        @Test
        @DisplayName("valid activeProfiles")
        void validActiveProfiles() {
            assertThat(parserSettings.getActiveProfiles()).containsExactlyInAnyOrder("profile1", "profile2");
        }

        @Test
        @DisplayName("valid ignoredPathPatterns")
        void validIgnoredPathPatterns() {
         assertThat(parserSettings.getIgnoredPathPatterns()).containsExactlyInAnyOrder("/**/.idea/*", "/**/.git/*");
        }
    }

}