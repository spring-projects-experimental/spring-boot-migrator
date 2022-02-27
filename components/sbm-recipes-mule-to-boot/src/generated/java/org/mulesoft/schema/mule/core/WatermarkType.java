
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for watermarkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="watermarkType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractPollOverrideType"&gt;
 *       &lt;attribute name="variable" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="default-expression" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="update-expression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="selector"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.mulesoft.org/schema/mule/core}substitutableName"&gt;
 *             &lt;enumeration value="MIN"/&gt;
 *             &lt;enumeration value="MAX"/&gt;
 *             &lt;enumeration value="FIRST"/&gt;
 *             &lt;enumeration value="LAST"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="selector-expression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="object-store-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "watermarkType")
public class WatermarkType
    extends AbstractPollOverrideType
{

    @XmlAttribute(name = "variable", required = true)
    protected String variable;
    @XmlAttribute(name = "default-expression", required = true)
    protected String defaultExpression;
    @XmlAttribute(name = "update-expression")
    protected String updateExpression;
    @XmlAttribute(name = "selector")
    protected String selector;
    @XmlAttribute(name = "selector-expression")
    protected String selectorExpression;
    @XmlAttribute(name = "object-store-ref")
    protected String objectStoreRef;

    /**
     * Gets the value of the variable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Sets the value of the variable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariable(String value) {
        this.variable = value;
    }

    /**
     * Gets the value of the defaultExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultExpression() {
        return defaultExpression;
    }

    /**
     * Sets the value of the defaultExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultExpression(String value) {
        this.defaultExpression = value;
    }

    /**
     * Gets the value of the updateExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdateExpression() {
        return updateExpression;
    }

    /**
     * Sets the value of the updateExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdateExpression(String value) {
        this.updateExpression = value;
    }

    /**
     * Gets the value of the selector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelector() {
        return selector;
    }

    /**
     * Sets the value of the selector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelector(String value) {
        this.selector = value;
    }

    /**
     * Gets the value of the selectorExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectorExpression() {
        return selectorExpression;
    }

    /**
     * Sets the value of the selectorExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectorExpression(String value) {
        this.selectorExpression = value;
    }

    /**
     * Gets the value of the objectStoreRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectStoreRef() {
        return objectStoreRef;
    }

    /**
     * Sets the value of the objectStoreRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectStoreRef(String value) {
        this.objectStoreRef = value;
    }

}
