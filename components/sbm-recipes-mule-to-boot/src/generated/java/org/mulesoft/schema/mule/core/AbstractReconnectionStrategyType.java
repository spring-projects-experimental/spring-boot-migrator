
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractReconnectionStrategyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractReconnectionStrategyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-reconnect-notifier" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="blocking" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractReconnectionStrategyType", propOrder = {
    "abstractReconnectNotifier"
})
@XmlSeeAlso({
    ReconnectSimpleStrategyType.class,
    ReconnectForeverStrategyType.class,
    ReconnectCustomStrategyType.class
})
public class AbstractReconnectionStrategyType {

    @XmlElementRef(name = "abstract-reconnect-notifier", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractReconnectNotifierType> abstractReconnectNotifier;
    @XmlAttribute(name = "blocking")
    protected String blocking;

    /**
     * Gets the value of the abstractReconnectNotifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ReconnectCustomNotifierType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectNotifierType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractReconnectNotifierType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractReconnectNotifierType> getAbstractReconnectNotifier() {
        return abstractReconnectNotifier;
    }

    /**
     * Sets the value of the abstractReconnectNotifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ReconnectCustomNotifierType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectNotifierType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractReconnectNotifierType }{@code >}
     *     
     */
    public void setAbstractReconnectNotifier(JAXBElement<? extends AbstractReconnectNotifierType> value) {
        this.abstractReconnectNotifier = value;
    }

    /**
     * Gets the value of the blocking property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlocking() {
        if (blocking == null) {
            return "true";
        } else {
            return blocking;
        }
    }

    /**
     * Sets the value of the blocking property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlocking(String value) {
        this.blocking = value;
    }

}
