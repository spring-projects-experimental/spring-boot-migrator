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

package org.springframework.sbm.boot.web.api;

import org.springframework.sbm.java.api.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides an API for classes annotated with {@code RestController}.
 *
 * @author  Fabian Krüger
 */
public record RestControllerBean(JavaSource js, Type restControllerType) {
    private static final RestMethodMapper restMethodMapper = new RestMethodMapper();
    public static final List<String> SPRING_REST_METHOD_ANNOTATIONS = SpringRestMethodAnnotation
            .getAll()
            .stream()
            .map(SpringRestMethodAnnotation::getFullyQualifiedName)
            .toList();

    /**
     * Return the list of methods annotated with any of the Spring Boot request mapping annotations like
     * {@code @RequestMapping} or {@code @GetMapping}.
     */
    public List<RestMethod> getRestMethods() {
        return this.restControllerType.getMethods().stream()
                .filter(this::isRestMethod)
                .map(Method.class::cast)
                .map(restMethodMapper::map)
                .collect(Collectors.toList());
    }

    private boolean isRestMethod(Method method) {
        return method.getAnnotations().stream()
                .anyMatch(methodAnnotation -> SPRING_REST_METHOD_ANNOTATIONS.contains(methodAnnotation.getFullyQualifiedName()));
    }

}
