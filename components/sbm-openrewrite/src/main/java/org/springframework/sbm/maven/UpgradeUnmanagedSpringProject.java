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

import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.marker.SearchResult;
import org.openrewrite.maven.MavenIsoVisitor;
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
    private final String springVersion;
    private final String minVersion;
    private Map<String, String> springBootDependenciesMap;

    private final Pattern versionPattern;

    public UpgradeUnmanagedSpringProject(String springVersion, String minVersion) {

        this.springVersion = springVersion;
        this.minVersion = minVersion;
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

                            Optional<Xml.Tag> artifactId = tag.getChild("artifactId");

                            if (artifactId.isPresent()) {
                                Optional<String> artifactIdValue = artifactId.get().getValue();
                                if (artifactIdValue.isPresent()) {
                                    if (artifactIdValue.get().equals("spring-boot-starter-parent")) {
                                        validForFurtherReview = false;
                                    }
                                }
                            }
                        }
                        return super.visitTag(tag, executionContext);
                    }
                }.visit(document, 0);

                return super.visitDocument(document, executionContext);
            }

            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
                Xml.Tag resultTag = super.visitTag(tag, executionContext);

                if (isDependencyTag() && validForFurtherReview) {
                    ResolvedDependency dependency = findDependency(resultTag);
                    if (dependency.getGroupId().equals(SPRINGBOOT_GROUP)
                        && satisfiesMinVersion(dependency.getVersion())) {
                        return resultTag.withMarkers(resultTag.getMarkers().addIfAbsent(new SearchResult(UUID.randomUUID(), "SpringBoot dependency")));
                    }
                }
                return resultTag;
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

    private Map<String, String> buildDependencyMap () {
        Map<Path, Pom> poms = new HashMap<>();
        MavenPomDownloader downloader = new MavenPomDownloader(poms, new InMemoryExecutionContext());
        GroupArtifactVersion gav = new GroupArtifactVersion("org.springframework.boot", "spring-boot-dependencies", springVersion);
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

                if (isDependencyTag()) {
                    ResolvedDependency dependency = findDependency(tag);

                    String key = dependency.getGroupId() + ":" + dependency.getArtifactId();
                    if (getDependenciesMap().containsKey(key)) {
                        String dependencyVersion = getDependenciesMap().get(key);
                        Optional<Xml.Tag> version = tag.getChild("version");
                        version.ifPresent(xml -> doAfterVisit(new ChangeTagValueVisitor(xml, dependencyVersion)));
                    }
                }
                return super.visitTag(tag, executionContext);
            }
        };
    }
}
