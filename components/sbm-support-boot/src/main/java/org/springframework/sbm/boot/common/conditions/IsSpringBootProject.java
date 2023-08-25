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
package org.springframework.sbm.boot.common.conditions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

public class IsSpringBootProject implements Condition {
    private HasDecalredSpringBootStarterParent parentCondition;
    private HasSpringBootDependencyImport importCondition;
    private HasSpringBootDependencyManuallyManaged manualManagedCondition;

    public void setVersionPattern(String versionPattern) {
        parentCondition = new HasDecalredSpringBootStarterParent();
        parentCondition.setVersionPattern(versionPattern);

        importCondition = new HasSpringBootDependencyImport();
        importCondition.setVersionPattern(versionPattern);

        manualManagedCondition = new HasSpringBootDependencyManuallyManaged();
        manualManagedCondition.setVersionPattern(versionPattern);
    }

    @Override
    public String getDescription() {
        return "Checks if scanned project is Spring Boot project";
    }

    @Override
    public boolean evaluate(ProjectContext context) {

        return parentCondition.evaluate(context) ||
                importCondition.evaluate(context) ||
                manualManagedCondition.evaluate(context);
    }
}
