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
package org.springframework.sbm.mule.actions.javadsl.translators;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.sbm.java.util.Helper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Keeps all information for a snippet of Spring Integration DSL.
 */

@Getter
@RequiredArgsConstructor
public class DslSnippet {

    private final String renderedSnippet;

    /**
     * Imports for the types required for this snippet
     */
    private final Set<String> requiredImports;

    /**
     * Dependencies required to be added to the classpath
     * <p>
     * the dependencies mst be provided as Maven coordinates
     */
    private final Set<String> requiredDependencies;

    private final Set<Bean> beans;

    private final boolean isUnknownStatement;

    private final String externalClassContent;

    public DslSnippet(String renderedSnippet,
                      Set<String> requiredImports) {
        this(renderedSnippet, requiredImports, Collections.emptySet(), Collections.emptySet(), false, "");
    }

    public DslSnippet(String renderedSnippet,
                      Set<String> requiredImports,
                      Set<String> requiredDependencies,
                      Set<Bean> beans) {
        this(renderedSnippet, requiredImports, requiredDependencies, beans, false, "");
    }

    public DslSnippet(String renderedSnippet,
                      Set<String> requiredImports,
                      Set<String> requiredDependencies,
                      String externalClassContent) {
        this(renderedSnippet, requiredImports, requiredDependencies, Collections.emptySet(), false, externalClassContent);
    }

    public static String renderMethodParameters(List<DslSnippet> dslSnippets) {
        return dslSnippets.stream()
                .flatMap(dsl -> dsl.getBeans().stream())
                .collect(Collectors.toSet())
                .stream()
                .map(b -> b.getBeanClass() + " " + b.getBeanName())
                .collect(Collectors.joining(", "));
    }
}
