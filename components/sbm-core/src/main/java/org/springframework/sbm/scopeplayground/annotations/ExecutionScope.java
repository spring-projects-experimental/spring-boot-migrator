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
package org.springframework.sbm.scopeplayground.annotations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.sbm.engine.context.ProjectContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark beans for executionScope.
 *
 * The `executionScope` starts with
 * - the evaluation of conditions in {@link org.springframework.sbm.engine.commands.ApplicableRecipeListCommand#execute(ProjectContext)}
 * - or with a recipe-run in {@link org.springframework.sbm.engine.commands.ApplicableRecipeListCommand#execute(ProjectContext)}
 *
 * The `executionScope` ends with
 * - the end of recipe-run
 * - or when the application stops.
 *
 * @author Fabian Krüger
 */
@Qualifier
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Scope(scopeName = org.springframework.sbm.scopeplayground.ExecutionScope.SCOPE_NAME, proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface ExecutionScope {
}
