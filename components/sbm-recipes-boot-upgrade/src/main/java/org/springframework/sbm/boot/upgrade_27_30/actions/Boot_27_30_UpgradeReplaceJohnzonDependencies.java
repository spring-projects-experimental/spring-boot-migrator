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
package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.springframework.sbm.boot.upgrade_27_30.conditions.JohnzonDependencyCondition;
import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

public class Boot_27_30_UpgradeReplaceJohnzonDependencies extends AbstractAction {

    private static final String JOHNZON_DEPENDENCY_PATTERN = "org\\.apache\\.johnzon\\:johnzon-core\\:.*";
    private static final String JOHNZON_DEPENDENCY = "org.apache.johnzon:johnzon-core:1.2.18:jakarta";

    @Override
    public void apply(ProjectContext context) {

        context.getApplicationModules()
                .stream()
                .map(ApplicationModule::getBuildFile)
                .forEach(bf -> {
                    bf.removeDependenciesMatchingRegex(JOHNZON_DEPENDENCY_PATTERN);
                    bf.addDependency(Dependency.fromCoordinates(JOHNZON_DEPENDENCY));
                });
    }

    @Override
    public boolean isApplicable(ProjectContext context) {
        return new JohnzonDependencyCondition().evaluate(context);
    }

}
