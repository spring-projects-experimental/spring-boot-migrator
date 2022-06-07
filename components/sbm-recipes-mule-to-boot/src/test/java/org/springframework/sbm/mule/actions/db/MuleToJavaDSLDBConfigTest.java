package org.springframework.sbm.mule.actions.db;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;


public class MuleToJavaDSLDBConfigTest extends JavaDSLActionBaseTest {

    @Test
    public void fillApplicationPropertiesForDBConnection() {
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
                "    <db:oracle-config name=\"Oracle_Configuration\" dataSource-ref=\"oracleConnectorUMT\" doc:name=\"Oracle Configuration\" />\n" +
                "    <http:listener-config name=\"http-lc-0.0.0.0-8081\" host=\"0.0.0.0\" port=\"8080\" protocol=\"HTTP\"/>\n" +
                "</mule>";

        addXMLFileToResource(xml);
        runAction();

        Assertions.assertThat(getApplicationPropertyContent()).isEqualTo("server.port=8080\n" +
                "spring.datasource.url=--INSERT--DB-URL-HERE-Example:jdbc:mysql://localhost:3306/mulemigration\n" +
                "spring.datasource.username=--INSERT-USER-NAME--\n" +
                "spring.datasource.password=--INSERT-PASSWORD--\n" +
                "spring.datasource.driverClassName=--INSERT-DRIVER-CLASS: Mysql Example: com.mysql.jdbc.Driver, Oracle: oracle.jdbc.OracleDriver\n" +
                "spring.jpa.show-sql=true"
        );
    }
}
