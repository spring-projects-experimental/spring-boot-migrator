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
import org.apache.maven.Maven;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.execution.*;
import org.apache.maven.graph.GraphBuilder;
import org.apache.maven.model.Profile;
import org.apache.maven.model.building.Result;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.UserLocalArtifactRepository;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Uses Mavens {@link GraphBuilder} to build the graph of Maven projects from the list of {@link Resource}s.
 *
 * Internally a Maven application context is created and an instance of {@link GraphBuilder} is retrieved from the container.
 * The {@link GraphBuilder} is then  class requires the provided resources to exist on filesystem.
 *
 * TODO: Check if GraphBuilder uses to active profiles
 *
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
class MavenBuildFileGraph implements BuildFileGraph {

    public static final String LOCAL_REPOSITORY = Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository").toString();
    private final MavenPlexusContainerFactory containerFactory;


    @Override
    public List<Resource> build(Path baseDir, List<Resource> resources) {
        try {
            PlexusContainer plexusContainer = containerFactory.create();
            GraphBuilder graphBuilder = plexusContainer.lookup(GraphBuilder.class);

            Maven maven = plexusContainer.lookup(Maven.class);

            MavenExecutionRequest request = new DefaultMavenExecutionRequest();
            ArtifactRepositoryFactory repositoryFactory = plexusContainer.lookup(ArtifactRepositoryFactory.class);
            ArtifactRepository repository = new UserLocalArtifactRepository(repositoryFactory.createArtifactRepository("local", "file://" + LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null));// repositoryFactory.createArtifactRepository("local", "file://" + LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null); // new MavenArtifactRepository("local", "file://"+LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null);
            repository.setUrl("file://" + LOCAL_REPOSITORY);
            request.setBaseDirectory(baseDir.toFile());
            request.setLocalRepositoryPath(LOCAL_REPOSITORY);
            request.setActiveProfiles(List.of("default")); // TODO: make profile configurable
            // fixes the maven run when plugins depending on Java version are encountered.
            // This is the case for some transitive dependencies when running against the SBM code base itself.
            // In these cases the Java version could not be retrieved without this line
            request.setSystemProperties(System.getProperties());

            Profile profile = new Profile();
            profile.setId("default");
            request.setProfiles(List.of(profile));
            request.setDegreeOfConcurrency(1);
            request.setLoggingLevel(MavenExecutionRequest.LOGGING_LEVEL_DEBUG);
            request.setMultiModuleProjectDirectory(baseDir.toFile());
            request.setLocalRepository(repository);
            request.setGoals(List.of("validate"));
            request.setPom(baseDir.resolve("pom.xml").toAbsolutePath().normalize().toFile());

            AtomicReference<Result<? extends ProjectDependencyGraph>> reference = new AtomicReference<>();
            request.setExecutionListener(new AbstractExecutionListener() {
                @Override
                public void projectSucceeded(ExecutionEvent event) {
                    Result<? extends ProjectDependencyGraph> build = graphBuilder.build(event.getSession());
                    reference.set(build);
                }
            });

            MavenExecutionResult result = maven.execute(request);

            List<MavenProject> topologicallySortedProjects = result.getTopologicallySortedProjects();

            List<Resource> ordered = new ArrayList<>();
//            ordered.add(result.getProject().getFile().toPath());
            ordered = topologicallySortedProjects
                            .stream()
                            .map(MavenProject::getFile)
                            .map(File::toPath)
                    .map(m -> this.findResourceWithPath(m, resources))
                            .toList();

            // TODO: Should pom files not belonging to the reactor be filtered out?!

//            List<Path> finalOrdered = ordered;
//            List<Resource> list = resources.stream()
//                    .filter(resource -> finalOrdered.contains(ResourceUtil.getPath(resource)))
//                    .toList();

            return ordered;

//            List<File> pomFiles = resources.stream()
//                    .filter(r -> ResourceUtil.getPath(r).toFile().getName().equals("pom.xml"))
//                    .map(ResourceUtil::getPath)
//                    .map(Path::toFile)
//                    .toList();
//
//            graphBuilder.build(new MavenSession(plexusContainer, ) {
//
//            });
//
//            return gra;
        } catch (ComponentLookupException e) {
            throw new RuntimeException(e);
        }
    }

    private Resource findResourceWithPath(Path m, List<Resource> resources) {
        return resources.stream()
                .filter(r -> ResourceUtil.getPath(r).equals(m))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find a resource in the list of resources that matches the path of pom '%s'".formatted(m.toString())));
    }

}
