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
package org.springframework.sbm.project.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.rewrite.utils.LinuxWindowsPathUnifier;
import org.springframework.rewrite.utils.OsAgnosticPathMatcher;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.PathMatcher;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathScanner {

	private final SbmApplicationProperties sbmApplicationProperties;
	private final ResourceHelper resourceHelper;
	private final PathMatcher pathMatcher = new OsAgnosticPathMatcher();

	public List<Resource> scan(Path projectRoot) {
		Path absoluteRootPath = projectRoot.toAbsolutePath();
		String pattern = LinuxWindowsPathUnifier.unifiedPathString(absoluteRootPath) + "/**";
		Resource[] resources = resourceHelper.loadResources("file:" + pattern);

		return Arrays.asList(resources)
				.stream()
				.filter(p -> this.isRelevant(projectRoot, getPath(p)))
				.collect(Collectors.toList());
	}

	private boolean isRelevant(Path projectRoot, Path givenResource) {
		if (givenResource.toFile().isDirectory()) {
			return false;
		}
		return sbmApplicationProperties.getIgnoredPathsPatterns().stream()
				.noneMatch(ir -> pathMatcher.match(ir,
						LinuxWindowsPathUnifier.unifiedPathString(projectRoot.relativize(givenResource))));
	}

	private Path getPath(Resource r) {
		try {
			return r.getFile().toPath().toAbsolutePath().normalize();
		}
		catch (IOException e) {
			throw new ProjectParserException(String.format("Error retrieving path for Resource '%s'", r), e);
		}
	}

}
