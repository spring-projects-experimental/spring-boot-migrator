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
package org.springframework.rewrite.test.util;

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Fabian Krüger
 */
public class OpenRewriteDummyRecipeInstaller {

	public void installRecipe() {
		Invoker invoker = new DefaultInvoker();
		InvocationRequest request = new DefaultInvocationRequest();
		File dummRecipeProject = Path.of("./testcode/openrewrite-dummy-recipe").toFile();
		request.setBaseDirectory(dummRecipeProject);
		request.setGoals(List.of("clean", "install"));
		try {
			InvocationResult result = invoker.execute(request);
		}
		catch (MavenInvocationException e) {
			throw new RuntimeException(e);
		}
	}

}
