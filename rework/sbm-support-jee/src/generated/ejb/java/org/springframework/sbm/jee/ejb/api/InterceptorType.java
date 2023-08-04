
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
 *                 The interceptorType element declares information about a single
 *                 interceptor class. It consists of :
 * 
 *                 - An optional description.
 *                 - The fully-qualified name of the interceptor class.
 *                 - An optional list of around invoke methods declared on the
 *                 interceptor class and/or its super-classes.
 *                 - An optional list of around timeout methods declared on the
 *                 interceptor class and/or its super-classes.
 *                 - An optional list environment dependencies for the interceptor
 *                 class and/or its super-classes.
 *                 - An optional list of post-activate methods declared on the
 *                 interceptor class and/or its super-classes.
 *                 - An optional list of pre-passivate methods declared on the
 *                 interceptor class and/or its super-classes.
 * 
 *             
 * 
 * <p>Java class for interceptorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="interceptorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://xmlns.jcp.org/xml/ns/javaee}descriptionType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="interceptor-class" type="{http://xmlns.jcp.org/xml/ns/javaee}fully-qualified-classType"/&gt;
 *         &lt;element name="around-invoke" type="{http://xmlns.jcp.org/xml/ns/javaee}around-invokeType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="around-timeout" type="{http://xmlns.jcp.org/xml/ns/javaee}around-timeoutType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="around-construct" type="{http://xmlns.jcp.org/xml/ns/javaee}lifecycle-callbackType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}jndiEnvironmentRefsGroup"/&gt;
 *         &lt;element name="post-activate" type="{http://xmlns.jcp.org/xml/ns/javaee}lifecycle-callbackType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="pre-passivate" type="{http://xmlns.jcp.org/xml/ns/javaee}lifecycle-callbackType" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlType(name = "interceptorType", propOrder = {
    "description",
    "interceptorClass",
    "aroundInvoke",
    "aroundTimeout",
    "aroundConstruct",
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
    "postActivate",
    "prePassivate"
})
public class InterceptorType {

    protected List<DescriptionType> description;
    @XmlElement(name = "interceptor-class", required = true)
    protected FullyQualifiedClassType interceptorClass;
    @XmlElement(name = "around-invoke")
    protected List<AroundInvokeType> aroundInvoke;
    @XmlElement(name = "around-timeout")
    protected List<AroundTimeoutType> aroundTimeout;
    @XmlElement(name = "around-construct")
    protected List<LifecycleCallbackType> aroundConstruct;
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
    @XmlElement(name = "post-activate")
    protected List<LifecycleCallbackType> postActivate;
    @XmlElement(name = "pre-passivate")
    protected List<LifecycleCallbackType> prePassivate;
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
     * Gets the value of the interceptorClass property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getInterceptorClass() {
        return interceptorClass;
    }

    /**
     * Sets the value of the interceptorClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setInterceptorClass(FullyQualifiedClassType value) {
        this.interceptorClass = value;
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
     * Gets the value of the aroundConstruct property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aroundConstruct property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAroundConstruct().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LifecycleCallbackType }
     * 
     * 
     */
    public List<LifecycleCallbackType> getAroundConstruct() {
        if (aroundConstruct == null) {
            aroundConstruct = new ArrayList<LifecycleCallbackType>();
        }
        return this.aroundConstruct;
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
     * Gets the value of the postActivate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the postActivate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPostActivate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LifecycleCallbackType }
     * 
     * 
     */
    public List<LifecycleCallbackType> getPostActivate() {
        if (postActivate == null) {
            postActivate = new ArrayList<LifecycleCallbackType>();
        }
        return this.postActivate;
    }

    /**
     * Gets the value of the prePassivate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prePassivate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrePassivate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LifecycleCallbackType }
     * 
     * 
     */
    public List<LifecycleCallbackType> getPrePassivate() {
        if (prePassivate == null) {
            prePassivate = new ArrayList<LifecycleCallbackType>();
        }
        return this.prePassivate;
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
