
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
 *                 Support for entity beans is optional as of EJB 3.2.
 * 
 *                 The entity-beanType declares an entity bean. The declaration
 *                 consists of:
 * 
 *                 - an optional description
 *                 - an optional display name
 *                 - an optional icon element that contains a small and a large
 *                 icon file name
 *                 - a unique name assigned to the enterprise bean
 *                 in the deployment descriptor
 *                 - an optional mapped-name element that can be used to provide
 *                 vendor-specific deployment information such as the physical
 *                 jndi-name of the entity bean's remote home interface. This
 *                 element is not required to be supported by all implementations.
 *                 Any use of this element is non-portable.
 *                 - the names of the entity bean's remote home
 *                 and remote interfaces, if any
 *                 - the names of the entity bean's local home and local
 *                 interfaces, if any
 *                 - the entity bean's implementation class
 *                 - the optional entity bean's persistence management type. If
 *                 this element is not specified it is defaulted to Container.
 *                 - the entity bean's primary key class name
 *                 - an indication of the entity bean's reentrancy
 *                 - an optional specification of the
 *                 entity bean's cmp-version
 *                 - an optional specification of the entity bean's
 *                 abstract schema name
 *                 - an optional list of container-managed fields
 *                 - an optional specification of the primary key
 *                 field
 *                 - an optional declaration of the bean's environment
 *                 entries
 *                 - an optional declaration of the bean's EJB
 *                 references
 *                 - an optional declaration of the bean's local
 *                 EJB references
 *                 - an optional declaration of the bean's web
 *                 service references
 *                 - an optional declaration of the security role
 *                 references
 *                 - an optional declaration of the security identity
 *                 to be used for the execution of the bean's methods
 *                 - an optional declaration of the bean's
 *                 resource manager connection factory references
 *                 - an optional declaration of the bean's
 *                 resource environment references
 *                 - an optional declaration of the bean's message
 *                 destination references
 *                 - an optional set of query declarations
 *                 for finder and select methods for an entity
 *                 bean with cmp-version 2.x.
 * 
 *                 The optional abstract-schema-name element must be specified
 *                 for an entity bean with container-managed persistence and
 *                 cmp-version 2.x.
 * 
 *                 The optional primkey-field may be present in the descriptor
 *                 if the entity's persistence-type is Container.
 * 
 *                 The optional cmp-version element may be present in the
 *                 descriptor if the entity's persistence-type is Container. If
 *                 the persistence-type is Container and the cmp-version
 *                 element is not specified, its value defaults to 2.x.
 * 
 *                 The optional home and remote elements must be specified if
 *                 the entity bean cmp-version is 1.x.
 * 
 *                 The optional home and remote elements must be specified if
 *                 the entity bean has a remote home and remote interface.
 * 
 *                 The optional local-home and local elements must be specified
 *                 if the entity bean has a local home and local interface.
 * 
 *                 Either both the local-home and the local elements or both
 *                 the home and the remote elements must be specified.
 * 
 *                 The optional query elements must be present if the
 *                 persistence-type is Container and the cmp-version is 2.x and
 *                 query methods other than findByPrimaryKey have been defined
 *                 for the entity bean.
 * 
 *                 The other elements that are optional are "optional" in the
 *                 sense that they are omitted if the lists represented by them
 *                 are empty.
 * 
 *                 At least one cmp-field element must be present in the
 *                 descriptor if the entity's persistence-type is Container and
 *                 the cmp-version is 1.x, and none must not be present if the
 *                 entity's persistence-type is Bean.
 * 
 *             
 * 
 * <p>Java class for entity-beanType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="entity-beanType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}descriptionGroup"/&gt;
 *         &lt;element name="ejb-name" type="{http://xmlns.jcp.org/xml/ns/javaee}ejb-nameType"/&gt;
 *         &lt;element name="mapped-name" type="{http://xmlns.jcp.org/xml/ns/javaee}xsdStringType" minOccurs="0"/&gt;
 *         &lt;element name="home" type="{http://xmlns.jcp.org/xml/ns/javaee}homeType" minOccurs="0"/&gt;
 *         &lt;element name="remote" type="{http://xmlns.jcp.org/xml/ns/javaee}remoteType" minOccurs="0"/&gt;
 *         &lt;element name="local-home" type="{http://xmlns.jcp.org/xml/ns/javaee}local-homeType" minOccurs="0"/&gt;
 *         &lt;element name="local" type="{http://xmlns.jcp.org/xml/ns/javaee}localType" minOccurs="0"/&gt;
 *         &lt;element name="ejb-class" type="{http://xmlns.jcp.org/xml/ns/javaee}ejb-classType"/&gt;
 *         &lt;element name="persistence-type" type="{http://xmlns.jcp.org/xml/ns/javaee}persistence-typeType"/&gt;
 *         &lt;element name="prim-key-class" type="{http://xmlns.jcp.org/xml/ns/javaee}fully-qualified-classType"/&gt;
 *         &lt;element name="reentrant" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType"/&gt;
 *         &lt;element name="cmp-version" type="{http://xmlns.jcp.org/xml/ns/javaee}cmp-versionType" minOccurs="0"/&gt;
 *         &lt;element name="abstract-schema-name" type="{http://xmlns.jcp.org/xml/ns/javaee}java-identifierType" minOccurs="0"/&gt;
 *         &lt;element name="cmp-field" type="{http://xmlns.jcp.org/xml/ns/javaee}cmp-fieldType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="primkey-field" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}jndiEnvironmentRefsGroup"/&gt;
 *         &lt;element name="security-role-ref" type="{http://xmlns.jcp.org/xml/ns/javaee}security-role-refType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="security-identity" type="{http://xmlns.jcp.org/xml/ns/javaee}security-identityType" minOccurs="0"/&gt;
 *         &lt;element name="query" type="{http://xmlns.jcp.org/xml/ns/javaee}queryType" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlType(name = "entity-beanType", propOrder = {
    "description",
    "displayName",
    "icon",
    "ejbName",
    "mappedName",
    "home",
    "remote",
    "localHome",
    "local",
    "ejbClass",
    "persistenceType",
    "primKeyClass",
    "reentrant",
    "cmpVersion",
    "abstractSchemaName",
    "cmpField",
    "primkeyField",
    "envEntry",
    "ejbRef",
    "ejbLocalRef",
    "serviceRef",
    "resourceRef",
    "resourceEnvRef",
    "messageDestinationRef",
    "persistenceContextRef",
    "persistenceUnitRef",
    "postConstruct",
    "preDestroy",
    "dataSource",
    "jmsConnectionFactory",
    "jmsDestination",
    "mailSession",
    "connectionFactory",
    "administeredObject",
    "securityRoleRef",
    "securityIdentity",
    "query"
})
public class EntityBeanType {

    protected List<DescriptionType> description;
    @XmlElement(name = "display-name")
    protected List<DisplayNameType> displayName;
    protected List<IconType> icon;
    @XmlElement(name = "ejb-name", required = true)
    protected EjbNameType ejbName;
    @XmlElement(name = "mapped-name")
    protected XsdStringType mappedName;
    protected HomeType home;
    protected RemoteType remote;
    @XmlElement(name = "local-home")
    protected LocalHomeType localHome;
    protected LocalType local;
    @XmlElement(name = "ejb-class", required = true)
    protected EjbClassType ejbClass;
    @XmlElement(name = "persistence-type", required = true)
    protected PersistenceTypeType persistenceType;
    @XmlElement(name = "prim-key-class", required = true)
    protected FullyQualifiedClassType primKeyClass;
    @XmlElement(required = true)
    protected TrueFalseType reentrant;
    @XmlElement(name = "cmp-version")
    protected CmpVersionType cmpVersion;
    @XmlElement(name = "abstract-schema-name")
    protected JavaIdentifierType abstractSchemaName;
    @XmlElement(name = "cmp-field")
    protected List<CmpFieldType> cmpField;
    @XmlElement(name = "primkey-field")
    protected org.springframework.sbm.jee.ejb.api.String primkeyField;
    @XmlElement(name = "env-entry")
    protected List<EnvEntryType> envEntry;
    @XmlElement(name = "ejb-ref")
    protected List<EjbRefType> ejbRef;
    @XmlElement(name = "ejb-local-ref")
    protected List<EjbLocalRefType> ejbLocalRef;
    @XmlElement(name = "service-ref")
    protected List<ServiceRefType> serviceRef;
    @XmlElement(name = "resource-ref")
    protected List<ResourceRefType> resourceRef;
    @XmlElement(name = "resource-env-ref")
    protected List<ResourceEnvRefType> resourceEnvRef;
    @XmlElement(name = "message-destination-ref")
    protected List<MessageDestinationRefType> messageDestinationRef;
    @XmlElement(name = "persistence-context-ref")
    protected List<PersistenceContextRefType> persistenceContextRef;
    @XmlElement(name = "persistence-unit-ref")
    protected List<PersistenceUnitRefType> persistenceUnitRef;
    @XmlElement(name = "post-construct")
    protected List<LifecycleCallbackType> postConstruct;
    @XmlElement(name = "pre-destroy")
    protected List<LifecycleCallbackType> preDestroy;
    @XmlElement(name = "data-source")
    protected List<DataSourceType> dataSource;
    @XmlElement(name = "jms-connection-factory")
    protected List<JmsConnectionFactoryType> jmsConnectionFactory;
    @XmlElement(name = "jms-destination")
    protected List<JmsDestinationType> jmsDestination;
    @XmlElement(name = "mail-session")
    protected List<MailSessionType> mailSession;
    @XmlElement(name = "connection-factory")
    protected List<ConnectionFactoryResourceType> connectionFactory;
    @XmlElement(name = "administered-object")
    protected List<AdministeredObjectType> administeredObject;
    @XmlElement(name = "security-role-ref")
    protected List<SecurityRoleRefType> securityRoleRef;
    @XmlElement(name = "security-identity")
    protected SecurityIdentityType securityIdentity;
    protected List<QueryType> query;
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
     * Gets the value of the ejbName property.
     * 
     * @return
     *     possible object is
     *     {@link EjbNameType }
     *     
     */
    public EjbNameType getEjbName() {
        return ejbName;
    }

    /**
     * Sets the value of the ejbName property.
     * 
     * @param value
     *     allowed object is
     *     {@link EjbNameType }
     *     
     */
    public void setEjbName(EjbNameType value) {
        this.ejbName = value;
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
     * Gets the value of the home property.
     * 
     * @return
     *     possible object is
     *     {@link HomeType }
     *     
     */
    public HomeType getHome() {
        return home;
    }

    /**
     * Sets the value of the home property.
     * 
     * @param value
     *     allowed object is
     *     {@link HomeType }
     *     
     */
    public void setHome(HomeType value) {
        this.home = value;
    }

    /**
     * Gets the value of the remote property.
     * 
     * @return
     *     possible object is
     *     {@link RemoteType }
     *     
     */
    public RemoteType getRemote() {
        return remote;
    }

    /**
     * Sets the value of the remote property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoteType }
     *     
     */
    public void setRemote(RemoteType value) {
        this.remote = value;
    }

    /**
     * Gets the value of the localHome property.
     * 
     * @return
     *     possible object is
     *     {@link LocalHomeType }
     *     
     */
    public LocalHomeType getLocalHome() {
        return localHome;
    }

    /**
     * Sets the value of the localHome property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalHomeType }
     *     
     */
    public void setLocalHome(LocalHomeType value) {
        this.localHome = value;
    }

    /**
     * Gets the value of the local property.
     * 
     * @return
     *     possible object is
     *     {@link LocalType }
     *     
     */
    public LocalType getLocal() {
        return local;
    }

    /**
     * Sets the value of the local property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalType }
     *     
     */
    public void setLocal(LocalType value) {
        this.local = value;
    }

    /**
     * Gets the value of the ejbClass property.
     * 
     * @return
     *     possible object is
     *     {@link EjbClassType }
     *     
     */
    public EjbClassType getEjbClass() {
        return ejbClass;
    }

    /**
     * Sets the value of the ejbClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link EjbClassType }
     *     
     */
    public void setEjbClass(EjbClassType value) {
        this.ejbClass = value;
    }

    /**
     * Gets the value of the persistenceType property.
     * 
     * @return
     *     possible object is
     *     {@link PersistenceTypeType }
     *     
     */
    public PersistenceTypeType getPersistenceType() {
        return persistenceType;
    }

    /**
     * Sets the value of the persistenceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersistenceTypeType }
     *     
     */
    public void setPersistenceType(PersistenceTypeType value) {
        this.persistenceType = value;
    }

    /**
     * Gets the value of the primKeyClass property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getPrimKeyClass() {
        return primKeyClass;
    }

    /**
     * Sets the value of the primKeyClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setPrimKeyClass(FullyQualifiedClassType value) {
        this.primKeyClass = value;
    }

    /**
     * Gets the value of the reentrant property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getReentrant() {
        return reentrant;
    }

    /**
     * Sets the value of the reentrant property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setReentrant(TrueFalseType value) {
        this.reentrant = value;
    }

    /**
     * Gets the value of the cmpVersion property.
     * 
     * @return
     *     possible object is
     *     {@link CmpVersionType }
     *     
     */
    public CmpVersionType getCmpVersion() {
        return cmpVersion;
    }

    /**
     * Sets the value of the cmpVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link CmpVersionType }
     *     
     */
    public void setCmpVersion(CmpVersionType value) {
        this.cmpVersion = value;
    }

    /**
     * Gets the value of the abstractSchemaName property.
     * 
     * @return
     *     possible object is
     *     {@link JavaIdentifierType }
     *     
     */
    public JavaIdentifierType getAbstractSchemaName() {
        return abstractSchemaName;
    }

    /**
     * Sets the value of the abstractSchemaName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JavaIdentifierType }
     *     
     */
    public void setAbstractSchemaName(JavaIdentifierType value) {
        this.abstractSchemaName = value;
    }

    /**
     * Gets the value of the cmpField property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cmpField property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCmpField().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CmpFieldType }
     * 
     * 
     */
    public List<CmpFieldType> getCmpField() {
        if (cmpField == null) {
            cmpField = new ArrayList<CmpFieldType>();
        }
        return this.cmpField;
    }

    /**
     * Gets the value of the primkeyField property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getPrimkeyField() {
        return primkeyField;
    }

    /**
     * Sets the value of the primkeyField property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setPrimkeyField(org.springframework.sbm.jee.ejb.api.String value) {
        this.primkeyField = value;
    }

    /**
     * Gets the value of the envEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the envEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnvEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnvEntryType }
     * 
     * 
     */
    public List<EnvEntryType> getEnvEntry() {
        if (envEntry == null) {
            envEntry = new ArrayList<EnvEntryType>();
        }
        return this.envEntry;
    }

    /**
     * Gets the value of the ejbRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ejbRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEjbRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EjbRefType }
     * 
     * 
     */
    public List<EjbRefType> getEjbRef() {
        if (ejbRef == null) {
            ejbRef = new ArrayList<EjbRefType>();
        }
        return this.ejbRef;
    }

    /**
     * Gets the value of the ejbLocalRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ejbLocalRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEjbLocalRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EjbLocalRefType }
     * 
     * 
     */
    public List<EjbLocalRefType> getEjbLocalRef() {
        if (ejbLocalRef == null) {
            ejbLocalRef = new ArrayList<EjbLocalRefType>();
        }
        return this.ejbLocalRef;
    }

    /**
     * Gets the value of the serviceRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceRefType }
     * 
     * 
     */
    public List<ServiceRefType> getServiceRef() {
        if (serviceRef == null) {
            serviceRef = new ArrayList<ServiceRefType>();
        }
        return this.serviceRef;
    }

    /**
     * Gets the value of the resourceRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceRefType }
     * 
     * 
     */
    public List<ResourceRefType> getResourceRef() {
        if (resourceRef == null) {
            resourceRef = new ArrayList<ResourceRefType>();
        }
        return this.resourceRef;
    }

    /**
     * Gets the value of the resourceEnvRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceEnvRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceEnvRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceEnvRefType }
     * 
     * 
     */
    public List<ResourceEnvRefType> getResourceEnvRef() {
        if (resourceEnvRef == null) {
            resourceEnvRef = new ArrayList<ResourceEnvRefType>();
        }
        return this.resourceEnvRef;
    }

    /**
     * Gets the value of the messageDestinationRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageDestinationRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageDestinationRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageDestinationRefType }
     * 
     * 
     */
    public List<MessageDestinationRefType> getMessageDestinationRef() {
        if (messageDestinationRef == null) {
            messageDestinationRef = new ArrayList<MessageDestinationRefType>();
        }
        return this.messageDestinationRef;
    }

    /**
     * Gets the value of the persistenceContextRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the persistenceContextRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersistenceContextRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersistenceContextRefType }
     * 
     * 
     */
    public List<PersistenceContextRefType> getPersistenceContextRef() {
        if (persistenceContextRef == null) {
            persistenceContextRef = new ArrayList<PersistenceContextRefType>();
        }
        return this.persistenceContextRef;
    }

    /**
     * Gets the value of the persistenceUnitRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the persistenceUnitRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersistenceUnitRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersistenceUnitRefType }
     * 
     * 
     */
    public List<PersistenceUnitRefType> getPersistenceUnitRef() {
        if (persistenceUnitRef == null) {
            persistenceUnitRef = new ArrayList<PersistenceUnitRefType>();
        }
        return this.persistenceUnitRef;
    }

    /**
     * Gets the value of the postConstruct property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the postConstruct property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPostConstruct().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LifecycleCallbackType }
     * 
     * 
     */
    public List<LifecycleCallbackType> getPostConstruct() {
        if (postConstruct == null) {
            postConstruct = new ArrayList<LifecycleCallbackType>();
        }
        return this.postConstruct;
    }

    /**
     * Gets the value of the preDestroy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the preDestroy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreDestroy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LifecycleCallbackType }
     * 
     * 
     */
    public List<LifecycleCallbackType> getPreDestroy() {
        if (preDestroy == null) {
            preDestroy = new ArrayList<LifecycleCallbackType>();
        }
        return this.preDestroy;
    }

    /**
     * Gets the value of the dataSource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataSource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataSource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataSourceType }
     * 
     * 
     */
    public List<DataSourceType> getDataSource() {
        if (dataSource == null) {
            dataSource = new ArrayList<DataSourceType>();
        }
        return this.dataSource;
    }

    /**
     * Gets the value of the jmsConnectionFactory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jmsConnectionFactory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJmsConnectionFactory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JmsConnectionFactoryType }
     * 
     * 
     */
    public List<JmsConnectionFactoryType> getJmsConnectionFactory() {
        if (jmsConnectionFactory == null) {
            jmsConnectionFactory = new ArrayList<JmsConnectionFactoryType>();
        }
        return this.jmsConnectionFactory;
    }

    /**
     * Gets the value of the jmsDestination property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jmsDestination property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJmsDestination().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JmsDestinationType }
     * 
     * 
     */
    public List<JmsDestinationType> getJmsDestination() {
        if (jmsDestination == null) {
            jmsDestination = new ArrayList<JmsDestinationType>();
        }
        return this.jmsDestination;
    }

    /**
     * Gets the value of the mailSession property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mailSession property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMailSession().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MailSessionType }
     * 
     * 
     */
    public List<MailSessionType> getMailSession() {
        if (mailSession == null) {
            mailSession = new ArrayList<MailSessionType>();
        }
        return this.mailSession;
    }

    /**
     * Gets the value of the connectionFactory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connectionFactory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnectionFactory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConnectionFactoryResourceType }
     * 
     * 
     */
    public List<ConnectionFactoryResourceType> getConnectionFactory() {
        if (connectionFactory == null) {
            connectionFactory = new ArrayList<ConnectionFactoryResourceType>();
        }
        return this.connectionFactory;
    }

    /**
     * Gets the value of the administeredObject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the administeredObject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdministeredObject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdministeredObjectType }
     * 
     * 
     */
    public List<AdministeredObjectType> getAdministeredObject() {
        if (administeredObject == null) {
            administeredObject = new ArrayList<AdministeredObjectType>();
        }
        return this.administeredObject;
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
     * Gets the value of the securityIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityIdentityType }
     *     
     */
    public SecurityIdentityType getSecurityIdentity() {
        return securityIdentity;
    }

    /**
     * Sets the value of the securityIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityIdentityType }
     *     
     */
    public void setSecurityIdentity(SecurityIdentityType value) {
        this.securityIdentity = value;
    }

    /**
     * Gets the value of the query property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the query property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuery().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QueryType }
     * 
     * 
     */
    public List<QueryType> getQuery() {
        if (query == null) {
            query = new ArrayList<QueryType>();
        }
        return this.query;
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
