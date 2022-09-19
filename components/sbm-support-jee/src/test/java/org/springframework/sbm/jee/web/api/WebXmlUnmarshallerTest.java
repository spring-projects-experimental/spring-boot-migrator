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

import org.assertj.core.api.Assertions;
import org.eclipse.persistence.jaxb.JAXBUnmarshaller;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.project.web.api.ServletType;
import org.springframework.sbm.project.web.api.WebAppType;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import javax.xml.transform.sax.SAXSource;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Krüger
 */
@Disabled("See #")
class WebXmlUnmarshallerTest {

    @Test
    void test_renameMe() throws JAXBException, XMLStreamException, SAXException {
        String webXml =
              """
              <?xml version="1.0" encoding="ISO-8859-1"?>
              <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                       http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
                       version="3.1">
                  <servlet>
                      <servlet-name>TheServlet</servlet-name>
                      <servlet-class>com.examples.jee.web.TheServlet</servlet-class>
                  </servlet>
                  <servlet-mapping>
                      <servlet-name>TheServlet</servlet-name>
                      <url-pattern>/foo</url-pattern>
                      <url-pattern>/bar</url-pattern>
                  </servlet-mapping>
              </web-app>
              """;

        final WebAppType webApp;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(WebAppType.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement element = (JAXBElement) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(webXml.getBytes()));
            webApp = (WebAppType) element.getValue();
            assertThat(webApp.getModuleNameOrDescriptionAndDisplayName()).isNotEmpty();
            assertThat(((ServletType)webApp.getModuleNameOrDescriptionAndDisplayName().get(0).getValue()).getServletName()).isEqualTo("TheServlet");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }




//        WebXml.WebXmlUnmarshaller webXmlUnmarshaller = new WebXml.WebXmlUnmarshaller();
//        WebAppType value1 = webXmlUnmarshaller.unmarshal(webXml);
//        assertThat(value1.getModuleNameOrDescriptionAndDisplayName()).isNotEmpty();
//        assertThat(((ServletType)value1.getModuleNameOrDescriptionAndDisplayName().get(0).getValue()).getServletName()).isEqualTo("TheServlet");


        // 3
//        XMLStreamReader xsr = XMLInputFactory
//                .newFactory().createXMLStreamReader(new ByteArrayInputStream(webXml.getBytes(
//                        StandardCharsets.UTF_8)));
//        WebXml.WebXmlUnmarshaller.XMLReaderWithoutNamespace xr = new WebXml.WebXmlUnmarshaller.XMLReaderWithoutNamespace(xsr);
//        JAXBContext jaxbContext = JAXBContext.newInstance(WebAppType.class);
//        Unmarshaller jc = jaxbContext.createUnmarshaller();
//        WebAppType value = jc.unmarshal(xr, WebAppType.class).getValue();
//        assertThat(value.getModuleNameOrDescriptionAndDisplayName()).isNotEmpty();

//        WebXml.WebXmlUnmarshaller sut = new WebXml.WebXmlUnmarshaller();
//        WebAppType unmarshal = sut.unmarshal(webXml);
//        Assertions.assertThat(unmarshal.getModuleNameOrDescriptionAndDisplayName()).isNotEmpty();
    }

}