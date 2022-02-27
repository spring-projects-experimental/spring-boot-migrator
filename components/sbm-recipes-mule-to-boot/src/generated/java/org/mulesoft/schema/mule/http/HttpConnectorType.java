
package org.mulesoft.schema.mule.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.tcp.NoProtocolTcpConnectorType;


/**
 * <p>Java class for httpConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="httpConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/tcp}noProtocolTcpConnectorType"&gt;
 *       &lt;attribute name="cookieSpec"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="netscape"/&gt;
 *             &lt;enumeration value="rfc2109"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="proxyHostname" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="proxyPassword" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="proxyPort" type="{http://www.mulesoft.org/schema/mule/core}substitutablePortNumber" /&gt;
 *       &lt;attribute name="proxyUsername" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="proxyNtlmAuthentication" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="enableCookies" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "httpConnectorType")
@XmlSeeAlso({
    HttpPollingConnectorType.class
})
public class HttpConnectorType
    extends NoProtocolTcpConnectorType
{

    @XmlAttribute(name = "cookieSpec")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String cookieSpec;
    @XmlAttribute(name = "proxyHostname")
    protected String proxyHostname;
    @XmlAttribute(name = "proxyPassword")
    protected String proxyPassword;
    @XmlAttribute(name = "proxyPort")
    protected String proxyPort;
    @XmlAttribute(name = "proxyUsername")
    protected String proxyUsername;
    @XmlAttribute(name = "proxyNtlmAuthentication")
    protected String proxyNtlmAuthentication;
    @XmlAttribute(name = "enableCookies")
    protected String enableCookies;

    /**
     * Gets the value of the cookieSpec property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCookieSpec() {
        return cookieSpec;
    }

    /**
     * Sets the value of the cookieSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCookieSpec(String value) {
        this.cookieSpec = value;
    }

    /**
     * Gets the value of the proxyHostname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProxyHostname() {
        return proxyHostname;
    }

    /**
     * Sets the value of the proxyHostname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProxyHostname(String value) {
        this.proxyHostname = value;
    }

    /**
     * Gets the value of the proxyPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * Sets the value of the proxyPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProxyPassword(String value) {
        this.proxyPassword = value;
    }

    /**
     * Gets the value of the proxyPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProxyPort() {
        return proxyPort;
    }

    /**
     * Sets the value of the proxyPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProxyPort(String value) {
        this.proxyPort = value;
    }

    /**
     * Gets the value of the proxyUsername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProxyUsername() {
        return proxyUsername;
    }

    /**
     * Sets the value of the proxyUsername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProxyUsername(String value) {
        this.proxyUsername = value;
    }

    /**
     * Gets the value of the proxyNtlmAuthentication property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProxyNtlmAuthentication() {
        return proxyNtlmAuthentication;
    }

    /**
     * Sets the value of the proxyNtlmAuthentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProxyNtlmAuthentication(String value) {
        this.proxyNtlmAuthentication = value;
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
        return enableCookies;
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

}
