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
package org.springframework.sbm.boot.upgrade_27_30.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.rewrite.project.resource.ProjectResourceSet;
import org.springframework.rewrite.project.resource.finder.ProjectResourceFinder;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFinder;
import org.springframework.sbm.properties.api.PropertiesSource;

import java.util.List;

@Slf4j
public class LoggingDateFormatPropertyFinder implements ProjectResourceFinder<List<? extends PropertiesSource>> {

    private static final String LOGGING_DATE_FORMAT_KEY = "logging.pattern.dateformat";

    @Override
    public List<? extends PropertiesSource> apply(ProjectResourceSet projectResourceSet) {
        List<SpringBootApplicationProperties> springBootApplicationProperties = new SpringBootApplicationPropertiesResourceListFinder().apply(projectResourceSet);

        return springBootApplicationProperties.stream()
                .filter(x -> x.getProperty(LOGGING_DATE_FORMAT_KEY).isPresent())
                .toList();
    }
}
