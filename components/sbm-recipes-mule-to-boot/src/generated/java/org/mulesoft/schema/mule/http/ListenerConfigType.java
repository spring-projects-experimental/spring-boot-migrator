
package org.mulesoft.schema.mule.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractExtensionType;
import org.mulesoft.schema.mule.core.AsynchronousThreadingProfileType;
import org.mulesoft.schema.mule.tls.TlsContextType;


/**
 * <p>Java class for listenerConfigType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listenerConfigType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractExtensionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/tls}context" minOccurs="0"/&gt;
 *         &lt;element name="worker-threading-profile" type="{http://www.mulesoft.org/schema/mule/core}asynchronousThreadingProfileType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;attribute name="protocol" type="{http://www.mulesoft.org/schema/mule/http}httpProtocol" default="HTTP" /&gt;
 *       &lt;attribute name="host" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="port" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="basePath" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="tlsContext-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="parseRequest" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="connectionIdleTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="30000" /&gt;
 *       &lt;attribute name="usePersistentConnections" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listenerConfigType", propOrder = {
    "context",
    "workerThreadingProfile"
})
public class ListenerConfigType
    extends AbstractExtensionType
{

    @XmlElement(namespace = "http://www.mulesoft.org/schema/mule/tls")
    protected TlsContextType context;
    @XmlElement(name = "worker-threading-profile")
    protected AsynchronousThreadingProfileType workerThreadingProfile;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "protocol")
    protected String protocol;
    @XmlAttribute(name = "host", required = true)
    protected String host;
    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "basePath")
    protected String basePath;
    @XmlAttribute(name = "tlsContext-ref")
    protected String tlsContextRef;
    @XmlAttribute(name = "parseRequest")
    protected String parseRequest;
    @XmlAttribute(name = "connectionIdleTimeout")
    protected String connectionIdleTimeout;
    @XmlAttribute(name = "usePersistentConnections")
    protected String usePersistentConnections;

    /**
     * Gets the value of the context property.
     * 
     * @return
     *     possible object is
     *     {@link TlsContextType }
     *     
     */
    public TlsContextType getContext() {
        return context;
    }

    /**
     * Sets the value of the context property.
     * 
     * @param value
     *     allowed object is
     *     {@link TlsContextType }
     *     
     */
    public void setContext(TlsContextType value) {
        this.context = value;
    }

    /**
     * Gets the value of the workerThreadingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link AsynchronousThreadingProfileType }
     *     
     */
    public AsynchronousThreadingProfileType getWorkerThreadingProfile() {
        return workerThreadingProfile;
    }

    /**
     * Sets the value of the workerThreadingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link AsynchronousThreadingProfileType }
     *     
     */
    public void setWorkerThreadingProfile(AsynchronousThreadingProfileType value) {
        this.workerThreadingProfile = value;
    }

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
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocol() {
        if (protocol == null) {
            return "HTTP";
        } else {
            return protocol;
        }
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocol(String value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the host property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the value of the host property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost(String value) {
        this.host = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPort(String value) {
        this.port = value;
    }

    /**
     * Gets the value of the basePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * Sets the value of the basePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBasePath(String value) {
        this.basePath = value;
    }

    /**
     * Gets the value of the tlsContextRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTlsContextRef() {
        return tlsContextRef;
    }

    /**
     * Sets the value of the tlsContextRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTlsContextRef(String value) {
        this.tlsContextRef = value;
    }

    /**
     * Gets the value of the parseRequest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParseRequest() {
        return parseRequest;
    }

    /**
     * Sets the value of the parseRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParseRequest(String value) {
        this.parseRequest = value;
    }

    /**
     * Gets the value of the connectionIdleTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectionIdleTimeout() {
        if (connectionIdleTimeout == null) {
            return "30000";
        } else {
            return connectionIdleTimeout;
        }
    }

    /**
     * Sets the value of the connectionIdleTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectionIdleTimeout(String value) {
        this.connectionIdleTimeout = value;
    }

    /**
     * Gets the value of the usePersistentConnections property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsePersistentConnections() {
        if (usePersistentConnections == null) {
            return "true";
        } else {
            return usePersistentConnections;
        }
    }

    /**
     * Sets the value of the usePersistentConnections property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsePersistentConnections(String value) {
        this.usePersistentConnections = value;
    }

}
