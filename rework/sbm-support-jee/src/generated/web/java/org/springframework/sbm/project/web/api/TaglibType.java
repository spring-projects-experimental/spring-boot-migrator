
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 
 *         The taglibType defines the syntax for declaring in
 *         the deployment descriptor that a tag library is
 *         available to the application.  This can be done
 *         to override implicit map entries from TLD files and
 *         from the container.
 *         
 *       
 * 
 * <p>Java class for taglibType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="taglibType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="taglib-uri" type="{http://xmlns.jcp.org/xml/ns/javaee}string"/&gt;
 *         &lt;element name="taglib-location" type="{http://xmlns.jcp.org/xml/ns/javaee}pathType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taglibType", propOrder = {
    "taglibUri",
    "taglibLocation"
})
public class TaglibType {

    @XmlElement(name = "taglib-uri", required = true)
    protected org.springframework.sbm.project.web.api.String taglibUri;
    @XmlElement(name = "taglib-location", required = true)
    protected PathType taglibLocation;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the taglibUri property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.project.web.api.String }
     *     
     */
    public org.springframework.sbm.project.web.api.String getTaglibUri() {
        return taglibUri;
    }

    /**
     * Sets the value of the taglibUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.project.web.api.String }
     *     
     */
    public void setTaglibUri(org.springframework.sbm.project.web.api.String value) {
        this.taglibUri = value;
    }

    /**
     * Gets the value of the taglibLocation property.
     * 
     * @return
     *     possible object is
     *     {@link PathType }
     *     
     */
    public PathType getTaglibLocation() {
        return taglibLocation;
    }

    /**
     * Sets the value of the taglibLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link PathType }
     *     
     */
    public void setTaglibLocation(PathType value) {
        this.taglibLocation = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

}
