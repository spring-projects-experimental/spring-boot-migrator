package org.springframework.sbm.boot.cleanup.conditions;

import org.springframework.sbm.boot.common.conditions.IsSpringBootProject;
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

		IsSpringBootProject isSpringBootProject = new IsSpringBootProject();
		isSpringBootProject.setVersionPattern(".*");

		MavenPluginDoesNotExist mavenPluginDoesNotExist = new MavenPluginDoesNotExist();
		OpenRewriteMavenPlugin plugin = OpenRewriteMavenPlugin.builder().groupId("org.apache.maven.plugins").artifactId("maven-compiler-plugin")
				.build();
		mavenPluginDoesNotExist.setPlugin(plugin);

		return fileMatchingPatternExist.evaluate(context) && !mavenPluginDoesNotExist.evaluate(context)
				&& isSpringBootProject.evaluate(context);
	}

}
