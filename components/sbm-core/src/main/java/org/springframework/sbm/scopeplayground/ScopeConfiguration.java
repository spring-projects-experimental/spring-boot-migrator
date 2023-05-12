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

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Fabian Krüger
 */
@Configuration
public class ScopeConfiguration {
/*
    @Bean(name ="executionContext")
//    @RecipeScope
    @Scope(value = "recipeScope", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ExecutionContext executionContext() {
        String s = UUID.randomUUID().toString();
        System.out.println("Create ExecutionContext: " + s);
        RewriteExecutionContext executionContext = new RewriteExecutionContext();
        executionContext.putMessage("contextId", s);
        return executionContext;
    }*/

    /**
     * Register scanScope
     *
     * See https://github.com/spring-projects/spring-batch/blob/c4ad90b9b191b94cb4d21f90e5759bda9857e341/spring-batch-core/src/main/java/org/springframework/batch/core/scope/BatchScopeSupport.java#L104}
     */
    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor(ExecutionRuntimeScope executionRuntimeScope, ScanRuntimeScope scanRuntimeScope) {
        return beanFactory -> {
            beanFactory.registerScope(ScanRuntimeScope.SCOPE_NAME, scanRuntimeScope);
            beanFactory.registerScope(ExecutionRuntimeScope.SCOPE_NAME, executionRuntimeScope);
        };
    }

}
