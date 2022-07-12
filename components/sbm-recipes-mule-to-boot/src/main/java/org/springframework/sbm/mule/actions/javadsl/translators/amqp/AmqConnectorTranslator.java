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
package org.springframework.sbm.mule.actions.javadsl.translators.amqp;

import org.mulesoft.schema.mule.amqp.AmqpConnectorType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * https://docs.mulesoft.com/amqp-connector/0.3.9/
 */
@Component
public class AmqConnectorTranslator implements MuleComponentToSpringIntegrationDslTranslator<AmqpConnectorType> {

    @Override
    public Class getSupportedMuleType() {
        return AmqpConnectorType.class;
    }

    @Override
    public DslSnippet translate(int id, AmqpConnectorType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName, Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        return DslSnippet.builder()
                .renderedSnippet("// FIXME: <amq:connector/>  cannot be translated yet.")
                .build();
    }
}
