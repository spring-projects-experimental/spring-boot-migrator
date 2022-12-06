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
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import org.openrewrite.maven.internal.MavenXmlMapper;

import org.springframework.sbm.build.api.Plugin;

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

	private String dependencies;

	public OpenRewriteMavenPlugin(org.openrewrite.maven.tree.Plugin openRewritePlugin) {
		this.groupId = openRewritePlugin.getGroupId();
		this.artifactId = openRewritePlugin.getArtifactId();
		this.version = openRewritePlugin.getVersion();
		this.configuration = mapConfiguration(openRewritePlugin.getConfiguration());
		this.executions = mapExecutions(openRewritePlugin.getExecutions());
		this.dependencies = null;
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
	@Getter
	@Setter
	public class OpenRewriteMavenPluginConfiguration implements Configuration {

		private Map<String, Object> configuration;

		@JsonAnyGetter
		public Map<String, Object> getConfiguration() {
			return configuration;
		}

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
				return "";
				// String propertyWithoutBraces = propertyValue.replace("${",
				// "").replace("}", "");
				// return MavenBuildFileUtil
				// .findMavenResolution(OpenRewriteMavenPlugin.this.resourceWrapper.getSourceFile()).get().getPom()
				// .getProperties().get(propertyWithoutBraces);
			}
			else {
				return propertyValue;
			}
		}

		@Override
		public void setDeclaredStringValue(String property, String value) {
			configuration.put(property, value);
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
