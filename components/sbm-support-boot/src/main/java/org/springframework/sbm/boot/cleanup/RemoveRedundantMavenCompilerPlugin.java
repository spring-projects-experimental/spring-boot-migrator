package org.springframework.sbm.boot.cleanup;

import java.util.Map;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

public class RemoveRedundantMavenCompilerPlugin extends AbstractAction {

	private static final String GROUP_ID = "org.apache.maven.plugins";

	private static final String ARTIFACT_ID = "maven-compiler-plugin";

	@Override
	public void apply(ProjectContext context) {

		Map<String, Object> configurationMap = context.getBuildFile().getPluginConfiguration(GROUP_ID, ARTIFACT_ID);

		if (configurationMap == null) {
			return;
		}

		long otherConfiguration = configurationMap.entrySet().stream()
				.filter(config -> !config.getKey().equals("source")).filter(config -> !config.getKey().equals("target"))
				.count();

		if (otherConfiguration == 0) {
			context.getBuildFile().removePlugins(GROUP_ID + ":" + ARTIFACT_ID);

		}

	}

}
