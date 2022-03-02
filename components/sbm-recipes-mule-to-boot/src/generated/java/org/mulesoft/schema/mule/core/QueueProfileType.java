
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queueProfileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queueProfileType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractQueueProfileType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-queue-store" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="maxOutstandingMessages" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queueProfileType", propOrder = {
    "abstractQueueStore"
})
public class QueueProfileType
    extends AbstractQueueProfileType
{

    @XmlElementRef(name = "abstract-queue-store", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractQueueStoreType> abstractQueueStore;
    @XmlAttribute(name = "maxOutstandingMessages")
    protected String maxOutstandingMessages;

    /**
     * 
     *                             The queue store that stortes the queue's elements.  If not specified, this will be the default-in-memory-queue-store.
     *                         
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CustomQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractQueueStoreType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractQueueStoreType> getAbstractQueueStore() {
        return abstractQueueStore;
    }

    /**
     * Sets the value of the abstractQueueStore property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CustomQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractQueueStoreType }{@code >}
     *     
     */
    public void setAbstractQueueStore(JAXBElement<? extends AbstractQueueStoreType> value) {
        this.abstractQueueStore = value;
    }

    /**
     * Gets the value of the maxOutstandingMessages property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxOutstandingMessages() {
        return maxOutstandingMessages;
    }

    /**
     * Sets the value of the maxOutstandingMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxOutstandingMessages(String value) {
        this.maxOutstandingMessages = value;
    }

}
