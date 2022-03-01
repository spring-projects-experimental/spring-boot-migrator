
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.amqp.BasicAckType;
import org.mulesoft.schema.mule.amqp.BasicRejectType;
import org.mulesoft.schema.mule.amqp.ReturnHandlerType;
import org.mulesoft.schema.mule.ee.dw.TransformMessageType;
import org.mulesoft.schema.mule.http.BasicSecurityFilterType;
import org.mulesoft.schema.mule.http.GlobalResponseBuilderType;
import org.mulesoft.schema.mule.http.RequestType;
import org.mulesoft.schema.mule.http.RestServiceWrapperType;
import org.mulesoft.schema.mule.http.StaticResourceHandlerType;
import org.mulesoft.schema.mule.jms.PropertyFilter;


/**
 * <p>Java class for otherwiseMessageProcessorFilterPairType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="otherwiseMessageProcessorFilterPairType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageProcessorFilterPairType"&gt;
 *       &lt;group ref="{http://www.mulesoft.org/schema/mule/core}messageProcessorOrOutboundEndpoint" maxOccurs="unbounded"/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "otherwiseMessageProcessorFilterPairType", propOrder = {
    "messageProcessorOrOutboundEndpoint"
})
public class OtherwiseMessageProcessorFilterPairType
    extends AbstractMessageProcessorFilterPairType
{

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
