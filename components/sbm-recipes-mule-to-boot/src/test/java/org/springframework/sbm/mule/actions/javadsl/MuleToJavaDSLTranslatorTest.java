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

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MuleToJavaDSLTranslatorTest {
    private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private final MuleToJavaDSLTranslator muleToJavaDSLTranslator = new MuleToJavaDSLTranslator();

    private String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
            "xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\">\n" +
            "<amqp:config name=\"AMQP_Config\" doc:name=\"AMQP Config\" doc:id=\"a1161a09-026e-48c8-918c-ce5d1855d68e\" >\n" +
            "<amqp:connection host=\"localhost\" port=\"5672\" username=\"guest\" password=\"guest\"/>\n" +
            "</amqp:config>\n" +
            "<flow name=\"amqp-testFlow\"\n" +
            "doc:id=\"591949db-7702-437b-8945-7a8e0eeadf8d\">\n" +
            "<amqp:listener doc:name=\"Listener\"\n" +
            "doc:id=\"50219da8-efde-433e-a8ba-e4b657b9d803\"\n" +
            "config-ref=\"AMQP_Config\" queueName=\"FirstQueue\" />\n" +
            "<logger level=\"INFO\" doc:name=\"Logger\"\n" +
            "doc:id=\"017f86b2-60f7-4d3d-9e8f-ca84804d09ad\"\n" +
            "message=\"#[output application/java&#10;---&#10;payload]\" />\n" +
            "<amqp:publish doc:name=\"Publish\"\n" +
            "doc:id=\"33dd89d5-5a58-4979-81de-445a88159e8d\"\n" +
            "config-ref=\"AMQP_Config\" exchangeName=\"si.out.exchange\">\n" +
            "<amqp:routing-keys>\n" +
            "<amqp:routing-key value=\"si.out.queue\" />\n" +
            "</amqp:routing-keys>\n" +
            "</amqp:publish>\n" +
            "</flow>\n" +
            "</mule>";

    private String muleXmlWithUnknownStatement = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
            "xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\">\n" +
            "<amqp:config name=\"AMQP_Config\" doc:name=\"AMQP Config\" doc:id=\"a1161a09-026e-48c8-918c-ce5d1855d68e\" >\n" +
            "<amqp:connection host=\"localhost\" port=\"5672\" username=\"guest\" password=\"guest\"/>\n" +
            "</amqp:config>\n" +
            "<flow name=\"amqp-testFlow\"\n" +
            "doc:id=\"591949db-7702-437b-8945-7a8e0eeadf8d\">\n" +
            "<unknownComponent></unknownComponent>\n" +
            "</flow>\n" +
            "</mule>";

    private String muleXmlWithHttpListener = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
            "\txmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
            "\t<http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"9bbc3cb7-bbe9-4d97-81cf-2fb4beb980ae\" basePath=\"/route\" >\n" +
            "\t\t<http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
            "\t</http:listener-config>\n" +
            "\t<flow name=\"http-routeFlow\" doc:id=\"ff11723e-78e9-4cc2-b760-2bec156ef0f2\" >\n" +
            "\t\t<http:listener doc:name=\"Listener\" doc:id=\"9f602d5c-5386-4fc9-ac8f-024d754c17e5\" config-ref=\"HTTP_Listener_config\" path=\"/test\"/>\n" +
            "\t\t<logger level=\"INFO\" doc:name=\"Logger\" doc:id=\"4585ec7f-2d4a-4d86-af24-b678d4a99227\" />\n" +
            "\t</flow>\n" +
            "</mule>";

    private String expectedAmqpJavaDSL = "IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, \"FirstQueue\"))\n" +
            ".log()\n" +
            ".handle(Amqp.outboundAdapter(rabbitTemplate).routingKey(\"si.out.queue\"))";

    private String expectedHttpJavaDSL = "IntegrationFlows.from(Http.inboundChannelAdapter(\"/test\"))\n" +
            ".log()";

    @Test
    public void shouldTranslateAmqpRoute() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        String javaDSL = muleToJavaDSLTranslator.translate(getMuleDocument(muleXml));
        assertEquals(expectedAmqpJavaDSL, javaDSL);
    }

    @Test
    public void shouldTranslateHtpRoute() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        String javaDSL = muleToJavaDSLTranslator.translate(getMuleDocument(muleXmlWithHttpListener));
        assertEquals(expectedHttpJavaDSL, javaDSL);
    }

    @Test
    public void shouldProduceCommentForUnknownFlowStatement() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        String comment = muleToJavaDSLTranslator.translate(getMuleDocument(muleXmlWithUnknownStatement));
        assertThat(comment).isEqualTo("// FIXME: we cannot migrate unknownComponent");
    }

    private Document getMuleDocument(String xmlContent) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xmlDocument = db.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
        return xmlDocument;
    }
}
