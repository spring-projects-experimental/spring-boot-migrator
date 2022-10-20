package org.springframework.sbm.boot;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml.Document;

import org.springframework.sbm.openrewrite.MavenRefactoringTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

class ChangeMavenPluginConfigurationTest {

	@Test
	void customProperty() {
		Recipe recipe = new ChangeMavenCompilerPluginConfiguration();

		InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
			throw new RuntimeException("Error due ChangeMavenCompilerPluginConfiguration Recipe: " + ex.getMessage(),
					ex);
		});

		MavenParser parser = MavenParser.builder().build();
		List<Document> documentList = parser
				.parse("""
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
						                """);

		List<Result> result = recipe.run(documentList, ctx).getResults();

		assertThat(result).hasSize(1);

		assertThat(result.get(0).getAfter().printAll()).isEqualTo(
				"""
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
						                    <source>${maven.compiler.source}</source>
						                    <target>${maven.compiler.target}</target>
						                </configuration>
						            </plugin>
						        </plugins>
						    </build>
						</project>
						""");
	}

	@Test
	void fixedValue() {
		Recipe recipe = new ChangeMavenCompilerPluginConfiguration();

		InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
			throw new RuntimeException("Error due ChangeMavenCompilerPluginConfiguration Recipe: " + ex.getMessage(),
					ex);
		});

		MavenParser parser = MavenParser.builder().build();
		List<Document> documentList = parser
				.parse("""
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
						                """);

		List<Result> result = recipe.run(documentList, ctx).getResults();

		assertThat(result).hasSize(1);

		assertThat(result.get(0).getAfter().printAll()).isEqualTo(
				"""
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
						                    <source>${maven.compiler.source}</source>
						                    <target>${maven.compiler.target}</target>
						                </configuration>
						            </plugin>
						        </plugins>
						    </build>
						</project>
						""");
	}



}