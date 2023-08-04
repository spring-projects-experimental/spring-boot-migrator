
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

import java.util.ArrayList;
import java.util.List;
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
 *         The session-configType defines the session parameters
 *         for this web application.
 *         
 *         Used in: web-app
 *         
 *       
 * 
 * <p>Java class for session-configType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="session-configType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="session-timeout" type="{http://xmlns.jcp.org/xml/ns/javaee}xsdIntegerType" minOccurs="0"/&gt;
 *         &lt;element name="cookie-config" type="{http://xmlns.jcp.org/xml/ns/javaee}cookie-configType" minOccurs="0"/&gt;
 *         &lt;element name="tracking-mode" type="{http://xmlns.jcp.org/xml/ns/javaee}tracking-modeType" maxOccurs="3" minOccurs="0"/&gt;
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
@XmlType(name = "session-configType", propOrder = {
    "sessionTimeout",
    "cookieConfig",
    "trackingMode"
})
public class SessionConfigType {

    @XmlElement(name = "session-timeout")
    protected XsdIntegerType sessionTimeout;
    @XmlElement(name = "cookie-config")
    protected CookieConfigType cookieConfig;
    @XmlElement(name = "tracking-mode")
    protected List<TrackingModeType> trackingMode;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the sessionTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link XsdIntegerType }
     *     
     */
    public XsdIntegerType getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Sets the value of the sessionTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdIntegerType }
     *     
     */
    public void setSessionTimeout(XsdIntegerType value) {
        this.sessionTimeout = value;
    }

    /**
     * Gets the value of the cookieConfig property.
     * 
     * @return
     *     possible object is
     *     {@link CookieConfigType }
     *     
     */
    public CookieConfigType getCookieConfig() {
        return cookieConfig;
    }

    /**
     * Sets the value of the cookieConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link CookieConfigType }
     *     
     */
    public void setCookieConfig(CookieConfigType value) {
        this.cookieConfig = value;
    }

    /**
     * Gets the value of the trackingMode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trackingMode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrackingMode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TrackingModeType }
     * 
     * 
     */
    public List<TrackingModeType> getTrackingMode() {
        if (trackingMode == null) {
            trackingMode = new ArrayList<TrackingModeType>();
        }
        return this.trackingMode;
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
