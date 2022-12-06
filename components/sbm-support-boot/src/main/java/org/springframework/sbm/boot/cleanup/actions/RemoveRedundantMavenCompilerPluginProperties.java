package org.springframework.sbm.boot.cleanup.actions;

import java.util.Optional;

import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

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
						plugin.getConfiguration().setDeclaredStringValue("source", JAVA_VERSION_PLACEHOLDER);
						plugin.getConfiguration().setDeclaredStringValue("target", JAVA_VERSION_PLACEHOLDER);
						if(source.get().startsWith("${")){
							buildFile.deleteProperty(source.get().replace("${", "").replace("}", ""));
						}
						if(target.get().startsWith("${")){
							buildFile.deleteProperty(target.get().replace("${", "").replace("}", ""));
						}
					}

				}

			}); //Plugin exits

			String sourcePropertyValue = buildFile.getProperty(MAVEN_COMPILER_SOURCE_PROPERTY);
			String targetPropertyValue = buildFile.getProperty(MAVEN_COMPILER_TARGET_PROPERTY);

			if (sourcePropertyValue != null && sourcePropertyValue.equals(targetPropertyValue)){
				buildFile.setProperty(JAVA_VERSION_PROPERTY, sourcePropertyValue);
				buildFile.deleteProperty(MAVEN_COMPILER_SOURCE_PROPERTY);
				buildFile.deleteProperty(MAVEN_COMPILER_TARGET_PROPERTY);
			} // If only maven property exists

		});
	}

}
