
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
package org.springframework.sbm.project.web.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 *         The message-destination-linkType is used to link a message
 *         destination reference or message-driven bean to a message
 *         destination.
 *         
 *         The Assembler sets the value to reflect the flow of messages
 *         between producers and consumers in the application.
 *         
 *         The value must be the message-destination-name of a message
 *         destination in the same Deployment File or in another
 *         Deployment File in the same Java EE application unit.
 *         
 *         Alternatively, the value may be composed of a path name
 *         specifying a Deployment File containing the referenced
 *         message destination with the message-destination-name of the
 *         destination appended and separated from the path name by
 *         "#". The path name is relative to the Deployment File
 *         containing Deployment Component that is referencing the
 *         message destination.  This allows multiple message
 *         destinations with the same name to be uniquely identified.
 *         
 *       
 * 
 * <p>Java class for message-destination-linkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="message-destination-linkType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;restriction base="&lt;http://xmlns.jcp.org/xml/ns/javaee&gt;string"&gt;
 *     &lt;/restriction&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "message-destination-linkType")
public class MessageDestinationLinkType
    extends String
{


}
