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
package org.springframework.sbm.build.impl;

import org.junit.jupiter.api.Test;
import org.openrewrite.maven.internal.MavenXmlMapper;

import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Plugin;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class OpenRewriteMavenPluginTest {

	@Test
	void serializePlugin() {
		String pomXml =
				"""
						<?xml version="1.0" encoding="UTF-8"?>
						<project xmlns="http://maven.apache.org/POM/4.0.0"\s
						    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\s
						         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
						    <modelVersion>4.0.0</modelVersion>
						    <groupId>com.vmwre.sbm.examples</groupId>
						    <artifactId>migrate-jsf-2.x-to-spring-boot</artifactId>
						    <version>1.0</version>
						    <packaging>jar</packaging>
						    <properties>
						        <maven.compiler.source>17</maven.compiler.source>
						    </properties>
						    <build>
						        <plugins>
						            <plugin>
						                <groupId>org.apache.maven.plugins</groupId>
						                <artifactId>maven-compiler-plugin</artifactId>
						                <version>3.10.1</version>
						                <executions>
						                    <execution>
						                        <id>default-compile</id>
						                        <configuration>
						                            <release>9</release>
						                         </configuration>
						                    </execution>
						                    <execution>
						                        <id>base-compile</id>
						                        <goals>
						                            <goal>compile</goal>
						                        </goals>
						                        <configuration>
						                            <excludes>
						                                <exclude>module-info.java</exclude>
						                            </excludes>
						                        </configuration>
						                    </execution>
						                </executions>
						                <configuration>
						                    <maven.compiler.source>${maven.compiler.source}</maven.compiler.source>
						                    <maven.compiler.target>17</maven.compiler.target>
						                </configuration>
						            </plugin>        </plugins>
						    </build>
						</project>
						""";

		BuildFile buildFile = TestProjectContext.buildProjectContext()
				.withMavenRootBuildFileSource(pomXml)
				.build()
				.getBuildFile();

		Plugin plugin = buildFile.getPlugins().get(0);

		assertThat(plugin.getGroupId()).isEqualTo("org.apache.maven.plugins");
		assertThat(plugin.getArtifactId()).isEqualTo("maven-compiler-plugin");
		assertThat(plugin.getVersion()).isEqualTo("3.10.1");

		//Plugin execution Tests
		assertThat(plugin.getExecutions()).hasSize(2);
		assertThat(plugin.getExecutions().get(0).getId()).isEqualTo("default-compile");
		assertThat(plugin.getExecutions().get(1).getId()).isEqualTo("base-compile");
		assertThat(plugin.getExecutions().get(1).getGoals()).hasSize(1);
		assertThat(plugin.getExecutions().get(1).getGoals().get(0)).isEqualTo("compile");


		// Plugin Configuration Tests
		assertThat(plugin.getConfiguration()).isNotNull();
//		assertThat(plugin.getConfiguration().getConfigurationPropertyKeys()).hasSize(2);
		assertThat(plugin.getConfiguration().getDeclaredStringValue("maven.compiler.source").orElseThrow())
				.isEqualTo("${maven.compiler.source}");
		assertThat(plugin.getConfiguration().getDeclaredStringValue("maven.compiler.target").orElseThrow())
				.isEqualTo("17");
//		assertThat(plugin.getConfiguration().getResolvedStringValue("maven.compiler.source"))
//				.isEqualTo("17");
//		assertThat(plugin.getConfiguration().getResolvedStringValue("maven.compiler.target"))
//				.isEqualTo("17");

	}


	@Test
	void deSerializePluginString() {

		String pomXml =
				"""
						<?xml version="1.0" encoding="UTF-8"?>
						<project xmlns="http://maven.apache.org/POM/4.0.0"\s
						    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\s
						         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
						    <modelVersion>4.0.0</modelVersion>
						    <groupId>com.vmwre.sbm.examples</groupId>
						    <artifactId>migrate-jsf-2.x-to-spring-boot</artifactId>
						    <version>1.0</version>
						    <packaging>jar</packaging>
						    <properties>
						        <maven.compiler.source>17</maven.compiler.source>
						    </properties>
						    <build>
						        <plugins>
						            <plugin>
						                <groupId>org.apache.maven.plugins</groupId>
						                <artifactId>maven-compiler-plugin</artifactId>
						                <version>3.10.1</version>
						                <executions>
						                    <execution>
						                        <id>default-compile</id>
						                        <configuration>
						                            <release>9</release>
						                         </configuration>
						                    </execution>
						                    <execution>
						                        <id>base-compile</id>
						                        <goals>
						                            <goal>compile</goal>
						                        </goals>
						                        <configuration>
						                            <excludes>
						                                <exclude>module-info.java</exclude>
						                            </excludes>
						                        </configuration>
						                    </execution>
						                </executions>
						                <configuration>
						                    <maven.compiler.source>${maven.compiler.source}</maven.compiler.source>
						                    <maven.compiler.target>17</maven.compiler.target>
						                </configuration>
						            </plugin>        </plugins>
						    </build>
						</project>
						""";

		BuildFile buildFile = TestProjectContext.buildProjectContext()
				.withMavenRootBuildFileSource(pomXml)
				.build()
				.getBuildFile();

		Plugin plugin = buildFile.getPlugins().get(0);
		plugin.getConfiguration().setDeclaredStringValue("maven.compiler.source","17");
		assertThat(plugin.getConfiguration().getDeclaredStringValue("maven.compiler.source").orElseThrow())
				.isEqualTo("17");
	}
}