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

package org.springframework.sbm.maven;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.marker.SearchResult;
import org.openrewrite.maven.AddProperty;
import org.openrewrite.maven.ChangePropertyValue;
import org.openrewrite.maven.MavenIsoVisitor;
import org.openrewrite.maven.UpdateMavenModel;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.GroupArtifactVersion;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.ResolvedManagedDependency;
import org.openrewrite.maven.tree.ResolvedPom;
import org.openrewrite.xml.ChangeTagValueVisitor;
import org.openrewrite.xml.tree.Xml;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class UpgradeUnmanagedSpringProject extends Recipe {

    public static final String SPRINGBOOT_GROUP = "org.springframework.boot";
    public static final String SPRING_BOOT_STARTER_PARENT = "spring-boot-starter-parent";
    public static final String SPRING_BOOT_DEPENDENCIES = "spring-boot-dependencies";
    public static final String ARTIFACT_ID = "artifactId";
    private String springVersion;
    private Map<String, String> springBootDependenciesMap;

    private Pattern versionPattern;

    public UpgradeUnmanagedSpringProject() {
    }

    public UpgradeUnmanagedSpringProject(String springVersion, String minVersion) {

        this.springVersion = springVersion;
        this.versionPattern = Pattern.compile(minVersion);
    }

    public void setSpringVersion(String springVersion) {
        this.springVersion = springVersion;
    }

    public void setMinVersion(String minVersion) {
        this.versionPattern = Pattern.compile(minVersion);
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getApplicableTest() {
        return new MavenIsoVisitor<>() {
            private boolean validForFurtherReview = true;

            @Override
            public Xml.Document visitDocument(Xml.Document document, ExecutionContext executionContext) {

                new MavenIsoVisitor<Integer>() {
                    @Override
                    public Xml.Tag visitTag(Xml.Tag tag, Integer executionContext) {
                        if (isParentTag()) {

                            Optional<Xml.Tag> artifactId = tag.getChild(ARTIFACT_ID);

                            if (artifactId.isPresent()) {
                                Optional<String> artifactIdValue = artifactId.get().getValue();
                                if (artifactIdValue.isPresent()) {
                                    if (artifactIdValue.get().equals(SPRING_BOOT_STARTER_PARENT)) {
                                        validForFurtherReview = false;
                                    }
                                }
                            }
                        }
                        if (isManagedDependencyTag(SPRINGBOOT_GROUP, SPRING_BOOT_DEPENDENCIES)) {
                            validForFurtherReview = false;
                        }
                        return super.visitTag(tag, executionContext);
                    }
                }.visit(document, 0);

                return super.visitDocument(document, executionContext);
            }

            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
                Xml.Tag resultTag = super.visitTag(tag, executionContext);

                if (validForFurtherReview) {
                    if (isManagedDependencyTag()) {

                        ResolvedManagedDependency managedDependency = findManagedDependency(resultTag);

                        if ((managedDependency != null) && managedDependency.getGroupId().equals(SPRINGBOOT_GROUP)
                                && satisfiesMinVersion(managedDependency.getVersion())) {
                            return applyThisRecipe(resultTag);
                        }
                    }

                    if (isDependencyTag()) {
                        ResolvedDependency dependency = findDependency(resultTag);
                        if ((dependency != null) && dependency.getGroupId().equals(SPRINGBOOT_GROUP)
                                && satisfiesMinVersion(dependency.getVersion())) {
                            return applyThisRecipe(resultTag);
                        }
                    }
                }

                return resultTag;
            }

            @NotNull
            private Xml.Tag applyThisRecipe(Xml.Tag resultTag) {
                return resultTag.withMarkers(resultTag.getMarkers().addIfAbsent(new SearchResult(UUID.randomUUID(), "SpringBoot dependency")));
            }

            private boolean satisfiesMinVersion(String version) {
                return versionPattern.matcher(version).matches();
            }
        };
    }

    @Override
    public String getDisplayName() {
        return "Upgrade unmanaged spring project";
    }

    public synchronized Map<String, String> getDependenciesMap() {
        if (springBootDependenciesMap == null) {
            springBootDependenciesMap = buildDependencyMap();
        }
        return springBootDependenciesMap;
    }

    private Map<String, String> buildDependencyMap() {
        Map<Path, Pom> poms = new HashMap<>();
        MavenPomDownloader downloader = new MavenPomDownloader(poms, new InMemoryExecutionContext());
        GroupArtifactVersion gav = new GroupArtifactVersion(SPRINGBOOT_GROUP, SPRING_BOOT_DEPENDENCIES, springVersion);
        String relativePath = "";
        ResolvedPom containingPom = null;
        Pom pom = downloader.download(gav, relativePath, containingPom, List.of());
        ResolvedPom resolvedPom = pom.resolve(List.of(), downloader, new InMemoryExecutionContext());
        List<ResolvedManagedDependency> dependencyManagement = resolvedPom.getDependencyManagement();
        Map<String, String> dependencyMap = new HashMap<>();
        dependencyManagement
                .stream()
                .filter(d -> d.getVersion() != null)
                .forEach(d -> dependencyMap.put(d.getGroupId() + ":" + d.getArtifactId().toLowerCase(), d.getVersion()));
        return dependencyMap;
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new MavenIsoVisitor<>() {
            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {

                if (isManagedDependencyTag()) {
                    ResolvedManagedDependency managedDependency = findManagedDependency(tag);
                    String key = managedDependency.getGroupId() + ":" + managedDependency.getArtifactId();
                    mayBeUpdateVersion(key, tag);
                }
                if (isDependencyTag()) {
                    ResolvedDependency dependency = findDependency(tag);
                    if (dependency != null) {
                        String key = dependency.getGroupId() + ":" + dependency.getArtifactId();
                        mayBeUpdateVersion(key, tag);
                    }
                }
                return super.visitTag(tag, executionContext);
            }

            private void mayBeUpdateVersion(String key, Xml.Tag tag) {
                if (getDependenciesMap().containsKey(key)) {
                    String dependencyVersion = getDependenciesMap().get(key);
                    Optional<Xml.Tag> version = tag.getChild("version");
                    if (version.isEmpty() || version.get().getValue().isEmpty()) {
                        return;
                    }
                    String versionValue = version.get().getValue().get();
                    if (versionValue.startsWith("${")) {
                        String propertyName = versionValue.substring(2, versionValue.length() - 1);
                        version.ifPresent(xml -> doAfterVisit(new ChangePropertyValue(propertyName, dependencyVersion, true)));
                    } else {
                        version.ifPresent(xml -> doAfterVisit(new ChangeTagValueVisitor(xml, dependencyVersion)));
                    }
                    doAfterVisit(new UpdateMavenModel<>());
                }
            }
        };
    }
}
