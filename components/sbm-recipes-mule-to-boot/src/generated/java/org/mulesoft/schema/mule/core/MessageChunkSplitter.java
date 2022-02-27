
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}baseSplitterType"&gt;
 *       &lt;attribute name="messageSize" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class MessageChunkSplitter
    extends BaseSplitterType
{

    @XmlAttribute(name = "messageSize", required = true)
    protected String messageSize;

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

}
