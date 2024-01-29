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

import org.springframework.rewrite.resource.ProjectResource;
import org.springframework.rewrite.resource.finder.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSectionHelper;
import org.springframework.sbm.engine.context.ProjectContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SpringFactoriesHelper extends SpringBootUpgradeReportSectionHelper<List<String>>  {

    private List<String> files;

    @Override
    public boolean evaluate(ProjectContext context) {
        List<ProjectResource> search = context
                .search(
                        new PathPatternMatchingProjectResourceFinder(
                                "/**/src/main/resources/META-INF/spring.factories"
                        ));

        files = search.stream().map(k -> k.getAbsolutePath().toString()).toList();

        return search
                .stream()
                .anyMatch(r -> isRightProperty(r.print()));
    }

    private boolean isRightProperty(String propertyString) {

        Properties prop = new Properties();
        try {
            prop.load(new ByteArrayInputStream(propertyString.getBytes()));
            String enableAutoConfig = prop.getProperty("org.springframework.boot.autoconfigure.EnableAutoConfiguration");

            return enableAutoConfig != null;
        } catch (IOException e) {

            return false;
        }
    }

    @Override
    public Map<String, List<String>> getData() {
        return Map.of("files", files);
    }
}
