
package org.mulesoft.schema.mule.tcp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for byteOrMessageProtocolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="byteOrMessageProtocolType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/tcp}abstractProtocolType"&gt;
 *       &lt;attribute name="payloadOnly" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "byteOrMessageProtocolType")
@XmlSeeAlso({
    LengthProtocolType.class
})
public class ByteOrMessageProtocolType
    extends AbstractProtocolType
{

    @XmlAttribute(name = "payloadOnly", required = true)
    protected String payloadOnly;

    /**
     * Gets the value of the payloadOnly property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayloadOnly() {
        return payloadOnly;
    }

    /**
     * Sets the value of the payloadOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayloadOnly(String value) {
        this.payloadOnly = value;
    }

}
