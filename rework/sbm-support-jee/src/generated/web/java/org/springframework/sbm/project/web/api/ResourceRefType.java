
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
 *         [
 *         The resource-refType contains a declaration of a
 *         Deployment Component's reference to an external resource. It
 *         consists of an optional description, the resource manager
 *         connection factory reference name, an optional indication of
 *         the resource manager connection factory type expected by the
 *         Deployment Component code, an optional type of authentication
 *         (Application or Container), and an optional specification of
 *         the shareability of connections obtained from the resource
 *         (Shareable or Unshareable).
 *         
 *         It also includes optional elements to define injection of
 *         the named resource into fields or JavaBeans properties.
 *         
 *         The connection factory type must be supplied unless an
 *         injection target is specified, in which case the type
 *         of the target is used.  If both are specified, the type
 *         must be assignment compatible with the type of the injection
 *         target.
 *         
 *         Example:
 *         
 *         <resource-ref>
 *         <res-ref-name>jdbc/EmployeeAppDB</res-ref-name>
 *         <res-type>javax.sql.DataSource</res-type>
 *         <res-auth>Container</res-auth>
 *         <res-sharing-scope>Shareable</res-sharing-scope>
 *         </resource-ref>
 *         
 *         
 *       
 * 
 * <p>Java class for resource-refType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resource-refType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://xmlns.jcp.org/xml/ns/javaee}descriptionType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="res-ref-name" type="{http://xmlns.jcp.org/xml/ns/javaee}jndi-nameType"/&gt;
 *         &lt;element name="res-type" type="{http://xmlns.jcp.org/xml/ns/javaee}fully-qualified-classType" minOccurs="0"/&gt;
 *         &lt;element name="res-auth" type="{http://xmlns.jcp.org/xml/ns/javaee}res-authType" minOccurs="0"/&gt;
 *         &lt;element name="res-sharing-scope" type="{http://xmlns.jcp.org/xml/ns/javaee}res-sharing-scopeType" minOccurs="0"/&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}resourceGroup"/&gt;
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
@XmlType(name = "resource-refType", propOrder = {
    "description",
    "resRefName",
    "resType",
    "resAuth",
    "resSharingScope",
    "mappedName",
    "injectionTarget",
    "lookupName"
})
public class ResourceRefType {

    protected List<DescriptionType> description;
    @XmlElement(name = "res-ref-name", required = true)
    protected JndiNameType resRefName;
    @XmlElement(name = "res-type")
    protected FullyQualifiedClassType resType;
    @XmlElement(name = "res-auth")
    protected ResAuthType resAuth;
    @XmlElement(name = "res-sharing-scope")
    protected ResSharingScopeType resSharingScope;
    @XmlElement(name = "mapped-name")
    protected XsdStringType mappedName;
    @XmlElement(name = "injection-target")
    protected List<InjectionTargetType> injectionTarget;
    @XmlElement(name = "lookup-name")
    protected XsdStringType lookupName;
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
     * Gets the value of the resRefName property.
     * 
     * @return
     *     possible object is
     *     {@link JndiNameType }
     *     
     */
    public JndiNameType getResRefName() {
        return resRefName;
    }

    /**
     * Sets the value of the resRefName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JndiNameType }
     *     
     */
    public void setResRefName(JndiNameType value) {
        this.resRefName = value;
    }

    /**
     * Gets the value of the resType property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getResType() {
        return resType;
    }

    /**
     * Sets the value of the resType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setResType(FullyQualifiedClassType value) {
        this.resType = value;
    }

    /**
     * Gets the value of the resAuth property.
     * 
     * @return
     *     possible object is
     *     {@link ResAuthType }
     *     
     */
    public ResAuthType getResAuth() {
        return resAuth;
    }

    /**
     * Sets the value of the resAuth property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResAuthType }
     *     
     */
    public void setResAuth(ResAuthType value) {
        this.resAuth = value;
    }

    /**
     * Gets the value of the resSharingScope property.
     * 
     * @return
     *     possible object is
     *     {@link ResSharingScopeType }
     *     
     */
    public ResSharingScopeType getResSharingScope() {
        return resSharingScope;
    }

    /**
     * Sets the value of the resSharingScope property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResSharingScopeType }
     *     
     */
    public void setResSharingScope(ResSharingScopeType value) {
        this.resSharingScope = value;
    }

    /**
     * Gets the value of the mappedName property.
     * 
     * @return
     *     possible object is
     *     {@link XsdStringType }
     *     
     */
    public XsdStringType getMappedName() {
        return mappedName;
    }

    /**
     * Sets the value of the mappedName property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdStringType }
     *     
     */
    public void setMappedName(XsdStringType value) {
        this.mappedName = value;
    }

    /**
     * Gets the value of the injectionTarget property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the injectionTarget property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInjectionTarget().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InjectionTargetType }
     * 
     * 
     */
    public List<InjectionTargetType> getInjectionTarget() {
        if (injectionTarget == null) {
            injectionTarget = new ArrayList<InjectionTargetType>();
        }
        return this.injectionTarget;
    }

    /**
     * Gets the value of the lookupName property.
     * 
     * @return
     *     possible object is
     *     {@link XsdStringType }
     *     
     */
    public XsdStringType getLookupName() {
        return lookupName;
    }

    /**
     * Sets the value of the lookupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdStringType }
     *     
     */
    public void setLookupName(XsdStringType value) {
        this.lookupName = value;
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
