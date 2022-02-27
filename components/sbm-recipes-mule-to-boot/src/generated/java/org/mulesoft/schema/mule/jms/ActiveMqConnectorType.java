
package org.mulesoft.schema.mule.jms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for activeMqConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="activeMqConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/jms}vendorJmsConnectorType"&gt;
 *       &lt;attribute name="brokerURL" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "activeMqConnectorType")
public class ActiveMqConnectorType
    extends VendorJmsConnectorType
{

    @XmlAttribute(name = "brokerURL")
    protected String brokerURL;

    /**
     * Gets the value of the brokerURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrokerURL() {
        return brokerURL;
    }

    /**
     * Sets the value of the brokerURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrokerURL(String value) {
        this.brokerURL = value;
    }

}
