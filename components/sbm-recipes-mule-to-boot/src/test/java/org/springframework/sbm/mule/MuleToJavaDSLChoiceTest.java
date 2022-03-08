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
package org.springframework.sbm.mule;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;

public class MuleToJavaDSLChoiceTest extends JavaDSLActionBaseTest {
    private static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\">\n" +
            "        <http:listener-config name=\"HTTP_Listener_Configuration\" host=\"0.0.0.0\" port=\"9081\" doc:name=\"HTTP Listener Configuration\"/>\n" +
            "    <flow name=\"choiceFlow\">\n" +
            "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/choice\" doc:name=\"HTTP\"/>\n" +
            "        <expression-filter expression=\"#[message.inboundProperties.'http.request.uri' != '/favicon.ico']\" doc:name=\"Expression\"/>\n" +
            "        <set-variable variableName=\"language\" value=\"#[message.inboundProperties.'http.query.params'.language]\" doc:name=\"Set Language Variable\"/>\n" +
            "        <choice doc:name=\"Choice\">\n" +
            "            <when expression=\"#[flowVars.language == 'Spanish']\">\n" +
            "                <set-payload doc:name=\"Reply in Spanish\" value=\"Hola!\"/>\n" +
            "            </when>\n" +
            "            <when expression=\"#[flowVars.language == 'French']\">\n" +
            "                <set-payload doc:name=\"Reply in French\" value=\"Bonjour!\"/>\n" +
            "            </when>\n" +
            "            <otherwise>\n" +
            "                <set-variable variableName=\"langugae\" value=\"English\" doc:name=\"Set Language to English\"/>\n" +
            "                <set-payload doc:name=\"Reply in English\" value=\"Hello\"/>\n" +
            "            </otherwise>\n" +
            "        </choice>\n" +
            "        <logger message=\"#[payload]\" level=\"INFO\" doc:name=\"Logger\"/>\n" +
            "    </flow>\n" +
            "</mule>";

    @Test
    public void test() {

    }
}
