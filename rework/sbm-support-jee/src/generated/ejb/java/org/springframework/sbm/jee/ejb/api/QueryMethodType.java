
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
 *         The query-method specifies the method for a finder or select
 *         query.
 *         
 *         The method-name element specifies the name of a finder or select
 *         method in the entity bean's implementation class.
 *         
 *         Each method-param must be defined for a query-method using the
 *         method-params element.
 *         
 *         It is used by the query-method element. 
 *         
 *         Example:
 *         
 *         <query>
 *         <description>Method finds large orders</description>
 *         <query-method>
 *         	  <method-name>findLargeOrders</method-name>
 *         	  <method-params></method-params>
 *         </query-method>
 *         <ejb-ql>
 *         	SELECT OBJECT(o) FROM Order o
 *         	  WHERE o.amount &gt; 1000
 *         </ejb-ql>
 *         </query>
 *         
 *         Support for entity beans is optional as of EJB 3.2.
 *         
 *         
 *             
 * 
 * <p>Java class for query-methodType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="query-methodType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="method-name" type="{http://xmlns.jcp.org/xml/ns/javaee}method-nameType"/&gt;
 *         &lt;element name="method-params" type="{http://xmlns.jcp.org/xml/ns/javaee}method-paramsType"/&gt;
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
@XmlType(name = "query-methodType", propOrder = {
    "methodName",
    "methodParams"
})
public class QueryMethodType {

    @XmlElement(name = "method-name", required = true)
    protected MethodNameType methodName;
    @XmlElement(name = "method-params", required = true)
    protected MethodParamsType methodParams;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

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
