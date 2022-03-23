
package org.mulesoft.schema.mule.amqp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractComponentType;
import org.mulesoft.schema.mule.core.AbstractEmptyMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractGlobalInterceptingMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractInterceptingMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractInterceptorType;
import org.mulesoft.schema.mule.core.AbstractMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractMixedContentMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractObserverMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractOutboundEndpointType;
import org.mulesoft.schema.mule.core.AbstractRoutingMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractSecurityFilterType;
import org.mulesoft.schema.mule.core.AbstractTransactional;
import org.mulesoft.schema.mule.core.AbstractTransformerType;
import org.mulesoft.schema.mule.core.AppendStringTransformerType;
import org.mulesoft.schema.mule.core.AsyncType;
import org.mulesoft.schema.mule.core.BaseAggregatorType;
import org.mulesoft.schema.mule.core.BaseMultipleRoutesRoutingMessageProcessorType;
import org.mulesoft.schema.mule.core.BeanBuilderTransformer;
import org.mulesoft.schema.mule.core.CollectionFilterType;
import org.mulesoft.schema.mule.core.CollectionSplitter;
import org.mulesoft.schema.mule.core.CombineCollectionsTransformer;
import org.mulesoft.schema.mule.core.CommonFilterType;
import org.mulesoft.schema.mule.core.CommonTransformerType;
import org.mulesoft.schema.mule.core.CopyAttachmentType;
import org.mulesoft.schema.mule.core.CopyPropertiesType;
import org.mulesoft.schema.mule.core.CustomAggregator;
import org.mulesoft.schema.mule.core.CustomFilterType;
import org.mulesoft.schema.mule.core.CustomInterceptorType;
import org.mulesoft.schema.mule.core.CustomMessageProcessorType;
import org.mulesoft.schema.mule.core.CustomRouter;
import org.mulesoft.schema.mule.core.CustomSecurityFilterType;
import org.mulesoft.schema.mule.core.CustomSplitter;
import org.mulesoft.schema.mule.core.CustomTransformerType;
import org.mulesoft.schema.mule.core.DefaultComponentType;
import org.mulesoft.schema.mule.core.DefaultJavaComponentType;
import org.mulesoft.schema.mule.core.DynamicAll;
import org.mulesoft.schema.mule.core.DynamicFirstSuccessful;
import org.mulesoft.schema.mule.core.DynamicRoundRobin;
import org.mulesoft.schema.mule.core.EncryptionSecurityFilterType;
import org.mulesoft.schema.mule.core.EncryptionTransformerType;
import org.mulesoft.schema.mule.core.ExpressionComponent;
import org.mulesoft.schema.mule.core.ExpressionFilterType;
import org.mulesoft.schema.mule.core.ExpressionTransformerType;
import org.mulesoft.schema.mule.core.FirstSuccessful;
import org.mulesoft.schema.mule.core.FlowRef;
import org.mulesoft.schema.mule.core.ForeachProcessorType;
import org.mulesoft.schema.mule.core.IdempotentMessageFilterType;
import org.mulesoft.schema.mule.core.IdempotentSecureHashMessageFilter;
import org.mulesoft.schema.mule.core.InvokeType;
import org.mulesoft.schema.mule.core.LoggerType;
import org.mulesoft.schema.mule.core.MapSplitter;
import org.mulesoft.schema.mule.core.MessageChunkSplitter;
import org.mulesoft.schema.mule.core.MessageEnricherType;
import org.mulesoft.schema.mule.core.MessageFilterType;
import org.mulesoft.schema.mule.core.MessageProcessorChainType;
import org.mulesoft.schema.mule.core.MessagePropertiesTransformerType;
import org.mulesoft.schema.mule.core.ParseTemplateTransformerType;
import org.mulesoft.schema.mule.core.PooledJavaComponentType;
import org.mulesoft.schema.mule.core.ProcessorWithAtLeastOneTargetType;
import org.mulesoft.schema.mule.core.RecipientList;
import org.mulesoft.schema.mule.core.RefFilterType;
import org.mulesoft.schema.mule.core.RefMessageProcessorType;
import org.mulesoft.schema.mule.core.RefTransformerType;
import org.mulesoft.schema.mule.core.RegexFilterType;
import org.mulesoft.schema.mule.core.RemoveAttachmentType;
import org.mulesoft.schema.mule.core.RemovePropertyType;
import org.mulesoft.schema.mule.core.RemoveVariableType;
import org.mulesoft.schema.mule.core.RequestReplyType;
import org.mulesoft.schema.mule.core.ScatterGather;
import org.mulesoft.schema.mule.core.ScopedPropertyFilterType;
import org.mulesoft.schema.mule.core.SelectiveOutboundRouterType;
import org.mulesoft.schema.mule.core.SetAttachmentType;
import org.mulesoft.schema.mule.core.SetPayloadTransformerType;
import org.mulesoft.schema.mule.core.SetPropertyType;
import org.mulesoft.schema.mule.core.SetVariableType;
import org.mulesoft.schema.mule.core.Splitter;
import org.mulesoft.schema.mule.core.StaticComponentType;
import org.mulesoft.schema.mule.core.TypeFilterType;
import org.mulesoft.schema.mule.core.UnitaryFilterType;
import org.mulesoft.schema.mule.core.UntilSuccessful;
import org.mulesoft.schema.mule.core.UsernamePasswordFilterType;
import org.mulesoft.schema.mule.core.ValueExtractorTransformerType;
import org.mulesoft.schema.mule.core.WildcardFilterType;
import org.mulesoft.schema.mule.core.WireTap;
import org.mulesoft.schema.mule.ee.dw.TransformMessageType;
import org.mulesoft.schema.mule.http.BasicSecurityFilterType;
import org.mulesoft.schema.mule.http.GlobalResponseBuilderType;
import org.mulesoft.schema.mule.http.RequestType;
import org.mulesoft.schema.mule.http.RestServiceWrapperType;
import org.mulesoft.schema.mule.http.StaticResourceHandlerType;
import org.mulesoft.schema.mule.jms.PropertyFilter;


/**
 * Defines the strategy to use to handle messages returned by the AMQP broker.
 * 
 * <p>Java class for returnHandlerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="returnHandlerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/core}messageProcessorOrOutboundEndpoint" minOccurs="0"/&gt;
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
@XmlType(name = "returnHandlerType", propOrder = {
    "abstractMessageProcessor",
    "abstractOutboundEndpoint",
    "abstractMixedContentMessageProcessor"
})
public class ReturnHandlerType
    extends AbstractMessageProcessorType
{

    @XmlElementRef(name = "abstract-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractMessageProcessorType> abstractMessageProcessor;
    @XmlElementRef(name = "abstract-outbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractOutboundEndpointType> abstractOutboundEndpoint;
    @XmlElementRef(name = "abstract-mixed-content-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<?> abstractMixedContentMessageProcessor;

    /**
     * 
     *                         A message processor
     *                     
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GlobalResponseBuilderType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEmptyMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link WireTap }{@code >}
     *     {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     *     {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReturnHandlerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicRejectType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageEnricherType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageProcessorChainType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RequestType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransactional }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FlowRef }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicAckType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RequestReplyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FirstSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link RecipientList }{@code >}
     *     {@link JAXBElement }{@code <}{@link ProcessorWithAtLeastOneTargetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicRoundRobin }{@code >}
     *     {@link JAXBElement }{@code <}{@link SelectiveOutboundRouterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicFirstSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicAll }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomRouter }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseMultipleRoutesRoutingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScatterGather }{@code >}
     *     {@link JAXBElement }{@code <}{@link UntilSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractRoutingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetPayloadTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link InvokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticResourceHandlerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AsyncType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TransformMessageType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMessageProcessorType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractMessageProcessorType> getAbstractMessageProcessor() {
        return abstractMessageProcessor;
    }

    /**
     * Sets the value of the abstractMessageProcessor property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GlobalResponseBuilderType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEmptyMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link WireTap }{@code >}
     *     {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     *     {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReturnHandlerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicRejectType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageEnricherType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageProcessorChainType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RequestType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransactional }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FlowRef }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicAckType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RequestReplyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FirstSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link RecipientList }{@code >}
     *     {@link JAXBElement }{@code <}{@link ProcessorWithAtLeastOneTargetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicRoundRobin }{@code >}
     *     {@link JAXBElement }{@code <}{@link SelectiveOutboundRouterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicFirstSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicAll }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomRouter }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseMultipleRoutesRoutingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScatterGather }{@code >}
     *     {@link JAXBElement }{@code <}{@link UntilSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractRoutingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetPayloadTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link InvokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticResourceHandlerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AsyncType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TransformMessageType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMessageProcessorType }{@code >}
     *     
     */
    public void setAbstractMessageProcessor(JAXBElement<? extends AbstractMessageProcessorType> value) {
        this.abstractMessageProcessor = value;
    }

    /**
     * 
     *                         An outbound endpoint
     *                     
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractOutboundEndpointType> getAbstractOutboundEndpoint() {
        return abstractOutboundEndpoint;
    }

    /**
     * Sets the value of the abstractOutboundEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     *     
     */
    public void setAbstractOutboundEndpoint(JAXBElement<? extends AbstractOutboundEndpointType> value) {
        this.abstractOutboundEndpoint = value;
    }

    /**
     * Gets the value of the abstractMixedContentMessageProcessor property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ExpressionComponent }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMixedContentMessageProcessorType }{@code >}
     *     
     */
    public JAXBElement<?> getAbstractMixedContentMessageProcessor() {
        return abstractMixedContentMessageProcessor;
    }

    /**
     * Sets the value of the abstractMixedContentMessageProcessor property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ExpressionComponent }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMixedContentMessageProcessorType }{@code >}
     *     
     */
    public void setAbstractMixedContentMessageProcessor(JAXBElement<?> value) {
        this.abstractMixedContentMessageProcessor = value;
    }

}
