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
package org.springframework.sbm.mule.actions;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpConfigTypeAdapter;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpInboundEndpointTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpOutboundEndpointTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.common.ExpressionLanguageTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.core.FlowRefTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.core.SetPayloadTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.core.SetPropertyTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.core.TransformerTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.dwl.DwlTransformTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.http.HttpListenerConfigTypeAdapter;
import org.springframework.sbm.mule.actions.javadsl.translators.http.HttpListenerTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.logging.LoggingTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.wmq.WmqConnectorTypeAdapter;
import org.springframework.sbm.mule.actions.javadsl.translators.wmq.WmqInboundEndpointTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.wmq.WmqOutboundEndpointTranslator;
import org.springframework.sbm.mule.api.MuleMigrationContextFactory;
import org.springframework.sbm.mule.api.toplevel.FlowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.SubflowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.TopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapterFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurationsExtractor;

import java.util.List;

import static org.mockito.Mockito.mock;

public class JavaDSLActionBaseTest {


    protected JavaDSLAction2 myAction;
    protected final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    public void setup() {
        List<MuleComponentToSpringIntegrationDslTranslator> translators = List.of(
                new HttpListenerTranslator(),
                new LoggingTranslator(new ExpressionLanguageTranslator()),
                new SetPayloadTranslator(),
                new FlowRefTranslator(),
                new AmqpOutboundEndpointTranslator(),
                new AmqpInboundEndpointTranslator(),
                new SetPropertyTranslator(),
                new TransformerTranslator(),
                new WmqOutboundEndpointTranslator(),
                new WmqInboundEndpointTranslator(),
                new DwlTransformTranslator()
        );
        List<TopLevelElementFactory> topLevelTypeFactories = List.of(
                new FlowTopLevelElementFactory(translators),
                new SubflowTopLevelElementFactory(translators)
        );

        ConfigurationTypeAdapterFactory configurationTypeAdapterFactory = new ConfigurationTypeAdapterFactory(
                List.of(
                        new AmqpConfigTypeAdapter(),
                        new HttpListenerConfigTypeAdapter(),
                        new WmqConnectorTypeAdapter()
                )
        );
        MuleMigrationContextFactory muleMigrationContextFactory = new MuleMigrationContextFactory(new MuleConfigurationsExtractor(configurationTypeAdapterFactory));
        myAction = new JavaDSLAction2(muleMigrationContextFactory, topLevelTypeFactories);
        myAction.setEventPublisher(eventPublisher);
    }
}
