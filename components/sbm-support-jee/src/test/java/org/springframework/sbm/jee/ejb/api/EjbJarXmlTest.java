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
package org.springframework.sbm.jee.ejb.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.jee.ejb.filter.EjbJarXmlResourceFinder;
import org.springframework.sbm.jee.ejb.resource.JeeEjbJarXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.TestProjectContext;

import javax.xml.bind.JAXBException;
import java.lang.String;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EjbJarXmlTest {

    public static final String EJB_CLASS_FQNAME = "com.example.jee.ejb.stateless.local.deploymentdescriptor.NoInterfaceViewBean";
    public static final String EJB_TYPE = "Stateless";
    private static final String EJB_NAME = "noInterfaceView";


    @Test
    void unmarshal_21_ejb_jarXml() {
        String ejbJarXmlContent =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ejb-jar id=\"ejb-jar_1\" xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"\n" +
                "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "   xsi:schemaLocation=\"http://java.sun.com/xml/ns/j2ee\n" +
                "   http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_2_1.xsd\" version=\"2.1\">\n" +
                "   <description>Example of a session bean</description>\n" +
                "   <display-name>MyTimeBeanEJBName</display-name>\n" +
                "   <enterprise-beans>\n" +
                "      <session id=\"Session_MyTime\">\n" +
                "         <description>An EJB named MyTimeBean</description>\n" +
                "         <display-name>MyTimeBeanName</display-name>\n" +
                "         <ejb-name>MyTimeBean</ejb-name>\n" +
                "         <local-home>mytimepak.MyTimeLocalHome</local-home>\n" +
                "         <local>mytimepak.MyTimeLocal</local>\n" +
                "         <ejb-class>mytimepak.MyTimeBean</ejb-class>\n" +
                "         <session-type>Stateless</session-type>\n" +
                "         <transaction-type>Container</transaction-type>\n" +
                "      </session>\n" +
                "   </enterprise-beans>\n" +
                "</ejb-jar>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addRegistrar(new JeeEjbJarXmlProjectResourceRegistrar())
                .withProjectResource("src/main/resources/ejb-jar.xml", ejbJarXmlContent)
                .build();

        Optional<EjbJarXml> ejbJarXmlOptional = projectContext.search(new EjbJarXmlResourceFinder());
        EjbJarType ejbJarXml = ejbJarXmlOptional.get().getEjbJarXml();
        assertThat(ejbJarXml.getEnterpriseBeans().getSessionOrEntityOrMessageDriven()).hasSize(1);
        assertThat(ejbJarXml.getEnterpriseBeans().getSessionOrEntityOrMessageDriven().get(0)).isInstanceOf(SessionBeanType.class);
        SessionBeanType sb = (SessionBeanType) ejbJarXml.getEnterpriseBeans().getSessionOrEntityOrMessageDriven().get(0);
        assertThat(sb.getDescription().get(0).getValue()).isEqualTo("An EJB named MyTimeBean");
        assertThat(sb.getDisplayName().get(0).getValue()).isEqualTo("MyTimeBeanName");
        assertThat(sb.getEjbName().getValue()).isEqualTo("MyTimeBean");
        assertThat(sb.getLocalHome().getValue()).isEqualTo("mytimepak.MyTimeLocalHome");
        assertThat(sb.getLocal().getValue()).isEqualTo("mytimepak.MyTimeLocal");
        assertThat(sb.getEjbClass().getValue()).isEqualTo("mytimepak.MyTimeBean");
        assertThat(sb.getSessionType().getValue()).isEqualTo("Stateless");
        assertThat(sb.getTransactionType().getValue()).isEqualTo("Container");
    }

    @Test
    void unmarshal_jcp_3_2_schema() throws JAXBException {

        String ejbJarXmlContent =
                "<ejb-jar xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"\n" +
                        "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "      xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee\n" +
                        "      http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_3_2.xsd\"\n" +
                        "      version=\"3.2\">\n" +
                        "    <enterprise-beans>\n" +
                        "        <session>\n" +
                        "    <ejb-name>" + EJB_NAME + "</ejb-name>\n" +
                        "    <ejb-class>" + EJB_CLASS_FQNAME + "</ejb-class>\n" +
                        "    <session-type>" + EJB_TYPE + "</session-type>\n" +
                        "        </session>\n" +
                        "    </enterprise-beans>\n" +
                        "</ejb-jar>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addRegistrar(new JeeEjbJarXmlProjectResourceRegistrar())
                .withProjectResource("src/main/resources/ejb-jar.xml", ejbJarXmlContent)
                .build();

        Optional<EjbJarXml> ejbJarXmlOptional = projectContext.search(new EjbJarXmlResourceFinder());

        EjbJarType ejbJarType = ejbJarXmlOptional.get().unmarshal(ejbJarXmlContent);

        Assertions.assertThat(ejbJarType).isNotNull();
        Assertions.assertThat(ejbJarType.getEnterpriseBeans()).isNotNull();
        List<Object> sessionOrEntityOrMessageDriven = ejbJarType.getEnterpriseBeans().getSessionOrEntityOrMessageDriven();
        assertThat(sessionOrEntityOrMessageDriven.size()).isEqualTo(1);
        assertThat(sessionOrEntityOrMessageDriven.get(0)).isInstanceOf(SessionBeanType.class);
        SessionBeanType sessionBeanType = (SessionBeanType) sessionOrEntityOrMessageDriven.get(0);
        Assertions.assertThat(sessionBeanType.getEjbClass().getValue()).isEqualTo(EJB_CLASS_FQNAME);
        Assertions.assertThat(sessionBeanType.getSessionType().getValue()).isEqualTo(EJB_TYPE);
    }

    @Test
    @Disabled("TODO: create issue: read schemas with different or no namespace at all.")
    void unmarshal_no_schema() throws JAXBException {
        String ejbJarXmlContent = "<?xml version=\"1.0\"?>\n" +
                "<ejb-jar>\n" +
                "    <enterprise-beans>\n" +
                "        <session>\n" +
                "            <ejb-name>NoInterfaceViewBean</ejb-name>\n" +
                "            <business-local>com.example.jee.ejb.stateless.ejb.local.deploymentdescriptor.NoInterfaceView</business-local>\n" +
                "            <ejb-class>com.example.jee.ejb.stateless.ejb.local.deploymentdescriptor.NoInterfaceViewBean</ejb-class>\n" +
                "            <session-type>Stateless</session-type>\n" +
                "        </session>\n" +
                "        <!-- Local Stateless EJB3 -->\n" +
                "        <session>\n" +
                "            <ejb-name>MyExampleBean</ejb-name>\n" +
                "            <business-local>com.example.jee.ejb.stateless.example.ExampleBusinessInterface</business-local>\n" +
                "            <ejb-class>com.example.jee.ejb.stateless.example.ExampleBean</ejb-class>\n" +
                "            <session-type>Stateless</session-type>\n" +
                "            <transaction-type>Container</transaction-type>\n" +
                "        </session>\n" +
                "        <!-- Remote Stateless EJB3 -->\n" +
                "        <session>\n" +
                "            <ejb-name>MyExampleBeanRemote</ejb-name>\n" +
                "            <business-remote>com.example.jee.ejb.stateless.remote.deploymentdescriptor.RemoteExampleBusinessInterface</business-remote>\n" +
                "            <ejb-class>com.example.jee.ejb.stateless.remote.deploymentdescriptor.ExampleBeanRemote</ejb-class>\n" +
                "            <session-type>Stateless</session-type>\n" +
                "            <transaction-type>Container</transaction-type>\n" +
                "        </session>\n" +
                "    </enterprise-beans>\n" +
                "</ejb-jar>";

        Path sourcePath = Path.of("some/path/ejb-jar.xml");
        Xml.Document xml = (Xml.Document) new XmlParser().parse(ejbJarXmlContent).toList().get(0);
        EjbJarXml jeeEjbJarXmlProjectResourceRegistrar = new EjbJarXml(sourcePath, xml);
        EjbJarType ejbJarType = jeeEjbJarXmlProjectResourceRegistrar.unmarshal(ejbJarXmlContent);
        Assertions.assertThat(ejbJarType).isNotNull();
        Assertions.assertThat(ejbJarType.getEnterpriseBeans()).isNotNull();
    }

    @Test
    void createEjbJarXmlwithOneEjb_removeEjb_shouldResultInEmpty() {

        String ejbJarXmlContent =
                "<ejb-jar xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"\n" +
                        "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "      xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee\n" +
                        "      http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_3_2.xsd\"\n" +
                        "      version=\"3.2\">\n" +
                        "    <enterprise-beans>\n" +
                        "       <session>\n" +
                        "           <ejb-name>" + EJB_NAME + "</ejb-name>\n" +
                        "           <ejb-class>" + EJB_CLASS_FQNAME + "</ejb-class>\n" +
                        "           <session-type>" + EJB_TYPE + "</session-type>\n" +
                        "       </session>\n" +
                        "    </enterprise-beans>\n" +
                        "</ejb-jar>";

        JeeEjbJarXmlProjectResourceRegistrar sut = new JeeEjbJarXmlProjectResourceRegistrar();

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectRoot(Path.of(".").toAbsolutePath())
                .addRegistrar(new JeeEjbJarXmlProjectResourceRegistrar())
                .withProjectResource(Path.of("./src/main/resources/META-INF/ejb-jar.xml"), ejbJarXmlContent)
                .build();


        Optional<EjbJarXml> ejbJarXmlOptional = projectContext.search(new EjbJarXmlResourceFinder());

        assertThat(ejbJarXmlOptional).isNotEmpty();
        EjbJarXml ejbJarXml = ejbJarXmlOptional.get();
        SessionBeanType sessionBeanType = (SessionBeanType) ejbJarXml.getEjbJarXml().getEnterpriseBeans().getSessionOrEntityOrMessageDriven().get(0);
        ejbJarXml.removeSessionBean(sessionBeanType);
        assertThat(ejbJarXml.isEmpty()).isTrue();
    }

}