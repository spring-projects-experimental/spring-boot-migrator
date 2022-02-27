
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for notificationManagerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="notificationManagerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="notification" type="{http://www.mulesoft.org/schema/mule/core}defineNotificationType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="disable-notification" type="{http://www.mulesoft.org/schema/mule/core}disableNotificationType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="notification-listener" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="subscription" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="dynamic" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "notificationManagerType", propOrder = {
    "notification",
    "disableNotification",
    "notificationListener"
})
public class NotificationManagerType
    extends AnnotatedType
{

    protected List<DefineNotificationType> notification;
    @XmlElement(name = "disable-notification")
    protected List<DisableNotificationType> disableNotification;
    @XmlElement(name = "notification-listener")
    protected List<NotificationManagerType.NotificationListener> notificationListener;
    @XmlAttribute(name = "dynamic")
    protected String dynamic;

    /**
     * Gets the value of the notification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the notification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNotification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DefineNotificationType }
     * 
     * 
     */
    public List<DefineNotificationType> getNotification() {
        if (notification == null) {
            notification = new ArrayList<DefineNotificationType>();
        }
        return this.notification;
    }

    /**
     * Gets the value of the disableNotification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disableNotification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisableNotification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DisableNotificationType }
     * 
     * 
     */
    public List<DisableNotificationType> getDisableNotification() {
        if (disableNotification == null) {
            disableNotification = new ArrayList<DisableNotificationType>();
        }
        return this.disableNotification;
    }

    /**
     * Gets the value of the notificationListener property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the notificationListener property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNotificationListener().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NotificationManagerType.NotificationListener }
     * 
     * 
     */
    public List<NotificationManagerType.NotificationListener> getNotificationListener() {
        if (notificationListener == null) {
            notificationListener = new ArrayList<NotificationManagerType.NotificationListener>();
        }
        return this.notificationListener;
    }

    /**
     * Gets the value of the dynamic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamic() {
        return dynamic;
    }

    /**
     * Sets the value of the dynamic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamic(String value) {
        this.dynamic = value;
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
     *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="subscription" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class NotificationListener {

        @XmlAttribute(name = "ref", required = true)
        protected String ref;
        @XmlAttribute(name = "subscription")
        protected String subscription;

        /**
         * Gets the value of the ref property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRef() {
            return ref;
        }

        /**
         * Sets the value of the ref property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRef(String value) {
            this.ref = value;
        }

        /**
         * Gets the value of the subscription property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSubscription() {
            return subscription;
        }

        /**
         * Sets the value of the subscription property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSubscription(String value) {
            this.subscription = value;
        }

    }

}
