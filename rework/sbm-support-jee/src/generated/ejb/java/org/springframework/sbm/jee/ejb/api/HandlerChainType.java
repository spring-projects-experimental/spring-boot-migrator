
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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 
 *                 The handler-chain element defines the handlerchain.
 *                 Handlerchain can be defined such that the handlers in the
 *                 handlerchain operate,all ports of a service, on a specific
 *                 port or on a list of protocol-bindings. The choice of elements
 *                 service-name-pattern, port-name-pattern and protocol-bindings
 *                 are used to specify whether the handlers in handler-chain are
 *                 for a service, port or protocol binding. If none of these
 *                 choices are specified with the handler-chain element then the
 *                 handlers specified in the handler-chain will be applied on
 *                 everything.
 * 
 *             
 * 
 * <p>Java class for handler-chainType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="handler-chainType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element name="service-name-pattern" type="{http://xmlns.jcp.org/xml/ns/javaee}qname-pattern"/&gt;
 *           &lt;element name="port-name-pattern" type="{http://xmlns.jcp.org/xml/ns/javaee}qname-pattern"/&gt;
 *           &lt;element name="protocol-bindings" type="{http://xmlns.jcp.org/xml/ns/javaee}protocol-bindingListType"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="handler" type="{http://xmlns.jcp.org/xml/ns/javaee}handlerType" maxOccurs="unbounded"/&gt;
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
@XmlType(name = "handler-chainType", propOrder = {
    "serviceNamePattern",
    "portNamePattern",
    "protocolBindings",
    "handler"
})
public class HandlerChainType {

    @XmlElement(name = "service-name-pattern")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected java.lang.String serviceNamePattern;
    @XmlElement(name = "port-name-pattern")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected java.lang.String portNamePattern;
    @XmlList
    @XmlElement(name = "protocol-bindings")
    protected List<java.lang.String> protocolBindings;
    @XmlElement(required = true)
    protected List<HandlerType> handler;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the serviceNamePattern property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getServiceNamePattern() {
        return serviceNamePattern;
    }

    /**
     * Sets the value of the serviceNamePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setServiceNamePattern(java.lang.String value) {
        this.serviceNamePattern = value;
    }

    /**
     * Gets the value of the portNamePattern property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getPortNamePattern() {
        return portNamePattern;
    }

    /**
     * Sets the value of the portNamePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setPortNamePattern(java.lang.String value) {
        this.portNamePattern = value;
    }

    /**
     * Gets the value of the protocolBindings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the protocolBindings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocolBindings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String }
     * 
     * 
     */
    public List<java.lang.String> getProtocolBindings() {
        if (protocolBindings == null) {
            protocolBindings = new ArrayList<java.lang.String>();
        }
        return this.protocolBindings;
    }

    /**
     * Gets the value of the handler property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the handler property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHandler().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HandlerType }
     * 
     * 
     */
    public List<HandlerType> getHandler() {
        if (handler == null) {
            handler = new ArrayList<HandlerType>();
        }
        return this.handler;
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
