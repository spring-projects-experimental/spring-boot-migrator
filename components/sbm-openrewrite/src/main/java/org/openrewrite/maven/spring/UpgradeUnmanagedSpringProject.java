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

package org.openrewrite.maven.spring;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.marker.SearchResult;
import org.openrewrite.maven.ChangePropertyValue;
import org.openrewrite.maven.MavenIsoVisitor;
import org.openrewrite.maven.UpdateMavenModel;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.*;
import org.openrewrite.semver.LatestRelease;
import org.openrewrite.semver.VersionComparator;
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
    private String newVersion;
    private Pattern oldVersionPattern;

    private Map<String, String> springBootDependenciesMap;
    private VersionComparator versionComparator = new LatestRelease(null);

    public UpgradeUnmanagedSpringProject() {
    }

    public UpgradeUnmanagedSpringProject(String newVersion, String versionPattern) {

        this.newVersion = newVersion;
        this.oldVersionPattern = Pattern.compile(versionPattern);
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public void setVersionPattern(String versionPattern) {
        this.oldVersionPattern = Pattern.compile(versionPattern);
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getApplicableTest() {
        return new MavenIsoVisitor<>() {
            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
                Xml.Tag resultTag = super.visitTag(tag, executionContext);
                if (isManagedDependencyTag()) {
                    ResolvedManagedDependency managedDependency = findManagedDependency(resultTag);
                    if ((managedDependency != null) && managedDependency.getGroupId().equals(SPRINGBOOT_GROUP)
                            && satisfiesOldVersionPattern(managedDependency.getVersion())) {
                        return applyThisRecipe(resultTag);
                    }
                }

                if (isDependencyTag()) {
                    ResolvedDependency dependency = findDependency(resultTag);
                    if ((dependency != null) && dependency.getGroupId().equals(SPRINGBOOT_GROUP)
                            && satisfiesOldVersionPattern(dependency.getVersion())) {
                        return applyThisRecipe(resultTag);
                    }
                }
                return resultTag;
            }

            @NotNull
            private Xml.Tag applyThisRecipe(Xml.Tag resultTag) {
                return resultTag.withMarkers(resultTag.getMarkers().addIfAbsent(new SearchResult(UUID.randomUUID(), "SpringBoot dependency")));
            }

            private boolean satisfiesOldVersionPattern(String version) {
                return oldVersionPattern.matcher(version).matches();
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
    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new MavenIsoVisitor<>() {
            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
                Xml.Tag resultTag = super.visitTag(tag, executionContext);
                if (isManagedDependencyTag()) {
                    ResolvedManagedDependency managedDependency = findManagedDependency(resultTag);
                    if (managedDependency != null) {
                        String key = managedDependency.getGroupId() + ":" + managedDependency.getArtifactId();
                        mayBeUpdateVersion(key, resultTag);
                    }
                }
                if (isDependencyTag()) {
                    ResolvedDependency dependency = findDependency(resultTag);
                    if (dependency != null) {
                        String key = dependency.getGroupId() + ":" + dependency.getArtifactId();
                        mayBeUpdateVersion(key, resultTag);
                    }
                }
                return resultTag;
            }

            private void mayBeUpdateVersion(String key, Xml.Tag tag) {
                if (getDependenciesMap().containsKey(key)) {
                    String dependencyVersion = getDependenciesMap().get(key);
                    Optional<Xml.Tag> version = tag.getChild("version");
                    if (version.isEmpty() || version.get().getValue().isEmpty()) {
                        return;
                    }
                    String versionValue = version.get().getValue().get();
                    if (!isVersionToUpgrade(dependencyVersion, versionValue)) {
                        return;
                    }
                    if (versionValue.startsWith("${")) {
                        String propertyName = versionValue.substring(2, versionValue.length() - 1);
                        version.ifPresent(xml -> doAfterVisit(new ChangePropertyValue(propertyName, dependencyVersion, true)));
                    } else {
                        version.ifPresent(xml -> doAfterVisit(new ChangeTagValueVisitor(xml, dependencyVersion)));
                    }
                    doAfterVisit(new UpdateMavenModel<>());
                }
            }

            private boolean isVersionToUpgrade(String upgradeVersion, String versionValue) {
                String currentVersion = versionValue;
                if (versionValue.startsWith("${")) {
                    String versionName = versionValue.substring(2, versionValue.length() - 1);
                    Map<String, String> properties = getResolutionResult().getPom().getProperties();
                    if ((properties != null) && properties.containsKey(versionName)) {
                        currentVersion = properties.get(versionName);
                    } else {
                        return false;
                    }
                }
                return versionComparator.compare(null, upgradeVersion, currentVersion) > 0;
            }
        };
    }

    private Map<String, String> buildDependencyMap() {
        Map<Path, Pom> poms = new HashMap<>();
        MavenPomDownloader downloader = new MavenPomDownloader(poms, new InMemoryExecutionContext());
        GroupArtifactVersion gav = new GroupArtifactVersion(SPRINGBOOT_GROUP, SPRING_BOOT_DEPENDENCIES, newVersion);
        String relativePath = "";
        ResolvedPom containingPom = null;
        List<MavenRepository> repositories = new ArrayList<>();
        repositories.add(new MavenRepository("repository.spring.milestone", "https://repo.spring.io/milestone", true, true, null, null));
        repositories.add(new MavenRepository("spring-snapshot", "https://repo.spring.io/snapshot", false, true, null, null));
        repositories.add(new MavenRepository("spring-release", "https://repo.spring.io/release", true, false, null, null));
        Pom pom = downloader.download(gav, relativePath, containingPom, repositories);
        ResolvedPom resolvedPom = pom.resolve(List.of(), downloader, repositories, new InMemoryExecutionContext());
        List<ResolvedManagedDependency> dependencyManagement = resolvedPom.getDependencyManagement();
        Map<String, String> dependencyMap = new HashMap<>();
        dependencyManagement
                .stream()
                .filter(d -> d.getVersion() != null)
                .forEach(d -> dependencyMap.put(d.getGroupId() + ":" + d.getArtifactId().toLowerCase(), d.getVersion()));
        return dependencyMap;
    }
}
