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
package org.springframework.sbm.boot.upgrade_27_30;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.properties.PropertiesVisitor;
import org.openrewrite.properties.tree.Properties;

public class SamlRelyingPartyPropertyApplicationPropertiesMove extends Recipe {
    private final static String REGEX_PATTERN = "(spring\\.security\\.saml2\\.relyingparty\\.registration\\..*)(\\.identityprovider)(.*)";

    @Override
    public String getDisplayName() {
        return "Move SAML relying party identity provider property to asserting party";
    }

    @Override
    public String getDescription() {
        return "Renames spring.security.saml2.relyingparty.registration.(any).identityprovider to " +
                "spring.security.saml2.relyingparty.registration.(any).assertingparty.";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {

        return new PropertiesVisitor<ExecutionContext>() {

            @Override
            public Properties visitEntry(Properties.Entry entry, ExecutionContext executionContext) {

                if (entry.getKey().matches(REGEX_PATTERN)) {
                    return super.visitEntry(updateEntry(entry), executionContext);
                }

                return super.visitEntry(entry, executionContext);
            }

            @NotNull
            private Properties.Entry updateEntry(Properties.Entry entry) {
                return entry.withKey(entry.getKey().replaceAll(REGEX_PATTERN, "$1.assertingparty$3"));
            }
        };
    }
}
