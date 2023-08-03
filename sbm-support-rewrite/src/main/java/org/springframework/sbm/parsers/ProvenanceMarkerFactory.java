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
import org.apache.maven.project.MavenProject;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.apache.maven.rtinfo.internal.DefaultRuntimeInformation;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.*;

/**
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
class ProvenanceMarkerFactory {

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

        MavenMojoProjectParser helper = mavenMojoProjectParserFactory.create(baseDir, runtimeInformation, settingsDecrypter);
        Map<Path, List<Marker>> result = new HashMap<>();

        pomFileResources.getSortedProjects().forEach(mavenProject -> {
            // FIXME: this results in another Maven execution but the MavenProject could be retrieved from the current execution.
            // FIXME: This results in multiple calls to 'mvn install'
            List<Marker> markers = helper.generateProvenance(mavenProject);
            Resource resource = pomFileResources.getMatchingBuildFileResource(mavenProject);
            Path path = ResourceUtil.getPath(resource);
            result.put(path, markers);
        });
        return result;
    }

}
