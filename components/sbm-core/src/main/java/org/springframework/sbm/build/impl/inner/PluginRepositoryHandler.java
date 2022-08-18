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

package org.springframework.sbm.build.impl.inner;

import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.build.api.RepositoryDefinition;

import java.util.List;
import java.util.stream.Collectors;

public class PluginRepositoryHandler {
    public static final String PLUGIN_REPOSITORIES = "pluginRepositories";
    public static final String PLUGIN_REPOSITORY = "pluginRepository";

    public List<RepositoryDefinition> getRepositoryDefinitions(Xml.Document sourceFile) {
        List<Xml.Tag> tags = sourceFile
                .getRoot()
                .getChild(PLUGIN_REPOSITORIES)
                .map(t -> t.getChildren(PLUGIN_REPOSITORY))
                .orElse(List.of());

        return tags.stream()
                .map(k -> k.getChild("url"))
                .map(k -> k.orElseThrow(() -> new RuntimeException("url is not set for plugin repository")).getValue())
                .map(k -> k.orElseThrow(() -> new RuntimeException("url value is not set")))
                .map(k -> RepositoryDefinition.builder().url(k).build()).collect(Collectors.toList());
    }
}
