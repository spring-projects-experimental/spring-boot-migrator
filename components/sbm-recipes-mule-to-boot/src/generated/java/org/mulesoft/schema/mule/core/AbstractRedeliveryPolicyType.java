
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractRedeliveryPolicyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractRedeliveryPolicyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dead-letter-queue" type="{http://www.mulesoft.org/schema/mule/core}singleTarget" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="maxRedeliveryCount" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="5" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractRedeliveryPolicyType", propOrder = {
    "deadLetterQueue"
})
@XmlSeeAlso({
    IdempotentRedeliveryPolicyType.class
})
public class AbstractRedeliveryPolicyType
    extends AnnotatedType
{

    @XmlElement(name = "dead-letter-queue")
    protected SingleTarget deadLetterQueue;
    @XmlAttribute(name = "maxRedeliveryCount")
    protected String maxRedeliveryCount;

    /**
     * Gets the value of the deadLetterQueue property.
     * 
     * @return
     *     possible object is
     *     {@link SingleTarget }
     *     
     */
    public SingleTarget getDeadLetterQueue() {
        return deadLetterQueue;
    }

    /**
     * Sets the value of the deadLetterQueue property.
     * 
     * @param value
     *     allowed object is
     *     {@link SingleTarget }
     *     
     */
    public void setDeadLetterQueue(SingleTarget value) {
        this.deadLetterQueue = value;
    }

    /**
     * Gets the value of the maxRedeliveryCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxRedeliveryCount() {
        if (maxRedeliveryCount == null) {
            return "5";
        } else {
            return maxRedeliveryCount;
        }
    }

    /**
     * Sets the value of the maxRedeliveryCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxRedeliveryCount(String value) {
        this.maxRedeliveryCount = value;
    }

}
