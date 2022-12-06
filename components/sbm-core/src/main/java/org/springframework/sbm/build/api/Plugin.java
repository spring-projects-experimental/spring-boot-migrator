package org.springframework.sbm.build.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

		Set<String> getPropertyKeys();

	}

	interface Execution {

		String getId();

		List<String> getGoals();

		String getPhase();

		String getConfiguration();

	}

}