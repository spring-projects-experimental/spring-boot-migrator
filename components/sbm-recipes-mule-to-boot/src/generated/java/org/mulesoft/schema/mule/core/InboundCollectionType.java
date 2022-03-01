
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for inboundCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="inboundCollectionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-inbound-endpoint" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-inbound-router"/&gt;
 *           &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-transformer"/&gt;
 *           &lt;element ref="{http://www.mulesoft.org/schema/mule/core}splitter"/&gt;
 *           &lt;element ref="{http://www.mulesoft.org/schema/mule/core}collection-splitter"/&gt;
 *           &lt;element ref="{http://www.mulesoft.org/schema/mule/core}processor"/&gt;
 *           &lt;element ref="{http://www.mulesoft.org/schema/mule/core}custom-processor"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-catch-all-strategy" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inboundCollectionType", propOrder = {
    "abstractInboundEndpoint",
    "abstractInboundRouterOrAbstractTransformerOrSplitter",
    "abstractCatchAllStrategy"
})
public class InboundCollectionType {

    @XmlElementRef(name = "abstract-inbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends AbstractInboundEndpointType>> abstractInboundEndpoint;
    @XmlElementRefs({
        @XmlElementRef(name = "abstract-inbound-router", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-transformer", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "splitter", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "collection-splitter", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "custom-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<? extends AnnotatedType>> abstractInboundRouterOrAbstractTransformerOrSplitter;
    @XmlElementRef(name = "abstract-catch-all-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractCatchAllStrategyType> abstractCatchAllStrategy;

    /**
     * Gets the value of the abstractInboundEndpoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractInboundEndpoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractInboundEndpoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractInboundEndpointType>> getAbstractInboundEndpoint() {
        if (abstractInboundEndpoint == null) {
            abstractInboundEndpoint = new ArrayList<JAXBElement<? extends AbstractInboundEndpointType>>();
        }
        return this.abstractInboundEndpoint;
    }

    /**
     * Gets the value of the abstractInboundRouterOrAbstractTransformerOrSplitter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractInboundRouterOrAbstractTransformerOrSplitter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractInboundRouterOrAbstractTransformerOrSplitter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link CustomInboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WireTapRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentSecureHashReceiverType }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentReceiverType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageChunkingAggregatorRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link FilteredInboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomCorrelationAggregatorRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AnnotatedType>> getAbstractInboundRouterOrAbstractTransformerOrSplitter() {
        if (abstractInboundRouterOrAbstractTransformerOrSplitter == null) {
            abstractInboundRouterOrAbstractTransformerOrSplitter = new ArrayList<JAXBElement<? extends AnnotatedType>>();
        }
        return this.abstractInboundRouterOrAbstractTransformerOrSplitter;
    }

    /**
     * Gets the value of the abstractCatchAllStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CustomCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomForwardingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ForwardingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LoggingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractCatchAllStrategyType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractCatchAllStrategyType> getAbstractCatchAllStrategy() {
        return abstractCatchAllStrategy;
    }

    /**
     * Sets the value of the abstractCatchAllStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CustomCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomForwardingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ForwardingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LoggingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractCatchAllStrategyType }{@code >}
     *     
     */
    public void setAbstractCatchAllStrategy(JAXBElement<? extends AbstractCatchAllStrategyType> value) {
        this.abstractCatchAllStrategy = value;
    }

}
