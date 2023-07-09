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
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.PluginArtifact;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.apache.maven.rtinfo.internal.DefaultRuntimeInformation;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
class ProvenanceMarkerFactory {

    private final ParserSettings parserSettings;
    private final MavenProjectFactory mavenProjectFactory;
    private final MavenMojoProjectParserFactory mavenMojoProjectParserFactory;

    /**
     * Reuses {@link MavenMojoProjectParser#generateProvenance(MavenProject)} to create {@link Marker}s for pom files in
     * provided {@code pomFileResources}.
     *
     * @return the map of pom.xml {@link Resource}s and their {@link Marker}s.
     */
    public Map<Path, List<Marker>> generateProvenanceMarkers(Path baseDir, List<Resource> pomFileResources) {

        RuntimeInformation runtimeInformation = new DefaultRuntimeInformation();
        MavenSession mavenSession = null;
        SettingsDecrypter settingsDecrypter = null;

        MavenMojoProjectParser helper = getMavenMojoProjectParser(baseDir, runtimeInformation, mavenSession, settingsDecrypter);
        Map<Path, List<Marker>> result = new HashMap<>();
        pomFileResources.forEach(pom -> {
            MavenProject mavenProject = createMavenProject(pom);
            List<Marker> markers = helper.generateProvenance(mavenProject);
            result.put(ResourceUtil.getPath(pom), markers);
        });
        return result;
    }

    @NotNull
    private MavenMojoProjectParser getMavenMojoProjectParser(Path baseDir, RuntimeInformation runtimeInformation, MavenSession mavenSession, SettingsDecrypter settingsDecrypter) {
        return mavenMojoProjectParserFactory.create(baseDir, runtimeInformation, settingsDecrypter);
    }

    private MavenProject createMavenProject(Resource pom) {
        return mavenProjectFactory.createMavenProject(pom);
    }

    private Log getLogger(ParserSettings parserSettings) {
        String loggerClassName = parserSettings.getLoggerClass();
        Log log = new SystemStreamLog();
        if(loggerClassName != null) {
            try {
                Class<?> loggerClass = Class.forName(loggerClassName);
                Object loggerObj = loggerClass.getConstructor().newInstance();
                if(loggerObj instanceof Log logger) {
                    log = logger;
                } else {
                    throw new ClassCastException("Class name provided as 'parser.loggerClass' is not of type %s".formatted(Log.class.getName()));
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Could not find type '%s' which is provided as 'parser.loggerClass'".formatted(loggerClassName), e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Could not find default constructor on logger class type: '%s' which is provided as 'parser.loggerClass'".formatted(loggerClassName), e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Could not invoke default constructor in logger class '%s' which is provided as 'parser.loggerClass'".formatted(loggerClassName), e);
            }
        }
        return log;
    }
}
