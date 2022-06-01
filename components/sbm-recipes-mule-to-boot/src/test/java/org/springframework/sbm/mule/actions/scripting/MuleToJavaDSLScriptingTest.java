package org.springframework.sbm.mule.actions.scripting;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLScriptingTest extends JavaDSLActionBaseTest {

    @Test
    public void sbmAcknowledgesScriptTag() {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<mule xmlns:scripting=\"http://www.mulesoft.org/schema/mule/scripting\"\n" +
                "\txmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:api-platform-gw=\"http://www.mulesoft.org/schema/mule/api-platform-gw\" xmlns:apikit=\"http://www.mulesoft.org/schema/mule/apikit\" xmlns:cmis=\"http://www.mulesoft.org/schema/mule/cmis\" xmlns:context=\"http://www.springframework.org/schema/context\" xmlns:db=\"http://www.mulesoft.org/schema/mule/db\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\" xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns:ee=\"http://www.mulesoft.org/schema/mule/ee/core\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:spring=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/scripting http://www.mulesoft.org/schema/mule/scripting/current/mule-scripting.xsd\n" +
                "http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/api-platform-gw http://www.mulesoft.org/schema/mule/api-platform-gw/current/mule-api-platform-gw.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\n" +
                "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/apikit http://www.mulesoft.org/schema/mule/apikit/current/mule-apikit.xsd\n" +
                "http://www.mulesoft.org/schema/mule/cmis http://www.mulesoft.org/schema/mule/cmis/current/mule-cmis.xsd\n" +
                "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/core http://www.mulesoft.org/schema/mule/ee/core/current/mule-ee.xsd\">\n" +
                "    \n" +
                "    <flow name=\"get:/canary/{birdName}:cmb-hsbcnet-ss-sa-entitlement-change-request-config\">\n" +
                "        <choice doc:name=\"Choice\">\n" +
                "            <when expression=\"#[message.inboundProperties.'http.uri.params'.birdName ==&quot;tweety&quot;]\">\n" +
                "                <set-payload value=\"cmb-hsbcnet-ss-sa-entitlement-change-request-v${majorVersion}\" doc:name=\"Set Payload\"/>\n" +
                "            </when>\n" +
                "            <when expression=\"#[message.inboundProperties.'http.uri.params'.birdName !=&quot;tweety&quot;]\">\n" +
                "                <scripting:component doc:name=\"Groovy\">\n" +
                "                    <scripting:script engine=\"Groovy\"><![CDATA[throw new javax.ws.rs.BadRequestException();]]></scripting:script>\n" +
                "                </scripting:component>\n" +
                "            </when>\n" +
                "            <otherwise>\n" +
                "                <set-payload value=\"sa-cmb-entitlements falied\" doc:name=\"Set Payload\"/>\n" +
                "            </otherwise>\n" +
                "        </choice>\n" +
                "    </flow>\n" +
                "</mule>";
        addXMLFileToResource(xml);
        runAction();

        assertThat(getGeneratedJavaFile()).isEqualTo("ds");
    }
}
