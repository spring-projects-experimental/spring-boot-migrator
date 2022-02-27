
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customTransactionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customTransactionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}baseTransactionType"&gt;
 *       &lt;attribute name="factory-class" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="factory-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customTransactionType")
public class CustomTransactionType
    extends BaseTransactionType
{

    @XmlAttribute(name = "factory-class")
    protected String factoryClass;
    @XmlAttribute(name = "factory-ref")
    protected String factoryRef;

    /**
     * Gets the value of the factoryClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactoryClass() {
        return factoryClass;
    }

    /**
     * Sets the value of the factoryClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactoryClass(String value) {
        this.factoryClass = value;
    }

    /**
     * Gets the value of the factoryRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactoryRef() {
        return factoryRef;
    }

    /**
     * Sets the value of the factoryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactoryRef(String value) {
        this.factoryRef = value;
    }

}
