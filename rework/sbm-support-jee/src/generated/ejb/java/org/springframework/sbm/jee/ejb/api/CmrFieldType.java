
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
 *                 The cmr-fieldType describes the Bean Provider's view of
 *                 a relationship. It consists of an optional description, and
 *                 the name and the class type of a field in the source of a
 *                 role of a relationship. The cmr-field-name element
 *                 corresponds to the name used for the get and set accessor
 *                 methods for the relationship. The cmr-field-type element is
 *                 used only for collection-valued cmr-fields. It specifies the
 *                 type of the collection that is used.
 * 
 *                 Support for entity beans is optional as of EJB 3.2.
 * 
 *             
 * 
 * <p>Java class for cmr-fieldType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cmr-fieldType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://xmlns.jcp.org/xml/ns/javaee}descriptionType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="cmr-field-name" type="{http://xmlns.jcp.org/xml/ns/javaee}string"/&gt;
 *         &lt;element name="cmr-field-type" type="{http://xmlns.jcp.org/xml/ns/javaee}cmr-field-typeType" minOccurs="0"/&gt;
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
@XmlType(name = "cmr-fieldType", propOrder = {
    "description",
    "cmrFieldName",
    "cmrFieldType"
})
public class CmrFieldType {

    protected List<DescriptionType> description;
    @XmlElement(name = "cmr-field-name", required = true)
    protected org.springframework.sbm.jee.ejb.api.String cmrFieldName;
    @XmlElement(name = "cmr-field-type")
    protected CmrFieldTypeType cmrFieldType;
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
     * Gets the value of the cmrFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getCmrFieldName() {
        return cmrFieldName;
    }

    /**
     * Sets the value of the cmrFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setCmrFieldName(org.springframework.sbm.jee.ejb.api.String value) {
        this.cmrFieldName = value;
    }

    /**
     * Gets the value of the cmrFieldType property.
     * 
     * @return
     *     possible object is
     *     {@link CmrFieldTypeType }
     *     
     */
    public CmrFieldTypeType getCmrFieldType() {
        return cmrFieldType;
    }

    /**
     * Sets the value of the cmrFieldType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CmrFieldTypeType }
     *     
     */
    public void setCmrFieldType(CmrFieldTypeType value) {
        this.cmrFieldType = value;
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
