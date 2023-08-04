
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
 *                 The session-beanType declares an session bean. The
 *                 declaration consists of:
 * 
 *                 - an optional description
 *                 - an optional display name
 *                 - an optional icon element that contains a small and a large
 *                 icon file name
 *                 - a name assigned to the enterprise bean
 *                 in the deployment description
 *                 - an optional mapped-name element that can be used to provide
 *                 vendor-specific deployment information such as the physical
 *                 jndi-name of the session bean's remote home/business interface.
 *                 This element is not required to be supported by all
 *                 implementations. Any use of this element is non-portable.
 *                 - the names of all the remote or local business interfaces,
 *                 if any
 *                 - the names of the session bean's remote home and
 *                 remote interfaces, if any
 *                 - the names of the session bean's local home and
 *                 local interfaces, if any
 *                 - an optional declaration that this bean exposes a
 *                 no-interface view
 *                 - the name of the session bean's web service endpoint
 *                 interface, if any
 *                 - the session bean's implementation class
 *                 - the session bean's state management type
 *                 - an optional declaration of a stateful session bean's timeout value
 *                 - an optional declaration of the session bean's timeout method for
 *                 handling programmatically created timers
 *                 - an optional declaration of timers to be automatically created at
 *                 deployment time
 *                 - an optional declaration that a Singleton bean has eager
 *                 initialization
 *                 - an optional declaration of a Singleton/Stateful bean's concurrency
 *                 management type
 *                 - an optional declaration of the method locking metadata
 *                 for a Singleton with container managed concurrency
 *                 - an optional declaration of the other Singleton beans in the
 *                 application that must be initialized before this bean
 *                 - an optional declaration of the session bean's asynchronous
 *                 methods
 *                 - the optional session bean's transaction management type.
 *                 If it is not present, it is defaulted to Container.
 *                 - an optional declaration of a stateful session bean's
 *                 afterBegin, beforeCompletion, and/or afterCompletion methods
 *                 - an optional list of the session bean class and/or
 *                 superclass around-invoke methods.
 *                 - an optional list of the session bean class and/or
 *                 superclass around-timeout methods.
 *                 - an optional declaration of the bean's
 *                 environment entries
 *                 - an optional declaration of the bean's EJB references
 *                 - an optional declaration of the bean's local
 *                 EJB references
 *                 - an optional declaration of the bean's web
 *                 service references
 *                 - an optional declaration of the security role
 *                 references
 *                 - an optional declaration of the security identity
 *                 to be used for the execution of the bean's methods
 *                 - an optional declaration of the bean's resource
 *                 manager connection factory references
 *                 - an optional declaration of the bean's resource
 *                 environment references.
 *                 - an optional declaration of the bean's message
 *                 destination references
 *                 - an optional specification as to whether the stateful
 *                 session bean is passivation capable or not. If not
 *                 specified, the bean is assumed to be passivation capable
 * 
 *                 The elements that are optional are "optional" in the sense
 *                 that they are omitted when if lists represented by them are
 *                 empty.
 * 
 *                 The service-endpoint element may only be specified if the
 *                 bean is a stateless session bean.
 * 
 *             
 * 
 * <p>Java class for session-beanType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="session-beanType"&gt;
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
 *         &lt;element name="business-local" type="{http://xmlns.jcp.org/xml/ns/javaee}fully-qualified-classType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="business-remote" type="{http://xmlns.jcp.org/xml/ns/javaee}fully-qualified-classType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="local-bean" type="{http://xmlns.jcp.org/xml/ns/javaee}emptyType" minOccurs="0"/&gt;
 *         &lt;element name="service-endpoint" type="{http://xmlns.jcp.org/xml/ns/javaee}fully-qualified-classType" minOccurs="0"/&gt;
 *         &lt;element name="ejb-class" type="{http://xmlns.jcp.org/xml/ns/javaee}ejb-classType" minOccurs="0"/&gt;
 *         &lt;element name="session-type" type="{http://xmlns.jcp.org/xml/ns/javaee}session-typeType" minOccurs="0"/&gt;
 *         &lt;element name="stateful-timeout" type="{http://xmlns.jcp.org/xml/ns/javaee}stateful-timeoutType" minOccurs="0"/&gt;
 *         &lt;element name="timeout-method" type="{http://xmlns.jcp.org/xml/ns/javaee}named-methodType" minOccurs="0"/&gt;
 *         &lt;element name="timer" type="{http://xmlns.jcp.org/xml/ns/javaee}timerType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="init-on-startup" type="{http://xmlns.jcp.org/xml/ns/javaee}true-falseType" minOccurs="0"/&gt;
 *         &lt;element name="concurrency-management-type" type="{http://xmlns.jcp.org/xml/ns/javaee}concurrency-management-typeType" minOccurs="0"/&gt;
 *         &lt;element name="concurrent-method" type="{http://xmlns.jcp.org/xml/ns/javaee}concurrent-methodType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="depends-on" type="{http://xmlns.jcp.org/xml/ns/javaee}depends-onType" minOccurs="0"/&gt;
 *         &lt;element name="init-method" type="{http://xmlns.jcp.org/xml/ns/javaee}init-methodType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="remove-method" type="{http://xmlns.jcp.org/xml/ns/javaee}remove-methodType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="async-method" type="{http://xmlns.jcp.org/xml/ns/javaee}async-methodType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="transaction-type" type="{http://xmlns.jcp.org/xml/ns/javaee}transaction-typeType" minOccurs="0"/&gt;
 *         &lt;element name="after-begin-method" type="{http://xmlns.jcp.org/xml/ns/javaee}named-methodType" minOccurs="0"/&gt;
 *         &lt;element name="before-completion-method" type="{http://xmlns.jcp.org/xml/ns/javaee}named-methodType" minOccurs="0"/&gt;
 *         &lt;element name="after-completion-method" type="{http://xmlns.jcp.org/xml/ns/javaee}named-methodType" minOccurs="0"/&gt;
 *         &lt;element name="around-invoke" type="{http://xmlns.jcp.org/xml/ns/javaee}around-invokeType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="around-timeout" type="{http://xmlns.jcp.org/xml/ns/javaee}around-timeoutType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;group ref="{http://xmlns.jcp.org/xml/ns/javaee}jndiEnvironmentRefsGroup"/&gt;
 *         &lt;element name="post-activate" type="{http://xmlns.jcp.org/xml/ns/javaee}lifecycle-callbackType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="pre-passivate" type="{http://xmlns.jcp.org/xml/ns/javaee}lifecycle-callbackType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="security-role-ref" type="{http://xmlns.jcp.org/xml/ns/javaee}security-role-refType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="security-identity" type="{http://xmlns.jcp.org/xml/ns/javaee}security-identityType" minOccurs="0"/&gt;
 *         &lt;element name="passivation-capable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlType(name = "session-beanType", propOrder = {
    "description",
    "displayName",
    "icon",
    "ejbName",
    "mappedName",
    "home",
    "remote",
    "localHome",
    "local",
    "businessLocal",
    "businessRemote",
    "localBean",
    "serviceEndpoint",
    "ejbClass",
    "sessionType",
    "statefulTimeout",
    "timeoutMethod",
    "timer",
    "initOnStartup",
    "concurrencyManagementType",
    "concurrentMethod",
    "dependsOn",
    "initMethod",
    "removeMethod",
    "asyncMethod",
    "transactionType",
    "afterBeginMethod",
    "beforeCompletionMethod",
    "afterCompletionMethod",
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
    "postActivate",
    "prePassivate",
    "securityRoleRef",
    "securityIdentity",
    "passivationCapable"
})
public class SessionBeanType {

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
    @XmlElement(name = "business-local")
    protected List<FullyQualifiedClassType> businessLocal;
    @XmlElement(name = "business-remote")
    protected List<FullyQualifiedClassType> businessRemote;
    @XmlElement(name = "local-bean")
    protected EmptyType localBean;
    @XmlElement(name = "service-endpoint")
    protected FullyQualifiedClassType serviceEndpoint;
    @XmlElement(name = "ejb-class")
    protected EjbClassType ejbClass;
    @XmlElement(name = "session-type")
    protected SessionTypeType sessionType;
    @XmlElement(name = "stateful-timeout")
    protected StatefulTimeoutType statefulTimeout;
    @XmlElement(name = "timeout-method")
    protected NamedMethodType timeoutMethod;
    protected List<TimerType> timer;
    @XmlElement(name = "init-on-startup")
    protected TrueFalseType initOnStartup;
    @XmlElement(name = "concurrency-management-type")
    protected ConcurrencyManagementTypeType concurrencyManagementType;
    @XmlElement(name = "concurrent-method")
    protected List<ConcurrentMethodType> concurrentMethod;
    @XmlElement(name = "depends-on")
    protected DependsOnType dependsOn;
    @XmlElement(name = "init-method")
    protected List<InitMethodType> initMethod;
    @XmlElement(name = "remove-method")
    protected List<RemoveMethodType> removeMethod;
    @XmlElement(name = "async-method")
    protected List<AsyncMethodType> asyncMethod;
    @XmlElement(name = "transaction-type")
    protected TransactionTypeType transactionType;
    @XmlElement(name = "after-begin-method")
    protected NamedMethodType afterBeginMethod;
    @XmlElement(name = "before-completion-method")
    protected NamedMethodType beforeCompletionMethod;
    @XmlElement(name = "after-completion-method")
    protected NamedMethodType afterCompletionMethod;
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
    @XmlElement(name = "post-activate")
    protected List<LifecycleCallbackType> postActivate;
    @XmlElement(name = "pre-passivate")
    protected List<LifecycleCallbackType> prePassivate;
    @XmlElement(name = "security-role-ref")
    protected List<SecurityRoleRefType> securityRoleRef;
    @XmlElement(name = "security-identity")
    protected SecurityIdentityType securityIdentity;
    @XmlElement(name = "passivation-capable", defaultValue = "true")
    protected Boolean passivationCapable;
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
     * Gets the value of the businessLocal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the businessLocal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBusinessLocal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FullyQualifiedClassType }
     * 
     * 
     */
    public List<FullyQualifiedClassType> getBusinessLocal() {
        if (businessLocal == null) {
            businessLocal = new ArrayList<FullyQualifiedClassType>();
        }
        return this.businessLocal;
    }

    /**
     * Gets the value of the businessRemote property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the businessRemote property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBusinessRemote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FullyQualifiedClassType }
     * 
     * 
     */
    public List<FullyQualifiedClassType> getBusinessRemote() {
        if (businessRemote == null) {
            businessRemote = new ArrayList<FullyQualifiedClassType>();
        }
        return this.businessRemote;
    }

    /**
     * Gets the value of the localBean property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getLocalBean() {
        return localBean;
    }

    /**
     * Sets the value of the localBean property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setLocalBean(EmptyType value) {
        this.localBean = value;
    }

    /**
     * Gets the value of the serviceEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getServiceEndpoint() {
        return serviceEndpoint;
    }

    /**
     * Sets the value of the serviceEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setServiceEndpoint(FullyQualifiedClassType value) {
        this.serviceEndpoint = value;
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
     * Gets the value of the sessionType property.
     * 
     * @return
     *     possible object is
     *     {@link SessionTypeType }
     *     
     */
    public SessionTypeType getSessionType() {
        return sessionType;
    }

    /**
     * Sets the value of the sessionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SessionTypeType }
     *     
     */
    public void setSessionType(SessionTypeType value) {
        this.sessionType = value;
    }

    /**
     * Gets the value of the statefulTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link StatefulTimeoutType }
     *     
     */
    public StatefulTimeoutType getStatefulTimeout() {
        return statefulTimeout;
    }

    /**
     * Sets the value of the statefulTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatefulTimeoutType }
     *     
     */
    public void setStatefulTimeout(StatefulTimeoutType value) {
        this.statefulTimeout = value;
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
     * Gets the value of the initOnStartup property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalseType }
     *     
     */
    public TrueFalseType getInitOnStartup() {
        return initOnStartup;
    }

    /**
     * Sets the value of the initOnStartup property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalseType }
     *     
     */
    public void setInitOnStartup(TrueFalseType value) {
        this.initOnStartup = value;
    }

    /**
     * Gets the value of the concurrencyManagementType property.
     * 
     * @return
     *     possible object is
     *     {@link ConcurrencyManagementTypeType }
     *     
     */
    public ConcurrencyManagementTypeType getConcurrencyManagementType() {
        return concurrencyManagementType;
    }

    /**
     * Sets the value of the concurrencyManagementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConcurrencyManagementTypeType }
     *     
     */
    public void setConcurrencyManagementType(ConcurrencyManagementTypeType value) {
        this.concurrencyManagementType = value;
    }

    /**
     * Gets the value of the concurrentMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the concurrentMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConcurrentMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConcurrentMethodType }
     * 
     * 
     */
    public List<ConcurrentMethodType> getConcurrentMethod() {
        if (concurrentMethod == null) {
            concurrentMethod = new ArrayList<ConcurrentMethodType>();
        }
        return this.concurrentMethod;
    }

    /**
     * Gets the value of the dependsOn property.
     * 
     * @return
     *     possible object is
     *     {@link DependsOnType }
     *     
     */
    public DependsOnType getDependsOn() {
        return dependsOn;
    }

    /**
     * Sets the value of the dependsOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link DependsOnType }
     *     
     */
    public void setDependsOn(DependsOnType value) {
        this.dependsOn = value;
    }

    /**
     * Gets the value of the initMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the initMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInitMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InitMethodType }
     * 
     * 
     */
    public List<InitMethodType> getInitMethod() {
        if (initMethod == null) {
            initMethod = new ArrayList<InitMethodType>();
        }
        return this.initMethod;
    }

    /**
     * Gets the value of the removeMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the removeMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRemoveMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RemoveMethodType }
     * 
     * 
     */
    public List<RemoveMethodType> getRemoveMethod() {
        if (removeMethod == null) {
            removeMethod = new ArrayList<RemoveMethodType>();
        }
        return this.removeMethod;
    }

    /**
     * Gets the value of the asyncMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asyncMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsyncMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AsyncMethodType }
     * 
     * 
     */
    public List<AsyncMethodType> getAsyncMethod() {
        if (asyncMethod == null) {
            asyncMethod = new ArrayList<AsyncMethodType>();
        }
        return this.asyncMethod;
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
     * Gets the value of the afterBeginMethod property.
     * 
     * @return
     *     possible object is
     *     {@link NamedMethodType }
     *     
     */
    public NamedMethodType getAfterBeginMethod() {
        return afterBeginMethod;
    }

    /**
     * Sets the value of the afterBeginMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamedMethodType }
     *     
     */
    public void setAfterBeginMethod(NamedMethodType value) {
        this.afterBeginMethod = value;
    }

    /**
     * Gets the value of the beforeCompletionMethod property.
     * 
     * @return
     *     possible object is
     *     {@link NamedMethodType }
     *     
     */
    public NamedMethodType getBeforeCompletionMethod() {
        return beforeCompletionMethod;
    }

    /**
     * Sets the value of the beforeCompletionMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamedMethodType }
     *     
     */
    public void setBeforeCompletionMethod(NamedMethodType value) {
        this.beforeCompletionMethod = value;
    }

    /**
     * Gets the value of the afterCompletionMethod property.
     * 
     * @return
     *     possible object is
     *     {@link NamedMethodType }
     *     
     */
    public NamedMethodType getAfterCompletionMethod() {
        return afterCompletionMethod;
    }

    /**
     * Sets the value of the afterCompletionMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamedMethodType }
     *     
     */
    public void setAfterCompletionMethod(NamedMethodType value) {
        this.afterCompletionMethod = value;
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
     * Gets the value of the passivationCapable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPassivationCapable() {
        return passivationCapable;
    }

    /**
     * Sets the value of the passivationCapable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPassivationCapable(Boolean value) {
        this.passivationCapable = value;
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
