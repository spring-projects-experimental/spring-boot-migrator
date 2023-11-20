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

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin;
import org.springframework.sbm.build.migration.actions.OpenRewriteMavenBuildFileTestSupport;
import org.springframework.sbm.test.util.PomBuilder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class RemoveRedundantMavenCompilerPluginPropertiesTest {

	@Test
	void mavenCompilerPluginNotDefined() {
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
				                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
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
				                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				                """;

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);

	}

	@Test
	void sourceAndTargetVersionDifferent() {
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
				                    <source>1.8</source>
				                    <target>1.6</target>
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
				                    <source>1.8</source>
				                    <target>1.6</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				""";

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);

	}

	@Test
	void mavenCompilerProperties() {
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
				    <properties>
				        <maven.compiler.source>17</maven.compiler.source>
				        <maven.compiler.target>17</maven.compiler.target>
				    </properties>
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
				    <properties>
				        <java.version>17</java.version>
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${java.version}</source>
				                    <target>${java.version}</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				""";

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);
	}

	@Test
	void customProperty() {
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
				    <properties>
				        <source>17</source>
				        <target>17</target>
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${source}</source>
				                    <target>${target}</target>
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
				    <properties>
				        <java.version>17</java.version>
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${java.version}</source>
				                    <target>${java.version}</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				""";

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);
	}

	@Test
	void fixedValueAndExistingJavaVersionProperty() {
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
				    <properties>
				        <java.version>15</java.version>
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>17</source>
				                    <target>17</target>
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
				    <properties>
				        <java.version>17</java.version>
				    </properties>
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.apache.maven.plugins</groupId>
				                <artifactId>maven-compiler-plugin</artifactId>
				                <configuration>
				                    <source>${java.version}</source>
				                    <target>${java.version}</target>
				                </configuration>
				            </plugin>
				        </plugins>
				    </build>
				</project>
				""";

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
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
				            </plugin>
				        </plugins>
				    </build>
				</project>
				""";

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomXml, expected, sut);
	}

	@Test
	void multiModuleTest() {

		String parentPomStr = """
						<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
						    <modelVersion>4.0.0</modelVersion>
						    <parent>
						      		<groupId>org.springframework.boot</groupId>
						      		<artifactId>spring-boot-starter-parent</artifactId>
						      		<version>2.7.5</version>
						      		<relativePath/>
						    </parent>
						    <properties>
						        <maven.compiler.source>17</maven.compiler.source>
						        <maven.compiler.target>17</maven.compiler.target>
						    </properties>
						    <groupId>com.example</groupId>
						    <artifactId>parent</artifactId>
						    <version>1.0</version>
						    <modules>
						        <module>module1</module>
						    </modules>
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

		String childPom1 = """
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
				    <modelVersion>4.0.0</modelVersion>
				    <parent>
				        <groupId>com.example</groupId>
				        <artifactId>parent</artifactId>
				        <version>1.0</version>
				    </parent>
				    <artifactId>module1</artifactId>

				</project>
				""";

		ProjectContext projectContext = TestProjectContext.buildProjectContext()
				.withMavenRootBuildFileSource(parentPomStr).withMavenBuildFileSource("module1", childPom1).build();

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		sut.apply(projectContext);

	}

	@Test
	void multiModuleWithPluginAndPropertiesDefinedInParentModule() {

		OpenRewriteMavenPlugin mavenPlugin = OpenRewriteMavenPlugin.builder()
				.groupId("org.apache.maven.plugins")
				.artifactId("maven-compiler-plugin")
				.build();
		mavenPlugin.setConfiguration(mavenPlugin.new OpenRewriteMavenPluginConfiguration(Map.of("source", "${maven.compiler.source}",
																								"target", "${maven.compiler.target}")));

		String rootPom = PomBuilder.buildPom("com.example:parent:1.0")
				.packaging("pom")
				.plugins(mavenPlugin)
				.property("maven.compiler.source", "17")
				.property("maven.compiler.target", "17")
				.withModules("module1")
				.build();

		String module1Pom = PomBuilder.buildPom("com.example:parent:1.0", "module1").build();

		ProjectContext projectContext = TestProjectContext.buildProjectContext()
				.withMavenBuildFileSource("pom.xml", rootPom).withMavenBuildFileSource("module1/pom.xml", module1Pom)
				.build();

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		sut.apply(projectContext);

		Module rootModule = projectContext.getApplicationModules().getRootModule();
		assertThat(rootModule.getBuildFile().getProperty("source")).isNull();
		assertThat(rootModule.getBuildFile().getProperty("target")).isNull();
		assertThat(rootModule.getBuildFile().getProperty("java.version")).isEqualTo("17");
		assertThat(
				rootModule.getBuildFile().getPlugins().get(0).getConfiguration().getDeclaredStringValue("source").get())
				.isEqualTo("${java.version}");
		assertThat(
				rootModule.getBuildFile().getPlugins().get(0).getConfiguration().getDeclaredStringValue("target").get())
				.isEqualTo("${java.version}");
	}

	@Test
	void multiModuleWithPluginDefinedInParentModuleAndPropertiesInChildModule() {
		OpenRewriteMavenPlugin mavenPlugin = OpenRewriteMavenPlugin.builder()
				.groupId("org.apache.maven.plugins")
				.artifactId("maven-compiler-plugin")
				.build();
		mavenPlugin.setConfiguration(mavenPlugin.new OpenRewriteMavenPluginConfiguration(
				Map.of(
						"source", "${maven.compiler.source}",
						"target", "${maven.compiler.target}"
				)
		));

		String rootPom = PomBuilder.buildPom("com.example:parent:1.0")
				.packaging("pom")
				.property("maven.compiler.source", "17")
				.property("maven.compiler.target", "17")
				.withModules("module1")
				.plugins(mavenPlugin)
				.build();

		String module1Pom = PomBuilder.buildPom("com.example:parent:1.0", "module1")
				.packaging("jar")
				.property("maven.compiler.source", "17")
				.property("maven.compiler.target", "17")
				.build();

		ProjectContext projectContext = TestProjectContext.buildProjectContext()
				.withMavenBuildFileSource("pom.xml", rootPom)
				.withMavenBuildFileSource("module1/pom.xml", module1Pom)
				.build();


		// Remove redundant plugin properties
		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		sut.apply(projectContext);

		// verify root module
		BuildFile rootModule = projectContext.getApplicationModules().getRootModule().getBuildFile();
		assertThat(rootModule.getProperty("maven.compiler.source")).isNull();
		assertThat(rootModule.getProperty("maven.compiler.target")).isNull();
		assertThat(rootModule.getProperty("java.version")).isEqualTo("17");
		assertThat(rootModule.getPlugins().get(0).getConfiguration().getDeclaredStringValue("target").get()).isEqualTo("${java.version}");
		assertThat(rootModule.getPlugins().get(0).getConfiguration().getDeclaredStringValue("source").get()).isEqualTo("${java.version}");


		BuildFile childModule = projectContext.getApplicationModules().getModule("module1").getBuildFile();
		assertThat(childModule.getProperty("java.version")).isEqualTo("17");
		assertThat(childModule.getProperty("maven.compiler.source")).isNull();
		assertThat(childModule.getProperty("maven.compiler.target")).isNull();
	}

	@Test
	void multiModuleWithPluginAndPropertiesDefinedInChildModule() {

		OpenRewriteMavenPlugin mavenPlugin = OpenRewriteMavenPlugin.builder()
				.groupId("org.apache.maven.plugins")
				.artifactId("maven-compiler-plugin")
				.build();
		Map<String, Object> configMap = new LinkedHashMap<>();
		configMap.put("source", "${maven.compiler.source}");
		configMap.put("target", "${maven.compiler.target}");
		mavenPlugin.setConfiguration(mavenPlugin.new OpenRewriteMavenPluginConfiguration(configMap));

		String rootPom = PomBuilder.buildPom("com.example:parent:1.0").packaging("pom")
				.withModules("module1")
				.build();

		String module1Pom = PomBuilder.buildPom("com.example:parent:1.0", "module1").packaging("jar")
				.property("maven.compiler.source", "17").property("maven.compiler.target", "17")
				.plugins(mavenPlugin).build();

		ProjectContext projectContext = TestProjectContext.buildProjectContext()
				.withMavenBuildFileSource("pom.xml", rootPom).withMavenBuildFileSource("module1/pom.xml", module1Pom)
				.build();

		RemoveRedundantMavenCompilerPluginProperties sut = new RemoveRedundantMavenCompilerPluginProperties();
		sut.apply(projectContext);

		Module rootModule = projectContext.getApplicationModules().getRootModule();
		assertThat(rootModule.getBuildFile().print()).isEqualTo(rootPom);


		Module childModule = projectContext.getApplicationModules().getModule("module1");
		assertThat(childModule.getBuildFile().getProperty("source")).isNull();
		assertThat(childModule.getBuildFile().getProperty("target")).isNull();
		assertThat(childModule.getBuildFile().getProperty("java.version")).isEqualTo("17");
		assertThat(
				childModule.getBuildFile().getPlugins().get(0).getConfiguration().getDeclaredStringValue("source").get())
				.isEqualTo("${java.version}");
		assertThat(
				childModule.getBuildFile().getPlugins().get(0).getConfiguration().getDeclaredStringValue("target").get())
				.isEqualTo("${java.version}");

	}

	@Test
	void multiModuleWithPluginDefinedInChildAndPropertiesInParentModule() {

	}

	@Test
	void multiModulePluginDefinedInParentModuleAndDifferentPropertiesInChildModule() {

	}

}