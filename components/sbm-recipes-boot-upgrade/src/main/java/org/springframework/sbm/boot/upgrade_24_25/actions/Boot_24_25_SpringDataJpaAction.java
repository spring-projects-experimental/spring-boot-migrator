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
package org.springframework.sbm.boot.upgrade_24_25.actions;

import org.springframework.sbm.boot.common.finder.MatchingMethod;
import org.springframework.sbm.boot.common.finder.MethodPatternMatchingMethod;
import org.springframework.sbm.boot.upgrade_24_25.conditions.Boot_24_25_SpringDataJpaActionCondition;
import org.springframework.sbm.boot.upgrade_24_25.filter.SpringDataJpaAnalyzer;
import org.springframework.sbm.build.MultiModuleApplicationNotSupportedException;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.MethodCall;

import java.util.List;

public class Boot_24_25_SpringDataJpaAction extends AbstractAction {

    private final SpringDataJpaAnalyzer springDataJpaAnalyzer = new SpringDataJpaAnalyzer();


    @Override
    public boolean isApplicable(ProjectContext context) {
        return new Boot_24_25_SpringDataJpaActionCondition().evaluate(context);
    }

    @Override
    public void apply(ProjectContext context) {

        if (context.getApplicationModules().isSingleModuleApplication()) {
            applyToModule(context);
        } else {
            throw new MultiModuleApplicationNotSupportedException("Action can only be applied to applications with single module.");
        }

    }

    private void applyToModule(ProjectContext context) {

        List<MethodPatternMatchingMethod> jpaRepositoriesWithGetByIdMethod = springDataJpaAnalyzer.getJpaRepositoriesWithGetByIdMethod(context);
        renameGetByIdMethods(jpaRepositoriesWithGetByIdMethod);

        List<MethodCall> callsToGetOneMethods = springDataJpaAnalyzer.findCallsToGetOneMethod(context);
        refactorCallsToGetOne(callsToGetOneMethods);

    }

    private void renameGetByIdMethods(List<MethodPatternMatchingMethod> jpaRepositoriesWithGetByIdMethod) {
        jpaRepositoriesWithGetByIdMethod.forEach(repo -> {
            String methodPattern = "com.example.springboot24to25example.TaskRepository " + repo.getMethodPattern();
            String newMethodName = "get" + repo.getMethod().getReturnValue() + "ById";
            repo.getMethod().rename(methodPattern, newMethodName);
        });
    }

    private void refactorCallsToGetOne(List<MethodCall> callsToGetOneMethods) {
        callsToGetOneMethods.forEach(c -> c.getJavaSource()
                // FIXME: calculate target type
                .renameMethodCalls(/*c.getMethodMatcher().getTargetTypePattern() + */"com.example.springboot24to25example.TagRepository " + c.getMethodMatcher().getMethodNamePattern() + "(" + c.getMethodMatcher().getArgumentPattern() + ")", "getById")
        );
    }

}
