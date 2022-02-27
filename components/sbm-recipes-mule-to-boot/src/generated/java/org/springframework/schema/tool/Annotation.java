
package org.springframework.schema.tool;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence minOccurs="0"&gt;
 *         &lt;element name="expected-type" type="{http://www.springframework.org/schema/tool}typedParameterType" minOccurs="0"/&gt;
 *         &lt;element name="assignable-to" type="{http://www.springframework.org/schema/tool}assignableToType" minOccurs="0"/&gt;
 *         &lt;element name="exports" type="{http://www.springframework.org/schema/tool}exportsType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="registers-scope" type="{http://www.springframework.org/schema/tool}registersScopeType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="expected-method" type="{http://www.springframework.org/schema/tool}expectedMethodType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="kind" default="direct"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="ref"/&gt;
 *             &lt;enumeration value="direct"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "expectedType",
    "assignableTo",
    "exports",
    "registersScope",
    "expectedMethod"
})
@XmlRootElement(name = "annotation")
public class Annotation {

    @XmlElement(name = "expected-type")
    protected TypedParameterType expectedType;
    @XmlElement(name = "assignable-to")
    protected AssignableToType assignableTo;
    protected List<ExportsType> exports;
    @XmlElement(name = "registers-scope")
    protected List<RegistersScopeType> registersScope;
    @XmlElement(name = "expected-method")
    protected List<ExpectedMethodType> expectedMethod;
    @XmlAttribute(name = "kind")
    protected String kind;

    /**
     * Gets the value of the expectedType property.
     * 
     * @return
     *     possible object is
     *     {@link TypedParameterType }
     *     
     */
    public TypedParameterType getExpectedType() {
        return expectedType;
    }

    /**
     * Sets the value of the expectedType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypedParameterType }
     *     
     */
    public void setExpectedType(TypedParameterType value) {
        this.expectedType = value;
    }

    /**
     * Gets the value of the assignableTo property.
     * 
     * @return
     *     possible object is
     *     {@link AssignableToType }
     *     
     */
    public AssignableToType getAssignableTo() {
        return assignableTo;
    }

    /**
     * Sets the value of the assignableTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssignableToType }
     *     
     */
    public void setAssignableTo(AssignableToType value) {
        this.assignableTo = value;
    }

    /**
     * Gets the value of the exports property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the exports property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExports().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExportsType }
     * 
     * 
     */
    public List<ExportsType> getExports() {
        if (exports == null) {
            exports = new ArrayList<ExportsType>();
        }
        return this.exports;
    }

    /**
     * Gets the value of the registersScope property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the registersScope property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegistersScope().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegistersScopeType }
     * 
     * 
     */
    public List<RegistersScopeType> getRegistersScope() {
        if (registersScope == null) {
            registersScope = new ArrayList<RegistersScopeType>();
        }
        return this.registersScope;
    }

    /**
     * Gets the value of the expectedMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the expectedMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExpectedMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExpectedMethodType }
     * 
     * 
     */
    public List<ExpectedMethodType> getExpectedMethod() {
        if (expectedMethod == null) {
            expectedMethod = new ArrayList<ExpectedMethodType>();
        }
        return this.expectedMethod;
    }

    /**
     * Gets the value of the kind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKind() {
        if (kind == null) {
            return "direct";
        } else {
            return kind;
        }
    }

    /**
     * Sets the value of the kind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKind(String value) {
        this.kind = value;
    }

}
