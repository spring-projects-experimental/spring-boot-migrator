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
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.MavenProject;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.apache.maven.rtinfo.internal.DefaultRuntimeInformation;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;

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
    public Map<Path, List<Marker>> generateProvenanceMarkers(Path baseDir, TopologicallySortedProjects pomFileResources) {

        RuntimeInformation runtimeInformation = new DefaultRuntimeInformation();
        SettingsDecrypter settingsDecrypter = null;

        MavenMojoProjectParser helper = getMavenMojoProjectParser(baseDir, runtimeInformation, settingsDecrypter);
        Map<Path, List<Marker>> result = new HashMap<>();

        pomFileResources.getOrdered().forEach(pom -> {
            // FIXME: this results in another Maven execution but the MavenProject could be retrieved from the current execution.
            // FIXME: This results in multiple calls to 'mvn install'
            MavenProject mavenProject = createMavenProject(pom);
            List<Marker> markers = helper.generateProvenance(mavenProject);
            result.put(ResourceUtil.getPath(pom), markers);
        });
        return result;
    }

    @NotNull
    private MavenMojoProjectParser getMavenMojoProjectParser(Path baseDir, RuntimeInformation runtimeInformation, SettingsDecrypter settingsDecrypter) {
        return mavenMojoProjectParserFactory.create(baseDir, runtimeInformation, settingsDecrypter);
    }

    private MavenProject createMavenProject(Resource pom) {
        return mavenProjectFactory.createMavenProjectFromMaven(pom);
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
