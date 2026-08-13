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
import org.eclipse.jgit.ignore.IgnoreNode;
import org.springframework.core.io.Resource;
import org.springframework.sbm.common.util.OsAgnosticPathMatcher;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.PathMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Scans a project for resources, skipping everything matched by
 * {@code sbm.ignoredPathsPatterns} or by the project's {@code .gitignore}.
 *
 * @author Fabian Krüger
 * @author Sumedh Gole
 */
@Service
@RequiredArgsConstructor
public class PathScanner {

	private static final String GIT_IGNORE_FILE = ".gitignore";

	private final SbmApplicationProperties sbmApplicationProperties;
	private final ResourceHelper resourceHelper;
	private final PathMatcher pathMatcher = new OsAgnosticPathMatcher();

	public List<Resource> scan(Path projectRoot) {
		String pattern = "%s/**".formatted(projectRoot.toAbsolutePath().toUri());
		Resource[] resources = resourceHelper.loadResources(pattern);
		IgnoreNode gitIgnore = readGitIgnore(projectRoot);

		return Arrays.stream(resources)
				.filter(p -> this.isRelevant(projectRoot, getPath(p), gitIgnore))
				.collect(Collectors.toList());
	}

	private boolean isRelevant(Path projectRoot, Path givenResource, IgnoreNode gitIgnore) {
		if (givenResource.toFile().isDirectory()) {
			return false;
		}
		Path relativePath = projectRoot.relativize(givenResource);
		if (isIgnoredByGit(gitIgnore, relativePath)) {
			return false;
		}
		return sbmApplicationProperties.getIgnoredPathsPatterns().stream()
				.noneMatch(ir -> pathMatcher.match(ir, relativePath.toString()));
	}

	/**
	 * Reads the {@code .gitignore} of the scanned project, if any. Returns an empty
	 * {@link IgnoreNode} when the project has none, which ignores nothing.
	 */
	private IgnoreNode readGitIgnore(Path projectRoot) {
		IgnoreNode ignoreNode = new IgnoreNode();
		Path gitIgnoreFile = projectRoot.resolve(GIT_IGNORE_FILE);
		if (Files.isRegularFile(gitIgnoreFile)) {
			try (InputStream in = Files.newInputStream(gitIgnoreFile)) {
				ignoreNode.parse(in);
			}
			catch (IOException e) {
				throw new ProjectParserException(String.format("Error reading '%s'", gitIgnoreFile), e);
			}
		}
		return ignoreNode;
	}

	/**
	 * Git never descends into an ignored directory, so a resource is ignored as soon as
	 * the resource itself or any of its parent directories is ignored.
	 */
	private boolean isIgnoredByGit(IgnoreNode gitIgnore, Path relativePath) {
		StringBuilder pathSoFar = new StringBuilder();
		for (int i = 0; i < relativePath.getNameCount(); i++) {
			if (i > 0) {
				pathSoFar.append('/');
			}
			pathSoFar.append(relativePath.getName(i));
			boolean isDirectory = i < relativePath.getNameCount() - 1;
			if (Boolean.TRUE.equals(gitIgnore.checkIgnored(pathSoFar.toString(), isDirectory))) {
				return true;
			}
		}
		return false;
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
