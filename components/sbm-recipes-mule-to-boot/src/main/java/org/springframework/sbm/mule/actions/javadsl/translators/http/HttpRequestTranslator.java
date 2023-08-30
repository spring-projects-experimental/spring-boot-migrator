/*
 * Copyright 2021 - 2023 the original author or authors.
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

import org.mulesoft.schema.mule.http.RequestConfigType;
import org.mulesoft.schema.mule.http.RequestType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapter;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.*;


/**
 * Translator for {@code <http:request> } elements.spring integration
 *
 *
 * <a href="https://docs.mulesoft.com/http-connector/0.3.9/http-request-connector">Mule Reference - Http Request Connector</a>
 * <a href="https://docs.spring.io/spring-integration/reference/html/http.html#http-outbound">Spring Integration Reference - HTTP Outbound Components</a>
 */
@Component
public class HttpRequestTranslator implements MuleComponentToSpringIntegrationDslTranslator<RequestType> {

    @Override
    public Class getSupportedMuleType() {
        return RequestType.class;
    }

    private static final String template = "                .headerFilter(\"accept-encoding\", false)\n" +
            "                .handle(\n" +
            "                        Http.outboundGateway(\"$PROTOCOL://$HOST:$PORT$PATH\")\n" +
            "                        .httpMethod(HttpMethod.$METHOD)\n" +
            "                        //FIXME: Use appropriate response class type here instead of String.class\n" +
            "                        .expectedResponseType(String.class)\n" +
            "                )";

    @Override
    public DslSnippet translate(int id, RequestType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName, Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {

        RequestConfigType config = getRequestConfiguration(component, muleConfigurations);

        return DslSnippet.builder()
                .renderedSnippet(
                        template
                                .replace("$PATH", emptyStringIfNull(component.getPath()))
                                .replace("$METHOD", defaultToValueIfNull(component.getMethod(), "GET"))
                                .replace("$HOST", emptyStringIfNull(config.getHost()))
                                .replace("$PORT", emptyStringIfNull(config.getPort()))
                                .replace("$PROTOCOL", defaultToValueIfNull(config.getProtocol(), "http").toLowerCase())
                )
                .requiredImports(Set.of("org.springframework.http.HttpMethod"))
                .build();
    }

    private RequestConfigType getRequestConfiguration(RequestType component, MuleConfigurations muleConfigurations) {
        RequestConfigType emptyRequestConfig = new RequestConfigType();

        ConfigurationTypeAdapter<RequestConfigType> configurationTypeAdapter =
                muleConfigurations.getConfigurations().get(component.getConfigRef());

        if (configurationTypeAdapter == null) {

            return emptyRequestConfig;
        }

        RequestConfigType requestConfig = configurationTypeAdapter
                .getMuleConfiguration();

        return requestConfig != null ? requestConfig : emptyRequestConfig;
    }

    private String defaultToValueIfNull(String originalValue, String defaultValue) {

        return originalValue == null ? defaultValue : originalValue;
    }

    private String emptyStringIfNull(String value) {
        return value == null ? "" : value;
    }
}
