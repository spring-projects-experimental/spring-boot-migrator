
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
 *         The methodType is used to denote a method of an enterprise
 *         bean.  The method may be any of the following or a set of
 *         any of the following methods may be designated:
 *         business interface method
 *         home interface method
 *         component interface method
 *         web service endpoint interface method
 *         no-interface view method
 *         singleton session bean lifecycle callback method
 *         stateful session bean lifecycle callback method (see 
 *         limitations)
 *         timeout callback method
 *         message-driven bean message listener method
 *         
 *         The ejb-name element must be the name of one of the enterprise 
 *         beans declared in the deployment descriptor.
 *         The optional method-intf element allows distinguishing between a
 *         method with the same signature that is multiply defined
 *         across any of the above.
 *         The method-name element specifies the method name.
 *         The optional method-params elements identify a single method 
 *         among multiple methods with an overloaded method name.
 *         
 *         There are three possible styles of using methodType element
 *         within a method element:
 *         
 *         1.
 *         <method>
 *         <ejb-name>EJBNAME</ejb-name>
 *         <method-name>*</method-name>
 *         </method>
 *         
 *         This style is used to refer to all of the following methods 
 *         of the specified enterprise bean:
 *         business interface methods
 *         home interface methods
 *         component interface methods
 *         web service endpoint interface methods
 *         no-interface view methods
 *         singleton session bean lifecycle callback methods
 *         timeout callback methods
 *         message-driven bean message listener method
 *         
 *         This style may also be used in combination with the 
 *         method-intf element that contains LifecycleCallback as 
 *         the value to specify transaction attributes of a stateful 
 *         session bean PostConstruct, PreDestroy, PrePassivate, 
 *         and PostActivate lifecycle callback methods or to override 
 *         transaction attributes of a singleton session bean 
 *         PostConstruct and PreDestroy lifecycle callback methods.
 *         
 *         2.
 *         <method>
 *         <ejb-name>EJBNAME</ejb-name>
 *         <method-name>METHOD</method-name>
 *         </method>
 *         
 *         This style is used to refer to the specified method of
 *         the specified enterprise bean. If there are multiple
 *         methods with the same overloaded name, the element of
 *         this style refers to all the methods with the overloaded
 *         name.
 *         
 *         This style may be used to refer to stateful session bean
 *         PostConstruct, PreDestroy, PrePassivate, and PostActivate 
 *         lifecycle callback methods to specify their transaction
 *         attributes if any of the following is true:
 *         there is only one method with this name in the specified 
 *         enterprise bean
 *         all overloaded methods with this name in the specified 
 *         enterprise bean are lifecycle callback methods
 *         method-intf element is specified and it contains 
 *         LifecycleCallback as the value 
 *         
 *         3.
 *         <method>
 *         <ejb-name>EJBNAME</ejb-name>
 *         <method-name>METHOD</method-name>
 *         <method-params>
 *         	  <method-param>PARAM-1</method-param>
 *         	  <method-param>PARAM-2</method-param>
 *         	  ...
 *         	  <method-param>PARAM-n</method-param>
 *         </method-params>
 *         </method>
 *         
 *         This style is used to refer to a single method within a
 *         set of methods with an overloaded name. PARAM-1 through
 *         PARAM-n are the fully-qualified Java types of the
 *         method's input parameters (if the method has no input
 *         arguments, the method-params element contains no
 *         method-param elements). Arrays are specified by the
 *         array element's type, followed by one or more pair of
 *         square brackets (e.g. int[][]). 
 *         If a method with the same name and signature is defined 
 *         on more than one interface of an enterprise bean, this 
 *         style refers to all those methods. 
 *         
 *         Examples:
 *         
 *         Style 1: The following method element refers to all of the
 *         following methods of the EmployeeService bean:
 *         no interface view methods
 *         business interface methods   
 *         home interface methods   
 *         component business interface methods   
 *         singleton session bean lifecycle callback methods, if any
 *         timeout callback methods
 *         web service endpoint interface methods
 *         message-driven bean message listener methods (if the bean
 *         a message-driven bean)
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-name>*</method-name>
 *         </method>
 *         
 *         Style 2: The following method element refers to all the
 *         create methods of the EmployeeService bean's home
 *         interface(s).
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-name>create</method-name>
 *         </method>
 *         
 *         Style 3: The following method element refers to the
 *         create(String firstName, String LastName) method of the
 *         EmployeeService bean's home interface(s).
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-name>create</method-name>
 *         <method-params>
 *         	  <method-param>java.lang.String</method-param>
 *         	  <method-param>java.lang.String</method-param>
 *         </method-params>
 *         </method>
 *         
 *         The following example illustrates a Style 3 element with
 *         more complex parameter types. The method 
 *         foobar(char s, int i, int[] iar, mypackage.MyClass mycl, 
 *         mypackage.MyClass[][] myclaar) would be specified as:
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-name>foobar</method-name>
 *         <method-params>
 *         	  <method-param>char</method-param>
 *         	  <method-param>int</method-param>
 *         	  <method-param>int[]</method-param>
 *         	  <method-param>mypackage.MyClass</method-param>
 *         	  <method-param>mypackage.MyClass[][]</method-param>
 *         </method-params>
 *         </method>
 *         
 *         The optional method-intf element can be used when it becomes
 *         necessary to differentiate between a method that is defined
 *         multiple times with the same name and signature across any
 *         of the following methods of an enterprise bean:
 *         business interface methods
 *         home interface methods
 *         component interface methods
 *         web service endpoint methods
 *         no-interface view methods
 *         singleton or stateful session bean lifecycle callback methods
 *         timeout callback methods
 *         message-driven bean message listener methods
 *         
 *         However, if the same method is a method of both the local 
 *         business interface, and the local component interface, 
 *         the same attribute applies to the method for both interfaces.
 *         Likewise, if the same method is a method of both the remote 
 *         business interface and the remote component interface, the same
 *         attribute applies to the method for both interfaces.
 *         
 *         For example, the method element
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-intf>Remote</method-intf>
 *         <method-name>create</method-name>
 *         <method-params>
 *         	  <method-param>java.lang.String</method-param>
 *         	  <method-param>java.lang.String</method-param>
 *         </method-params>
 *         </method>
 *         
 *         can be used to differentiate the create(String, String)
 *         method defined in the remote interface from the
 *         create(String, String) method defined in the remote home
 *         interface, which would be defined as
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-intf>Home</method-intf>
 *         <method-name>create</method-name>
 *         <method-params>
 *         	  <method-param>java.lang.String</method-param>
 *         	  <method-param>java.lang.String</method-param>
 *         </method-params>
 *         </method>
 *         
 *         and the create method that is defined in the local home
 *         interface which would be defined as
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-intf>LocalHome</method-intf>
 *         <method-name>create</method-name>
 *         <method-params>
 *         	  <method-param>java.lang.String</method-param>
 *         	  <method-param>java.lang.String</method-param>
 *         </method-params>
 *         </method>
 *         
 *         The method-intf element can be used with all three Styles
 *         of the method element usage. For example, the following
 *         method element example could be used to refer to all the
 *         methods of the EmployeeService bean's remote home interface
 *         and the remote business interface.
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-intf>Home</method-intf>
 *         <method-name>*</method-name>
 *         </method>
 *         
 *         
 *             
 * 
 * <p>Java class for methodType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="methodType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://xmlns.jcp.org/xml/ns/javaee}descriptionType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ejb-name" type="{http://xmlns.jcp.org/xml/ns/javaee}ejb-nameType"/&gt;
 *         &lt;element name="method-intf" type="{http://xmlns.jcp.org/xml/ns/javaee}method-intfType" minOccurs="0"/&gt;
 *         &lt;element name="method-name" type="{http://xmlns.jcp.org/xml/ns/javaee}method-nameType"/&gt;
 *         &lt;element name="method-params" type="{http://xmlns.jcp.org/xml/ns/javaee}method-paramsType" minOccurs="0"/&gt;
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
@XmlType(name = "methodType", propOrder = {
    "description",
    "ejbName",
    "methodIntf",
    "methodName",
    "methodParams"
})
public class MethodType {

    protected List<DescriptionType> description;
    @XmlElement(name = "ejb-name", required = true)
    protected EjbNameType ejbName;
    @XmlElement(name = "method-intf")
    protected MethodIntfType methodIntf;
    @XmlElement(name = "method-name", required = true)
    protected MethodNameType methodName;
    @XmlElement(name = "method-params")
    protected MethodParamsType methodParams;
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
     * Gets the value of the methodIntf property.
     * 
     * @return
     *     possible object is
     *     {@link MethodIntfType }
     *     
     */
    public MethodIntfType getMethodIntf() {
        return methodIntf;
    }

    /**
     * Sets the value of the methodIntf property.
     * 
     * @param value
     *     allowed object is
     *     {@link MethodIntfType }
     *     
     */
    public void setMethodIntf(MethodIntfType value) {
        this.methodIntf = value;
    }

    /**
     * Gets the value of the methodName property.
     * 
     * @return
     *     possible object is
     *     {@link MethodNameType }
     *     
     */
    public MethodNameType getMethodName() {
        return methodName;
    }

    /**
     * Sets the value of the methodName property.
     * 
     * @param value
     *     allowed object is
     *     {@link MethodNameType }
     *     
     */
    public void setMethodName(MethodNameType value) {
        this.methodName = value;
    }

    /**
     * Gets the value of the methodParams property.
     * 
     * @return
     *     possible object is
     *     {@link MethodParamsType }
     *     
     */
    public MethodParamsType getMethodParams() {
        return methodParams;
    }

    /**
     * Sets the value of the methodParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link MethodParamsType }
     *     
     */
    public void setMethodParams(MethodParamsType value) {
        this.methodParams = value;
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
