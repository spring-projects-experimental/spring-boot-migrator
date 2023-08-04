
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
 *         The error-pageType contains a mapping between an error code
 *         or exception type to the path of a resource in the web
 *         application.
 *         
 *         Error-page declarations using the exception-type element in
 *         the deployment descriptor must be unique up to the class name of
 *         the exception-type. Similarly, error-page declarations using the
 *         error-code element must be unique in the deployment descriptor
 *         up to the status code.
 *         
 *         If an error-page element in the deployment descriptor does not
 *         contain an exception-type or an error-code element, the error
 *         page is a default error page.
 *         
 *         Used in: web-app
 *         
 *       
 * 
 * <p>Java class for error-pageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="error-pageType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element name="error-code" type="{http://xmlns.jcp.org/xml/ns/javaee}error-codeType"/&gt;
 *           &lt;element name="exception-type" type="{http://xmlns.jcp.org/xml/ns/javaee}fully-qualified-classType"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="location" type="{http://xmlns.jcp.org/xml/ns/javaee}war-pathType"/&gt;
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
@XmlType(name = "error-pageType", propOrder = {
    "errorCode",
    "exceptionType",
    "location"
})
public class ErrorPageType {

    @XmlElement(name = "error-code")
    protected ErrorCodeType errorCode;
    @XmlElement(name = "exception-type")
    protected FullyQualifiedClassType exceptionType;
    @XmlElement(required = true)
    protected WarPathType location;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the errorCode property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorCodeType }
     *     
     */
    public ErrorCodeType getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorCodeType }
     *     
     */
    public void setErrorCode(ErrorCodeType value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the exceptionType property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getExceptionType() {
        return exceptionType;
    }

    /**
     * Sets the value of the exceptionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setExceptionType(FullyQualifiedClassType value) {
        this.exceptionType = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link WarPathType }
     *     
     */
    public WarPathType getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link WarPathType }
     *     
     */
    public void setLocation(WarPathType value) {
        this.location = value;
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
