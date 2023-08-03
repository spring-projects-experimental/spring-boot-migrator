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
package org.springframework.sbm.parsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.graph.DefaultProjectDependencyGraph;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.codehaus.plexus.PlexusContainer;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

/**
 * @author Fabian Krüger
 */
@Slf4j
@Component
@RequiredArgsConstructor
class MavenMojoProjectParserFactory {

    private final ParserSettings parserSettings;

    public MavenMojoProjectParser create(Path baseDir, List<MavenProject> mavenProjects, PlexusContainer plexusContainer, MavenSession session) {
        return buildMavenMojoProjectParser(
                baseDir,
                mavenProjects,
                parserSettings.isPomCacheEnabled(),
                parserSettings.getPomCacheDirectory(),
                parserSettings.isSkipMavenParsing(),
                parserSettings.getExclusions(),
                parserSettings.getPlainTextMasks(),
                parserSettings.getSizeThresholdMb(),
                parserSettings.isRunPerSubmodule(),
                plexusContainer,
                session);
    }

    @NotNull
    private MavenMojoProjectParser buildMavenMojoProjectParser(
            Path baseDir,
            List<MavenProject> mavenProjects,
            boolean pomCacheEnabled,
            String pomCacheDirectory,
            boolean skipMavenParsing,
            Collection<String> exclusions,
            Collection<String> plainTextMasks,
            int sizeThresholdMb,
            boolean runPerSubmodule,
            PlexusContainer plexusContainer,
            MavenSession session) {
        try {
            Log logger = new Slf4jToMavenLoggerAdapter(log);
            RuntimeInformation runtimeInformation = plexusContainer.lookup(RuntimeInformation.class);
            SettingsDecrypter decrypter = plexusContainer.lookup(SettingsDecrypter.class);

            MavenMojoProjectParser sut = new MavenMojoProjectParser(
                    logger,
                    baseDir,
                    pomCacheEnabled,
                    pomCacheDirectory,
                    runtimeInformation,
                    skipMavenParsing,
                    exclusions,
                    plainTextMasks,
                    sizeThresholdMb,
                    session,
                    decrypter,
                    runPerSubmodule);

            return sut;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MavenMojoProjectParser create(Path baseDir, RuntimeInformation runtimeInformation, SettingsDecrypter settingsDecrypter) {
        return new MavenMojoProjectParser(
                new Slf4jToMavenLoggerAdapter(log),
                baseDir,
                parserSettings.isPomCacheEnabled(),
                parserSettings.getPomCacheDirectory(),
                runtimeInformation,
                parserSettings.isSkipMavenParsing(),
                parserSettings.getExclusions(),
                parserSettings.getPlainTextMasks(),
                parserSettings.getSizeThresholdMb(),
                null,
                settingsDecrypter,
                parserSettings.isRunPerSubmodule()
        );
    }
}
