
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reconnectSimpleStrategyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reconnectSimpleStrategyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractReconnectionStrategyType"&gt;
 *       &lt;attribute name="frequency" type="{http://www.mulesoft.org/schema/mule/core}substitutableLong" default="2000" /&gt;
 *       &lt;attribute name="count" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="2" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reconnectSimpleStrategyType")
public class ReconnectSimpleStrategyType
    extends AbstractReconnectionStrategyType
{

    @XmlAttribute(name = "frequency")
    protected String frequency;
    @XmlAttribute(name = "count")
    protected String count;

    /**
     * Gets the value of the frequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrequency() {
        if (frequency == null) {
            return "2000";
        } else {
            return frequency;
        }
    }

    /**
     * Sets the value of the frequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrequency(String value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the count property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCount() {
        if (count == null) {
            return "2";
        } else {
            return count;
        }
    }

    /**
     * Sets the value of the count property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCount(String value) {
        this.count = value;
    }

}
