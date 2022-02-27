
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
package org.springframework.sbm.project.web.api;

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
 *         The security-constraintType is used to associate
 *         security constraints with one or more web resource
 *         collections
 *         
 *         Used in: web-app
 *         
 *       
 * 
 * <p>Java class for security-constraintType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="security-constraintType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="display-name" type="{http://xmlns.jcp.org/xml/ns/javaee}display-nameType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="web-resource-collection" type="{http://xmlns.jcp.org/xml/ns/javaee}web-resource-collectionType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="auth-constraint" type="{http://xmlns.jcp.org/xml/ns/javaee}auth-constraintType" minOccurs="0"/&gt;
 *         &lt;element name="user-data-constraint" type="{http://xmlns.jcp.org/xml/ns/javaee}user-data-constraintType" minOccurs="0"/&gt;
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
@XmlType(name = "security-constraintType", propOrder = {
    "displayName",
    "webResourceCollection",
    "authConstraint",
    "userDataConstraint"
})
public class SecurityConstraintType {

    @XmlElement(name = "display-name")
    protected List<DisplayNameType> displayName;
    @XmlElement(name = "web-resource-collection", required = true)
    protected List<WebResourceCollectionType> webResourceCollection;
    @XmlElement(name = "auth-constraint")
    protected AuthConstraintType authConstraint;
    @XmlElement(name = "user-data-constraint")
    protected UserDataConstraintType userDataConstraint;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

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
     * Gets the value of the webResourceCollection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the webResourceCollection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWebResourceCollection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WebResourceCollectionType }
     * 
     * 
     */
    public List<WebResourceCollectionType> getWebResourceCollection() {
        if (webResourceCollection == null) {
            webResourceCollection = new ArrayList<WebResourceCollectionType>();
        }
        return this.webResourceCollection;
    }

    /**
     * Gets the value of the authConstraint property.
     * 
     * @return
     *     possible object is
     *     {@link AuthConstraintType }
     *     
     */
    public AuthConstraintType getAuthConstraint() {
        return authConstraint;
    }

    /**
     * Sets the value of the authConstraint property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthConstraintType }
     *     
     */
    public void setAuthConstraint(AuthConstraintType value) {
        this.authConstraint = value;
    }

    /**
     * Gets the value of the userDataConstraint property.
     * 
     * @return
     *     possible object is
     *     {@link UserDataConstraintType }
     *     
     */
    public UserDataConstraintType getUserDataConstraint() {
        return userDataConstraint;
    }

    /**
     * Sets the value of the userDataConstraint property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserDataConstraintType }
     *     
     */
    public void setUserDataConstraint(UserDataConstraintType value) {
        this.userDataConstraint = value;
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
