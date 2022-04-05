
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.amqp.BasicAckType;
import org.mulesoft.schema.mule.amqp.BasicRejectType;
import org.mulesoft.schema.mule.amqp.ReturnHandlerType;
import org.mulesoft.schema.mule.db.BulkUpdateMessageProcessorType;
import org.mulesoft.schema.mule.db.ExecuteDdlMessageProcessorType;
import org.mulesoft.schema.mule.db.ExecuteStoredProcedureMessageProcessorType;
import org.mulesoft.schema.mule.db.InsertMessageProcessorType;
import org.mulesoft.schema.mule.db.SelectMessageProcessorType;
import org.mulesoft.schema.mule.db.UpdateMessageProcessorType;
import org.mulesoft.schema.mule.ee.dw.TransformMessageType;
import org.mulesoft.schema.mule.http.BasicSecurityFilterType;
import org.mulesoft.schema.mule.http.GlobalResponseBuilderType;
import org.mulesoft.schema.mule.http.RequestType;
import org.mulesoft.schema.mule.http.RestServiceWrapperType;
import org.mulesoft.schema.mule.http.StaticResourceHandlerType;
import org.mulesoft.schema.mule.jms.PropertyFilter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractRoutingMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="threading-profile" type="{http://www.mulesoft.org/schema/mule/core}asynchronousThreadingProfileType" minOccurs="0"/&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/core}messageProcessorOrOutboundEndpoint"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="objectStore-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="maxRetries" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="5" /&gt;
 *       &lt;attribute name="millisBetweenRetries" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="secondsBetweenRetries" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="failureExpression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ackExpression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="deadLetterQueue-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="synchronous" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "threadingProfile",
    "abstractMessageProcessor",
    "abstractOutboundEndpoint",
    "abstractMixedContentMessageProcessor"
})
public class UntilSuccessful
    extends AbstractRoutingMessageProcessorType
{

    @XmlElement(name = "threading-profile")
    protected AsynchronousThreadingProfileType threadingProfile;
    @XmlElementRef(name = "abstract-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractMessageProcessorType> abstractMessageProcessor;
    @XmlElementRef(name = "abstract-outbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractOutboundEndpointType> abstractOutboundEndpoint;
    @XmlElementRef(name = "abstract-mixed-content-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<?> abstractMixedContentMessageProcessor;
    @XmlAttribute(name = "objectStore-ref")
    protected String objectStoreRef;
    @XmlAttribute(name = "maxRetries")
    protected String maxRetries;
    @XmlAttribute(name = "millisBetweenRetries")
    protected String millisBetweenRetries;
    @XmlAttribute(name = "secondsBetweenRetries")
    protected String secondsBetweenRetries;
    @XmlAttribute(name = "failureExpression")
    protected String failureExpression;
    @XmlAttribute(name = "ackExpression")
    protected String ackExpression;
    @XmlAttribute(name = "deadLetterQueue-ref")
    protected String deadLetterQueueRef;
    @XmlAttribute(name = "synchronous")
    protected String synchronous;

    /**
     * Gets the value of the threadingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link AsynchronousThreadingProfileType }
     *     
     */
    public AsynchronousThreadingProfileType getThreadingProfile() {
        return threadingProfile;
    }

    /**
     * Sets the value of the threadingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link AsynchronousThreadingProfileType }
     *     
     */
    public void setThreadingProfile(AsynchronousThreadingProfileType value) {
        this.threadingProfile = value;
    }

    /**
     * 
     *                         A message processor
     *                     
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MessageEnricherType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TransformMessageType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetPayloadTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SelectMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RequestReplyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicAckType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UpdateMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicRejectType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseMultipleRoutesRoutingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicAll }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomRouter }{@code >}
     *     {@link JAXBElement }{@code <}{@link RecipientList }{@code >}
     *     {@link JAXBElement }{@code <}{@link ProcessorWithAtLeastOneTargetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicFirstSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicRoundRobin }{@code >}
     *     {@link JAXBElement }{@code <}{@link FirstSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScatterGather }{@code >}
     *     {@link JAXBElement }{@code <}{@link SelectiveOutboundRouterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UntilSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractRoutingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReturnHandlerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExecuteStoredProcedureMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransactional }{@code >}
     *     {@link JAXBElement }{@code <}{@link UpdateMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticResourceHandlerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     *     {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link WireTap }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FlowRef }{@code >}
     *     {@link JAXBElement }{@code <}{@link InvokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RequestType }{@code >}
     *     {@link JAXBElement }{@code <}{@link InsertMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AsyncType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageProcessorChainType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExecuteDdlMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link GlobalResponseBuilderType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEmptyMessageProcessorType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link MessageEnricherType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TransformMessageType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetPayloadTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SelectMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RequestReplyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicAckType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UpdateMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasicRejectType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseMultipleRoutesRoutingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicAll }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomRouter }{@code >}
     *     {@link JAXBElement }{@code <}{@link RecipientList }{@code >}
     *     {@link JAXBElement }{@code <}{@link ProcessorWithAtLeastOneTargetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicFirstSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link DynamicRoundRobin }{@code >}
     *     {@link JAXBElement }{@code <}{@link FirstSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScatterGather }{@code >}
     *     {@link JAXBElement }{@code <}{@link SelectiveOutboundRouterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UntilSuccessful }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractRoutingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReturnHandlerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExecuteStoredProcedureMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransactional }{@code >}
     *     {@link JAXBElement }{@code <}{@link UpdateMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticResourceHandlerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     *     {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link WireTap }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FlowRef }{@code >}
     *     {@link JAXBElement }{@code <}{@link InvokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RequestType }{@code >}
     *     {@link JAXBElement }{@code <}{@link InsertMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AsyncType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MessageProcessorChainType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExecuteDdlMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link GlobalResponseBuilderType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEmptyMessageProcessorType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link BulkUpdateMessageProcessorType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link BulkUpdateMessageProcessorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMixedContentMessageProcessorType }{@code >}
     *     
     */
    public void setAbstractMixedContentMessageProcessor(JAXBElement<?> value) {
        this.abstractMixedContentMessageProcessor = value;
    }

    /**
     * Gets the value of the objectStoreRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectStoreRef() {
        return objectStoreRef;
    }

    /**
     * Sets the value of the objectStoreRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectStoreRef(String value) {
        this.objectStoreRef = value;
    }

    /**
     * Gets the value of the maxRetries property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxRetries() {
        if (maxRetries == null) {
            return "5";
        } else {
            return maxRetries;
        }
    }

    /**
     * Sets the value of the maxRetries property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxRetries(String value) {
        this.maxRetries = value;
    }

    /**
     * Gets the value of the millisBetweenRetries property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMillisBetweenRetries() {
        return millisBetweenRetries;
    }

    /**
     * Sets the value of the millisBetweenRetries property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMillisBetweenRetries(String value) {
        this.millisBetweenRetries = value;
    }

    /**
     * Gets the value of the secondsBetweenRetries property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondsBetweenRetries() {
        return secondsBetweenRetries;
    }

    /**
     * Sets the value of the secondsBetweenRetries property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondsBetweenRetries(String value) {
        this.secondsBetweenRetries = value;
    }

    /**
     * Gets the value of the failureExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailureExpression() {
        return failureExpression;
    }

    /**
     * Sets the value of the failureExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailureExpression(String value) {
        this.failureExpression = value;
    }

    /**
     * Gets the value of the ackExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAckExpression() {
        return ackExpression;
    }

    /**
     * Sets the value of the ackExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAckExpression(String value) {
        this.ackExpression = value;
    }

    /**
     * Gets the value of the deadLetterQueueRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeadLetterQueueRef() {
        return deadLetterQueueRef;
    }

    /**
     * Sets the value of the deadLetterQueueRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeadLetterQueueRef(String value) {
        this.deadLetterQueueRef = value;
    }

    /**
     * Gets the value of the synchronous property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSynchronous() {
        if (synchronous == null) {
            return "false";
        } else {
            return synchronous;
        }
    }

    /**
     * Sets the value of the synchronous property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSynchronous(String value) {
        this.synchronous = value;
    }

}
