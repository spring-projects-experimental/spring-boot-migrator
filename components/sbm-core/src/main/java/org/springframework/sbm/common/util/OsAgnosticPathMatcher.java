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
package org.springframework.sbm.common.util;

import org.springframework.sbm.utils.LinuxWindowsPathUnifier;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Comparator;
import java.util.Map;

/**
 * PathMatcher implementation for Unix and Windows style paths.
 *
 * For Windows, absolute paths {@code C:\my\dir\} will be translated to a Unix like
 * absolute path {@code /my/dir} and then the matching pattern is applied against this
 * path.
 *
 * @author Fabian Krüger
 */
public class OsAgnosticPathMatcher implements PathMatcher {

	private final PathMatcher pathMatcher = new AntPathMatcher();

	@Override
	public boolean isPattern(String s) {
		return pathMatcher.isPattern(s);
	}

	@Override
	public boolean match(String pattern, String path) {
		return pathMatcher.match(pattern, unifyPath(path));
	}

	private String unifyPath(String path) {
		return LinuxWindowsPathUnifier.transformToLinuxPath(path);
	}

	@Override
	public boolean matchStart(String pattern, String path) {
		return pathMatcher.matchStart(pattern, unifyPath(path));
	}

	@Override
	public String extractPathWithinPattern(String pattern, String path) {
		return pathMatcher.extractPathWithinPattern(pattern, unifyPath(path));
	}

	@Override
	public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
		return pathMatcher.extractUriTemplateVariables(pattern, unifyPath(path));
	}

	@Override
	public Comparator<String> getPatternComparator(String path) {
		return pathMatcher.getPatternComparator(unifyPath(path));
	}

	@Override
	public String combine(String pattern1, String pattern2) {
		return pathMatcher.combine(pattern1, pattern2);
	}
}
