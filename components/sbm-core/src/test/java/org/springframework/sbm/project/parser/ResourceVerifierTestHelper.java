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

import org.openrewrite.SourceFile;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.marker.BuildTool;
import org.openrewrite.marker.GitProvenance;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    public static MarkerVerifier mavenResolutionResult(String parentPomCoordinate, String coordinate, List<String> modules, Map<? extends Scope, ? extends List<ResolvedDependency>> dependencies) {
        return new MavenResolutionResultMarkerVerifier(parentPomCoordinate, coordinate, modules, dependencies);
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


    private static <T extends Marker> T getFirstMarker(RewriteSourceFileHolder r1, Class<T> markerClass) {
        return r1.getSourceFile().getMarkers().findFirst(markerClass).orElseThrow(() -> new RuntimeException("Could not find marker '" + markerClass + "' on '" + r1.getAbsolutePath() + "'"));
    }

    private static <T extends Marker> List<T> getMarkers(RewriteSourceFileHolder r1, Class<T> markerClass) {
        return r1.getSourceFile().getMarkers().getMarkers().stream()
                .filter(m -> markerClass.isInstance(m))
                .map(markerClass::cast)
                .collect(Collectors.toList());
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
            BuildTool buildToolMarker = getFirstMarker(rewriteSourceFileHolder, BuildTool.class);

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
            JavaVersion javaVersion = getFirstMarker(rewriteSourceFileHolder, JavaVersion.class);

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
            JavaProject javaProjectMarker = getFirstMarker(rewriteSourceFileHolder, JavaProject.class);
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
            List<JavaSourceSet> javaSourceSetMarker = getMarkers(rewriteSourceFileHolder, JavaSourceSet.class);

            assertThat(javaSourceSetMarker).filteredOn(js -> name.equals(js.getName()))
                    .as("Invalid marker [JavaSourceSet] for resource '%s'. Expected name to be '%s' but no Marker with this name was found.", rewriteSourceFileHolder, name)
                    .isNotEmpty();

            List<String> dependencies = javaSourceSetMarker.stream().filter(js -> "main".equals(js.getName()))
                    .flatMap(js -> js.getClasspath().stream())
                    .map(fq -> fq.getFullyQualifiedName())
                    .collect(Collectors.toList());

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
            GitProvenance gitProvenanceMarker = getFirstMarker(rewriteSourceFileHolder, GitProvenance.class);

            assertThat(gitProvenanceMarker.getBranch())
                    .as("Invalid marker [GitProvenance] for resource '%s'. Expected branch to be '%s' but was '%s'", rewriteSourceFileHolder, branch, gitProvenanceMarker.getBranch())
                    .isEqualTo(branch);
        }
    }

    private static class MavenResolutionResultMarkerVerifier extends MarkerVerifier {
        private String parentPomCoordinate;
        private final String coordinate;
        private List<String> modules;
        private Map<? extends Scope, ? extends List<ResolvedDependency>> dependencies;

        public MavenResolutionResultMarkerVerifier(String parentPomCoordinate, String coordinate, List<String> modules, Map<? extends Scope, ? extends List<ResolvedDependency>> dependencies) {
            this.parentPomCoordinate = parentPomCoordinate;
            this.coordinate = coordinate;
            this.modules = modules;
            this.dependencies = dependencies;
        }

        @Override
        public void verify(RewriteSourceFileHolder rewriteSourceFileHolder) {
            MavenResolutionResult mavenModel = rewriteSourceFileHolder.getSourceFile().getMarkers().findFirst(MavenResolutionResult.class).get();
            String coordinate = mavenModel.getPom().getGroupId() + ":" + mavenModel.getPom().getArtifactId() + ":" + mavenModel.getPom().getVersion();
            if(parentPomCoordinate == null) {
                assertThat(mavenModel.getParent()).isNull();
            } else {
                assertThat(mavenModel.getParent().getPom().getGav().toString()).isEqualTo(parentPomCoordinate);
            }

            assertThat(mavenModel.getModules().stream().map(m -> m.getPom().getGav().toString()).collect(Collectors.toList())).containsExactlyInAnyOrder(modules.toArray(new String[]{}));
            assertThat(mavenModel.getDependencies()).containsExactlyInAnyOrderEntriesOf(dependencies);
            assertThat(coordinate).isEqualTo(coordinate);
        }
    }

    private static class ModulesMarkerVerifier extends MarkerVerifier {
        private final String[] modules;

        public ModulesMarkerVerifier(String... modules) {
            this.modules = modules;
        }

        @Override
        public void verify(RewriteSourceFileHolder rewriteSourceFileHolder) {
            MavenResolutionResult marker = getFirstMarker(rewriteSourceFileHolder, MavenResolutionResult.class);
            List<String> modulesList = marker.getModules().stream().map(m -> m.getPom().getGav().toString()).collect(Collectors.toList());

            assertThat(modulesList)
                    .as("Invalid marker [Modules] for resource '%s'. Expected modules to be '%s' but was '%s'", rewriteSourceFileHolder, modules, modulesList)
                    .containsExactlyInAnyOrder(modules);
        }
    }
}

abstract class MarkerVerifier {
    public abstract void verify(RewriteSourceFileHolder rewriteSourceFileHolder);
}
