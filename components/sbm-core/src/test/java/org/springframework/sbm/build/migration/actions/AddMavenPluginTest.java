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
package org.springframework.sbm.build.migration.actions;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.sbm.build.api.ApplicationModules;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin;
import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin.OpenRewriteMavenPluginExecution;
import org.springframework.sbm.engine.context.ProjectContext;

import static org.mockito.Mockito.*;

class AddMavenPluginTest {

    @Test
    void testApply() {
        AddMavenPlugin addMavenPlugin = new AddMavenPlugin();
        OpenRewriteMavenPlugin plugin = new OpenRewriteMavenPlugin();
        addMavenPlugin.setPlugin(plugin);

        BuildFile buildFile = mock(BuildFile.class);
        ProjectContext projectContext = mock(ProjectContext.class);

        ApplicationModules modules = mock(ApplicationModules.class);
        org.springframework.sbm.build.api.Module rootModule = mock(org.springframework.sbm.build.api.Module.class);
        when(projectContext.getApplicationModules()).thenReturn(modules);
        when(modules.getRootModule()).thenReturn(rootModule);
        when(rootModule.getBuildFile()).thenReturn(buildFile);

        addMavenPlugin.apply(projectContext);

        verify(buildFile).addPlugin(plugin);
    }

    @Test
    void addMavenPluginMinimalFields() {
        AddMavenPlugin sut = new AddMavenPlugin();
        OpenRewriteMavenPlugin plugin = OpenRewriteMavenPlugin.builder()
                .groupId("org.joinfaces")
                .artifactId("joinfaces-maven-plugin")
                .build();
        sut.setPlugin(plugin);

        String pomSource =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>some.group.id</groupId>\n" +
                        "    <artifactId>with-artifact</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>100.23.01-SNAPSHOT</version>\n" +
                        "</project>";

        String expected =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>some.group.id</groupId>\n" +
                        "    <artifactId>with-artifact</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>100.23.01-SNAPSHOT</version>\n" +
                        "    <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.joinfaces</groupId>\n" +
                        "                <artifactId>joinfaces-maven-plugin</artifactId>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>";

        OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomSource, expected, sut);
    }

    @Test
    void addMavenPluginAllFields() {
        AddMavenPlugin sut = new AddMavenPlugin();
        OpenRewriteMavenPlugin plugin = OpenRewriteMavenPlugin.builder()
                .groupId("org.joinfaces")
                .artifactId("joinfaces-maven-plugin")
                .version("2.4.2")
                .execution(
                        OpenRewriteMavenPluginExecution.builder()
                                .id("some-id")
                                .goal("classpath-scan")
                                .goal("another-goal")
                                .build()
                )
                .execution(
                        OpenRewriteMavenPluginExecution.builder()
                                .goal("and-another")
                                .build()
                )
                .build();
        sut.setPlugin(plugin);

        String pomSource =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>some.group.id</groupId>\n" +
                        "    <artifactId>with-artifact</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>100.23.01-SNAPSHOT</version>\n" +
                        "</project>";

        String expected =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>some.group.id</groupId>\n" +
                        "    <artifactId>with-artifact</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>100.23.01-SNAPSHOT</version>\n" +
                        "    <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.joinfaces</groupId>\n" +
                        "                <artifactId>joinfaces-maven-plugin</artifactId>\n" +
                        "                <version>2.4.2</version>\n" +
                        "                <executions>\n" +
                        "                    <execution>\n" +
                        "                        <id>some-id</id>\n" +
                        "                        <goals>\n" +
                        "                            <goal>classpath-scan</goal>\n" +
                        "                            <goal>another-goal</goal>\n" +
                        "                        </goals>\n" +
                        "                    </execution>\n" +
                        "                    <execution>\n" +
                        "                        <goals>\n" +
                        "                            <goal>and-another</goal>\n" +
                        "                        </goals>\n" +
                        "                    </execution>\n" +
                        "                </executions>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>";

        OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomSource, expected, sut);
    }


    @Test
    void addMavenPlugin() {

        String pomSource =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>some.group.id</groupId>\n" +
                        "    <artifactId>with-artifact</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>100.23.01-SNAPSHOT</version>\n" +
                        "    <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.apache.maven.plugins</groupId>\n" +
                        "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                        "                <version>3.5.1</version>\n" +
                        "                <configuration>\n" +
                        "                    <source>1.7</source>\n" +
                        "                    <target>1.7</target>\n" +
                        "                    <compilerArguments>\n" +
                        "                        <endorseddirs>${endorsed.dir}</endorseddirs>\n" +
                        "                    </compilerArguments>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.apache.maven.plugins</groupId>\n" +
                        "                <artifactId>maven-war-plugin</artifactId>\n" +
                        "                <version>2.6</version>\n" +
                        "                <configuration>\n" +
                        "                    <failOnMissingWebXml>false</failOnMissingWebXml>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.apache.maven.plugins</groupId>\n" +
                        "                <artifactId>maven-dependency-plugin</artifactId>\n" +
                        "                <version>2.10</version>\n" +
                        "                <executions>\n" +
                        "                    <execution>\n" +
                        "                        <phase>validate</phase>\n" +
                        "                        <goals>\n" +
                        "                            <goal>copy</goal>\n" +
                        "                        </goals>\n" +
                        "                        <configuration>\n" +
                        "                            <outputDirectory>${endorsed.dir}</outputDirectory>\n" +
                        "                            <silent>true</silent>\n" +
                        "                            <artifactItems>\n" +
                        "                                <artifactItem>\n" +
                        "                                    <groupId>javax</groupId>\n" +
                        "                                    <artifactId>javaee-endorsed-api</artifactId>\n" +
                        "                                    <version>7.0</version>\n" +
                        "                                    <type>jar</type>\n" +
                        "                                </artifactItem>\n" +
                        "                            </artifactItems>\n" +
                        "                        </configuration>\n" +
                        "                    </execution>\n" +
                        "                </executions>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.springframework.boot</groupId>\n" +
                        "                <artifactId>spring-boot-maven-plugin</artifactId>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>";

        String expected =
                         "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>some.group.id</groupId>\n" +
                        "    <artifactId>with-artifact</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>100.23.01-SNAPSHOT</version>\n" +
                        "    <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.apache.maven.plugins</groupId>\n" +
                        "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                        "                <version>3.5.1</version>\n" +
                        "                <configuration>\n" +
                        "                    <source>1.7</source>\n" +
                        "                    <target>1.7</target>\n" +
                        "                    <compilerArguments>\n" +
                        "                        <endorseddirs>${endorsed.dir}</endorseddirs>\n" +
                        "                    </compilerArguments>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.apache.maven.plugins</groupId>\n" +
                        "                <artifactId>maven-war-plugin</artifactId>\n" +
                        "                <version>2.6</version>\n" +
                        "                <configuration>\n" +
                        "                    <failOnMissingWebXml>false</failOnMissingWebXml>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.apache.maven.plugins</groupId>\n" +
                        "                <artifactId>maven-dependency-plugin</artifactId>\n" +
                        "                <version>2.10</version>\n" +
                        "                <executions>\n" +
                        "                    <execution>\n" +
                        "                        <phase>validate</phase>\n" +
                        "                        <goals>\n" +
                        "                            <goal>copy</goal>\n" +
                        "                        </goals>\n" +
                        "                        <configuration>\n" +
                        "                            <outputDirectory>${endorsed.dir}</outputDirectory>\n" +
                        "                            <silent>true</silent>\n" +
                        "                            <artifactItems>\n" +
                        "                                <artifactItem>\n" +
                        "                                    <groupId>javax</groupId>\n" +
                        "                                    <artifactId>javaee-endorsed-api</artifactId>\n" +
                        "                                    <version>7.0</version>\n" +
                        "                                    <type>jar</type>\n" +
                        "                                </artifactItem>\n" +
                        "                            </artifactItems>\n" +
                        "                        </configuration>\n" +
                        "                    </execution>\n" +
                        "                </executions>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.springframework.boot</groupId>\n" +
                        "                <artifactId>spring-boot-maven-plugin</artifactId>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.joinfaces</groupId>\n" +
                        "                <artifactId>joinfaces-maven-plugin</artifactId>\n" +
                        "                <executions>\n" +
                        "                    <execution>\n" +
                        "                        <goals>\n" +
                        "                            <goal>classpath-scan</goal>\n" +
                        "                        </goals>\n" +
                        "                    </execution>\n" +
                        "                </executions>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>";

        AddMavenPlugin addMavenPlugin = new AddMavenPlugin();
        OpenRewriteMavenPlugin plugin = OpenRewriteMavenPlugin.builder()
                        .
                groupId("org.joinfaces")
                .artifactId("joinfaces-maven-plugin")
                .execution(
                        OpenRewriteMavenPluginExecution.builder()
                                .goals(Sets.newSet("classpath-scan"))
                                .build()
                )
                .build();
        addMavenPlugin.setPlugin(plugin);
        OpenRewriteMavenBuildFileTestSupport.verifyRefactoring(pomSource, expected, addMavenPlugin);
    }

}