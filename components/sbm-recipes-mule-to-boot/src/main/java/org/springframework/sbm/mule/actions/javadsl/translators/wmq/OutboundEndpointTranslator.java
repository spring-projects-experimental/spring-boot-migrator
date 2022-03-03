package org.springframework.sbm.mule.actions.javadsl.translators.wmq;

import org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType;
import org.springframework.sbm.mule.actions.javadsl.translators.Bean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Set;

@Component
public class OutboundEndpointTranslator implements MuleComponentToSpringIntegrationDslTranslator<OutboundEndpointType> {
    @Override
    public Class<OutboundEndpointType> getSupportedMuleType() {
        return OutboundEndpointType.class;
    }

    @Override
    public DslSnippet translate(OutboundEndpointType component, QName name, MuleConfigurations muleConfigurations) {
        return new DslSnippet(
                ".handle(Jms.outboundAdapter(connectionFactory).destination(\"Q1\"))",
                Set.of("javax.jms.ConnectionFactory", "org.springframework.integration.jms.dsl.Jms"),
                Collections.emptySet(),
                Set.of(new Bean("connectionFactory", "javax.jms.ConnectionFactory"))
        );
    }
}
