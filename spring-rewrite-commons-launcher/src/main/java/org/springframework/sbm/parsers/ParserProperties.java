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

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ConfigurationProperties with prefix {@code parser}. Defaults coming from
 * {@code META-INF/sbm-support-rewrite.properties}
 *
 * @author Fabian Krüger
 */
@ConfigurationProperties(prefix = "parser")
@Getter
@Setter
public class ParserProperties {

	/**
	 * Whether to skip parsing maven pom files
	 */
	private boolean skipMavenParsing = false;

	/**
	 * Enable org.openrewrite.maven.cache.RocksdbMavenPomCache on 64-Bit system
	 */
	private boolean pomCacheEnabled = false;

	/**
	 * Directory used by RocksdbMavenPomCache when pomCacheEnabled is true
	 */
	private String pomCacheDirectory = Path.of(System.getProperty("user.home"))
		.resolve(".rewrite-cache")
		.toAbsolutePath()
		.normalize()
		.toString();

	/**
	 * Comma-separated list of patterns used to create PathMatcher The pattern should not
	 * contain a leading 'glob:'
	 */
	private Set<String> plainTextMasks = new HashSet<>();

	/**
	 * Project resources exceeding this threshold will not be parsed and provided as
	 * org.openrewrite.quark.Quark
	 */
	private int sizeThresholdMb = -1;

	/**
	 * Whether only the current Maven module will be parsed
	 */
	private boolean runPerSubmodule = false;

	/**
	 * Whether the discovery should fail on invalid active recipes. TODO: Move to
	 * 'discovery' prefix
	 */
	private boolean failOnInvalidActiveRecipes = true;

	/**
	 * Comma-separated list of active Maven profiles
	 */
	private List<String> activeProfiles = List.of("default");

	/**
	 * Comma-separated list of patterns used to create PathMatcher to exclude paths from
	 * being parsed.
	 */
	private Set<String> ignoredPathPatterns = Set.of("**/target/**", "target/**", "**/.idea/**", ".idea/**", ".mvn/**",
			"**/.mvn/**");

}
