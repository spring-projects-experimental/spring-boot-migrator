
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
 *         The servletType is used to declare a servlet.
 *         It contains the declarative data of a
 *         servlet. If a jsp-file is specified and the load-on-startup
 *         element is present, then the JSP should be precompiled and
 *         loaded.
 *         
 *         Used in: web-app
 *         
 *       
 * 
 * <p>Java class for servletType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="servletType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}descriptionGroup"/&gt;
 *         &lt;element name="servlet-name" type="{http://xmlns.jcp.org/xml/ns/javaee}servlet-nameType"/&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element name="servlet-class" type="{http://xmlns.jcp.org/xml/ns/javaee}fully-qualified-classType"/&gt;
 *           &lt;element name="jsp-file" type="{http://xmlns.jcp.org/xml/ns/javaee}jsp-fileType"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="init-param" type="{http://xmlns.jcp.org/xml/ns/javaee}param-valueType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="load-on-startup" type="{http://xmlns.jcp.org/xml/ns/javaee}load-on-startupType" minOccurs="0"/&gt;
 *         &lt;element name="enabled" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="async-supported" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="run-as" type="{http://xmlns.jcp.org/xml/ns/javaee}run-asType" minOccurs="0"/&gt;
 *         &lt;element name="security-role-ref" type="{http://xmlns.jcp.org/xml/ns/javaee}security-role-refType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="multipart-config" type="{http://xmlns.jcp.org/xml/ns/javaee}multipart-configType" minOccurs="0"/&gt;
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
@XmlType(name = "servletType", propOrder = {
    "description",
    "displayName",
    "icon",
    "servletName",
    "servletClass",
    "jspFile",
    "initParam",
    "loadOnStartup",
    "enabled",
    "asyncSupported",
    "runAs",
    "securityRoleRef",
    "multipartConfig"
})
public class ServletType {

    protected List<DescriptionType> description;
    @XmlElement(name = "display-name")
    protected List<DisplayNameType> displayName;
    protected List<IconType> icon;
    @XmlElement(name = "servlet-name", required = true)
    protected ServletNameType servletName;
    @XmlElement(name = "servlet-class")
    protected FullyQualifiedClassType servletClass;
    @XmlElement(name = "jsp-file")
    protected JspFileType jspFile;
    @XmlElement(name = "init-param")
    protected List<ParamValueType> initParam;
    @XmlElement(name = "load-on-startup")
    protected java.lang.String loadOnStartup;
    protected TrueFalseType enabled;
    @XmlElement(name = "async-supported")
    protected TrueFalseType asyncSupported;
    @XmlElement(name = "run-as")
    protected RunAsType runAs;
    @XmlElement(name = "security-role-ref")
    protected List<SecurityRoleRefType> securityRoleRef;
    @XmlElement(name = "multipart-config")
    protected MultipartConfigType multipartConfig;
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
     * Gets the value of the servletName property.
     * 
     * @return
     *     possible object is
     *     {@link ServletNameType }
     *     
     */
    public ServletNameType getServletName() {
        return servletName;
    }

    /**
     * Sets the value of the servletName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServletNameType }
     *     
     */
    public void setServletName(ServletNameType value) {
        this.servletName = value;
    }

    /**
     * Gets the value of the servletClass property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getServletClass() {
        return servletClass;
    }

    /**
     * Sets the value of the servletClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setServletClass(FullyQualifiedClassType value) {
        this.servletClass = value;
    }

    /**
     * Gets the value of the jspFile property.
     * 
     * @return
     *     possible object is
     *     {@link JspFileType }
     *     
     */
    public JspFileType getJspFile() {
        return jspFile;
    }

    /**
     * Sets the value of the jspFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link JspFileType }
     *     
     */
    public void setJspFile(JspFileType value) {
        this.jspFile = value;
    }

    /**
     * Gets the value of the initParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the initParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInitParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParamValueType }
     * 
     * 
     */
    public List<ParamValueType> getInitParam() {
        if (initParam == null) {
            initParam = new ArrayList<ParamValueType>();
        }
        return this.initParam;
    }

    /**
     * Gets the value of the loadOnStartup property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getLoadOnStartup() {
        return loadOnStartup;
    }

    /**
     * Sets the value of the loadOnStartup property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setLoadOnStartup(java.lang.String value) {
        this.loadOnStartup = value;
    }

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
     * Gets the value of the asyncSupported property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getAsyncSupported() {
        return asyncSupported;
    }

    /**
     * Sets the value of the asyncSupported property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setAsyncSupported(TrueFalseType value) {
        this.asyncSupported = value;
    }

    /**
     * Gets the value of the runAs property.
     * 
     * @return
     *     possible object is
     *     {@link RunAsType }
     *     
     */
    public RunAsType getRunAs() {
        return runAs;
    }

    /**
     * Sets the value of the runAs property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunAsType }
     *     
     */
    public void setRunAs(RunAsType value) {
        this.runAs = value;
    }

    /**
     * Gets the value of the securityRoleRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityRoleRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityRoleRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SecurityRoleRefType }
     * 
     * 
     */
    public List<SecurityRoleRefType> getSecurityRoleRef() {
        if (securityRoleRef == null) {
            securityRoleRef = new ArrayList<SecurityRoleRefType>();
        }
        return this.securityRoleRef;
    }

    /**
     * Gets the value of the multipartConfig property.
     * 
     * @return
     *     possible object is
     *     {@link MultipartConfigType }
     *     
     */
    public MultipartConfigType getMultipartConfig() {
        return multipartConfig;
    }

    /**
     * Sets the value of the multipartConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultipartConfigType }
     *     
     */
    public void setMultipartConfig(MultipartConfigType value) {
        this.multipartConfig = value;
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
