
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
 * 
 *                 The queryType defines a finder or select
 *                 query. It contains
 *                 - an optional description of the query
 *                 - the specification of the finder or select
 *                 method it is used by
 *                 - an optional specification of the result type
 *                 mapping, if the query is for a select method
 *                 and entity objects are returned.
 *                 - the EJB QL query string that defines the query.
 * 
 *                 Queries that are expressible in EJB QL must use the ejb-ql
 *                 element to specify the query. If a query is not expressible
 *                 in EJB QL, the description element should be used to
 *                 describe the semantics of the query and the ejb-ql element
 *                 should be empty.
 * 
 *                 The result-type-mapping is an optional element. It can only
 *                 be present if the query-method specifies a select method
 *                 that returns entity objects. The default value for the
 *                 result-type-mapping element is "Local".
 * 
 *             
 * 
 * <p>Java class for queryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://xmlns.jcp.org/xml/ns/javaee}descriptionType" minOccurs="0"/&gt;
 *         &lt;element name="query-method" type="{http://xmlns.jcp.org/xml/ns/javaee}query-methodType"/&gt;
 *         &lt;element name="result-type-mapping" type="{http://xmlns.jcp.org/xml/ns/javaee}result-type-mappingType" minOccurs="0"/&gt;
 *         &lt;element name="ejb-ql" type="{http://xmlns.jcp.org/xml/ns/javaee}xsdStringType"/&gt;
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
@XmlType(name = "queryType", propOrder = {
    "description",
    "queryMethod",
    "resultTypeMapping",
    "ejbQl"
})
public class QueryType {

    protected DescriptionType description;
    @XmlElement(name = "query-method", required = true)
    protected QueryMethodType queryMethod;
    @XmlElement(name = "result-type-mapping")
    protected ResultTypeMappingType resultTypeMapping;
    @XmlElement(name = "ejb-ql", required = true)
    protected XsdStringType ejbQl;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionType }
     *     
     */
    public DescriptionType getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionType }
     *     
     */
    public void setDescription(DescriptionType value) {
        this.description = value;
    }

    /**
     * Gets the value of the queryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link QueryMethodType }
     *     
     */
    public QueryMethodType getQueryMethod() {
        return queryMethod;
    }

    /**
     * Sets the value of the queryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryMethodType }
     *     
     */
    public void setQueryMethod(QueryMethodType value) {
        this.queryMethod = value;
    }

    /**
     * Gets the value of the resultTypeMapping property.
     * 
     * @return
     *     possible object is
     *     {@link ResultTypeMappingType }
     *     
     */
    public ResultTypeMappingType getResultTypeMapping() {
        return resultTypeMapping;
    }

    /**
     * Sets the value of the resultTypeMapping property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultTypeMappingType }
     *     
     */
    public void setResultTypeMapping(ResultTypeMappingType value) {
        this.resultTypeMapping = value;
    }

    /**
     * Gets the value of the ejbQl property.
     * 
     * @return
     *     possible object is
     *     {@link XsdStringType }
     *     
     */
    public XsdStringType getEjbQl() {
        return ejbQl;
    }

    /**
     * Sets the value of the ejbQl property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdStringType }
     *     
     */
    public void setEjbQl(XsdStringType value) {
        this.ejbQl = value;
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
