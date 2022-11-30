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
package org.springframework.sbm.build.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import org.openrewrite.maven.internal.MavenXmlMapper;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plugin {

    @NotNull
    private String groupId;

    @NotNull
    private String artifactId;

    private String version;

    @Singular("execution")
    private List<Execution> executions;

    Map<String, Object> configuration;

    private String dependencies;

	public Plugin(org.openrewrite.maven.tree.Plugin openRewritePlugin){
		this.groupId = openRewritePlugin.getGroupId();
		this.artifactId = openRewritePlugin.getArtifactId();
		this.version = openRewritePlugin.getVersion();
		this.configuration = mapConfiguration(openRewritePlugin.getConfiguration());
		this.executions = mapExecutions(openRewritePlugin.getExecutions());
		this.dependencies = "";
	}

	private List<Execution> mapExecutions(
			List<org.openrewrite.maven.tree.Plugin.Execution> openRewritePluginExecutions) {
		if (openRewritePluginExecutions == null || openRewritePluginExecutions.isEmpty()) {
			return new ArrayList<>();
		}
		return openRewritePluginExecutions.stream().map(
				orExecution -> new Execution(orExecution.getId(), orExecution.getGoals(), orExecution.getPhase(), null))
				.collect(Collectors.toList());

	}

	private Map<String, Object> mapConfiguration(JsonNode openRewritePluginConfiguration) {
		if (openRewritePluginConfiguration == null || openRewritePluginConfiguration.isEmpty()) {
			return new LinkedHashMap<>();
		}
		return MavenXmlMapper.readMapper().convertValue(openRewritePluginConfiguration,
				new TypeReference<>() {
				});
	}

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class Execution {
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
