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
package org.springframework.sbm.jee.jaxws;

import org.springframework.sbm.engine.recipe.UserInteractions;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.TestProjectContext;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Alex Boyko
 */
@SpringBootTest
@Disabled("https://github.com/pivotal/spring-boot-migrator/issues/244")
public class GenerateWebServicesTest {

    @SpringBootApplication
    public static class TestApp {
    }

    @MockBean
    UserInteractions ui;

    @Autowired
    private Configuration configuration;

    private GenerateWebServices action;

    @BeforeEach
    void setUp() {
        if (action == null) {
            action = new GenerateWebServices(ui, configuration);
        }
    }


    @Test
    void sanity() throws Exception {

        String javaClass = "package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "import javax.jws.WebService;\n" +
                "\n" +
                "@Stateless\n" +
                "@WebService(\n" +
                "        portName = \"CalculatorPort\",\n" +
                "        serviceName = \"CalculatorService\",\n" +
                "        targetNamespace = \"http://superbiz.org/wsdl\",\n" +
                "        endpointInterface = \"org.superbiz.calculator.ws.CalculatorWs\")\n" +
                "public class Calculator implements CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2) {\n" +
                "        return add1 + add2;\n" +
                "    }\n" +
                "\n" +
                "    public int multiply(int mul1, int mul2) {\n" +
                "        return mul1 * mul2;\n" +
                "    }\n" +
                "}\n";

        String endpointInterface = "package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.jws.WebService;\n" +
                "\n" +
                "@WebService(targetNamespace = \"http://superbiz.org/wsdl\")\n" +
                "public interface CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2);\n" +
                "\n" +
                "    public int multiply(int mul1, int mul2);\n" +
                "}";

        String pom = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "  <groupId>org.superbiz</groupId>\n" +
                "  <artifactId>simple-webservice</artifactId>\n" +
                "  <packaging>jar</packaging>\n" +
                "  <version>8.0.8-SNAPSHOT</version>\n" +
                "  <name>TomEE :: Examples :: Simple Webservice</name>\n" +
                "  <properties>\n" +
                "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "  </properties>\n" +
                "  <build>\n" +
                "    <plugins>\n" +
                "      <plugin>\n" +
                "        <groupId>org.apache.maven.plugins</groupId>\n" +
                "        <artifactId>maven-compiler-plugin</artifactId>\n" +
                "        <version>3.5.1</version>\n" +
                "        <configuration>\n" +
                "          <source>1.8</source>\n" +
                "          <target>1.8</target>\n" +
                "        </configuration>\n" +
                "      </plugin>\n" +
                "      <plugin>\n" +
                "        <groupId>org.tomitribe.transformer</groupId>\n" +
                "        <artifactId>org.eclipse.transformer.maven</artifactId>\n" +
                "        <version>0.1.1a</version>\n" +
                "        <configuration>\n" +
                "          <classifier>jakartaee9</classifier>\n" +
                "        </configuration>\n" +
                "        <executions>\n" +
                "          <execution>\n" +
                "            <goals>\n" +
                "              <goal>run</goal>\n" +
                "            </goals>\n" +
                "            <phase>package</phase>\n" +
                "          </execution>\n" +
                "        </executions>\n" +
                "      </plugin>\n" +
                "    </plugins>\n" +
                "  </build>\n" +
                "</project>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pom)
                .withBuildFileHavingDependencies("org.apache.tomee.bom:tomee-plus-api:8.0.7")
                .withJavaSources(javaClass, endpointInterface)
                .build();

        String wsdlPathStr = "./testcode/wsdl/calculator.wsdl";
        String question = String.format(GenerateWebServices.QUESTION, "Calculator");
        when(ui.askForInput(question)).thenReturn(wsdlPathStr);

        action.apply(projectContext);

        assertThat(projectContext.getBuildFile().print()).isEqualTo("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "  <groupId>org.superbiz</groupId>\n" +
                "  <artifactId>simple-webservice</artifactId>\n" +
                "  <packaging>jar</packaging>\n" +
                "  <version>8.0.8-SNAPSHOT</version>\n" +
                "  <name>TomEE :: Examples :: Simple Webservice</name>\n" +
                "  <properties>\n" +
                "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "    <generated-sources.dir>src/generated</generated-sources.dir>\n" +
                "  </properties>\n" +
                "  <build>\n" +
                "    <plugins>\n" +
                "      <plugin>\n" +
                "        <groupId>org.apache.maven.plugins</groupId>\n" +
                "        <artifactId>maven-compiler-plugin</artifactId>\n" +
                "        <version>3.5.1</version>\n" +
                "        <configuration>\n" +
                "          <source>1.8</source>\n" +
                "          <target>1.8</target>\n" +
                "        </configuration>\n" +
                "      </plugin>\n" +
                "      <plugin>\n" +
                "        <groupId>org.tomitribe.transformer</groupId>\n" +
                "        <artifactId>org.eclipse.transformer.maven</artifactId>\n" +
                "        <version>0.1.1a</version>\n" +
                "        <configuration>\n" +
                "          <classifier>jakartaee9</classifier>\n" +
                "        </configuration>\n" +
                "        <executions>\n" +
                "          <execution>\n" +
                "            <goals>\n" +
                "              <goal>run</goal>\n" +
                "            </goals>\n" +
                "            <phase>package</phase>\n" +
                "          </execution>\n" +
                "        </executions>\n" +
                "      </plugin>\n" +
                "      <plugin>\n" +
                "        <groupId>org.jvnet.jaxb2.maven2</groupId>\n" +
                "        <artifactId>maven-jaxb2-plugin</artifactId>\n" +
                "        <version>0.14.0</version>\n" +
                "        <executions>\n" +
                "          <execution>\n" +
                "            <goals>\n" +
                "              <goal>generate</goal>\n" +
                "            </goals>\n" +
                "            <configuration>\n" +
                "              <schemaDirectory>${project.basedir}/src/main/resources</schemaDirectory>\n" +
                "              <schemaIncludes>*.wsdl</schemaIncludes>\n" +
                "              <generateDirectory>${generated-sources.dir}</generateDirectory>\n" +
                "              <generatePackage>org.superbiz.wsdl</generatePackage></configuration></execution>\n" +
                "        </executions>\n" +
                "      </plugin>\n" +
                "      <plugin>\n" +
                "        <groupId>org.codehaus.mojo</groupId>\n" +
                "        <artifactId>build-helper-maven-plugin</artifactId>\n" +
                "        <executions>\n" +
                "          <execution>\n" +
                "            <goals>\n" +
                "              <goal>add-source</goal>\n" +
                "            </goals>\n" +
                "            <phase>generate-sources</phase><configuration>\n" +
                "              <sources>\n" +
                "                <source>${generated-sources.dir}</source>\n" +
                "              </sources>\n" +
                "            </configuration></execution>\n" +
                "        </executions>\n" +
                "      </plugin>\n" +
                "    </plugins>\n" +
                "  </build>\n" +
                "</project>");

        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(4);

        assertThat(projectContext.getProjectJavaSources().list().get(0).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "\n" +
                "@Stateless\n" +
                "public class Calculator implements CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2) {\n" +
                "        return add1 + add2;\n" +
                "    }\n" +
                "\n" +
                "    public int multiply(int mul1, int mul2) {\n" +
                "        return mul1 * mul2;\n" +
                "    }\n" +
                "}\n");

        assertThat(projectContext.getProjectJavaSources().list().get(1).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "public interface CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2);\n" +
                "\n" +
                "    public int multiply(int mul1, int mul2);\n" +
                "}");

        assertThat(projectContext.getProjectJavaSources().list().get(2).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import org.springframework.boot.web.servlet.ServletRegistrationBean;\n" +
                "import org.springframework.context.ApplicationContext;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.core.io.ClassPathResource;\n" +
                "import org.springframework.ws.config.annotation.EnableWs;\n" +
                "import org.springframework.ws.config.annotation.WsConfigurerAdapter;\n" +
                "import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;\n" +
                "import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;\n" +
                "import org.springframework.ws.transport.http.MessageDispatcherServlet;\n" +
                "\n" +
                "@EnableWs\n" +
                "@Configuration\n" +
                "public class WebServiceConfig extends WsConfigurerAdapter {\n" +
                "\n" +
                "    @Bean\n" +
                "    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {\n" +
                "        MessageDispatcherServlet servlet = new MessageDispatcherServlet();\n" +
                "        servlet.setApplicationContext(applicationContext);\n" +
                "        servlet.setTransformWsdlLocations(true);\n" +
                "        return new ServletRegistrationBean<>(servlet, \"/simple-webservice/*\");\n" +
                "    }\n" +
                "\n" +
                "    @Bean(name = \"Calculator\")\n" +
                "    SimpleWsdl11Definition calculator() {\n" +
                "        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();\n" +
                "        wsdl11Definition.setWsdl(new ClassPathResource(\"calculator.wsdl\"));\n" +
                "        return wsdl11Definition;\n" +
                "    }\n" +
                "\n" +
                "}\n");

        assertThat(projectContext.getProjectJavaSources().list().get(3).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import org.springframework.ws.server.endpoint.annotation.Endpoint;\n" +
                "import org.springframework.ws.server.endpoint.annotation.PayloadRoot;\n" +
                "import org.springframework.ws.server.endpoint.annotation.RequestPayload;\n" +
                "import org.springframework.ws.server.endpoint.annotation.ResponsePayload;\n" +
                "import org.superbiz.calculator.ws.CalculatorWs;\n" +
                "import org.superbiz.wsdl.Multiply;\n" +
                "import org.superbiz.wsdl.MultiplyResponse;\n" +
                "import org.superbiz.wsdl.Sum;\n" +
                "import org.superbiz.wsdl.SumResponse;\n" +
                "\n" +
                "@Endpoint\n" +
                "public class CalculatorWsEndpoint {\n" +
                "\n" +
                "    private static final String NAMESPACE_URI = \"http://superbiz.org/wsdl\";\n" +
                "\n" +
                "    private CalculatorWs calculatorWs;\n" +
                "\n" +
                "    CalculatorWsEndpoint(CalculatorWs calculatorWs) {\n" +
                "        this.calculatorWs = calculatorWs;\n" +
                "    }\n" +
                "\n" +
                "    @PayloadRoot(namespace = NAMESPACE_URI, localPart = \"sum\")\n" +
                "    @ResponsePayload\n" +
                "    public SumResponse sum(@RequestPayload Sum request) {\n" +
                "        SumResponse response = new SumResponse();\n" +
                "        response.setReturn(calculatorWs.sum(request.getArg0(), request.getArg1()));\n" +
                "        return response;\n" +
                "    }\n" +
                "\n" +
                "    @PayloadRoot(namespace = NAMESPACE_URI, localPart = \"multiply\")\n" +
                "    @ResponsePayload\n" +
                "    public MultiplyResponse multiply(@RequestPayload Multiply request) {\n" +
                "        MultiplyResponse response = new MultiplyResponse();\n" +
                "        response.setReturn(calculatorWs.multiply(request.getArg0(), request.getArg1()));\n" +
                "        return response;\n" +
                "    }\n" +
                "\n" +
                "}\n");

        Optional<RewriteSourceFileHolder<?>> wsdl = projectContext.getProjectResources().stream()
                .filter(r -> r.getAbsolutePath().endsWith(Path.of("src/main/resources/calculator.wsdl")))
                .findFirst();

        assertThat(wsdl).isPresent();

        assertThat(wsdl.get().print().trim()).isEqualTo(IOUtils.toString(Files.newInputStream(Path.of(wsdlPathStr)), Charset.defaultCharset().name()).trim());

        verify(ui).askForInput(question);
        verifyNoMoreInteractions(ui);

    }

    @Test
    void noMavenChangesToGeneratePojo() throws Exception {

        String javaClass = "package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "import javax.jws.WebService;\n" +
                "\n" +
                "@Stateless\n" +
                "@WebService(\n" +
                "        portName = \"CalculatorPort\",\n" +
                "        serviceName = \"CalculatorService\",\n" +
                "        targetNamespace = \"http://superbiz.org/wsdl\",\n" +
                "        endpointInterface = \"org.superbiz.calculator.ws.CalculatorWs\")\n" +
                "public class Calculator implements CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2) {\n" +
                "        return add1 + add2;\n" +
                "    }\n" +
                "\n" +
                "    public int multiply(int mul1, int mul2) {\n" +
                "        return mul1 * mul2;\n" +
                "    }\n" +
                "}\n";


        String endpointInterface = "package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.jws.WebService;\n" +
                "\n" +
                "@WebService(targetNamespace = \"http://superbiz.org/wsdl\")\n" +
                "public interface CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2);\n" +
                "\n" +
                "    public int multiply(int mul1, int mul2);\n" +
                "}";

        String pojo = "" +
                "package org.superbiz.wsdl;\n" +
                "\n" +
                "import javax.xml.bind.annotation.XmlAccessType;\n" +
                "import javax.xml.bind.annotation.XmlAccessorType;\n" +
                "import javax.xml.bind.annotation.XmlType;\n" +
                "\n" +
                "@XmlAccessorType(XmlAccessType.FIELD)\n" +
                "@XmlType(name = \"sum\", propOrder = {\n" +
                "    \"arg0\",\n" +
                "    \"arg1\"\n" +
                "})\n" +
                "public class Sum {\n" +
                "\n" +
                "    protected int arg0;\n" +
                "    protected int arg1;\n" +
                "\n" +
                "    /**\n" +
                "     * Gets the value of the arg0 property.\n" +
                "     * \n" +
                "     */\n" +
                "    public int getArg0() {\n" +
                "        return arg0;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Sets the value of the arg0 property.\n" +
                "     * \n" +
                "     */\n" +
                "    public void setArg0(int value) {\n" +
                "        this.arg0 = value;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Gets the value of the arg1 property.\n" +
                "     * \n" +
                "     */\n" +
                "    public int getArg1() {\n" +
                "        return arg1;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Sets the value of the arg1 property.\n" +
                "     * \n" +
                "     */\n" +
                "    public void setArg1(int value) {\n" +
                "        this.arg1 = value;\n" +
                "    }\n" +
                "\n" +
                "}\n";

        String pom = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "  <groupId>org.superbiz</groupId>\n" +
                "  <artifactId>simple-webservice</artifactId>\n" +
                "  <packaging>jar</packaging>\n" +
                "  <version>8.0.8-SNAPSHOT</version>\n" +
                "  <name>TomEE :: Examples :: Simple Webservice</name>\n" +
                "  <properties>\n" +
                "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "  </properties>\n" +
                "  <build>\n" +
                "    <plugins>\n" +
                "    </plugins>\n" +
                "  </build>\n" +
                "</project>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pom)
                .withBuildFileHavingDependencies("org.apache.tomee.bom:tomee-plus-api:8.0.7")
                .withJavaSources(javaClass, endpointInterface, pojo)
                .build();

        String wsdlPathStr = "./testcode/wsdl/calculator.wsdl";
        String question = String.format(GenerateWebServices.QUESTION, "Calculator");
        when(ui.askForInput(question)).thenReturn(wsdlPathStr);

        action.apply(projectContext);

        // No changes to pom - as POJO code is supposed to be present
        assertThat(projectContext.getBuildFile().print()).isEqualTo(pom);

        verify(ui).askForInput(question);
        verifyNoMoreInteractions(ui);

    }

    @Test
    void oneWayAnnotation() throws Exception {

        String javaClass = "package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "import javax.jws.WebService;\n" +
                "\n" +
                "@Stateless\n" +
                "@WebService(\n" +
                "        portName = \"CalculatorPort\",\n" +
                "        serviceName = \"CalculatorService\",\n" +
                "        targetNamespace = \"http://superbiz.org/wsdl\",\n" +
                "        endpointInterface = \"org.superbiz.calculator.ws.CalculatorWs\")\n" +
                "public class Calculator implements CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2) {\n" +
                "        return add1 + add2;\n" +
                "    }\n" +
                "}\n";

        String endpointInterface = "package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.jws.Oneway;\n" +
                "import javax.jws.WebService;\n" +
                "\n" +
                "@WebService(targetNamespace = \"http://superbiz.org/wsdl\")\n" +
                "public interface CalculatorWs {\n" +
                "\n" +
                "    @Oneway\n" +
                "    public int sum(int add1, int add2);\n" +
                "}";

        String pom = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "  <groupId>org.superbiz</groupId>\n" +
                "  <artifactId>simple-webservice</artifactId>\n" +
                "  <packaging>jar</packaging>\n" +
                "  <version>8.0.8-SNAPSHOT</version>\n" +
                "  <name>TomEE :: Examples :: Simple Webservice</name>\n" +
                "  <properties>\n" +
                "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "  </properties>\n" +
                "  <build>\n" +
                "    <plugins>\n" +
                "      <plugin>\n" +
                "        <groupId>org.apache.maven.plugins</groupId>\n" +
                "        <artifactId>maven-compiler-plugin</artifactId>\n" +
                "        <version>3.5.1</version>\n" +
                "        <configuration>\n" +
                "          <source>1.8</source>\n" +
                "          <target>1.8</target>\n" +
                "        </configuration>\n" +
                "      </plugin>\n" +
                "      <plugin>\n" +
                "        <groupId>org.tomitribe.transformer</groupId>\n" +
                "        <artifactId>org.eclipse.transformer.maven</artifactId>\n" +
                "        <version>0.1.1a</version>\n" +
                "        <configuration>\n" +
                "          <classifier>jakartaee9</classifier>\n" +
                "        </configuration>\n" +
                "        <executions>\n" +
                "          <execution>\n" +
                "            <goals>\n" +
                "              <goal>run</goal>\n" +
                "            </goals>\n" +
                "            <phase>package</phase>\n" +
                "          </execution>\n" +
                "        </executions>\n" +
                "      </plugin>\n" +
                "    </plugins>\n" +
                "  </build>\n" +
                "</project>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pom)
                .withBuildFileHavingDependencies("org.apache.tomee.bom:tomee-plus-api:8.0.7")
                .withJavaSources(javaClass, endpointInterface)
                .build();

        String wsdlPathStr = "./testcode/wsdl/calculator.wsdl";
        String question = String.format(GenerateWebServices.QUESTION, "Calculator");
        when(ui.askForInput(question)).thenReturn(wsdlPathStr);

        action.apply(projectContext);

        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(4);

        assertThat(projectContext.getProjectJavaSources().list().get(0).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "\n" +
                "@Stateless\n" +
                "public class Calculator implements CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2) {\n" +
                "        return add1 + add2;\n" +
                "    }\n" +
                "}\n");

        assertThat(projectContext.getProjectJavaSources().list().get(1).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "public interface CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2);\n" +
                "}");

        assertThat(projectContext.getProjectJavaSources().list().get(2).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import org.springframework.boot.web.servlet.ServletRegistrationBean;\n" +
                "import org.springframework.context.ApplicationContext;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.core.io.ClassPathResource;\n" +
                "import org.springframework.ws.config.annotation.EnableWs;\n" +
                "import org.springframework.ws.config.annotation.WsConfigurerAdapter;\n" +
                "import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;\n" +
                "import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;\n" +
                "import org.springframework.ws.transport.http.MessageDispatcherServlet;\n" +
                "\n" +
                "@EnableWs\n" +
                "@Configuration\n" +
                "public class WebServiceConfig extends WsConfigurerAdapter {\n" +
                "\n" +
                "    @Bean\n" +
                "    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {\n" +
                "        MessageDispatcherServlet servlet = new MessageDispatcherServlet();\n" +
                "        servlet.setApplicationContext(applicationContext);\n" +
                "        servlet.setTransformWsdlLocations(true);\n" +
                "        return new ServletRegistrationBean<>(servlet, \"/simple-webservice/*\");\n" +
                "    }\n" +
                "\n" +
                "    @Bean(name = \"Calculator\")\n" +
                "    SimpleWsdl11Definition calculator() {\n" +
                "        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();\n" +
                "        wsdl11Definition.setWsdl(new ClassPathResource(\"calculator.wsdl\"));\n" +
                "        return wsdl11Definition;\n" +
                "    }\n" +
                "\n" +
                "}\n");

        assertThat(projectContext.getProjectJavaSources().list().get(3).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import org.springframework.ws.server.endpoint.annotation.Endpoint;\n" +
                "import org.springframework.ws.server.endpoint.annotation.PayloadRoot;\n" +
                "import org.springframework.ws.server.endpoint.annotation.RequestPayload;\n" +
                "import org.superbiz.calculator.ws.CalculatorWs;\n" +
                "import org.superbiz.wsdl.Sum;\n" +
                "\n" +
                "@Endpoint\n" +
                "public class CalculatorWsEndpoint {\n" +
                "\n" +
                "    private static final String NAMESPACE_URI = \"http://superbiz.org/wsdl\";\n" +
                "\n" +
                "    private CalculatorWs calculatorWs;\n" +
                "\n" +
                "    CalculatorWsEndpoint(CalculatorWs calculatorWs) {\n" +
                "        this.calculatorWs = calculatorWs;\n" +
                "    }\n" +
                "\n" +
                "    @PayloadRoot(namespace = NAMESPACE_URI, localPart = \"sum\")\n" +
                "    public void sum(@RequestPayload Sum request) {\n" +
                "        calculatorWs.sum(request.getArg0(), request.getArg1());\n" +
                "    }\n" +
                "\n" +
                "}\n");

        Optional<RewriteSourceFileHolder<?>> wsdl = projectContext.getProjectResources().stream()
                .filter(r -> r.getAbsolutePath().endsWith(Path.of("src/main/resources/calculator.wsdl")))
                .findFirst();

        assertThat(wsdl).isPresent();

        InputStream inputStream = Files.newInputStream(Path.of(wsdlPathStr));
        assertThat(wsdl.get().print().trim()).isEqualTo(IOUtils.toString(inputStream, Charset.defaultCharset().name()).trim());

        verify(ui).askForInput(question);
        verifyNoMoreInteractions(ui);

    }

    @Test
    void webAnnotations() {

        String javaClass = "package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "import javax.jws.WebService;\n" +
                "\n" +
                "@Stateless\n" +
                "@WebService(\n" +
                "        portName = \"CalculatorPort\",\n" +
                "        serviceName = \"CalculatorService\",\n" +
                "        targetNamespace = \"http://superbiz.org/wsdl\",\n" +
                "        endpointInterface = \"org.superbiz.calculator.ws.CalculatorWs\")\n" +
                "public class Calculator implements CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2) {\n" +
                "        return add1 + add2;\n" +
                "    }\n" +
                "\n" +
                "    public int multiply(int mul1, int mul2) {\n" +
                "        return mul1 * mul2;\n" +
                "    }\n" +
                "}\n";

        String endpointInterface = "package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.jws.WebMethod;\n" +
                "import javax.jws.WebParam;\n" +
                "import javax.jws.WebResult;\n" +
                "import javax.jws.WebService;\n" +
                "\n" +
                "@WebService(targetNamespace = \"http://superbiz.org/wsdl\")\n" +
                "public interface CalculatorWs {\n" +
                "\n" +
                "    @WebMethod(operationName = \"add\")\n" +
                "    @WebResult(name = \"result\")\n" +
                "    public int sum(@WebParam(name = \"firstParameter\") int add1, int add2);\n" +
                "\n" +
                "    public int multiply(int mul1, @WebParam(name = \"times\") int mul2);\n" +
                "}";

        String pom = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "  <groupId>org.superbiz</groupId>\n" +
                "  <artifactId>simple-webservice</artifactId>\n" +
                "  <packaging>jar</packaging>\n" +
                "  <version>8.0.8-SNAPSHOT</version>\n" +
                "  <name>TomEE :: Examples :: Simple Webservice</name>\n" +
                "  <properties>\n" +
                "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "  </properties>\n" +
                "  <build>\n" +
                "    <plugins>\n" +
                "      <plugin>\n" +
                "        <groupId>org.apache.maven.plugins</groupId>\n" +
                "        <artifactId>maven-compiler-plugin</artifactId>\n" +
                "        <version>3.5.1</version>\n" +
                "        <configuration>\n" +
                "          <source>1.8</source>\n" +
                "          <target>1.8</target>\n" +
                "        </configuration>\n" +
                "      </plugin>\n" +
                "      <plugin>\n" +
                "        <groupId>org.tomitribe.transformer</groupId>\n" +
                "        <artifactId>org.eclipse.transformer.maven</artifactId>\n" +
                "        <version>0.1.1a</version>\n" +
                "        <configuration>\n" +
                "          <classifier>jakartaee9</classifier>\n" +
                "        </configuration>\n" +
                "        <executions>\n" +
                "          <execution>\n" +
                "            <goals>\n" +
                "              <goal>run</goal>\n" +
                "            </goals>\n" +
                "            <phase>package</phase>\n" +
                "          </execution>\n" +
                "        </executions>\n" +
                "      </plugin>\n" +
                "    </plugins>\n" +
                "  </build>\n" +
                "</project>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pom)
                .withBuildFileHavingDependencies("org.apache.tomee.bom:tomee-plus-api:8.0.7")
                .withJavaSources(javaClass, endpointInterface)
                .build();

        String wsdlPathStr = "./testcode/wsdl/calculator.wsdl";
        String question = String.format(GenerateWebServices.QUESTION, "Calculator");
        when(ui.askForInput(question)).thenReturn(wsdlPathStr);

        action.apply(projectContext);

        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(4);

        assertThat(projectContext.getProjectJavaSources().list().get(0).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "\n" +
                "@Stateless\n" +
                "public class Calculator implements CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2) {\n" +
                "        return add1 + add2;\n" +
                "    }\n" +
                "\n" +
                "    public int multiply(int mul1, int mul2) {\n" +
                "        return mul1 * mul2;\n" +
                "    }\n" +
                "}\n");

        assertThat(projectContext.getProjectJavaSources().list().get(1).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "public interface CalculatorWs {\n" +
                "\n" +
                "    public int sum(int add1, int add2);\n" +
                "\n" +
                "    public int multiply(int mul1, int mul2);\n" +
                "}");

        assertThat(projectContext.getProjectJavaSources().list().get(2).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import org.springframework.boot.web.servlet.ServletRegistrationBean;\n" +
                "import org.springframework.context.ApplicationContext;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.core.io.ClassPathResource;\n" +
                "import org.springframework.ws.config.annotation.EnableWs;\n" +
                "import org.springframework.ws.config.annotation.WsConfigurerAdapter;\n" +
                "import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;\n" +
                "import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;\n" +
                "import org.springframework.ws.transport.http.MessageDispatcherServlet;\n" +
                "\n" +
                "@EnableWs\n" +
                "@Configuration\n" +
                "public class WebServiceConfig extends WsConfigurerAdapter {\n" +
                "\n" +
                "    @Bean\n" +
                "    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {\n" +
                "        MessageDispatcherServlet servlet = new MessageDispatcherServlet();\n" +
                "        servlet.setApplicationContext(applicationContext);\n" +
                "        servlet.setTransformWsdlLocations(true);\n" +
                "        return new ServletRegistrationBean<>(servlet, \"/simple-webservice/*\");\n" +
                "    }\n" +
                "\n" +
                "    @Bean(name = \"Calculator\")\n" +
                "    SimpleWsdl11Definition calculator() {\n" +
                "        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();\n" +
                "        wsdl11Definition.setWsdl(new ClassPathResource(\"calculator.wsdl\"));\n" +
                "        return wsdl11Definition;\n" +
                "    }\n" +
                "\n" +
                "}\n");

        assertThat(projectContext.getProjectJavaSources().list().get(3).getResource().print()).isEqualTo("package org.superbiz.calculator.ws;\n" +
                "\n" +
                "import org.springframework.ws.server.endpoint.annotation.Endpoint;\n" +
                "import org.springframework.ws.server.endpoint.annotation.PayloadRoot;\n" +
                "import org.springframework.ws.server.endpoint.annotation.RequestPayload;\n" +
                "import org.springframework.ws.server.endpoint.annotation.ResponsePayload;\n" +
                "import org.superbiz.calculator.ws.CalculatorWs;\n" +
                "import org.superbiz.wsdl.Add;\n" +
                "import org.superbiz.wsdl.AddResponse;\n" +
                "import org.superbiz.wsdl.Multiply;\n" +
                "import org.superbiz.wsdl.MultiplyResponse;\n" +
                "\n" +
                "@Endpoint\n" +
                "public class CalculatorWsEndpoint {\n" +
                "\n" +
                "    private static final String NAMESPACE_URI = \"http://superbiz.org/wsdl\";\n" +
                "\n" +
                "    private CalculatorWs calculatorWs;\n" +
                "\n" +
                "    CalculatorWsEndpoint(CalculatorWs calculatorWs) {\n" +
                "        this.calculatorWs = calculatorWs;\n" +
                "    }\n" +
                "\n" +
                "    @PayloadRoot(namespace = NAMESPACE_URI, localPart = \"add\")\n" +
                "    @ResponsePayload\n" +
                "    public AddResponse add(@RequestPayload Add request) {\n" +
                "        AddResponse response = new AddResponse();\n" +
                "        response.setResult(calculatorWs.sum(request.getFirstParameter(), request.getArg1()));\n" +
                "        return response;\n" +
                "    }\n" +
                "\n" +
                "    @PayloadRoot(namespace = NAMESPACE_URI, localPart = \"multiply\")\n" +
                "    @ResponsePayload\n" +
                "    public MultiplyResponse multiply(@RequestPayload Multiply request) {\n" +
                "        MultiplyResponse response = new MultiplyResponse();\n" +
                "        response.setReturn(calculatorWs.multiply(request.getArg0(), request.getTimes()));\n" +
                "        return response;\n" +
                "    }\n" +
                "\n" +
                "}\n");

        verify(ui).askForInput(question);
        verifyNoMoreInteractions(ui);

    }
}
