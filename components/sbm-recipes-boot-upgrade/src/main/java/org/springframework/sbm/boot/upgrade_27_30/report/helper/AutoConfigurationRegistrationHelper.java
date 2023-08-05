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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.sbm.boot.common.conditions.IsSpringBootProject;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSectionHelper;
import org.springframework.sbm.common.filter.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.ProjectResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Fabian Krüger
 */
public class AutoConfigurationRegistrationHelper extends SpringBootUpgradeReportSectionHelper<List<Pair<Properties, ProjectResource>>> {

    public static final String VERSION_PATTERN = "(2\\.7\\..*)|(3\\.0\\..*)";

    private static final String SPRING_FACTORIES_PATH = "/**/src/**/resources/META-INF/spring.factories";

    private Map<String, List<Pair<Properties, ProjectResource>>> matchingSpringFactoriesFiles;

    @Override
    public boolean evaluate(ProjectContext context) {
        IsSpringBootProject isSpringBootProjectCondition = new IsSpringBootProject();
        isSpringBootProjectCondition.setVersionPattern(VERSION_PATTERN);
        boolean isSpringBoot3Application = isSpringBootProjectCondition.evaluate(context);
        if(! isSpringBoot3Application) {
            return false;
        }

        Optional<Pair<Properties, ProjectResource>> props = getSpringFactoriesProperties(context);

        if (props.isEmpty()) {
            return false;
        }

        this.matchingSpringFactoriesFiles = Map.of("matchingSpringFactoriesFiles", List.of(props.get()));
        return true;
    }

    @Override
    public Map<String, List<Pair<Properties, ProjectResource>>> getData() {
        return matchingSpringFactoriesFiles;
    }


    private Optional<Pair<Properties, ProjectResource>> getSpringFactoriesProperties(ProjectContext context) {
        List<ProjectResource> search = context.search(
                new PathPatternMatchingProjectResourceFinder(
                        SPRING_FACTORIES_PATH
                ));

        if (search.size() > 0) {
            String oldConfigFile = search.get(0).print();
            Properties prop = new Properties();

            try {
                prop.load(new ByteArrayInputStream(oldConfigFile.getBytes()));
                return Optional.of(new ImmutablePair<>(prop, search.get(0)));
            } catch (IOException e) {
                throw new RuntimeException("Error whilst reading property file " + SPRING_FACTORIES_PATH, e);
            }
        }
        return Optional.empty();
    }
}
