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
package org.springframework.sbm.mule.actions.javadsl.translators.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ExpressionLanguageTranslatorTest {

    ExpressionLanguageTranslator sut = new ExpressionLanguageTranslator();

    @Test
    void messageWithoutElShouldBeReturnedAsIs() {
        String message = "Hello world";
        assertThat(sut.translate(message)).isEqualTo("Hello world");
    }

    @Test
    void translateMuleEl() {
        String message = "#[payload]";
        assertThat(sut.translate(message)).isEqualTo("${payload}");
    }

    @Test
    void test() {
        String message = "transactionId=\"#[flowVars.transactionId]\", extCorrelationId=\"#[flowVars.extCorrelationId]\", step=\"RequestParametersReceived\",functionalId=\"#[flowVars.functionalId]\", requesterAppId=\"#[flowVars.requesterAppId]\", requesterAppName=\"#[flowVars.requesterAppName]\",interfaceType=\"#[flowVars.interfaceType]\", requesterUserId=\"#[flowVars.requesterUserId]\", httpMethod=\"#[message.inboundProperties.'http.method']\", httpScheme=\"#[message.inboundProperties.'http.scheme']\", httpHost=\"#[message.inboundProperties.'host']\", httpRequestUri=\"#[message.inboundProperties.'http.request.uri']\", httpQueryString=\"#[message.inboundProperties.'http.query.string']\" httpVersion=\"#[message.inboundProperties.'http.version']\", contentType=\"#[message.inboundProperties.'content-type']\", proxyClientId=\"#[message.inboundProperties.'client_id']\"";
        String translate = sut.translate(message);
        assertThat(translate).isEqualTo("transactionId=\\\"${flowVars.transactionId}\\\", extCorrelationId=\\\"${flowVars.extCorrelationId}\\\", step=\\\"RequestParametersReceived\\\",functionalId=\\\"${flowVars.functionalId}\\\", requesterAppId=\\\"${flowVars.requesterAppId}\\\", requesterAppName=\\\"${flowVars.requesterAppName}\\\",interfaceType=\\\"${flowVars.interfaceType}\\\", requesterUserId=\\\"${flowVars.requesterUserId}\\\", httpMethod=\\\"#[message.inboundProperties.'http.method']\\\", httpScheme=\\\"#[message.inboundProperties.'http.scheme']\\\", httpHost=\\\"#[message.inboundProperties.'host']\\\", httpRequestUri=\\\"#[message.inboundProperties.'http.request.uri']\\\", httpQueryString=\\\"#[message.inboundProperties.'http.query.string']\\\" httpVersion=\\\"#[message.inboundProperties.'http.version']\\\", contentType=\\\"#[message.inboundProperties.'content-type']\\\", proxyClientId=\\\"#[message.inboundProperties.'client_id']\\\"");
    }

    @Test
    void shouldEscapeQuotationMarks() {
        String message = "transactionId=\"#[flowVars.transactionId]\", extCorrelationId=\"#[flowVars.extCorrelationId]\", step=\"RequestPayloadSent\"";
        String translated = sut.translate(message);
        assertThat(translated).isEqualTo("transactionId=\\\"${flowVars.transactionId}\\\", extCorrelationId=\\\"${flowVars.extCorrelationId}\\\", step=\\\"RequestPayloadSent\\\"");
    }

    @Test
    void test2() {
        String message = "transactionId=&quot;#[flowVars.transactionId]&quot;, " +
                "extCorrelationId=&quot;#[flowVars.extCorrelationId]&quot;, " +
                "step=&quot;RequestPayloadSent&quot;, " +
                "functionalId=&quot;#[flowVars.functionalId]&quot;, " +
                "requesterAppId=&quot;#[flowVars.requesterAppId]&quot;, " +
                "requesterAppName=&quot;#[flowVars.requesterAppName]&quot;," +
                "interfaceType=&#34;#[flowVars.interfaceType]&#34;, " +
                "requesterUserId=&quot;#[flowVars.requesterUserId]&quot;, " +
                "[payload] #[message.payloadAs(java.lang.String)]";

        String translated = sut.translate(message);

        assertThat(translated).isEqualTo(
                "transactionId=&quot;${flowVars.transactionId}&quot;, " +
                "extCorrelationId=&quot;${flowVars.extCorrelationId}&quot;, " +
                "step=&quot;RequestPayloadSent&quot;, " +
                "functionalId=&quot;${flowVars.functionalId}&quot;, " +
                "requesterAppId=&quot;${flowVars.requesterAppId}&quot;, " +
                "requesterAppName=&quot;${flowVars.requesterAppName}&quot;," +
                "interfaceType=&#34;${flowVars.interfaceType}&#34;, " +
                "requesterUserId=&quot;${flowVars.requesterUserId}&quot;, " +
                "[payload] ${message.payloadAs(java.lang.String)}");

    }

}