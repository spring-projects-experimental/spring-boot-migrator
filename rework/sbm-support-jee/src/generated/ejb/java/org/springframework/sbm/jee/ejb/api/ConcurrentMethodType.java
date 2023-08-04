
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
 *                 The concurrent-methodType specifies information about a method
 *                 of a bean with container managed concurrency.
 * 
 *                 The optional lock element specifies the kind of concurrency
 *                 lock asssociated with the method.
 * 
 *                 The optional access-timeout element specifies the amount of
 *                 time (in a given time unit) the container should wait for a
 *                 concurrency lock before throwing an exception to the client.
 * 
 *             
 * 
 * <p>Java class for concurrent-methodType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="concurrent-methodType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="method" type="{http://xmlns.jcp.org/xml/ns/javaee}named-methodType"/&gt;
 *         &lt;element name="lock" type="{http://xmlns.jcp.org/xml/ns/javaee}concurrent-lock-typeType" minOccurs="0"/&gt;
 *         &lt;element name="access-timeout" type="{http://xmlns.jcp.org/xml/ns/javaee}access-timeoutType" minOccurs="0"/&gt;
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
@XmlType(name = "concurrent-methodType", propOrder = {
    "method",
    "lock",
    "accessTimeout"
})
public class ConcurrentMethodType {

    @XmlElement(required = true)
    protected NamedMethodType method;
    protected ConcurrentLockTypeType lock;
    @XmlElement(name = "access-timeout")
    protected AccessTimeoutType accessTimeout;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link NamedMethodType }
     *     
     */
    public NamedMethodType getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamedMethodType }
     *     
     */
    public void setMethod(NamedMethodType value) {
        this.method = value;
    }

    /**
     * Gets the value of the lock property.
     * 
     * @return
     *     possible object is
     *     {@link ConcurrentLockTypeType }
     *     
     */
    public ConcurrentLockTypeType getLock() {
        return lock;
    }

    /**
     * Sets the value of the lock property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConcurrentLockTypeType }
     *     
     */
    public void setLock(ConcurrentLockTypeType value) {
        this.lock = value;
    }

    /**
     * Gets the value of the accessTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link AccessTimeoutType }
     *     
     */
    public AccessTimeoutType getAccessTimeout() {
        return accessTimeout;
    }

    /**
     * Sets the value of the accessTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessTimeoutType }
     *     
     */
    public void setAccessTimeout(AccessTimeoutType value) {
        this.accessTimeout = value;
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
