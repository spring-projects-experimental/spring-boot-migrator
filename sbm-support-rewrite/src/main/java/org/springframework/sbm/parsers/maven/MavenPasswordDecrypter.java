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
package org.springframework.sbm.parsers.maven;

import lombok.RequiredArgsConstructor;
import org.openrewrite.maven.MavenSettings;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;
import org.sonatype.plexus.components.cipher.PlexusCipherException;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;

import java.nio.file.Path;
import java.util.List;

/**
 * Decrypt Maven Server passwords.
 *
 * Requires access to Maven security settings file (~/.settings-security.xml) to decrypt the server passwords provided in
 * the server settings in Maven settings file (~/.m2/settings.xml) which are provided with {@link MavenSettings}.
 *
 * @author Fabian Krüger
 */
class MavenPasswordDecrypter {

    private final SecDispatcher secDispatcher;

    public MavenPasswordDecrypter() {
        try {
            this.secDispatcher = new DefaultSecDispatcher(new DefaultPlexusCipher());
        } catch (PlexusCipherException e) {
            throw new RuntimeException(e);
        }
    }

    public void decryptMavenServerPasswords(MavenSettings mavenSettings, Path mavenSecuritySettingsFile) {
        System.setProperty("settings.security", mavenSecuritySettingsFile.toString());
        if (mavenSettings.getServers() != null && mavenSettings.getServers().getServers() != null) {
            List<MavenSettings.Server> servers = mavenSettings.getServers().getServers();
            for (int i = 0; i < servers.size(); i++) {
                MavenSettings.Server server = servers.get(i);
                if (server.getPassword() != null) {
                    MavenSettings.Server serverWithDecodedPw = decryptPassword(server);
                    servers.set(i, serverWithDecodedPw);
                }
            }
        }
    }

    private MavenSettings.Server decryptPassword(MavenSettings.Server server) {
        try {
            String decryptionResult = secDispatcher.decrypt(server.getPassword());
            return server.withPassword(decryptionResult);
        } catch (SecDispatcherException e) {
            throw new RuntimeException(e);
        }
    }
}
