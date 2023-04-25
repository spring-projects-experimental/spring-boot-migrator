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
package org.springframework.sbm.mule.actions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLDwlTransformTest extends JavaDSLActionBaseTest {

    // workaround to force-enable the TriggerMesh transform mode
    private void enableTriggerMeshTransform() {
        myAction.setMuleTriggerMeshTransformEnabled(true);
        System.setProperty("sbm.muleTriggerMeshTransformEnabled", "true");
    }

    private void disableTriggerMeshTransform() {
        myAction.setMuleTriggerMeshTransformEnabled(false);
        System.setProperty("sbm.muleTriggerMeshTransformEnabled", "false");
    }

    private static final String muleXmlSetPayload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\">\n" +
            "    <flow name=\"dwlFlow\">\n" +
            "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/dwl\" doc:name=\"HTTP\"/>\n" +
            "    \n" +
            "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "        \n" +
            "        <dw:transform-message doc:name=\"action transform\">\n" +
            "            <dw:set-payload><![CDATA[%dw 1.0\n" +
            "%output application/json\n" +
            "---\n" +
            "{\n" +
            "    action_Code: 10,\n" +
            "    returnCode:  20\n" +
            "}]]></dw:set-payload>\n" +
            "        </dw:transform-message>\n" +
            "        \n" +
            "         <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "    </flow>\n" +
            "</mule>\n";

    @Test
    public void shouldTranslateDwlTransformationWithSetPayload() {
        addXMLFileToResource(muleXmlSetPayload);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(2);
        assertThat(getGeneratedJavaFile())
                .isEqualTo(
                        """
                                package com.example.javadsl;
                                import org.springframework.context.annotation.Bean;
                                import org.springframework.context.annotation.Configuration;
                                import org.springframework.integration.dsl.IntegrationFlow;
                                import org.springframework.integration.dsl.IntegrationFlows;
                                import org.springframework.integration.handler.LoggingHandler;
                                import org.springframework.integration.http.dsl.Http;
                                                                
                                @Configuration
                                public class FlowConfigurations {
                                    @Bean
                                    IntegrationFlow dwlFlow() {
                                        return IntegrationFlows.from(Http.inboundGateway("/dwl")).handle((p, h) -> p)
                                                .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                                .transform(DwlFlowTransform_2::transform)
                                                .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                                .get();
                                    }
                                }""");
        assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                .isEqualTo("""
                                   package com.example.javadsl;
                                                                      
                                   public class DwlFlowTransform_2 {
                                       /*
                                        * TODO:
                                        *
                                        * Please add necessary transformation for below snippet
                                        * [%dw 1.0
                                        * %output application/json
                                        * ---
                                        * {
                                        *     action_Code: 10,
                                        *     returnCode:  20
                                        * }]
                                        * */
                                       public static DwlFlowTransform_2 transform(Object payload) {
                                                                      
                                           return new DwlFlowTransform_2();
                                       }
                                   }""");
    }

    @Test
    public void shouldTranslateDwlTransformationWithMuleTriggerMeshTransformAndSetPayloadEnabled() {
        enableTriggerMeshTransform();
        addXMLFileToResource(muleXmlSetPayload);
        runAction();

        assertThat(projectContext.getProjectJavaSources().list()).hasSize(3);
        assertThat(getGeneratedJavaFile())
                .isEqualTo("""
                        package com.example.javadsl;
                        import org.springframework.context.annotation.Bean;
                        import org.springframework.context.annotation.Configuration;
                        import org.springframework.integration.dsl.IntegrationFlow;
                        import org.springframework.integration.dsl.IntegrationFlows;
                        import org.springframework.integration.handler.LoggingHandler;
                        import org.springframework.integration.http.dsl.Http;
                                                          
                        @Configuration
                        public class FlowConfigurations {
                            @Bean
                            IntegrationFlow dwlFlow() {
                                return IntegrationFlows.from(Http.inboundGateway("/dwl")).handle((p, h) -> p)
                                        .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                        .handle((p, h) -> {
                                            TmDwPayload dwPayload = new TmDwPayload();
                                            String contentType = "application/json";
                                            if (h.get("contentType") != null) {
                                                contentType = h.get("contentType").toString();
                                            }
                                            dwPayload.setId(h.getId().toString());
                                            dwPayload.setSourceType(contentType);
                                            dwPayload.setSource(h.get("http_requestUrl").toString());
                                            dwPayload.setPayload(p.toString());
                                            return dwPayload;
                                        })
                                        .transform(DwlFlowTransformTM_2::transform)
                                        .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                        .get();
                            }
                        }""");
        assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                .isEqualTo("""
                                   package com.example.javadsl;
                                   import org.springframework.context.annotation.Configuration;
                                                                      
                                   import lombok.Data;
                                                                      
                                   /* Included with the baseline to support bridging between the Flow configuration and the translation implementation. */
                                                                      
                                   @Data
                                   public class TmDwPayload {
                                       private String id;
                                       private String source;
                                       private String sourceType;
                                       private String payload;
                                   }
                                   """
                );
        assertThat(projectContext.getProjectJavaSources().list().get(2).print())
                .isEqualTo("""
                                   package com.example.javadsl;
                                                                      
                                   import com.fasterxml.jackson.databind.ObjectMapper;
                                                                      
                                   import java.net.URI;
                                   import java.net.http.HttpClient;
                                   import java.net.http.HttpRequest;
                                   import java.net.http.HttpResponse;
                                                                      
                                   public class DwlFlowTransformTM_2 {
                                       public static class DataWeavePayload {
                                           public String input_data;
                                           public String spell;
                                           public String input_content_type;
                                           public String output_content_type;
                                       };
                                                                      
                                       public static String transform(TmDwPayload payload) {
                                           String uuid = payload.getId();
                                           String url = System.getenv("K_SINK");
                                           HttpClient client = HttpClient.newHttpClient();
                                           HttpRequest.Builder requestBuilder;
                                           DataWeavePayload dwPayload = new DataWeavePayload();
                                                                      
                                           if (payload.getSourceType().contains(";")) {
                                               dwPayload.input_content_type = payload.getSourceType().split(";")[0];
                                           } else {
                                               dwPayload.input_content_type = payload.getSourceType();
                                           }
                                           dwPayload.output_content_type = "application/json";
                                                                      
                                           //TODO: Verify the spell conforms to Dataweave 2.x: https://docs.mulesoft.com/mule-runtime/4.4/migration-dataweave
                                           dwPayload.spell = "%dw 1.0\\n%output application/json\\n---\\n{\\n    action_Code: 10,\\n    returnCode:  20\\n}";
                                           dwPayload.input_data = payload.getPayload();
                                           String body;
                                                                      
                                           try {
                                               requestBuilder = HttpRequest.newBuilder(new URI(url));
                                               ObjectMapper om = new ObjectMapper();
                                               body = om.writeValueAsString(dwPayload);
                                           } catch (Exception e) {
                                               System.out.println("Error sending request: " + e.toString());
                                               return null;
                                           }
                                                                      
                                           requestBuilder.setHeader("content-type", "application/json");
                                           requestBuilder.setHeader("ce-specversion", "1.0");
                                           requestBuilder.setHeader("ce-source", payload.getSource());
                                           requestBuilder.setHeader("ce-type", "io.triggermesh.dataweave.transform");
                                           requestBuilder.setHeader("ce-id", payload.getId());
                                                                      
                                           HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body)).build();
                                                                      
                                           try {
                                               HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                                               // TODO: verify the response status and body
                                               return response.body();
                                           } catch (Exception e) {
                                               System.out.println("Error sending event: " + e.toString());
                                               return null;
                                           }
                                       }
                                   }
                                   """);
        disableTriggerMeshTransform();
    }

    @Test
    public void shouldTransformDWLWithFileWithSetPayload() {
        final String dwlXMLWithExternalFile = """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw" xmlns:http="http://www.mulesoft.org/schema/mule/http"
                      xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                      xmlns:spring="http://www.springframework.org/schema/beans"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd">
                    <flow name="dwlFlow">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/dwl" doc:name="HTTP"/>
                                
                        <logger message="payload to be sent: #[new String(payload)]" level="INFO"
                                doc:name="Log the message content to be sent"/>
                                
                        <dw:transform-message doc:name="action transform via file">
                            <dw:input-payload mimeType="text/plain">
                                <dw:reader-property name="schemaPath" value="schemas/MQOutput.ffd"/>
                            </dw:input-payload>
                            <dw:set-payload resource="classpath:dwl/mapClientRiskRatingResponse.dwl"/>
                        </dw:transform-message>
                                
                        <logger message="payload to be sent: #[new String(payload)]" level="INFO"
                                doc:name="Log the message content to be sent"/>
                    </flow>
                </mule>
                """;
        addXMLFileToResource(dwlXMLWithExternalFile);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(2);
        assertThat(getGeneratedJavaFile())
                .isEqualTo("""
                        package com.example.javadsl;
                        import org.springframework.context.annotation.Bean;
                        import org.springframework.context.annotation.Configuration;
                        import org.springframework.integration.dsl.IntegrationFlow;
                        import org.springframework.integration.dsl.IntegrationFlows;
                        import org.springframework.integration.handler.LoggingHandler;
                        import org.springframework.integration.http.dsl.Http;
                                                
                        @Configuration
                        public class FlowConfigurations {
                            @Bean
                            IntegrationFlow dwlFlow() {
                                return IntegrationFlows.from(Http.inboundGateway("/dwl")).handle((p, h) -> p)
                                        .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                        .transform(MapClientRiskRatingResponseTransform::transform)
                                        .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                        .get();
                            }
                        }""");
        assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                .isEqualTo("""
                                   package com.example.javadsl;
                                                                      
                                   public class MapClientRiskRatingResponseTransform {
                                       /*
                                        * TODO:
                                        *
                                        * Please add necessary transformation for below snippet
                                        * from file dwl/mapClientRiskRatingResponse.dwl     * */
                                       public static MapClientRiskRatingResponseTransform transform(Object payload) {
                                                                      
                                           return new MapClientRiskRatingResponseTransform();
                                       }
                                   }""");
    }

    @Test
    public void shouldTranslateDWLTransformationWithOnlyOneSetVariable() {
        String muleXMLSetVariable = """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw" xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                    xmlns:spring="http://www.springframework.org/schema/beans"\s
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd">
                    <flow name="dwlFlow">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/dwl" doc:name="HTTP"/>
                
                        <dw:transform-message doc:name="action transform">
                        <dw:set-variable variableName="temp"><![CDATA[%dw 1.0
                %output application/json
                ---
                {
                    action_Code: 10,
                    returnCode:  20
                }]]>
                    
                        </dw:set-variable>
                        </dw:transform-message>
                    
                         <logger message="Hello World:  #[flowVars.temp]" level="INFO" doc:name="Log the message content to be sent"/>
                    </flow>
                </mule>
                """;
        addXMLFileToResource(muleXMLSetVariable);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
        assertThat(getGeneratedJavaFile())
                .isEqualTo("""
                        package com.example.javadsl;
                        import org.springframework.context.annotation.Bean;
                        import org.springframework.context.annotation.Configuration;
                        import org.springframework.integration.dsl.IntegrationFlow;
                        import org.springframework.integration.dsl.IntegrationFlows;
                        import org.springframework.integration.handler.LoggingHandler;
                        import org.springframework.integration.http.dsl.Http;
                                                
                        @Configuration
                        public class FlowConfigurations {
                            @Bean
                            IntegrationFlow dwlFlow() {
                                return IntegrationFlows.from(Http.inboundGateway("/dwl")).handle((p, h) -> p)
                                        // FIXME: No support for following DW transformation: <dw:set-property/> <dw:set-session-variable /> <dw:set-variable />
                                        .log(LoggingHandler.Level.INFO, "Hello World:  ${flowVars.temp}")
                                        .get();
                            }
                        }""");
    }

    @Test
    public void shouldNotErrorWhenDWLFileHasDash() {
        final String dwlExternalFileSpecialChars = """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw" xmlns:http="http://www.mulesoft.org/schema/mule/http"
                      xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                      xmlns:spring="http://www.springframework.org/schema/beans"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd">
                    <flow name="dwlFlow">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/dwl" doc:name="HTTP"/>
                                
                        <logger message="payload to be sent: #[new String(payload)]" level="INFO"
                                doc:name="Log the message content to be sent"/>
                                
                        <dw:transform-message doc:name="action transform via file">
                            <dw:input-payload mimeType="text/plain">
                                <dw:reader-property name="schemaPath" value="schemas/MQOutput.ffd"/>
                            </dw:input-payload>
                            <dw:set-payload resource="classpath:dwl/map-client-risk-rating-response.dwl"/>
                        </dw:transform-message>
                                
                        <logger message="payload to be sent: #[new String(payload)]" level="INFO"
                                doc:name="Log the message content to be sent"/>
                    </flow>
                </mule>
                """;
        addXMLFileToResource(dwlExternalFileSpecialChars);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(2);
        assertThat(getGeneratedJavaFile())
                .isEqualTo("""
                        package com.example.javadsl;
                        import org.springframework.context.annotation.Bean;
                        import org.springframework.context.annotation.Configuration;
                        import org.springframework.integration.dsl.IntegrationFlow;
                        import org.springframework.integration.dsl.IntegrationFlows;
                        import org.springframework.integration.handler.LoggingHandler;
                        import org.springframework.integration.http.dsl.Http;
                                                
                        @Configuration
                        public class FlowConfigurations {
                            @Bean
                            IntegrationFlow dwlFlow() {
                                return IntegrationFlows.from(Http.inboundGateway("/dwl")).handle((p, h) -> p)
                                        .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                        .transform(MapclientriskratingresponseTransform::transform)
                                        .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                        .get();
                            }
                        }""");
        assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                .isEqualTo("""
                                   package com.example.javadsl;
                                                                      
                                   public class MapclientriskratingresponseTransform {
                                       /*
                                        * TODO:
                                        *
                                        * Please add necessary transformation for below snippet
                                        * from file dwl/map-client-risk-rating-response.dwl     * */
                                       public static MapclientriskratingresponseTransform transform(Object payload) {
                                                                      
                                           return new MapclientriskratingresponseTransform();
                                       }
                                   }""");
    }

    @Test
    public void multipleDWLTransformInSameFlowShouldProduceMultipleClasses() {
        final String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <mule xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd">
                    <flow name="multipleTransforms">
                        <dw:transform-message doc:name="Transform Message">
                            <dw:set-payload><![CDATA[%dw 1.0
                %output application/json indent = true, skipNullOn = "everywhere"
                ---
                {
                    "hello": {
                        "world": {
                            "hello": "indeed!",
                        },
                    }
                }]]></dw:set-payload>
                        </dw:transform-message>
                        <logger />
                        <dw:transform-message doc:name="Build Response Message">
                            <dw:set-payload><![CDATA[%dw 1.0
                %output application/json indent = true, skipNullOn = "everywhere"
                ---
                {
                    "responseBody": {
                        "responseInfo": {
                            "responseStatus": "200"
                        },
                    }
                }]]></dw:set-payload>
                        </dw:transform-message>
                    </flow>
                </mule>
                """;

        addXMLFileToResource(xml);
        runAction();

        assertThat(projectContext.getProjectJavaSources().list()).hasSize(3);
        assertThat(projectContext.getProjectJavaSources().list().get(0).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.FlowConfigurations");
        assertThat(projectContext.getProjectJavaSources().list().get(2).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.MultipleTransformsTransform_2");
        assertThat(projectContext.getProjectJavaSources().list().get(1).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.MultipleTransformsTransform_0");
    }

    @Test
    public void multipleDWLTransformInSameFlowShouldProduceMultipleClassesWithTriggerMeshEnabled() {
        enableTriggerMeshTransform();

        final String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <mule xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd">
                    <flow name="multipleTransforms">
                        <dw:transform-message doc:name="Transform Message">
                            <dw:set-payload><![CDATA[%dw 1.0
                %output application/json indent = true, skipNullOn = "everywhere"
                ---
                {
                    "hello": {
                        "world": {
                            "hello": "indeed!",
                        },
                    }
                }]]></dw:set-payload>
                        </dw:transform-message>
                        <logger />
                        <dw:transform-message doc:name="Build Response Message">
                            <dw:set-payload><![CDATA[%dw 1.0
                %output application/json indent = true, skipNullOn = "everywhere"
                ---
                {
                    "responseBody": {
                        "responseInfo": {
                            "responseStatus": "200"
                        },
                    }
                }]]></dw:set-payload>
                        </dw:transform-message>
                    </flow>
                </mule>
                """;

        addXMLFileToResource(xml);
        runAction();

        assertThat(projectContext.getProjectJavaSources().list()).hasSize(4);
        assertThat(projectContext.getProjectJavaSources().list().get(0).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.FlowConfigurations");
        assertThat(projectContext.getProjectJavaSources().list().get(1).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.TmDwPayload");
        assertThat(projectContext.getProjectJavaSources().list().get(2).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.MultipleTransformsTransformTM_2");
        assertThat(projectContext.getProjectJavaSources().list().get(3).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.MultipleTransformsTransformTM_0");

        disableTriggerMeshTransform();
    }
}
