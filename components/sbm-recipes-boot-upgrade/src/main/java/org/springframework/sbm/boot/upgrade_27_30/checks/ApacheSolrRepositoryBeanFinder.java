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

package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.springframework.sbm.java.api.JavaSourceAndType;
import org.springframework.sbm.java.impl.OpenRewriteJavaSource;
import org.springframework.sbm.java.impl.OpenRewriteType;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ApacheSolrRepositoryBeanFinder implements ProjectResourceFinder<List<JavaSourceAndType>> {
    private static final String SOLR_REPOSITORY_CLASS = "org.springframework.data.solr.repository.SolrCrudRepository";

    @Override
    public List<JavaSourceAndType> apply(ProjectResourceSet projectResourceSet) {
        return projectResourceSet.stream()
                .filter(OpenRewriteJavaSource.class::isInstance)
                .map(OpenRewriteJavaSource.class::cast)
                .filter(js -> js.getTypes().stream().anyMatch(t -> t.getFullyQualifiedName().equals(SOLR_REPOSITORY_CLASS)))
                .map(js -> {
                    Optional<OpenRewriteType> type = js.getTypes().stream()
                            .filter(t -> t.getFullyQualifiedName().equals(SOLR_REPOSITORY_CLASS))
                            .findFirst();
                    if(type.isPresent()) {
                        return new JavaSourceAndType(js, type.get());
                    } else {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }
}
