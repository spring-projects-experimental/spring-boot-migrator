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
package org.springframework.sbm.engine.precondition;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Component
class JavaSourceDirExistsPreconditionCheck extends PreconditionCheck {

	private static final String PATTERN = "/**/src/main/java/**";
	private final String JAVA_SRC_DIR = "src/main/java";
	private AntPathMatcher antPathMatcher = new AntPathMatcher(File.separator);

	@Override
	public PreconditionCheckResult verify(Path projectRoot, List<Resource> projectResources) {
		if (projectResources.stream()
				.noneMatch(r -> antPathMatcher.match(projectRoot.resolve(PATTERN).normalize().toString(), getPath(r).toAbsolutePath().toString()))) {
			return new PreconditionCheckResult(ResultState.FAILED, "PreconditionCheck check could not find a '" + JAVA_SRC_DIR + "' dir. This dir is required.");
		}
		return new PreconditionCheckResult(ResultState.PASSED, "Found required source dir 'src/main/java'.");
	}

}
