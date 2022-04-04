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
package org.springframework.sbm.engine.precondition;


import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public abstract class PreconditionCheck {

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
