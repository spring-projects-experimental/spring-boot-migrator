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
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;

import java.util.List;
import java.util.Map;

/**
 * @author Fabian Krüger
 */
public class UpgradeSpringBootVersionHelper extends SpringBootUpgradeReportSectionHelper<String> {

    public static final String VERSION_PATTERN = "(2\\.7\\..*)|(3\\.0\\..*)";

    @Override
    public boolean evaluate(ProjectContext context) {
        IsSpringBootProject isSpringBootProject = new IsSpringBootProject();
        isSpringBootProject.setVersionPattern(VERSION_PATTERN);
        return isSpringBootProject.evaluate(context);
    }

    @Override
    public Map<String, String> getData() {
        // FIXME: Provide correct boot version, see https://github.com/spring-projects-experimental/spring-boot-migrator/issues/560
        return Map.of("bootVersion", "2.7.x");
    }
}
