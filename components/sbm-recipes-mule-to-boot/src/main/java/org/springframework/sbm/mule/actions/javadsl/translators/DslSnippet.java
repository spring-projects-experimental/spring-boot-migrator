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

import lombok.Builder;
import lombok.Getter;
import org.springframework.sbm.mule.api.toplevel.AbstractTopLevelElement;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Keeps all information for a snippet of Spring Integration DSL.
 */

@Getter
@Builder(toBuilder=true)
public class DslSnippet {

    private String renderedSnippet;

    @Builder.Default
    private Set<String> requiredImports = Collections.emptySet();

    /**
     * Dependencies required to be added to the classpath
     * <p>
     * the dependencies must be provided as Maven coordinates
     */
    @Builder.Default
    private Set<String> requiredDependencies = Collections.emptySet();

    @Builder.Default
    private Set<Bean> beans = Collections.emptySet();

    private boolean isUnknownStatement;

    @Builder.Default
    private String externalClassContent = "";

    @Builder.Default
    private String renderedDependentFlows = "";

    public static String renderMethodParameters(List<DslSnippet> dslSnippets) {
        return dslSnippets.stream()
                .flatMap(dsl -> dsl.getBeans().stream())
                .distinct()
                .map(b -> b.getBeanClass() + " " + b.getBeanName())
                .collect(Collectors.joining(", "));
    }

    public static DslSnippet createDSLSnippetFromTopLevelElement(AbstractTopLevelElement topLevelElement) {

        Set<Bean> beans = new HashSet<>();
        Set<String> requiredImports = new HashSet<>();
        Set<String> dependencies = new HashSet<>();
        topLevelElement
                .getDslSnippets()
                .forEach(dslSnippet -> {
                    beans.addAll(dslSnippet.getBeans());
                    requiredImports.addAll(dslSnippet.getRequiredImports());
                    dependencies.addAll(dslSnippet.getRequiredDependencies());
                    if (dslSnippet.getExternalClassContent() != null
                            && !dslSnippet.getExternalClassContent().isBlank()) {
                    }
                });

        Optional<String> optionalExternalClassContent = topLevelElement
                .getDslSnippets()
                .stream()
                .map(DslSnippet::getExternalClassContent)
                .filter(k -> k !=null && !k.isBlank())
                .findFirst();

        return DslSnippet.builder()
                .beans(beans)
                .requiredImports(requiredImports)
                .requiredDependencies(dependencies)
                .externalClassContent(optionalExternalClassContent.orElse(null))
                .build();
    }
}
