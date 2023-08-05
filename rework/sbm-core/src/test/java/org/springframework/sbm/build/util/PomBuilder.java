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
package org.springframework.sbm.build.util;

import org.springframework.sbm.project.parser.DependencyHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.openrewrite.maven.internal.MavenXmlMapper;

import org.springframework.sbm.build.api.Plugin;

import org.openrewrite.maven.tree.Dependency;
import org.openrewrite.maven.tree.Scope;

public class PomBuilder {
        private String coordinate;
        private List<String> modules;
		private String packaging;
        private String parent;
        private String artifactId;
		private Map<String, String> properties = new HashMap<>();
		private Map<Scope, org.openrewrite.maven.tree.Dependency> dependencies = new LinkedHashMap<Scope, Dependency>();
		private List<Plugin> plugins = new ArrayList<>();

    	private DependencyHelper dependencyHelper = new DependencyHelper();
    	private String parentPom;

		public static PomBuilder buildPom(String coordinate) {
			PomBuilder pomBuilder = new PomBuilder();
			pomBuilder.coordinate = coordinate;
			return pomBuilder;
		}

    public static PomBuilder buildPom(String parentCoordinate, String artifactId) {
        PomBuilder pomBuilder = new PomBuilder();
        pomBuilder.parent = parentCoordinate;
        pomBuilder.artifactId = artifactId;
        return pomBuilder;
    }

    /**
     * Build a parent pom file with a parent, e.g. spring-boot-starter-parent
     *
     * @param parentCoordinate
     * @param coordinate
     */
    public static PomBuilder buildParentPom(String parentCoordinate, String coordinate) {
        PomBuilder pomBuilder = new PomBuilder();
        pomBuilder.parentPom = parentCoordinate;
        pomBuilder.coordinate = coordinate;
        return pomBuilder;
    }

    /**
     * Create a root {@code pom.xml} with a parent, e.g. spring-boot-starter-parent
     */
    public static PomBuilder buildRootWithParent(String parentCoordinate, String pomCoordinate) {
        PomBuilder pomBuilder = new PomBuilder();
        pomBuilder.parentPom = parentCoordinate;
        pomBuilder.coordinate = pomCoordinate;
        return pomBuilder;
    }

    /**
     * Add modules to a pom.
     *
     * @param moduleArtifactNames one or more module artifactIds
     */
    public PomBuilder withModules(String... moduleArtifactNames) {
        this.modules = Arrays.asList(moduleArtifactNames);
        if(this.modules.stream().anyMatch(m -> m.contains(":"))) throw new RuntimeException("Found ':' in artifact name but artifact names of modules must not be provided as coordinate.");
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                          <?xml version="1.0" encoding="UTF-8"?>
                          <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                              <modelVersion>4.0.0</modelVersion>
                          """);

        if(parentPom != null && parent != null) {
            throw new IllegalStateException("parentPom and parent were set.");
        }

        if (parent != null) {
            String[] coord = parent.split(":");
            sb.append("    <parent>").append("\n");
            sb.append("        <groupId>").append(coord[0]).append("</groupId>").append("\n");
            sb.append("        <artifactId>").append(coord[1]).append("</artifactId>").append("\n");
            sb.append("        <version>").append(coord[2]).append("</version>").append("\n");
            sb.append("    </parent>").append("\n");
            sb.append("    <artifactId>").append(artifactId).append("</artifactId>").append("\n");
        } else if (parentPom != null) {
            String[] coord = parentPom.split(":");
            sb.append("    <parent>").append("\n");
            sb.append("        <groupId>").append(coord[0]).append("</groupId>").append("\n");
            sb.append("        <artifactId>").append(coord[1]).append("</artifactId>").append("\n");
            sb.append("        <version>").append(coord[2]).append("</version>").append("\n");
            sb.append("    </parent>").append("\n");
        } if (parent == null){
            String[] coord = coordinate.split(":");
            sb.append("    <groupId>").append(coord[0]).append("</groupId>").append("\n");
            sb.append("    <artifactId>").append(coord[1]).append("</artifactId>").append("\n");
            sb.append("    <version>").append(coord[2]).append("</version>").append("\n");
        }

		if(packaging != null ){
			sb.append("    <packaging>").append(packaging).append("</packaging>").append("\n");
		}

		if(!properties.isEmpty()){
			sb.append(buildProperties(properties));
		}

        if (modules != null && !modules.isEmpty()) {
            sb.append("    <modules>").append("\n");
            modules.forEach(m -> sb.append("        <module>").append(m).append("</module>\n"));
            sb.append("    </modules>").append("\n");
        }

        if (!dependencies.isEmpty()) {
            String dependenciesRendered = renderDependencies(dependencies);
            sb.append(dependenciesRendered);
        }

		if(!plugins.isEmpty()){
			sb.append(renderPlugins());
		}

        sb.append("</project>\n");
        return sb.toString();
    }

	String buildProperties(Map<String, String> properties) {
		StringBuilder builder = new StringBuilder();
		builder.append("    ").append("<properties>").append("\n");
		String props = properties.entrySet().stream().map(entry -> "    " + "    " + "<" + entry.getKey() + ">"
				+ entry.getValue() + "</" + entry.getKey() + ">").collect(Collectors.joining("\n"));
		builder.append(props).append("\n");
		builder.append("    ").append("</properties>").append("\n");
		return builder.toString();
	}

    String renderDependencies(Map<Scope, org.openrewrite.maven.tree.Dependency> dependencies) {
        StringBuilder dependenciesSection = new StringBuilder();
        dependenciesSection.append("    ").append("<dependencies>").append("\n");
        dependencies.entrySet().forEach(e -> {
            renderDependency(dependenciesSection, e.getKey(), e.getValue());
        });
        dependenciesSection.append("    ").append("</dependencies>").append("\n");
        String dependenciesText = dependenciesSection.toString();
        return dependenciesText;
    }

    private void renderDependency(StringBuilder dependenciesSection, Scope scope, org.openrewrite.maven.tree.Dependency dependency) {

        dependenciesSection
                .append("    ")
                .append("    ")
                .append("<dependency>")
                .append("\n");
        dependenciesSection
                    .append("    ")
                    .append("    ")
                    .append("    ")
                    .append("<groupId>")
                    .append(dependency.getGroupId())
                    .append("</groupId>")
                    .append("\n");
            dependenciesSection
                    .append("    ")
                    .append("    ")
                    .append("    ")
                    .append("<artifactId>")
                    .append(dependency.getArtifactId())
                    .append("</artifactId>")
                    .append("\n");
            if(dependency.getVersion() != null) {
                dependenciesSection
                        .append("    ")
                        .append("    ")
                        .append("    ")
                        .append("<version>")
                        .append(dependency.getVersion())
                        .append("</version>")
                        .append("\n");
            }
            if(scope != Scope.None) {
                dependenciesSection
                        .append("    ")
                        .append("    ")
                        .append("    ")
                        .append("<scope>")
                        .append(scope.name().toLowerCase())
                        .append("</scope>")
                        .append("\n");
            }
        dependenciesSection
                .append("    ")
                .append("    ")
                .append("</dependency>")
                .append("\n");
    }

	private String renderPlugins(){
		StringBuilder pluginSection = new StringBuilder();
		if (!plugins.isEmpty()) {
			pluginSection.append("    ").append("<build>").append("\n");
			pluginSection.append("    ").append("    ").append("<plugins>").append("\n");
			try {
				String plugin = MavenXmlMapper.writeMapper().writerWithDefaultPrettyPrinter().writeValueAsString(plugins.get(0));
				pluginSection.append(plugin.replaceAll("  ", "    ").replaceAll("(?m)^", " ".repeat(12)));
			}
			catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			pluginSection.append("    ").append("    ").append("</plugins>").append("\n");
			pluginSection.append("    ").append("</build>").append("\n");
		}
		return pluginSection.toString();
	}

	public PomBuilder packaging(String type) {
		this.packaging = type;
		return this;
	}

    public PomBuilder unscopedDependencies(String... coordinates) {
        dependencyHelper.mapCoordinatesToDependencies(Arrays.asList(coordinates))
                .stream()
                .forEach(c -> this.dependencies.put(Scope.None, c));
        return this;
    }

    public PomBuilder compileScopeDependencies(String... coordinates) {
        dependencyHelper.mapCoordinatesToDependencies(Arrays.asList(coordinates))
                .stream()
                .forEach(c -> this.dependencies.put(Scope.Compile, c));
        return this;
    }

    public PomBuilder testScopeDependencies(String... coordinates) {
        dependencyHelper.mapCoordinatesToDependencies(Arrays.asList(coordinates))
                .stream()
                .forEach(c -> this.dependencies.put(Scope.Test, c));
        return this;
    }

    public PomBuilder withProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

	public PomBuilder property(String property, String value){
		this.properties.put(property,value);
		return this;
	}

	public PomBuilder plugins(Plugin... p) {
		this.plugins = Arrays.asList(p);
		return this;
	}
}
