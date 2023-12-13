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
package org.springframework.rewrite.project.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "sbm")
public class SbmApplicationProperties {

	private boolean gitSupportEnabled;

	private boolean muleTriggerMeshTransformEnabled;

	private String defaultBasePackage;

	private boolean writeInMavenLocal;

	private boolean javaParserLoggingCompilationWarningsAndErrors;

	private List<String> ignoredPathsPatterns = new ArrayList<>();

	public void setIgnoredPathsPatterns(List<String> patterns) {
		List<String> absolutePatterns = patterns.stream()
			.filter(pattern -> pattern.startsWith("/"))
			.collect(Collectors.toList());

		if (!absolutePatterns.isEmpty()) {
			throw new IllegalArgumentException(
					"Found absolute ignore paths patterns defined in sbm.ignoredPathsPatterns. Patterns must be relative and not start with '/'. Invalid patterns found: ['"
							+ String.join("', '", absolutePatterns) + "'].");
		}

		this.ignoredPathsPatterns = patterns;
	}

	public boolean isGitSupportEnabled() {
		return gitSupportEnabled;
	}

	public void setGitSupportEnabled(boolean gitSupportEnabled) {
		this.gitSupportEnabled = gitSupportEnabled;
	}

	public boolean isMuleTriggerMeshTransformEnabled() {
		return muleTriggerMeshTransformEnabled;
	}

	public void setMuleTriggerMeshTransformEnabled(boolean muleTriggerMeshTransformEnabled) {
		this.muleTriggerMeshTransformEnabled = muleTriggerMeshTransformEnabled;
	}

	public String getDefaultBasePackage() {
		return defaultBasePackage;
	}

	public void setDefaultBasePackage(String defaultBasePackage) {
		this.defaultBasePackage = defaultBasePackage;
	}

	public boolean isWriteInMavenLocal() {
		return writeInMavenLocal;
	}

	public void setWriteInMavenLocal(boolean writeInMavenLocal) {
		this.writeInMavenLocal = writeInMavenLocal;
	}

	public boolean isJavaParserLoggingCompilationWarningsAndErrors() {
		return javaParserLoggingCompilationWarningsAndErrors;
	}

	public void setJavaParserLoggingCompilationWarningsAndErrors(
			boolean javaParserLoggingCompilationWarningsAndErrors) {
		this.javaParserLoggingCompilationWarningsAndErrors = javaParserLoggingCompilationWarningsAndErrors;
	}

	public List<String> getIgnoredPathsPatterns() {
		return ignoredPathsPatterns;
	}

}
