
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serviceExceptionStrategyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceExceptionStrategyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractExceptionStrategyType"&gt;
 *       &lt;attribute name="stopMessageProcessing" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceExceptionStrategyType")
public class ServiceExceptionStrategyType
    extends AbstractExceptionStrategyType
{

    @XmlAttribute(name = "stopMessageProcessing")
    protected String stopMessageProcessing;

    /**
     * Gets the value of the stopMessageProcessing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStopMessageProcessing() {
        if (stopMessageProcessing == null) {
            return "false";
        } else {
            return stopMessageProcessing;
        }
    }

    /**
     * Sets the value of the stopMessageProcessing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStopMessageProcessing(String value) {
        this.stopMessageProcessing = value;
    }

}
