
package org.mulesoft.schema.mule.tcp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractProtocolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractProtocolType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="rethrowExceptionOnRead" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractProtocolType")
@XmlSeeAlso({
    ByteOrMessageProtocolType.class,
    CustomProtocolType.class
})
public class AbstractProtocolType {

    @XmlAttribute(name = "rethrowExceptionOnRead")
    protected String rethrowExceptionOnRead;

    /**
     * Gets the value of the rethrowExceptionOnRead property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRethrowExceptionOnRead() {
        return rethrowExceptionOnRead;
    }

    /**
     * Sets the value of the rethrowExceptionOnRead property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRethrowExceptionOnRead(String value) {
        this.rethrowExceptionOnRead = value;
    }

}
