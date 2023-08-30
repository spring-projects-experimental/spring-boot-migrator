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
package org.springframework.sbm.boot.upgrade_27_30.report;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
@Component
public class SpringBootUpgradeReportDataProvider implements SpringBootUpgradeReportAction.DataProvider {
    @Override
    public Map<String, Object> getData(ProjectContext context, @Valid List<SpringBootUpgradeReportSection> sections) {
        Map<String, Object> data = new HashMap<>();

        data.put("timestamp", Instant.now().toString());
        Set<SpringBootUpgradeReportSection.Author> authors = sections
                .stream()
                .flatMap(s -> s.getAuthors().stream())
                .collect(Collectors.toSet());
        data.put("contributors", authors);

        String scannedCoordinate = context.getApplicationModules().getRootModule().getBuildFile().getCoordinates();
        data.put("scannedCoordinate", scannedCoordinate);

        data.put("scannedProjectRoot", context.getProjectRootDirectory());

        data.put("revision", context.getRevision());

        if(context.getBuildFile().getName().isPresent()) {
            data.put("projectName", context.getBuildFile().getName().get());
        }

        // FIXME: results in all conditons for all sections being evaluated twice
        data.put("numberOfChanges", sections.stream().filter(s -> s.shouldRender(context)).count());

        // FIXME: Retrieve Boot version from Finder
        data.put("bootVersion", "2.7.3");

        return data;
    }
}
