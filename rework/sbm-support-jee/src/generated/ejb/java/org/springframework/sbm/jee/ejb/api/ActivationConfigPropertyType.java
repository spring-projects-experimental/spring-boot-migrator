
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 
 *                 The activation-config-propertyType contains a name/value
 *                 configuration property pair for a message-driven bean.
 * 
 *                 The properties that are recognized for a particular
 *                 message-driven bean are determined by the messaging type.
 * 
 *             
 * 
 * <p>Java class for activation-config-propertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="activation-config-propertyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="activation-config-property-name" type="{http://xmlns.jcp.org/xml/ns/javaee}xsdStringType"/&gt;
 *         &lt;element name="activation-config-property-value" type="{http://xmlns.jcp.org/xml/ns/javaee}xsdStringType"/&gt;
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
@XmlType(name = "activation-config-propertyType", propOrder = {
    "activationConfigPropertyName",
    "activationConfigPropertyValue"
})
public class ActivationConfigPropertyType {

    @XmlElement(name = "activation-config-property-name", required = true)
    protected XsdStringType activationConfigPropertyName;
    @XmlElement(name = "activation-config-property-value", required = true)
    protected XsdStringType activationConfigPropertyValue;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the activationConfigPropertyName property.
     * 
     * @return
     *     possible object is
     *     {@link XsdStringType }
     *     
     */
    public XsdStringType getActivationConfigPropertyName() {
        return activationConfigPropertyName;
    }

    /**
     * Sets the value of the activationConfigPropertyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdStringType }
     *     
     */
    public void setActivationConfigPropertyName(XsdStringType value) {
        this.activationConfigPropertyName = value;
    }

    /**
     * Gets the value of the activationConfigPropertyValue property.
     * 
     * @return
     *     possible object is
     *     {@link XsdStringType }
     *     
     */
    public XsdStringType getActivationConfigPropertyValue() {
        return activationConfigPropertyValue;
    }

    /**
     * Sets the value of the activationConfigPropertyValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdStringType }
     *     
     */
    public void setActivationConfigPropertyValue(XsdStringType value) {
        this.activationConfigPropertyValue = value;
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
