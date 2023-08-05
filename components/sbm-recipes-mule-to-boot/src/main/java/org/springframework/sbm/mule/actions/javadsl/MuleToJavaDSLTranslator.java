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
package org.springframework.sbm.mule.actions.javadsl;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MuleToJavaDSLTranslator {
    public static final String FIXME_WE_CANNOT_MIGRATE = "// FIXME: we cannot migrate ";
    private final Map<String, MuleToJavaDSLStatementTranslator> translatorMap = new HashMap<>();

    public MuleToJavaDSLTranslator() {
        String listenerTemplate = "IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, \"--QueueName--\"))";
        MuleToJavaDSLStatementTranslator listenerTranslator = new MuleToJavaDSLStatementTranslator(listenerTemplate, "QueueName", "//@queueName");
        // TODO: use element with namespace instead prefix
        translatorMap.put("amqp:listener", listenerTranslator);

        String loggerTemplate = "log()";
        MuleToJavaDSLStatementTranslator loggerTranslator = new MuleToJavaDSLStatementTranslator(loggerTemplate, null, null);
        translatorMap.put("logger", loggerTranslator);

        String publishTemplate = "handle(Amqp.outboundAdapter(rabbitTemplate).routingKey(\"--RoutingKey--\"))";
        MuleToJavaDSLStatementTranslator publisher = new MuleToJavaDSLStatementTranslator(publishTemplate, "RoutingKey", "//routing-keys/routing-key/@value");
        translatorMap.put("amqp:publish", publisher);

        String httpTemplate = "IntegrationFlows.from(Http.inboundChannelAdapter(\"--Path--\"))";
        MuleToJavaDSLStatementTranslator httpTranslator = new MuleToJavaDSLStatementTranslator(httpTemplate, "Path", "//@path");

        translatorMap.put("http:listener", httpTranslator);
    }

    public String translate(Document document) {
        List<Element> flowElementsList = extractFlowElements(document);

        return flowElementsList.stream()
                .map(getElementStringFunction())
                .collect(Collectors.joining("\n."));
    }

    @NotNull
    private Function<Element, String> getElementStringFunction() {
        return e -> {
            Optional<MuleToJavaDSLStatementTranslator> translator = Optional.ofNullable(translatorMap.get(e.getNodeName()));
            if (translator.isPresent()) {
                return translator.get().translate(e);
            } else {
                return FIXME_WE_CANNOT_MIGRATE + e.getNodeName();
            }
        };
    }

    private List<Element> extractFlowElements(Document muleConfiguration) {
        List<Element> flowElements = new ArrayList<>();
        NodeList flow = muleConfiguration.getDocumentElement().getElementsByTagName("flow");
        if (flow.getLength() == 0) {
            return Collections.emptyList();
        }
        NodeList childNodes = flow.item(0).getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i) instanceof Element) {
                flowElements.add((Element) childNodes.item(i));
            }
        }
        return flowElements;
    }

}
