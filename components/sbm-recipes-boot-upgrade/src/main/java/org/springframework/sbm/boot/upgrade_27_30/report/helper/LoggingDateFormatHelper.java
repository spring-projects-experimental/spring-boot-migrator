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
import org.springframework.sbm.boot.upgrade_27_30.filter.LoggingDateFormatPropertyFinder;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSection;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSectionHelper;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.properties.api.PropertiesSource;

import java.util.List;
import java.util.Map;

/**
 * @author Fabian Krüger
 */
public class LoggingDateFormatHelper extends SpringBootUpgradeReportSectionHelper<List<? extends PropertiesSource>> {

    public static final String VERSION_PATTERN = "(2\\.7\\..*)|(3\\.0\\..*)";
    private List<? extends PropertiesSource> propertiesSources;

    @Override
    public boolean evaluate(ProjectContext context) {
        IsSpringBootProject isSpringBootProjectCondition = new IsSpringBootProject();
        isSpringBootProjectCondition.setVersionPattern(VERSION_PATTERN);
        boolean isSpringBoot3Application = isSpringBootProjectCondition.evaluate(context);
        if(! isSpringBoot3Application) {
            return false;
        }

        propertiesSources = context.search(new LoggingDateFormatPropertyFinder());
        return propertiesSources.isEmpty();
    }

    @Override
    public Map<String, List<? extends PropertiesSource>> getData() {
        return Map.of("properties", propertiesSources);
    }
}
