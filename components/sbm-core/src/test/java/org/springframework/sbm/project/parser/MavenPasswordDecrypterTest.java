package org.springframework.sbm.project.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.MavenSettings;
import org.springframework.sbm.project.parser.MavenPasswordDecrypter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class MavenPasswordDecrypterTest {

    @Test
    void decryptMavenServerPasswords(@TempDir Path tempDir) throws IOException {
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
        sut.decryptMavenServerPasswords(mavenSettings, mavenSecurityFilePath);
        // password has been decrypted
        assertThat(mavenSettings.getServers().getServers().get(0).getPassword()).isEqualTo("xxx");
    }
}