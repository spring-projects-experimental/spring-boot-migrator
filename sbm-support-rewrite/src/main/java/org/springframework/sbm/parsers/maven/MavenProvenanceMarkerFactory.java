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
package org.springframework.sbm.parsers.maven;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.openrewrite.Tree;
import org.openrewrite.internal.StringUtils;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.marker.*;
import org.openrewrite.marker.ci.BuildEnvironment;
import org.springframework.sbm.parsers.SbmMavenProject;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Fabian Krüger
 */
@Slf4j
@RequiredArgsConstructor
public class MavenProvenanceMarkerFactory {

    public List<Marker> generateProvenance(Path baseDir, SbmMavenProject mavenProject) {
        MavenRuntimeInformation runtime = mavenProject.getMavenRuntimeInformation();
        BuildTool buildTool = new BuildTool(Tree.randomId(), BuildTool.Type.Maven, runtime.getMavenVersion());

        String javaRuntimeVersion = System.getProperty("java.specification.version");
        String javaVendor = System.getProperty("java.vm.vendor");
        String sourceCompatibility = null;
        String targetCompatibility = null;
        Plugin compilerPlugin = mavenProject.getPlugin("org.apache.maven.plugins:maven-compiler-plugin");
        if (compilerPlugin != null && compilerPlugin.getConfiguration() instanceof Xpp3Dom) {
            Xpp3Dom dom = (Xpp3Dom)compilerPlugin.getConfiguration();
            Xpp3Dom release = dom.getChild("release");
            if (release != null && StringUtils.isNotEmpty(release.getValue()) && !release.getValue().contains("${")) {
                sourceCompatibility = release.getValue();
                targetCompatibility = release.getValue();
            } else {
                Xpp3Dom source = dom.getChild("source");
                if (source != null && StringUtils.isNotEmpty(source.getValue()) && !source.getValue().contains("${")) {
                    sourceCompatibility = source.getValue();
                }

                Xpp3Dom target = dom.getChild("target");
                if (target != null && StringUtils.isNotEmpty(target.getValue()) && !target.getValue().contains("${")) {
                    targetCompatibility = target.getValue();
                }
            }
        }

        if (sourceCompatibility == null || targetCompatibility == null) {
            String propertiesReleaseCompatibility = (String)mavenProject.getProperties().get("maven.compiler.release");
            if (propertiesReleaseCompatibility != null) {
                sourceCompatibility = propertiesReleaseCompatibility;
                targetCompatibility = propertiesReleaseCompatibility;
            } else {
                String propertiesSourceCompatibility = (String)mavenProject.getProperties().get("maven.compiler.source");
                if (sourceCompatibility == null && propertiesSourceCompatibility != null) {
                    sourceCompatibility = propertiesSourceCompatibility;
                }

                String propertiesTargetCompatibility = (String)mavenProject.getProperties().get("maven.compiler.target");
                if (targetCompatibility == null && propertiesTargetCompatibility != null) {
                    targetCompatibility = propertiesTargetCompatibility;
                }
            }
        }

        if (sourceCompatibility == null) {
            sourceCompatibility = javaRuntimeVersion;
        }

        if (targetCompatibility == null) {
            targetCompatibility = sourceCompatibility;
        }

        BuildEnvironment buildEnvironment = BuildEnvironment.build(System::getenv);
        return (List) Stream.of(buildEnvironment, this.gitProvenance(baseDir, buildEnvironment), OperatingSystemProvenance.current(), buildTool, new JavaVersion(Tree.randomId(), javaRuntimeVersion, javaVendor, sourceCompatibility, targetCompatibility), new JavaProject(Tree.randomId(), mavenProject.getName(), new JavaProject.Publication(mavenProject.getGroupId(), mavenProject.getArtifactId(), mavenProject.getVersion()))).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private @Nullable GitProvenance gitProvenance(Path baseDir, @Nullable BuildEnvironment buildEnvironment) {
        try {
            return GitProvenance.fromProjectDirectory(baseDir, buildEnvironment);
        } catch (Exception var4) {
            log.debug("Unable to determine git provenance", var4);
            return null;
        }
    }
}
