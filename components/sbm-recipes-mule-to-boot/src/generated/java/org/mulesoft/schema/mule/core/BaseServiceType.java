
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.http.RestServiceWrapperType;


/**
 * 
 *                 Base support for a model-based wrapper around the POJO service (SEDA, Streaming, etc.)
 *             
 * 
 * <p>Java class for baseServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="baseServiceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractServiceType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://www.mulesoft.org/schema/mule/core}descriptionType" minOccurs="0"/&gt;
 *         &lt;element name="inbound" type="{http://www.mulesoft.org/schema/mule/core}inboundCollectionType" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-component" minOccurs="0"/&gt;
 *         &lt;element name="outbound" type="{http://www.mulesoft.org/schema/mule/core}outboundCollectionType" minOccurs="0"/&gt;
 *         &lt;element name="async-reply" type="{http://www.mulesoft.org/schema/mule/core}asyncReplyCollectionType" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}legacy-abstract-exception-strategy" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-message-info-mapping" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="initialState" default="started"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="started"/&gt;
 *             &lt;enumeration value="stopped"/&gt;
 *             &lt;enumeration value="paused"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseServiceType", propOrder = {
    "description",
    "inbound",
    "abstractComponent",
    "outbound",
    "asyncReply",
    "legacyAbstractExceptionStrategy",
    "abstractMessageInfoMapping"
})
@XmlSeeAlso({
    SedaServiceType.class,
    CustomServiceType.class
})
public class BaseServiceType
    extends AbstractServiceType
{

    protected DescriptionType description;
    protected InboundCollectionType inbound;
    @XmlElementRef(name = "abstract-component", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractComponentType> abstractComponent;
    protected OutboundCollectionType outbound;
    @XmlElement(name = "async-reply")
    protected AsyncReplyCollectionType asyncReply;
    @XmlElementRef(name = "legacy-abstract-exception-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends ExceptionStrategyType> legacyAbstractExceptionStrategy;
    @XmlElementRef(name = "abstract-message-info-mapping", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractMessageInfoMappingType> abstractMessageInfoMapping;
    @XmlAttribute(name = "initialState")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String initialState;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionType }
     *     
     */
    public DescriptionType getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionType }
     *     
     */
    public void setDescription(DescriptionType value) {
        this.description = value;
    }

    /**
     * Gets the value of the inbound property.
     * 
     * @return
     *     possible object is
     *     {@link InboundCollectionType }
     *     
     */
    public InboundCollectionType getInbound() {
        return inbound;
    }

    /**
     * Sets the value of the inbound property.
     * 
     * @param value
     *     allowed object is
     *     {@link InboundCollectionType }
     *     
     */
    public void setInbound(InboundCollectionType value) {
        this.inbound = value;
    }

    /**
     * 
     *                                 The service component that is invoked when incoming messages are received. If this element is not present, the service simply bridges the inbound and outbound using a pass-through component.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractComponentType> getAbstractComponent() {
        return abstractComponent;
    }

    /**
     * Sets the value of the abstractComponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     *     
     */
    public void setAbstractComponent(JAXBElement<? extends AbstractComponentType> value) {
        this.abstractComponent = value;
    }

    /**
     * Gets the value of the outbound property.
     * 
     * @return
     *     possible object is
     *     {@link OutboundCollectionType }
     *     
     */
    public OutboundCollectionType getOutbound() {
        return outbound;
    }

    /**
     * Sets the value of the outbound property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutboundCollectionType }
     *     
     */
    public void setOutbound(OutboundCollectionType value) {
        this.outbound = value;
    }

    /**
     * Gets the value of the asyncReply property.
     * 
     * @return
     *     possible object is
     *     {@link AsyncReplyCollectionType }
     *     
     */
    public AsyncReplyCollectionType getAsyncReply() {
        return asyncReply;
    }

    /**
     * Sets the value of the asyncReply property.
     * 
     * @param value
     *     allowed object is
     *     {@link AsyncReplyCollectionType }
     *     
     */
    public void setAsyncReply(AsyncReplyCollectionType value) {
        this.asyncReply = value;
    }

    /**
     * Gets the value of the legacyAbstractExceptionStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     
     */
    public JAXBElement<? extends ExceptionStrategyType> getLegacyAbstractExceptionStrategy() {
        return legacyAbstractExceptionStrategy;
    }

    /**
     * Sets the value of the legacyAbstractExceptionStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     
     */
    public void setLegacyAbstractExceptionStrategy(JAXBElement<? extends ExceptionStrategyType> value) {
        this.legacyAbstractExceptionStrategy = value;
    }

    /**
     * 
     *                                 The message info mapper used to extract key bits of the message
     *                                 information, such as Message ID or Correlation ID. these
     *                                 properties
     *                                 are used by some routers and this mapping information tells Mule
     *                                 where to get the information from in the current message.
     *                             
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
     * Gets the value of the initialState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialState() {
        if (initialState == null) {
            return "started";
        } else {
            return initialState;
        }
    }

    /**
     * Sets the value of the initialState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialState(String value) {
        this.initialState = value;
    }

}
