
package org.mulesoft.schema.mule.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for globalEndpointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="globalEndpointType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}globalEndpointType"&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/http}propertyAttributes"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/http}addressAttributes"/&gt;
 *       &lt;attribute name="followRedirects" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="exceptionOnMessageError" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "globalEndpointType")
public class GlobalEndpointType
    extends org.mulesoft.schema.mule.core.GlobalEndpointType
{

    @XmlAttribute(name = "followRedirects")
    protected Boolean followRedirects;
    @XmlAttribute(name = "exceptionOnMessageError")
    protected Boolean exceptionOnMessageError;
    @XmlAttribute(name = "contentType")
    protected String contentType;
    @XmlAttribute(name = "method")
    protected HttpMethodTypes method;
    @XmlAttribute(name = "keepAlive")
    protected String keepAlive;
    @XmlAttribute(name = "user")
    protected String user;
    @XmlAttribute(name = "password")
    protected String password;
    @XmlAttribute(name = "host")
    protected String host;
    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "path")
    protected String path;

    /**
     * Gets the value of the followRedirects property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFollowRedirects() {
        return followRedirects;
    }

    /**
     * Sets the value of the followRedirects property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFollowRedirects(Boolean value) {
        this.followRedirects = value;
    }

    /**
     * Gets the value of the exceptionOnMessageError property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isExceptionOnMessageError() {
        if (exceptionOnMessageError == null) {
            return true;
        } else {
            return exceptionOnMessageError;
        }
    }

    /**
     * Sets the value of the exceptionOnMessageError property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExceptionOnMessageError(Boolean value) {
        this.exceptionOnMessageError = value;
    }

    /**
     * Gets the value of the contentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the value of the contentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentType(String value) {
        this.contentType = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link HttpMethodTypes }
     *     
     */
    public HttpMethodTypes getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link HttpMethodTypes }
     *     
     */
    public void setMethod(HttpMethodTypes value) {
        this.method = value;
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
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
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
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

}
