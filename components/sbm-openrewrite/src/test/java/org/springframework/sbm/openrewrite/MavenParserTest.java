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
package org.springframework.sbm.openrewrite;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.cache.RocksdbMavenPomCache;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.Problem;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MavenParserTest {

	@Problem(description = "java.io.UncheckedIOException: Failed to parse pom", version = "7.18.2")
	@Disabled("#497")
	void testParsingPomWithEmptyDependenciesSection() {
//		String pomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
//				+ "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" \n" +
//				"    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
//				"    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
//				+ "    <modelVersion>4.0.0</modelVersion>\n"
//				+ "    <groupId>com.example</groupId>\n"
//				+ "    <artifactId>foo-bar</artifactId>\n"
//				+ "    <version>0.1.0-SNAPSHOT</version>\n"
//				+ "    <dependencies>\n</dependencies>\n"
//				+ "</project>";

		String pomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
				"    <modelVersion>4.0.0</modelVersion>\n" +
				"    <groupId>com.example</groupId>\n" +
				"    <artifactId>foo-bar</artifactId>\n" +
				"    <version>0.1.0-SNAPSHOT</version>\n" +
				"    <dependencies></dependencies>\n" +
				"</project>";

		List<Xml.Document> parse = MavenParser.builder().build().parse(pomXml);
	}

	@Test
	void test(@TempDir Path tempDir) {
		String pomXml =
				"<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
						"    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
						"    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
						"    <modelVersion>4.0.0</modelVersion>\n" +
						"    <groupId>foo</groupId>\n" +
						"    <artifactId>bar</artifactId>\n" +
						"    <version>0.0.1-SNAPSHOT</version>\n" +
						"    <name>foobat</name>\n" +
						"    <repositories>\n" +
						"        <repository>\n" +
						"            <id>jcenter</id>\n" +
						"            <name>jcenter</name>\n" +
						"            <url>https://jcenter.bintray.com</url>\n" +
						"        </repository>\n" +
						"        <repository>\n" +
						"            <id>mavencentral</id>\n" +
						"            <name>mavencentral</name>\n" +
						"            <url>https://repo.maven.apache.org/maven2</url>\n" +
						"        </repository>\n" +
						"    </repositories>" +
						"    <dependencies>\n" +
						"        <dependency>\n" +
						"            <groupId>org.apache.tomee</groupId>\n" +
						"            <artifactId>openejb-core-hibernate</artifactId>\n" +
						"            <version>8.0.5</version>\n" +
//						"            <type>pom</type>\n" + // FIXME:
						"        </dependency>\n" +
						"    </dependencies>\n" +
						"</project>";

		Xml.Document document = MavenParser.builder().build().parse(pomXml).get(0);
		MavenResolutionResult r = document.getMarkers().findFirst(MavenResolutionResult.class).get();

		InMemoryExecutionContext executionContext = new InMemoryExecutionContext((t) -> System.out.println(t.getMessage()));
		MavenExecutionContextView ctx = MavenExecutionContextView.view(executionContext);
		ctx.setPomCache(new RocksdbMavenPomCache(tempDir.resolve("rewrite-cache")));
		HashMap<Path, Pom> projectPoms = new HashMap<>();
		MavenPomDownloader mavenPomDownloader = new MavenPomDownloader(projectPoms, ctx);
		List<ResolvedDependency> resolvedDependencies = r.getDependencies().get(Scope.Compile);
		assertThat(r.getDependencies()).hasSize(4);
		assertThat(resolvedDependencies).hasSize(84);
	}

}
