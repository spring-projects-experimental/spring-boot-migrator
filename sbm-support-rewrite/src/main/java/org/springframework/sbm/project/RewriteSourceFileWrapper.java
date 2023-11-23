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
package org.springframework.sbm.project;

import org.openrewrite.SourceFile;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class RewriteSourceFileWrapper {

	public List<RewriteSourceFileHolder<? extends SourceFile>> wrapRewriteSourceFiles(Path absoluteProjectDir,
			List<SourceFile> parsedByRewrite) {
		List<RewriteSourceFileHolder<?>> rewriteProjectResources = parsedByRewrite.stream()
			.map(sf -> wrapRewriteSourceFile(absoluteProjectDir, sf))
			.collect(Collectors.toList());
		return rewriteProjectResources;
	}

	private RewriteSourceFileHolder<?> wrapRewriteSourceFile(Path absoluteProjectDir, SourceFile sourceFile) {
		RewriteSourceFileHolder<?> rewriteSourceFileHolder = new RewriteSourceFileHolder<>(absoluteProjectDir,
				sourceFile);
		return rewriteSourceFileHolder;
	}

}
