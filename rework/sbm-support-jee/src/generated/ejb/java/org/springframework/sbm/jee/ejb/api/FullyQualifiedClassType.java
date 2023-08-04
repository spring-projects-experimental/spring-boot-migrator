
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
package org.springframework.sbm.jee.ejb.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 *                 The elements that use this type designate the name of a
 *                 Java class or interface. The name is in the form of a
 *                 "binary name", as defined in the JLS. This is the form
 *                 of name used in Class.forName(). Tools that need the
 *                 canonical name (the name used in source code) will need
 *                 to convert this binary name to the canonical name.
 * 
 *             
 * 
 * <p>Java class for fully-qualified-classType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fully-qualified-classType"&gt;
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
@XmlType(name = "fully-qualified-classType")
@XmlSeeAlso({
    EnvEntryTypeValuesType.class,
    HomeType.class,
    LocalType.class,
    LocalHomeType.class,
    RemoteType.class,
    MessageDestinationTypeType.class,
    EjbClassType.class
})
public class FullyQualifiedClassType
    extends String
{


}
