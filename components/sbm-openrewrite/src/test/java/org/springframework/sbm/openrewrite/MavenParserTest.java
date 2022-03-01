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
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.Problem;

import java.util.List;

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
				"    <dependencies>\n" +
				"    </dependencies>\n" +
				"</project>";

		List<Xml.Document> parse = MavenParser.builder().build().parse(pomXml);
	}

}
