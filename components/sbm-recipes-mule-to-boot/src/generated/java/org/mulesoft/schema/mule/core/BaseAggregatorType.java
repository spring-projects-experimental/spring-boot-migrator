
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for baseAggregatorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="baseAggregatorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractInterceptingMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-message-info-mapping" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="timeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="failOnTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="processed-groups-object-store-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="event-groups-object-store-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="persistentStores" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
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
@XmlType(name = "baseAggregatorType", propOrder = {
    "abstractMessageInfoMapping"
})
@XmlSeeAlso({
    CustomAggregator.class
})
public class BaseAggregatorType
    extends AbstractInterceptingMessageProcessorType
{

    @XmlElementRef(name = "abstract-message-info-mapping", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractMessageInfoMappingType> abstractMessageInfoMapping;
    @XmlAttribute(name = "timeout")
    protected String timeout;
    @XmlAttribute(name = "failOnTimeout")
    protected String failOnTimeout;
    @XmlAttribute(name = "processed-groups-object-store-ref")
    protected String processedGroupsObjectStoreRef;
    @XmlAttribute(name = "event-groups-object-store-ref")
    protected String eventGroupsObjectStoreRef;
    @XmlAttribute(name = "persistentStores")
    protected String persistentStores;
    @XmlAttribute(name = "storePrefix")
    protected String storePrefix;

    /**
     * Gets the value of the abstractMessageInfoMapping property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractMessageInfoMappingType> getAbstractMessageInfoMapping() {
        return abstractMessageInfoMapping;
    }

    /**
     * Sets the value of the abstractMessageInfoMapping property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     *     
     */
    public void setAbstractMessageInfoMapping(JAXBElement<? extends AbstractMessageInfoMappingType> value) {
        this.abstractMessageInfoMapping = value;
    }

    /**
     * Gets the value of the timeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * Sets the value of the timeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeout(String value) {
        this.timeout = value;
    }

    /**
     * Gets the value of the failOnTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailOnTimeout() {
        return failOnTimeout;
    }

    /**
     * Sets the value of the failOnTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailOnTimeout(String value) {
        this.failOnTimeout = value;
    }

    /**
     * Gets the value of the processedGroupsObjectStoreRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessedGroupsObjectStoreRef() {
        return processedGroupsObjectStoreRef;
    }

    /**
     * Sets the value of the processedGroupsObjectStoreRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessedGroupsObjectStoreRef(String value) {
        this.processedGroupsObjectStoreRef = value;
    }

    /**
     * Gets the value of the eventGroupsObjectStoreRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventGroupsObjectStoreRef() {
        return eventGroupsObjectStoreRef;
    }

    /**
     * Sets the value of the eventGroupsObjectStoreRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventGroupsObjectStoreRef(String value) {
        this.eventGroupsObjectStoreRef = value;
    }

    /**
     * Gets the value of the persistentStores property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersistentStores() {
        if (persistentStores == null) {
            return "false";
        } else {
            return persistentStores;
        }
    }

    /**
     * Sets the value of the persistentStores property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersistentStores(String value) {
        this.persistentStores = value;
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
