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
package org.springframework.rewrite.boot.autoconfigure;

import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.maven.cache.MavenPomCache;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.rewrite.scopes.ExecutionScope;
import org.springframework.rewrite.scopes.ProjectMetadata;
import org.springframework.rewrite.scopes.ScanScope;

import java.util.function.Supplier;

/**
 * @author Fabian Krüger
 */
@AutoConfiguration
public class ScopeConfiguration {

	@Bean
	ExecutionScope executionScope() {
		return new ExecutionScope();
	}

	@Bean
	ScanScope scanScope() {
		return new ScanScope();
	}

	/**
	 * Register {@link ScanScope} and {@link ExecutionScope}.
	 */
	@Bean
	public static BeanFactoryPostProcessor beanFactoryPostProcessor(ExecutionScope executionScope,
			ScanScope scanScope) {
		return beanFactory -> {
			beanFactory.registerScope(ScanScope.SCOPE_NAME, scanScope);
			beanFactory.registerScope(ExecutionScope.SCOPE_NAME, executionScope);
		};
	}

	@Bean
	@org.springframework.rewrite.scopes.annotations.ScanScope
	ProjectMetadata projectMetadata() {
		return new ProjectMetadata();
	}

	@Bean
	@ConditionalOnMissingBean(name = "executionContextSupplier")
	Supplier<ExecutionContext> executionContextSupplier() {
		return () -> new InMemoryExecutionContext(t -> {
			throw new RuntimeException(t);
		});
	}

	@Bean
	@org.springframework.rewrite.scopes.annotations.ScanScope
	ExecutionContext executionContext(ProjectMetadata projectMetadata,
			Supplier<ExecutionContext> executionContextSupplier, MavenPomCache mavenPomCache) {
		ExecutionContext executionContext = executionContextSupplier.get();
		return executionContext;
	}

}
