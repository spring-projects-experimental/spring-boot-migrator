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

import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSectionHelper;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Fabian Krüger
 */
public class JohnzonDependencyHelper extends SpringBootUpgradeReportSectionHelper<List<String>> {

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean evaluate(ProjectContext context) {

        Optional<Dependency> d = context.getBuildFile().getDeclaredDependencies().stream()
                .filter(x -> x.getCoordinates().contains("org.apache.johnzon:johnzon-core")).findFirst();
        return d.isPresent();
    }

    @Override
    public Map<String, List<String>> getData() {
        return Map.of();
    }
}
