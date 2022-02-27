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
package org.springframework.sbm.jee.jsf.conditions;

import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.migration.conditions.NoExactDependencyExist;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.java.migration.conditions.HasImportStartingWith;
import org.springframework.sbm.engine.context.ProjectContext;

public class IsMigrateJsf2ToSpringBootApplicableCondition implements Condition {
    @Override
    public String getDescription() {
        return "Check if recipe 'migrate-jsf-2.x-to-spring-boot' is applicable";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        Dependency dependency = Dependency.builder()
                .groupId("org.joinfaces")
                .artifactId("jsf-spring-boot-starter")
                .build();
        NoExactDependencyExist noExactDependencyExistCondition = new NoExactDependencyExist(dependency);
        HasImportStartingWith hasImportStartingWithCondition = new HasImportStartingWith("javax.faces", "Search for imports starting with 'javax.faces'");

        return noExactDependencyExistCondition.evaluate(context) && hasImportStartingWithCondition.evaluate(context);
    }
}
