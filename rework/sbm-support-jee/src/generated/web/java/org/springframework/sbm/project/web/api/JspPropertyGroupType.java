
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
 *         The jsp-property-groupType is used to group a number of
 *         files so they can be given global property information.
 *         All files so described are deemed to be JSP files.  The
 *         following additional properties can be described:
 *         
 *         - Control whether EL is ignored.
 *         - Control whether scripting elements are invalid.
 *         - Indicate pageEncoding information.
 *         - Indicate that a resource is a JSP document (XML).
 *         - Prelude and Coda automatic includes.
 *         - Control whether the character sequence #{ is allowed
 *         when used as a String literal.
 *         - Control whether template text containing only
 *         whitespaces must be removed from the response output.
 *         - Indicate the default contentType information.
 *         - Indicate the default buffering model for JspWriter
 *         - Control whether error should be raised for the use of
 *         undeclared namespaces in a JSP page.
 *         
 *       
 * 
 * <p>Java class for jsp-property-groupType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jsp-property-groupType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}descriptionGroup"/&gt;
 *         &lt;element name="url-pattern" type="{http://xmlns.jcp.org/xml/ns/javaee}url-patternType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="el-ignored" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="page-encoding" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="scripting-invalid" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="is-xml" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="include-prelude" type="{http://xmlns.jcp.org/xml/ns/javaee}pathType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="include-coda" type="{http://xmlns.jcp.org/xml/ns/javaee}pathType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="deferred-syntax-allowed-as-literal" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="trim-directive-whitespaces" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="default-content-type" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="buffer" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="error-on-undeclared-namespace" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
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
@XmlType(name = "jsp-property-groupType", propOrder = {
    "description",
    "displayName",
    "icon",
    "urlPattern",
    "elIgnored",
    "pageEncoding",
    "scriptingInvalid",
    "isXml",
    "includePrelude",
    "includeCoda",
    "deferredSyntaxAllowedAsLiteral",
    "trimDirectiveWhitespaces",
    "defaultContentType",
    "buffer",
    "errorOnUndeclaredNamespace"
})
public class JspPropertyGroupType {

    protected List<DescriptionType> description;
    @XmlElement(name = "display-name")
    protected List<DisplayNameType> displayName;
    protected List<IconType> icon;
    @XmlElement(name = "url-pattern", required = true)
    protected List<UrlPatternType> urlPattern;
    @XmlElement(name = "el-ignored")
    protected TrueFalseType elIgnored;
    @XmlElement(name = "page-encoding")
    protected org.springframework.sbm.project.web.api.String pageEncoding;
    @XmlElement(name = "scripting-invalid")
    protected TrueFalseType scriptingInvalid;
    @XmlElement(name = "is-xml")
    protected TrueFalseType isXml;
    @XmlElement(name = "include-prelude")
    protected List<PathType> includePrelude;
    @XmlElement(name = "include-coda")
    protected List<PathType> includeCoda;
    @XmlElement(name = "deferred-syntax-allowed-as-literal")
    protected TrueFalseType deferredSyntaxAllowedAsLiteral;
    @XmlElement(name = "trim-directive-whitespaces")
    protected TrueFalseType trimDirectiveWhitespaces;
    @XmlElement(name = "default-content-type")
    protected org.springframework.sbm.project.web.api.String defaultContentType;
    protected org.springframework.sbm.project.web.api.String buffer;
    @XmlElement(name = "error-on-undeclared-namespace")
    protected TrueFalseType errorOnUndeclaredNamespace;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DescriptionType }
     * 
     * 
     */
    public List<DescriptionType> getDescription() {
        if (description == null) {
            description = new ArrayList<DescriptionType>();
        }
        return this.description;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the displayName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisplayName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DisplayNameType }
     * 
     * 
     */
    public List<DisplayNameType> getDisplayName() {
        if (displayName == null) {
            displayName = new ArrayList<DisplayNameType>();
        }
        return this.displayName;
    }

    /**
     * Gets the value of the icon property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the icon property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIcon().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IconType }
     * 
     * 
     */
    public List<IconType> getIcon() {
        if (icon == null) {
            icon = new ArrayList<IconType>();
        }
        return this.icon;
    }

    /**
     * Gets the value of the urlPattern property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the urlPattern property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUrlPattern().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UrlPatternType }
     * 
     * 
     */
    public List<UrlPatternType> getUrlPattern() {
        if (urlPattern == null) {
            urlPattern = new ArrayList<UrlPatternType>();
        }
        return this.urlPattern;
    }

    /**
     * Gets the value of the elIgnored property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getElIgnored() {
        return elIgnored;
    }

    /**
     * Sets the value of the elIgnored property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setElIgnored(TrueFalseType value) {
        this.elIgnored = value;
    }

    /**
     * Gets the value of the pageEncoding property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.project.web.api.String }
     *     
     */
    public org.springframework.sbm.project.web.api.String getPageEncoding() {
        return pageEncoding;
    }

    /**
     * Sets the value of the pageEncoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.project.web.api.String }
     *     
     */
    public void setPageEncoding(org.springframework.sbm.project.web.api.String value) {
        this.pageEncoding = value;
    }

    /**
     * Gets the value of the scriptingInvalid property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getScriptingInvalid() {
        return scriptingInvalid;
    }

    /**
     * Sets the value of the scriptingInvalid property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setScriptingInvalid(TrueFalseType value) {
        this.scriptingInvalid = value;
    }

    /**
     * Gets the value of the isXml property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getIsXml() {
        return isXml;
    }

    /**
     * Sets the value of the isXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setIsXml(TrueFalseType value) {
        this.isXml = value;
    }

    /**
     * Gets the value of the includePrelude property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the includePrelude property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncludePrelude().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PathType }
     * 
     * 
     */
    public List<PathType> getIncludePrelude() {
        if (includePrelude == null) {
            includePrelude = new ArrayList<PathType>();
        }
        return this.includePrelude;
    }

    /**
     * Gets the value of the includeCoda property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the includeCoda property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncludeCoda().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PathType }
     * 
     * 
     */
    public List<PathType> getIncludeCoda() {
        if (includeCoda == null) {
            includeCoda = new ArrayList<PathType>();
        }
        return this.includeCoda;
    }

    /**
     * Gets the value of the deferredSyntaxAllowedAsLiteral property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getDeferredSyntaxAllowedAsLiteral() {
        return deferredSyntaxAllowedAsLiteral;
    }

    /**
     * Sets the value of the deferredSyntaxAllowedAsLiteral property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setDeferredSyntaxAllowedAsLiteral(TrueFalseType value) {
        this.deferredSyntaxAllowedAsLiteral = value;
    }

    /**
     * Gets the value of the trimDirectiveWhitespaces property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getTrimDirectiveWhitespaces() {
        return trimDirectiveWhitespaces;
    }

    /**
     * Sets the value of the trimDirectiveWhitespaces property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setTrimDirectiveWhitespaces(TrueFalseType value) {
        this.trimDirectiveWhitespaces = value;
    }

    /**
     * Gets the value of the defaultContentType property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.project.web.api.String }
     *     
     */
    public org.springframework.sbm.project.web.api.String getDefaultContentType() {
        return defaultContentType;
    }

    /**
     * Sets the value of the defaultContentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.project.web.api.String }
     *     
     */
    public void setDefaultContentType(org.springframework.sbm.project.web.api.String value) {
        this.defaultContentType = value;
    }

    /**
     * Gets the value of the buffer property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.project.web.api.String }
     *     
     */
    public org.springframework.sbm.project.web.api.String getBuffer() {
        return buffer;
    }

    /**
     * Sets the value of the buffer property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.project.web.api.String }
     *     
     */
    public void setBuffer(org.springframework.sbm.project.web.api.String value) {
        this.buffer = value;
    }

    /**
     * Gets the value of the errorOnUndeclaredNamespace property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getErrorOnUndeclaredNamespace() {
        return errorOnUndeclaredNamespace;
    }

    /**
     * Sets the value of the errorOnUndeclaredNamespace property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setErrorOnUndeclaredNamespace(TrueFalseType value) {
        this.errorOnUndeclaredNamespace = value;
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
