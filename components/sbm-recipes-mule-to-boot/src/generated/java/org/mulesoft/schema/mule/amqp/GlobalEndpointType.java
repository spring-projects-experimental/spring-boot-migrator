
package org.mulesoft.schema.mule.amqp;

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
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/amqp}propertyAttributes"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/amqp}addressAttributes"/&gt;
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

    @XmlAttribute(name = "routingKey")
    protected String routingKey;
    @XmlAttribute(name = "consumerTag")
    protected String consumerTag;
    @XmlAttribute(name = "exchangeType")
    protected String exchangeType;
    @XmlAttribute(name = "exchangeDurable")
    protected String exchangeDurable;
    @XmlAttribute(name = "exchangeAutoDelete")
    protected String exchangeAutoDelete;
    @XmlAttribute(name = "queueDurable")
    protected String queueDurable;
    @XmlAttribute(name = "queueAutoDelete")
    protected String queueAutoDelete;
    @XmlAttribute(name = "queueExclusive")
    protected String queueExclusive;
    @XmlAttribute(name = "exchangeName")
    protected String exchangeName;
    @XmlAttribute(name = "queueName")
    protected String queueName;

    /**
     * Gets the value of the routingKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoutingKey() {
        return routingKey;
    }

    /**
     * Sets the value of the routingKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoutingKey(String value) {
        this.routingKey = value;
    }

    /**
     * Gets the value of the consumerTag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerTag() {
        return consumerTag;
    }

    /**
     * Sets the value of the consumerTag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerTag(String value) {
        this.consumerTag = value;
    }

    /**
     * Gets the value of the exchangeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExchangeType() {
        return exchangeType;
    }

    /**
     * Sets the value of the exchangeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExchangeType(String value) {
        this.exchangeType = value;
    }

    /**
     * Gets the value of the exchangeDurable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExchangeDurable() {
        return exchangeDurable;
    }

    /**
     * Sets the value of the exchangeDurable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExchangeDurable(String value) {
        this.exchangeDurable = value;
    }

    /**
     * Gets the value of the exchangeAutoDelete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExchangeAutoDelete() {
        return exchangeAutoDelete;
    }

    /**
     * Sets the value of the exchangeAutoDelete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExchangeAutoDelete(String value) {
        this.exchangeAutoDelete = value;
    }

    /**
     * Gets the value of the queueDurable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueueDurable() {
        return queueDurable;
    }

    /**
     * Sets the value of the queueDurable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueueDurable(String value) {
        this.queueDurable = value;
    }

    /**
     * Gets the value of the queueAutoDelete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueueAutoDelete() {
        return queueAutoDelete;
    }

    /**
     * Sets the value of the queueAutoDelete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueueAutoDelete(String value) {
        this.queueAutoDelete = value;
    }

    /**
     * Gets the value of the queueExclusive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueueExclusive() {
        return queueExclusive;
    }

    /**
     * Sets the value of the queueExclusive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueueExclusive(String value) {
        this.queueExclusive = value;
    }

    /**
     * Gets the value of the exchangeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExchangeName() {
        return exchangeName;
    }

    /**
     * Sets the value of the exchangeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExchangeName(String value) {
        this.exchangeName = value;
    }

    /**
     * Gets the value of the queueName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * Sets the value of the queueName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueueName(String value) {
        this.queueName = value;
    }

}
