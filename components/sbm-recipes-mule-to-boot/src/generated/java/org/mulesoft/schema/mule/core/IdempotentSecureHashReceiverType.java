
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for idempotentSecureHashReceiverType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="idempotentSecureHashReceiverType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractInboundRouterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-object-store" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="messageDigestAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "idempotentSecureHashReceiverType", propOrder = {
    "abstractObjectStore"
})
public class IdempotentSecureHashReceiverType
    extends AbstractInboundRouterType
{

    @XmlElementRef(name = "abstract-object-store", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractObjectStoreType> abstractObjectStore;
    @XmlAttribute(name = "messageDigestAlgorithm")
    protected String messageDigestAlgorithm;

    /**
     * Gets the value of the abstractObjectStore property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SpringObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ManagedObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMonitoredObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TextFileObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObjectStoreType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractObjectStoreType> getAbstractObjectStore() {
        return abstractObjectStore;
    }

    /**
     * Sets the value of the abstractObjectStore property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SpringObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ManagedObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMonitoredObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TextFileObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObjectStoreType }{@code >}
     *     
     */
    public void setAbstractObjectStore(JAXBElement<? extends AbstractObjectStoreType> value) {
        this.abstractObjectStore = value;
    }

    /**
     * Gets the value of the messageDigestAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageDigestAlgorithm() {
        return messageDigestAlgorithm;
    }

    /**
     * Sets the value of the messageDigestAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageDigestAlgorithm(String value) {
        this.messageDigestAlgorithm = value;
    }

}
