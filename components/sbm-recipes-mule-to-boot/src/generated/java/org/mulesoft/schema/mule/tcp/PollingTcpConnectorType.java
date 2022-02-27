
package org.mulesoft.schema.mule.tcp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pollingTcpConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pollingTcpConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/tcp}tcpConnectorType"&gt;
 *       &lt;attribute name="timeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableLong" /&gt;
 *       &lt;attribute name="pollingFrequency" type="{http://www.mulesoft.org/schema/mule/core}substitutableLong" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pollingTcpConnectorType")
public class PollingTcpConnectorType
    extends TcpConnectorType
{

    @XmlAttribute(name = "timeout")
    protected String timeout;
    @XmlAttribute(name = "pollingFrequency")
    protected String pollingFrequency;

    /**
     * Gets the value of the timeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * Sets the value of the timeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeout(String value) {
        this.timeout = value;
    }

    /**
     * Gets the value of the pollingFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPollingFrequency() {
        return pollingFrequency;
    }

    /**
     * Sets the value of the pollingFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPollingFrequency(String value) {
        this.pollingFrequency = value;
    }

}
