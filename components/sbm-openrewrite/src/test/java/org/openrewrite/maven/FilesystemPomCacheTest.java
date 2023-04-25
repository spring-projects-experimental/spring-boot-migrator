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

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.maven.internal.RawPom;
import org.openrewrite.maven.tree.MavenRepository;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.ResolvedGroupArtifactVersion;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilesystemPomCacheTest {

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
                      </properties>
                      <dependencies>
                        <dependency>
                          <groupId>org.apache.activemq</groupId>
                          <artifactId>activemq-broker</artifactId>
                        </dependency>
                        <dependency>
                          <groupId>org.apache.derby</groupId>
                          <artifactId>derby</artifactId>
                          <optional>true</optional>
                        </dependency>
                        <dependency>
                          <groupId>${project.groupId}</groupId>
                          <artifactId>activeio-core</artifactId>
                          <optional>true</optional>
                        </dependency>
                        <dependency>
                          <groupId>junit</groupId>
                          <artifactId>junit</artifactId>
                          <scope>test</scope>
                        </dependency>
                        <dependency>
                          <groupId>org.mockito</groupId>
                          <artifactId>mockito-core</artifactId>
                          <scope>test</scope>
                        </dependency>
                        <dependency>
                          <groupId>org.apache.activemq</groupId>
                          <artifactId>activemq-broker</artifactId>
                          <type>test-jar</type>
                          <scope>test</scope>
                        </dependency>
                        <dependency>
                          <groupId>org.slf4j</groupId>
                          <artifactId>slf4j-log4j12</artifactId>
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
    private ResolvedGroupArtifactVersion resolvedGav;
    private MavenRepository repo = new MavenRepository("Maven", "https://repo.maven.apache.org/maven2/", null, null, false, null, null, false);
    private RawPom rawPom = RawPom.parse(new ByteArrayInputStream(POM.getBytes()), null);
    private FilesystemPomCache sut;
    private Pom pomModel;
    @TempDir
    static Path cacheDir;

    @BeforeEach
    void beforeAll() {
        resolvedGav = new ResolvedGroupArtifactVersion(repo.getUri(), "org.apache.activemq", "activemq-jdbc-store", "5.16.0", null);
        pomModel = rawPom.toPom(cacheDir, repo).withGav(resolvedGav);
        sut = new FilesystemPomCache(cacheDir);
    }

    @Test
    @Order(1)
    void storeNonExistentPom() {
        assertThat(cacheDir.toFile().listFiles()).isEmpty();

        sut.put(resolvedGav, Optional.of(pomModel));

        assertThat(cacheDir.toFile().listFiles()).hasSize(1);
    }

    @Test
    @Order(2)
    void retrievePreviouslyPutPom() {
        assertThat(cacheDir.toFile().listFiles()).hasSize(1);

        Optional<Pom> returnedPom = sut.getIfPresent(resolvedGav);

        assertThat(returnedPom).isPresent();
        Pom pom = returnedPom.get();
        assertThat(pom.getSourcePath()).isEqualTo(cacheDir.resolve("org/apache/activemq/activemq-jdbc-store/5.16.0/activemq-jdbc-store-5.16.0.pom"));

        eq(pom.getParent().getGroupId(), "org.apache.activemq");
        eq(pom.getParent().getVersion(), "5.16.0");
        eq(pom.getArtifactId(),  "activemq-parent");

        eq(pom.getGroupId(), "org.apache.activemq");
        eq(pom.getArtifactId(), "activemq-jdbc-store");
    }

    private void eq(String given, String expected) {
        assertThat(given).isEqualTo(expected);
    }

}