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
package org.springframework.sbm.parsers;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.MavenSettings;
import org.sonatype.plexus.components.cipher.PlexusCipher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class MavenPasswordDecrypterTest {

    public static final String ENCRYPTED_PASSWORD = "{gY56I8DPBrwHu3dWkh+EGDqp+ppuTnBaWe9fNpdTPIw=}";
    public static final String DECRYPTED_PASSWORD = "xxx";

    @Test
    @DisplayName("Using Maven to decrypt password")
    void usingMavenToDecryptPassword() throws ComponentLookupException, SecDispatcherException {
        MavenPlexusContainerFactory containerFactory = new MavenPlexusContainerFactory();
        PlexusContainer plexusContainer = containerFactory.create();
        SecDispatcher plexusCipher = plexusContainer.lookup(SecDispatcher.class);
        String decryptedPassword = plexusCipher.decrypt(ENCRYPTED_PASSWORD);
        assertThat(decryptedPassword).isEqualTo(DECRYPTED_PASSWORD);
    }

    @Test
    void decryptMavenServerPasswords(@TempDir Path tempDir) throws IOException, ComponentLookupException {
        MavenPasswordDecrypter sut = new MavenPasswordDecrypter();

        // create MavenSettings with Server using an encrypted password
        MavenSettings.@Nullable Servers serversWithPassword = new MavenSettings.Servers();
        String encryptedPassword = "{gY56I8DPBrwHu3dWkh+EGDqp+ppuTnBaWe9fNpdTPIw=}";
        MavenSettings.Server serverWithPassword = new MavenSettings.Server("id", "username", encryptedPassword);
        ArrayList servers = new ArrayList<>();
        servers.add(serverWithPassword);
        serversWithPassword.setServers(servers);
        MavenSettings mavenSettings = new MavenSettings(null, null, null, null, serversWithPassword);

        // Write Maven security settings file to file system
        // Required because {@link DefaultSecDispatcher} uses SecUtil which reads the file from
        // provided location
        Path mavenSecurityFilePath = tempDir.resolve(".settings-security.xml");
        String mavenSecurityFileContent = """
                <settingsSecurity>
                  <master>{H39p2LFBhRQHqKIsRiu0R/zhfke3/B4k0Wt+BZlC8mc=}</master>
                </settingsSecurity>
                """;
        Files.writeString(mavenSecurityFilePath, mavenSecurityFileContent);

        // Server has encrypted password
        assertThat(mavenSettings.getServers().getServers().get(0).getPassword()).isEqualTo(encryptedPassword);
        // call system under test
        MavenPlexusContainerFactory containerFactory = new MavenPlexusContainerFactory();
        PlexusContainer plexusContainer = containerFactory.create();
        SecDispatcher plexusCipher = plexusContainer.lookup(SecDispatcher.class);
        sut.decryptMavenServerPasswords2(plexusCipher, mavenSettings, mavenSecurityFilePath);
        // password has been decrypted
        assertThat(mavenSettings.getServers().getServers().get(0).getPassword()).isEqualTo(DECRYPTED_PASSWORD);
    }
}