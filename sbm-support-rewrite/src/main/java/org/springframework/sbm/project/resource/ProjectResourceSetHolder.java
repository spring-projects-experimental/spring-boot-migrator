/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.project.resource;

import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;

import java.util.ArrayList;

// TODO: Making ProjectResourceSet a scoped bean would allow to remove this class.
@RequiredArgsConstructor
public class ProjectResourceSetHolder {

	private ProjectResourceSet projectResourceSet;

	private final ExecutionContext executionContext;

	private final RewriteMigrationResultMerger migrationResultMerger;

	public void setProjectResourceSet(ProjectResourceSet projectResourceSet) {
		this.projectResourceSet = projectResourceSet;
	}

	public ProjectResourceSet getProjectResourceSet() {
		return projectResourceSet == null
				? new ProjectResourceSet(new ArrayList<>(), executionContext, migrationResultMerger)
				: projectResourceSet;
	}

}
