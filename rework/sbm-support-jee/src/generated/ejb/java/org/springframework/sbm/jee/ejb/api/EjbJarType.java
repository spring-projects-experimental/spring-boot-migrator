
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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 
 *                 The ejb-jarType defines the root element of the EJB
 *                 deployment descriptor. It contains
 * 
 *                 - an optional description of the ejb-jar file
 *                 - an optional display name
 *                 - an optional icon that contains a small and a large
 *                 icon file name
 *                 - an optional module name. Only applicable to
 *                 stand-alone ejb-jars or ejb-jars packaged in an ear.
 *                 Ignored if specified for an ejb-jar.xml within a .war file.
 *                 In that case, standard .war file module-name rules apply.
 *                 - structural information about all included
 *                 enterprise beans that is not specified through
 *                 annotations
 *                 - structural information about interceptor classes
 *                 - a descriptor for container managed relationships,
 *                 if any.
 *                 - an optional application-assembly descriptor
 *                 - an optional name of an ejb-client-jar file for the
 *                 ejb-jar.
 * 
 *             
 * 
 * <p>Java class for ejb-jarType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ejb-jarType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="module-name" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}descriptionGroup"/&gt;
 *         &lt;element name="enterprise-beans" type="{http://xmlns.jcp.org/xml/ns/javaee}enterprise-beansType" minOccurs="0"/&gt;
 *         &lt;element name="interceptors" type="{http://xmlns.jcp.org/xml/ns/javaee}interceptorsType" minOccurs="0"/&gt;
 *         &lt;element name="relationships" type="{http://xmlns.jcp.org/xml/ns/javaee}relationshipsType" minOccurs="0"/&gt;
 *         &lt;element name="assembly-descriptor" type="{http://xmlns.jcp.org/xml/ns/javaee}assembly-descriptorType" minOccurs="0"/&gt;
 *         &lt;element name="ejb-client-jar" type="{http://xmlns.jcp.org/xml/ns/javaee}pathType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="version" use="required" type="{http://xmlns.jcp.org/xml/ns/javaee}dewey-versionType" fixed="3.2" /&gt;
 *       &lt;attribute name="metadata-complete" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ejb-jarType", propOrder = {
    "moduleName",
    "description",
    "displayName",
    "icon",
    "enterpriseBeans",
    "interceptors",
    "relationships",
    "assemblyDescriptor",
    "ejbClientJar"
})
public class EjbJarType {

    @XmlElement(name = "module-name")
    protected org.springframework.sbm.jee.ejb.api.String moduleName;
    protected List<DescriptionType> description;
    @XmlElement(name = "display-name")
    protected List<DisplayNameType> displayName;
    protected List<IconType> icon;
    @XmlElement(name = "enterprise-beans")
    protected EnterpriseBeansType enterpriseBeans;
    protected InterceptorsType interceptors;
    protected RelationshipsType relationships;
    @XmlElement(name = "assembly-descriptor")
    protected AssemblyDescriptorType assemblyDescriptor;
    @XmlElement(name = "ejb-client-jar")
    protected PathType ejbClientJar;
    @XmlAttribute(name = "version", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected java.lang.String version;
    @XmlAttribute(name = "metadata-complete")
    protected Boolean metadataComplete;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the moduleName property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getModuleName() {
        return moduleName;
    }

    /**
     * Sets the value of the moduleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setModuleName(org.springframework.sbm.jee.ejb.api.String value) {
        this.moduleName = value;
    }

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
     * Gets the value of the enterpriseBeans property.
     * 
     * @return
     *     possible object is
     *     {@link EnterpriseBeansType }
     *     
     */
    public EnterpriseBeansType getEnterpriseBeans() {
        return enterpriseBeans;
    }

    /**
     * Sets the value of the enterpriseBeans property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnterpriseBeansType }
     *     
     */
    public void setEnterpriseBeans(EnterpriseBeansType value) {
        this.enterpriseBeans = value;
    }

    /**
     * Gets the value of the interceptors property.
     * 
     * @return
     *     possible object is
     *     {@link InterceptorsType }
     *     
     */
    public InterceptorsType getInterceptors() {
        return interceptors;
    }

    /**
     * Sets the value of the interceptors property.
     * 
     * @param value
     *     allowed object is
     *     {@link InterceptorsType }
     *     
     */
    public void setInterceptors(InterceptorsType value) {
        this.interceptors = value;
    }

    /**
     * Gets the value of the relationships property.
     * 
     * @return
     *     possible object is
     *     {@link RelationshipsType }
     *     
     */
    public RelationshipsType getRelationships() {
        return relationships;
    }

    /**
     * Sets the value of the relationships property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelationshipsType }
     *     
     */
    public void setRelationships(RelationshipsType value) {
        this.relationships = value;
    }

    /**
     * Gets the value of the assemblyDescriptor property.
     * 
     * @return
     *     possible object is
     *     {@link AssemblyDescriptorType }
     *     
     */
    public AssemblyDescriptorType getAssemblyDescriptor() {
        return assemblyDescriptor;
    }

    /**
     * Sets the value of the assemblyDescriptor property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssemblyDescriptorType }
     *     
     */
    public void setAssemblyDescriptor(AssemblyDescriptorType value) {
        this.assemblyDescriptor = value;
    }

    /**
     * Gets the value of the ejbClientJar property.
     * 
     * @return
     *     possible object is
     *     {@link PathType }
     *     
     */
    public PathType getEjbClientJar() {
        return ejbClientJar;
    }

    /**
     * Sets the value of the ejbClientJar property.
     * 
     * @param value
     *     allowed object is
     *     {@link PathType }
     *     
     */
    public void setEjbClientJar(PathType value) {
        this.ejbClientJar = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getVersion() {
        if (version == null) {
            return "3.2";
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setVersion(java.lang.String value) {
        this.version = value;
    }

    /**
     * Gets the value of the metadataComplete property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMetadataComplete() {
        return metadataComplete;
    }

    /**
     * Sets the value of the metadataComplete property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMetadataComplete(Boolean value) {
        this.metadataComplete = value;
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
