
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
 *                 [
 *         The persistence-context-ref element contains a declaration
 *         of Deployment Component's reference to a persistence context
 *         associated within a Deployment Component's
 *         environment. It consists of:
 *         
 *         - an optional description
 *         - the persistence context reference name
 *         - an optional persistence unit name.  If not specified,
 *         the default persistence unit is assumed.
 *         - an optional specification as to whether
 *         the persistence context type is Transaction or
 *         Extended.  If not specified, Transaction is assumed.
 *         - an optional specification as to whether
 *         the persistence context synchronization with the current
 *         transaction is Synchronized or Unsynchronized. If not
 *         specified, Synchronized is assumed.
 *         - an optional list of persistence properties
 *         - optional injection targets
 *         
 *         Examples:
 *         
 *         <persistence-context-ref>
 *         <persistence-context-ref-name>myPersistenceContext
 *         </persistence-context-ref-name>
 *         </persistence-context-ref>
 *         
 *         <persistence-context-ref>
 *         <persistence-context-ref-name>myPersistenceContext
 *         </persistence-context-ref-name>
 *         <persistence-unit-name>PersistenceUnit1
 *         </persistence-unit-name>
 *         <persistence-context-type>Extended</persistence-context-type>
 *         </persistence-context-ref>
 *         
 *         
 *             
 * 
 * <p>Java class for persistence-context-refType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="persistence-context-refType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://xmlns.jcp.org/xml/ns/javaee}descriptionType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="persistence-context-ref-name" type="{http://xmlns.jcp.org/xml/ns/javaee}jndi-nameType"/&gt;
 *         &lt;element name="persistence-unit-name" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="persistence-context-type" type="{http://xmlns.jcp.org/xml/ns/javaee}persistence-context-typeType" minOccurs="0"/&gt;
 *         &lt;element name="persistence-context-synchronization" type="{http://xmlns.jcp.org/xml/ns/javaee}persistence-context-synchronizationType" minOccurs="0"/&gt;
 *         &lt;element name="persistence-property" type="{http://xmlns.jcp.org/xml/ns/javaee}propertyType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}resourceBaseGroup"/&gt;
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
@XmlType(name = "persistence-context-refType", propOrder = {
    "description",
    "persistenceContextRefName",
    "persistenceUnitName",
    "persistenceContextType",
    "persistenceContextSynchronization",
    "persistenceProperty",
    "mappedName",
    "injectionTarget"
})
public class PersistenceContextRefType {

    protected List<DescriptionType> description;
    @XmlElement(name = "persistence-context-ref-name", required = true)
    protected JndiNameType persistenceContextRefName;
    @XmlElement(name = "persistence-unit-name")
    protected org.springframework.sbm.jee.ejb.api.String persistenceUnitName;
    @XmlElement(name = "persistence-context-type")
    protected PersistenceContextTypeType persistenceContextType;
    @XmlElement(name = "persistence-context-synchronization")
    protected PersistenceContextSynchronizationType persistenceContextSynchronization;
    @XmlElement(name = "persistence-property")
    protected List<PropertyType> persistenceProperty;
    @XmlElement(name = "mapped-name")
    protected XsdStringType mappedName;
    @XmlElement(name = "injection-target")
    protected List<InjectionTargetType> injectionTarget;
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
     * Gets the value of the persistenceContextRefName property.
     * 
     * @return
     *     possible object is
     *     {@link JndiNameType }
     *     
     */
    public JndiNameType getPersistenceContextRefName() {
        return persistenceContextRefName;
    }

    /**
     * Sets the value of the persistenceContextRefName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JndiNameType }
     *     
     */
    public void setPersistenceContextRefName(JndiNameType value) {
        this.persistenceContextRefName = value;
    }

    /**
     * Gets the value of the persistenceUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    /**
     * Sets the value of the persistenceUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setPersistenceUnitName(org.springframework.sbm.jee.ejb.api.String value) {
        this.persistenceUnitName = value;
    }

    /**
     * Gets the value of the persistenceContextType property.
     * 
     * @return
     *     possible object is
     *     {@link PersistenceContextTypeType }
     *     
     */
    public PersistenceContextTypeType getPersistenceContextType() {
        return persistenceContextType;
    }

    /**
     * Sets the value of the persistenceContextType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersistenceContextTypeType }
     *     
     */
    public void setPersistenceContextType(PersistenceContextTypeType value) {
        this.persistenceContextType = value;
    }

    /**
     * Gets the value of the persistenceContextSynchronization property.
     * 
     * @return
     *     possible object is
     *     {@link PersistenceContextSynchronizationType }
     *     
     */
    public PersistenceContextSynchronizationType getPersistenceContextSynchronization() {
        return persistenceContextSynchronization;
    }

    /**
     * Sets the value of the persistenceContextSynchronization property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersistenceContextSynchronizationType }
     *     
     */
    public void setPersistenceContextSynchronization(PersistenceContextSynchronizationType value) {
        this.persistenceContextSynchronization = value;
    }

    /**
     * Gets the value of the persistenceProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the persistenceProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersistenceProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyType }
     * 
     * 
     */
    public List<PropertyType> getPersistenceProperty() {
        if (persistenceProperty == null) {
            persistenceProperty = new ArrayList<PropertyType>();
        }
        return this.persistenceProperty;
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
