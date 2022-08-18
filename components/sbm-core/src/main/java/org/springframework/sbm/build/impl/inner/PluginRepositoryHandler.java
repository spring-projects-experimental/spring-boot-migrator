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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PluginRepositoryHandler {
    public static final String PLUGIN_REPOSITORIES = "pluginRepositories";
    public static final String PLUGIN_REPOSITORY = "pluginRepository";

    public List<RepositoryDefinition> getRepositoryDefinitions(Xml.Document sourceFile) {
        List<Xml.Tag> tags = sourceFile
                .getRoot()
                .getChild(PLUGIN_REPOSITORIES)
                .map(t -> t.getChildren(PLUGIN_REPOSITORY))
                .orElse(List.of());

        List<RepositoryDefinition> result = new ArrayList<>();
        for (Xml.Tag t : tags) {
            RepositoryDefinition.RepositoryDefinitionBuilder builder = RepositoryDefinition.builder();
            getRepositoryAttribute(t, "url", builder::url, true);
            getRepositoryAttribute(t, "id", builder::id, true);
            getRepositoryAttribute(t, "layout", builder::layout, false);
            getRepositoryAttribute(t, "snapshots.enabled", (k) -> builder.snapshotsEnabled(Boolean.valueOf(k)), false);
            getRepositoryAttribute(t, "snapshots.checksumPolicy", builder::snapshotsChecksumPolicy, false);
            getRepositoryAttribute(t, "snapshots.updatePolicy", builder::snapShotsUpdatePolicy, false);
            getRepositoryAttribute(t, "releases.enabled", (k) -> builder.releasesEnabled(Boolean.valueOf(k)), false);
            getRepositoryAttribute(t, "releases.checksumPolicy", builder::releasesChecksumPolicy, false);
            getRepositoryAttribute(t, "releases.updatePolicy",  builder::releasesUpdatePolicy, false);
            result.add(builder.build());
        }
        return result;
    }

    private void getRepositoryAttribute(Xml.Tag tag, String attributeName, Consumer<String> initDefinition, boolean mandatory) {

        String[] hierarchy = attributeName.split("\\.");

        for (int i = 0; i < hierarchy.length - 1; i++) {
            Optional<Xml.Tag> tagOptional = tag.getChild(hierarchy[i]);

            if (tagOptional.isPresent()) {
                tag = tagOptional.get();
            }
        }

        attributeName = hierarchy[hierarchy.length - 1];
        Optional<String> attributeValue = tag.getChildValue(attributeName);
        if (mandatory && attributeValue.isEmpty()) {
            throw new RuntimeException(attributeName + " is not set for plugin repository");
        }
        attributeValue.ifPresent(initDefinition);
    }
}
