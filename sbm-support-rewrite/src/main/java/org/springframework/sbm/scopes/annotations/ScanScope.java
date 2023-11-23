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
package org.springframework.sbm.scopes.annotations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * ## scanScope Beans annotated with
 * {@link org.springframework.sbm.scopes.annotations.ScanScope} will be created on first
 * access during scan/parse and added to the scanScope. Subsequent usages will receive
 * instances from the scanScope until the scope ends and all scoped beans get removed from
 * the scope.
 *
 * The `scanScope` starts with - parsing a given application
 *
 * The `scanScope` ends with - a new scan - or when the application stops
 *
 * @author Fabian Krüger
 */
@Qualifier
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Scope(scopeName = org.springframework.sbm.scopes.ScanScope.SCOPE_NAME, proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface ScanScope {

}
