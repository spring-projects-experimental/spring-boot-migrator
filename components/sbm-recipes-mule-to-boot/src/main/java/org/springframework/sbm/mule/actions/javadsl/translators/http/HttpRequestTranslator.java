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

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.mulesoft.schema.mule.http.RequestConfigType;
import org.mulesoft.schema.mule.http.RequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapter;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Translator for {@code <http:request> } elements.spring integration
 *
 *
 * <a href="https://docs.mulesoft.com/http-connector/0.3.9/http-request-connector">Mule Reference - Http Request Connector</a>
 * <a href="https://docs.spring.io/spring-integration/reference/html/http.html#http-outbound">Spring Integration Reference - HTTP Outbound Components</a>
 */
@Component
public class HttpRequestTranslator implements MuleComponentToSpringIntegrationDslTranslator<RequestType> {

    @Autowired
    private Configuration configuration;

    @Override
    public Class getSupportedMuleType() {
        return RequestType.class;
    }

    @Override
    public DslSnippet translate(RequestType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName) {


        String templateStr = "return IntegrationFlows\n" +
                "  .from\n" +
                "    (\n" +
                "      Http.inboundChannelAdapter(\"${host}<#if port?has_content>:${port}</#if>/${basePath}\")\n" +
                "        .requestMapping(m -> m.methods(HttpMethod.GET))\n" +
                "<#if responseTimeout?has_content>" +
                "        .replyTimeout(${responseTimeout})\n" +
                "</#if>" +
                "      )\n" +
                "  .channel(INBOUND_DEMO_CHANNEL)\n" +
                "  .get();";

        try {
            Map<String, Object> data = new HashMap<>();

            // TODO: requires access to config, e.g. muleMigrationContext.getConfigRef("...")
            String configRef = component.getConfigRef();
            Optional<? extends ConfigurationTypeAdapter> configurationTypeAdapter = muleConfigurations.find(configRef);
            if(configurationTypeAdapter.isPresent()) {
                RequestConfigType cast = (RequestConfigType) configurationTypeAdapter.get().getMuleConfiguration();
                data.put("host", cast.getHost());
                data.put("port", cast.getPort());
            }


            data.put("basePath", component.getPath());
            data.put("method", component.getMethod());
            data.put("responseTimeout", component.getResponseTimeout());
            Template t = new Template("name", new StringReader(templateStr), configuration);
            StringWriter stringWriter = new StringWriter();
            t.process(data,
                    stringWriter);
            return new DslSnippet(stringWriter.toString(), Collections.emptySet());
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }


        return null;
    }
}
