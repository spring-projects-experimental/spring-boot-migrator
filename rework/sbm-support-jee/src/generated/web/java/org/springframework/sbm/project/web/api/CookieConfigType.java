
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
 *         The cookie-configType defines the configuration for the
 *         session tracking cookies of this web application.
 *         
 *         Used in: session-config
 *         
 *       
 * 
 * <p>Java class for cookie-configType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cookie-configType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://xmlns.jcp.org/xml/ns/javaee}cookie-nameType" minOccurs="0"/&gt;
 *         &lt;element name="domain" type="{http://xmlns.jcp.org/xml/ns/javaee}cookie-domainType" minOccurs="0"/&gt;
 *         &lt;element name="path" type="{http://xmlns.jcp.org/xml/ns/javaee}cookie-pathType" minOccurs="0"/&gt;
 *         &lt;element name="comment" type="{http://xmlns.jcp.org/xml/ns/javaee}cookie-commentType" minOccurs="0"/&gt;
 *         &lt;element name="http-only" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="secure" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="max-age" type="{http://xmlns.jcp.org/xml/ns/javaee}xsdIntegerType" minOccurs="0"/&gt;
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
@XmlType(name = "cookie-configType", propOrder = {
    "name",
    "domain",
    "path",
    "comment",
    "httpOnly",
    "secure",
    "maxAge"
})
public class CookieConfigType {

    protected CookieNameType name;
    protected CookieDomainType domain;
    protected CookiePathType path;
    protected CookieCommentType comment;
    @XmlElement(name = "http-only")
    protected TrueFalseType httpOnly;
    protected TrueFalseType secure;
    @XmlElement(name = "max-age")
    protected XsdIntegerType maxAge;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link CookieNameType }
     *     
     */
    public CookieNameType getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link CookieNameType }
     *     
     */
    public void setName(CookieNameType value) {
        this.name = value;
    }

    /**
     * Gets the value of the domain property.
     * 
     * @return
     *     possible object is
     *     {@link CookieDomainType }
     *     
     */
    public CookieDomainType getDomain() {
        return domain;
    }

    /**
     * Sets the value of the domain property.
     * 
     * @param value
     *     allowed object is
     *     {@link CookieDomainType }
     *     
     */
    public void setDomain(CookieDomainType value) {
        this.domain = value;
    }

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link CookiePathType }
     *     
     */
    public CookiePathType getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link CookiePathType }
     *     
     */
    public void setPath(CookiePathType value) {
        this.path = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link CookieCommentType }
     *     
     */
    public CookieCommentType getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link CookieCommentType }
     *     
     */
    public void setComment(CookieCommentType value) {
        this.comment = value;
    }

    /**
     * Gets the value of the httpOnly property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getHttpOnly() {
        return httpOnly;
    }

    /**
     * Sets the value of the httpOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setHttpOnly(TrueFalseType value) {
        this.httpOnly = value;
    }

    /**
     * Gets the value of the secure property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getSecure() {
        return secure;
    }

    /**
     * Sets the value of the secure property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setSecure(TrueFalseType value) {
        this.secure = value;
    }

    /**
     * Gets the value of the maxAge property.
     * 
     * @return
     *     possible object is
     *     {@link XsdIntegerType }
     *     
     */
    public XsdIntegerType getMaxAge() {
        return maxAge;
    }

    /**
     * Sets the value of the maxAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdIntegerType }
     *     
     */
    public void setMaxAge(XsdIntegerType value) {
        this.maxAge = value;
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
