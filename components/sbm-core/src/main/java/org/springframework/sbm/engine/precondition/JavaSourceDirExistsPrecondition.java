package org.springframework.sbm.engine.precondition;

import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.List;

class JavaSourceDirExistsPrecondition extends PreconditionCheck {

	private final String JAVA_SRC_DIR = "src/main/java";

	@Override
	public PreconditionCheckResult verify(Path projectRoot, List<Resource> projectResources) {
		if (projectResources.stream()
				.noneMatch(r -> projectRoot.relativize(getPath(r)).startsWith(Path.of(JAVA_SRC_DIR).toString()))) {
			return new PreconditionCheckResult(ResultState.FAILED, "PreconditionCheck check could not find a '" + JAVA_SRC_DIR + "' dir. This dir is required.");
		}
		return new PreconditionCheckResult(ResultState.PASSED, "Found required source dir 'src/main/java'.");
	}

}
