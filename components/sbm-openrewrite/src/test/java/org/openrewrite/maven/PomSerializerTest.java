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
package org.openrewrite.maven;

import org.junit.jupiter.api.Test;
import org.openrewrite.maven.internal.RawPom;
import org.openrewrite.maven.tree.Pom;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Krüger
 */
class PomSerializerTest {

    private static final String POM = """
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                  <modelVersion>4.0.0</modelVersion>
                  <parent>
                    <groupId>org.apache.activemq</groupId>
                    <artifactId>activemq-parent</artifactId>
                    <version>5.16.0</version>
                  </parent>
                  <artifactId>activemq-jdbc-store</artifactId>
                  <packaging>jar</packaging>
                  <name>ActiveMQ :: JDBC Store</name>
                  <description>The ActiveMQ JDBC Store Implementation</description>
                  <properties>
                    <surefire.argLine>-Xmx512M</surefire.argLine>
                    <foo>bar</foo>
                  </properties>
                  <repositories>
                      <repository>
                          <id>jitpack.io</id>
                          <url>https://jitpack.io</url>
                      </repository>
                      <repository>
                          <id>jcenter</id>
                          <name>jcenter</name>
                          <url>https://jcenter.bintray.com</url>
                          <username>aUser</username>
                          <password>Z-je34</password>
                      </repository>
                      <repository>
                          <id>mavencentral</id>
                          <name>mavencentral</name>
                          <url>https://repo.maven.apache.org/maven2</url>
                      </repository>
                      <repository>
                          <id>repository.spring.milestone</id>
                          <name>Spring Milestone Repository</name>
                          <url>https://repo.spring.io/milestone</url>
                          <releases>
                            <enabled>false</enabled>
                          </releases>
                      </repository>
                  </repositories>
                  <dependencies>
                    <dependency>
                      <groupId>${project.groupId}</groupId>
                      <artifactId>activeio-core</artifactId>
                      <optional>true</optional>
                    </dependency>
                    <dependency>
                      <groupId>org.apache.activemq</groupId>
                      <artifactId>activemq-broker</artifactId>
                      <type>test-jar</type>
                      <scope>test</scope>
                    </dependency>
                  </dependencies>
                  <reporting>
                    <plugins>
                      <plugin>
                        <groupId>org.codehaus.mojo</groupId>
                        <artifactId>findbugs-maven-plugin</artifactId>
                        <version>${findbugs-maven-plugin-version}</version>
                        <configuration>
                          <threshold>Normal</threshold>
                          <effort>Default</effort>
                        </configuration>
                      </plugin>
                    </plugins>
                  </reporting>
                  <build>
                    <plugins>
                      <plugin>
                        <artifactId>maven-surefire-plugin</artifactId>
                        <configuration>
                            <forkCount>1</forkCount>
                            <reuseForks>false</reuseForks>
                          <argLine>${surefire.argLine}</argLine>
                          <runOrder>alphabetical</runOrder>
                          <systemProperties>
                            <property>
                              <name>org.apache.activemq.default.directory.prefix</name>
                              <value>target/</value>
                            </property>
                            <!-- Uncomment the following if you want to configure custom logging (using src/test/resources/log4j.properties)
                                 while running mvn:test
                                 Note: if you want to see log messages on the console window remove
                                       "redirectTestOutputToFile" from the parent pom
                            -->
                            <!--
                            <property>
                              <name>log4j.configuration</name>
                              <value>file:target/test-classes/log4j.properties</value>
                            </property>
                            -->
                          </systemProperties>
                          <includes>
                            <include>**/*Test.*</include>
                          </includes>
                        </configuration>
                      </plugin>
                    </plugins>
                  </build>
                </project>
            """;

    @Test
    void serialize() {
        RawPom rawPom = RawPom.parse(new ByteArrayInputStream(POM.getBytes()), null);
        Pom pom = rawPom.toPom(Path.of("path/to/pom"), null);

        assertThat(pom.getSourcePath()).isEqualTo(Path.of("path/to/pom"));

        String p = PomSerializer.serialize(pom);

        assertThat(p)
                .isEqualTo(
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                        <modelVersion>4.0.0</modelVersion>
                        <parent>
                            <groupId>org.apache.activemq</groupId>
                            <artifactId>activemq-parent</artifactId>
                            <version>5.16.0</version>
                        </parent>
                        <groupId>org.apache.activemq</groupId>
                        <artifactId>activemq-jdbc-store</artifactId>
                        <version>5.16.0</version>
                        <packaging>jar</packaging>
                        <name>ActiveMQ :: JDBC Store</name>
                        <properties>
                            <surefire.argLine>-Xmx512M</surefire.argLine>
                            <foo>bar</foo>
                        </properties>
                        <repositories>
                            <repository>
                                <id>jitpack.io</id>
                                <uri>https://jitpack.io</uri>
                            </repository>
                            <repository>
                                <id>jcenter</id>
                                <uri>https://jcenter.bintray.com</uri>
                            </repository>
                            <repository>
                                <id>mavencentral</id>
                                <uri>https://repo.maven.apache.org/maven2</uri>
                            </repository>
                            <repository>
                                <id>repository.spring.milestone</id>
                                <uri>https://repo.spring.io/milestone</uri>
                            </repository>
                        </repositories>                        
                        <dependencies>
                            <dependency>
                                <groupId>${project.groupId}</groupId>
                                <artifactId>activeio-core</artifactId>
                                <optional>true</optional>
                            </dependency>
                            <dependency>
                                <groupId>org.apache.activemq</groupId>
                                <artifactId>activemq-broker</artifactId>
                                <type>test-jar</type>
                                <scope>test</scope>
                            </dependency>
                        </dependencies>
                        <build>
                            <plugins>
                                <plugin>
                                    <artifactId>maven-surefire-plugin</artifactId>
                                    <configuration>
                                        <forkCount>1</forkCount>
                                        <reuseForks>false</reuseForks>
                                        <argLine>${surefire.argLine}</argLine>
                                        <runOrder>alphabetical</runOrder>
                                        <systemProperties>
                                            <property>
                                                <name>org.apache.activemq.default.directory.prefix</name>
                                                <value>target/</value>
                                            </property>
                                        </systemProperties>
                                        <includes>
                                            <include>**/*Test.*</include>
                                        </includes>
                                    </configuration>
                                </plugin>
                            </plugins>
                        </build>
                    </project>
                    """
                );
/*
        eq(pom.getParent().getGroupId(), "org.apache.activemq");
        eq(pom.getParent().getVersion(), "5.16.0");
        eq(pom.getParent().getArtifactId(),  "activemq-parent");

        eq(pom.getGroupId(), "org.apache.activemq");
        eq(pom.getArtifactId(), "activemq-jdbc-store");
        eq(pom.getVersion(), "5.16.0");
        eq(pom.getPackaging(), "pom");
        eq(pom.getName(), "ActiveMQ :: JDBC Store");
        assertThat(pom.getProperties()).containsAllEntriesOf(Map.of(
                "surefire.argLine", "-Xmx512M",
                "foo", "bar"
        ));

 */
    }

    private void eq(String given, String expected) {
        assertThat(given).isEqualTo(expected);
    }
}