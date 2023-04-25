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
package org.springframework.sbm.properties.migration.recipes;

import lombok.EqualsAndHashCode;
import org.openrewrite.*;
import org.openrewrite.marker.Markers;
import org.openrewrite.properties.PropertiesVisitor;
import org.openrewrite.properties.tree.Properties;
import org.openrewrite.properties.tree.Properties.Content;
import org.openrewrite.properties.tree.Properties.Entry;
import org.openrewrite.properties.tree.Properties.File;
import org.openrewrite.properties.tree.Properties.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@lombok.Value
@EqualsAndHashCode(callSuper = true)
public class AddProperty extends Recipe {

    @Option(displayName = "Property key",
            example = "management.metrics.binders.files.enabled")
    String key;

    @Option(displayName = "New value",
            example = "false")
    String value;

    /*
    @Option(displayName = "Optional delimiter",
            description = "Property entries support different delimiters (`=`, `:`, or whitespace). The default value is `=` unless provided the delimiter of the new property entry.",
            required = false,
            example = ":")
    @Nullable
    String delimiter;
    */

    @Override
    public String getDisplayName() {
        return "Add property with key and value";
    }

    @Override
    public String getDescription() {
        return "Add a property key value pair at the end.";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new ChangePropertyValueVisitor<>();
    }

    public class ChangePropertyValueVisitor<P> extends PropertiesVisitor<P> {

        public ChangePropertyValueVisitor() {
        }

        @Override
        public Properties visitFile(File file, P p) {
            List<Content> contents = new ArrayList<>(file.getContent());
            if (alreadyVisited(file)) {
                return file;
            }
            String prefix = "";
            if (!contents.isEmpty()) {
                prefix = "\n";
            }
            contents.add(
                    new Entry(
                            Tree.randomId(),
                            prefix,
                            Markers.EMPTY,
                            key,
                            "",
                            Entry.Delimiter.EQUALS,
                            new Value(Tree.randomId(), "", Markers.EMPTY, value)));
            return file.withContent(Collections.unmodifiableList(contents));
        }

        private boolean alreadyVisited(File file) {
            List<Content> contents = new ArrayList<>(file.getContent());
            if (!contents.isEmpty()) {
                Content c = contents.get(contents.size() - 1);
                if (c instanceof Entry) {
                    Entry e = (Entry) c;
                    return e != null && key.equals(e.getKey()) && e.getValue() != null && value.equals(e.getValue().getText());
                }
            }
            return false;
        }

    }

}
