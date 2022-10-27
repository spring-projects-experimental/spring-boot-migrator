package org.springframework.sbm.boot.cleanup.actions;

import java.util.Map;

import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

public class RemoveRedundantMavenCompilerPluginProperties extends AbstractAction {

	private static final String GROUP_ID = "org.apache.maven.plugins";

	private static final String ARTIFACT_ID = "maven-compiler-plugin";

	private static final String JAVA_VERSION = "java.version";

	private static final String MAVEN_COMPILER_SOURCE = "${maven.compiler.source}";

	private static final String MAVEN_COMPILER_TARGET = "${maven.compiler.target}";

	@Override
	public void apply(ProjectContext context) {

		Map<String, Object> configurationMap = context.getBuildFile()
				.getPluginConfiguration(GROUP_ID, ARTIFACT_ID);

		if (configurationMap == null) {
			return;
		}

		String sourceValue = (String) configurationMap.get("source");
		String targetValue = (String) configurationMap.get("target");

		if (sourceValue != null && targetValue != null) {

			String sourceLookupValue = sourceValue.startsWith("${") ?
					context.getBuildFile().getProperty(
							sourceValue.replace("${", "").replace("}", "")
					) : sourceValue;

			String targetLookupValue = targetValue.startsWith("${") ?
					context.getBuildFile().getProperty(
							targetValue.replace("${", "").replace("}", "")
					) : targetValue;

			if (sourceLookupValue != null && sourceLookupValue.equals(targetLookupValue)) {
				context.getBuildFile().setProperty(JAVA_VERSION, sourceLookupValue);
				configurationMap.put("source", MAVEN_COMPILER_SOURCE);
				configurationMap.put("target", MAVEN_COMPILER_TARGET);
				if(sourceValue.startsWith("${")){
					context.getBuildFile()
							.removePropertyAndReplaceAllOccurrences(sourceValue, JAVA_VERSION);
				}
				if (targetValue.startsWith("${")){
					context.getBuildFile()
							.removePropertyAndReplaceAllOccurrences(targetValue, JAVA_VERSION);
				}
				context.getBuildFile().changeMavenPluginConfiguration(GROUP_ID, ARTIFACT_ID, configurationMap);
			}
		}

	}

}
