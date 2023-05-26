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
package org.springframework.sbm.scopeplayground;

import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.MavenExecutionContextView;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;

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
    @org.springframework.sbm.scopeplayground.annotations.ExecutionScope
    ExecutionContext executionContext(ProjectMetadata projectMetadata) {
        RewriteExecutionContext rewriteExecutionContext = new RewriteExecutionContext();
        MavenExecutionContextView.view(rewriteExecutionContext).setMavenSettings(projectMetadata.getMavenSettings());
        return rewriteExecutionContext;
    }

}
