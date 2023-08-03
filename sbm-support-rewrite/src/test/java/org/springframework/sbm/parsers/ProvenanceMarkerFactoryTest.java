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
import org.apache.maven.project.MavenProject;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.apache.maven.rtinfo.internal.DefaultRuntimeInformation;
import org.apache.maven.settings.crypto.SettingsDecrypter;
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
import org.openrewrite.maven.MavenMojoProjectParser;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Fabian Krüger
 */
class ProvenanceMarkerFactoryTest {

    /**
     * Tests the MavenMojoProjectParser to verify assumptions.
     */
    @Nested
    public class MavenMojoProjectParserTest {
        @Test
        @DisplayName("test MavenMojoProjectParser.generateProvenance")
        void testMavenMojoProjectParserGenerateProvenance() {
            // the project for which the markers will be created
            Path baseDir = Path.of("./testcode/maven-projects/simple-spring-boot").toAbsolutePath().normalize();

            // create sut using a factory
            RuntimeInformation runtimeInformation = new DefaultRuntimeInformation();
            SettingsDecrypter settingsDecrypter = null;
            MavenMojoProjectParserFactory mavenMojoProjectParserFactory = new MavenMojoProjectParserFactory(new ParserSettings());
            MavenMojoProjectParser sut = mavenMojoProjectParserFactory.create(baseDir, runtimeInformation, settingsDecrypter);

            // the sut requires a MavenProject, let's retrieve it from Maven
            MavenExecutor mavenExecutor = new MavenExecutor(new MavenExecutionRequestFactory(new MavenConfigFileParser()), new MavenPlexusContainer());

            // doing a 'mvn clean install'
            mavenExecutor.onProjectSucceededEvent(baseDir, List.of("clean", "package"), event -> {

                // and then use the MavenProject from the MavenSession
                MavenProject mavenModel = event.getSession().getCurrentProject();

                // to call the sut
                List<Marker> markers = sut.generateProvenance(mavenModel);

                // and assert markers
                assertThat(markers).hasSize(5);
                JavaVersion jv = findMarker(markers, JavaVersion.class);
                assertThat(countGetters(jv)).isEqualTo(7);
                assertThat(jv.getCreatedBy()).isEqualTo(System.getProperty("java.specification.version"));
//            assertThat(jv.getMajorVersion()).isEqualTo(Integer.parseInt(System.getProperty("java.specification.version")));
                assertThat(jv.getMajorVersion()).isEqualTo(18);
                assertThat(jv.getSourceCompatibility()).isEqualTo("18");
                assertThat(jv.getTargetCompatibility()).isEqualTo("17");
                assertThat(jv.getMajorReleaseVersion()).isEqualTo(17);
                assertThat(jv.getVmVendor()).isEqualTo(System.getProperty("java.vm.vendor"));
                assertThat(jv.getId()).isInstanceOf(UUID.class);

                JavaProject jp = findMarker(markers, JavaProject.class);
                assertThat(countGetters(jp)).isEqualTo(3);
                assertThat(jp.getId()).isInstanceOf(UUID.class);
                assertThat(jp.getProjectName()).isEqualTo("simple-spring-boot-project");
                JavaProject.Publication publication = jp.getPublication();
                assertThat(countGetters(publication)).isEqualTo(3);
                assertThat(publication.getGroupId()).isEqualTo("com.example");
                assertThat(publication.getArtifactId()).isEqualTo("simple-spring-boot");
                assertThat(publication.getVersion()).isEqualTo("0.0.1-SNAPSHOT");

                String branch = getCurrentGitBranchName();
                String origin = getCurrentGitOrigin();
                String gitHash = getCurrentGitHash();
                GitProvenance expectedGitProvenance = GitProvenance.fromProjectDirectory(baseDir, BuildEnvironment.build(System::getenv));
                GitProvenance gitProvenance = findMarker(markers, GitProvenance.class);
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

                OperatingSystemProvenance operatingSystemProvenance = findMarker(markers, OperatingSystemProvenance.class);
                OperatingSystemProvenance expected = OperatingSystemProvenance.current();
                assertThat(operatingSystemProvenance.getName()).isEqualTo(expected.getName());
                // ...

                BuildTool buildTool = findMarker(markers, BuildTool.class);
                assertThat(countGetters(buildTool)).isEqualTo(3);
                assertThat(buildTool.getId()).isInstanceOf(UUID.class);
                String mavenVersion = new DefaultRuntimeInformation().getMavenVersion();
                assertThat(buildTool.getVersion()).isEqualTo(mavenVersion);
                assertThat(buildTool.getType()).isEqualTo(BuildTool.Type.Maven);

            });
        }

        private <T extends Marker> T findMarker(List<Marker> markers, Class<T> markerClass) {
            return (T) markers.stream().filter(m -> markerClass.isAssignableFrom(m.getClass())).findFirst().orElseThrow();
        }
    }




    @Nested
    public class GivenSimpleMultiModuleProject {

        @Test
        @DisplayName("Should Create Provenance Markers")
        void shouldCreateProvenanceMarkers(@TempDir Path tempDir)  {
            Path baseDir = Path.of(".").toAbsolutePath().normalize();

            // The MavenMojoProjectParserFactory creates an instance of OpenRewrite's MavenMojoProjectParser
            // We provide a mock, there's a test for MavenMojoProjectParser
            MavenMojoProjectParserFactory parserFactory = mock(MavenMojoProjectParserFactory.class);
            MavenMojoProjectParser mojoProjectParser = mock(MavenMojoProjectParser.class);
            when(parserFactory.create(isA(Path.class), isA(DefaultRuntimeInformation.class), isNull())).thenReturn(mojoProjectParser);

            ProvenanceMarkerFactory sut = new ProvenanceMarkerFactory(parserFactory);


            SortedProjects sortedProjects = mock(SortedProjects.class);
            MavenProject mavenProject1 = mock(MavenProject.class);
            MavenProject mavenProject2 = mock(MavenProject.class);
            List<MavenProject> mavenProjects = List.of(
                    mavenProject1,
                    mavenProject2
            );
            // The provided TopologicallySortedProjects instance will
            // provide the sorted MavenProjects
            when(sortedProjects.getSortedProjects()).thenReturn(mavenProjects);

            // internally the Maven projects will be matched with the provided resources
            Path path1 = Path.of("some/path").toAbsolutePath().normalize();
            // path1 matches with mavenProject1
            when(sortedProjects.getMatchingBuildFileResource(mavenProject1)).thenReturn(new DummyResource(path1, ""));
            Path path2 = Path.of("some/other").toAbsolutePath().normalize();
            // path2 matches with mavenProject2
            when(sortedProjects.getMatchingBuildFileResource(mavenProject2)).thenReturn(new DummyResource(path2, ""));
            List<Marker> markers1 = List.of();
            List<Marker> markers2 = List.of();
            when(mojoProjectParser.generateProvenance(mavenProject1)).thenReturn(markers1);
            when(mojoProjectParser.generateProvenance(mavenProject2)).thenReturn(markers2);

            Map<Path, List<Marker>> resourceListMap = sut.generateProvenanceMarkers(baseDir, sortedProjects);

            assertThat(resourceListMap.get(path1)).isEqualTo(markers1);
            assertThat(resourceListMap.get(path2)).isEqualTo(markers2);
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