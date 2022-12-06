package org.springframework.sbm.build.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Plugin {

	String getGroupId();

	String getArtifactId();

	String getVersion();

	String getDependencies();

	List<? extends Execution> getExecutions();

	Configuration getConfiguration();

	interface Configuration {

		Optional<String> getDeclaredStringValue(String property);

		String getResolvedStringValue(String property);

		void setDeclaredStringValue(String property, String value);

		Map<String,Object> getConfiguration();

	}

	interface Execution {

		String getId();

		List<String> getGoals();

		String getPhase();

		String getConfiguration();

	}

}