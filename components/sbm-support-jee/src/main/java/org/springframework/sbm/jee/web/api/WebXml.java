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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.web.api.ServletMappingType;
import org.springframework.sbm.project.web.api.ServletType;
import org.springframework.sbm.project.web.api.UrlPatternType;
import org.springframework.sbm.project.web.api.WebAppType;
import org.openrewrite.xml.tree.Xml;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Resource representation for a web.xml deployment descriptor.
 */
public class WebXml extends RewriteSourceFileHolder<Xml.Document> {

    private final WebAppType webApp;

    public WebXml(Path absoluteProjectDir, Xml.Document resource) {
        super(absoluteProjectDir, resource);
        webApp = initWebApp(resource);
    }

    private WebAppType initWebApp(Xml.Document resource) {
        return new WebXmlUnmarshaller().unmarshal(resource.printAll());
    }

    public static class MyNamespacePrefixMapper extends NamespacePrefixMapper {

        public String getPreferredPrefix(String namespaceUri,
                                         String suggestion,
                                         boolean requirePrefix) {
            if (requirePrefix) {
                if ("http://xmlns.jcp.org/xml/ns/javaee".equals(namespaceUri)) {
                    return "";
                }
                if ("http://www.w3.org/1999/xlink".equals(namespaceUri)) {
                    return "xlink";
                }
                return suggestion;
            } else {
                return "";
            }
        }
    }

    @Override
    public String print() {
        try {
            JAXBElement<WebAppType> element = new JAXBElement<>(new QName("", "web-app"), WebAppType.class, webApp);
            JAXBContext jaxbContext = JAXBContext.newInstance(WebAppType.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty("jaxb.formatted.output", true);
            jaxbMarshaller.setProperty("jaxb.schemaLocation", "http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd");
            jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper());
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(element, sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteServletDefinition(String servletName) {
        List<JAXBElement<?>> elementsToRemove = new ArrayList<>();
        List<JAXBElement<?>> jaxbElements = webApp.getModuleNameOrDescriptionAndDisplayName();
        for(JAXBElement e : jaxbElements) {
            int i = jaxbElements.indexOf(e);
            if (e.getDeclaredType().isAssignableFrom(ServletType.class)) {
                if(((ServletType) e.getValue()).getServletName().getValue().equals(servletName)) {
                    elementsToRemove.add(e);
                }
            }
            if (e.getDeclaredType().isAssignableFrom(ServletMappingType.class)) {
                if(((ServletMappingType) e.getValue()).getServletName().getValue().equals(servletName)) {
                    elementsToRemove.add(e);
                }
            }
        }
        jaxbElements.removeAll(elementsToRemove);
        syncModel();
    }

    private void syncModel() {
        Xml.Tag tag = Xml.Tag.build(print());
        getSourceFile().withRoot(tag);
    }

    List<ServletDefinition> getServletDefinitions() {
        final Map<String, ServletDefinition> servlets = new HashMap<>();

        for(JAXBElement<?> e : webApp.getModuleNameOrDescriptionAndDisplayName()) {

            if (e.getDeclaredType().isAssignableFrom(ServletType.class)) {
                ServletType servletType = (ServletType) e.getValue();
                String servletName = servletType.getServletName().getValue();
                String servletClass = servletType.getServletClass().getValue();
                ServletDefinition servletDefinition;
                if (servlets.containsKey(servletName)) {
                    servletDefinition = servlets.get(servletName);
                } else {
                    servletDefinition = new ServletDefinition();
                    servletDefinition.setServletName(servletName);
                    servlets.put(servletName, servletDefinition);
                }
                servletDefinition.setFullyQualifiedServletClassName(servletClass);
            } else if (e.getDeclaredType().isAssignableFrom(ServletMappingType.class)) {
                ServletMappingType servletMappingType = (ServletMappingType) e.getValue();
                String servletName = servletMappingType.getServletName().getValue();
                ServletDefinition servletDefinition;
                if (servlets.containsKey(servletName)) {
                    servletDefinition = servlets.get(servletName);
                } else {
                    servletDefinition = new ServletDefinition();
                    servletDefinition.setServletName(servletName);
                    servlets.put(servletName, servletDefinition);
                }
                List<String> urlPatterns = servletMappingType.getUrlPattern().stream()
                        .map(UrlPatternType::getValue)
                        .map(s -> s)
                        .collect(Collectors.toList());
                servletDefinition.setUrlPattern(urlPatterns);
            }
        }
        return new ArrayList<>(servlets.values());
    }

    public String getVersion() {
        return webApp.getVersion();
    }

    static class WebXmlUnmarshaller {

        /**
         * Takes the raw web.xml source and attempts to map it to JAXB classes created from a 4.0 web-app schema.
         * Namespace information will be removed to allow unmarshalling all versions into the same JAXB model classes.
         */
        public WebAppType unmarshal(String xml) {
            try {
                XMLStreamReader xsr = XMLInputFactory.newFactory().createXMLStreamReader(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
                WebXml.WebXmlUnmarshaller.XMLReaderWithoutNamespace xr = new WebXml.WebXmlUnmarshaller.XMLReaderWithoutNamespace(xsr);
                JAXBContext jaxbContext = JAXBContext.newInstance(WebAppType.class);
                Unmarshaller jc = jaxbContext.createUnmarshaller();
                return jc.unmarshal(xr, WebAppType.class).getValue();
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
}
