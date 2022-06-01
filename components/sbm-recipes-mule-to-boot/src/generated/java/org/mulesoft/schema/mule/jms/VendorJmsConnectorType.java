
package org.mulesoft.schema.mule.jms;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.core.TransactedConnectorType;
import org.mulesoft.schema.mule.ee.wmq.WmqConnectorType;


/**
 * <p>Java class for vendorJmsConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vendorJmsConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}transactedConnectorType"&gt;
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/jms}abstract-jndi-name-resolver" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/jms}jmsConnectorAttributes"/&gt;
 *       &lt;attribute name="connectionFactory-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vendorJmsConnectorType", propOrder = {
    "abstractJndiNameResolver"
})
@XmlSeeAlso({
    WmqConnectorType.class,
    ActiveMqConnectorType.class,
    MuleMqConnectorType.class
})
public class VendorJmsConnectorType
    extends TransactedConnectorType
{

    @XmlElementRef(name = "abstract-jndi-name-resolver", namespace = "http://www.mulesoft.org/schema/mule/jms", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends AbstractJndiNameResolverType>> abstractJndiNameResolver;
    @XmlAttribute(name = "connectionFactory-ref")
    protected String connectionFactoryRef;
    @XmlAttribute(name = "redeliveryHandlerFactory-ref")
    protected String redeliveryHandlerFactoryRef;
    @XmlAttribute(name = "acknowledgementMode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String acknowledgementMode;
    @XmlAttribute(name = "clientId")
    protected String clientId;
    @XmlAttribute(name = "durable")
    protected String durable;
    @XmlAttribute(name = "noLocal")
    protected String noLocal;
    @XmlAttribute(name = "persistentDelivery")
    protected String persistentDelivery;
    @XmlAttribute(name = "honorQosHeaders")
    protected String honorQosHeaders;
    @XmlAttribute(name = "maxRedelivery")
    protected String maxRedelivery;
    @XmlAttribute(name = "maxQueuePrefetch")
    protected String maxQueuePrefetch;
    @XmlAttribute(name = "cacheJmsSessions")
    protected String cacheJmsSessions;
    @XmlAttribute(name = "eagerConsumer")
    protected String eagerConsumer;
    @XmlAttribute(name = "specification")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String specification;
    @XmlAttribute(name = "username")
    protected String username;
    @XmlAttribute(name = "password")
    protected String password;
    @XmlAttribute(name = "numberOfConsumers")
    protected String numberOfConsumers;
    @XmlAttribute(name = "jndiInitialFactory")
    protected String jndiInitialFactory;
    @XmlAttribute(name = "jndiProviderUrl")
    protected String jndiProviderUrl;
    @XmlAttribute(name = "jndiProviderProperties-ref")
    protected String jndiProviderPropertiesRef;
    @XmlAttribute(name = "connectionFactoryJndiName")
    protected String connectionFactoryJndiName;
    @XmlAttribute(name = "jndiDestinations")
    protected Boolean jndiDestinations;
    @XmlAttribute(name = "forceJndiDestinations")
    protected Boolean forceJndiDestinations;
    @XmlAttribute(name = "disableTemporaryReplyToDestinations")
    protected Boolean disableTemporaryReplyToDestinations;
    @XmlAttribute(name = "embeddedMode")
    protected Boolean embeddedMode;

    /**
     * Gets the value of the abstractJndiNameResolver property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractJndiNameResolver property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractJndiNameResolver().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link DefaultJndiNameResolverType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomJndiNameResolverType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractJndiNameResolverType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractJndiNameResolverType>> getAbstractJndiNameResolver() {
        if (abstractJndiNameResolver == null) {
            abstractJndiNameResolver = new ArrayList<JAXBElement<? extends AbstractJndiNameResolverType>>();
        }
        return this.abstractJndiNameResolver;
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
     * Gets the value of the redeliveryHandlerFactoryRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRedeliveryHandlerFactoryRef() {
        return redeliveryHandlerFactoryRef;
    }

    /**
     * Sets the value of the redeliveryHandlerFactoryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRedeliveryHandlerFactoryRef(String value) {
        this.redeliveryHandlerFactoryRef = value;
    }

    /**
     * Gets the value of the acknowledgementMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcknowledgementMode() {
        if (acknowledgementMode == null) {
            return "AUTO_ACKNOWLEDGE";
        } else {
            return acknowledgementMode;
        }
    }

    /**
     * Sets the value of the acknowledgementMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcknowledgementMode(String value) {
        this.acknowledgementMode = value;
    }

    /**
     * Gets the value of the clientId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the value of the clientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientId(String value) {
        this.clientId = value;
    }

    /**
     * Gets the value of the durable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDurable() {
        return durable;
    }

    /**
     * Sets the value of the durable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDurable(String value) {
        this.durable = value;
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
        return noLocal;
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
     * Gets the value of the persistentDelivery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersistentDelivery() {
        return persistentDelivery;
    }

    /**
     * Sets the value of the persistentDelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersistentDelivery(String value) {
        this.persistentDelivery = value;
    }

    /**
     * Gets the value of the honorQosHeaders property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHonorQosHeaders() {
        return honorQosHeaders;
    }

    /**
     * Sets the value of the honorQosHeaders property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHonorQosHeaders(String value) {
        this.honorQosHeaders = value;
    }

    /**
     * Gets the value of the maxRedelivery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxRedelivery() {
        return maxRedelivery;
    }

    /**
     * Sets the value of the maxRedelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxRedelivery(String value) {
        this.maxRedelivery = value;
    }

    /**
     * Gets the value of the maxQueuePrefetch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxQueuePrefetch() {
        if (maxQueuePrefetch == null) {
            return "-1";
        } else {
            return maxQueuePrefetch;
        }
    }

    /**
     * Sets the value of the maxQueuePrefetch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxQueuePrefetch(String value) {
        this.maxQueuePrefetch = value;
    }

    /**
     * Gets the value of the cacheJmsSessions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCacheJmsSessions() {
        if (cacheJmsSessions == null) {
            return "true";
        } else {
            return cacheJmsSessions;
        }
    }

    /**
     * Sets the value of the cacheJmsSessions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCacheJmsSessions(String value) {
        this.cacheJmsSessions = value;
    }

    /**
     * Gets the value of the eagerConsumer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEagerConsumer() {
        return eagerConsumer;
    }

    /**
     * Sets the value of the eagerConsumer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEagerConsumer(String value) {
        this.eagerConsumer = value;
    }

    /**
     * Gets the value of the specification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecification() {
        if (specification == null) {
            return "1.0.2b";
        } else {
            return specification;
        }
    }

    /**
     * Sets the value of the specification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecification(String value) {
        this.specification = value;
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

    /**
     * Gets the value of the numberOfConsumers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfConsumers() {
        return numberOfConsumers;
    }

    /**
     * Sets the value of the numberOfConsumers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfConsumers(String value) {
        this.numberOfConsumers = value;
    }

    /**
     * Gets the value of the jndiInitialFactory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJndiInitialFactory() {
        return jndiInitialFactory;
    }

    /**
     * Sets the value of the jndiInitialFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJndiInitialFactory(String value) {
        this.jndiInitialFactory = value;
    }

    /**
     * Gets the value of the jndiProviderUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJndiProviderUrl() {
        return jndiProviderUrl;
    }

    /**
     * Sets the value of the jndiProviderUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJndiProviderUrl(String value) {
        this.jndiProviderUrl = value;
    }

    /**
     * Gets the value of the jndiProviderPropertiesRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJndiProviderPropertiesRef() {
        return jndiProviderPropertiesRef;
    }

    /**
     * Sets the value of the jndiProviderPropertiesRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJndiProviderPropertiesRef(String value) {
        this.jndiProviderPropertiesRef = value;
    }

    /**
     * Gets the value of the connectionFactoryJndiName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectionFactoryJndiName() {
        return connectionFactoryJndiName;
    }

    /**
     * Sets the value of the connectionFactoryJndiName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectionFactoryJndiName(String value) {
        this.connectionFactoryJndiName = value;
    }

    /**
     * Gets the value of the jndiDestinations property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJndiDestinations() {
        return jndiDestinations;
    }

    /**
     * Sets the value of the jndiDestinations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJndiDestinations(Boolean value) {
        this.jndiDestinations = value;
    }

    /**
     * Gets the value of the forceJndiDestinations property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isForceJndiDestinations() {
        return forceJndiDestinations;
    }

    /**
     * Sets the value of the forceJndiDestinations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setForceJndiDestinations(Boolean value) {
        this.forceJndiDestinations = value;
    }

    /**
     * Gets the value of the disableTemporaryReplyToDestinations property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDisableTemporaryReplyToDestinations() {
        return disableTemporaryReplyToDestinations;
    }

    /**
     * Sets the value of the disableTemporaryReplyToDestinations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDisableTemporaryReplyToDestinations(Boolean value) {
        this.disableTemporaryReplyToDestinations = value;
    }

    /**
     * Gets the value of the embeddedMode property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isEmbeddedMode() {
        if (embeddedMode == null) {
            return false;
        } else {
            return embeddedMode;
        }
    }

    /**
     * Sets the value of the embeddedMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEmbeddedMode(Boolean value) {
        this.embeddedMode = value;
    }

}
