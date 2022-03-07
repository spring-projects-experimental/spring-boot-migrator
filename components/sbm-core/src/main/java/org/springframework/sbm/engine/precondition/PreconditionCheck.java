package org.springframework.sbm.engine.precondition;


import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

abstract class PreconditionCheck {

	private ResultState resultState;

	public abstract PreconditionCheckResult verify(Path projectRoot, List<Resource> projectResources);

	public enum ResultState {
		WARN, FAILED, PASSED;
	}


	protected Path getPath(Resource r) {
		try {
			return r.getFile().toPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
