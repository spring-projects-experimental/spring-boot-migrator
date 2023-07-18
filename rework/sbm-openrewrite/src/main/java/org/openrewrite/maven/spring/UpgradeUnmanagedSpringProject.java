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

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.marker.SearchResult;
import org.openrewrite.maven.ChangePropertyValue;
import org.openrewrite.maven.MavenDownloadingException;
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

@Slf4j
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

    @Override
    public String getDisplayName() {
        return "Upgrade unmanaged spring project";
    }

    @Override
    public String getDescription() {
        return getDisplayName();
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

    // FIXME: What happens to getApplicableTest()
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

    public synchronized Map<String, String> getDependenciesMap(ExecutionContext ctx) {
        if (springBootDependenciesMap == null) {
            springBootDependenciesMap = buildDependencyMap(ctx);
        }
        return springBootDependenciesMap;
    }
    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new MavenIsoVisitor<>() {
            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
                Xml.Tag resultTag = super.visitTag(tag, executionContext);
                if (isManagedDependencyTag()) {
                    ResolvedManagedDependency managedDependency = findManagedDependency(resultTag);
                    if (managedDependency != null) {
                        String key = managedDependency.getGroupId() + ":" + managedDependency.getArtifactId();
                        mayBeUpdateVersion(key, resultTag, executionContext);
                    }
                }
                if (isDependencyTag()) {
                    ResolvedDependency dependency = findDependency(resultTag);
                    if (dependency != null) {
                        String key = dependency.getGroupId() + ":" + dependency.getArtifactId();
                        mayBeUpdateVersion(key, resultTag, executionContext);
                    }
                }
                return resultTag;
            }

            private void mayBeUpdateVersion(String key, Xml.Tag tag, ExecutionContext ctx) {
                if (getDependenciesMap(ctx).containsKey(key)) {
                    String dependencyVersion = getDependenciesMap(ctx).get(key);
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
                        ChangePropertyValue visitor = new ChangePropertyValue(propertyName, dependencyVersion, true, true);
                        version.ifPresent(xml -> doAfterVisit(visitor.getVisitor()));
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

    private Map<String, String> buildDependencyMap(ExecutionContext ctx) {
        Map<Path, Pom> poms = new HashMap<>();
        MavenPomDownloader downloader = new MavenPomDownloader(poms, ctx);
        GroupArtifactVersion gav = new GroupArtifactVersion(SPRINGBOOT_GROUP, SPRING_BOOT_DEPENDENCIES, newVersion);
        String relativePath = "";
        ResolvedPom containingPom = null;
        List<MavenRepository> repositories = new ArrayList<>();
        repositories.add(new MavenRepository("repository.spring.milestone", "https://repo.spring.io/milestone", "true", "true", null, null));
        repositories.add(new MavenRepository("spring-snapshot", "https://repo.spring.io/snapshot", "false", "true", null, null));
        repositories.add(new MavenRepository("spring-release", "https://repo.spring.io/release", "true", "false", null, null));
        Pom pom = null;
        ResolvedPom resolvedPom = null;
        Map<String, String> dependencyMap = new HashMap<>();
        try {
            pom = downloader.download(gav, relativePath, containingPom, repositories);
            resolvedPom = pom.resolve(List.of(), downloader, repositories, ctx);
            List<ResolvedManagedDependency> dependencyManagement = resolvedPom.getDependencyManagement();
            dependencyManagement
                    .stream()
                    .filter(d -> d.getVersion() != null)
                    .forEach(d -> dependencyMap.put(d.getGroupId() + ":" + d.getArtifactId().toLowerCase(), d.getVersion()));
        } catch (MavenDownloadingException e) {
            log.error("Error while downloading dependency.", e);
        }
        return dependencyMap;
    }
}
