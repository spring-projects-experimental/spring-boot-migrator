
package org.mulesoft.schema.mule.tcp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tcpServerSocketPropertiesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcpServerSocketPropertiesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/tcp}tcpAbstractSocketPropertiesType"&gt;
 *       &lt;attribute name="reuseAddress" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;attribute name="receiveBacklog" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="serverTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="0" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcpServerSocketPropertiesType")
public class TcpServerSocketPropertiesType
    extends TcpAbstractSocketPropertiesType
{

    @XmlAttribute(name = "reuseAddress")
    protected String reuseAddress;
    @XmlAttribute(name = "receiveBacklog")
    protected String receiveBacklog;
    @XmlAttribute(name = "serverTimeout")
    protected String serverTimeout;

    /**
     * Gets the value of the reuseAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReuseAddress() {
        if (reuseAddress == null) {
            return "true";
        } else {
            return reuseAddress;
        }
    }

    /**
     * Sets the value of the reuseAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReuseAddress(String value) {
        this.reuseAddress = value;
    }

    /**
     * Gets the value of the receiveBacklog property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveBacklog() {
        return receiveBacklog;
    }

    /**
     * Sets the value of the receiveBacklog property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveBacklog(String value) {
        this.receiveBacklog = value;
    }

    /**
     * Gets the value of the serverTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerTimeout() {
        if (serverTimeout == null) {
            return "0";
        } else {
            return serverTimeout;
        }
    }

    /**
     * Sets the value of the serverTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerTimeout(String value) {
        this.serverTimeout = value;
    }

}
