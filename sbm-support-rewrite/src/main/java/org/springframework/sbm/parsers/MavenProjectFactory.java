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
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.execution.AbstractExecutionListener;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.*;
import org.apache.maven.project.artifact.PluginArtifact;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
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


/**
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
public class MavenProjectFactory {

    private final MavenPlexusContainerFactory plexusContainerFactory;
    private final MavenExecutor mavenExecutor;
    private final MavenExecutionRequestFactory requestFactory;

    /**
     * Takes a {@code pom.xml} {@link File} and returns the {@link MavenProject} for it.
     */
    public MavenProject createMavenProject(File file) {
        if (!file.isFile() || !"pom.xml".equals(file.getName())) {
            throw new IllegalArgumentException("Maven pom.xml file must be provided.");
        }
        try {
            Path baseDir = file.toPath().getParent();
            PlexusContainer plexusContainer = plexusContainerFactory.create();
            AtomicReference<MavenProject> projectAtomicReference = new AtomicReference<>();
            final ProjectBuilder builder = plexusContainer.lookup(ProjectBuilder.class);
            MavenExecutionRequest request = requestFactory.createMavenExecutionRequest(plexusContainer, baseDir);
            request.setExecutionListener(new AbstractExecutionListener() {
                @Override
                public void sessionStarted(ExecutionEvent event) {

                    super.sessionStarted(event);
                    try {
                        DefaultProjectBuildingRequest request = new DefaultProjectBuildingRequest();

                        request.setSystemProperties(System.getProperties());
                        request.setProcessPlugins(false);
                        request.setRepositorySession(event.getSession().getRepositorySession());
                        ProjectBuildingResult buildingResult = builder.build(file, request);
                        projectAtomicReference.set(buildingResult.getProject());
                    } catch (ProjectBuildingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            request.setGoals(List.of("validate"));
            mavenExecutor.execute(request);
            return projectAtomicReference.get();
        } catch (ComponentLookupException e) {
            throw new RuntimeException(e);
        }
    }

    public MavenProject createMavenProject(Resource pom) {
        try {
            return createMavenProject(pom.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MavenProject createMavenProject(String s) {
        try {

            DefaultProjectBuilder builder = new DefaultProjectBuilder();
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new ByteArrayInputStream(s.getBytes()));
            ProjectBuildingRequest request = buildRequest();
//            builder.build(model., request);

            MavenProject mavenProject = new MavenProject(model);
            mavenProject.setName(model.getName());
            mavenProject.setGroupId(model.getGroupId());
            mavenProject.setArtifactId(model.getArtifactId());
            mavenProject.setVersion(model.getVersion());
            if (model.getBuild() != null) {
                Plugin plugin = model.getBuild().getPlugins().get(0);

                PluginArtifact pluginArtifact = new PluginArtifact(plugin, new DefaultArtifact(
                        plugin.getGroupId(),
                        plugin.getArtifactId(),
                        plugin.getVersion(),
                        "",
                        "",
                        "",
                        new DefaultArtifactHandler()
                ));
                mavenProject.setPluginArtifacts(Set.of(pluginArtifact));
            }
            return mavenProject;
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }
    }

    private ProjectBuildingRequest buildRequest() {
        DefaultProjectBuildingRequest request = new DefaultProjectBuildingRequest();
        request.setSystemProperties(System.getProperties());
        return request;
    }
}
