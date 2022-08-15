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
package org.springframework.sbm.boot.upgrade_24_25.conditions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.sbm.boot.common.finder.MethodPatternMatchingMethod;
import org.springframework.sbm.boot.upgrade_24_25.filter.SpringDataJpaAnalyzer;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.java.api.MethodCall;

import java.util.List;

@Slf4j
public class Boot_24_25_SpringDataJpaActionCondition implements Condition {
    @Override
    public String getDescription() {
        return "Check if getOne() is used or if getById() is declared on JpaRepositories.";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getApplicationModules().isSingleModuleApplication() && evaluateAgainstModule(context);
    }

    private boolean evaluateAgainstModule(ProjectContext context) {
        SpringDataJpaAnalyzer springDataJpaAnalyzer = new SpringDataJpaAnalyzer();
        List<MethodCall> callsToGetOneMethod = springDataJpaAnalyzer.findCallsToGetOneMethod(context);
        List<MethodPatternMatchingMethod> jpaRepositoriesWithGetByIdMethod = springDataJpaAnalyzer.getJpaRepositoriesWithGetByIdMethod(context);

        return !(callsToGetOneMethod.isEmpty() || jpaRepositoriesWithGetByIdMethod.isEmpty());
    }
}
