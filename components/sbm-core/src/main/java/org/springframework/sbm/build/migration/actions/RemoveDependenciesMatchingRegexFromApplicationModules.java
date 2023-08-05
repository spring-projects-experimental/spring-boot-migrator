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
package org.springframework.sbm.build.migration.actions;

import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.migration.actions.RemoveDependenciesMatchingRegex;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.MultiModuleHandler;

public class RemoveDependenciesMatchingRegexFromApplicationModules implements MultiModuleHandler<RemoveDependenciesMatchingRegex> {

    private RemoveDependenciesMatchingRegex action;

    @Override
    public void handle(ProjectContext context) {
        context.getApplicationModules().getTopmostApplicationModules().stream()
                .map(Module::getBuildFile)
                .forEach(b -> action.getDependenciesRegex().stream().forEach(b::removeDependenciesMatchingRegex));
    }

    @Override
    public void setAction(RemoveDependenciesMatchingRegex action) {
        this.action = action;
    }
}
