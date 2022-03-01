
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.amqp.BasicAckType;
import org.mulesoft.schema.mule.amqp.BasicRejectType;
import org.mulesoft.schema.mule.amqp.ReturnHandlerType;
import org.mulesoft.schema.mule.ee.dw.TransformMessageType;
import org.mulesoft.schema.mule.http.BasicSecurityFilterType;
import org.mulesoft.schema.mule.http.GlobalResponseBuilderType;
import org.mulesoft.schema.mule.http.ListenerType;
import org.mulesoft.schema.mule.http.RequestType;
import org.mulesoft.schema.mule.http.RestServiceWrapperType;
import org.mulesoft.schema.mule.http.StaticResourceHandlerType;
import org.mulesoft.schema.mule.jms.PropertyFilter;


/**
 * <p>Java class for flowType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="flowType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://www.mulesoft.org/schema/mule/core}descriptionType" minOccurs="0"/&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/core}messageSourceOrInboundEndpoint" minOccurs="0"/&gt;
 *         &lt;choice maxOccurs="unbounded"&gt;
 *           &lt;group ref="{http://www.mulesoft.org/schema/mule/core}messageProcessorOrOutboundEndpoint"/&gt;
 *           &lt;element name="response"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;group ref="{http://www.mulesoft.org/schema/mule/core}messageProcessorOrOutboundEndpoint" maxOccurs="unbounded"/&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}legacy-abstract-exception-strategy" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-message-info-mapping" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
 *       &lt;attribute name="processingStrategy" type="{http://www.mulesoft.org/schema/mule/core}flowProcessingStrategyType" /&gt;
 *       &lt;attribute name="initialState" default="started"&gt;
 *         &lt;simpleType&gt;
 *           &lt;union&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.mulesoft.org/schema/mule/core}propertyPlaceholderType"&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *                 &lt;enumeration value="started"/&gt;
 *                 &lt;enumeration value="stopped"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/union&gt;
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
@XmlType(name = "flowType", propOrder = {
    "description",
    "abstractMessageSource",
    "abstractInboundEndpoint",
    "abstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor",
    "legacyAbstractExceptionStrategy",
    "abstractMessageInfoMapping"
})
public class FlowType
    extends AnnotatedType
{

    protected DescriptionType description;
    @XmlElementRef(name = "abstract-message-source", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractMessageSourceType> abstractMessageSource;
    @XmlElementRef(name = "abstract-inbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractInboundEndpointType> abstractInboundEndpoint;
    @XmlElementRefs({
        @XmlElementRef(name = "abstract-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-outbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-mixed-content-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "response", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> abstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor;
    @XmlElementRef(name = "legacy-abstract-exception-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends ExceptionStrategyType> legacyAbstractExceptionStrategy;
    @XmlElementRef(name = "abstract-message-info-mapping", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractMessageInfoMappingType> abstractMessageInfoMapping;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "processingStrategy")
    protected String processingStrategy;
    @XmlAttribute(name = "initialState")
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
     * 
     *                         A message source
     *                     
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ListenerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PollInboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CompositeMessageSourceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomMessageSourceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMessageSourceType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractMessageSourceType> getAbstractMessageSource() {
        return abstractMessageSource;
    }

    /**
     * Sets the value of the abstractMessageSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ListenerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PollInboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CompositeMessageSourceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomMessageSourceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMessageSourceType }{@code >}
     *     
     */
    public void setAbstractMessageSource(JAXBElement<? extends AbstractMessageSourceType> value) {
        this.abstractMessageSource = value;
    }

    /**
     * 
     *                         An inbound endpoint
     *                     
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractInboundEndpointType> getAbstractInboundEndpoint() {
        return abstractInboundEndpoint;
    }

    /**
     * Sets the value of the abstractInboundEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     *     
     */
    public void setAbstractInboundEndpoint(JAXBElement<? extends AbstractInboundEndpointType> value) {
        this.abstractInboundEndpoint = value;
    }

    /**
     * Gets the value of the abstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ReturnHandlerType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageProcessorChainType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link StaticResourceHandlerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RequestType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetPayloadTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link InvokeType }{@code >}
     * {@link JAXBElement }{@code <}{@link FlowRef }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link WireTap }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link TransformMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransactional }{@code >}
     * {@link JAXBElement }{@code <}{@link RequestReplyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AsyncType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageEnricherType }{@code >}
     * {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     * {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     * {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     * {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     * {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     * {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     * {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     * {@link JAXBElement }{@code <}{@link GlobalResponseBuilderType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractEmptyMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link SelectiveOutboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RecipientList }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseMultipleRoutesRoutingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link DynamicAll }{@code >}
     * {@link JAXBElement }{@code <}{@link DynamicRoundRobin }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomRouter }{@code >}
     * {@link JAXBElement }{@code <}{@link FirstSuccessful }{@code >}
     * {@link JAXBElement }{@code <}{@link UntilSuccessful }{@code >}
     * {@link JAXBElement }{@code <}{@link DynamicFirstSuccessful }{@code >}
     * {@link JAXBElement }{@code <}{@link ScatterGather }{@code >}
     * {@link JAXBElement }{@code <}{@link ProcessorWithAtLeastOneTargetType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractRoutingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link BasicRejectType }{@code >}
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
     * {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link BasicAckType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionComponent }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractMixedContentMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link FlowType.Response }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor() {
        if (abstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor == null) {
            abstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor = new ArrayList<JAXBElement<?>>();
        }
        return this.abstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor;
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
     *                                 The message info mapper used to extract key bits of the message information, such as Message ID or Correlation ID. these properties are used by some routers and this mapping information tells Mule where to get the information from in the current message.
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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the processingStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessingStrategy() {
        return processingStrategy;
    }

    /**
     * Sets the value of the processingStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessingStrategy(String value) {
        this.processingStrategy = value;
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;group ref="{http://www.mulesoft.org/schema/mule/core}messageProcessorOrOutboundEndpoint" maxOccurs="unbounded"/&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "messageProcessorOrOutboundEndpoint"
    })
    public static class Response {

        @XmlElementRefs({
            @XmlElementRef(name = "abstract-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "abstract-outbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "abstract-mixed-content-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
        })
        protected List<JAXBElement<?>> messageProcessorOrOutboundEndpoint;

        /**
         * Gets the value of the messageProcessorOrOutboundEndpoint property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the messageProcessorOrOutboundEndpoint property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMessageProcessorOrOutboundEndpoint().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JAXBElement }{@code <}{@link ReturnHandlerType }{@code >}
         * {@link JAXBElement }{@code <}{@link MessageProcessorChainType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
         * {@link JAXBElement }{@code <}{@link CustomInterceptorType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
         * {@link JAXBElement }{@code <}{@link StaticResourceHandlerType }{@code >}
         * {@link JAXBElement }{@code <}{@link RequestType }{@code >}
         * {@link JAXBElement }{@code <}{@link SetPayloadTransformerType }{@code >}
         * {@link JAXBElement }{@code <}{@link InvokeType }{@code >}
         * {@link JAXBElement }{@code <}{@link FlowRef }{@code >}
         * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
         * {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
         * {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
         * {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
         * {@link JAXBElement }{@code <}{@link WireTap }{@code >}
         * {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
         * {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
         * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
         * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
         * {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
         * {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
         * {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
         * {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
         * {@link JAXBElement }{@code <}{@link Splitter }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
         * {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
         * {@link JAXBElement }{@code <}{@link TransformMessageType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractTransactional }{@code >}
         * {@link JAXBElement }{@code <}{@link RequestReplyType }{@code >}
         * {@link JAXBElement }{@code <}{@link AsyncType }{@code >}
         * {@link JAXBElement }{@code <}{@link MessageEnricherType }{@code >}
         * {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
         * {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
         * {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
         * {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
         * {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
         * {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
         * {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
         * {@link JAXBElement }{@code <}{@link GlobalResponseBuilderType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractEmptyMessageProcessorType }{@code >}
         * {@link JAXBElement }{@code <}{@link SelectiveOutboundRouterType }{@code >}
         * {@link JAXBElement }{@code <}{@link RecipientList }{@code >}
         * {@link JAXBElement }{@code <}{@link BaseMultipleRoutesRoutingMessageProcessorType }{@code >}
         * {@link JAXBElement }{@code <}{@link DynamicAll }{@code >}
         * {@link JAXBElement }{@code <}{@link DynamicRoundRobin }{@code >}
         * {@link JAXBElement }{@code <}{@link CustomRouter }{@code >}
         * {@link JAXBElement }{@code <}{@link FirstSuccessful }{@code >}
         * {@link JAXBElement }{@code <}{@link UntilSuccessful }{@code >}
         * {@link JAXBElement }{@code <}{@link DynamicFirstSuccessful }{@code >}
         * {@link JAXBElement }{@code <}{@link ScatterGather }{@code >}
         * {@link JAXBElement }{@code <}{@link ProcessorWithAtLeastOneTargetType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractRoutingMessageProcessorType }{@code >}
         * {@link JAXBElement }{@code <}{@link BasicRejectType }{@code >}
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
         * {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
         * {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
         * {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
         * {@link JAXBElement }{@code <}{@link BasicAckType }{@code >}
         * {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractMessageProcessorType }{@code >}
         * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
         * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
         * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
         * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
         * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
         * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
         * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
         * {@link JAXBElement }{@code <}{@link ExpressionComponent }{@code >}
         * {@link JAXBElement }{@code <}{@link AbstractMixedContentMessageProcessorType }{@code >}
         * 
         * 
         */
        public List<JAXBElement<?>> getMessageProcessorOrOutboundEndpoint() {
            if (messageProcessorOrOutboundEndpoint == null) {
                messageProcessorOrOutboundEndpoint = new ArrayList<JAXBElement<?>>();
            }
            return this.messageProcessorOrOutboundEndpoint;
        }

    }

}
