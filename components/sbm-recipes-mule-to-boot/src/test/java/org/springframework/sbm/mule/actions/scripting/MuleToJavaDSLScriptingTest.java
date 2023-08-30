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
package org.springframework.sbm.mule.actions.scripting;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLScriptingTest extends JavaDSLActionBaseTest {

    @Test
    public void sbmAcknowledgesScriptTag() {

        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <mule xmlns:scripting="http://www.mulesoft.org/schema/mule/scripting"
                	xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:api-platform-gw="http://www.mulesoft.org/schema/mule/api-platform-gw" xmlns:apikit="http://www.mulesoft.org/schema/mule/apikit" xmlns:cmis="http://www.mulesoft.org/schema/mule/cmis" xmlns:context="http://www.springframework.org/schema/context" xmlns:db="http://www.mulesoft.org/schema/mule/db" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation" xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw" xmlns:ee="http://www.mulesoft.org/schema/mule/ee/core" xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:spring="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.mulesoft.org/schema/mule/scripting http://www.mulesoft.org/schema/mule/scripting/current/mule-scripting.xsd
                http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-current.xsd
                http://www.mulesoft.org/schema/mule/api-platform-gw http://www.mulesoft.org/schema/mule/api-platform-gw/current/mule-api-platform-gw.xsd
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd
                http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/apikit http://www.mulesoft.org/schema/mule/apikit/current/mule-apikit.xsd
                http://www.mulesoft.org/schema/mule/cmis http://www.mulesoft.org/schema/mule/cmis/current/mule-cmis.xsd
                http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd
                http://www.mulesoft.org/schema/mule/ee/core http://www.mulesoft.org/schema/mule/ee/core/current/mule-ee.xsd">
                   \s
                    <flow name="get:/canary/{birdName}:cmb-hsbcnet-ss-sa-entitlement-change-request-config">
                        <scripting:component doc:name="Groovy">
                            <scripting:script engine="Groovy"><![CDATA[throw new javax.ws.rs.BadRequestException();]]></scripting:script>
                        </scripting:component>
                    </flow>
                </mule>
                """;
        addXMLFileToResource(xml);
        runAction(projectContext1 ->
            assertThat(getGeneratedJavaFile()).isEqualTo(
                    """
                    package com.example.javadsl;
                    import org.springframework.context.annotation.Bean;
                    import org.springframework.context.annotation.Configuration;
                    import org.springframework.http.HttpMethod;
                    import org.springframework.integration.dsl.IntegrationFlow;
                    import org.springframework.integration.dsl.IntegrationFlows;
                    import org.springframework.integration.http.dsl.Http;
                                     
                    @Configuration
                    public class FlowConfigurations {
                        @Bean
                        IntegrationFlow get__canary__birdName__cmb_hsbcnet_ss_sa_entitlement_change_request_config() {
                            // FIXME: the base path for Http.inboundGateway must be extracted from http:listener in flow containing apikit:router with config-ref="cmb-hsbcnet-ss-sa-entitlement-change-request-config"
                            // FIXME: add all JavaDSL generated components between http:listener and apikit:router with config-ref="cmb-hsbcnet-ss-sa-entitlement-change-request-config" into this flow
                            // FIXME: remove the JavaDSL generated method containing apikit:router with config-ref="cmb-hsbcnet-ss-sa-entitlement-change-request-config"
                            return IntegrationFlows.from(
                                    Http.inboundGateway("/canary/{birdName}").requestMapping(r -> r.methods(HttpMethod.GET)))
                                    //FIXME: element is not supported for conversion: <scripting:component/>
                                    .get();
                        }
                    }""")
        );
    }
}
