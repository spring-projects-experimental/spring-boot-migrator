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
package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.springframework.sbm.boot.common.conditions.IsSpringBootProject;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportAction;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSection;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;

import java.util.List;
import java.util.Map;

/**
 * @author Fabian Krüger
 */
public class UpgradeDependenciesHelper implements SpringBootUpgradeReportSection.Helper<List<String>> {

    public static final String VERSION_PATTERN = "(2\\.7\\..*)|(3\\.0\\..*)";
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

        // FIXME: dummy
        return true;
    }

    @Override
    public Map<String, List<String>> getData() {
        // FIXME: dummy
        return Map.of("ehcache", List.of("org.ehcache:ehcache:3.10.0"));
    }
}
