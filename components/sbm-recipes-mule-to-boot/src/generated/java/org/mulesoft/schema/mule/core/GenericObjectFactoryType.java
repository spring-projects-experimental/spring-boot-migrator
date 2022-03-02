
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for genericObjectFactoryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="genericObjectFactoryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-object-factory"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "genericObjectFactoryType", propOrder = {
    "abstractObjectFactory"
})
public class GenericObjectFactoryType {

    @XmlElementRef(name = "abstract-object-factory", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractObjectFactoryType> abstractObjectFactory;

    /**
     * Gets the value of the abstractObjectFactory property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PrototypeObjectFactoryType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SpringBeanLookupType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SingletonObjectFactoryType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObjectFactoryType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractObjectFactoryType> getAbstractObjectFactory() {
        return abstractObjectFactory;
    }

    /**
     * Sets the value of the abstractObjectFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PrototypeObjectFactoryType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SpringBeanLookupType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SingletonObjectFactoryType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObjectFactoryType }{@code >}
     *     
     */
    public void setAbstractObjectFactory(JAXBElement<? extends AbstractObjectFactoryType> value) {
        this.abstractObjectFactory = value;
    }

}
