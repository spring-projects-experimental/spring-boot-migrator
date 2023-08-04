
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
 *         The ejb-relationship-roleType describes a role within a
 *         relationship. There are two roles in each relationship.
 *         
 *         The ejb-relationship-roleType contains an optional
 *         description; an optional name for the relationship role; a
 *         specification of the multiplicity of the role; an optional
 *         specification of cascade-delete functionality for the role;
 *         the role source; and a declaration of the cmr-field, if any,
 *         by means of which the other side of the relationship is
 *         accessed from the perspective of the role source.
 *         
 *         The multiplicity and role-source element are mandatory.
 *         
 *         The relationship-role-source element designates an entity
 *         bean by means of an ejb-name element. For bidirectional
 *         relationships, both roles of a relationship must declare a
 *         relationship-role-source element that specifies a cmr-field
 *         in terms of which the relationship is accessed. The lack of
 *         a cmr-field element in an ejb-relationship-role specifies
 *         that the relationship is unidirectional in navigability and
 *         the entity bean that participates in the relationship is
 *         "not aware" of the relationship.
 *         
 *         Example:
 *         
 *         <ejb-relation>
 *         <ejb-relation-name>Product-LineItem</ejb-relation-name>
 *         <ejb-relationship-role>
 *         	  <ejb-relationship-role-name>product-has-lineitems
 *         	  </ejb-relationship-role-name>
 *         	  <multiplicity>One</multiplicity>
 *         	  <relationship-role-source>
 *         	  <ejb-name>ProductEJB</ejb-name>
 *         	  </relationship-role-source>
 *         </ejb-relationship-role>
 *         </ejb-relation>
 *         
 *         Support for entity beans is optional as of EJB 3.2.
 *         
 *         
 *             
 * 
 * <p>Java class for ejb-relationship-roleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ejb-relationship-roleType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://xmlns.jcp.org/xml/ns/javaee}descriptionType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ejb-relationship-role-name" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="multiplicity" type="{http://xmlns.jcp.org/xml/ns/javaee}multiplicityType"/&gt;
 *         &lt;element name="cascade-delete" type="{http://xmlns.jcp.org/xml/ns/javaee}emptyType" minOccurs="0"/&gt;
 *         &lt;element name="relationship-role-source" type="{http://xmlns.jcp.org/xml/ns/javaee}relationship-role-sourceType"/&gt;
 *         &lt;element name="cmr-field" type="{http://xmlns.jcp.org/xml/ns/javaee}cmr-fieldType" minOccurs="0"/&gt;
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
@XmlType(name = "ejb-relationship-roleType", propOrder = {
    "description",
    "ejbRelationshipRoleName",
    "multiplicity",
    "cascadeDelete",
    "relationshipRoleSource",
    "cmrField"
})
public class EjbRelationshipRoleType {

    protected List<DescriptionType> description;
    @XmlElement(name = "ejb-relationship-role-name")
    protected org.springframework.sbm.jee.ejb.api.String ejbRelationshipRoleName;
    @XmlElement(required = true)
    protected MultiplicityType multiplicity;
    @XmlElement(name = "cascade-delete")
    protected EmptyType cascadeDelete;
    @XmlElement(name = "relationship-role-source", required = true)
    protected RelationshipRoleSourceType relationshipRoleSource;
    @XmlElement(name = "cmr-field")
    protected CmrFieldType cmrField;
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
     * Gets the value of the ejbRelationshipRoleName property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getEjbRelationshipRoleName() {
        return ejbRelationshipRoleName;
    }

    /**
     * Sets the value of the ejbRelationshipRoleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setEjbRelationshipRoleName(org.springframework.sbm.jee.ejb.api.String value) {
        this.ejbRelationshipRoleName = value;
    }

    /**
     * Gets the value of the multiplicity property.
     * 
     * @return
     *     possible object is
     *     {@link MultiplicityType }
     *     
     */
    public MultiplicityType getMultiplicity() {
        return multiplicity;
    }

    /**
     * Sets the value of the multiplicity property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiplicityType }
     *     
     */
    public void setMultiplicity(MultiplicityType value) {
        this.multiplicity = value;
    }

    /**
     * Gets the value of the cascadeDelete property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getCascadeDelete() {
        return cascadeDelete;
    }

    /**
     * Sets the value of the cascadeDelete property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setCascadeDelete(EmptyType value) {
        this.cascadeDelete = value;
    }

    /**
     * Gets the value of the relationshipRoleSource property.
     * 
     * @return
     *     possible object is
     *     {@link RelationshipRoleSourceType }
     *     
     */
    public RelationshipRoleSourceType getRelationshipRoleSource() {
        return relationshipRoleSource;
    }

    /**
     * Sets the value of the relationshipRoleSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelationshipRoleSourceType }
     *     
     */
    public void setRelationshipRoleSource(RelationshipRoleSourceType value) {
        this.relationshipRoleSource = value;
    }

    /**
     * Gets the value of the cmrField property.
     * 
     * @return
     *     possible object is
     *     {@link CmrFieldType }
     *     
     */
    public CmrFieldType getCmrField() {
        return cmrField;
    }

    /**
     * Sets the value of the cmrField property.
     * 
     * @param value
     *     allowed object is
     *     {@link CmrFieldType }
     *     
     */
    public void setCmrField(CmrFieldType value) {
        this.cmrField = value;
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
