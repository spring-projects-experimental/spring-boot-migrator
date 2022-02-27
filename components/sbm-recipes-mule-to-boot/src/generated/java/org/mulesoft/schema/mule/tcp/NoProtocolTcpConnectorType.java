
package org.mulesoft.schema.mule.tcp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.ConnectorType;
import org.mulesoft.schema.mule.http.HttpConnectorType;


/**
 * <p>Java class for noProtocolTcpConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="noProtocolTcpConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}connectorType"&gt;
 *       &lt;attribute name="sendBufferSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="receiveBufferSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="receiveBacklog" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="sendTcpNoDelay" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="reuseAddress" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="connectionTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="clientSoTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="serverSoTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="socketSoLinger" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="keepSendSocketOpen" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="keepAlive" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="socketMaxWait" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="failOnUnresolvedHost" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "noProtocolTcpConnectorType")
@XmlSeeAlso({
    HttpConnectorType.class,
    TcpConnectorType.class
})
public class NoProtocolTcpConnectorType
    extends ConnectorType
{

    @XmlAttribute(name = "sendBufferSize")
    protected String sendBufferSize;
    @XmlAttribute(name = "receiveBufferSize")
    protected String receiveBufferSize;
    @XmlAttribute(name = "receiveBacklog")
    protected String receiveBacklog;
    @XmlAttribute(name = "sendTcpNoDelay")
    protected String sendTcpNoDelay;
    @XmlAttribute(name = "reuseAddress")
    protected String reuseAddress;
    @XmlAttribute(name = "connectionTimeout")
    protected String connectionTimeout;
    @XmlAttribute(name = "clientSoTimeout")
    protected String clientSoTimeout;
    @XmlAttribute(name = "serverSoTimeout")
    protected String serverSoTimeout;
    @XmlAttribute(name = "socketSoLinger")
    protected String socketSoLinger;
    @XmlAttribute(name = "keepSendSocketOpen")
    protected String keepSendSocketOpen;
    @XmlAttribute(name = "keepAlive")
    protected String keepAlive;
    @XmlAttribute(name = "socketMaxWait")
    protected String socketMaxWait;
    @XmlAttribute(name = "failOnUnresolvedHost")
    protected String failOnUnresolvedHost;

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
     * Gets the value of the sendTcpNoDelay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendTcpNoDelay() {
        return sendTcpNoDelay;
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
     * Gets the value of the reuseAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReuseAddress() {
        return reuseAddress;
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
     * Gets the value of the connectionTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets the value of the connectionTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectionTimeout(String value) {
        this.connectionTimeout = value;
    }

    /**
     * Gets the value of the clientSoTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientSoTimeout() {
        return clientSoTimeout;
    }

    /**
     * Sets the value of the clientSoTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientSoTimeout(String value) {
        this.clientSoTimeout = value;
    }

    /**
     * Gets the value of the serverSoTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerSoTimeout() {
        return serverSoTimeout;
    }

    /**
     * Sets the value of the serverSoTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerSoTimeout(String value) {
        this.serverSoTimeout = value;
    }

    /**
     * Gets the value of the socketSoLinger property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSocketSoLinger() {
        return socketSoLinger;
    }

    /**
     * Sets the value of the socketSoLinger property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSocketSoLinger(String value) {
        this.socketSoLinger = value;
    }

    /**
     * Gets the value of the keepSendSocketOpen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeepSendSocketOpen() {
        return keepSendSocketOpen;
    }

    /**
     * Sets the value of the keepSendSocketOpen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeepSendSocketOpen(String value) {
        this.keepSendSocketOpen = value;
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

    /**
     * Gets the value of the socketMaxWait property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSocketMaxWait() {
        return socketMaxWait;
    }

    /**
     * Sets the value of the socketMaxWait property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSocketMaxWait(String value) {
        this.socketMaxWait = value;
    }

    /**
     * Gets the value of the failOnUnresolvedHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailOnUnresolvedHost() {
        return failOnUnresolvedHost;
    }

    /**
     * Sets the value of the failOnUnresolvedHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailOnUnresolvedHost(String value) {
        this.failOnUnresolvedHost = value;
    }

}
