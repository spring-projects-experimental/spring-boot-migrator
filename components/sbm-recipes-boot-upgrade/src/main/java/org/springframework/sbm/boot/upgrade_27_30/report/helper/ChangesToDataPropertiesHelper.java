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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.sbm.boot.common.conditions.IsSpringBootProject;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSection;
import org.springframework.sbm.build.migration.conditions.NoDependencyExistMatchingRegex;
import org.springframework.sbm.engine.context.ProjectContext;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
public class ChangesToDataPropertiesHelper implements SpringBootUpgradeReportSection.Helper<List<ChangesToDataPropertiesHelper.Match>> {

    public static final String VERSION_PATTERN = "(2\\.7\\..*)|(3\\.0\\..*)";

    private Map<String, List<Match>> data = new HashMap<>();

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        IsSpringBootProject isSpringBootProjectCondition = new IsSpringBootProject();
        isSpringBootProjectCondition.setVersionPattern(VERSION_PATTERN);
        boolean isSpringBoot3Application = isSpringBootProjectCondition.evaluate(context);
        if(! isSpringBoot3Application) {
            return false;
        }


        boolean noDepExists = new NoDependencyExistMatchingRegex(List.of("org\\.springframework\\.data\\:.*")).evaluate(
                context);
        List<SpringBootApplicationProperties> search = context
                .search(new SpringBootApplicationPropertiesResourceListFilter());

        data = new HashMap<>();

        search.forEach(p -> {
            Path absolutePath = p.getAbsolutePath();
            List<Object> propertiesFound = p
                    .getProperties()
                    .keySet()
                    .stream()
                    .filter(k -> k.toString().startsWith("spring.data."))
                    .collect(Collectors.toList());
            if (!propertiesFound.isEmpty()) {
                if (data.containsKey("matches")) {
                    data.get("matches").add(new Match(absolutePath.toString(), p.getSourcePath().toString(), propertiesFound));
                } else {
                    List<Match> matches = new ArrayList<>();
                    matches.add(new Match(absolutePath.toString(), p.getSourcePath().toString(), propertiesFound));
                    data.put("matches", matches);
                }
            }

        });
        return noDepExists && !data.isEmpty();
    }

    @Override
    public Map<String, List<Match>> getData() {
        return data;
    }

    @RequiredArgsConstructor
    @Getter
    public class Match {
        private final String absolutePath;
        private final String relativePath;
        private final List<Object> propertiesFound;
    }
}
