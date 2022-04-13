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

import org.mulesoft.schema.mule.http.RequestType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
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
            "                        Http.outboundGateway(\"https://catfact.ninja:443$PATH\")\n" +
            "                        .httpMethod(HttpMethod.$METHOD)\n" +
            "                        //FIXME: Use appropriate response class type here instead of String.class\n" +
            "                        .expectedResponseType(String.class)\n" +
            "                )";

    @Override
    public DslSnippet translate(RequestType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName) {
        return new DslSnippet(
                template
                        .replace("$PATH", emptyStringIfNull(component.getPath()))
                        .replace("$METHOD", defaultToGetIfNull(component.getMethod()))
                ,
                Set.of("org.springframework.http.HttpMethod")
        );
    }

    private String defaultToGetIfNull(String originalValue) {
        return originalValue == null ? "GET" : originalValue;
    }

    private String emptyStringIfNull(String value) {
        return value == null ? "" : value;
    }
}
