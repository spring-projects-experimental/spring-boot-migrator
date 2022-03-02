
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.amqp.AmqpConnectorType;
import org.mulesoft.schema.mule.tcp.NoProtocolTcpConnectorType;
import org.springframework.schema.beans.PropertyType;


/**
 * <p>Java class for connectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="connectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractConnectorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.springframework.org/schema/beans}property" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="receiver-threading-profile" type="{http://www.mulesoft.org/schema/mule/core}threadingProfileType" minOccurs="0"/&gt;
 *         &lt;element name="dispatcher-threading-profile" type="{http://www.mulesoft.org/schema/mule/core}threadingProfileType" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-reconnection-strategy" minOccurs="0"/&gt;
 *         &lt;element name="service-overrides" type="{http://www.mulesoft.org/schema/mule/core}serviceOverridesType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="dynamicNotification" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="validateConnections" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;attribute name="dispatcherPoolFactory-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "connectorType", propOrder = {
    "property",
    "receiverThreadingProfile",
    "dispatcherThreadingProfile",
    "abstractReconnectionStrategy",
    "serviceOverrides"
})
@XmlSeeAlso({
    JndiConnectorType.class,
    AmqpConnectorType.class,
    NoProtocolTcpConnectorType.class,
    CustomConnectorType.class,
    TransactedConnectorType.class
})
public class ConnectorType
    extends AbstractConnectorType
{

    @XmlElement(namespace = "http://www.springframework.org/schema/beans")
    protected List<PropertyType> property;
    @XmlElement(name = "receiver-threading-profile")
    protected ThreadingProfileType receiverThreadingProfile;
    @XmlElement(name = "dispatcher-threading-profile")
    protected ThreadingProfileType dispatcherThreadingProfile;
    @XmlElementRef(name = "abstract-reconnection-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractReconnectionStrategyType> abstractReconnectionStrategy;
    @XmlElement(name = "service-overrides")
    protected ServiceOverridesType serviceOverrides;
    @XmlAttribute(name = "dynamicNotification")
    protected String dynamicNotification;
    @XmlAttribute(name = "validateConnections")
    protected String validateConnections;
    @XmlAttribute(name = "dispatcherPoolFactory-ref")
    protected String dispatcherPoolFactoryRef;

    /**
     * Gets the value of the property property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the property property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyType }
     * 
     * 
     */
    public List<PropertyType> getProperty() {
        if (property == null) {
            property = new ArrayList<PropertyType>();
        }
        return this.property;
    }

    /**
     * Gets the value of the receiverThreadingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link ThreadingProfileType }
     *     
     */
    public ThreadingProfileType getReceiverThreadingProfile() {
        return receiverThreadingProfile;
    }

    /**
     * Sets the value of the receiverThreadingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThreadingProfileType }
     *     
     */
    public void setReceiverThreadingProfile(ThreadingProfileType value) {
        this.receiverThreadingProfile = value;
    }

    /**
     * Gets the value of the dispatcherThreadingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link ThreadingProfileType }
     *     
     */
    public ThreadingProfileType getDispatcherThreadingProfile() {
        return dispatcherThreadingProfile;
    }

    /**
     * Sets the value of the dispatcherThreadingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThreadingProfileType }
     *     
     */
    public void setDispatcherThreadingProfile(ThreadingProfileType value) {
        this.dispatcherThreadingProfile = value;
    }

    /**
     * 
     *                                 Reconnection strategy that defines how Mule should handle a connection failure.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ReconnectSimpleStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectForeverStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectCustomStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractReconnectionStrategyType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractReconnectionStrategyType> getAbstractReconnectionStrategy() {
        return abstractReconnectionStrategy;
    }

    /**
     * Sets the value of the abstractReconnectionStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ReconnectSimpleStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectForeverStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectCustomStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractReconnectionStrategyType }{@code >}
     *     
     */
    public void setAbstractReconnectionStrategy(JAXBElement<? extends AbstractReconnectionStrategyType> value) {
        this.abstractReconnectionStrategy = value;
    }

    /**
     * Gets the value of the serviceOverrides property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceOverridesType }
     *     
     */
    public ServiceOverridesType getServiceOverrides() {
        return serviceOverrides;
    }

    /**
     * Sets the value of the serviceOverrides property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceOverridesType }
     *     
     */
    public void setServiceOverrides(ServiceOverridesType value) {
        this.serviceOverrides = value;
    }

    /**
     * Gets the value of the dynamicNotification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicNotification() {
        if (dynamicNotification == null) {
            return "false";
        } else {
            return dynamicNotification;
        }
    }

    /**
     * Sets the value of the dynamicNotification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicNotification(String value) {
        this.dynamicNotification = value;
    }

    /**
     * Gets the value of the validateConnections property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidateConnections() {
        if (validateConnections == null) {
            return "true";
        } else {
            return validateConnections;
        }
    }

    /**
     * Sets the value of the validateConnections property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidateConnections(String value) {
        this.validateConnections = value;
    }

    /**
     * Gets the value of the dispatcherPoolFactoryRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDispatcherPoolFactoryRef() {
        return dispatcherPoolFactoryRef;
    }

    /**
     * Sets the value of the dispatcherPoolFactoryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDispatcherPoolFactoryRef(String value) {
        this.dispatcherPoolFactoryRef = value;
    }

}
