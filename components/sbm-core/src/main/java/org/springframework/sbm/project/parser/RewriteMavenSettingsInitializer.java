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

import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenSettings;
import org.springframework.core.io.Resource;
import org.springframework.sbm.project.parser.MavenPasswordDecrypter;
import org.springframework.sbm.testhelper.ResourceUtil;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
public class RewriteMavenSettingsInitializer {

    private final MavenPasswordDecrypter mavenPasswordDecrypter;

    /**
     * @deprecated initialization in ExecutionoContext is done in ProjectParser
     */
    @Deprecated(forRemoval = true)
    public void initializeMavenSettings(ExecutionContext executionContext) {
        // Read .m2/settings.xml
        // TODO: Add support for global Maven settings (${maven.home}/conf/settings.xml).
        MavenExecutionContextView mavenExecutionContextView = MavenExecutionContextView.view(executionContext);
        Path mavenSettingsFile = Path.of(System.getProperty("user.home")).resolve(".m2/settings.xml");
        if (Files.exists(mavenSettingsFile)) {
            MavenSettings mavenSettings = MavenSettings.parse(mavenSettingsFile, mavenExecutionContextView);
            mavenExecutionContextView.setMavenSettings(mavenSettings);
        }
    }

    public MavenSettings initializeMavenSettings(ExecutionContext executionContext, Resource mavenSettingsFile, Path securitySettingsFilePath) {
        Parser.Input input = new Parser.Input(ResourceUtil.getPath(mavenSettingsFile), () -> ResourceUtil.getInputStream(mavenSettingsFile));
        MavenSettings mavenSettings = MavenSettings.parse(input, executionContext);
        if(securitySettingsFilePath != null && securitySettingsFilePath.toFile().exists()) {
            mavenPasswordDecrypter.decryptMavenServerPasswords(mavenSettings, securitySettingsFilePath);
        }
        MavenExecutionContextView.view(executionContext).setMavenSettings(mavenSettings);
        return mavenSettings;
    }

}
