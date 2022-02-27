
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sedaServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sedaServiceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}baseServiceType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-service-threading-profile" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-queue-profile" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="queueTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sedaServiceType", propOrder = {
    "abstractServiceThreadingProfile",
    "abstractQueueProfile"
})
public class SedaServiceType
    extends BaseServiceType
{

    @XmlElementRef(name = "abstract-service-threading-profile", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractServiceThreadingProfileType> abstractServiceThreadingProfile;
    @XmlElementRef(name = "abstract-queue-profile", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractQueueProfileType> abstractQueueProfile;
    @XmlAttribute(name = "queueTimeout")
    protected String queueTimeout;

    /**
     * Gets the value of the abstractServiceThreadingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ThreadingProfileType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractServiceThreadingProfileType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractServiceThreadingProfileType> getAbstractServiceThreadingProfile() {
        return abstractServiceThreadingProfile;
    }

    /**
     * Sets the value of the abstractServiceThreadingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ThreadingProfileType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractServiceThreadingProfileType }{@code >}
     *     
     */
    public void setAbstractServiceThreadingProfile(JAXBElement<? extends AbstractServiceThreadingProfileType> value) {
        this.abstractServiceThreadingProfile = value;
    }

    /**
     * Gets the value of the abstractQueueProfile property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QueueProfileType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractQueueProfileType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractQueueProfileType> getAbstractQueueProfile() {
        return abstractQueueProfile;
    }

    /**
     * Sets the value of the abstractQueueProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QueueProfileType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractQueueProfileType }{@code >}
     *     
     */
    public void setAbstractQueueProfile(JAXBElement<? extends AbstractQueueProfileType> value) {
        this.abstractQueueProfile = value;
    }

    /**
     * Gets the value of the queueTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueueTimeout() {
        return queueTimeout;
    }

    /**
     * Sets the value of the queueTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueueTimeout(String value) {
        this.queueTimeout = value;
    }

}
