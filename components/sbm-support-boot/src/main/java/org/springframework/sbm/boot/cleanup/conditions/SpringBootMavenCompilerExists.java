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
package org.springframework.sbm.boot.cleanup.conditions;

import org.springframework.sbm.boot.common.conditions.HasDecalredSpringBootStarterParent;
import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin;
import org.springframework.sbm.build.migration.conditions.MavenPluginDoesNotExist;
import org.springframework.sbm.common.migration.conditions.FileMatchingPatternExist;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

public class SpringBootMavenCompilerExists implements Condition {

	@Override
	public String getDescription() {
		return "Checks if it's a spring boot project pom has a maven compiler plugin exists";
	}

	@Override
	public boolean evaluate(ProjectContext context) {

		FileMatchingPatternExist fileMatchingPatternExist = new FileMatchingPatternExist();
		fileMatchingPatternExist.setPattern("/**/pom.xml");

		HasDecalredSpringBootStarterParent hasDecalredSpringBootStarterParent = new HasDecalredSpringBootStarterParent();
		hasDecalredSpringBootStarterParent.setVersionPattern(".*");

		MavenPluginDoesNotExist mavenPluginDoesNotExist = new MavenPluginDoesNotExist();
		OpenRewriteMavenPlugin plugin = OpenRewriteMavenPlugin.builder().groupId("org.apache.maven.plugins").artifactId("maven-compiler-plugin")
				.build();
		mavenPluginDoesNotExist.setPlugin(plugin);

		return fileMatchingPatternExist.evaluate(context) && !mavenPluginDoesNotExist.evaluate(context)
				&& hasDecalredSpringBootStarterParent.evaluate(context);
	}

}
