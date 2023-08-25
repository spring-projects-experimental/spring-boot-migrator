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
package org.springframework.sbm.boot.cleanup.actions;

import org.junit.jupiter.api.Test;

import org.springframework.sbm.build.migration.actions.OpenRewriteMavenBuildFileTestSupport;

class RemoveRedundantMavenCompilerPluginTest {

	@Test
	void standardConfigWithSourceAndTarget() {

		String pomXml = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
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
				                """;
		String expected = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				</project>
				""";

		RemoveRedundantMavenCompilerPlugin sut = new RemoveRedundantMavenCompilerPlugin();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);
	}

	@Test
	void additionalConfig() {
		String pomXml = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${maven.compiler.source}</source>
				                    <target>${maven.compiler.target}</target>
				                    <fork>false</fork>
				                    <showWarnings>true</showWarnings>
				                    <showDeprecation>true</showDeprecation>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				                """;
		String expected = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${maven.compiler.source}</source>
				                    <target>${maven.compiler.target}</target>
				                    <fork>false</fork>
				                    <showWarnings>true</showWarnings>
				                    <showDeprecation>true</showDeprecation>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				                """;

		RemoveRedundantMavenCompilerPlugin sut = new RemoveRedundantMavenCompilerPlugin();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);
	}

	@Test
	void emptyConfiguration() {

		String pomXml = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration/>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				                """;
		String expected = """
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <groupId>org.springframework.boot</groupId>
				    <artifactId>spring-boot-starter-parent</artifactId>
				    <version>2.7.3</version>
				    <name>clean-maven-properties</name>
				    <description>Remove Redundant configuration</description>
				</project>
				""";

		RemoveRedundantMavenCompilerPlugin sut = new RemoveRedundantMavenCompilerPlugin();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);
	}

//	@Test
//	void multiModuleWithPluginDefinedInParentTest() {
//		OpenRewriteMavenPlugin plugin = OpenRewriteMavenPlugin.builder()
//				.groupId("org.apache.maven.plugins")
//				.artifactId("maven-compiler-plugin")
//				.build();
//		plugin.addConfiguration("source","17");
//		plugin.addConfiguration("target","17");
//
//		String rootPom = PomBuilder
//				.buildPom("com.example:parent:1.0")
//				.packaging("pom")
//				.withModules("module1")
//				.plugins(plugin)
//				.build();
//
//
//		String module1Pom = PomBuilder
//				.buildPom("com.example:parent:1.0", "module1")
//				.build();
//
//		String expectedParentPom = """
//				<?xml version="1.0" encoding="UTF-8"?>
//				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
//				    <modelVersion>4.0.0</modelVersion>
//				    <groupId>com.example</groupId>
//				    <artifactId>parent</artifactId>
//				    <version>1.0</version>
//				    <packaging>pom</packaging>
//				    <modules>
//				        <module>module1</module>
//				    </modules>
//				</project>""";
//
//		ProjectContext projectContext = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(rootPom)
//				.withMavenBuildFileSource("module1", module1Pom).build();
//
//		RemoveRedundantMavenCompilerPlugin sut = new RemoveRedundantMavenCompilerPlugin();
//		sut.apply(projectContext);
//
//		Module parentModule = projectContext.getApplicationModules().getRootModule();
//		assertThat(expectedParentPom).isEqualTo(parentModule.getBuildFile().print());
//		System.out.println(parentModule.getBuildFile().getPlugins().size());
//
//	}
//
//	@Test
//	void multiModuleWithPluginDefinedInChildTest() {
//
//		String rootPom = PomBuilder
//				.buildPom("com.example:parent:1.0")
//				.packaging("pom")
//				.withModules("module1")
//				.build();
//
//		OpenRewriteMavenPlugin plugin = OpenRewriteMavenPlugin.builder()
//				.groupId("org.apache.maven.plugins")
//				.artifactId("maven-compiler-plugin")
//				.build();
//		plugin.addConfiguration("source","17");
//		plugin.addConfiguration("target","17");
//
//
//		String module1Pom = PomBuilder
//				.buildPom("com.example:parent:1.0", "module1")
//				.plugins(plugin)
//				.build();
//
//		String expectedChildPom = """
//				<?xml version="1.0" encoding="UTF-8"?>
//				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
//				    <modelVersion>4.0.0</modelVersion>
//				    <parent>
//				        <groupId>com.example</groupId>
//				        <artifactId>parent</artifactId>
//				        <version>1.0</version>
//				    </parent>
//				    <artifactId>module1</artifactId>
//				</project>""";
//
//		ProjectContext projectContext = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(rootPom)
//				.withMavenBuildFileSource("module1", module1Pom).build();
//
//		RemoveRedundantMavenCompilerPlugin sut = new RemoveRedundantMavenCompilerPlugin();
//		sut.apply(projectContext);
//
//		Module childModule = projectContext.getApplicationModules().getModule("module1");
//		assertThat(expectedChildPom).isEqualTo(childModule.getBuildFile().print());
//	}
//
//	@Test
//	void multiModuleWithPluginDefinedInParentAndChildTest() {
//
//		OpenRewriteMavenPlugin plugin = OpenRewriteMavenPlugin.builder()
//				.groupId("org.apache.maven.plugins")
//				.artifactId("maven-compiler-plugin")
//				.build();
//		plugin.addConfiguration("source","${maven.compiler.source}");
//		plugin.addConfiguration("target","${maven.compiler.target}");
//
//		String rootPom = PomBuilder
//				.buildPom("com.example:parent:1.0")
//				.packaging("pom")
//				.withModules("module1")
//				.plugins(plugin)
//				.build();
//
//
//		String module1Pom = PomBuilder
//				.buildPom("com.example:parent:1.0", "module1")
//				.plugins(plugin)
//				.build();
//
//		String expectedParentPom = """
//				<?xml version="1.0" encoding="UTF-8"?>
//				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
//				    <modelVersion>4.0.0</modelVersion>
//				    <groupId>com.example</groupId>
//				    <artifactId>parent</artifactId>
//				    <version>1.0</version>
//				    <packaging>pom</packaging>
//				    <modules>
//				        <module>module1</module>
//				    </modules>
//				</project>""";
//
//		String expectedChildPom = """
//				<?xml version="1.0" encoding="UTF-8"?>
//				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
//				    <modelVersion>4.0.0</modelVersion>
//				    <parent>
//				        <groupId>com.example</groupId>
//				        <artifactId>parent</artifactId>
//				        <version>1.0</version>
//				    </parent>
//				    <artifactId>module1</artifactId>
//				</project>""";
//
//		ProjectContext projectContext = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(rootPom)
//				.withMavenBuildFileSource("module1", module1Pom).build();
//
//		RemoveRedundantMavenCompilerPlugin sut = new RemoveRedundantMavenCompilerPlugin();
//		sut.apply(projectContext);
//
//		Module rootModule = projectContext.getApplicationModules().getRootModule();
//		Module childModule = projectContext.getApplicationModules().getModule("module1");
//		assertThat(rootModule.getBuildFile().print()).isEqualTo(expectedParentPom);
//		assertThat(childModule.getBuildFile().print()).isEqualTo(expectedChildPom);
//
//	}

}