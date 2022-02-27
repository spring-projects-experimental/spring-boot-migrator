
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sedaModelType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sedaModelType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractModelType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-queue-profile" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="inherit" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sedaModelType", propOrder = {
    "abstractQueueProfile"
})
@XmlSeeAlso({
    DefaultModelType.class
})
public class SedaModelType
    extends AbstractModelType
{

    @XmlElementRef(name = "abstract-queue-profile", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractQueueProfileType> abstractQueueProfile;
    @XmlAttribute(name = "inherit")
    protected Boolean inherit;

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
     * Gets the value of the inherit property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInherit() {
        return inherit;
    }

    /**
     * Sets the value of the inherit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInherit(Boolean value) {
        this.inherit = value;
    }

}
