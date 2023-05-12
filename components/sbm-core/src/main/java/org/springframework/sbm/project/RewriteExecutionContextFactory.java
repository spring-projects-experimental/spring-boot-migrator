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
package org.springframework.sbm.project;

import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.MavenSettings;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.scopeplayground.ProjectMetadata;
import org.springframework.stereotype.Component;

/**
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
public class RewriteExecutionContextFactory {
    private final ConfigurableListableBeanFactory beanFactory;

    public ExecutionContext createExecutionContext() {
        return beanFactory.getBean(RewriteExecutionContext.class);
    }
}
