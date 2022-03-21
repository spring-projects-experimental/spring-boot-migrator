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
package org.springframework.sbm.mule.actions.javadsl.translators.http;

import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import lombok.extern.slf4j.Slf4j;
import org.mulesoft.schema.mule.http.ListenerType;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Set;

/**
 * Transforms a {@code <http:listener/>} elements to SI DSL.
 *
 * https://docs.mulesoft.com/http-connector/0.3.9/http-listener-connector
 */
@Component
@Slf4j
public class HttpListenerTranslator implements MuleComponentToSpringIntegrationDslTranslator<ListenerType> {

    private final static String javaDslHttpListenerTemplate =
            "return IntegrationFlows.from(Http.inboundChannelAdapter(\"${path}\")).handle((p, h) -> p)";

    @Override
    public Class<ListenerType> getSupportedMuleType() {
        return ListenerType.class;
    }

    @Override
    public DslSnippet translate(ListenerType component, QName name, MuleConfigurations muleConfigurations, String flowName) {
        /*
        * In the connector component on your flow, the only required fields are the Path
        * (the path-absolute URL defining the resource location), which by default is /,
        * and a configuration reference to a global element, containing the necessary parameters Host and Port.
        */
        String path = component.getPath();
        if(path == null || path.isEmpty()) {
            log.error("Path attribute of <http:listener> must not be set.");
        }

        String snippet = path == null ? javaDslHttpListenerTemplate :
                javaDslHttpListenerTemplate.replace("${path}", path);

        return new DslSnippet(snippet,
                Set.of("org.springframework.context.annotation.Bean",
                        "org.springframework.context.annotation.Configuration",
                        "org.springframework.integration.dsl.IntegrationFlow",
                        "org.springframework.integration.dsl.IntegrationFlows",
                        "org.springframework.integration.http.dsl.Http",
                        "org.springframework.integration.transformer.ObjectToStringTransformer"
                        ),
                Set.of(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-http:5.4.4"
                ),
                Collections.emptySet());
    }
}
