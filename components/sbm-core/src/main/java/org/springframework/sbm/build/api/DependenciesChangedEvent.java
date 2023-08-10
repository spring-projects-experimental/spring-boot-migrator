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
package org.springframework.sbm.build.api;

import lombok.Getter;

import java.util.List;

/**
 * Event published when new dependencies were added to a {@link BuildFile}.
 * A listener can then use the information to recompile affected java source files.
 *
 * @author Fabian Krueger
 */
@Getter
public record DependenciesChangedEvent(
        org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile openRewriteMavenBuildFile, java.util.Map<org.openrewrite.maven.tree.Scope, List<org.openrewrite.maven.tree.ResolvedDependency>> resolvedDependencies) {
}
