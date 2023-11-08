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
package org.springframework.sbm.parsers.maven;

import lombok.RequiredArgsConstructor;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.model.Profile;
import org.apache.maven.repository.UserLocalArtifactRepository;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
class MavenExecutionRequestFactory {

    private final MavenConfigFileParser mavenConfigFileParser;
    private static final String LOCAL_REPOSITORY = Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository").toString();
    private static final List<String> MAVEN_GOALS = List.of("clean", "package");// "dependency:resolve";

    public MavenExecutionRequest createMavenExecutionRequest(PlexusContainer plexusContainer, Path baseDir) {
        try {
            MavenExecutionRequest request = new DefaultMavenExecutionRequest();
            ArtifactRepositoryFactory repositoryFactory = plexusContainer.lookup(ArtifactRepositoryFactory.class);

            repositoryFactory.setGlobalChecksumPolicy("warn");
            repositoryFactory.setGlobalUpdatePolicy("never");
            ArtifactRepository repository = new UserLocalArtifactRepository(repositoryFactory.createArtifactRepository("local", "file://" + LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null));// repositoryFactory.createArtifactRepository("local", "file://" + LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null); // new MavenArtifactRepository("local", "file://"+LOCAL_REPOSITORY, new DefaultRepositoryLayout(), null, null);
            repository.setUrl("file://" + LOCAL_REPOSITORY);
            repository.setReleaseUpdatePolicy(new ArtifactRepositoryPolicy(true, "never", "warn"));
            repository.setMirroredRepositories(List.of());
            repository.setSnapshotUpdatePolicy(new ArtifactRepositoryPolicy(true, "never", "warn"));

            request.setBaseDirectory(baseDir.toFile());
            request.setShowErrors(true);
            request.setLocalRepositoryPath(LOCAL_REPOSITORY);
            request.setPluginArtifactRepositories(List.of(repository));

            List<String> activatedProfiles = mavenConfigFileParser.getActivatedProfiles(baseDir);
            if (activatedProfiles.isEmpty()) {
                request.setActiveProfiles(List.of("default"));
            } else {
                request.setActiveProfiles(activatedProfiles);
            }

            Map<String, String> userPropertiesFromConfig = mavenConfigFileParser.getUserProperties(baseDir);
            Properties userProperties = new Properties();
            if (!userPropertiesFromConfig.isEmpty()) {
                userProperties.putAll(userPropertiesFromConfig);
            }
            userProperties.put("skipTests", "true");
            request.setUserProperties(userProperties);

            request.setRemoteRepositories(List.of(new MavenArtifactRepository("central", "https://repo.maven.apache.org/maven2", new DefaultRepositoryLayout(), new ArtifactRepositoryPolicy(true, "never", "warn"), new ArtifactRepositoryPolicy(true, "never", "warn"))));

            // TODO: make profile configurable
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
            request.setGoals(MAVEN_GOALS);
            request.setPom(baseDir.resolve("pom.xml").toFile());
            return request;
        } catch (ComponentLookupException e) {
            throw new RuntimeException(e);
        }
    }
}
