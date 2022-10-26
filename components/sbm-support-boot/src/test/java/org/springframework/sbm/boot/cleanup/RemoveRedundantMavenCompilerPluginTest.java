package org.springframework.sbm.boot.cleanup;

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
}