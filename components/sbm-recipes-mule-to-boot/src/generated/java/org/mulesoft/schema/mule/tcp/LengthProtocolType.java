
package org.mulesoft.schema.mule.tcp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for lengthProtocolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lengthProtocolType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/tcp}byteOrMessageProtocolType"&gt;
 *       &lt;attribute name="maxMessageLength" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lengthProtocolType")
@XmlSeeAlso({
    CustomClassLoadingProtocolType.class
})
public class LengthProtocolType
    extends ByteOrMessageProtocolType
{

    @XmlAttribute(name = "maxMessageLength")
    protected String maxMessageLength;

    /**
     * Gets the value of the maxMessageLength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxMessageLength() {
        return maxMessageLength;
    }

    /**
     * Sets the value of the maxMessageLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxMessageLength(String value) {
        this.maxMessageLength = value;
    }

}
