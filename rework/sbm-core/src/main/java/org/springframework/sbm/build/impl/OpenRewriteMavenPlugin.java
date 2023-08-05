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
package org.springframework.sbm.build.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import org.openrewrite.maven.ChangePluginConfiguration;
import org.openrewrite.maven.internal.MavenXmlMapper;
import org.openrewrite.xml.tree.Xml;
import org.openrewrite.xml.tree.Xml.Document;

import org.springframework.sbm.build.api.Plugin;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "plugin")
@JsonInclude(Include.NON_NULL)
public class OpenRewriteMavenPlugin implements Plugin {

	@NotNull
	private String groupId;

	@NotNull
	private String artifactId;

	private String version;

	@Singular("execution")
	private List<OpenRewriteMavenPluginExecution> executions;

	private OpenRewriteMavenPluginConfiguration configuration;

	@JsonIgnore
	private RewriteSourceFileHolder<Document> resourceWrapper;

	@JsonIgnore
	private MavenBuildFileRefactoring<Xml.Document> refactoring;

	private String dependencies;

	public OpenRewriteMavenPlugin(org.openrewrite.maven.tree.Plugin openRewritePlugin,RewriteSourceFileHolder<Document> resource, MavenBuildFileRefactoring<Xml.Document> refactoring) {
		this.groupId = openRewritePlugin.getGroupId();
		this.artifactId = openRewritePlugin.getArtifactId();
		this.version = openRewritePlugin.getVersion();
		this.configuration = mapConfiguration(openRewritePlugin.getConfiguration());
		this.executions = mapExecutions(openRewritePlugin.getExecutions());
		this.dependencies = null;
		this.resourceWrapper = resource;
		this.refactoring = refactoring;
	}

	private List<OpenRewriteMavenPluginExecution> mapExecutions(
			List<org.openrewrite.maven.tree.Plugin.Execution> openRewritePluginExecutions) {
		if (openRewritePluginExecutions == null || openRewritePluginExecutions.isEmpty()) {
			return new ArrayList<>();
		}
		return openRewritePluginExecutions.stream()
				.map(orExecution -> new OpenRewriteMavenPluginExecution(orExecution.getId(), orExecution.getGoals(),
						orExecution.getPhase(), null))
				.collect(Collectors.toList());

	}

	private OpenRewriteMavenPluginConfiguration mapConfiguration(JsonNode openRewritePluginConfiguration) {
		if (openRewritePluginConfiguration == null || openRewritePluginConfiguration.isEmpty()) {
			return new OpenRewriteMavenPluginConfiguration(new LinkedHashMap<>());
		}
		return new OpenRewriteMavenPluginConfiguration(
				MavenXmlMapper.readMapper().convertValue(openRewritePluginConfiguration, new TypeReference<>() {
				}));
	}

	@AllArgsConstructor
	@NoArgsConstructor
	public class OpenRewriteMavenPluginConfiguration implements Configuration {

		@JsonAnyGetter
		private Map<String, Object> configuration;

		@Override
		public Optional<String> getDeclaredStringValue(String property) {
			return Optional.ofNullable((String) configuration.get(property));
		}

		@Override
		public String getResolvedStringValue(String property) {
			Optional<String> value = getDeclaredStringValue(property);
			String propertyValue = value
					.orElseThrow((() -> new IllegalStateException("Found no value for property " + property)));
			if (propertyValue.startsWith("${")) {
				String propertyWithoutBraces = propertyValue.replace("${", "").replace("}", "");
				return MavenBuildFileUtil
						.findMavenResolution(OpenRewriteMavenPlugin.this.resourceWrapper.getSourceFile()).get().getPom()
						.getProperties().get(propertyWithoutBraces);
			}
			else {
				return propertyValue;
			}
		}

		@Override
		public void setDeclaredStringValue(String property, String value) {
			configuration.put(property, value);
			changeConfiguration();
		}

		@Override
		@JsonIgnore
		public Set<String> getPropertyKeys() {
			return configuration.keySet();
		}

		private void changeConfiguration() {
			try {
				String configurationXml = MavenXmlMapper.writeMapper().writerWithDefaultPrettyPrinter()
						.writeValueAsString(configuration).replaceFirst("<LinkedHashMap>", "")
						.replace("</LinkedHashMap>", "").replace("<LinkedHashMap/>", "").trim();
				OpenRewriteMavenPlugin.this.refactoring.execute(
						OpenRewriteMavenPlugin.this.getResourceWrapper(),
						new ChangePluginConfiguration(
							OpenRewriteMavenPlugin.this.groupId,
							OpenRewriteMavenPlugin.this.artifactId,
							configurationXml
						)
				);
				OpenRewriteMavenPlugin.this.refactoring.refreshPomModels();
			}
			catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}

	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Setter
	@JsonInclude(Include.NON_NULL)
	@JacksonXmlRootElement(localName = "execution")
	public static class OpenRewriteMavenPluginExecution implements Execution {

		@Null
		private String id;

		@Singular("goal")
		private List<String> goals;

		@Null
		private String phase;

		@Null
		private String configuration;

	}

}
