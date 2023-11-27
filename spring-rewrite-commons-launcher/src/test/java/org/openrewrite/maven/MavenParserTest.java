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
package org.openrewrite.maven;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ExpectedToFail;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.SourceFile;

import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.ibm.icu.impl.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class MavenParserTest {

	@Test
	@DisplayName("Should Read .mvn/maven.config")
	@ExpectedToFail("See https://github.com/openrewrite/rewrite/issues/3409")
	void shouldReadMvnMavenConfig() {
		String mavenConfig = """
				-Drevision=42
				-Dvalidation-api.version=2.0.1.Final
				-Psome-profile,another-profile
				""";

		Path baseDir = Path.of("./testcode/maven-projects/maven-config");
		Stream<SourceFile> parse = MavenParser.builder()
			.mavenConfig(baseDir.resolve(".mvn/maven.config"))
			.build()
			.parse(List.of(baseDir.resolve("pom.xml")), null, new InMemoryExecutionContext(t -> {
				t.printStackTrace();
				fail("exception");
			}));
	}

	@Test
	@DisplayName("testRegex")
	// TODO: Tryout of the regex that leads to the failing test (above), remove this test
	// when issue #3409 is fixed.
	void testRegex() {

		String regex = "(?:$|\\s)-P\\s+([^\\s]+)";
		assertThat(Pattern.compile(regex).matcher("\n-P someProfile\n").find()).isTrue();
		assertThat(Pattern.compile(regex).matcher("""
				-Psome-profile,another-profile
				""").find()).isFalse();
		assertThat(Pattern.compile(regex).matcher("-PsomeProfile").find()).isFalse();
		assertThat(Pattern.compile(regex).matcher("-P someProfile").find()).isFalse();
	}

}
