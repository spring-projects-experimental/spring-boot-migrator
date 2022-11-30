package org.springframework.sbm.build.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PomBuilderTest {

	@Test
	void testPomBuilderWithFields() {
		String pom = PomBuilder
				.buildPom("com.example:parent:1.0")
				.packaging("pom")
				.property("java.version", "17")
				.dependencies("org.openrewrite:rewrite-maven:7.33.0","org.projectlombok:lombok:1.18.24")

				.build();

		String pomXml =
				"""
						<?xml version="1.0" encoding="UTF-8"?>
						<project xmlns="http://maven.apache.org/POM/4.0.0"\s
						    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\s
						         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
						    <modelVersion>4.0.0</modelVersion>
						    <groupId>com.example</groupId>
						    <artifactId>parent</artifactId>
						    <version>1.0</version>
						    <packaging>pom</packaging>
						    <properties>
						        <java.version>17</java.version>
						    </properties>
						    <dependencies>
						        <dependency>
						            <groupId>org.openrewrite</groupId>
						            <artifactId>rewrite-maven</artifactId>
						            <version>7.33.0</version>
						        </dependency>
						        <dependency>
						            <groupId>org.projectlombok</groupId>
						            <artifactId>lombok</artifactId>
						            <version>1.18.24</version>
						        </dependency>
						    </dependencies>
						</project>
						""";

		assertThat(pom).isEqualTo(pomXml);

	}
}