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
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.maven.MavenRuntimeInformation;
import org.springframework.sbm.utils.ResourceUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Predicate;

@Getter
@Setter
/**
 * @author Fabian Krüger
 */
public class MavenProject {

	private final Path projectRoot;

	private final Resource pomFile;

	// FIXME: 945 temporary method, model should nopt come from Maven
	private final Model pomModel;

	private List<MavenProject> collectedProjects = new ArrayList<>();

	private Xml.Document sourceFile;

	private final MavenArtifactDownloader rewriteMavenArtifactDownloader;

	private final List<Resource> resources;

	private ProjectId projectId;

	public MavenProject(Path projectRoot, Resource pomFile, Model pomModel,
			MavenArtifactDownloader rewriteMavenArtifactDownloader, List<Resource> resources) {
		this.projectRoot = projectRoot;
		this.pomFile = pomFile;
		this.pomModel = pomModel;
		this.rewriteMavenArtifactDownloader = rewriteMavenArtifactDownloader;
		this.resources = resources;
		projectId = new ProjectId(getGroupId(), getArtifactId());
	}

	public File getFile() {
		return ResourceUtil.getPath(pomFile).toFile();
	}

	public Path getBasedir() {
		// TODO: 945 Check if this is correct
		return pomFile == null ? null : ResourceUtil.getPath(pomFile).getParent();
	}

	public void setCollectedProjects(List<MavenProject> collected) {
		this.collectedProjects = collected;
	}

	public List<MavenProject> getCollectedProjects() {
		return collectedProjects;
	}

	public Resource getResource() {
		return pomFile;
	}

	public Path getModuleDir() {
		if (getBasedir() == null) {
			return null;
		}
		else if (projectRoot.relativize(ResourceUtil.getPath(pomFile)).toString().equals("pom.xml")) {
			return Path.of("");
		}
		else {
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

	/**
	 * FIXME: when the version of parent pom is null (inherited by it's parent) the
	 * version will be null.
	 */
	public String getVersion() {
		return pomModel.getVersion() == null ? pomModel.getParent().getVersion() : pomModel.getVersion();
	}

	@Override
	public String toString() {
		String groupId = pomModel.getGroupId() == null ? pomModel.getParent().getGroupId() : pomModel.getGroupId();
		return groupId + ":" + pomModel.getArtifactId();
	}

	public String getBuildDirectory() {
		String s = pomModel.getBuild() != null ? pomModel.getBuild().getDirectory() : null;
		return s == null
				? ResourceUtil.getPath(pomFile).getParent().resolve("target").toAbsolutePath().normalize().toString()
				: s;
	}

	public String getSourceDirectory() {
		String s = pomModel.getBuild() != null ? pomModel.getBuild().getSourceDirectory() : null;
		return s == null ? ResourceUtil.getPath(pomFile)
			.getParent()
			.resolve("src/main/java")
			.toAbsolutePath()
			.normalize()
			.toString() : s;
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
		if (resolvedDependencies != null) {
			return resolvedDependencies
				// FIXME: 945 - deal with dependencies to projects in reactor
				//
				.stream()
				.filter(rd -> rd.getRepository() != null)
				.map(rd -> rewriteMavenArtifactDownloader.downloadArtifact(rd))
				.filter(Objects::nonNull)
				.distinct()
				.toList();
		}
		else {
			return new ArrayList<>();
		}
	}

	public String getTestSourceDirectory() {
		String s = pomModel.getBuild() != null ? pomModel.getBuild().getSourceDirectory() : null;
		return s == null ? ResourceUtil.getPath(pomFile)
			.getParent()
			.resolve("src/test/java")
			.toAbsolutePath()
			.normalize()
			.toString() : s;
	}

	public void setSourceFile(Xml.Document sourceFile) {
		this.sourceFile = sourceFile;
	}

	private static List<Resource> listJavaSources(List<Resource> resources, Path sourceDirectory) {
		return resources.stream().filter(whenIn(sourceDirectory)).filter(whenFileNameEndsWithJava()).toList();
	}

	@NotNull
	private static Predicate<Resource> whenFileNameEndsWithJava() {
		return p -> ResourceUtil.getPath(p).getFileName().toString().endsWith(".java");
	}

	@NotNull
	private static Predicate<Resource> whenIn(Path sourceDirectory) {
		return r -> ResourceUtil.getPath(r).toString().startsWith(sourceDirectory.toString());
	}

	public List<Resource> getJavaSourcesInTarget() {
		return listJavaSources(getResources(), getBasedir().resolve(getBuildDirectory()));
	}

	private List<Resource> getResources() {
		return this.resources;
	}

	public List<Resource> getMainJavaSources() {
		return listJavaSources(resources, getProjectRoot().resolve(getModuleDir()).resolve("src/main/java"));
	}

	public Path getModulePath() {
		return projectRoot.resolve(getModuleDir());
	}

	public ProjectId getProjectId() {
		return projectId;
	}

	public Object getProjectEncoding() {
		return getPomModel().getProperties().get("project.build.sourceEncoding");
	}

}
