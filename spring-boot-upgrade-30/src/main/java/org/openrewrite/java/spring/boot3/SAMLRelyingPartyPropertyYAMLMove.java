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
package org.openrewrite.java.spring.boot3;

import org.openrewrite.Cursor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.yaml.YamlIsoVisitor;
import org.openrewrite.yaml.tree.Yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SAMLRelyingPartyPropertyYAMLMove extends Recipe {

    @Override
    public String getDisplayName() {
        return "Move SAML relying party identity provider property to asserting party";
    }

    @Override
    public String getDescription() {
        return "Renames spring.security.saml2.relyingparty.registration.(any).identityprovider to " +
                "spring.security.saml2.relyingparty.registration.(any).assertingparty";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {

        return new YamlIsoVisitor<>() {
            @Override
            public Yaml.Mapping.Entry visitMappingEntry(Yaml.Mapping.Entry entry,
                                                        ExecutionContext executionContext) {
                entry = super.visitMappingEntry(entry, executionContext);

                if (isIdentityProviderNode(entry) && isOfCorrectHierarchy()) {
                    entry = entry.withKey(entry.getKey().withValue("assertingparty"));
                }

                return entry;
            }

            private boolean isOfCorrectHierarchy() {

                Cursor current = getCursor();

                List<Yaml.Mapping.Entry> yamlEntries = new ArrayList<>();

                while (current != null) {
                    if (current.getValue() instanceof Yaml.Mapping.Entry) {
                        yamlEntries.add(current.getValue());
                    }

                    current = current.getParent();
                }

                String hierarchy = yamlEntries
                        .stream()
                        .map(entry -> entry.getKey().getValue())
                        .collect(Collectors.joining("->"));

                return hierarchy.matches("identityprovider->.*->registration->relyingparty->saml2->security->spring");
            }

            private boolean isIdentityProviderNode(Yaml.Mapping.Entry entry) {
                return entry.getKey().getValue().equals("identityprovider");
            }
        };
    }
}
