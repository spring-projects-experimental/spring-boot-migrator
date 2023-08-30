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
import org.springframework.sbm.boot.common.conditions.IsSpringBootProject;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSection;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSectionHelper;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.ProjectResource;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
public class ActuatorEndpointsSanitizationHelper extends SpringBootUpgradeReportSectionHelper<List<BuildFile>> {

    private static final String ACTUATOR_GROUP_ID = "org.springframework.boot";
    private static final String ACTUATOR_ARTIFACT_ID = "spring-boot-actuator";
    public static final String VERSION_PATTERN = "(2\\.7\\..*)|(3\\.0\\..*)";
    private List<BuildFile> buildFilesWithActuatorOnClasspath;

    @Override
    public boolean evaluate(ProjectContext context) {
        IsSpringBootProject isSpringBootProjectCondition = new IsSpringBootProject();
        isSpringBootProjectCondition.setVersionPattern(VERSION_PATTERN);
        boolean isSpringBoot3Application = isSpringBootProjectCondition.evaluate(context);
        if(! isSpringBoot3Application) {
            return false;
        }
        buildFilesWithActuatorOnClasspath = getActuatorDependency(context);
        return ! buildFilesWithActuatorOnClasspath.isEmpty();
    }

    private List<BuildFile> getActuatorDependency(ProjectContext context) {
        return context.getApplicationModules().stream()
                .map(Module::getBuildFile)
                .filter(b -> b.getEffectiveDependencies().stream().anyMatch(d -> d.getGroupId().equals(ACTUATOR_GROUP_ID) && d.getArtifactId().equals(ACTUATOR_ARTIFACT_ID)))
                .sorted(Comparator.comparing(ProjectResource::getSourcePath))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<BuildFile>> getData() {
        return Map.of("matchingBuildFiles", buildFilesWithActuatorOnClasspath);
    }
}
