
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 [
 *         The display-name type contains a short name that is intended
 *         to be displayed by tools. It is used by display-name
 *         elements.  The display name need not be unique.
 *         
 *         Example: 
 *         
 *         ...
 *         <display-name xml:lang="en">
 *         Employee Self Service
 *         </display-name>
 *         
 *         The value of the xml:lang attribute is "en" (English) by default. 
 *         
 *         
 *             
 * 
 * <p>Java class for display-nameType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="display-nameType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://xmlns.jcp.org/xml/ns/javaee&gt;string"&gt;
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "display-nameType")
public class DisplayNameType
    extends org.springframework.sbm.jee.ejb.api.String
{

    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    protected java.lang.String lang;

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setLang(java.lang.String value) {
        this.lang = value;
    }

}
