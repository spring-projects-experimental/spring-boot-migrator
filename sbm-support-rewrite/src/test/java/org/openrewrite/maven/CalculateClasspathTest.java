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

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.SourceFile;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.sbm.boot.autoconfigure.ScannerConfiguration;
import org.springframework.sbm.parsers.RewriteProjectParser;
import org.springframework.sbm.parsers.RewriteProjectParsingResult;
import org.springframework.sbm.test.util.DummyResource;
import org.springframework.sbm.test.util.TestProjectHelper;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@SpringBootTest(classes = { ScannerConfiguration.class })
public class CalculateClasspathTest {

	@Autowired
	RewriteProjectParser parser;

	@Test
	@DisplayName("classpath for single-module project")
	void classpathForSingleModuleProject(@TempDir Path tmpDir) {
		@Language("xml")
		String pom = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>com.example</groupId>
				    <artifactId>example-1</artifactId>
				    <version>0.1.0-SNAPSHOT</version>
				    <properties>
				        <maven.compiler.target>17</maven.compiler.target>
				        <maven.compiler.source>17</maven.compiler.source>
				    </properties>
				    <dependencies>
				        <dependency>
				            <groupId>javax.validation</groupId>
				            <artifactId>validation-api</artifactId>
				            <version>2.0.1.Final</version>
				        </dependency>
				        <dependency>
				            <groupId>org.junit.jupiter</groupId>
				            <artifactId>junit-jupiter-api</artifactId>
				            <version>5.9.3</version>
				            <scope>test</scope>
				        </dependency>
				    </dependencies>
				</project>
				""";

		@Language("java")
		String mainClass = """
				package com.example;
				import javax.validation.constraints.Min;

				public class MainClass {
				    @Min("10")
				    private int value;
				}
				""";

		@Language("java")
		String testClass = """
				package com.example;
				import org.junit.jupiter.api.Test;

				public class TestClass {
				    @Test
				    void someTest() {}
				}
				""";

		Path baseDir = tmpDir.resolve("/example-1").toAbsolutePath().normalize();
		List<Resource> resources = List.of(new DummyResource(baseDir.resolve("pom.xml"), pom),
				new DummyResource(baseDir.resolve("src/main/java/com/example/MainClass.java"), mainClass),
				new DummyResource(baseDir.resolve("src/test/java/com/example/TestClass.java"), testClass));

		RewriteProjectParsingResult parsingResult = parser.parse(baseDir, resources);

		// verify types in use
		SourceFile mainSourceFile = parsingResult.sourceFiles().get(1);
		J.CompilationUnit mainCu = (J.CompilationUnit) mainSourceFile;
		// Having Min annotation resolved proves type resolution is working for main
		// resources
		assertThat(mainCu.getTypesInUse().getTypesInUse().stream().map(t -> t.toString()))
			.containsExactlyInAnyOrder("int", "String", "javax.validation.constraints.Min");

		SourceFile testSourceFile = parsingResult.sourceFiles().get(2);
		J.CompilationUnit testCu = (J.CompilationUnit) testSourceFile;
		// Having Test annotation resolved proves type resolution is working for test
		// resources
		assertThat(testCu.getTypesInUse().getTypesInUse().stream().map(t -> t.toString()))
			.containsExactlyInAnyOrder("void", "org.junit.jupiter.api.Test");

		// verify classpath
		List<String> mainClasspath = mainCu.getMarkers()
			.findFirst(JavaSourceSet.class)
			.get()
			.getClasspath()
			.stream()
			.map(JavaType.FullyQualified::getFullyQualifiedName)
			.toList();
		// Min is on main classpath
		assertThat(mainClasspath).contains("javax.validation.constraints.Min");
		// Test is not
		assertThat(mainClasspath).doesNotContain("org.junit.jupiter.api.Test");

		List<String> testClasspath = testCu.getMarkers()
			.findFirst(JavaSourceSet.class)
			.get()
			.getClasspath()
			.stream()
			.map(JavaType.FullyQualified::getFullyQualifiedName)
			.toList();
		// all main classes on test classpath
		assertThat(testClasspath).containsAll(mainClasspath);
		// plus the classes from test dependencies
		assertThat(testClasspath).contains("org.junit.jupiter.api.Test");
	}

	/**
	 * Given a multi-module Maven reactor project. - Where module A depends on B and both
	 * inherit from same parent. - Module A has a
	 */
	@Test
	@DisplayName("classpath for reactor build")
	void classpathForReactorBuild() {
		Path mavenProject = TestProjectHelper.getMavenProject("classpath-test/example-1");
		RewriteProjectParsingResult parsingResult = parser.parse(mavenProject);
	}

}
