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
package org.springframework.sbm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.openrewrite.maven.MavenParser;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class ParserTest {
    @Test
    @DisplayName("MavenParser should parse pom")
    void mavenParserShouldParsePom() {
        SourceFile sourceFile = MavenParser.builder().build().parse("""
				<?xml version="1.0" encoding="UTF-8"?>
				<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
						 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
					<modelVersion>4.0.0</modelVersion>
					<parent>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-starter-parent</artifactId>
						<version>2.6.3</version>
						<relativePath/> <!-- lookup parent from repository -->
					</parent>
					<groupId>com.example</groupId>
					<artifactId>demo</artifactId>
					<version>0.0.1-SNAPSHOT</version>
					<name>demo</name>
					<description>Demo project for Spring Data JPA</description>
					<properties>
						<java.version>1.8</java.version>
					</properties>
					<dependencies>
						<dependency>
							<groupId>org.springframework.boot</groupId>
							<artifactId>spring-boot-starter-data-jpa</artifactId>
						</dependency>
				    
						<dependency>
							<groupId>com.h2database</groupId>
							<artifactId>h2</artifactId>
							<scope>runtime</scope>
						</dependency>
						<dependency>
							<groupId>org.springframework.boot</groupId>
							<artifactId>spring-boot-starter-test</artifactId>
							<scope>test</scope>
						</dependency>
					</dependencies>
				    
					<build>
						<plugins>
							<plugin>
								<groupId>org.springframework.boot</groupId>
								<artifactId>spring-boot-maven-plugin</artifactId>
							</plugin>
						</plugins>
					</build>
				    
				</project>
				""").toList().get(0);
		assertThat(sourceFile).isNotNull();
    }
}
