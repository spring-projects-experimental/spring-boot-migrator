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

package org.springframework.sbm.boot.web.finder;

import org.springframework.sbm.boot.web.api.RestControllerBean;
import org.springframework.sbm.java.filter.JavaSourceListFilter;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Find all classes annotated with {@code RestController} and return {@code RestControllerBean} providing an API for rest controller.
 */
public class FindRestControllerBeans implements ProjectResourceFinder<List<RestControllerBean>> {

    private static final String REST_CONTROLLER_ANNOTATION = "org.springframework.web.bind.annotation.RestController";

    @Override
    public List<RestControllerBean> apply(ProjectResourceSet projectResourceSet) {
        return new JavaSourceListFilter().apply(projectResourceSet).stream()
                .flatMap(js -> {
                    return js
                            .getTypes()
                            .stream()
                            .filter(t -> t.hasAnnotation(REST_CONTROLLER_ANNOTATION))
                            .map(t -> new RestControllerBean(js, t));
                })
                .collect(Collectors.toList());
    }
}
