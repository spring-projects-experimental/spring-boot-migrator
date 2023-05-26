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
package org.springframework.sbm.build.impl;

import org.junit.jupiter.api.*;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.tree.MavenRepository;
import org.springframework.core.io.Resource;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.TestDummyResource;
import org.springframework.sbm.project.parser.MavenPasswordDecrypter;
import org.springframework.sbm.project.parser.RewriteMavenSettingsInitializer;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class RewriteMavenSettingsInitializerTest {

    // localRepository.getUri() will differ from '"file:" + userHome + "/.m2/repository/"' because
    // MavenRepository.MAVEN_LOCAL_DEFAULT gets returned and this field is statically initialized.
    // For this test it means that running the test in isolation succeeds but running it in combination
    // with a test that loads MavenRepository before 'user.home' was changed in this test, it fails.
    // And maybe even worse, running this test before others would set the local maven repository to the
    // dummy dir used in this test.
    // To prevent this it will be initialized (if it wasn't already) with the original settings with this line:
    MavenRepository mavenLocalDefault = MavenRepository.MAVEN_LOCAL_DEFAULT;
    private String actualUserHome;
    private Path fakedUserHome;

    @BeforeEach
    void beforeEach() {
        // Faking the local maven dir to provide the settings.xml for this test
        fakedUserHome = Path.of("./testcode/project-with-maven-settings/user-home").toAbsolutePath().normalize();
        actualUserHome = System.getProperty("user.home");
        System.setProperty("user.home", fakedUserHome.toString());
    }

    @Test
    void mavenParserMustAdhereToSettingsXmlTest() throws URISyntaxException {


        RewriteExecutionContext executionContext = new RewriteExecutionContext();
        RewriteMavenSettingsInitializer sut = new RewriteMavenSettingsInitializer(new MavenPasswordDecrypter());
        Resource mavenSettingsFile = new TestDummyResource(Path.of("./testcode/project-with-maven-settings/user-home/settings.xml").toAbsolutePath().normalize(), """
                                                                     <?xml version="1.0" encoding="UTF-8" ?>
                                                                     <settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd'
                                                                               xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
                                                                         <localRepository>${java.io.tmpdir}</localRepository>
                                                                         <profiles>
                                                                             <profile>
                                                                                 <activation>
                                                                                     <activeByDefault>true</activeByDefault>
                                                                                 </activation>
                                                                                 <repositories>
                                                                                     <repository>
                                                                                         <snapshots>
                                                                                             <enabled>false</enabled>
                                                                                         </snapshots>
                                                                                         <id>central</id>
                                                                                         <name>bintray</name>
                                                                                         <url>https://jcenter.bintray.com</url>
                                                                                     </repository>
                                                                                 </repositories>
                                                                                 <pluginRepositories>
                                                                                     <pluginRepository>
                                                                                         <snapshots>
                                                                                             <enabled>false</enabled>
                                                                                         </snapshots>
                                                                                         <id>central</id>
                                                                                         <name>bintray-plugins</name>
                                                                                         <url>https://jcenter.bintray.com</url>
                                                                                     </pluginRepository>
                                                                                 </pluginRepositories>
                                                                                 <id>bintray</id>
                                                                             </profile>
                                                                         </profiles>
                                                                         <activeProfiles>
                                                                             <activeProfile>bintray</activeProfile>
                                                                         </activeProfiles>
                                                                         <proxies>
                                                                             <proxy>
                                                                                 <id>my-proxy</id>
                                                                                 <active>true</active>
                                                                                 <protocol>http</protocol>
                                                                                 <host>my-corporate-proxy-host</host>
                                                                                 <port>80</port>
                                                                             </proxy>
                                                                         </proxies>
                                                                     </settings>
                                                                     """);
        Path securitySettingsFile = null;
        sut.initializeMavenSettings(executionContext, mavenSettingsFile, securitySettingsFile);
        MavenExecutionContextView mavenExecutionContextView = MavenExecutionContextView.view(executionContext);

        assertThat(mavenExecutionContextView.getRepositories()).hasSize(1);

        MavenRepository mavenRepository = mavenExecutionContextView.getRepositories().get(0);

        assertThat(mavenRepository.getId()).isEqualTo("central");
        assertThat(mavenRepository.getUri()).isEqualTo("https://jcenter.bintray.com");
        assertThat(mavenRepository.getReleases()).isNull();
        assertThat(mavenRepository.getSnapshots()).isEqualToIgnoringCase("false");

        MavenRepository localRepository = mavenExecutionContextView.getLocalRepository();
        assertThat(localRepository.getSnapshots()).isNull();

        String tmpDir = removeTrailingSlash(System.getProperty("java.io.tmpdir"));
        String customLocalRepository = new URI("file://" + tmpDir).toString();
        assertThat(removeTrailingSlash(localRepository.getUri())).isEqualTo(customLocalRepository);
        assertThat(localRepository.getSnapshots()).isNull();
        assertThat(localRepository.isKnownToExist()).isTrue();
        assertThat(localRepository.getUsername()).isNull();
        assertThat(localRepository.getPassword()).isNull();
    }

    String removeTrailingSlash(String string) {
        if(string.endsWith("/")){
            return string.substring(0, string.length()-1);
        }
        return string;
    }

    @AfterEach
    public void reset() {
        // reset
        System.setProperty("user.home", actualUserHome);
    }
}