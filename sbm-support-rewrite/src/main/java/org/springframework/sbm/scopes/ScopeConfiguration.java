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
package org.springframework.sbm.scopes;

import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.cache.MavenPomCache;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

/**
 * @author Fabian Krüger
 */
@Configuration
public class ScopeConfiguration {
    /**
     * Register {@link ScanScope} and {@link ExecutionScope}.
     */
    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor(ExecutionScope executionScope, ScanScope scanScope) {
        return beanFactory -> {
            beanFactory.registerScope(ScanScope.SCOPE_NAME, scanScope);
            beanFactory.registerScope(ExecutionScope.SCOPE_NAME, executionScope);
        };
    }

    @Bean
    ProjectMetadata projectMetadata() {
        return new ProjectMetadata();
    }

    @Bean
    @ConditionalOnMissingBean(name = "executionContextSupplier")
    Supplier<ExecutionContext> executionContextSupplier() {
        return () -> new InMemoryExecutionContext(t -> {throw new RuntimeException(t);});
    }

    @Bean
    @org.springframework.sbm.scopes.annotations.ExecutionScope
    ExecutionContext executionContext(ProjectMetadata projectMetadata, Supplier<ExecutionContext> executionContextSupplier, MavenPomCache mavenPomCache) {
        ExecutionContext executionContext = executionContextSupplier.get();
        MavenExecutionContextView contextView = MavenExecutionContextView.view(executionContext);
        contextView.setMavenSettings(projectMetadata.getMavenSettings());
        contextView.setPomCache(mavenPomCache);
        return executionContext;
    }

}
