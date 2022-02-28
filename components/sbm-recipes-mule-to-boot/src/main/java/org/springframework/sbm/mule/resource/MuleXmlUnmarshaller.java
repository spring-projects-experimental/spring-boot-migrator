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
package org.springframework.sbm.mule.resource;

import org.mulesoft.schema.mule.core.MuleType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class MuleXmlUnmarshaller {
    public MuleType unmarshal(String xml) {
        try {
            XMLStreamReader xsr = XMLInputFactory.newFactory().createXMLStreamReader(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
//            PersistenceXmlUnmarshaller.XMLReaderWithoutNamespace xr = new PersistenceXmlUnmarshaller.XMLReaderWithoutNamespace(xsr);
            JAXBContext jaxbContext = JAXBContext.newInstance(MuleType.class);
            Unmarshaller jc = jaxbContext.createUnmarshaller();
            JAXBElement<MuleType> unmarshal = (JAXBElement<MuleType>) jc.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            return unmarshal.getValue();
        } catch (JAXBException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
}
