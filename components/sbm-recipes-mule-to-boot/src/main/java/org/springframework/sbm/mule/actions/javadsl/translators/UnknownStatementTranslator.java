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

import org.jetbrains.annotations.NotNull;
import org.mulesoft.schema.mule.core.AbstractInboundEndpointType;
import org.springframework.sbm.mule.api.MuleMigrationContext;

import javax.xml.namespace.QName;
import java.util.Collections;

public class UnknownStatementTranslator implements MuleComponentToSpringIntegrationDslTranslator {

    @Override
    public Class getSupportedMuleType() {
        return null;
    }

    @Override
    public DslSnippet translate(MuleMigrationContext context, Object component, QName name) {
        return new DslSnippet(
                generateDSLStatement(component),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet(),
                true
        );
    }

    @NotNull
    private String generateDSLStatement(Object component) {
        String res = "// FIXME: Conversion is not supported for Mule type: " + component.getClass().getName();
        return component instanceof AbstractInboundEndpointType ? res + "\nIntegrationFlows.from(\"\")" : res;
    }
}
