
package org.mulesoft.schema.mule.jms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractExtensionType;


/**
 * <p>Java class for connectionFactoryPoolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="connectionFactoryPoolType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractExtensionType"&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;attribute name="sessionCacheSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="1" /&gt;
 *       &lt;attribute name="cacheProducers" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;attribute name="connectionFactory-ref" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;attribute name="username" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="password" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "connectionFactoryPoolType")
public class ConnectionFactoryPoolType
    extends AbstractExtensionType
{

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "sessionCacheSize")
    protected String sessionCacheSize;
    @XmlAttribute(name = "cacheProducers")
    protected String cacheProducers;
    @XmlAttribute(name = "connectionFactory-ref", required = true)
    protected String connectionFactoryRef;
    @XmlAttribute(name = "username")
    protected String username;
    @XmlAttribute(name = "password")
    protected String password;

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
     * Gets the value of the sessionCacheSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionCacheSize() {
        if (sessionCacheSize == null) {
            return "1";
        } else {
            return sessionCacheSize;
        }
    }

    /**
     * Sets the value of the sessionCacheSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionCacheSize(String value) {
        this.sessionCacheSize = value;
    }

    /**
     * Gets the value of the cacheProducers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCacheProducers() {
        if (cacheProducers == null) {
            return "true";
        } else {
            return cacheProducers;
        }
    }

    /**
     * Sets the value of the cacheProducers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCacheProducers(String value) {
        this.cacheProducers = value;
    }

    /**
     * Gets the value of the connectionFactoryRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectionFactoryRef() {
        return connectionFactoryRef;
    }

    /**
     * Sets the value of the connectionFactoryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectionFactoryRef(String value) {
        this.connectionFactoryRef = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
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

}
