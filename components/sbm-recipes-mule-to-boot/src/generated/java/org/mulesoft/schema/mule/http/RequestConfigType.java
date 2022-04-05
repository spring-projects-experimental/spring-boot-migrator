
package org.mulesoft.schema.mule.http;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.core.AbstractExtensionType;
import org.mulesoft.schema.mule.tcp.TcpClientSocketPropertiesType;
import org.mulesoft.schema.mule.tls.TlsContextType;


/**
 * <p>Java class for requestConfigType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="requestConfigType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractExtensionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/http}abstract-http-request-authentication-provider" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/tcp}client-socket-properties" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/tls}context" minOccurs="0"/&gt;
 *         &lt;element name="raml-api-configuration" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="location" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;choice&gt;
 *           &lt;element name="proxy" type="{http://www.mulesoft.org/schema/mule/http}proxyType" minOccurs="0"/&gt;
 *           &lt;element name="ntlm-proxy" type="{http://www.mulesoft.org/schema/mule/http}NtlmProxyType" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/http}commonRequestAttributes"/&gt;
 *       &lt;attribute name="protocol" type="{http://www.mulesoft.org/schema/mule/http}httpProtocol" default="HTTP" /&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;attribute name="basePath" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="tlsContext-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="clientSocketProperties-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="proxy-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="maxConnections" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="-1" /&gt;
 *       &lt;attribute name="streamResponse" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="responseBufferSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="10240" /&gt;
 *       &lt;attribute name="connectionIdleTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="30000" /&gt;
 *       &lt;attribute name="usePersistentConnections" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;attribute name="enableCookies" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestConfigType", propOrder = {
    "abstractHttpRequestAuthenticationProvider",
    "clientSocketProperties",
    "context",
    "ramlApiConfiguration",
    "proxy",
    "ntlmProxy"
})
public class RequestConfigType
    extends AbstractExtensionType
{

    @XmlElementRef(name = "abstract-http-request-authentication-provider", namespace = "http://www.mulesoft.org/schema/mule/http", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractHttpRequestAuthenticationProvider> abstractHttpRequestAuthenticationProvider;
    @XmlElement(name = "client-socket-properties", namespace = "http://www.mulesoft.org/schema/mule/tcp")
    protected TcpClientSocketPropertiesType clientSocketProperties;
    @XmlElement(namespace = "http://www.mulesoft.org/schema/mule/tls")
    protected TlsContextType context;
    @XmlElement(name = "raml-api-configuration")
    protected RequestConfigType.RamlApiConfiguration ramlApiConfiguration;
    protected ProxyType proxy;
    @XmlElement(name = "ntlm-proxy")
    protected NtlmProxyType ntlmProxy;
    @XmlAttribute(name = "protocol")
    protected String protocol;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "basePath")
    protected String basePath;
    @XmlAttribute(name = "tlsContext-ref")
    protected String tlsContextRef;
    @XmlAttribute(name = "clientSocketProperties-ref")
    protected String clientSocketPropertiesRef;
    @XmlAttribute(name = "proxy-ref")
    protected String proxyRef;
    @XmlAttribute(name = "maxConnections")
    protected String maxConnections;
    @XmlAttribute(name = "streamResponse")
    protected String streamResponse;
    @XmlAttribute(name = "responseBufferSize")
    protected String responseBufferSize;
    @XmlAttribute(name = "connectionIdleTimeout")
    protected String connectionIdleTimeout;
    @XmlAttribute(name = "usePersistentConnections")
    protected String usePersistentConnections;
    @XmlAttribute(name = "enableCookies")
    protected String enableCookies;
    @XmlAttribute(name = "followRedirects")
    protected String followRedirects;
    @XmlAttribute(name = "host")
    protected String host;
    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "parseResponse")
    protected Boolean parseResponse;
    @XmlAttribute(name = "requestStreamingMode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String requestStreamingMode;
    @XmlAttribute(name = "sendBodyMode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String sendBodyMode;
    @XmlAttribute(name = "responseTimeout")
    protected String responseTimeout;

    /**
     * Gets the value of the abstractHttpRequestAuthenticationProvider property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DigestAuthenticationType }{@code >}
     *     {@link JAXBElement }{@code <}{@link NtlmAuthenticationType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicAuthenticationType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractHttpRequestAuthenticationProvider }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractHttpRequestAuthenticationProvider> getAbstractHttpRequestAuthenticationProvider() {
        return abstractHttpRequestAuthenticationProvider;
    }

    /**
     * Sets the value of the abstractHttpRequestAuthenticationProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DigestAuthenticationType }{@code >}
     *     {@link JAXBElement }{@code <}{@link NtlmAuthenticationType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicAuthenticationType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractHttpRequestAuthenticationProvider }{@code >}
     *     
     */
    public void setAbstractHttpRequestAuthenticationProvider(JAXBElement<? extends AbstractHttpRequestAuthenticationProvider> value) {
        this.abstractHttpRequestAuthenticationProvider = value;
    }

    /**
     * Gets the value of the clientSocketProperties property.
     * 
     * @return
     *     possible object is
     *     {@link TcpClientSocketPropertiesType }
     *     
     */
    public TcpClientSocketPropertiesType getClientSocketProperties() {
        return clientSocketProperties;
    }

    /**
     * Sets the value of the clientSocketProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link TcpClientSocketPropertiesType }
     *     
     */
    public void setClientSocketProperties(TcpClientSocketPropertiesType value) {
        this.clientSocketProperties = value;
    }

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
     * Gets the value of the ramlApiConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link RequestConfigType.RamlApiConfiguration }
     *     
     */
    public RequestConfigType.RamlApiConfiguration getRamlApiConfiguration() {
        return ramlApiConfiguration;
    }

    /**
     * Sets the value of the ramlApiConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestConfigType.RamlApiConfiguration }
     *     
     */
    public void setRamlApiConfiguration(RequestConfigType.RamlApiConfiguration value) {
        this.ramlApiConfiguration = value;
    }

    /**
     * Gets the value of the proxy property.
     * 
     * @return
     *     possible object is
     *     {@link ProxyType }
     *     
     */
    public ProxyType getProxy() {
        return proxy;
    }

    /**
     * Sets the value of the proxy property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProxyType }
     *     
     */
    public void setProxy(ProxyType value) {
        this.proxy = value;
    }

    /**
     * Gets the value of the ntlmProxy property.
     * 
     * @return
     *     possible object is
     *     {@link NtlmProxyType }
     *     
     */
    public NtlmProxyType getNtlmProxy() {
        return ntlmProxy;
    }

    /**
     * Sets the value of the ntlmProxy property.
     * 
     * @param value
     *     allowed object is
     *     {@link NtlmProxyType }
     *     
     */
    public void setNtlmProxy(NtlmProxyType value) {
        this.ntlmProxy = value;
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
     * Gets the value of the clientSocketPropertiesRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientSocketPropertiesRef() {
        return clientSocketPropertiesRef;
    }

    /**
     * Sets the value of the clientSocketPropertiesRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientSocketPropertiesRef(String value) {
        this.clientSocketPropertiesRef = value;
    }

    /**
     * Gets the value of the proxyRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProxyRef() {
        return proxyRef;
    }

    /**
     * Sets the value of the proxyRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProxyRef(String value) {
        this.proxyRef = value;
    }

    /**
     * Gets the value of the maxConnections property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxConnections() {
        if (maxConnections == null) {
            return "-1";
        } else {
            return maxConnections;
        }
    }

    /**
     * Sets the value of the maxConnections property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxConnections(String value) {
        this.maxConnections = value;
    }

    /**
     * Gets the value of the streamResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreamResponse() {
        if (streamResponse == null) {
            return "false";
        } else {
            return streamResponse;
        }
    }

    /**
     * Sets the value of the streamResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreamResponse(String value) {
        this.streamResponse = value;
    }

    /**
     * Gets the value of the responseBufferSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseBufferSize() {
        if (responseBufferSize == null) {
            return "10240";
        } else {
            return responseBufferSize;
        }
    }

    /**
     * Sets the value of the responseBufferSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseBufferSize(String value) {
        this.responseBufferSize = value;
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

    /**
     * Gets the value of the enableCookies property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableCookies() {
        if (enableCookies == null) {
            return "false";
        } else {
            return enableCookies;
        }
    }

    /**
     * Sets the value of the enableCookies property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableCookies(String value) {
        this.enableCookies = value;
    }

    /**
     * Gets the value of the followRedirects property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFollowRedirects() {
        return followRedirects;
    }

    /**
     * Sets the value of the followRedirects property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFollowRedirects(String value) {
        this.followRedirects = value;
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
     * Gets the value of the parseResponse property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isParseResponse() {
        if (parseResponse == null) {
            return true;
        } else {
            return parseResponse;
        }
    }

    /**
     * Sets the value of the parseResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setParseResponse(Boolean value) {
        this.parseResponse = value;
    }

    /**
     * Gets the value of the requestStreamingMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestStreamingMode() {
        return requestStreamingMode;
    }

    /**
     * Sets the value of the requestStreamingMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestStreamingMode(String value) {
        this.requestStreamingMode = value;
    }

    /**
     * Gets the value of the sendBodyMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendBodyMode() {
        return sendBodyMode;
    }

    /**
     * Sets the value of the sendBodyMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendBodyMode(String value) {
        this.sendBodyMode = value;
    }

    /**
     * Gets the value of the responseTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseTimeout() {
        return responseTimeout;
    }

    /**
     * Sets the value of the responseTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseTimeout(String value) {
        this.responseTimeout = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="location" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class RamlApiConfiguration {

        @XmlAttribute(name = "location", required = true)
        protected String location;

        /**
         * Gets the value of the location property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLocation() {
            return location;
        }

        /**
         * Sets the value of the location property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLocation(String value) {
            this.location = value;
        }

    }

}
