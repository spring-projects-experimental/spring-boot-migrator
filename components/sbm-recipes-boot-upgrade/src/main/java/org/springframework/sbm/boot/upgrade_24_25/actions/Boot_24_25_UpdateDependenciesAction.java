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
package org.springframework.sbm.boot.upgrade_24_25.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;

public class Boot_24_25_UpdateDependenciesAction extends AbstractAction {
    @Override
    public void apply(ProjectContext context) {
        if(hasSpringBootParent(context)) {
            context.getBuildFile().upgradeParentVersion("2.5.6");
        } else {
            // FIXME: Add support to upgrade spring boot in dependencyManagement section
            // FIXME: Support spring boot application not having a parent neither dependencyManagement section
            throw new RuntimeException("Upgrading Spring Boot Dependency currently only supported for projects having a spring-boot-starter-parent.");
        }
    }

    private boolean hasSpringBootParent(ProjectContext context) {
        return context.getBuildFile().hasParent();
    }

}
