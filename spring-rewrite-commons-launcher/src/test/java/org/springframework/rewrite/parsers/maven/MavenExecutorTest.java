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
package org.springframework.rewrite.parsers.maven;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class MavenExecutorTest {

	@Test
	@DisplayName("Verify MavenSession when running in Maven")
	void verifyMavenSessionWhenRunningInMaven() {
		MavenExecutionRequestFactory requestFactory = new MavenExecutionRequestFactory(new MavenConfigFileParser());
		MavenPlexusContainer containerFactory = new MavenPlexusContainer();
		MavenExecutor sut = new MavenExecutor(requestFactory, containerFactory);
		Path baseDir = Path.of("./testcode/maven-projects/maven-config");
		List<String> goals = List.of("clean", "install");
		sut.onProjectSucceededEvent(baseDir, goals, event -> {
			assertThat(event.getSession()).isNotNull();
		});
	}

}