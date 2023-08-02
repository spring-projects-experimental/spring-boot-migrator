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
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.*;
import org.apache.maven.project.artifact.PluginArtifact;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
public class MavenProjectFactory {

    private final MavenExecutor mavenExecutor;

    /**
     * Convenience method for {@link #createMavenProjectFromMaven(File)}.
     */
     public MavenProject createMavenProjectFromMaven(Resource pom) {
        try {
            return createMavenProjectFromMaven(pom.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Creates {@link MavenProject} instance from a given pom file.
     * It uses the {@link MavenExecutor} to run `{@code dependency:resolve}` goal
     * and provides the {@link MavenProject} received from {@link org.apache.maven.execution.ExecutionEvent}.
     * All classpath elements are resolved.
     *
     * @deprecated
     * This method starts a new Maven build. There should be only one Maven build
     */
    @Deprecated
    public MavenProject createMavenProjectFromMaven(File file) {
        if (!file.isFile() || !"pom.xml".equals(file.getName())) {
            throw new IllegalArgumentException("Maven pom.xml file must be provided.");
        }

        Path baseDir = file.toPath().getParent();
        AtomicReference<MavenProject> projectAtomicReference = new AtomicReference<>();
        mavenExecutor.onProjectSucceededEvent(baseDir, List.of("clean", "install"), event -> {
            MavenProject project = event.getProject();
            projectAtomicReference.set(project);
        });
        return projectAtomicReference.get();
    }

    /**
     *
     */
    public MavenProject createMavenProjectFromPomContent(String s) {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new ByteArrayInputStream(s.getBytes()));
            MavenProject mavenProject = new MavenProject(model);
            mavenProject.setName(model.getName());
            mavenProject.setGroupId(model.getGroupId());
            mavenProject.setArtifactId(model.getArtifactId());
            mavenProject.setVersion(model.getVersion());
            if (model.getBuild() != null) {
                Set<Artifact> pluginArtifacts = model.getBuild().getPlugins().stream()
                        .map(plugin -> new PluginArtifact(plugin, new DefaultArtifact(
                                plugin.getGroupId(),
                                plugin.getArtifactId(),
                                plugin.getVersion(),
                                "",
                                "",
                                "",
                                new DefaultArtifactHandler()
                        )))
                        .collect(Collectors.toSet());
                mavenProject.setPluginArtifacts(pluginArtifacts);
            }
            return mavenProject;
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }
    }
}
