
package org.mulesoft.schema.mule.tcp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractExtensionType;


/**
 * <p>Java class for tcpAbstractSocketPropertiesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcpAbstractSocketPropertiesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractExtensionType"&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sendBufferSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="receiveBufferSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="sendTcpNoDelay" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;attribute name="timeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="0" /&gt;
 *       &lt;attribute name="linger" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="-1" /&gt;
 *       &lt;attribute name="keepAlive" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcpAbstractSocketPropertiesType")
@XmlSeeAlso({
    TcpClientSocketPropertiesType.class,
    TcpServerSocketPropertiesType.class
})
public class TcpAbstractSocketPropertiesType
    extends AbstractExtensionType
{

    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "sendBufferSize")
    protected String sendBufferSize;
    @XmlAttribute(name = "receiveBufferSize")
    protected String receiveBufferSize;
    @XmlAttribute(name = "sendTcpNoDelay")
    protected String sendTcpNoDelay;
    @XmlAttribute(name = "timeout")
    protected String timeout;
    @XmlAttribute(name = "linger")
    protected String linger;
    @XmlAttribute(name = "keepAlive")
    protected String keepAlive;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the sendBufferSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendBufferSize() {
        return sendBufferSize;
    }

    /**
     * Sets the value of the sendBufferSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendBufferSize(String value) {
        this.sendBufferSize = value;
    }

    /**
     * Gets the value of the receiveBufferSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveBufferSize() {
        return receiveBufferSize;
    }

    /**
     * Sets the value of the receiveBufferSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveBufferSize(String value) {
        this.receiveBufferSize = value;
    }

    /**
     * Gets the value of the sendTcpNoDelay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendTcpNoDelay() {
        if (sendTcpNoDelay == null) {
            return "true";
        } else {
            return sendTcpNoDelay;
        }
    }

    /**
     * Sets the value of the sendTcpNoDelay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendTcpNoDelay(String value) {
        this.sendTcpNoDelay = value;
    }

    /**
     * Gets the value of the timeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeout() {
        if (timeout == null) {
            return "0";
        } else {
            return timeout;
        }
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
     * Gets the value of the linger property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinger() {
        if (linger == null) {
            return "-1";
        } else {
            return linger;
        }
    }

    /**
     * Sets the value of the linger property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinger(String value) {
        this.linger = value;
    }

    /**
     * Gets the value of the keepAlive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeepAlive() {
        return keepAlive;
    }

    /**
     * Sets the value of the keepAlive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeepAlive(String value) {
        this.keepAlive = value;
    }

}
