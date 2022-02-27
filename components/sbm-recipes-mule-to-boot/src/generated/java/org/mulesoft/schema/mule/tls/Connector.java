
package org.mulesoft.schema.mule.tls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.TlsClientKeyStoreType;
import org.mulesoft.schema.mule.core.TlsKeyStoreType;
import org.mulesoft.schema.mule.core.TlsProtocolHandler;
import org.mulesoft.schema.mule.core.TlsServerTrustStoreType;
import org.mulesoft.schema.mule.tcp.TcpConnectorType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/tcp}tcpConnectorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="client" type="{http://www.mulesoft.org/schema/mule/core}tlsClientKeyStoreType" minOccurs="0"/&gt;
 *         &lt;element name="key-store" type="{http://www.mulesoft.org/schema/mule/core}tlsKeyStoreType" minOccurs="0"/&gt;
 *         &lt;element name="server" type="{http://www.mulesoft.org/schema/mule/core}tlsServerTrustStoreType" minOccurs="0"/&gt;
 *         &lt;element name="protocol-handler" type="{http://www.mulesoft.org/schema/mule/core}tlsProtocolHandler" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "client",
    "keyStore",
    "server",
    "protocolHandler"
})
public class Connector
    extends TcpConnectorType
{

    protected TlsClientKeyStoreType client;
    @XmlElement(name = "key-store")
    protected TlsKeyStoreType keyStore;
    protected TlsServerTrustStoreType server;
    @XmlElement(name = "protocol-handler")
    protected TlsProtocolHandler protocolHandler;

    /**
     * Gets the value of the client property.
     * 
     * @return
     *     possible object is
     *     {@link TlsClientKeyStoreType }
     *     
     */
    public TlsClientKeyStoreType getClient() {
        return client;
    }

    /**
     * Sets the value of the client property.
     * 
     * @param value
     *     allowed object is
     *     {@link TlsClientKeyStoreType }
     *     
     */
    public void setClient(TlsClientKeyStoreType value) {
        this.client = value;
    }

    /**
     * Gets the value of the keyStore property.
     * 
     * @return
     *     possible object is
     *     {@link TlsKeyStoreType }
     *     
     */
    public TlsKeyStoreType getKeyStore() {
        return keyStore;
    }

    /**
     * Sets the value of the keyStore property.
     * 
     * @param value
     *     allowed object is
     *     {@link TlsKeyStoreType }
     *     
     */
    public void setKeyStore(TlsKeyStoreType value) {
        this.keyStore = value;
    }

    /**
     * Gets the value of the server property.
     * 
     * @return
     *     possible object is
     *     {@link TlsServerTrustStoreType }
     *     
     */
    public TlsServerTrustStoreType getServer() {
        return server;
    }

    /**
     * Sets the value of the server property.
     * 
     * @param value
     *     allowed object is
     *     {@link TlsServerTrustStoreType }
     *     
     */
    public void setServer(TlsServerTrustStoreType value) {
        this.server = value;
    }

    /**
     * Gets the value of the protocolHandler property.
     * 
     * @return
     *     possible object is
     *     {@link TlsProtocolHandler }
     *     
     */
    public TlsProtocolHandler getProtocolHandler() {
        return protocolHandler;
    }

    /**
     * Sets the value of the protocolHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link TlsProtocolHandler }
     *     
     */
    public void setProtocolHandler(TlsProtocolHandler value) {
        this.protocolHandler = value;
    }

}
