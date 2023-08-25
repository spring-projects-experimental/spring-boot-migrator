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
package org.springframework.sbm.jee.jpa.api;

import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import lombok.Getter;
import org.openrewrite.xml.tree.Xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class PersistenceXml extends RewriteSourceFileHolder<Xml.Document> {

    @Getter
    private final Persistence persistence;

    // FIXME: all resources must exist as RewriteSourceFileHolder
    @Deprecated
    public PersistenceXml(Path absolutePath, Persistence persistence) {
        super(absolutePath, null);
        this.persistence = persistence;
    }

    public Persistence getPersistence() {
        return persistence;
    }

    public PersistenceXml(Path absoluteProjectDir, Xml.Document rewriteResource) {
        super(absoluteProjectDir, rewriteResource);
        persistence = unmarshalPersistenceXml(rewriteResource.printAll());
    }

    @Override
    public String print() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Persistence.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(persistence, sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private Persistence unmarshalPersistenceXml(String xml) {
        return new PersistenceXmlUnmarshaller().unmarshal(xml);
    }


    static class PersistenceXmlUnmarshaller {

        /**
         * Takes the raw persistence.xml source and attempts to map it to JAXB classes created from a JPA 2.2 schema.
         * Namespace information will be removed to allow unmarshalling all versions (namespace changed in 2.1) into
         * the same JAXB model classes.
         */
        public Persistence unmarshal(String xml) {
            try {
                XMLStreamReader xsr = XMLInputFactory.newFactory().createXMLStreamReader(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
                PersistenceXmlUnmarshaller.XMLReaderWithoutNamespace xr = new PersistenceXmlUnmarshaller.XMLReaderWithoutNamespace(xsr);
                JAXBContext jaxbContext = JAXBContext.newInstance(Persistence.class);
                Unmarshaller jc = jaxbContext.createUnmarshaller();
                return jc.unmarshal(xr, Persistence.class).getValue();
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
