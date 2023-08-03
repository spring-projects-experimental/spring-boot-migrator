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
import org.apache.maven.execution.*;
import org.apache.maven.graph.GraphBuilder;
import org.apache.maven.model.building.Result;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Uses Mavens {@link GraphBuilder} to build the graph of Maven projects from the list of {@link Resource}s.
 * <p>
 * Internally a Maven application context is created and an instance of {@link GraphBuilder} is retrieved from the container.
 * The {@link GraphBuilder} is then  class requires the provided resources to exist on filesystem.
 * <p>
 * TODO: Check if GraphBuilder uses to active profiles
 *
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
class MavenBuildFileGraph implements BuildFileGraph {

    public static final String LOCAL_REPOSITORY = Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository").toString();
    private final PlexusContainerProvider plexusContainerProvider;


    @Override
    public TopologicallySortedProjects build(List<Resource> resources, MavenSession mavenSession) {
        try {
            PlexusContainer plexusContainer = plexusContainerProvider.get();
            GraphBuilder graphBuilder = plexusContainer.lookup(GraphBuilder.class);
//
//            Maven maven = plexusContainer.lookup(Maven.class);
//
//            MavenExecutionRequest request = new DefaultMavenExecutionRequest();
//            ArtifactRepositoryFactory repositoryFactory = plexusContainer.lookup(ArtifactRepositoryFactory.class);
//            ArtifactRepository repository = new UserLocalArtifactRepository(repositoryFactory.createArtifactRepository("local", "file://" + LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null));// repositoryFactory.createArtifactRepository("local", "file://" + LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null); // new MavenArtifactRepository("local", "file://"+LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null);
//            repository.setUrl("file://" + LOCAL_REPOSITORY);
//            request.setBaseDirectory(baseDir.toFile());
//            request.setLocalRepositoryPath(LOCAL_REPOSITORY);
//            request.setActiveProfiles(List.of("default")); // TODO: make profile configurable
//            // fixes the maven run when plugins depending on Java version are encountered.
//            // This is the case for some transitive dependencies when running against the SBM code base itself.
//            // In these cases the Java version could not be retrieved without this line
//            request.setSystemProperties(System.getProperties());
//
//            Profile profile = new Profile();
//            profile.setId("default");
//            request.setProfiles(List.of(profile));
//            request.setDegreeOfConcurrency(1);
//            request.setLoggingLevel(MavenExecutionRequest.LOGGING_LEVEL_DEBUG);
//            request.setMultiModuleProjectDirectory(baseDir.toFile());
//            request.setLocalRepository(repository);
//            request.setGoals(List.of("validate"));
//            request.setPom(baseDir.resolve("pom.xml").toAbsolutePath().normalize().toFile());

            AtomicReference<Result<? extends ProjectDependencyGraph>> reference = new AtomicReference<>();
//            request.setExecutionListener(new AbstractExecutionListener() {
//                @Override
//                public void projectSucceeded(ExecutionEvent event) {
            Result<? extends ProjectDependencyGraph> result = graphBuilder.build(mavenSession);
            List<MavenProject> allProjects = result.get().getSortedProjects();
            List<Resource> ordered = new ArrayList<>();
//            ordered.add(result.getProject().getFile().toPath());
            ordered = allProjects
                    .stream()
                    .map(MavenProject::getFile)
                    .map(File::toPath)
                    .map(m -> this.findResourceWithPath(m, resources))
                    .toList();
            return new TopologicallySortedProjects(ordered);
//                    reference.set(build);
//                }
//            });
//
//            MavenExecutionResult result = maven.execute(request);
//
//            List<MavenProject> topologicallySortedProjects = result.getTopologicallySortedProjects();
//
//
//
//            // TODO: Should pom files not belonging to the reactor be filtered out?!
//
////            List<Path> finalOrdered = ordered;
////            List<Resource> list = resources.stream()
////                    .filter(resource -> finalOrdered.contains(ResourceUtil.getPath(resource)))
////                    .toList();
//
//            return new TopologicallySortedProjects(ordered);
//
////            List<File> pomFiles = resources.stream()
////                    .filter(r -> ResourceUtil.getPath(r).toFile().getName().equals("pom.xml"))
////                    .map(ResourceUtil::getPath)
////                    .map(Path::toFile)
////                    .toList();
////
////            graphBuilder.build(new MavenSession(plexusContainer, ) {
////
////            });
////
////            return gra;
        } catch (ComponentLookupException e) {
            throw new RuntimeException(e);
        }
    }

}
