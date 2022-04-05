
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for idempotentMessageFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="idempotentMessageFilterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}baseMessageFilterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-object-store" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="idExpression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="valueExpression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="storePrefix" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "idempotentMessageFilterType", propOrder = {
    "abstractObjectStore"
})
@XmlSeeAlso({
    IdempotentSecureHashMessageFilter.class
})
public class IdempotentMessageFilterType
    extends BaseMessageFilterType
{

    @XmlElementRef(name = "abstract-object-store", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractObjectStoreType> abstractObjectStore;
    @XmlAttribute(name = "idExpression")
    protected String idExpression;
    @XmlAttribute(name = "valueExpression")
    protected String valueExpression;
    @XmlAttribute(name = "storePrefix")
    protected String storePrefix;

    /**
     * Gets the value of the abstractObjectStore property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ManagedObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SpringObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMonitoredObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TextFileObjectStoreType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link ManagedObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SpringObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMonitoredObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TextFileObjectStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObjectStoreType }{@code >}
     *     
     */
    public void setAbstractObjectStore(JAXBElement<? extends AbstractObjectStoreType> value) {
        this.abstractObjectStore = value;
    }

    /**
     * Gets the value of the idExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdExpression() {
        return idExpression;
    }

    /**
     * Sets the value of the idExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdExpression(String value) {
        this.idExpression = value;
    }

    /**
     * Gets the value of the valueExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueExpression() {
        return valueExpression;
    }

    /**
     * Sets the value of the valueExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueExpression(String value) {
        this.valueExpression = value;
    }

    /**
     * Gets the value of the storePrefix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStorePrefix() {
        return storePrefix;
    }

    /**
     * Sets the value of the storePrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStorePrefix(String value) {
        this.storePrefix = value;
    }

}
