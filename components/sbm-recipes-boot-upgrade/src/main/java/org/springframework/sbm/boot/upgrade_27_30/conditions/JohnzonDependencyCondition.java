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
package org.springframework.sbm.boot.upgrade_27_30.conditions;

import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

/**
 * This filter finds out if any of the resource uses Johnzon
 */
public class JohnzonDependencyCondition implements Condition {

    private static final String JOHNZON_DEPENDENCY_PATTERN = "org\\.apache\\.johnzon\\:johnzon-core\\:.*";

    @Override
    public String getDescription() {
        return "Checks if the project has declared dependency on Johnzon library";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getApplicationModules()
                .stream()
                .map(ApplicationModule::getBuildFile)
                .anyMatch(b -> b.hasDeclaredDependencyMatchingRegex(JOHNZON_DEPENDENCY_PATTERN));
    }
}
