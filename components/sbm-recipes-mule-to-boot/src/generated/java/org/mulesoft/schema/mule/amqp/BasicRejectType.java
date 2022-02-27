
package org.mulesoft.schema.mule.amqp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractMessageProcessorType;


/**
 * 
 *         Rejects the AMQP message that is currently
 *         processed by Mule.
 *       
 * 
 * <p>Java class for basicRejectType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="basicRejectType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageProcessorType"&gt;
 *       &lt;attribute name="requeue" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "basicRejectType")
public class BasicRejectType
    extends AbstractMessageProcessorType
{

    @XmlAttribute(name = "requeue")
    protected String requeue;

    /**
     * Gets the value of the requeue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequeue() {
        if (requeue == null) {
            return "false";
        } else {
            return requeue;
        }
    }

    /**
     * Sets the value of the requeue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequeue(String value) {
        this.requeue = value;
    }

}
