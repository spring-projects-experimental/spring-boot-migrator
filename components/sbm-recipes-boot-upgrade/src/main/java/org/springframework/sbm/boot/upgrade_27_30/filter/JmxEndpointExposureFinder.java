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
package org.springframework.sbm.boot.upgrade_27_30.filter;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;
import org.springframework.sbm.properties.api.PropertiesSource;

import java.util.List;

public class JmxEndpointExposureFinder implements ProjectResourceFinder<List<? extends PropertiesSource>> {
    public static final String JMX_ENDPOINT_KEY = "management.endpoints.jmx.exposure.include";

    @Override
    public List<? extends PropertiesSource> apply(ProjectResourceSet projectResourceSet) {
        List<SpringBootApplicationProperties> springBootApplicationProperties = new SpringBootApplicationPropertiesResourceListFilter().apply(projectResourceSet);
        return springBootApplicationProperties.stream()
                .filter(find -> find.getProperty(JMX_ENDPOINT_KEY).isPresent())
                .toList();
    }
}
