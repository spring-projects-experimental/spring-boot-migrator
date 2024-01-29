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

import org.openrewrite.xml.tree.Xml;
import org.springframework.rewrite.resource.RewriteSourceFileHolder;
import org.xml.sax.InputSource;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.String;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class EjbJarXml extends RewriteSourceFileHolder<Xml.Document> {

    private EjbJarType wrapped;

    public EjbJarXml(Path projectPath, Xml.Document xml) {
        super(projectPath, xml);
        wrapped = unmarshal(xml.printAll());
    }

    public EjbJarType getEjbJarXml() {
        return wrapped;
    }

    @Override
    public String print() {
        try {
            JAXBElement<EjbJarType> element = new JAXBElement<>(new QName("", "ejb-jar"), EjbJarType.class, wrapped);
            JAXBContext jaxbContext = JAXBContext.newInstance(EjbJarType.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//            jaxbMarshaller.setProperty("jaxb.formatted.output", true);
//            jaxbMarshaller.setProperty("jaxb.schemaLocation", "http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd");
//            jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper());
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(element, sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    EjbJarType unmarshal(String content) {
        return new EjbJarXmlUnmarshaller().unmarshal(content);
    }


    static class EjbJarXmlUnmarshaller {

        /**
         * Takes the raw ejb-jar.xml source and attempts to map it to JAXB classes created from a EJB 3.1 schema.
         * Namespace information will be removed to allow unmarshalling all versions into the same JAXB model classes.
         */
        public EjbJarType unmarshal(String xml) {
            try {
                XMLStreamReader xsr = XMLInputFactory.newFactory().createXMLStreamReader(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
                EjbJarXml.EjbJarXmlUnmarshaller.XMLReaderWithoutNamespace xr = new EjbJarXml.EjbJarXmlUnmarshaller.XMLReaderWithoutNamespace(xsr);
                JAXBContext jaxbContext = JAXBContext.newInstance(EjbJarType.class);
                Unmarshaller jc = jaxbContext.createUnmarshaller();
                return jc.unmarshal(xr, EjbJarType.class).getValue();
            } catch (JAXBException | XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }

        class XMLReaderWithoutNamespace extends StreamReaderDelegate {
            public XMLReaderWithoutNamespace(XMLStreamReader reader) {
                super(reader);
            }

            @Override
            public String getAttributeNamespace(int arg0) {
                return "";
            }

            @Override
            public String getNamespaceURI() {
                return "";
            }
        }
    }

    public boolean isEmpty() {
        try {
            String content = print();
            InputSource inputXML = new InputSource(new StringReader(content));
            XPath xPath = XPathFactory.newInstance().newXPath();
            String result = xPath.evaluate("/ejb-jar/child::node()", inputXML);
            return "".equals(result);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("Failed to execute xPath expression '/ejb-jar' on ejb-jar.xml");
        }
    }

    public void removeSessionBean(SessionBeanType sbt) {
        wrapped.getEnterpriseBeans().getSessionOrEntityOrMessageDriven().remove(sbt);
    }

    public void removeSessionBeans(List<SessionBeanType> sessionBeansToRemove) {
        wrapped.getEnterpriseBeans().getSessionOrEntityOrMessageDriven().removeAll(sessionBeansToRemove);
    }
}
