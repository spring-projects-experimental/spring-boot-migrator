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
package org.springframework.sbm.mule.resource;

import org.junit.jupiter.api.Test;
import org.mulesoft.schema.mule.core.FlowType;
import org.mulesoft.schema.mule.http.InboundEndpointType;
import org.mulesoft.schema.mule.core.MuleType;

import javax.xml.bind.JAXBElement;

import static org.assertj.core.api.Assertions.assertThat;

class MuleXmlUnmarshallerTest {

    @Test
    void unmarshalMuleXml() {
        String muleXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<mule xmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\"\n" +
                        "\txmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                        "\txmlns:spring=\"http://www.springframework.org/schema/beans\" version=\"EE-3.4.0\"\n" +
                        "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "\txsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                        "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                        "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                        "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\n" +
                        "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\">\n" +
                        "\t\n" +
                        "\t<flow name=\"Send_Message_Topic\" doc:name=\"Send_Message_Topic\">\n" +
                        "        <http:inbound-endpoint exchange-pattern=\"request-response\" host=\"localhost\" port=\"8090\" doc:name=\"HTTP\" path=\"topic\"/>\n" +
                        "        <set-payload value=\"#['im a Topic message'.getBytes()]\" doc:name=\"Set payload for amqp message as ByteArray\"/>\n" +
                        "        <amqp:outbound-endpoint responseTimeout=\"10000\"  doc:name=\"Send Topic Message\" connector-ref=\"amqp_config\" ref=\"amqp_topic_endpoint\"/>\n" +
                        "        <set-payload value=\"#['Message Sended']\" doc:name=\"Set payload as String\"/>\n" +
                        "        <logger message=\"Topic message sended\" level=\"INFO\" doc:name=\"Logger\"/>\n" +
                        "    </flow>\n" +
                        "    \n" +
                        "    <flow name=\"Recive_Message_Topic_1\" doc:name=\"Recive_Message_Topic_1\">\n" +
                        "        <amqp:inbound-endpoint responseTimeout=\"10000\"  doc:name=\"Recive Topic Message\" connector-ref=\"amqp_config\" ref=\"amqp_topic_endpoint\" routingKey=\"#\"/>\n" +
                        "        <byte-array-to-string-transformer doc:name=\"Transform bytearray message to String\"/>\n" +
                        "        <logger message=\"Recived message on Queue{topicQ} with Key {#}: #[payload]\" level=\"INFO\" doc:name=\"Logger\"/>\n" +
                        "    </flow>\n" +
                        "    \n" +
                        "    <flow name=\"Recive_Message_Topic_2\" doc:name=\"Recive_Message_Topic_2\">\n" +
                        "        <amqp:inbound-endpoint responseTimeout=\"10000\"  doc:name=\"Recive Topic Message\" connector-ref=\"amqp_config\"  routingKey=\"#.#\" queueName=\"topicQB\" exchangeName=\"topicEx\" exchangeType=\"topic\" queueDurable=\"true\"/>\n" +
                        "        <byte-array-to-string-transformer doc:name=\"Transform bytearray message to String\"/>\n" +
                        "        <logger message=\"Recived message on Queue{topicQB} with Key {#.#}: #[payload]\" level=\"INFO\" doc:name=\"Logger\"/>\n" +
                        "    </flow>\n" +
                        "    \n" +
                        "    <flow name=\"Recive_Message_Topic_3\" doc:name=\"Recive_Message_Topic_3\">\n" +
                        "        <amqp:inbound-endpoint responseTimeout=\"10000\"  doc:name=\"Recive Topic Message\" connector-ref=\"amqp_config\"  routingKey=\"routing.key\" queueName=\"topicQC\" exchangeName=\"topicEx\" exchangeType=\"topic\" queueDurable=\"true\"/>\n" +
                        "        <byte-array-to-string-transformer doc:name=\"Transform bytearray message to String\"/>\n" +
                        "        <logger message=\"Recived message on Queue{topicQC} with Key {direct2.key2}: #[payload]\" level=\"INFO\" doc:name=\"Logger\"/>\n" +
                        "    </flow>\n" +
                        "</mule>";

        MuleXmlUnmarshaller sut = new MuleXmlUnmarshaller();
        MuleType unmarshal = sut.unmarshal(muleXml);

        InboundEndpointType inboundEndpoint = (InboundEndpointType) ((JAXBElement) ((FlowType) ((JAXBElement) unmarshal.getBeansOrBeanOrPropertyPlaceholder().get(0)).getValue()).getAbstractInboundEndpoint()).getValue();
        assertThat(inboundEndpoint.getExchangePattern()).isEqualTo("request-response");
        assertThat(inboundEndpoint.getPort()).isEqualTo("8090");


        //((SetPayloadTransformerType)((JAXBElement)((FlowType)((JAXBElement)unmarshal.getBeansOrBeanOrPropertyPlaceholder().get(0)).getValue()).getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor().get(0)).getValue()).getOtherAttributes()

//        ((FlowType)((JAXBElement)unmarshal.getBeansOrBeanOrPropertyPlaceholder().get(0)).getValue()).getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor().stream()
//                .map(JAXBElement.class::cast)
//                .map(JAXBElement::getValue)
//                .collect(Collectors.toList())
    }
}
