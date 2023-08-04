
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
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 *                 This specifies the WS-Addressing requirements for a JAX-WS web service.
 *                 It corresponds to javax.xml.ws.soap.Addressing annotation or its
 *                 feature javax.xml.ws.soap.AddressingFeature.
 * 
 *                 If the "enabled" element is "true", WS-Addressing is enabled.
 *                 It means that the endpoint supports WS-Addressing but does not require
 *                 its use. The default value for "enabled" is "true".
 * 
 *                 If the WS-Addressing is enabled and the "required" element is "true",
 *                 it means that the endpoint requires WS-Addressing. The default value
 *                 for "required" is "false".
 * 
 *                 If WS-Addressing is enabled, the "responses" element determines
 *                 if an endpoint requires the use of only anonymous responses,
 *                 or only non-anonymous responses, or all. The value of the "responses"
 *                 element must be one of the following:
 * 
 *                 ANONYMOUS
 *                 NON_ANONYMOUS
 *                 ALL
 * 
 *                 The default value for the "responses" is ALL.
 * 
 *             
 * 
 * <p>Java class for addressingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="addressingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="enabled" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="required" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="responses" type="{http://xmlns.jcp.org/xml/ns/javaee}addressing-responsesType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addressingType", propOrder = {
    "enabled",
    "required",
    "responses"
})
public class AddressingType {

    protected TrueFalseType enabled;
    protected TrueFalseType required;
    protected AddressingResponsesType responses;

    /**
     * Gets the value of the enabled property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getEnabled() {
        return enabled;
    }

    /**
     * Sets the value of the enabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setEnabled(TrueFalseType value) {
        this.enabled = value;
    }

    /**
     * Gets the value of the required property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getRequired() {
        return required;
    }

    /**
     * Sets the value of the required property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setRequired(TrueFalseType value) {
        this.required = value;
    }

    /**
     * Gets the value of the responses property.
     * 
     * @return
     *     possible object is
     *     {@link AddressingResponsesType }
     *     
     */
    public AddressingResponsesType getResponses() {
        return responses;
    }

    /**
     * Sets the value of the responses property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressingResponsesType }
     *     
     */
    public void setResponses(AddressingResponsesType value) {
        this.responses = value;
    }

}
