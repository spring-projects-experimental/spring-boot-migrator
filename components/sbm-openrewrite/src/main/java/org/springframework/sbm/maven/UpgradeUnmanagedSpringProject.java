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
import org.openrewrite.java.tree.J;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UpgradeUnmanagedSpringProject extends Recipe {

    public static final String SPRINGBOOT_GROUP = "org.springframework.boot";
    private final String springVersion;

    private Map<String, String> springBootDependenciesMap;

    public UpgradeUnmanagedSpringProject(String springVersion) {

        this.springVersion = springVersion;
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getApplicableTest() {
        return new MavenIsoVisitor<>() {
            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
                if (isDependencyTag()) {
                    ResolvedDependency dependency = findDependency(tag);
                    if (dependency.getGroupId().equals(SPRINGBOOT_GROUP)) {
                        return tag.withMarkers(tag.getMarkers().searchResult());
                    }
                }
                return super.visitTag(tag, executionContext);
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
                        if (version.isPresent()) {
                            doAfterVisit(new ChangeTagValueVisitor(version.get(), dependencyVersion));
                        }
                    }
                }
                return super.visitTag(tag, executionContext);
            }
        };
    }
}
