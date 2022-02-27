
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pooledJavaComponentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pooledJavaComponentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}defaultJavaComponentType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-pooling-profile" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pooledJavaComponentType", propOrder = {
    "abstractPoolingProfile"
})
public class PooledJavaComponentType
    extends DefaultJavaComponentType
{

    @XmlElementRef(name = "abstract-pooling-profile", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractPoolingProfileType> abstractPoolingProfile;

    /**
     * Characteristics of the object pool.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PoolingProfileType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractPoolingProfileType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractPoolingProfileType> getAbstractPoolingProfile() {
        return abstractPoolingProfile;
    }

    /**
     * Sets the value of the abstractPoolingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PoolingProfileType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractPoolingProfileType }{@code >}
     *     
     */
    public void setAbstractPoolingProfile(JAXBElement<? extends AbstractPoolingProfileType> value) {
        this.abstractPoolingProfile = value;
    }

}
