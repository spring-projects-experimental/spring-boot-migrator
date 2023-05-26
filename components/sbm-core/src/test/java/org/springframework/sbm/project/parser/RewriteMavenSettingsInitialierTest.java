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
package org.springframework.sbm.project.parser;

import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.*;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.crypto.*;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.maven.tree.MavenRepository;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;
import org.sonatype.plexus.components.cipher.PlexusCipherException;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.springframework.core.io.Resource;
import org.springframework.sbm.project.TestDummyResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test feature parity between OpenRewrite Maven plugin settings initialization and |@link {@link RewriteMavenSettingsInitializer}.
 *
 * - {@link #initializeMavenSettings_withOpenRewriteMavenPlugin()} uses OpenRewrite Maven plugin to create {@link MavenSettings}.
 * - {@link #initializeMavenSettings_withRewriteMavenSettingsInitializer()} uses SBM {@link RewriteMavenSettingsInitializer} to initialize {@link MavenSettings}.
 *
 * The tests verify {@link #verifyMavenSettings(MavenSettings)} that both approaches yield the same results given same input.
 *
 * @author Fabian Krüger
 */
class RewriteMavenSettingsInitialierTest {

    private final String profile1_id = "profile1";
    private static final Path mavenHome = Path.of("./target/.m2").toAbsolutePath().normalize();
    private static final Path mavenRepository = mavenHome.resolve("repository-changed");
    private static final String mirror1_id = "mirror1";
    private static final String mirror1_url = "mirror1_url";
    private static final String mirror1_mirrorOf = "mirror1_mirrorOf";
    private static final String server1_id = "server1_id";
    private static final String server1_username = "server1_username";
    private static final String server1_password = "{gY56I8DPBrwHu3dWkh+EGDqp+ppuTnBaWe9fNpdTPIw=}";
    private static final String server1_privateKey = "server1_privateKey";
    private static final String server1_passphrase = "server1_passphrase";
    private static final String server1_filePermissions = "server1_filePermissions";
    private static final String server1_directoryPermissions = "server1_directoryPermissions";
    private MavenSession mavenSession = mock(MavenSession.class);
    private SettingsDecrypter settingsDecrypter;
    private static final String decryptedPassword = "xxx";

    private static final String MAVEN_SECURITY_FILE = """
            <settingsSecurity>
              <!-- Password: XXX -->
              <master>{H39p2LFBhRQHqKIsRiu0R/zhfke3/B4k0Wt+BZlC8mc=}</master>
            </settingsSecurity>
            """;

    @Language("xml")
    private static final String MAVEN_SETTINGS_FILE = """
            <?xml version="1.0" encoding="UTF-8"?>
            <settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd'
                      xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
                <localRepository>%s</localRepository>
                <interactiveMode/>
                <offline/>
                <pluginGroups/>
                 <servers>
                        <server>
                            <id>%s</id>
                            <username>%s</username>
                            <password>%s</password>
                            <privateKey>%s</privateKey>
                            <passphrase>%s</passphrase>
                            <filePermissions>%s</filePermissions>
                            <directoryPermissions>%s</directoryPermissions>
                            <configuration></configuration>
                        </server>
                    </servers>
                <mirrors>
                    <mirror>
                        <id>%s</id>
                        <url>%s</url>
                        <mirrorOf>%s</mirrorOf>
                    </mirror>
                </mirrors>
                <proxies/>
                 <profiles>
                        <profile>
                            <id>profile1</id>
                            <activation>
                                <activeByDefault>true</activeByDefault>
                                <jdk>17</jdk>
                            </activation>
                        </profile>
                    </profiles>
                    <activeProfiles>
                        <activeProfile>profile1</activeProfile>
                    </activeProfiles>
            </settings>
            """.formatted(
                    mavenRepository,
                    server1_id,
                    server1_username,
                    server1_password,
                    server1_privateKey,
                    server1_passphrase,
                    server1_filePermissions,
                    server1_directoryPermissions,
                    mirror1_id,
                    mirror1_url,
                    mirror1_mirrorOf);
    private Path mavenSecurityFilePath;
    private Path mavenSettingsFilePath;

    @BeforeEach
    void beforeEach(@TempDir Path tmpDir) {
        // Prepare internal classes to decrypt Server password
        try {
            // DefaultSecDispatcher reads file with security settings
            mavenSecurityFilePath = tmpDir.resolve("settings-security.xml");
            Files.writeString(mavenSecurityFilePath, MAVEN_SECURITY_FILE);
            mavenSettingsFilePath = tmpDir.resolve("settings.xml");
            Files.writeString(mavenSettingsFilePath, MAVEN_SETTINGS_FILE);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Use OpenRewrite Maven Plugin (@link MavenMojoProjectParser} to initialize settings.
     */
    @Test
    void initializeMavenSettings_withOpenRewriteMavenPlugin() throws PlexusCipherException {
        MavenMojoProjectParser comparingParser = createMavenMojoProjectParser();

        MavenExecutionRequest mavenExecutionRequest = mock(MavenExecutionRequest.class);
        when(mavenSession.getRequest()).thenReturn(mavenExecutionRequest);
        // Maven profiles
        Profile profile1 = mock(Profile.class);
        when(profile1.getId()).thenReturn(profile1_id);

        Activation profile1_activation = mock(Activation.class);
        when(profile1_activation.isActiveByDefault()).thenReturn(true);
        when(profile1_activation.getJdk()).thenReturn("profile1_jdk");
        when(profile1.getActivation()).thenReturn(profile1_activation);

        ActivationProperty profile1_property = mock(ActivationProperty.class);
        when(profile1_activation.getProperty()).thenReturn(profile1_property);
        when(profile1_property.getName()).thenReturn("profile1_property_name");
        when(profile1_property.getValue()).thenReturn("profile1_property_value");

        Repository profile1_repository1 = mock(Repository.class);
        List<Repository> profile1_repositories = List.of(profile1_repository1);
        when(profile1.getRepositories()).thenReturn(profile1_repositories);

        SettingsDecryptionResult decryptionResult = mock(SettingsDecryptionResult.class);
        Server decryptionResultServer = mock(Server.class);
        when(decryptionResult.getServer()).thenReturn(decryptionResultServer);
        when(decryptionResultServer.getPassword()).thenReturn(decryptedPassword);

        when(mavenExecutionRequest.getProfiles()).thenReturn(List.of(profile1)); // with profiles
        // Active profiles
        when(mavenExecutionRequest.getActiveProfiles()).thenReturn(List.of(profile1_id)); // with active profiles
        // Mirrors
        Mirror mirror1 = mock(Mirror.class);
        when(mirror1.getId()).thenReturn(mirror1_id);
        when(mirror1.getMirrorOf()).thenReturn(mirror1_mirrorOf);
        when(mirror1.getUrl()).thenReturn(mirror1_url);
        verify(mirror1, never()).getLayout();

        Mirror mirror2 = mock(Mirror.class);
        List<Mirror> mirrors = List.of(mirror1);

        when(mavenExecutionRequest.getMirrors()).thenReturn(mirrors); // with mirrors
        // Servers

        Server server1 = new Server();
        server1.setPassword(server1_password);
        server1.setId(server1_id);
        server1.setUsername(server1_username);
        server1.setFilePermissions(server1_filePermissions);
        server1.setDirectoryPermissions(server1_directoryPermissions);
        server1.setPassphrase(server1_passphrase);
        server1.setPrivateKey(server1_privateKey);
        List<Server> servers = List.of(server1);
        when(mavenExecutionRequest.getServers()).thenReturn(servers); // with Servers
        // repository path
        when(mavenExecutionRequest.getLocalRepositoryPath()).thenReturn(mavenRepository.toFile());

        MavenSettings mavenSettings = comparingParser.buildSettings();

        verifyMavenSettings(mavenSettings);
    }

    @Test
    void initializeMavenSettings_withRewriteMavenSettingsInitializer() {
        ExecutionContext executionContext = new InMemoryExecutionContext();
        Resource securitySettingsFile = new TestDummyResource(mavenSecurityFilePath, MAVEN_SECURITY_FILE);
        Resource mavenSettingsFile = new TestDummyResource(mavenSettingsFilePath, MAVEN_SETTINGS_FILE);

        RewriteMavenSettingsInitializer sut = new RewriteMavenSettingsInitializer(new MavenPasswordDecrypter());
        MavenSettings mavenSettings = sut.initializeMavenSettings(executionContext, mavenSettingsFile, mavenSecurityFilePath);

        verifyMavenSettings(mavenSettings);
    }

    private void verifyMavenSettings(MavenSettings mavenSettings) {
        assertThat(mavenSettings).isNotNull();

        MavenSettings.ActiveProfiles activeProfiles = mavenSettings.getActiveProfiles();
        assertThat(activeProfiles.getActiveProfiles().get(0)).isEqualTo(profile1_id);

        MavenRepository mavenLocal = mavenSettings.getMavenLocal();
        assertThat(mavenLocal.isKnownToExist()).isTrue();
        assertThat(mavenLocal.getUsername()).isNull();
        assertThat(mavenLocal.getPassword()).isNull();
        assertThat(mavenLocal.getSnapshots()).isNull();
        assertThat(mavenLocal.getReleases()).isNull();
        assertThat(mavenLocal.getUri()).isEqualTo("file://" + mavenRepository);
        assertThat(mavenLocal.getId()).isEqualTo("local");
        assertThat(mavenLocal.getDeriveMetadataIfMissing()).isNull();

        MavenSettings.Profiles profiles = mavenSettings.getProfiles();
        assertThat(profiles.getProfiles()).hasSize(1);

        assertThat(mavenSettings.getMirrors()).isNotNull();
        assertThat(mavenSettings.getMirrors().getMirrors()).hasSize(1);
        MavenSettings.Mirror mirror1 = mavenSettings.getMirrors().getMirrors().get(0);
        assertThat(mirror1.getId()).isEqualTo(mirror1_id);
        assertThat(mirror1.getUrl()).isEqualTo(mirror1_url);
        assertThat(mirror1.getMirrorOf()).isEqualTo(mirror1_mirrorOf);
        assertThat(mirror1.getReleases()).isNull();
        assertThat(mirror1.getSnapshots()).isNull();
        assertThat(countGetter(MavenSettings.Mirror.class)).isEqualTo(5);


        MavenSettings.Servers servers = mavenSettings.getServers();
        assertThat(servers.getServers()).hasSize(1);
        MavenSettings.Server server = servers.getServers().get(0);
        assertThat(server.getId()).isEqualTo(server1_id);
        assertThat(server.getUsername()).isEqualTo(server1_username);
        assertThat(server.getPassword()).isEqualTo(decryptedPassword);
        assertThat(countGetter(server.getClass())).isEqualTo(3);
    }

    private long countGetter(Class<?> clazz) {
        long allGetter = internalGetGetter(clazz);
        long objectGetter = internalGetGetter(Object.class);
        return allGetter - objectGetter;
    }

    private long internalGetGetter(Class<?> clazz) {
        return Arrays
                .stream(clazz.getMethods())
                .filter(m -> m.getName().startsWith("get"))
                .filter(m -> m.getParameterCount() == 0)
                .count();
    }

    private MavenMojoProjectParser createMavenMojoProjectParser() throws PlexusCipherException {
        DefaultSecDispatcher securityDispatcher = new DefaultSecDispatcher();
        // hack, the cipher is required but can't be set from outside
        ReflectionTestUtils.setField(securityDispatcher, "_cipher", new DefaultPlexusCipher());
        settingsDecrypter = new DefaultSettingsDecrypter(securityDispatcher);
        // The file location is retrieved from env, default is ~/${user.home}/.settings-security.xml
        System.setProperty("settings.security", mavenSecurityFilePath.toString());

        Log logger = mock(Log.class);
        Path baseDir = Path.of("./src/test/resources/rewrite-maven-plugin");
        boolean pomCacheEnabled = false;
        @Nullable String pomCacheDirectory = null;
        RuntimeInformation runtime = mock(RuntimeInformation.class);
        boolean skipMavenParsing = false;
        Collection<String> exclusions = List.of();
        Collection<String> plainTextMasks = List.of();
        int sizeThresholdMb = 1_000_000;

        return new MavenMojoProjectParser(logger, baseDir, pomCacheEnabled, pomCacheDirectory, runtime,
                                          skipMavenParsing, exclusions, plainTextMasks, sizeThresholdMb, mavenSession,
                                          settingsDecrypter);
    }

}