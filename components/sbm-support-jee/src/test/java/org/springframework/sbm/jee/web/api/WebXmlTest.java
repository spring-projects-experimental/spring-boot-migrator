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
package org.springframework.sbm.jee.web.api;

import org.junit.jupiter.api.Test;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.xml.tree.Xml;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WebXmlTest {

    public static final String GIVEN_SERVLET_NAME = "TheServlet";
    public static final String GIVEN_SERVLET_CLASS_NAME = "com.examples.jee.web.TheServlet";
    public static final String GIVEN_URL_PATTERN_1 = "/foo";
    public static final String GIVEN_URL_PATTERN_2 = "/bar";
    public static final String WEB_XML_VERSION = "3.1";
    public static final String CONTENT = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
            "<web-app xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"\n" +
            "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee\n" +
            "         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd\"\n" +
            "         version=\"" + WEB_XML_VERSION + "\">\n" +
            "    <servlet>\n" +
            "        <servlet-name>" + GIVEN_SERVLET_NAME + "</servlet-name>\n" +
            "        <servlet-class>" + GIVEN_SERVLET_CLASS_NAME + "</servlet-class>\n" +
            "    </servlet>\n" +
            "    <servlet-mapping>\n" +
            "        <servlet-name>" + GIVEN_SERVLET_NAME + "</servlet-name>\n" +
            "        <url-pattern>" + GIVEN_URL_PATTERN_1 + "</url-pattern>\n" +
            "        <url-pattern>" + GIVEN_URL_PATTERN_2 + "</url-pattern>\n" +
            "    </servlet-mapping>\n" +
            "</web-app>";

    @Test
    void deserializeWebXml() throws IOException, JAXBException {

        String expectedXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<web-app version=\"3.1\" xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://xmlns.jcp.org/xml/ns/javaee\">\n" +
                        "    <ns3:servlet>\n" +
                        "        <ns3:servlet-name>TheServlet</ns3:servlet-name>\n" +
                        "        <ns3:servlet-class>com.examples.jee.web.TheServlet</ns3:servlet-class>\n" +
                        "    </ns3:servlet>\n" +
                        "    <ns3:servlet-mapping>\n" +
                        "        <ns3:servlet-name>TheServlet</ns3:servlet-name>\n" +
                        "        <ns3:url-pattern>/foo</ns3:url-pattern>\n" +
                        "        <ns3:url-pattern>/bar</ns3:url-pattern>\n" +
                        "    </ns3:servlet-mapping>\n" +
                        "</web-app>\n";

        WebXml webXml = createWebXml(Path.of("FOR_TEST").toAbsolutePath());
        List<ServletDefinition> servletDefinitions = webXml.getServletDefinitions();

        assertThat(webXml.getVersion()).isEqualTo("3.1");
        assertThat(servletDefinitions.get(0).getServletName()).isEqualTo(GIVEN_SERVLET_NAME);
        assertThat(servletDefinitions.get(0).getFullyQualifiedServletClassName()).isEqualTo(GIVEN_SERVLET_CLASS_NAME);
        assertThat(servletDefinitions.get(0).getUrlPattern()).containsExactly(GIVEN_URL_PATTERN_1, GIVEN_URL_PATTERN_2);
        assertThat(webXml.print()).isEqualTo(expectedXml);

    }

    @Test
    void deleteServletDefinition() throws JAXBException {
        String expectedXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<web-app version=\"3.1\" xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://xmlns.jcp.org/xml/ns/javaee\"/>\n";

        WebXml webXml = createWebXml(Path.of("FOR_TEST").toAbsolutePath());
        assertThat(webXml.getServletDefinitions()).hasSize(1);

        webXml.deleteServletDefinition(GIVEN_SERVLET_NAME);

        assertThat(webXml.getServletDefinitions()).isEmpty();
        assertThat(webXml.print()).isEqualTo(expectedXml);
    }

    private WebXml createWebXml(Path absoluteProjectDir) throws JAXBException {
        Xml.Document document = new XmlParser().parse(CONTENT).get(0).withSourcePath(Path.of("src/main/webapp/WEB-INF/web.xml"));
        return new WebXml(absoluteProjectDir, document);
    }

}
