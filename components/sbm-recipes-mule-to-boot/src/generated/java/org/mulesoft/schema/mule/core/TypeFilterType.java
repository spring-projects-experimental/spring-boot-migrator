
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for typeFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="typeFilterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractFilterType"&gt;
 *       &lt;attribute name="expectedType" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "typeFilterType")
public class TypeFilterType
    extends AbstractFilterType
{

    @XmlAttribute(name = "expectedType", required = true)
    protected String expectedType;

    /**
     * Gets the value of the expectedType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpectedType() {
        return expectedType;
    }

    /**
     * Sets the value of the expectedType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpectedType(String value) {
        this.expectedType = value;
    }

}
