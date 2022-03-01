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
package org.springframework.sbm.project.parser;

import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.openrewrite.SourceFile;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.marker.BuildTool;
import org.openrewrite.marker.GitProvenance;
import org.openrewrite.marker.Marker;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceVerifierTestHelper {

    private final Path resourcePath;
    private Class wrappedType;
    private List<MarkerVerifier> markerVerifer;

    public ResourceVerifierTestHelper(String resourcePathString) {
        this.resourcePath = Path.of(resourcePathString).toAbsolutePath();
    }

    public static ResourceVerifierTestHelper verifyResource(String resourcePath) {
        return new ResourceVerifierTestHelper(resourcePath);
    }

    public ResourceVerifierTestHelper wrappedInstanceOf(Class wrappedType) {
        this.wrappedType = wrappedType;
        return this;
    }

    public ResourceVerifierTestHelper havingMarkers(MarkerVerifier... markerVerifer) {
        this.markerVerifer = Arrays.asList(markerVerifer);

        return this;
    }

    public static MarkerVerifier buildToolMarker(String name, String version) {
        return new BuildToolMarkerVerifier(name, version);
    }

    public static MarkerVerifier mavenModelMarker(String coordinate) {
        return new MavenModelMarkerVerifier(coordinate);
    }

    public static MarkerVerifier modulesMarker(String... modules) {
        return new ModulesMarkerVerifier(modules);
    }

    public static MarkerVerifier javaVersionMarker(int versionPattern, String source, String target) {
        return new JavaVersionMarkerVerifier(versionPattern, source, target);
    }

    public static MarkerVerifier javaProjectMarker(String projectName, String publication) {
        return new JavaProjectMarkerVerifier(projectName, publication);
    }

    public static MarkerVerifier javaSourceSetMarker(String name, String classpath) {
        return new JavaSourceSetMarkerVerifier(name, classpath);
    }

    public static MarkerVerifier gitProvenanceMarker(String branch) {
        return new GitProvenanceMarkerverifier(branch);
    }

    private RewriteSourceFileHolder findByPath(List<RewriteSourceFileHolder<? extends SourceFile>> projectResources, String toAbsolutePath) {
        Optional<RewriteSourceFileHolder<? extends SourceFile>> matchingResource = projectResources.stream().filter(r -> r.getAbsolutePath().equals(Path.of(toAbsolutePath).toAbsolutePath())).findFirst();
        assertThat(matchingResource)
                .as("Resource '%s' could not be found", toAbsolutePath)
                .isNotEmpty();
        return matchingResource.get();
    }

    void isContainedIn(List<RewriteSourceFileHolder<? extends SourceFile>> projectResources) {
        Optional<RewriteSourceFileHolder<? extends SourceFile>> matchingResource = projectResources.stream().filter(r -> r.getAbsolutePath().equals(resourcePath)).findFirst();
        assertThat(matchingResource)
                .as("Resource '%s' could not be found", resourcePath)
                .isNotEmpty();

        RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder = matchingResource.get();

        assertThat(rewriteSourceFileHolder.getSourceFile().getClass()).isInstanceOf(wrappedType.getClass());

        this.markerVerifer.forEach(v -> v.verify(rewriteSourceFileHolder));

        assertThat(rewriteSourceFileHolder.getSourceFile().getMarkers().getMarkers())
                .as("Invalid number of markers for resource '%s'. Expected '%s' but found '%s'", rewriteSourceFileHolder, markerVerifer.size(), rewriteSourceFileHolder.getSourceFile().getMarkers().getMarkers().size())
                .hasSize(markerVerifer.size());
    }


    private static <T extends Marker> T getMarker(RewriteSourceFileHolder r1, Class<T> markerClass) {
        return r1.getSourceFile().getMarkers().findFirst(markerClass).orElseThrow(() -> new RuntimeException("Could not find marker '" + markerClass + "' on '" + r1.getAbsolutePath() + "'"));
    }


    static class BuildToolMarkerVerifier extends MarkerVerifier {

        private final String name;
        private final String version;

        public BuildToolMarkerVerifier(String name, String version) {
            this.name = name;
            this.version = version;
        }

        @Override
        public void verify(RewriteSourceFileHolder rewriteSourceFileHolder) {
            BuildTool buildToolMarker = getMarker(rewriteSourceFileHolder, BuildTool.class);

            assertThat(buildToolMarker.getType().name())
                    .as("Invalid marker [BuildTool] for resource '%s'. Expected name to be '%s' but was '%s'", rewriteSourceFileHolder, name, buildToolMarker.getType().name())
                    .isEqualTo(name);

            assertThat(buildToolMarker.getVersion())
                    .as("Invalid marker [BuildTool] for resource '%s'. Expected version to be '%s' but was '%s'", rewriteSourceFileHolder, version, buildToolMarker.getVersion())
                    .isEqualTo(version);
        }
    }

    private static class JavaVersionMarkerVerifier extends MarkerVerifier {
        private final int version;
        private final String source;
        private final String target;

        public JavaVersionMarkerVerifier(int version, String source, String target) {
            this.version = version;
            this.source = source;
            this.target = target;
        }

        @Override
        public void verify(RewriteSourceFileHolder rewriteSourceFileHolder) {
            JavaVersion javaVersion = getMarker(rewriteSourceFileHolder, JavaVersion.class);

            assertThat(javaVersion.getCreatedBy())
                    .as("Invalid marker [JavaVersion] for resource '%s'. Expected targetCompatibility to be '%s' but was '%s'", rewriteSourceFileHolder, version, javaVersion.getSourceCompatibility())
                    .startsWith(Integer.toString(version));

            assertThat(javaVersion.getSourceCompatibility())
                    .as("Invalid marker [JavaVersion] for resource '%s'. Expected sourceCompatibility to be '%s' but was '%s'", rewriteSourceFileHolder, source, javaVersion.getSourceCompatibility())
                    .isEqualTo(source);

            assertThat(javaVersion.getTargetCompatibility())
                    .as("Invalid marker [JavaVersion] for resource '%s'. Expected targetCompatibility to be '%s' but was '%s'", rewriteSourceFileHolder, target, javaVersion.getSourceCompatibility())
                    .isEqualTo(target);
        }
    }

    private static class JavaProjectMarkerVerifier extends MarkerVerifier {
        private final String projectName;
        private final String publication;

        public JavaProjectMarkerVerifier(String projectName, String publication) {
            this.projectName = projectName;
            this.publication = publication;
        }

        @Override
        public void verify(RewriteSourceFileHolder rewriteSourceFileHolder) {
            JavaProject javaProjectMarker = getMarker(rewriteSourceFileHolder, JavaProject.class);
            assertThat(javaProjectMarker.getProjectName()).isEqualTo(projectName);
            assertThat(javaProjectMarker.getPublication().getGroupId() + ":" + javaProjectMarker.getPublication().getArtifactId() + ":" + javaProjectMarker.getPublication().getVersion()).isEqualTo(publication);
        }
    }

    private static class JavaSourceSetMarkerVerifier extends MarkerVerifier {
        private final String name;
        private final String classpath;

        public JavaSourceSetMarkerVerifier(String name, String classpathPattern) {
            this.name = name;
            this.classpath = classpathPattern;
        }

        @Override
        public void verify(RewriteSourceFileHolder rewriteSourceFileHolder) {
            JavaSourceSet javaSourceSetMarker = getMarker(rewriteSourceFileHolder, JavaSourceSet.class);

            assertThat(javaSourceSetMarker.getName())
                    .as("Invalid marker [JavaSourceSet] for resource '%s'. Expected name to be '%s' but was '%s'", rewriteSourceFileHolder, name, javaSourceSetMarker.getName())
                    .isEqualTo(name);

            List<String> dependencies = javaSourceSetMarker.getClasspath().stream().map(fq -> fq.getFullyQualifiedName()).collect(Collectors.toList());

            String[] split = classpath.split(", ");
            if (classpath.equals("")) {
                dependencies.add("");
            }

            assertThat(dependencies)
                    .as("Invalid marker [JavaSourceSet] for resource '%s'. Expected dependencies to be '%s' but was '%s'", rewriteSourceFileHolder, classpath, dependencies)
                    .contains(split);
        }
    }

    private static class GitProvenanceMarkerverifier extends MarkerVerifier {
        private final String branch;

        public GitProvenanceMarkerverifier(String branch) {
            this.branch = branch;
        }

        @Override
        public void verify(RewriteSourceFileHolder rewriteSourceFileHolder) {
            GitProvenance gitProvenanceMarker = getMarker(rewriteSourceFileHolder, GitProvenance.class);

            assertThat(gitProvenanceMarker.getBranch())
                    .as("Invalid marker [GitProvenance] for resource '%s'. Expected branch to be '%s' but was '%s'", rewriteSourceFileHolder, branch, gitProvenanceMarker.getBranch())
                    .isEqualTo(branch);
        }
    }

    private static class MavenModelMarkerVerifier extends MarkerVerifier {
        private final String coordinate;

        public MavenModelMarkerVerifier(String coordinate) {
            this.coordinate = coordinate;
        }

        @Override
        public void verify(RewriteSourceFileHolder rewriteSourceFileHolder) {
//            MavenModel mavenModel = getMarker(rewriteSourceFileHolder, MavenModel.class);
//            String coordinate = mavenModel.getPom().getGroupId() + ":" + mavenModel.getPom().getArtifactId() + ":" + mavenModel.getPom().getVersion();
            assertThat(coordinate).isEqualTo(""); //coordinate);
        }
    }

    private static class ModulesMarkerVerifier extends MarkerVerifier {
        private final String[] modules;

        public ModulesMarkerVerifier(String... modules) {
            this.modules = modules;
        }

        @Override
        public void verify(RewriteSourceFileHolder rewriteSourceFileHolder) {
//            Modules modulesMarker = getMarker(rewriteSourceFileHolder, Modules.class);
            List<String> modulesList = List.of(); //modulesMarker.getModules().stream().map(m -> m.getGroupId() + ":" + m.getArtifactId() + ":" + m.getVersion()).collect(Collectors.toList());

            assertThat(modulesList)
                    .as("Invalid marker [Modules] for resource '%s'. Expected modules to be '%s' but was '%s'", rewriteSourceFileHolder, modules, modulesList)
                    .containsExactlyInAnyOrder(modules);
        }
    }
}

abstract class MarkerVerifier {
    public abstract void verify(RewriteSourceFileHolder rewriteSourceFileHolder);
}
