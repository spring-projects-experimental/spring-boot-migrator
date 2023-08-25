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
package org.springframework.sbm.jee.jaxws;

import org.springframework.sbm.GitHubIssue;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.parsers.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openrewrite.xml.tree.Xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alex Boyko
 */
public class WebServiceDescriptorTest {

    @Test
    void wsdlParsing() throws Exception {
        Xml.Document doc = GenerateWebServices.parseWsdl(Path.of("../../testcode/simple-webservice/calculator.wsdl"), new RewriteExecutionContext());
        WebServiceDescriptor d = new WebServiceDescriptor(null, null, doc, null);
        assertThat(d.getNsUri()).isEqualTo("http://superbiz.org/wsdl");
        assertThat(d.getPackageName()).isEqualTo("org.superbiz.wsdl");
        assertThat(d.getPathContext()).isEqualTo("simple-webservice");
        assertThat(d.getWsdlDefBeanName()).isEqualTo("Calculator");
    }

    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/286")
    @Test
    void wsdlParsing2(@TempDir Path tmpDir) throws IOException {
        String typeAnnotatedAsWebService =
                "import javax.jws.WebMethod;\n" +
                        "import javax.jws.WebParam;\n" +
                        "import javax.jws.WebResult;\n" +
                        "import javax.jws.WebService;\n" +
                        "@WebService(targetNamespace = \"http://foo.com/jee/jaxws\")\n" +
                        "public interface CalculatorWs {\n" +
                        "\n" +
                        "    @WebResult(name = \"additionResult\")\n" +
                        "    @WebMethod(operationName = \"addition\")\n" +
                        "    Integer sum(@WebParam(name = \"firstNumber\") int add1, @WebParam(name = \"secondNumber\") int add2);\n" +
                        "\n" +
                        "    Integer multiply(int mul1, int mul2);\n" +
                        "}";

        String wsdl = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<definitions\n" +
                "        xmlns:wsam=\"http://www.w3.org/2007/05/addressing/metadata\" xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\"\n" +
                "        xmlns:tns=\"http://foo.com/jee/jaxws\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "        xmlns=\"http://schemas.xmlsoap.org/wsdl/\" targetNamespace=\"http://foo.com/jee/jaxws\" name=\"CalculatorService\">\n" +
                "    <types>\n" +
                "        <xsd:schema>\n" +
                "            <xsd:import namespace=\"http://foo.com/jee/jaxws\"\n" +
                "                        schemaLocation=\"http://localhost:7001/Calculator/CalculatorService?xsd=1\"/>\n" +
                "        </xsd:schema>\n" +
                "    </types>\n" +
                "    <message name=\"multiply\">\n" +
                "        <part name=\"parameters\" element=\"tns:multiply\"/>\n" +
                "    </message>\n" +
                "    <message name=\"multiplyResponse\">\n" +
                "        <part name=\"parameters\" element=\"tns:multiplyResponse\"/>\n" +
                "    </message>\n" +
                "    <message name=\"addition\">\n" +
                "        <part name=\"parameters\" element=\"tns:addition\"/>\n" +
                "    </message>\n" +
                "    <message name=\"additionResponse\">\n" +
                "        <part name=\"parameters\" element=\"tns:additionResponse\"/>\n" +
                "    </message>\n" +
                "    <portType name=\"CalculatorWs\">\n" +
                "        <operation name=\"multiply\">\n" +
                "            <input wsam:Action=\"http://foo.com/jee/jaxws/CalculatorWs/multiplyRequest\" message=\"tns:multiply\"/>\n" +
                "            <output wsam:Action=\"http://foo.com/jee/jaxws/CalculatorWs/multiplyResponse\"\n" +
                "                    message=\"tns:multiplyResponse\"/>\n" +
                "        </operation>\n" +
                "        <operation name=\"addition\">\n" +
                "            <input wsam:Action=\"http://foo.com/jee/jaxws/CalculatorWs/additionRequest\" message=\"tns:addition\"/>\n" +
                "            <output wsam:Action=\"http://foo.com/jee/jaxws/CalculatorWs/additionResponse\"\n" +
                "                    message=\"tns:additionResponse\"/>\n" +
                "        </operation>\n" +
                "    </portType>\n" +
                "    <binding name=\"CalculatorPortBinding\" type=\"tns:CalculatorWs\">\n" +
                "        <soap:binding transport=\"http://schemas.xmlsoap.org/soap/http\" style=\"document\"/>\n" +
                "        <operation name=\"multiply\">\n" +
                "            <soap:operation soapAction=\"\"/>\n" +
                "            <input>\n" +
                "                <soap:body use=\"literal\"/>\n" +
                "            </input>\n" +
                "            <output>\n" +
                "                <soap:body use=\"literal\"/>\n" +
                "            </output>\n" +
                "        </operation>\n" +
                "        <operation name=\"addition\">\n" +
                "            <soap:operation soapAction=\"\"/>\n" +
                "            <input>\n" +
                "                <soap:body use=\"literal\"/>\n" +
                "            </input>\n" +
                "            <output>\n" +
                "                <soap:body use=\"literal\"/>\n" +
                "            </output>\n" +
                "        </operation>\n" +
                "    </binding>\n" +
                "    <service name=\"CalculatorService\">\n" +
                "        <port name=\"CalculatorPort\" binding=\"tns:CalculatorPortBinding\">\n" +
                "            <soap:address location=\"http://localhost:7001/Calculator/CalculatorService\"/>\n" +
                "        </port>\n" +
                "    </service>\n" +
                "</definitions>";

        Path wsdlPath = tmpDir.resolve("calculator.wsdl");
        Files.write(wsdlPath, wsdl.getBytes());

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(typeAnnotatedAsWebService)
                .withBuildFileHavingDependencies("javax:javaee-api:8.0.1")
                .build();

        Type openRewriteType = projectContext.getProjectJavaSources().list().get(0).getTypes().get(0);

        Xml.Document doc = GenerateWebServices.parseWsdl(wsdlPath, new RewriteExecutionContext());
        WebServiceDescriptor d = new WebServiceDescriptor(openRewriteType, null, doc, null);
        assertThat(d.getNsUri()).isEqualTo("http://foo.com/jee/jaxws");
        assertThat(d.getPackageName()).isEqualTo("com.foo.jee.jaxws");
        assertThat(d.getPathContext()).isEqualTo("Calculator"); // error
        assertThat(d.getWsdlDefBeanName()).isEqualTo("CalculatorService");
    }


}
