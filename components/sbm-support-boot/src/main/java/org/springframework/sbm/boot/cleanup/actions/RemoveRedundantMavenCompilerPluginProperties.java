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
package org.springframework.sbm.boot.cleanup.actions;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

@Slf4j
public class RemoveRedundantMavenCompilerPluginProperties extends AbstractAction {

	private static final String GROUP_ID = "org.apache.maven.plugins";

	private static final String ARTIFACT_ID = "maven-compiler-plugin";

	private static final String JAVA_VERSION_PROPERTY = "java.version";

	private static final String JAVA_VERSION_PLACEHOLDER = "${java.version}";

	private static final String MAVEN_COMPILER_SOURCE_PROPERTY = "maven.compiler.source";

	private static final String MAVEN_COMPILER_TARGET_PROPERTY = "maven.compiler.target";

	@Override
	public void apply(ProjectContext context) {

		context.getApplicationModules().stream().map(Module::getBuildFile).forEach(buildFile -> {
			buildFile.findPlugin(GROUP_ID, ARTIFACT_ID).ifPresent(plugin -> {

				Optional<String> source = plugin.getConfiguration().getDeclaredStringValue("source");
				Optional<String> target = plugin.getConfiguration().getDeclaredStringValue("target");

				if (source.isPresent() && target.isPresent()) {

					String sourceLookupValue = plugin.getConfiguration().getResolvedStringValue("source");
					String targetLookupValue = plugin.getConfiguration().getResolvedStringValue("target");

					if (sourceLookupValue.equals(targetLookupValue)) {
						buildFile.setProperty(JAVA_VERSION_PROPERTY, sourceLookupValue);
						buildFile.findPlugin(GROUP_ID, ARTIFACT_ID).get().getConfiguration().setDeclaredStringValue("source", JAVA_VERSION_PLACEHOLDER);
						buildFile.findPlugin(GROUP_ID, ARTIFACT_ID).get().getConfiguration().setDeclaredStringValue("target", JAVA_VERSION_PLACEHOLDER);
						if(source.get().startsWith("${")){
							buildFile.deleteProperty(source.get().replace("${", "").replace("}", ""));
						}
						if(target.get().startsWith("${")){
							buildFile.deleteProperty(target.get().replace("${", "").replace("}", ""));
						}
					}

				}

			}); //Plugin exists

			String sourcePropertyValue = buildFile.getProperty(MAVEN_COMPILER_SOURCE_PROPERTY);
			String targetPropertyValue = buildFile.getProperty(MAVEN_COMPILER_TARGET_PROPERTY);
			log.debug("BuildFile: '%s'\n%s".formatted(buildFile.getSourcePath().toString(), buildFile.print()));
			log.debug("%s: %s  %s: %s".formatted(MAVEN_COMPILER_SOURCE_PROPERTY, sourcePropertyValue, MAVEN_COMPILER_TARGET_PROPERTY, targetPropertyValue));
			if (sourcePropertyValue != null && sourcePropertyValue.equals(targetPropertyValue)){
				buildFile.setProperty(JAVA_VERSION_PROPERTY, sourcePropertyValue);
				buildFile.deleteProperty(MAVEN_COMPILER_SOURCE_PROPERTY);
				buildFile.deleteProperty(MAVEN_COMPILER_TARGET_PROPERTY);
			} // If only maven property exists

		});
	}

}
