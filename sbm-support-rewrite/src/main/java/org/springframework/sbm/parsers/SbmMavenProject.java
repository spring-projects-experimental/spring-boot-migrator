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
package org.springframework.sbm.parsers;

import lombok.Getter;
import lombok.Setter;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.SourceFile;
import org.openrewrite.maven.tree.*;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
/**
 * @author Fabian Krüger
 */
public class SbmMavenProject {

    private final Path projectRoot;
    private final Resource pomFile;
    // FIXME: 945 temporary method, model should nopt come from Maven
    private final Model pomModel;
    private List<SbmMavenProject> collectedProjects = new ArrayList<>();
    private SourceFile sourceFile;
    private final MavenArtifactDownloader rewriteMavenArtifactDownloader;

    public SbmMavenProject(Path projectRoot, Resource pomFile, Model pomModel, MavenArtifactDownloader rewriteMavenArtifactDownloader) {
        this.projectRoot = projectRoot;
        this.pomFile = pomFile;
        this.pomModel = pomModel;
        this.rewriteMavenArtifactDownloader = rewriteMavenArtifactDownloader;
    }

    public File getFile() {
        return ResourceUtil.getPath(pomFile).toFile();
    }

    public Path getBasedir() {
        return pomFile == null ? null : ResourceUtil.getPath(pomFile).getParent();
    }

    public void setCollectedProjects(List<SbmMavenProject> collected) {
        this.collectedProjects = collected;
    }

    public List<SbmMavenProject> getCollectedProjects() {
        return collectedProjects;
    }

    public Resource getResource() {
        return pomFile;
    }

    public Path getModuleDir() {
        if(getBasedir() == null) {
            return null;
        } else if(projectRoot.relativize(ResourceUtil.getPath(pomFile)).toString().equals("pom.xml")){
            return Path.of("");
        } else {
            return projectRoot.relativize(ResourceUtil.getPath(pomFile)).getParent();
        }
    }


    public String getGroupIdAndArtifactId() {
        return this.pomModel.getGroupId() + ":" + pomModel.getArtifactId();
    }

    public Path getPomFilePath() {
        return ResourceUtil.getPath(pomFile);
    }

    public Plugin getPlugin(String s) {
        return pomModel.getBuild() == null ? null : pomModel.getBuild().getPluginsAsMap().get(s);
    }

    public Properties getProperties() {
        return pomModel.getProperties();
    }

    public MavenRuntimeInformation getMavenRuntimeInformation() {
        // FIXME: 945 implement this
        return new MavenRuntimeInformation();
    }

    public String getName() {
        return pomModel.getName();
    }

    public String getGroupId() {
        return pomModel.getGroupId() == null ? pomModel.getParent().getGroupId() : pomModel.getGroupId();
    }

    public String getArtifactId() {
        return pomModel.getArtifactId();
    }

    public String getVersion() {
        return pomModel.getVersion();
    }

    @Override
    public String toString() {
        String groupId = pomModel.getGroupId() == null ? pomModel.getParent().getGroupId() : pomModel.getGroupId();
        return groupId + ":" + pomModel.getArtifactId();
    }

    public String getBuildDirectory() {
        String s = pomModel.getBuild() != null ? pomModel.getBuild().getDirectory() : null;
        return s == null ? ResourceUtil.getPath(pomFile).getParent().resolve("target").toAbsolutePath().normalize().toString() : s;
    }

    public String getSourceDirectory() {
        String s = pomModel.getBuild() != null ? pomModel.getBuild().getSourceDirectory() : null;
        return s == null ? ResourceUtil.getPath(pomFile).getParent().resolve("src/main/java").toAbsolutePath().normalize().toString() : s;
    }

    public List<Path> getCompileClasspathElements() {
        Scope scope = Scope.Compile;
        return getClasspathElements(scope);
    }

    public List<Path> getTestClasspathElements() {
        return getClasspathElements(Scope.Test);
    }

    @NotNull
    private List<Path> getClasspathElements(Scope scope) {
        MavenResolutionResult pom = getSourceFile().getMarkers().findFirst(MavenResolutionResult.class).get();
        List<ResolvedDependency> resolvedDependencies = pom.getDependencies().get(scope);
        if(resolvedDependencies != null) {
            return resolvedDependencies
                    // FIXME: 945 - deal with dependencies to projects in reactor
                    //
                    .stream()
                    .filter(rd -> rd.getRepository() != null)
                    .map(rd -> rewriteMavenArtifactDownloader.downloadArtifact(rd))
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    public String getTestSourceDirectory() {
        String s = pomModel.getBuild() != null ? pomModel.getBuild().getSourceDirectory() : null;
        return s == null ? ResourceUtil.getPath(pomFile).getParent().resolve("src/test/java").toAbsolutePath().normalize().toString() : s;
    }

    public void setSourceFile(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
    }
}
