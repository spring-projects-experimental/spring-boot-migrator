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
import org.openrewrite.SourceFile;
import org.openrewrite.maven.internal.MavenXmlMapper;
import org.openrewrite.maven.internal.RawPom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class MavenParserIssueTest {

    @Test
    @DisplayName("parsing pom fails")
    void parsingPomFails() {
        @Language("xml")
        String pom =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xmlns="http://maven.apache.org/POM/4.0.0"
                  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>org.springframework.samples</groupId>
                  <artifactId>spring-petclinic</artifactId>
                  <version>2.3.0.BUILD-SNAPSHOT</version>
                                
                  <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>2.3.5.RELEASE</version>
                  </parent>
                  <name>petclinic</name>
                                
                  <properties>
                                
                    <!-- Generic properties -->
                    <java.version>1.8</java.version>
                    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
                                
                    <!-- Web dependencies -->
                    <webjars-bootstrap.version>3.3.6</webjars-bootstrap.version>
                    <webjars-jquery-ui.version>1.11.4</webjars-jquery-ui.version>
                    <webjars-jquery.version>2.2.4</webjars-jquery.version>
                    <wro4j.version>1.9.0</wro4j.version>
                                
                    <jacoco.version>0.8.5</jacoco.version>
                    <nohttp-checkstyle.version>0.0.4.RELEASE</nohttp-checkstyle.version>
                    <spring-format.version>0.0.25</spring-format.version>
                  </properties>
                                
                  <dependencies>
                    <!-- Spring and Spring Boot dependencies -->
                    <dependency>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-actuator</artifactId>
                    </dependency>
                    <dependency>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-cache</artifactId>
                    </dependency>
                    <dependency>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-data-jpa</artifactId>
                    </dependency>
                    <dependency>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-web</artifactId>
                    </dependency>
                    <dependency>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-validation</artifactId>
                    </dependency>
                    <dependency>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-thymeleaf</artifactId>
                    </dependency>
                    <dependency>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-test</artifactId>
                      <scope>test</scope>
                        <exclusions>
                            <exclusion>
                                <groupId>org.junit.vintage</groupId>
                                <artifactId>junit-vintage-engine</artifactId>
                            </exclusion>
                        </exclusions>
                    </dependency>
                                
                    <!-- Databases - Uses H2 by default -->
                    <dependency>
                      <groupId>com.h2database</groupId>
                      <artifactId>h2</artifactId>
                      <scope>runtime</scope>
                    </dependency>
                    <dependency>
                      <groupId>mysql</groupId>
                      <artifactId>mysql-connector-java</artifactId>
                      <scope>runtime</scope>
                    </dependency>
                                
                    <!-- caching -->
                    <dependency>
                      <groupId>javax.cache</groupId>
                      <artifactId>cache-api</artifactId>
                    </dependency>
                    <dependency>
                      <groupId>org.ehcache</groupId>
                      <artifactId>ehcache</artifactId>
                    </dependency>
                                
                    <!-- webjars -->
                    <dependency>
                      <groupId>org.webjars</groupId>
                      <artifactId>webjars-locator-core</artifactId>
                    </dependency>
                    <dependency>
                      <groupId>org.webjars</groupId>
                      <artifactId>jquery</artifactId>
                      <version>${webjars-jquery.version}</version>
                    </dependency>
                    <dependency>
                      <groupId>org.webjars</groupId>
                      <artifactId>jquery-ui</artifactId>
                      <version>${webjars-jquery-ui.version}</version>
                    </dependency>
                    <dependency>
                      <groupId>org.webjars</groupId>
                      <artifactId>bootstrap</artifactId>
                      <version>${webjars-bootstrap.version}</version>
                    </dependency>
                    <!-- end of webjars -->
                                
                    <dependency>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-devtools</artifactId>
                      <optional>true</optional>
                    </dependency>
                  </dependencies>
                                
                  <build>
                    <plugins>
                      <plugin>
                        <groupId>io.spring.javaformat</groupId>
                        <artifactId>spring-javaformat-maven-plugin</artifactId>
                        <version>${spring-format.version}</version>
                        <executions>
                          <execution>
                            <phase>validate</phase>
                            <goals>
                              <goal>validate</goal>
                            </goals>
                          </execution>
                        </executions>
                      </plugin>
                      <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-checkstyle-plugin</artifactId>
                        <version>3.1.1</version>
                        <dependencies>
                          <dependency>
                            <groupId>com.puppycrawl.tools</groupId>
                            <artifactId>checkstyle</artifactId>
                            <version>8.32</version>
                          </dependency>
                          <dependency>
                            <groupId>io.spring.nohttp</groupId>
                            <artifactId>nohttp-checkstyle</artifactId>
                            <version>${nohttp-checkstyle.version}</version>
                          </dependency>
                        </dependencies>
                        <executions>
                          <execution>
                            <id>nohttp-checkstyle-validation</id>
                            <phase>validate</phase>
                            <configuration>
                              <configLocation>src/checkstyle/nohttp-checkstyle.xml</configLocation>
                              <suppressionsLocation>src/checkstyle/nohttp-checkstyle-suppressions.xml</suppressionsLocation>
                              <encoding>UTF-8</encoding>
                              <sourceDirectories>${basedir}</sourceDirectories>
                              <includes>**/*</includes>
                              <excludes>**/.git/**/*,**/.idea/**/*,**/target/**/,**/.flattened-pom.xml,**/*.class</excludes>
                            </configuration>
                            <goals>
                              <goal>check</goal>
                            </goals>
                          </execution>
                        </executions>
                      </plugin>
                      <plugin>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-maven-plugin</artifactId>
                        <executions>
                          <execution>
                            <!-- Spring Boot Actuator displays build-related information
                              if a META-INF/build-info.properties file is present -->
                            <goals>
                              <goal>build-info</goal>
                            </goals>
                            <configuration>
                              <additionalProperties>
                                <encoding.source>${project.build.sourceEncoding}</encoding.source>
                                <encoding.reporting>${project.reporting.outputEncoding}</encoding.reporting>
                                <java.source>${maven.compiler.source}</java.source>
                                <java.target>${maven.compiler.target}</java.target>
                              </additionalProperties>
                            </configuration>
                          </execution>
                        </executions>
                      </plugin>
                      <plugin>
                        <groupId>org.jacoco</groupId>
                        <artifactId>jacoco-maven-plugin</artifactId>
                        <version>${jacoco.version}</version>
                        <executions>
                          <execution>
                            <goals>
                              <goal>prepare-agent</goal>
                            </goals>
                          </execution>
                          <execution>
                            <id>report</id>
                            <phase>prepare-package</phase>
                            <goals>
                              <goal>report</goal>
                            </goals>
                          </execution>
                        </executions>
                      </plugin>
                                
                      <!-- Spring Boot Actuator displays build-related information if a git.properties
                        file is present at the classpath -->
                      <plugin>
                        <groupId>pl.project13.maven</groupId>
                        <artifactId>git-commit-id-plugin</artifactId>
                        <executions>
                          <execution>
                            <goals>
                              <goal>revision</goal>
                            </goals>
                          </execution>
                        </executions>
                        <configuration>
                          <verbose>true</verbose>
                          <dateFormat>yyyy-MM-dd'T'HH:mm:ssZ</dateFormat>
                          <generateGitPropertiesFile>true</generateGitPropertiesFile>
                          <generateGitPropertiesFilename>${project.build.outputDirectory}/git.properties
                          </generateGitPropertiesFilename>
                          <failOnNoGitDirectory>false</failOnNoGitDirectory>
                        </configuration>
                      </plugin>
                                
                      <plugin>
                        <groupId>ro.isdc.wro4j</groupId>
                        <artifactId>wro4j-maven-plugin</artifactId>
                        <version>${wro4j.version}</version>
                        <executions>
                          <execution>
                            <phase>generate-resources</phase>
                            <goals>
                              <goal>run</goal>
                            </goals>
                          </execution>
                        </executions>
                        <configuration>
                          <wroManagerFactory>ro.isdc.wro.maven.plugin.manager.factory.ConfigurableWroManagerFactory</wroManagerFactory>
                          <cssDestinationFolder>${project.build.directory}/classes/static/resources/css</cssDestinationFolder>
                          <wroFile>${basedir}/src/main/wro/wro.xml</wroFile>
                          <extraConfigFile>${basedir}/src/main/wro/wro.properties</extraConfigFile>
                          <contextFolder>${basedir}/src/main/less</contextFolder>
                        </configuration>
                        <dependencies>
                          <dependency>
                            <groupId>org.webjars</groupId>
                            <artifactId>bootstrap</artifactId>
                            <version>${webjars-bootstrap.version}</version>
                          </dependency>
                          <dependency>
                            <groupId>org.mockito</groupId>
                            <artifactId>mockito-core</artifactId>
                            <version>${mockito.version}</version>
                          </dependency>
                        </dependencies>
                      </plugin>
                    </plugins>
                  </build>
                                
                  <licenses>
                    <license>
                      <name>Apache License, Version 2.0</name>
                      <url>https://www.apache.org/licenses/LICENSE-2.0</url>
                    </license>
                  </licenses>
                                
                  <repositories>
                    <repository>
                      <id>spring-snapshots</id>
                      <name>Spring Snapshots</name>
                      <url>https://repo.spring.io/snapshot</url>
                      <snapshots>
                        <enabled>true</enabled>
                      </snapshots>
                    </repository>
                    <repository>
                      <id>spring-milestones</id>
                      <name>Spring Milestones</name>
                      <url>https://repo.spring.io/milestone</url>
                      <snapshots>
                        <enabled>false</enabled>
                      </snapshots>
                    </repository>
                  </repositories>
                                
                  <pluginRepositories>
                    <pluginRepository>
                      <id>spring-snapshots</id>
                      <name>Spring Snapshots</name>
                      <url>https://repo.spring.io/snapshot</url>
                      <snapshots>
                        <enabled>true</enabled>
                      </snapshots>
                    </pluginRepository>
                    <pluginRepository>
                      <id>spring-milestones</id>
                      <name>Spring Milestones</name>
                      <url>https://repo.spring.io/milestone</url>
                      <snapshots>
                        <enabled>false</enabled>
                      </snapshots>
                    </pluginRepository>
                  </pluginRepositories>
                                
                  <profiles>
                    <profile>
                      <id>m2e</id>
                      <activation>
                        <property>
                          <name>m2e.version</name>
                        </property>
                      </activation>
                      <build>
                        <pluginManagement>
                          <plugins>
                            <!-- This plugin's configuration is used to store Eclipse m2e settings
                   only. It has no influence on the Maven build itself. -->
                            <plugin>
                              <groupId>org.eclipse.m2e</groupId>
                              <artifactId>lifecycle-mapping</artifactId>
                              <version>1.0.0</version>
                              <configuration>
                                <lifecycleMappingMetadata>
                                  <pluginExecutions>
                                    <pluginExecution>
                                      <pluginExecutionFilter>
                                        <groupId>org.apache.maven.plugins</groupId>
                                        <artifactId>maven-checkstyle-plugin</artifactId>
                                        <versionRange>[1,)</versionRange>
                                        <goals>
                                          <goal>check</goal>
                                        </goals>
                                      </pluginExecutionFilter>
                                      <action>
                                        <ignore/>
                                      </action>
                                    </pluginExecution>
                                    <pluginExecution>
                                      <pluginExecutionFilter>
                                        <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-maven-plugin</artifactId>
                                        <versionRange>[1,)</versionRange>
                                        <goals>
                                          <goal>build-info</goal>
                                        </goals>
                                      </pluginExecutionFilter>
                                      <action>
                                        <ignore/>
                                      </action>
                                    </pluginExecution>
                                  </pluginExecutions>
                                </lifecycleMappingMetadata>
                              </configuration>
                            </plugin>
                          </plugins>
                        </pluginManagement>
                      </build>
                    </profile>
                  </profiles>
                                
                </project>
                """;
        Stream<SourceFile> parse = MavenParser.builder().build().parse(pom);
        assertThat(parse).isNotNull();

    }


    @Test
    @DisplayName("another test with the failing pom")
    void anotherTestWithTheFailingPom() throws IOException {
        @Language("xml")
        String pomXml = """
                <project>
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.h2database</groupId>
                    <artifactId>h2</artifactId>
                    <version>1.4.200</version>
                    <packaging>jar</packaging>
                    <name>H2 Database Engine</name>
                    <url>https://h2database.com</url>
                    <description>H2 Database Engine</description>
                    <licenses>
                        <license>
                            <name>MPL 2.0 or EPL 1.0</name>
                            <url>https://h2database.com/html/license.html</url>
                            <distribution>repo</distribution>
                        </license>
                    </licenses>
                    <scm>
                        <connection>scm:git:https://github.com/h2database/h2database</connection>
                        <url>https://github.com/h2database/h2database</url>
                    </scm>
                    <developers>
                        <developer>
                            <id>thomas.tom.mueller</id>
                            <name>Thomas Mueller</name>
                            <email>thomas.tom.mueller at gmail dot com</email>
                        </developer>
                    </developers>
                    <dependencies>
                    </dependencies>
                </project>
                """;

        RawPom pom = MavenXmlMapper.readMapper().readValue(new ByteArrayInputStream(pomXml.getBytes()), RawPom.class);
        assertThat(pom).isNotNull();

        Stream<SourceFile> parse = MavenParser.builder().build().parse(pomXml);
        assertThat(parse).isNotNull();
    }

}
