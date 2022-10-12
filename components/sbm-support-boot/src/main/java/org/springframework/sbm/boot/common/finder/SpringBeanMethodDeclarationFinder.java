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

package org.springframework.sbm.boot.common.finder;

import lombok.RequiredArgsConstructor;
import org.springframework.sbm.java.filter.JavaSourceListFilter;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds {@code JavaSource}s with {@code Type}s declaring a Spring bean method with return type of {@code returnValueFqName}
 */
@RequiredArgsConstructor
public class SpringBeanMethodDeclarationFinder implements ProjectResourceFinder<List<MatchingMethod>> {

    public static final String SPRING_BEAN_ANNOTATION = "org.springframework.context.annotation.Bean";
    private final String returnValueFqName;

    @Override
    public List<MatchingMethod> apply(ProjectResourceSet projectResourceSet) {

        List<MatchingMethod> matches = new ArrayList<>();

        new JavaSourceListFilter()
                .apply(projectResourceSet)
                .stream()
                .filter(js -> js.hasImportStartingWith(SPRING_BEAN_ANNOTATION))
                .forEach(js -> {
                    js
                            .getTypes()
                            .stream()
                            .filter(t -> t
                                    .getMethods()
                                    .stream()
                                    .anyMatch(m -> m.getReturnValue().isPresent() &&
                                                    m.getReturnValue().get().equals(returnValueFqName) &&
                                                    m.hasAnnotation(SPRING_BEAN_ANNOTATION)
                                    ))
                            .forEach(t -> t
                                    .getMethods()
                                    .stream()
                                    .filter(m -> m.getReturnValue().get().equals(returnValueFqName))
                                    .forEach(m -> matches.add(new MatchingMethod(js, t, m))));
                });

        return matches;
    }
}
