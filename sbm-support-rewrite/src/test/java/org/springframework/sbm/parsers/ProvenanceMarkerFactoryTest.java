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

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.rtinfo.internal.DefaultRuntimeInformation;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.marker.BuildTool;
import org.openrewrite.marker.GitProvenance;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.OperatingSystemProvenance;
import org.openrewrite.marker.ci.BuildEnvironment;
import org.openrewrite.shaded.jgit.api.Git;
import org.openrewrite.shaded.jgit.lib.Repository;
import org.openrewrite.shaded.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.core.io.Resource;
import org.springframework.sbm.test.util.DummyResource;
import org.springframework.sbm.utils.ResourceUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class ProvenanceMarkerFactoryTest {

    @Nested
    public class GivenSimpleMultiModuleProject {

        @Test
        @DisplayName("Should Create Provenance Markers")
        void shouldCreateProvenanceMarkers(@TempDir Path tempDir)  {

            @Language("xml")
            String pom1Content =
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project xmlns="http://maven.apache.org/POM/4.0.0"
                             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                        <modelVersion>4.0.0</modelVersion>
                                    
                        <groupId>com.example</groupId>
                        <artifactId>parent-module</artifactId>
                        <version>1.0</version>
                        <packaging>pom</packaging>
                        <modules>
                            <module>module1</module>
                        </modules>
                    </project>
                    """;

            @Language("xml")
            String pom2Content =
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project xmlns="http://maven.apache.org/POM/4.0.0"
                             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                        <modelVersion>4.0.0</modelVersion>
                        <parent>
                            <groupId>com.example</groupId>
                            <artifactId>parent-module</artifactId>
                            <version>1.0</version>
                        </parent>
                        <packaging>pom</packaging>
                        <artifactId>module1</artifactId>
                        <modules>
                            <module>submodule</module>
                        </modules>
                    </project>
                    """;

            @Language("xml")
            String pom3Content =
                    """
                    <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                        <modelVersion>4.0.0</modelVersion>
                        <parent>
                            <groupId>com.example</groupId>
                            <artifactId>module1</artifactId>
                            <version>1.0</version>
                        </parent>
                        <name>TheSubmodule</name>
                        <version>1.1</version>
                        <artifactId>submodule</artifactId>
                    </project>
                    """;
            Resource pom1 = new DummyResource(tempDir.resolve("pom.xml"), pom1Content);
            Resource pom2 = new DummyResource(tempDir.resolve("module1/pom.xml"), pom2Content);
            Resource pom3 = new DummyResource(tempDir.resolve("module1/submodule/pom.xml"), pom3Content);

            List<Resource> pomFiles = List.of(pom1, pom2, pom3);
            ResourceUtil.write(tempDir, pomFiles);

            ParserSettings parserSettings = ParserSettings.builder()
                    .loggerClass(MyLogger.class.getName())
                    .pomCacheEnabled(true)
                    .pomCacheDirectory("pom-cache")
                    .skipMavenParsing(false)
                    .exclusions(Set.of())
                    .plainTextMasks(Set.of())
                    .sizeThresholdMb(-1)
                    .runPerSubmodule(false)
                    .build();

            PlexusContainerProvider containerFactory = new PlexusContainerProvider();
            MavenExecutionRequestFactory requestFactory = new MavenExecutionRequestFactory(new MavenConfigFileParser());
            ProvenanceMarkerFactory sut = new ProvenanceMarkerFactory(
                    parserSettings,
                    new MavenProjectFactory(new MavenExecutor(requestFactory, containerFactory)),
                    new MavenMojoProjectParserFactory(parserSettings)
            );
            Path baseDir = Path.of(".").toAbsolutePath().normalize();
            Map<Path, List<Marker>> resourceListMap = sut.generateProvenanceMarkers(baseDir, new TopologicallySortedProjects(pomFiles));

            String version = "1.0";

            verifyMarkers(pom1, baseDir, resourceListMap, "parent-module", "com.example", "parent-module", version);
            verifyMarkers(pom2, baseDir, resourceListMap, "module1", "com.example", "module1", version);
            verifyMarkers(pom3, baseDir, resourceListMap, "TheSubmodule", "com.example", "submodule", "1.1");
        }

        /**
         * With a configured maven-compile-plugin the source and target version should be taken from the plugin
         */
        @Nested
        public class GivenSimpleMultiModuleProjectWithCompilerPlugin {

        }
    }

    private void verifyMarkers(Resource resource, Path baseDir, Map<Path, List<Marker>> resourceListMap, String projectName, String groupId, String artifactModule, String version) {
        assertThat(resourceListMap.get(ResourceUtil.getPath(resource))).hasSize(5);

        JavaVersion jv = findMarker(resourceListMap, resource, JavaVersion.class);
        assertThat(countGetters(jv)).isEqualTo(7);
        assertThat(jv.getCreatedBy()).isEqualTo(System.getProperty("java.specification.version"));
        assertThat(jv.getMajorVersion()).isEqualTo(Integer.parseInt(System.getProperty("java.specification.version")));
        assertThat(jv.getSourceCompatibility()).isEqualTo(System.getProperty("java.specification.version"));
        assertThat(jv.getTargetCompatibility()).isEqualTo(System.getProperty("java.specification.version"));
        assertThat(jv.getMajorReleaseVersion()).isEqualTo(Integer.parseInt(System.getProperty("java.specification.version")));
        assertThat(jv.getVmVendor()).isEqualTo(System.getProperty("java.vm.vendor"));
        assertThat(jv.getId()).isInstanceOf(UUID.class);

        JavaProject jp = findMarker(resourceListMap, resource, JavaProject.class);
        assertThat(countGetters(jp)).isEqualTo(3);
        assertThat(jp.getId()).isInstanceOf(UUID.class);
        assertThat(jp.getProjectName()).isEqualTo(projectName);
        JavaProject.Publication publication = jp.getPublication();
        assertThat(countGetters(publication)).isEqualTo(3);
        assertThat(publication.getGroupId()).isEqualTo(groupId);
        assertThat(publication.getArtifactId()).isEqualTo(artifactModule);
        assertThat(publication.getVersion()).isEqualTo(version);

        String branch = getCurrentGitBranchName();
        String origin = getCurrentGitOrigin();
        String gitHash = getCurrentGitHash();
        GitProvenance expectedGitProvenance = GitProvenance.fromProjectDirectory(baseDir, BuildEnvironment.build(System::getenv));
        GitProvenance gitProvenance = findMarker(resourceListMap, resource, GitProvenance.class);
        assertThat(countGetters(gitProvenance)).isEqualTo(9);
        assertThat(gitProvenance.getId()).isInstanceOf(UUID.class);
        assertThat(gitProvenance.getBranch()).isEqualTo(branch);
        assertThat(gitProvenance.getEol()).isEqualTo(GitProvenance.EOL.Native);
        assertThat(gitProvenance.getOrigin()).isEqualTo(origin);
        assertThat(gitProvenance.getAutocrlf()).isEqualTo(GitProvenance.AutoCRLF.Input);
        assertThat(gitProvenance.getRepositoryName()).isEqualTo(expectedGitProvenance.getRepositoryName());
        assertThat(gitProvenance.getChange()).isEqualTo(gitHash);
        assertThat(gitProvenance.getOrganizationName()).isEqualTo("spring-projects-experimental");
        assertThat(gitProvenance.getOrganizationName("https://github.com")).isEqualTo("spring-projects-experimental");

        OperatingSystemProvenance operatingSystemProvenance = findMarker(resourceListMap, resource, OperatingSystemProvenance.class);
        OperatingSystemProvenance expected = OperatingSystemProvenance.current();
        assertThat(operatingSystemProvenance.getName()).isEqualTo(expected.getName());
        // ...

        BuildTool buildTool = findMarker(resourceListMap, resource, BuildTool.class);
        assertThat(countGetters(buildTool)).isEqualTo(3);
        assertThat(buildTool.getId()).isInstanceOf(UUID.class);
        String mavenVersion = new DefaultRuntimeInformation().getMavenVersion();
        assertThat(buildTool.getVersion()).isEqualTo(mavenVersion);
        assertThat(buildTool.getType()).isEqualTo(BuildTool.Type.Maven);
    }


    private String getCurrentGitHash() {
        try {
            Repository repo = findRepo();
            return repo.findRef("HEAD").getTarget().getObjectId().getName();
        } catch (IOException e) {
            throw new RuntimeException("Could not find reference to HEAD in given repo %s".formatted(findRepo().getDirectory().toString()));
        }
    }

    private String getCurrentGitOrigin() {
        Repository repo = findRepo();
        return repo.getConfig().getString("remote", "origin", "url");
    }

    private static String getCurrentGitBranchName() {
        try {
            Repository repo = findRepo();
            String branch = null;
            branch = repo.getBranch();
            return branch;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long countGetters(Object marker) {
        return getGetter(marker)
                .count();
    }

    @NotNull
    private static Stream<Method> getGetter(Object marker) {
        return Arrays
                .stream(marker.getClass().getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                //                .filter(m -> m.getParameterCount() == 0)
                .filter(m -> m.getName().startsWith("get"));
    }

    private static Repository findRepo() {
        try {
            FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
            repositoryBuilder.addCeilingDirectory(Path.of("../..").toAbsolutePath().toFile());
            FileRepositoryBuilder gitDir = repositoryBuilder.findGitDir(Path.of(".").toAbsolutePath().toFile());
            Repository repo = null;
            repo = Git.open(gitDir.getGitDir()).status().getRepository();
            return repo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T findMarker(Map<Path, List<Marker>> markedResources, Resource pom, Class<T> markerClass) {
        return markedResources.get(ResourceUtil.getPath(pom)).stream()
                .filter(markerClass::isInstance)
                .map(markerClass::cast)
                .findFirst()
                .get();
    }

    public static class MyLogger implements Log {

        @Override
        public boolean isDebugEnabled() {
            return false;
        }

        @Override
        public void debug(CharSequence charSequence) {

        }

        @Override
        public void debug(CharSequence charSequence, Throwable throwable) {

        }

        @Override
        public void debug(Throwable throwable) {

        }

        @Override
        public boolean isInfoEnabled() {
            return false;
        }

        @Override
        public void info(CharSequence charSequence) {

        }

        @Override
        public void info(CharSequence charSequence, Throwable throwable) {

        }

        @Override
        public void info(Throwable throwable) {

        }

        @Override
        public boolean isWarnEnabled() {
            return false;
        }

        @Override
        public void warn(CharSequence charSequence) {

        }

        @Override
        public void warn(CharSequence charSequence, Throwable throwable) {

        }

        @Override
        public void warn(Throwable throwable) {

        }

        @Override
        public boolean isErrorEnabled() {
            return false;
        }

        @Override
        public void error(CharSequence charSequence) {

        }

        @Override
        public void error(CharSequence charSequence, Throwable throwable) {

        }

        @Override
        public void error(Throwable throwable) {

        }
    }
}