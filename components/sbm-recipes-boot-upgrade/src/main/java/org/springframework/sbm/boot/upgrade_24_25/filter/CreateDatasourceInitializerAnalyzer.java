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
package org.springframework.sbm.boot.upgrade_24_25.filter;

import org.springframework.rewrite.resource.ProjectResource;
import org.springframework.rewrite.resource.finder.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFinder;
import org.springframework.sbm.build.api.Module;

import java.util.List;
import java.util.stream.Collectors;

public class CreateDatasourceInitializerAnalyzer {
    public List<SpringBootApplicationProperties> findPropertyFilesContainingDataProperty(Module module, List<SpringBootApplicationProperties> applicationProperties) {
        List<SpringBootApplicationProperties> sqlDataFileProperty = findPropertyFilesContainingProperty(applicationProperties, "spring.datasource.data");
        return sqlDataFileProperty;
    }

    private List<SpringBootApplicationProperties> findPropertyFilesContainingProperty(List<SpringBootApplicationProperties> applicationProperties, String propertyName) {
        return applicationProperties.stream()
                .filter(p -> p.getProperty(propertyName).isPresent())
                .collect(Collectors.toList());
    }

    public List<SpringBootApplicationProperties> findPropertyFilesContainingSchemaProperty(Module module, List<SpringBootApplicationProperties> applicationProperties) {
        return findPropertyFilesContainingProperty(applicationProperties, "spring.datasource.schema");
    }

    public List<ProjectResource> findSchemaAndDataFiles(Module module) {
        return module.search(new PathPatternMatchingProjectResourceFinder(List.of("**/resources/**/schema.sql", "**/resources/**/data.sql")));
    }

    public List<SpringBootApplicationProperties> findPropertyFilesContainingDataUsernameProperty(Module module) {
        List<SpringBootApplicationProperties> applicationProperties = module.search(new SpringBootApplicationPropertiesResourceListFinder());
        return findPropertyFilesContainingProperty(applicationProperties, "spring.datasource.data-username");
    }

    public List<SpringBootApplicationProperties> findPropertyFilesContainingDataPasswordProperty(Module context) {
        List<SpringBootApplicationProperties> applicationProperties = context.search(new SpringBootApplicationPropertiesResourceListFinder());
        return findPropertyFilesContainingProperty(applicationProperties, "spring.datasource.data-password");
    }

    public List<SpringBootApplicationProperties> findPropertyFilesContainingSchemaUsernameProperty(Module context) {
        List<SpringBootApplicationProperties> applicationProperties = context.search(new SpringBootApplicationPropertiesResourceListFinder());
        return findPropertyFilesContainingProperty(applicationProperties, "spring.datasource.schema-username");
    }
}
