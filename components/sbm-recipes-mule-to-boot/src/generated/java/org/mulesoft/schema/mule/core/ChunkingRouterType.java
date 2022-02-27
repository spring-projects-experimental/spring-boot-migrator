
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for chunkingRouterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="chunkingRouterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}multipleEndpointFilteringOutboundRouterType"&gt;
 *       &lt;attribute name="messageSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="numberOfMessages" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chunkingRouterType")
public class ChunkingRouterType
    extends MultipleEndpointFilteringOutboundRouterType
{

    @XmlAttribute(name = "messageSize")
    protected String messageSize;
    @XmlAttribute(name = "numberOfMessages")
    protected String numberOfMessages;

    /**
     * Gets the value of the messageSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageSize() {
        return messageSize;
    }

    /**
     * Sets the value of the messageSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageSize(String value) {
        this.messageSize = value;
    }

    /**
     * Gets the value of the numberOfMessages property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfMessages() {
        return numberOfMessages;
    }

    /**
     * Sets the value of the numberOfMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfMessages(String value) {
        this.numberOfMessages = value;
    }

}
