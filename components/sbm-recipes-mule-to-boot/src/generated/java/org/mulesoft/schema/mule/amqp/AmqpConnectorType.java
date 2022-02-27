
package org.mulesoft.schema.mule.amqp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.ConnectorType;


/**
 * <p>Java class for amqpConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="amqpConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}connectorType"&gt;
 *       &lt;attribute name="host" type="{http://www.w3.org/2001/XMLSchema}string" default="localhost" /&gt;
 *       &lt;attribute name="port" type="{http://www.mulesoft.org/schema/mule/core}substitutablePortNumber" default="5672" /&gt;
 *       &lt;attribute name="fallbackAddresses" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="virtualHost" type="{http://www.w3.org/2001/XMLSchema}string" default="/" /&gt;
 *       &lt;attribute name="username" type="{http://www.w3.org/2001/XMLSchema}string" default="guest" /&gt;
 *       &lt;attribute name="password" type="{http://www.w3.org/2001/XMLSchema}string" default="guest" /&gt;
 *       &lt;attribute name="deliveryMode" default="PERSISTENT"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="PERSISTENT"/&gt;
 *             &lt;enumeration value="NON_PERSISTENT"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="priority" default="0"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}byte"&gt;
 *             &lt;minInclusive value="0"/&gt;
 *             &lt;maxInclusive value="9"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="mandatory" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="immediate" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="default-return-endpoint-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ackMode" default="AMQP_AUTO"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="AMQP_AUTO"/&gt;
 *             &lt;enumeration value="MULE_AUTO"/&gt;
 *             &lt;enumeration value="MANUAL"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="prefetchSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="0" /&gt;
 *       &lt;attribute name="prefetchCount" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="0" /&gt;
 *       &lt;attribute name="noLocal" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="exclusiveConsumers" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="activeDeclarationsOnly" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "amqpConnectorType")
public class AmqpConnectorType
    extends ConnectorType
{

    @XmlAttribute(name = "host")
    protected String host;
    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "fallbackAddresses")
    protected String fallbackAddresses;
    @XmlAttribute(name = "virtualHost")
    protected String virtualHost;
    @XmlAttribute(name = "username")
    protected String username;
    @XmlAttribute(name = "password")
    protected String password;
    @XmlAttribute(name = "deliveryMode")
    protected String deliveryMode;
    @XmlAttribute(name = "priority")
    protected Byte priority;
    @XmlAttribute(name = "mandatory")
    protected String mandatory;
    @XmlAttribute(name = "immediate")
    protected String immediate;
    @XmlAttribute(name = "default-return-endpoint-ref")
    protected String defaultReturnEndpointRef;
    @XmlAttribute(name = "ackMode")
    protected String ackMode;
    @XmlAttribute(name = "prefetchSize")
    protected String prefetchSize;
    @XmlAttribute(name = "prefetchCount")
    protected String prefetchCount;
    @XmlAttribute(name = "noLocal")
    protected String noLocal;
    @XmlAttribute(name = "exclusiveConsumers")
    protected String exclusiveConsumers;
    @XmlAttribute(name = "activeDeclarationsOnly")
    protected String activeDeclarationsOnly;

    /**
     * Gets the value of the host property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        if (host == null) {
            return "localhost";
        } else {
            return host;
        }
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
        if (port == null) {
            return "5672";
        } else {
            return port;
        }
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
     * Gets the value of the fallbackAddresses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFallbackAddresses() {
        return fallbackAddresses;
    }

    /**
     * Sets the value of the fallbackAddresses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFallbackAddresses(String value) {
        this.fallbackAddresses = value;
    }

    /**
     * Gets the value of the virtualHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualHost() {
        if (virtualHost == null) {
            return "/";
        } else {
            return virtualHost;
        }
    }

    /**
     * Sets the value of the virtualHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualHost(String value) {
        this.virtualHost = value;
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
        if (username == null) {
            return "guest";
        } else {
            return username;
        }
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
        if (password == null) {
            return "guest";
        } else {
            return password;
        }
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
     * Gets the value of the deliveryMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryMode() {
        if (deliveryMode == null) {
            return "PERSISTENT";
        } else {
            return deliveryMode;
        }
    }

    /**
     * Sets the value of the deliveryMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryMode(String value) {
        this.deliveryMode = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public byte getPriority() {
        if (priority == null) {
            return ((byte) 0);
        } else {
            return priority;
        }
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setPriority(Byte value) {
        this.priority = value;
    }

    /**
     * Gets the value of the mandatory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMandatory() {
        if (mandatory == null) {
            return "false";
        } else {
            return mandatory;
        }
    }

    /**
     * Sets the value of the mandatory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMandatory(String value) {
        this.mandatory = value;
    }

    /**
     * Gets the value of the immediate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImmediate() {
        if (immediate == null) {
            return "false";
        } else {
            return immediate;
        }
    }

    /**
     * Sets the value of the immediate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImmediate(String value) {
        this.immediate = value;
    }

    /**
     * Gets the value of the defaultReturnEndpointRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultReturnEndpointRef() {
        return defaultReturnEndpointRef;
    }

    /**
     * Sets the value of the defaultReturnEndpointRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultReturnEndpointRef(String value) {
        this.defaultReturnEndpointRef = value;
    }

    /**
     * Gets the value of the ackMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAckMode() {
        if (ackMode == null) {
            return "AMQP_AUTO";
        } else {
            return ackMode;
        }
    }

    /**
     * Sets the value of the ackMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAckMode(String value) {
        this.ackMode = value;
    }

    /**
     * Gets the value of the prefetchSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrefetchSize() {
        if (prefetchSize == null) {
            return "0";
        } else {
            return prefetchSize;
        }
    }

    /**
     * Sets the value of the prefetchSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrefetchSize(String value) {
        this.prefetchSize = value;
    }

    /**
     * Gets the value of the prefetchCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrefetchCount() {
        if (prefetchCount == null) {
            return "0";
        } else {
            return prefetchCount;
        }
    }

    /**
     * Sets the value of the prefetchCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrefetchCount(String value) {
        this.prefetchCount = value;
    }

    /**
     * Gets the value of the noLocal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoLocal() {
        if (noLocal == null) {
            return "false";
        } else {
            return noLocal;
        }
    }

    /**
     * Sets the value of the noLocal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoLocal(String value) {
        this.noLocal = value;
    }

    /**
     * Gets the value of the exclusiveConsumers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExclusiveConsumers() {
        if (exclusiveConsumers == null) {
            return "false";
        } else {
            return exclusiveConsumers;
        }
    }

    /**
     * Sets the value of the exclusiveConsumers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExclusiveConsumers(String value) {
        this.exclusiveConsumers = value;
    }

    /**
     * Gets the value of the activeDeclarationsOnly property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActiveDeclarationsOnly() {
        if (activeDeclarationsOnly == null) {
            return "false";
        } else {
            return activeDeclarationsOnly;
        }
    }

    /**
     * Sets the value of the activeDeclarationsOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActiveDeclarationsOnly(String value) {
        this.activeDeclarationsOnly = value;
    }

}
