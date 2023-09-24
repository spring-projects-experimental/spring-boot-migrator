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
package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.openrewrite.ExecutionContext;
import org.openrewrite.java.search.UsesType;
import org.springframework.sbm.boot.common.conditions.IsSpringBootProject;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSectionHelper;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.impl.OpenRewriteJavaSource;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
public class SpringMVCAndWebFluxUrlMatchingChangesHelper extends SpringBootUpgradeReportSectionHelper<List<JavaSource>> {

    public static final String VERSION_PATTERN = "(2\\.7\\..*)|(3\\.0\\..*)";
    private static final String SPRING_REST_CONTROLLER_FQN = "org.springframework.web.bind.annotation.RestController";
    private List<JavaSource> matches = new ArrayList<>();

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        IsSpringBootProject isSpringBootProject = new IsSpringBootProject();
        isSpringBootProject.setVersionPattern(VERSION_PATTERN);
        boolean isSpringBootApplication = isSpringBootProject.evaluate(context);
        if(!isSpringBootApplication) {
            return false;
        }

        GenericOpenRewriteRecipe<UsesType<ExecutionContext>> usesTypeRecipe = new GenericOpenRewriteRecipe<>(() -> new UsesType<>(SPRING_REST_CONTROLLER_FQN, false));

        matches = context.getProjectJavaSources().find(usesTypeRecipe).stream()
                .filter(m -> OpenRewriteJavaSource.class.isInstance(m))
                .map(OpenRewriteJavaSource.class::cast)
                .sorted(Comparator.comparing(RewriteSourceFileHolder::getAbsolutePath))
                .collect(Collectors.toList());

        return !matches.isEmpty();
    }

    @Override
    public Map<String, List<JavaSource>> getData() {
        Map<String, List<JavaSource>> restControllerClasses = new HashMap<>();
        restControllerClasses.put("restControllers", matches);
        return restControllerClasses;
    }
}
