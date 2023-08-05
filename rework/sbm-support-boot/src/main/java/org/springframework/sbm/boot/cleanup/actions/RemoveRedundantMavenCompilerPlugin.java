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
package org.springframework.sbm.boot.cleanup.actions;

import java.util.Set;

import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

public class RemoveRedundantMavenCompilerPlugin extends AbstractAction {

	private static final String GROUP_ID = "org.apache.maven.plugins";

	private static final String ARTIFACT_ID = "maven-compiler-plugin";

	@Override
	public void apply(ProjectContext context) {

		context.getApplicationModules().stream().map(Module::getBuildFile)
				.forEach(buildFile -> buildFile.findPlugin(GROUP_ID, ARTIFACT_ID).ifPresent(plugin -> {
					Set<String> configuration = plugin.getConfiguration().getPropertyKeys();


					long otherConfiguration = configuration.stream().filter(config -> !config.equals("source"))
							.filter(config -> !config.equals("target")).count();
					if (otherConfiguration == 0) {
						buildFile.removePlugins(GROUP_ID + ":" + ARTIFACT_ID);
					}
				}));
	}

}
