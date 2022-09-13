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
package org.springframework.sbm.properties.api;

import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.properties.migration.recipes.AddProperty;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.properties.ChangePropertyKey;
import org.openrewrite.properties.ChangePropertyValue;
import org.openrewrite.properties.search.FindProperties;
import org.openrewrite.properties.tree.Properties;
import org.openrewrite.properties.tree.Properties.Entry;
import org.openrewrite.properties.tree.Properties.File;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// TODO: fcoi RewriteSourceFileHolder as member ?!
@Slf4j
public class PropertiesSource extends RewriteSourceFileHolder<Properties.File> {

    private final RewriteExecutionContext executionContext;

    public PropertiesSource(Path absoluteProjectDir, RewriteExecutionContext executionContext, File sourceFile) {
        super(absoluteProjectDir, sourceFile);
        this.executionContext = executionContext;
    }

    public void setProperty(String comment, String propertyName, String propertyValue) {
        if (FindProperties.find(getSourceFile(), propertyName, false).isEmpty()) {
            apply(new AddProperty(propertyName, propertyValue));
        } else {
            apply(new ChangePropertyValue(propertyName, propertyValue, null, null, null));
        }
    }

    public void setProperty(String key, String value) {
        if (FindProperties.find(getSourceFile(), key, false).isEmpty()) {
            apply(new AddProperty(key, value));
        } else {
            apply(new ChangePropertyValue(key, value, null, null, null));
        }
    }

    public void renameProperty(String oldProperyName, String newPropertyName) {
        if (!FindProperties.find(getSourceFile(), oldProperyName, false).isEmpty()) {
            apply(new ChangePropertyKey(oldProperyName, newPropertyName, null, null));
        }
    }

    public Optional<String> getProperty(String key) {
        Set<Entry> found = FindProperties.find(getSourceFile(), key, false);
        if (found.isEmpty()) {
            return Optional.empty();
        } else {
            if (found.size() > 1) {
                log.warn("Found more than one value for property " + key);
            }
            return Optional.of(found.iterator().next().getValue().getText());
        }

    }

    private void apply(Recipe r) {
        File rewriteResource = getSourceFile();
        List<Result> results = r.run(List.of(rewriteResource), executionContext).getResults();
        if (!results.isEmpty()) {
            replaceWith(getSourceFile().getClass().cast(results.get(0).getAfter()));
        }
    }

}
