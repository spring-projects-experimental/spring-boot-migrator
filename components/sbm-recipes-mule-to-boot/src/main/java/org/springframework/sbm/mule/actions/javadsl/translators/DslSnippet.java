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

import java.util.Collections;
import java.util.Set;

/**
 * Keeps all information for a snippet of Spring Integration DSL.
 *
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
     *
     * the dependencies mst be provided as Maven coordinates
     */
    private final Set<String> requiredDependencies;

    private final Set<RequiredBean> requiredBeans;

    private final boolean isUnknownStatement;

    public DslSnippet(String renderedSnippet,
                      Set<String> requiredImports) {
        this(renderedSnippet, requiredImports, Collections.emptySet(), Collections.emptySet(), false);
    }

    public DslSnippet(String renderedSnippet,
                      Set<String> requiredImports,
                      Set<String> requiredDependencies,
                      Set<RequiredBean> requiredBeans) {
        this(renderedSnippet, requiredImports, requiredDependencies, requiredBeans, false);
    }
}
