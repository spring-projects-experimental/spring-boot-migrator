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
package org.springframework.sbm.project.parser;

import org.openrewrite.SourceFile;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.marker.BuildTool;
import org.openrewrite.marker.GitProvenance;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.xml.tree.Xml;
import org.springframework.rewrite.project.resource.RewriteSourceFileHolder;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceVerifierTestHelper {

    private final Path resourcePath;
    private Class<? extends SourceFile> wrappedType;
    private List<MarkerVerifier> markerVerifer;

    public ResourceVerifierTestHelper(String resourcePathString) {
        this.resourcePath = Path.of(resourcePathString).toAbsolutePath();
    }

    public static ResourceVerifierTestHelper verifyResource(String resourcePath) {
        return new ResourceVerifierTestHelper(resourcePath);
    }

    public ResourceVerifierTestHelper wrapsInstanceOf(Class wrappedType) {
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

    public static MarkerVerifier mavenResolutionResult(String parentPomCoordinate, String coordinate, List<String> modules, Map<? extends Scope, ? extends List<String>> dependencies) {
        return new MavenResolutionResultMarkerVerifier(parentPomCoordinate, coordinate, modules, dependencies);
    }

    public static MarkerVerifier modulesMarker(String... modules) {
        return new ModulesMarkerVerifier(modules);
    }

    public static MarkerVerifier javaVersionMarker(String versionPattern, String source, String target) {
        return new JavaVersionMarkerVerifier(versionPattern, source, target);
    }

    public static MarkerVerifier javaProjectMarker(String projectName, String publication) {
        return new JavaProjectMarkerVerifier(projectName, publication);
    }

    public static MarkerVerifier javaSourceSetMarker(String name, String classpath) {
        return new JavaSourceSetMarkersVerifier(name, classpath);
    }

    public static MarkerVerifier javaSourceSetMarker(String name, int numTypes) {
        return new JavaSourceSetMarkersVerifier(name, numTypes);
    }

    public static MarkerVerifier gitProvenanceMarker(String branch) {
        return new GitProvenanceMarkerVerifier(branch);
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

        this.markerVerifer.forEach(v -> v.check(rewriteSourceFileHolder));

        List<Marker> markers = rewriteSourceFileHolder.getSourceFile().getMarkers().getMarkers();
        int size = markerVerifer.size();
        int actualSize = markers.size();
        assertThat(markers.size())
                .as(() ->
                {
                    String format = String.format("Invalid number of markers for resource '%s'. Expected '%d' but found '%d', '%s'", rewriteSourceFileHolder.getAbsolutePath().toString(), size, actualSize, markers.stream().map(m -> m.getClass().getName()).collect(Collectors.joining("', \n'")));
                    return format;
                })
                .isSameAs(markerVerifer.size());
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


    static class BuildToolMarkerVerifier implements MarkerVerifier<SourceFile, BuildTool> {

        private final String name;
        private final String version;

        public BuildToolMarkerVerifier(String name, String version) {
            this.name = name;
            this.version = version;
        }

        @Override
        public void check(RewriteSourceFileHolder rewriteSourceFileHolder) {
            BuildTool buildToolMarker = getFirstMarker(rewriteSourceFileHolder, BuildTool.class);
            assertMarker(rewriteSourceFileHolder.getSourceFile(), buildToolMarker);
        }

        @Override
        public void assertMarker(SourceFile sourceFile, BuildTool marker) {
            assertThat(marker.getType().name())
                    .as("Invalid marker [BuildTool] for resource '%s'. Expected name to be '%s' but was '%s'", sourceFile.getSourcePath().toString(), name, marker.getType().name())
                    .isEqualTo(name);

            assertThat(marker.getVersion())
                    .as("Invalid marker [BuildTool] for resource '%s'. Expected version to be '%s' but was '%s'", sourceFile.getSourcePath().toString(), version, marker.getVersion())
                    .isEqualTo(version);
        }

        @Override
        public void assertMarkers(SourceFile rewriteSourceFileHolder, List<BuildTool> markers) {
            throw new UnsupportedOperationException();
        }
    }

    private static class JavaVersionMarkerVerifier implements MarkerVerifier<SourceFile, JavaVersion> {
        private final int version;
        private final String source;
        private final String target;

        public JavaVersionMarkerVerifier(String version, String source, String target) {
            this.version = getBaseVersion(version);
            this.source = source;
            this.target = target;
        }

        private int getBaseVersion(String version) {
            String baseVersion = version.contains(".") ? version.split("\\.")[0] : version;
            return Integer.parseInt(baseVersion);
        }

        @Override
        public void check(RewriteSourceFileHolder rewriteSourceFileHolder) {
            JavaVersion javaVersion = getFirstMarker(rewriteSourceFileHolder, JavaVersion.class);

            assertMarker(rewriteSourceFileHolder.getSourceFile(), javaVersion);
        }

        @Override
        public void assertMarker(SourceFile sourceFile, JavaVersion marker) {
            assertThat(marker.getCreatedBy())
                    .as("Invalid marker [JavaVersion] for resource '%s'. Expected targetCompatibility to be '%s' but was '%s'", sourceFile.getSourcePath().toString(), version, marker.getSourceCompatibility())
                    .startsWith(Integer.toString(version));

            assertThat(marker.getSourceCompatibility())
                    .as("Invalid marker [JavaVersion] for resource '%s'. Expected sourceCompatibility to be '%s' but was '%s'", sourceFile.getSourcePath().toString(), source, marker.getSourceCompatibility())
                    .isEqualTo(source);

            assertThat(marker.getTargetCompatibility())
                    .as("Invalid marker [JavaVersion] for resource '%s'. Expected targetCompatibility to be '%s' but was '%s'", sourceFile.getSourcePath().toString(), target, marker.getSourceCompatibility())
                    .isEqualTo(target);
        }


        @Override
        public void assertMarkers(SourceFile rewriteSourceFileHolder, List<JavaVersion> markers) {
            throw new UnsupportedOperationException();
        }
    }

    private static class JavaProjectMarkerVerifier implements MarkerVerifier<SourceFile, JavaProject> {
        private final String projectName;
        private final String publication;

        public JavaProjectMarkerVerifier(String projectName, String publication) {
            this.projectName = projectName;
            this.publication = publication;
        }

        @Override
        public void check(RewriteSourceFileHolder rewriteSourceFileHolder) {
            JavaProject javaProjectMarker = getFirstMarker(rewriteSourceFileHolder, JavaProject.class);
            assertMarker(rewriteSourceFileHolder.getSourceFile(), javaProjectMarker);
        }

        @Override
        public void assertMarker(SourceFile sourceFile, JavaProject marker) {
            assertThat(marker.getProjectName()).isEqualTo(projectName);
            assertThat(marker.getPublication().getGroupId() + ":" + marker.getPublication().getArtifactId() + ":" + marker.getPublication().getVersion()).isEqualTo(publication);
        }

        @Override
        public void assertMarkers(SourceFile rewriteSourceFileHolder, List<JavaProject> markers) {
            throw new UnsupportedOperationException();
        }
    }

    private static class JavaSourceSetMarkersVerifier implements MarkerVerifier<SourceFile, JavaSourceSet> {
        private final String name;
        private final String classpath;
        private final Integer numTypes;

        public JavaSourceSetMarkersVerifier(String name, String classpathPattern) {
            this.name = name;
            this.numTypes = null;
            this.classpath = classpathPattern;
        }

        public JavaSourceSetMarkersVerifier(String name, int numTypes) {
            this.name = name;
            this.numTypes = numTypes;
            this.classpath = null;
        }

        @Override
        public void check(RewriteSourceFileHolder rewriteSourceFileHolder) {
            List<JavaSourceSet> javaSourceSetMarker = getMarkers(rewriteSourceFileHolder, JavaSourceSet.class);
            assertMarkers(rewriteSourceFileHolder.getSourceFile(), javaSourceSetMarker);
        }

        @Override
        public void assertMarker(SourceFile sourceFile, JavaSourceSet marker) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void assertMarkers(SourceFile rewriteSourceFileHolder, List<JavaSourceSet> javaSourceSetMarker) {
            assertThat(javaSourceSetMarker).filteredOn(js -> name.equals(js.getName()))
                    .as("Invalid marker [JavaSourceSet] for resource '%s'. Expected name to be '%s' but no Marker with this name was found.", rewriteSourceFileHolder.getSourcePath().toString(), name)
                    .isNotEmpty();

            List<String> dependencies = javaSourceSetMarker.stream().filter(js -> name.equals(js.getName()))
                    .flatMap(js -> js.getClasspath().stream())
                    .map(fq -> fq.getFullyQualifiedName())
                    .collect(Collectors.toList());

            if(classpath != null && !classpath.isEmpty()) {
                String[] split = classpath.split(", ");
                assertThat(dependencies)
                        .as("Invalid marker [JavaSourceSet] for resource '%s'. Expected dependencies to be '%s' but was '%s'", rewriteSourceFileHolder.getSourcePath().toString(), classpath, dependencies)
                        .contains(split);
            } else if(numTypes != null) {
                assertThat(dependencies)
                        .as("Invalid marker [JavaSourceSet] for resource '%s'. Expected dependencies to be of size '%d' but was '%d'", rewriteSourceFileHolder.getSourcePath().toString(), numTypes, dependencies.size())
                        .hasSize(numTypes);
            }

        }

    }

    private static class GitProvenanceMarkerVerifier implements MarkerVerifier<SourceFile, GitProvenance> {
        private final String branch;

        public GitProvenanceMarkerVerifier(String branch) {
            this.branch = branch;
        }

        @Override
        public void check(RewriteSourceFileHolder<SourceFile> rewriteSourceFileHolder) {
            GitProvenance gitProvenanceMarker = getFirstMarker(rewriteSourceFileHolder, GitProvenance.class);
            assertMarker(rewriteSourceFileHolder.getSourceFile(), gitProvenanceMarker);
        }

        @Override
        public void assertMarker(SourceFile sourceFile, GitProvenance marker) {
            assertThat(marker.getBranch())
                    .as("Invalid marker [GitProvenance] for resource '%s'. Expected branch to be '%s' but was '%s'", sourceFile.getSourcePath().toString(), branch, marker.getBranch())
                    .isEqualTo(branch);
        }

        @Override
        public void assertMarkers(SourceFile rewriteSourceFileHolder, List<GitProvenance> markers) {
            throw new UnsupportedOperationException();
        }
    }

    private static class MavenResolutionResultMarkerVerifier implements MarkerVerifier<Xml.Document, MavenResolutionResult> {
        private String parentPomCoordinate;
        private final String coordinate;
        private List<String> modules;
        private Map<? extends Scope, ? extends List<String>> dependencies;

        public MavenResolutionResultMarkerVerifier(String parentPomCoordinate, String coordinate, List<String> modules, Map<? extends Scope, ? extends List<String>> dependencies) {
            this.parentPomCoordinate = parentPomCoordinate;
            this.coordinate = coordinate;
            this.modules = modules;
            this.dependencies = dependencies;
        }

        @Override
        public void check(RewriteSourceFileHolder<Xml.Document> rewriteSourceFileHolder) {
            MavenResolutionResult mavenModel = rewriteSourceFileHolder.getSourceFile().getMarkers().findFirst(MavenResolutionResult.class).get();
            assertMarker(rewriteSourceFileHolder.getSourceFile(), mavenModel);
        }

        @Override
        public void assertMarker(Xml.Document mavenModel, MavenResolutionResult marker) {
            String coordinate = marker.getPom().getGroupId() + ":" + marker.getPom().getArtifactId() + ":" + marker.getPom().getVersion();
            if(parentPomCoordinate == null) {
                assertThat(marker.getParent()).isNull();
            } else {
                assertThat(marker.getParent().getPom().getGav().toString()).isEqualTo(parentPomCoordinate);
            }

            assertThat(marker.getModules().stream().map(m -> m.getPom().getGav().toString()).collect(Collectors.toList())).containsExactlyInAnyOrder(modules.toArray(new String[]{}));
            Map<Scope, List<String>> dependenciesGav = marker.getDependencies().entrySet().stream()
                    .collect(Collectors.toMap(
                                    entry -> entry.getKey(),
                                    entry -> entry.getValue().stream()
                                            .map(resolvedDependency -> resolvedDependency.getGav().toString())
                                            .collect(Collectors.toList())
                            )
                    );

            assertThat(dependenciesGav).containsExactlyInAnyOrderEntriesOf(this.dependencies);
            assertThat(coordinate).isEqualTo(this.coordinate);
        }

        @Override
        public void assertMarkers(Xml.Document rewriteSourceFileHolder, List<MavenResolutionResult> markers) {
            throw new UnsupportedOperationException();
        }

    }

    private static class ModulesMarkerVerifier implements MarkerVerifier<Xml.Document, MavenResolutionResult> {
        private final String[] modules;

        public ModulesMarkerVerifier(String... modules) {
            this.modules = modules;
        }

        @Override
        public void check(RewriteSourceFileHolder<Xml.Document> rewriteSourceFileHolder) {
            MavenResolutionResult marker = getFirstMarker(rewriteSourceFileHolder, MavenResolutionResult.class);
            assertMarker(rewriteSourceFileHolder.getSourceFile(), marker);
        }

        @Override
        public void assertMarker(Xml.Document sourceFile, MavenResolutionResult marker) {
            List<String> modulesList = marker.getModules().stream().map(m -> m.getPom().getGav().toString()).collect(Collectors.toList());

            assertThat(modulesList)
                    .as("Invalid marker [Modules] for resource '%s'. Expected modules to be '%s' but was '%s'", sourceFile.getSourcePath().toString(), modules, modulesList)
                    .containsExactlyInAnyOrder(modules);
        }

        @Override
        public void assertMarkers(Xml.Document rewriteSourceFileHolder, List<MavenResolutionResult> markers) {
            throw new UnsupportedOperationException();
        }
    }
}

interface MarkerVerifier<S extends SourceFile, M extends Marker> {
    void check(RewriteSourceFileHolder<S> rewriteSourceFileHolder);
    void assertMarker(S sourceFile, M marker);

    void assertMarkers(S rewriteSourceFileHolder, List<M> markers);
}
