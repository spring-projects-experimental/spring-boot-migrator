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

import lombok.RequiredArgsConstructor;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.crypto.*;
import org.openrewrite.maven.MavenSettings;
import org.sonatype.plexus.components.cipher.PlexusCipher;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
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
@Component
@RequiredArgsConstructor
// TODO: should be package private
public class MavenPasswordDecrypter {

    private MavenPlexusContainerFactory containerFactory;
    @Deprecated
    private SecDispatcher securityDispatcher;
    @Deprecated
    private final SettingsDecrypter settingsDecrypter = new DefaultSettingsDecrypter(securityDispatcher);

    public void decryptMavenServerPasswords(PlexusCipher plexusCipher, MavenSettings mavenSettings, Path mavenSecuritySettingsFile) {
//        try {
            // DefaultSecDispatcher reads file with security settings
            // hack, the cipher is required but can't be set from outside
            Field cipher1 = ReflectionUtils.findField(DefaultSecDispatcher.class, "_cipher");
            //Field cipher = ReflectionUtils.getField(cipher1, securityDispatcher);
            securityDispatcher = new DefaultSecDispatcher(plexusCipher);
//            ReflectionUtils.makeAccessible(cipher1);
//            ReflectionUtils.setField(cipher1, securityDispatcher, new DefaultPlexusCipher());
            // The file location is retrieved from env, default is ~/${user.home}/.settings-security.xml
            System.setProperty("settings.security", mavenSecuritySettingsFile.toString());
            decryptMavenServerPasswords(plexusCipher, mavenSettings, mavenSecuritySettingsFile);
//        } catch (PlexusCipherException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void decryptMavenServerPasswords2(SecDispatcher secDispatcher, MavenSettings mavenSettings, Path mavenSecuritySettingsFile) {
//        try {
        // DefaultSecDispatcher reads file with security settings
        // hack, the cipher is required but can't be set from outside
        //Field cipher = ReflectionUtils.getField(cipher1, securityDispatcher);
        securityDispatcher = secDispatcher;
//            ReflectionUtils.makeAccessible(cipher1);
//            ReflectionUtils.setField(cipher1, securityDispatcher, new DefaultPlexusCipher());
        // The file location is retrieved from env, default is ~/${user.home}/.settings-security.xml
        System.setProperty("settings.security", mavenSecuritySettingsFile.toString());
        decryptMavenServerPasswords(secDispatcher, mavenSettings, mavenSecuritySettingsFile);
//        } catch (PlexusCipherException e) {
//            throw new RuntimeException(e);
//        }
    }



    private void decryptMavenServerPasswords(SecDispatcher plexusCipher, MavenSettings mavenSettings, Path mavenSecurityFilePath) {
        if(mavenSettings.getServers() != null && mavenSettings.getServers().getServers() != null) {
            List<MavenSettings.Server> servers = mavenSettings.getServers().getServers();
            for(int i = 0; i< servers.size(); i++){
                MavenSettings.Server server = servers.get(i);
                if(server.getPassword() != null) {
                    MavenSettings.Server serverWithDecodedPw = decryptPassword(server);
                    servers.set(i, serverWithDecodedPw);
                }
            }
        }
    }

    private MavenSettings.Server decryptPassword(MavenSettings.Server server) {
        Server mavenServer = new Server();
        mavenServer.setPassword(server.getPassword());
        SettingsDecryptionRequest decryptionRequest = new DefaultSettingsDecryptionRequest(mavenServer);
        SettingsDecryptionResult decryptionResult = settingsDecrypter.decrypt(decryptionRequest);
        String decryptedPassword = decryptionResult.getServer().getPassword();
        return server.withPassword(decryptedPassword);
    }
}
