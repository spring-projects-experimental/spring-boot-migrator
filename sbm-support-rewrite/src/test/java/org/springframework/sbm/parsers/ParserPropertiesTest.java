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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.sbm.boot.autoconfigure.ScannerConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

class ParserPropertiesTest {

	@Nested
	@SpringBootTest(classes = { ScannerConfiguration.class })
	public class GivenDefaultProperties {

		@Autowired
		private ParserProperties parserProperties;

		@Test
		@DisplayName("parser.pomCacheEnabled")
		void defaultPomCacheEnabled() {
			assertThat(parserProperties.isPomCacheEnabled()).isFalse();
		}

		@Test
		@DisplayName("parser.pomCacheDirectory")
		void defaultPomCacheDirectory() {
			assertThat(parserProperties.getPomCacheDirectory()).isEqualTo("~/.rewrite-cache");
		}

		@Test
		@DisplayName("parser.skipMavenParsing")
		void defaultSkipMavenParsing() {
			assertThat(parserProperties.isSkipMavenParsing()).isFalse();
		}

		@Test
		@DisplayName("parser.plainTextMasks")
		void defaultPlainTextMasks() {
			assertThat(parserProperties.getPlainTextMasks()).containsExactlyInAnyOrder("*.txt");
		}

		@Test
		@DisplayName("parser.sizeThresholdMb")
		void defaultSizeThresholdMb() {
			assertThat(parserProperties.getSizeThresholdMb()).isEqualTo(10);
		}

		@Test
		@DisplayName("parser.runPerSubmodule")
		void defaultRunPerSubmodule() {
			assertThat(parserProperties.isRunPerSubmodule()).isFalse();
		}

		@Test
		@DisplayName("parser.failOnInvalidActiveRecipes")
		void defaultFailOnInvalidActiveRecipes() {
			assertThat(parserProperties.isFailOnInvalidActiveRecipes()).isTrue();
		}

		@Test
		@DisplayName("parser.activeProfiles")
		void defaultActiveProfiles() {
			assertThat(parserProperties.getActiveProfiles()).containsExactlyInAnyOrder("default");
		}

		@Test
		@DisplayName("parser.ignoredPathPatterns")
		void defaultIgnoredPathPatterns() {
			assertThat(parserProperties.getIgnoredPathPatterns()).containsExactlyInAnyOrder("**.idea/**", "**.git/**",
					"**/target/**", "target/**");
		}

	}

}