
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for defineNotificationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="defineNotificationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="event-class" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="event" type="{http://www.mulesoft.org/schema/mule/core}notificationTypes" /&gt;
 *       &lt;attribute name="interface-class" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="interface" type="{http://www.mulesoft.org/schema/mule/core}notificationTypes" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "defineNotificationType")
public class DefineNotificationType {

    @XmlAttribute(name = "event-class")
    protected String eventClass;
    @XmlAttribute(name = "event")
    protected NotificationTypes event;
    @XmlAttribute(name = "interface-class")
    protected String interfaceClass;
    @XmlAttribute(name = "interface")
    protected NotificationTypes _interface;

    /**
     * Gets the value of the eventClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventClass() {
        return eventClass;
    }

    /**
     * Sets the value of the eventClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventClass(String value) {
        this.eventClass = value;
    }

    /**
     * Gets the value of the event property.
     * 
     * @return
     *     possible object is
     *     {@link NotificationTypes }
     *     
     */
    public NotificationTypes getEvent() {
        return event;
    }

    /**
     * Sets the value of the event property.
     * 
     * @param value
     *     allowed object is
     *     {@link NotificationTypes }
     *     
     */
    public void setEvent(NotificationTypes value) {
        this.event = value;
    }

    /**
     * Gets the value of the interfaceClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterfaceClass() {
        return interfaceClass;
    }

    /**
     * Sets the value of the interfaceClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterfaceClass(String value) {
        this.interfaceClass = value;
    }

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link NotificationTypes }
     *     
     */
    public NotificationTypes getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link NotificationTypes }
     *     
     */
    public void setInterface(NotificationTypes value) {
        this._interface = value;
    }

}
