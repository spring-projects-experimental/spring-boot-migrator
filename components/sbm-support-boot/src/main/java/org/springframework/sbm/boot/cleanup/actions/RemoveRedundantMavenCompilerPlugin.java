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
