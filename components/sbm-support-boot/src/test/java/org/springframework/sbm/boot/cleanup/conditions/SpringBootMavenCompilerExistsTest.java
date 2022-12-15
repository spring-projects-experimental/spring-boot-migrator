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
package org.springframework.sbm.boot.cleanup.conditions;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringBootMavenCompilerExistsTest {

	@MethodSource("input")
	@ParameterizedTest()
	public void springBootDependencyAndMavenCompilerPluginExists(String pomXml, boolean expectedResult) {

		SpringBootMavenCompilerExists target = new SpringBootMavenCompilerExists();

		ProjectContext context = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build();
		boolean result = target.evaluate(context);

		assertThat(result).isEqualTo(expectedResult);
	}

	private static Stream<Arguments> input() {
		return Stream.of(
				Arguments.of(
						"""
								<?xml version="1.0" encoding="UTF-8"?>
										   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
											   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
											   <modelVersion>4.0.0</modelVersion>
											   <parent>
								      		       <groupId>org.springframework.boot</groupId>
								      		       <artifactId>spring-boot-starter-parent</artifactId>
								      		       <version>2.3.9.RELEASE</version>
								      		       <relativePath/> <!-- lookup parent from repository -->
								      	       </parent>
											   <groupId>com.example</groupId>
											   <artifactId>demo</artifactId>
											   <version>0.0.1-SNAPSHOT</version>
											   <dependencyManagement>
												   <dependencies>
													   <dependency>
														   <groupId>org.springframework.boot</groupId>
														   <artifactId>spring-boot-dependencies</artifactId>
														   <version>2.7.4</version>
														   <type>pom</type>
														   <scope>import</scope>
													   </dependency>
												   </dependencies>
											   </dependencyManagement>
											   <build>
											<plugins>
												<plugin>
													<groupId>org.apache.maven.plugins</groupId>
													<artifactId>maven-compiler-plugin</artifactId>
													<configuration>
														<source>${maven.compiler.source}</source>
														<target>${maven.compiler.target}</target>
													</configuration>
												</plugin>
											</plugins>
										</build>
										   </project>
										   """,
						true),
				Arguments.of(
						"""
								<?xml version="1.0" encoding="UTF-8"?>
												           <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
												           	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
												           	<modelVersion>4.0.0</modelVersion>
												           	<groupId>com.example</groupId>
												           	<artifactId>demo</artifactId>
												           	<version>0.0.1-SNAPSHOT</version>
												           	<build>
															<plugins>
																<plugin>
																	<groupId>org.apache.maven.plugins</groupId>
																	<artifactId>maven-compiler-plugin</artifactId>
																	<configuration>
																		<source>${maven.compiler.source}</source>
																		<target>${maven.compiler.target}</target>
																	</configuration>
																</plugin>
															</plugins>
														</build>
												           </project>

								""",
						false),
				Arguments.of(
						"""
								<?xml version="1.0" encoding="UTF-8"?>
								           <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
								           	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
								           	<modelVersion>4.0.0</modelVersion>
								           	<groupId>com.example</groupId>
								           	<artifactId>demo</artifactId>
								           	<version>0.0.1-SNAPSHOT</version>
								           	<dependencyManagement>
												<dependencies>
													<dependency>
														<groupId>org.springframework.boot</groupId>
														<artifactId>spring-boot-dependencies</artifactId>
														<version>2.7.4</version>
														<type>pom</type>
														<scope>import</scope>
													</dependency>
												</dependencies>
											</dependencyManagement>

								           </project>
								           """,
						false));
	}

}
