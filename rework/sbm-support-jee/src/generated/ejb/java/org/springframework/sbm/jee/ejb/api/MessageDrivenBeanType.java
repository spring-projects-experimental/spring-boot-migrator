
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
 *                 The message-driven element declares a message-driven
 *                 bean. The declaration consists of:
 * 
 *                 - an optional description
 *                 - an optional display name
 *                 - an optional icon element that contains a small and a large
 *                 icon file name.
 *                 - a name assigned to the enterprise bean in
 *                 the deployment descriptor
 *                 - an optional mapped-name element that can be used to provide
 *                 vendor-specific deployment information such as the physical
 *                 jndi-name of destination from which this message-driven bean
 *                 should consume. This element is not required to be supported
 *                 by all implementations. Any use of this element is non-portable.
 *                 - the message-driven bean's implementation class
 *                 - an optional declaration of the bean's messaging
 *                 type
 *                 - an optional declaration of the bean's timeout method for
 *                 handling programmatically created timers
 *                 - an optional declaration of timers to be automatically created at
 *                 deployment time
 *                 - the optional message-driven bean's transaction management
 *                 type. If it is not defined, it is defaulted to Container.
 *                 - an optional declaration of the bean's
 *                 message-destination-type
 *                 - an optional declaration of the bean's
 *                 message-destination-link
 *                 - an optional declaration of the message-driven bean's
 *                 activation configuration properties
 *                 - an optional list of the message-driven bean class and/or
 *                 superclass around-invoke methods.
 *                 - an optional list of the message-driven bean class and/or
 *                 superclass around-timeout methods.
 *                 - an optional declaration of the bean's environment
 *                 entries
 *                 - an optional declaration of the bean's EJB references
 *                 - an optional declaration of the bean's local EJB
 *                 references
 *                 - an optional declaration of the bean's web service
 *                 references
 *                 - an optional declaration of the security role
 *                 references
 *                 - an optional declaration of the security
 *                 identity to be used for the execution of the bean's
 *                 methods
 *                 - an optional declaration of the bean's
 *                 resource manager connection factory
 *                 references
 *                 - an optional declaration of the bean's resource
 *                 environment references.
 *                 - an optional declaration of the bean's message
 *                 destination references
 * 
 *             
 * 
 * <p>Java class for message-driven-beanType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="message-driven-beanType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}descriptionGroup"/&gt;
 *         &lt;element name="ejb-name" type="{http://xmlns.jcp.org/xml/ns/javaee}ejb-nameType"/&gt;
 *         &lt;element name="mapped-name" type="{http://xmlns.jcp.org/xml/ns/javaee}xsdStringType" minOccurs="0"/&gt;
 *         &lt;element name="ejb-class" type="{http://xmlns.jcp.org/xml/ns/javaee}ejb-classType" minOccurs="0"/&gt;
 *         &lt;element name="messaging-type" type="{http://xmlns.jcp.org/xml/ns/javaee}fully-qualified-classType" minOccurs="0"/&gt;
 *         &lt;element name="timeout-method" type="{http://xmlns.jcp.org/xml/ns/javaee}named-methodType" minOccurs="0"/&gt;
 *         &lt;element name="timer" type="{http://xmlns.jcp.org/xml/ns/javaee}timerType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="transaction-type" type="{http://xmlns.jcp.org/xml/ns/javaee}transaction-typeType" minOccurs="0"/&gt;
 *         &lt;element name="message-destination-type" type="{http://xmlns.jcp.org/xml/ns/javaee}message-destination-typeType" minOccurs="0"/&gt;
 *         &lt;element name="message-destination-link" type="{http://xmlns.jcp.org/xml/ns/javaee}message-destination-linkType" minOccurs="0"/&gt;
 *         &lt;element name="activation-config" type="{http://xmlns.jcp.org/xml/ns/javaee}activation-configType" minOccurs="0"/&gt;
 *         &lt;element name="around-invoke" type="{http://xmlns.jcp.org/xml/ns/javaee}around-invokeType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="around-timeout" type="{http://xmlns.jcp.org/xml/ns/javaee}around-timeoutType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}jndiEnvironmentRefsGroup"/&gt;
 *         &lt;element name="security-role-ref" type="{http://xmlns.jcp.org/xml/ns/javaee}security-role-refType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="security-identity" type="{http://xmlns.jcp.org/xml/ns/javaee}security-identityType" minOccurs="0"/&gt;
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
@XmlType(name = "message-driven-beanType", propOrder = {
    "description",
    "displayName",
    "icon",
    "ejbName",
    "mappedName",
    "ejbClass",
    "messagingType",
    "timeoutMethod",
    "timer",
    "transactionType",
    "messageDestinationType",
    "messageDestinationLink",
    "activationConfig",
    "aroundInvoke",
    "aroundTimeout",
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
    "securityIdentity"
})
public class MessageDrivenBeanType {

    protected List<DescriptionType> description;
    @XmlElement(name = "display-name")
    protected List<DisplayNameType> displayName;
    protected List<IconType> icon;
    @XmlElement(name = "ejb-name", required = true)
    protected EjbNameType ejbName;
    @XmlElement(name = "mapped-name")
    protected XsdStringType mappedName;
    @XmlElement(name = "ejb-class")
    protected EjbClassType ejbClass;
    @XmlElement(name = "messaging-type")
    protected FullyQualifiedClassType messagingType;
    @XmlElement(name = "timeout-method")
    protected NamedMethodType timeoutMethod;
    protected List<TimerType> timer;
    @XmlElement(name = "transaction-type")
    protected TransactionTypeType transactionType;
    @XmlElement(name = "message-destination-type")
    protected MessageDestinationTypeType messageDestinationType;
    @XmlElement(name = "message-destination-link")
    protected MessageDestinationLinkType messageDestinationLink;
    @XmlElement(name = "activation-config")
    protected ActivationConfigType activationConfig;
    @XmlElement(name = "around-invoke")
    protected List<AroundInvokeType> aroundInvoke;
    @XmlElement(name = "around-timeout")
    protected List<AroundTimeoutType> aroundTimeout;
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
     * Gets the value of the messagingType property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getMessagingType() {
        return messagingType;
    }

    /**
     * Sets the value of the messagingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setMessagingType(FullyQualifiedClassType value) {
        this.messagingType = value;
    }

    /**
     * Gets the value of the timeoutMethod property.
     * 
     * @return
     *     possible object is
     *     {@link NamedMethodType }
     *     
     */
    public NamedMethodType getTimeoutMethod() {
        return timeoutMethod;
    }

    /**
     * Sets the value of the timeoutMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamedMethodType }
     *     
     */
    public void setTimeoutMethod(NamedMethodType value) {
        this.timeoutMethod = value;
    }

    /**
     * Gets the value of the timer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimerType }
     * 
     * 
     */
    public List<TimerType> getTimer() {
        if (timer == null) {
            timer = new ArrayList<TimerType>();
        }
        return this.timer;
    }

    /**
     * Gets the value of the transactionType property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionTypeType }
     *     
     */
    public TransactionTypeType getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionTypeType }
     *     
     */
    public void setTransactionType(TransactionTypeType value) {
        this.transactionType = value;
    }

    /**
     * Gets the value of the messageDestinationType property.
     * 
     * @return
     *     possible object is
     *     {@link MessageDestinationTypeType }
     *     
     */
    public MessageDestinationTypeType getMessageDestinationType() {
        return messageDestinationType;
    }

    /**
     * Sets the value of the messageDestinationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageDestinationTypeType }
     *     
     */
    public void setMessageDestinationType(MessageDestinationTypeType value) {
        this.messageDestinationType = value;
    }

    /**
     * Gets the value of the messageDestinationLink property.
     * 
     * @return
     *     possible object is
     *     {@link MessageDestinationLinkType }
     *     
     */
    public MessageDestinationLinkType getMessageDestinationLink() {
        return messageDestinationLink;
    }

    /**
     * Sets the value of the messageDestinationLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageDestinationLinkType }
     *     
     */
    public void setMessageDestinationLink(MessageDestinationLinkType value) {
        this.messageDestinationLink = value;
    }

    /**
     * Gets the value of the activationConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ActivationConfigType }
     *     
     */
    public ActivationConfigType getActivationConfig() {
        return activationConfig;
    }

    /**
     * Sets the value of the activationConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivationConfigType }
     *     
     */
    public void setActivationConfig(ActivationConfigType value) {
        this.activationConfig = value;
    }

    /**
     * Gets the value of the aroundInvoke property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aroundInvoke property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAroundInvoke().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AroundInvokeType }
     * 
     * 
     */
    public List<AroundInvokeType> getAroundInvoke() {
        if (aroundInvoke == null) {
            aroundInvoke = new ArrayList<AroundInvokeType>();
        }
        return this.aroundInvoke;
    }

    /**
     * Gets the value of the aroundTimeout property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aroundTimeout property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAroundTimeout().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AroundTimeoutType }
     * 
     * 
     */
    public List<AroundTimeoutType> getAroundTimeout() {
        if (aroundTimeout == null) {
            aroundTimeout = new ArrayList<AroundTimeoutType>();
        }
        return this.aroundTimeout;
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
