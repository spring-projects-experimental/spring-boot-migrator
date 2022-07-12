
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.core package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AbstractMessageProcessor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-message-processor");
    private final static QName _Mule_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "mule");
    private final static QName _GlobalProperty_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "global-property");
    private final static QName _Configuration_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "configuration");
    private final static QName _Notifications_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "notifications");
    private final static QName _AbstractSharedExtension_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-shared-extension");
    private final static QName _AbstractExtension_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-extension");
    private final static QName _AbstractMixedContentExtension_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-mixed-content-extension");
    private final static QName _ExpressionComponent_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "expression-component");
    private final static QName _AbstractMixedContentMessageProcessor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-mixed-content-message-processor");
    private final static QName _AbstractSharedConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-shared-connector");
    private final static QName _AbstractConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-connector");
    private final static QName _AbstractAgent_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-agent");
    private final static QName _CustomAgent_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-agent");
    private final static QName _AbstractQueueStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-queue-store");
    private final static QName _QueueStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "queue-store");
    private final static QName _CustomQueueStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-queue-store");
    private final static QName _DefaultInMemoryQueueStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "default-in-memory-queue-store");
    private final static QName _DefaultPersistentQueueStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "default-persistent-queue-store");
    private final static QName _SimpleInMemoryQueueStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "simple-in-memory-queue-store");
    private final static QName _FileQueueStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "file-queue-store");
    private final static QName _AbstractGlobalEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-global-endpoint");
    private final static QName _AbstractInboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-inbound-endpoint");
    private final static QName _AbstractOutboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-outbound-endpoint");
    private final static QName _AbstractObserverMessageProcessor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-observer-message-processor");
    private final static QName _Processor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "processor");
    private final static QName _CustomProcessor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-processor");
    private final static QName _AbstractEmptyProcessor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-empty-processor");
    private final static QName _ProcessorChain_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "processor-chain");
    private final static QName _SubFlow_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "sub-flow");
    private final static QName _Invoke_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "invoke");
    private final static QName _Enricher_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "enricher");
    private final static QName _Async_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "async");
    private final static QName _RequestReply_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "request-reply");
    private final static QName _AbstractPollOverride_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-poll-override");
    private final static QName _Watermark_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "watermark");
    private final static QName _Logger_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "logger");
    private final static QName _Transactional_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "transactional");
    private final static QName _AbstractMessageSource_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-message-source");
    private final static QName _CustomSource_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-source");
    private final static QName _CompositeSource_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "composite-source");
    private final static QName _AbstractTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-transformer");
    private final static QName _AbstractCatchAllStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-catch-all-strategy");
    private final static QName _AbstractInboundRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-inbound-router");
    private final static QName _AbstractFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-filter");
    private final static QName _AbstractOutboundRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-outbound-router");
    private final static QName _AbstractTransactionManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-transaction-manager");
    private final static QName _AbstractSharedTransactionManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-shared-transaction-manager");
    private final static QName _AbstractConfigurationExtension_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-configuration-extension");
    private final static QName _AbstractModel_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-model");
    private final static QName _SedaModel_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "seda-model");
    private final static QName _Model_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "model");
    private final static QName _AbstractQueueProfile_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-queue-profile");
    private final static QName _AbstractEntryPointResolverSet_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-entry-point-resolver-set");
    private final static QName _LegacyEntryPointResolverSet_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "legacy-entry-point-resolver-set");
    private final static QName _EntryPointResolverSet_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "entry-point-resolver-set");
    private final static QName _CustomEntryPointResolverSet_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-entry-point-resolver-set");
    private final static QName _AbstractEntryPointResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-entry-point-resolver");
    private final static QName _CallableEntryPointResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "callable-entry-point-resolver");
    private final static QName _CustomEntryPointResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-entry-point-resolver");
    private final static QName _PropertyEntryPointResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "property-entry-point-resolver");
    private final static QName _MethodEntryPointResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "method-entry-point-resolver");
    private final static QName _ReflectionEntryPointResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "reflection-entry-point-resolver");
    private final static QName _ArrayEntryPointResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "array-entry-point-resolver");
    private final static QName _NoArgumentsEntryPointResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "no-arguments-entry-point-resolver");
    private final static QName _IncludeEntryPoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "include-entry-point");
    private final static QName _AbstractService_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-service");
    private final static QName _LegacyAbstractExceptionStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "legacy-abstract-exception-strategy");
    private final static QName _AbstractExceptionStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-exception-strategy");
    private final static QName _AbstractReconnectionStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-reconnection-strategy");
    private final static QName _AbstractReconnectNotifier_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-reconnect-notifier");
    private final static QName _Reconnect_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "reconnect");
    private final static QName _ReconnectForever_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "reconnect-forever");
    private final static QName _ReconnectCustomStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "reconnect-custom-strategy");
    private final static QName _ReconnectNotifier_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "reconnect-notifier");
    private final static QName _ReconnectCustomNotifier_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "reconnect-custom-notifier");
    private final static QName _AbstractAsyncReplyRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-async-reply-router");
    private final static QName _Service_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "service");
    private final static QName _CustomService_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-service");
    private final static QName _AbstractServiceThreadingProfile_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-service-threading-profile");
    private final static QName _AbstractFlowConstruct_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-flow-construct");
    private final static QName _Flow_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "flow");
    private final static QName _AbstractProcessingStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-processing-strategy");
    private final static QName _AsynchronousProcessingStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "asynchronous-processing-strategy");
    private final static QName _QueuedAsynchronousProcessingStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "queued-asynchronous-processing-strategy");
    private final static QName _ThreadPerProcessorProcessingStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "thread-per-processor-processing-strategy");
    private final static QName _QueuedThreadPerProcessorProcessingStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "queued-thread-per-processor-processing-strategy");
    private final static QName _NonBlockingProcessingStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "non-blocking-processing-strategy");
    private final static QName _CustomProcessingStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-processing-strategy");
    private final static QName _FlowRef_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "flow-ref");
    private final static QName _SimpleService_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "simple-service");
    private final static QName _Bridge_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "bridge");
    private final static QName _Validator_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "validator");
    private final static QName _AbstractComponent_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-component");
    private final static QName _Component_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "component");
    private final static QName _PooledComponent_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "pooled-component");
    private final static QName _EchoComponent_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "echo-component");
    private final static QName _LogComponent_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "log-component");
    private final static QName _NullComponent_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "null-component");
    private final static QName _StaticComponent_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "static-component");
    private final static QName _AbstractTransaction_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-transaction");
    private final static QName _AbstractXaTransaction_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-xa-transaction");
    private final static QName _AbstractMultiTransaction_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-multi-transaction");
    private final static QName _CustomTransaction_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-transaction");
    private final static QName _XaTransaction_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "xa-transaction");
    private final static QName _WebsphereTransactionManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "websphere-transaction-manager");
    private final static QName _JbossTransactionManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "jboss-transaction-manager");
    private final static QName _WeblogicTransactionManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "weblogic-transaction-manager");
    private final static QName _JrunTransactionManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "jrun-transaction-manager");
    private final static QName _ResinTransactionManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "resin-transaction-manager");
    private final static QName _JndiTransactionManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "jndi-transaction-manager");
    private final static QName _CustomTransactionManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-transaction-manager");
    private final static QName _Endpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "endpoint");
    private final static QName _InboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "inbound-endpoint");
    private final static QName _AbstractScheduler_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-scheduler");
    private final static QName _FixedFrequencyScheduler_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "fixed-frequency-scheduler");
    private final static QName _Poll_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "poll");
    private final static QName _OutboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "outbound-endpoint");
    private final static QName _AbstractSecurityFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-security-filter");
    private final static QName _AbstractInterceptingMessageProcessor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-intercepting-message-processor");
    private final static QName _UsernamePasswordFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "username-password-filter");
    private final static QName _CustomSecurityFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-security-filter");
    private final static QName _Filter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "filter");
    private final static QName _NotFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "not-filter");
    private final static QName _AndFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "and-filter");
    private final static QName _OrFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "or-filter");
    private final static QName _WildcardFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "wildcard-filter");
    private final static QName _ExpressionFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "expression-filter");
    private final static QName _RegexFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "regex-filter");
    private final static QName _MessagePropertyFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "message-property-filter");
    private final static QName _ExceptionTypeFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "exception-type-filter");
    private final static QName _PayloadTypeFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "payload-type-filter");
    private final static QName _CustomFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-filter");
    private final static QName _AbstractInterceptorStack_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-interceptor-stack");
    private final static QName _InterceptorStack_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "interceptor-stack");
    private final static QName _AbstractInterceptor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-interceptor");
    private final static QName _TimerInterceptor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "timer-interceptor");
    private final static QName _LoggingInterceptor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "logging-interceptor");
    private final static QName _CustomInterceptor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-interceptor");
    private final static QName _EncryptionSecurityFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "encryption-security-filter");
    private final static QName _Transformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "transformer");
    private final static QName _AutoTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "auto-transformer");
    private final static QName _CustomTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-transformer");
    private final static QName _MessagePropertiesTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "message-properties-transformer");
    private final static QName _SetProperty_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "set-property");
    private final static QName _RemoveProperty_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "remove-property");
    private final static QName _CopyProperties_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "copy-properties");
    private final static QName _SetVariable_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "set-variable");
    private final static QName _RemoveVariable_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "remove-variable");
    private final static QName _SetSessionVariable_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "set-session-variable");
    private final static QName _RemoveSessionVariable_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "remove-session-variable");
    private final static QName _SetAttachment_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "set-attachment");
    private final static QName _RemoveAttachment_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "remove-attachment");
    private final static QName _CopyAttachments_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "copy-attachments");
    private final static QName _Base64EncoderTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "base64-encoder-transformer");
    private final static QName _Base64DecoderTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "base64-decoder-transformer");
    private final static QName _XmlEntityEncoderTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "xml-entity-encoder-transformer");
    private final static QName _XmlEntityDecoderTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "xml-entity-decoder-transformer");
    private final static QName _GzipCompressTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "gzip-compress-transformer");
    private final static QName _GzipUncompressTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "gzip-uncompress-transformer");
    private final static QName _ByteArrayToHexStringTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "byte-array-to-hex-string-transformer");
    private final static QName _HexStringToByteArrayTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "hex-string-to-byte-array-transformer");
    private final static QName _ByteArrayToObjectTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "byte-array-to-object-transformer");
    private final static QName _ObjectToByteArrayTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "object-to-byte-array-transformer");
    private final static QName _ObjectToStringTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "object-to-string-transformer");
    private final static QName _ByteArrayToSerializableTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "byte-array-to-serializable-transformer");
    private final static QName _SerializableToByteArrayTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "serializable-to-byte-array-transformer");
    private final static QName _ByteArrayToStringTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "byte-array-to-string-transformer");
    private final static QName _StringToByteArrayTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "string-to-byte-array-transformer");
    private final static QName _MapToBeanTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "map-to-bean-transformer");
    private final static QName _BeanToMapTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "bean-to-map-transformer");
    private final static QName _AppendStringTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "append-string-transformer");
    private final static QName _ParseTemplate_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "parse-template");
    private final static QName _SetPayload_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "set-payload");
    private final static QName _EncryptTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "encrypt-transformer");
    private final static QName _DecryptTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "decrypt-transformer");
    private final static QName _BeanBuilderTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "bean-builder-transformer");
    private final static QName _ExpressionTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "expression-transformer");
    private final static QName _ValueExtractorTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "value-extractor-transformer");
    private final static QName _QueueProfile_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "queue-profile");
    private final static QName _AbstractSecurityManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-security-manager");
    private final static QName _SecurityManager_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "security-manager");
    private final static QName _ThreadingProfile_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "threading-profile");
    private final static QName _DefaultExceptionStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "default-exception-strategy");
    private final static QName _CatchExceptionStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "catch-exception-strategy");
    private final static QName _ChoiceExceptionStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "choice-exception-strategy");
    private final static QName _RollbackExceptionStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "rollback-exception-strategy");
    private final static QName _ExceptionStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "exception-strategy");
    private final static QName _CustomExceptionStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-exception-strategy");
    private final static QName _DefaultServiceExceptionStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "default-service-exception-strategy");
    private final static QName _CustomConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-connector");
    private final static QName _AbstractObjectStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-object-store");
    private final static QName _InMemoryStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "in-memory-store");
    private final static QName _SimpleTextFileStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "simple-text-file-store");
    private final static QName _ManagedStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "managed-store");
    private final static QName _CustomObjectStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-object-store");
    private final static QName _SpringObjectStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "spring-object-store");
    private final static QName _AbstractGlobalInterceptingMessageProcessor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-global-intercepting-message-processor");
    private final static QName _MessageFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "message-filter");
    private final static QName _IdempotentMessageFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "idempotent-message-filter");
    private final static QName _AbstractRedeliveryPolicy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-redelivery-policy");
    private final static QName _IdempotentRedeliveryPolicy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "idempotent-redelivery-policy");
    private final static QName _IdempotentSecureHashMessageFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "idempotent-secure-hash-message-filter");
    private final static QName _WireTap_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "wire-tap");
    private final static QName _CombineCollectionsTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "combine-collections-transformer");
    private final static QName _Resequencer_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "resequencer");
    private final static QName _CollectionAggregator_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "collection-aggregator");
    private final static QName _MessageChunkAggregator_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "message-chunk-aggregator");
    private final static QName _CustomAggregator_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-aggregator");
    private final static QName _Splitter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "splitter");
    private final static QName _CollectionSplitter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "collection-splitter");
    private final static QName _MapSplitter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "map-splitter");
    private final static QName _MessageChunkSplitter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "message-chunk-splitter");
    private final static QName _CustomSplitter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-splitter");
    private final static QName _Foreach_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "foreach");
    private final static QName _AbstractRoutingMessageProcessor_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-routing-message-processor");
    private final static QName _CustomRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-router");
    private final static QName _Choice_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "choice");
    private final static QName _All_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "all");
    private final static QName _FirstSuccessful_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "first-successful");
    private final static QName _UntilSuccessful_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "until-successful");
    private final static QName _ScatterGather_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "scatter-gather");
    private final static QName _RoundRobin_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "round-robin");
    private final static QName _RecipientList_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "recipient-list");
    private final static QName _DynamicRoundRobin_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "dynamic-round-robin");
    private final static QName _DynamicAll_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "dynamic-all");
    private final static QName _DynamicFirstSuccessful_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "dynamic-first-successful");
    private final static QName _AbstractDynamicRouteResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-dynamic-route-resolver");
    private final static QName _CustomRouteResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-route-resolver");
    private final static QName _IdempotentReceiverRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "idempotent-receiver-router");
    private final static QName _IdempotentSecureHashReceiverRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "idempotent-secure-hash-receiver-router");
    private final static QName _WireTapRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "wire-tap-router");
    private final static QName _ForwardingRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "forwarding-router");
    private final static QName _SelectiveConsumerRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "selective-consumer-router");
    private final static QName _CorrelationResequencerRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "correlation-resequencer-router");
    private final static QName _MessageChunkingAggregatorRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "message-chunking-aggregator-router");
    private final static QName _CustomCorrelationAggregatorRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-correlation-aggregator-router");
    private final static QName _CollectionAggregatorRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "collection-aggregator-router");
    private final static QName _CustomInboundRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-inbound-router");
    private final static QName _SingleAsyncReplyRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "single-async-reply-router");
    private final static QName _CollectionAsyncReplyRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "collection-async-reply-router");
    private final static QName _CustomAsyncReplyRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-async-reply-router");
    private final static QName _PassThroughRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "pass-through-router");
    private final static QName _FilteringRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "filtering-router");
    private final static QName _ChainingRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "chaining-router");
    private final static QName _ExceptionBasedRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "exception-based-router");
    private final static QName _RecipientListExceptionBasedRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "recipient-list-exception-based-router");
    private final static QName _MulticastingRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "multicasting-router");
    private final static QName _SequenceRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "sequence-router");
    private final static QName _EndpointSelectorRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "endpoint-selector-router");
    private final static QName _ListMessageSplitterRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "list-message-splitter-router");
    private final static QName _ExpressionSplitterRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "expression-splitter-router");
    private final static QName _MessageChunkingRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "message-chunking-router");
    private final static QName _StaticRecipientListRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "static-recipient-list-router");
    private final static QName _ExpressionRecipientListRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "expression-recipient-list-router");
    private final static QName _CustomOutboundRouter_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-outbound-router");
    private final static QName _LoggingCatchAllStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "logging-catch-all-strategy");
    private final static QName _CustomCatchAllStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-catch-all-strategy");
    private final static QName _ForwardingCatchAllStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "forwarding-catch-all-strategy");
    private final static QName _CustomForwardingCatchAllStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-forwarding-catch-all-strategy");
    private final static QName _AbstractMessageInfoMapping_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-message-info-mapping");
    private final static QName _ExpressionMessageInfoMapping_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "expression-message-info-mapping");
    private final static QName _CustomMessageInfoMapping_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-message-info-mapping");
    private final static QName _AbstractLifecycleAdapterFactory_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-lifecycle-adapter-factory");
    private final static QName _CustomLifecycleAdapterFactory_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "custom-lifecycle-adapter-factory");
    private final static QName _AbstractObjectFactory_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-object-factory");
    private final static QName _SpringObject_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "spring-object");
    private final static QName _SingletonObject_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "singleton-object");
    private final static QName _PrototypeObject_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "prototype-object");
    private final static QName _AbstractPoolingProfile_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-pooling-profile");
    private final static QName _PoolingProfile_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "pooling-profile");
    private final static QName _Properties_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "properties");
    private final static QName _AbstractCachingStrategy_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "abstract-caching-strategy");
    private final static QName _AnnotatedMixedContentTypeAnnotations_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "annotations");
    private final static QName _FlowTypeResponse_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "response");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.core
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RollbackExceptionStrategyType }
     * 
     */
    public RollbackExceptionStrategyType createRollbackExceptionStrategyType() {
        return new RollbackExceptionStrategyType();
    }

    /**
     * Create an instance of {@link ExpressionTransformerType }
     * 
     */
    public ExpressionTransformerType createExpressionTransformerType() {
        return new ExpressionTransformerType();
    }

    /**
     * Create an instance of {@link BeanBuilderTransformer }
     * 
     */
    public BeanBuilderTransformer createBeanBuilderTransformer() {
        return new BeanBuilderTransformer();
    }

    /**
     * Create an instance of {@link StaticComponentType }
     * 
     */
    public StaticComponentType createStaticComponentType() {
        return new StaticComponentType();
    }

    /**
     * Create an instance of {@link FlowType }
     * 
     */
    public FlowType createFlowType() {
        return new FlowType();
    }

    /**
     * Create an instance of {@link NotificationManagerType }
     * 
     */
    public NotificationManagerType createNotificationManagerType() {
        return new NotificationManagerType();
    }

    /**
     * Create an instance of {@link ConfigurationType }
     * 
     */
    public ConfigurationType createConfigurationType() {
        return new ConfigurationType();
    }

    /**
     * Create an instance of {@link AbstractMessageProcessorType }
     * 
     */
    public AbstractMessageProcessorType createAbstractMessageProcessorType() {
        return new AbstractMessageProcessorType();
    }

    /**
     * Create an instance of {@link MuleType }
     * 
     */
    public MuleType createMuleType() {
        return new MuleType();
    }

    /**
     * Create an instance of {@link GlobalPropertyType }
     * 
     */
    public GlobalPropertyType createGlobalPropertyType() {
        return new GlobalPropertyType();
    }

    /**
     * Create an instance of {@link AbstractExtensionType }
     * 
     */
    public AbstractExtensionType createAbstractExtensionType() {
        return new AbstractExtensionType();
    }

    /**
     * Create an instance of {@link AbstractMixedContentExtensionType }
     * 
     */
    public AbstractMixedContentExtensionType createAbstractMixedContentExtensionType() {
        return new AbstractMixedContentExtensionType();
    }

    /**
     * Create an instance of {@link ExpressionComponent }
     * 
     */
    public ExpressionComponent createExpressionComponent() {
        return new ExpressionComponent();
    }

    /**
     * Create an instance of {@link AbstractMixedContentMessageProcessorType }
     * 
     */
    public AbstractMixedContentMessageProcessorType createAbstractMixedContentMessageProcessorType() {
        return new AbstractMixedContentMessageProcessorType();
    }

    /**
     * Create an instance of {@link AbstractConnectorType }
     * 
     */
    public AbstractConnectorType createAbstractConnectorType() {
        return new AbstractConnectorType();
    }

    /**
     * Create an instance of {@link AbstractAgentType }
     * 
     */
    public AbstractAgentType createAbstractAgentType() {
        return new AbstractAgentType();
    }

    /**
     * Create an instance of {@link CustomAgentType }
     * 
     */
    public CustomAgentType createCustomAgentType() {
        return new CustomAgentType();
    }

    /**
     * Create an instance of {@link RefQueueStoreType }
     * 
     */
    public RefQueueStoreType createRefQueueStoreType() {
        return new RefQueueStoreType();
    }

    /**
     * Create an instance of {@link CustomQueueStoreType }
     * 
     */
    public CustomQueueStoreType createCustomQueueStoreType() {
        return new CustomQueueStoreType();
    }

    /**
     * Create an instance of {@link DefaultQueueStoreType }
     * 
     */
    public DefaultQueueStoreType createDefaultQueueStoreType() {
        return new DefaultQueueStoreType();
    }

    /**
     * Create an instance of {@link AbstractGlobalEndpointType }
     * 
     */
    public AbstractGlobalEndpointType createAbstractGlobalEndpointType() {
        return new AbstractGlobalEndpointType();
    }

    /**
     * Create an instance of {@link AbstractInboundEndpointType }
     * 
     */
    public AbstractInboundEndpointType createAbstractInboundEndpointType() {
        return new AbstractInboundEndpointType();
    }

    /**
     * Create an instance of {@link AbstractOutboundEndpointType }
     * 
     */
    public AbstractOutboundEndpointType createAbstractOutboundEndpointType() {
        return new AbstractOutboundEndpointType();
    }

    /**
     * Create an instance of {@link AbstractObserverMessageProcessorType }
     * 
     */
    public AbstractObserverMessageProcessorType createAbstractObserverMessageProcessorType() {
        return new AbstractObserverMessageProcessorType();
    }

    /**
     * Create an instance of {@link RefMessageProcessorType }
     * 
     */
    public RefMessageProcessorType createRefMessageProcessorType() {
        return new RefMessageProcessorType();
    }

    /**
     * Create an instance of {@link CustomMessageProcessorType }
     * 
     */
    public CustomMessageProcessorType createCustomMessageProcessorType() {
        return new CustomMessageProcessorType();
    }

    /**
     * Create an instance of {@link AbstractEmptyMessageProcessorType }
     * 
     */
    public AbstractEmptyMessageProcessorType createAbstractEmptyMessageProcessorType() {
        return new AbstractEmptyMessageProcessorType();
    }

    /**
     * Create an instance of {@link MessageProcessorChainType }
     * 
     */
    public MessageProcessorChainType createMessageProcessorChainType() {
        return new MessageProcessorChainType();
    }

    /**
     * Create an instance of {@link SubFlowType }
     * 
     */
    public SubFlowType createSubFlowType() {
        return new SubFlowType();
    }

    /**
     * Create an instance of {@link InvokeType }
     * 
     */
    public InvokeType createInvokeType() {
        return new InvokeType();
    }

    /**
     * Create an instance of {@link MessageEnricherType }
     * 
     */
    public MessageEnricherType createMessageEnricherType() {
        return new MessageEnricherType();
    }

    /**
     * Create an instance of {@link AsyncType }
     * 
     */
    public AsyncType createAsyncType() {
        return new AsyncType();
    }

    /**
     * Create an instance of {@link RequestReplyType }
     * 
     */
    public RequestReplyType createRequestReplyType() {
        return new RequestReplyType();
    }

    /**
     * Create an instance of {@link AbstractPollOverrideType }
     * 
     */
    public AbstractPollOverrideType createAbstractPollOverrideType() {
        return new AbstractPollOverrideType();
    }

    /**
     * Create an instance of {@link WatermarkType }
     * 
     */
    public WatermarkType createWatermarkType() {
        return new WatermarkType();
    }

    /**
     * Create an instance of {@link LoggerType }
     * 
     */
    public LoggerType createLoggerType() {
        return new LoggerType();
    }

    /**
     * Create an instance of {@link AbstractTransactional }
     * 
     */
    public AbstractTransactional createAbstractTransactional() {
        return new AbstractTransactional();
    }

    /**
     * Create an instance of {@link AbstractMessageSourceType }
     * 
     */
    public AbstractMessageSourceType createAbstractMessageSourceType() {
        return new AbstractMessageSourceType();
    }

    /**
     * Create an instance of {@link CustomMessageSourceType }
     * 
     */
    public CustomMessageSourceType createCustomMessageSourceType() {
        return new CustomMessageSourceType();
    }

    /**
     * Create an instance of {@link CompositeMessageSourceType }
     * 
     */
    public CompositeMessageSourceType createCompositeMessageSourceType() {
        return new CompositeMessageSourceType();
    }

    /**
     * Create an instance of {@link CommonTransformerType }
     * 
     */
    public CommonTransformerType createCommonTransformerType() {
        return new CommonTransformerType();
    }

    /**
     * Create an instance of {@link AbstractCatchAllStrategyType }
     * 
     */
    public AbstractCatchAllStrategyType createAbstractCatchAllStrategyType() {
        return new AbstractCatchAllStrategyType();
    }

    /**
     * Create an instance of {@link AbstractInboundRouterType }
     * 
     */
    public AbstractInboundRouterType createAbstractInboundRouterType() {
        return new AbstractInboundRouterType();
    }

    /**
     * Create an instance of {@link CommonFilterType }
     * 
     */
    public CommonFilterType createCommonFilterType() {
        return new CommonFilterType();
    }

    /**
     * Create an instance of {@link AbstractOutboundRouterType }
     * 
     */
    public AbstractOutboundRouterType createAbstractOutboundRouterType() {
        return new AbstractOutboundRouterType();
    }

    /**
     * Create an instance of {@link AbstractTransactionManagerType }
     * 
     */
    public AbstractTransactionManagerType createAbstractTransactionManagerType() {
        return new AbstractTransactionManagerType();
    }

    /**
     * Create an instance of {@link AbstractConfigurationExtensionType }
     * 
     */
    public AbstractConfigurationExtensionType createAbstractConfigurationExtensionType() {
        return new AbstractConfigurationExtensionType();
    }

    /**
     * Create an instance of {@link AbstractModelType }
     * 
     */
    public AbstractModelType createAbstractModelType() {
        return new AbstractModelType();
    }

    /**
     * Create an instance of {@link SedaModelType }
     * 
     */
    public SedaModelType createSedaModelType() {
        return new SedaModelType();
    }

    /**
     * Create an instance of {@link DefaultModelType }
     * 
     */
    public DefaultModelType createDefaultModelType() {
        return new DefaultModelType();
    }

    /**
     * Create an instance of {@link AbstractQueueProfileType }
     * 
     */
    public AbstractQueueProfileType createAbstractQueueProfileType() {
        return new AbstractQueueProfileType();
    }

    /**
     * Create an instance of {@link AbstractEntryPointResolverSetType }
     * 
     */
    public AbstractEntryPointResolverSetType createAbstractEntryPointResolverSetType() {
        return new AbstractEntryPointResolverSetType();
    }

    /**
     * Create an instance of {@link ExtensibleEntryPointResolverSet }
     * 
     */
    public ExtensibleEntryPointResolverSet createExtensibleEntryPointResolverSet() {
        return new ExtensibleEntryPointResolverSet();
    }

    /**
     * Create an instance of {@link CustomEntryPointResolverSetType }
     * 
     */
    public CustomEntryPointResolverSetType createCustomEntryPointResolverSetType() {
        return new CustomEntryPointResolverSetType();
    }

    /**
     * Create an instance of {@link AbstractEntryPointResolverType }
     * 
     */
    public AbstractEntryPointResolverType createAbstractEntryPointResolverType() {
        return new AbstractEntryPointResolverType();
    }

    /**
     * Create an instance of {@link CustomEntryPointResolverType }
     * 
     */
    public CustomEntryPointResolverType createCustomEntryPointResolverType() {
        return new CustomEntryPointResolverType();
    }

    /**
     * Create an instance of {@link PropertyEntryPointResolverType }
     * 
     */
    public PropertyEntryPointResolverType createPropertyEntryPointResolverType() {
        return new PropertyEntryPointResolverType();
    }

    /**
     * Create an instance of {@link MethodEntryPointResolverType }
     * 
     */
    public MethodEntryPointResolverType createMethodEntryPointResolverType() {
        return new MethodEntryPointResolverType();
    }

    /**
     * Create an instance of {@link ReflectionEntryPointResolverType }
     * 
     */
    public ReflectionEntryPointResolverType createReflectionEntryPointResolverType() {
        return new ReflectionEntryPointResolverType();
    }

    /**
     * Create an instance of {@link ComplexEntryPointResolverType }
     * 
     */
    public ComplexEntryPointResolverType createComplexEntryPointResolverType() {
        return new ComplexEntryPointResolverType();
    }

    /**
     * Create an instance of {@link MethodType }
     * 
     */
    public MethodType createMethodType() {
        return new MethodType();
    }

    /**
     * Create an instance of {@link AbstractServiceType }
     * 
     */
    public AbstractServiceType createAbstractServiceType() {
        return new AbstractServiceType();
    }

    /**
     * Create an instance of {@link ExceptionStrategyType }
     * 
     */
    public ExceptionStrategyType createExceptionStrategyType() {
        return new ExceptionStrategyType();
    }

    /**
     * Create an instance of {@link AbstractReconnectionStrategyType }
     * 
     */
    public AbstractReconnectionStrategyType createAbstractReconnectionStrategyType() {
        return new AbstractReconnectionStrategyType();
    }

    /**
     * Create an instance of {@link AbstractReconnectNotifierType }
     * 
     */
    public AbstractReconnectNotifierType createAbstractReconnectNotifierType() {
        return new AbstractReconnectNotifierType();
    }

    /**
     * Create an instance of {@link ReconnectSimpleStrategyType }
     * 
     */
    public ReconnectSimpleStrategyType createReconnectSimpleStrategyType() {
        return new ReconnectSimpleStrategyType();
    }

    /**
     * Create an instance of {@link ReconnectForeverStrategyType }
     * 
     */
    public ReconnectForeverStrategyType createReconnectForeverStrategyType() {
        return new ReconnectForeverStrategyType();
    }

    /**
     * Create an instance of {@link ReconnectCustomStrategyType }
     * 
     */
    public ReconnectCustomStrategyType createReconnectCustomStrategyType() {
        return new ReconnectCustomStrategyType();
    }

    /**
     * Create an instance of {@link ReconnectNotifierType }
     * 
     */
    public ReconnectNotifierType createReconnectNotifierType() {
        return new ReconnectNotifierType();
    }

    /**
     * Create an instance of {@link ReconnectCustomNotifierType }
     * 
     */
    public ReconnectCustomNotifierType createReconnectCustomNotifierType() {
        return new ReconnectCustomNotifierType();
    }

    /**
     * Create an instance of {@link AbstractAsyncReplyRouterType }
     * 
     */
    public AbstractAsyncReplyRouterType createAbstractAsyncReplyRouterType() {
        return new AbstractAsyncReplyRouterType();
    }

    /**
     * Create an instance of {@link SedaServiceType }
     * 
     */
    public SedaServiceType createSedaServiceType() {
        return new SedaServiceType();
    }

    /**
     * Create an instance of {@link CustomServiceType }
     * 
     */
    public CustomServiceType createCustomServiceType() {
        return new CustomServiceType();
    }

    /**
     * Create an instance of {@link AbstractServiceThreadingProfileType }
     * 
     */
    public AbstractServiceThreadingProfileType createAbstractServiceThreadingProfileType() {
        return new AbstractServiceThreadingProfileType();
    }

    /**
     * Create an instance of {@link AbstractFlowConstructType }
     * 
     */
    public AbstractFlowConstructType createAbstractFlowConstructType() {
        return new AbstractFlowConstructType();
    }

    /**
     * Create an instance of {@link ProcessingStrategyType }
     * 
     */
    public ProcessingStrategyType createProcessingStrategyType() {
        return new ProcessingStrategyType();
    }

    /**
     * Create an instance of {@link AsynchronousProcessingStrategy }
     * 
     */
    public AsynchronousProcessingStrategy createAsynchronousProcessingStrategy() {
        return new AsynchronousProcessingStrategy();
    }

    /**
     * Create an instance of {@link QueuedAsynchronousProcessingStrategy }
     * 
     */
    public QueuedAsynchronousProcessingStrategy createQueuedAsynchronousProcessingStrategy() {
        return new QueuedAsynchronousProcessingStrategy();
    }

    /**
     * Create an instance of {@link NonBlockingProcessingStrategy }
     * 
     */
    public NonBlockingProcessingStrategy createNonBlockingProcessingStrategy() {
        return new NonBlockingProcessingStrategy();
    }

    /**
     * Create an instance of {@link CustomProcessingStrategy }
     * 
     */
    public CustomProcessingStrategy createCustomProcessingStrategy() {
        return new CustomProcessingStrategy();
    }

    /**
     * Create an instance of {@link FlowRef }
     * 
     */
    public FlowRef createFlowRef() {
        return new FlowRef();
    }

    /**
     * Create an instance of {@link SimpleServiceType }
     * 
     */
    public SimpleServiceType createSimpleServiceType() {
        return new SimpleServiceType();
    }

    /**
     * Create an instance of {@link BridgeType }
     * 
     */
    public BridgeType createBridgeType() {
        return new BridgeType();
    }

    /**
     * Create an instance of {@link ValidatorType }
     * 
     */
    public ValidatorType createValidatorType() {
        return new ValidatorType();
    }

    /**
     * Create an instance of {@link AbstractComponentType }
     * 
     */
    public AbstractComponentType createAbstractComponentType() {
        return new AbstractComponentType();
    }

    /**
     * Create an instance of {@link DefaultJavaComponentType }
     * 
     */
    public DefaultJavaComponentType createDefaultJavaComponentType() {
        return new DefaultJavaComponentType();
    }

    /**
     * Create an instance of {@link PooledJavaComponentType }
     * 
     */
    public PooledJavaComponentType createPooledJavaComponentType() {
        return new PooledJavaComponentType();
    }

    /**
     * Create an instance of {@link DefaultComponentType }
     * 
     */
    public DefaultComponentType createDefaultComponentType() {
        return new DefaultComponentType();
    }

    /**
     * Create an instance of {@link AbstractTransactionType }
     * 
     */
    public AbstractTransactionType createAbstractTransactionType() {
        return new AbstractTransactionType();
    }

    /**
     * Create an instance of {@link CustomTransactionType }
     * 
     */
    public CustomTransactionType createCustomTransactionType() {
        return new CustomTransactionType();
    }

    /**
     * Create an instance of {@link XaTransactionType }
     * 
     */
    public XaTransactionType createXaTransactionType() {
        return new XaTransactionType();
    }

    /**
     * Create an instance of {@link TransactionManagerType }
     * 
     */
    public TransactionManagerType createTransactionManagerType() {
        return new TransactionManagerType();
    }

    /**
     * Create an instance of {@link JndiTransactionManagerType }
     * 
     */
    public JndiTransactionManagerType createJndiTransactionManagerType() {
        return new JndiTransactionManagerType();
    }

    /**
     * Create an instance of {@link JndiTransactionManager }
     * 
     */
    public JndiTransactionManager createJndiTransactionManager() {
        return new JndiTransactionManager();
    }

    /**
     * Create an instance of {@link CustomTransactionManagerType }
     * 
     */
    public CustomTransactionManagerType createCustomTransactionManagerType() {
        return new CustomTransactionManagerType();
    }

    /**
     * Create an instance of {@link GlobalEndpointType }
     * 
     */
    public GlobalEndpointType createGlobalEndpointType() {
        return new GlobalEndpointType();
    }

    /**
     * Create an instance of {@link InboundEndpointType }
     * 
     */
    public InboundEndpointType createInboundEndpointType() {
        return new InboundEndpointType();
    }

    /**
     * Create an instance of {@link AbstractSchedulerType }
     * 
     */
    public AbstractSchedulerType createAbstractSchedulerType() {
        return new AbstractSchedulerType();
    }

    /**
     * Create an instance of {@link FixedSchedulerType }
     * 
     */
    public FixedSchedulerType createFixedSchedulerType() {
        return new FixedSchedulerType();
    }

    /**
     * Create an instance of {@link PollInboundEndpointType }
     * 
     */
    public PollInboundEndpointType createPollInboundEndpointType() {
        return new PollInboundEndpointType();
    }

    /**
     * Create an instance of {@link OutboundEndpointType }
     * 
     */
    public OutboundEndpointType createOutboundEndpointType() {
        return new OutboundEndpointType();
    }

    /**
     * Create an instance of {@link org.mulesoft.schema.mule.core.Response }
     * 
     */
    public org.mulesoft.schema.mule.core.Response createResponse() {
        return new org.mulesoft.schema.mule.core.Response();
    }

    /**
     * Create an instance of {@link AbstractSecurityFilterType }
     * 
     */
    public AbstractSecurityFilterType createAbstractSecurityFilterType() {
        return new AbstractSecurityFilterType();
    }

    /**
     * Create an instance of {@link AbstractInterceptingMessageProcessorType }
     * 
     */
    public AbstractInterceptingMessageProcessorType createAbstractInterceptingMessageProcessorType() {
        return new AbstractInterceptingMessageProcessorType();
    }

    /**
     * Create an instance of {@link UsernamePasswordFilterType }
     * 
     */
    public UsernamePasswordFilterType createUsernamePasswordFilterType() {
        return new UsernamePasswordFilterType();
    }

    /**
     * Create an instance of {@link CustomSecurityFilterType }
     * 
     */
    public CustomSecurityFilterType createCustomSecurityFilterType() {
        return new CustomSecurityFilterType();
    }

    /**
     * Create an instance of {@link RefFilterType }
     * 
     */
    public RefFilterType createRefFilterType() {
        return new RefFilterType();
    }

    /**
     * Create an instance of {@link UnitaryFilterType }
     * 
     */
    public UnitaryFilterType createUnitaryFilterType() {
        return new UnitaryFilterType();
    }

    /**
     * Create an instance of {@link CollectionFilterType }
     * 
     */
    public CollectionFilterType createCollectionFilterType() {
        return new CollectionFilterType();
    }

    /**
     * Create an instance of {@link WildcardFilterType }
     * 
     */
    public WildcardFilterType createWildcardFilterType() {
        return new WildcardFilterType();
    }

    /**
     * Create an instance of {@link ExpressionFilterType }
     * 
     */
    public ExpressionFilterType createExpressionFilterType() {
        return new ExpressionFilterType();
    }

    /**
     * Create an instance of {@link RegexFilterType }
     * 
     */
    public RegexFilterType createRegexFilterType() {
        return new RegexFilterType();
    }

    /**
     * Create an instance of {@link ScopedPropertyFilterType }
     * 
     */
    public ScopedPropertyFilterType createScopedPropertyFilterType() {
        return new ScopedPropertyFilterType();
    }

    /**
     * Create an instance of {@link TypeFilterType }
     * 
     */
    public TypeFilterType createTypeFilterType() {
        return new TypeFilterType();
    }

    /**
     * Create an instance of {@link CustomFilterType }
     * 
     */
    public CustomFilterType createCustomFilterType() {
        return new CustomFilterType();
    }

    /**
     * Create an instance of {@link AbstractInterceptorStackType }
     * 
     */
    public AbstractInterceptorStackType createAbstractInterceptorStackType() {
        return new AbstractInterceptorStackType();
    }

    /**
     * Create an instance of {@link AbstractInterceptorType }
     * 
     */
    public AbstractInterceptorType createAbstractInterceptorType() {
        return new AbstractInterceptorType();
    }

    /**
     * Create an instance of {@link CustomInterceptorType }
     * 
     */
    public CustomInterceptorType createCustomInterceptorType() {
        return new CustomInterceptorType();
    }

    /**
     * Create an instance of {@link EncryptionSecurityFilterType }
     * 
     */
    public EncryptionSecurityFilterType createEncryptionSecurityFilterType() {
        return new EncryptionSecurityFilterType();
    }

    /**
     * Create an instance of {@link RefTransformerType }
     * 
     */
    public RefTransformerType createRefTransformerType() {
        return new RefTransformerType();
    }

    /**
     * Create an instance of {@link AbstractTransformerType }
     * 
     */
    public AbstractTransformerType createAbstractTransformerType() {
        return new AbstractTransformerType();
    }

    /**
     * Create an instance of {@link CustomTransformerType }
     * 
     */
    public CustomTransformerType createCustomTransformerType() {
        return new CustomTransformerType();
    }

    /**
     * Create an instance of {@link MessagePropertiesTransformerType }
     * 
     */
    public MessagePropertiesTransformerType createMessagePropertiesTransformerType() {
        return new MessagePropertiesTransformerType();
    }

    /**
     * Create an instance of {@link SetPropertyType }
     * 
     */
    public SetPropertyType createSetPropertyType() {
        return new SetPropertyType();
    }

    /**
     * Create an instance of {@link RemovePropertyType }
     * 
     */
    public RemovePropertyType createRemovePropertyType() {
        return new RemovePropertyType();
    }

    /**
     * Create an instance of {@link CopyPropertiesType }
     * 
     */
    public CopyPropertiesType createCopyPropertiesType() {
        return new CopyPropertiesType();
    }

    /**
     * Create an instance of {@link SetVariableType }
     * 
     */
    public SetVariableType createSetVariableType() {
        return new SetVariableType();
    }

    /**
     * Create an instance of {@link RemoveVariableType }
     * 
     */
    public RemoveVariableType createRemoveVariableType() {
        return new RemoveVariableType();
    }

    /**
     * Create an instance of {@link SetAttachmentType }
     * 
     */
    public SetAttachmentType createSetAttachmentType() {
        return new SetAttachmentType();
    }

    /**
     * Create an instance of {@link RemoveAttachmentType }
     * 
     */
    public RemoveAttachmentType createRemoveAttachmentType() {
        return new RemoveAttachmentType();
    }

    /**
     * Create an instance of {@link CopyAttachmentType }
     * 
     */
    public CopyAttachmentType createCopyAttachmentType() {
        return new CopyAttachmentType();
    }

    /**
     * Create an instance of {@link AppendStringTransformerType }
     * 
     */
    public AppendStringTransformerType createAppendStringTransformerType() {
        return new AppendStringTransformerType();
    }

    /**
     * Create an instance of {@link ParseTemplateTransformerType }
     * 
     */
    public ParseTemplateTransformerType createParseTemplateTransformerType() {
        return new ParseTemplateTransformerType();
    }

    /**
     * Create an instance of {@link SetPayloadTransformerType }
     * 
     */
    public SetPayloadTransformerType createSetPayloadTransformerType() {
        return new SetPayloadTransformerType();
    }

    /**
     * Create an instance of {@link EncryptionTransformerType }
     * 
     */
    public EncryptionTransformerType createEncryptionTransformerType() {
        return new EncryptionTransformerType();
    }

    /**
     * Create an instance of {@link ValueExtractorTransformerType }
     * 
     */
    public ValueExtractorTransformerType createValueExtractorTransformerType() {
        return new ValueExtractorTransformerType();
    }

    /**
     * Create an instance of {@link QueueProfileType }
     * 
     */
    public QueueProfileType createQueueProfileType() {
        return new QueueProfileType();
    }

    /**
     * Create an instance of {@link AbstractSecurityManagerType }
     * 
     */
    public AbstractSecurityManagerType createAbstractSecurityManagerType() {
        return new AbstractSecurityManagerType();
    }

    /**
     * Create an instance of {@link SecurityManagerType }
     * 
     */
    public SecurityManagerType createSecurityManagerType() {
        return new SecurityManagerType();
    }

    /**
     * Create an instance of {@link ThreadingProfileType }
     * 
     */
    public ThreadingProfileType createThreadingProfileType() {
        return new ThreadingProfileType();
    }

    /**
     * Create an instance of {@link ServiceExceptionStrategyType }
     * 
     */
    public ServiceExceptionStrategyType createServiceExceptionStrategyType() {
        return new ServiceExceptionStrategyType();
    }

    /**
     * Create an instance of {@link CatchExceptionStrategyType }
     * 
     */
    public CatchExceptionStrategyType createCatchExceptionStrategyType() {
        return new CatchExceptionStrategyType();
    }

    /**
     * Create an instance of {@link ChoiceExceptionStrategyType }
     * 
     */
    public ChoiceExceptionStrategyType createChoiceExceptionStrategyType() {
        return new ChoiceExceptionStrategyType();
    }

    /**
     * Create an instance of {@link ReferenceExceptionStrategyType }
     * 
     */
    public ReferenceExceptionStrategyType createReferenceExceptionStrategyType() {
        return new ReferenceExceptionStrategyType();
    }

    /**
     * Create an instance of {@link CustomExceptionStrategyType }
     * 
     */
    public CustomExceptionStrategyType createCustomExceptionStrategyType() {
        return new CustomExceptionStrategyType();
    }

    /**
     * Create an instance of {@link CustomConnectorType }
     * 
     */
    public CustomConnectorType createCustomConnectorType() {
        return new CustomConnectorType();
    }

    /**
     * Create an instance of {@link AbstractObjectStoreType }
     * 
     */
    public AbstractObjectStoreType createAbstractObjectStoreType() {
        return new AbstractObjectStoreType();
    }

    /**
     * Create an instance of {@link AbstractMonitoredObjectStoreType }
     * 
     */
    public AbstractMonitoredObjectStoreType createAbstractMonitoredObjectStoreType() {
        return new AbstractMonitoredObjectStoreType();
    }

    /**
     * Create an instance of {@link TextFileObjectStoreType }
     * 
     */
    public TextFileObjectStoreType createTextFileObjectStoreType() {
        return new TextFileObjectStoreType();
    }

    /**
     * Create an instance of {@link ManagedObjectStoreType }
     * 
     */
    public ManagedObjectStoreType createManagedObjectStoreType() {
        return new ManagedObjectStoreType();
    }

    /**
     * Create an instance of {@link CustomObjectStoreType }
     * 
     */
    public CustomObjectStoreType createCustomObjectStoreType() {
        return new CustomObjectStoreType();
    }

    /**
     * Create an instance of {@link SpringObjectStoreType }
     * 
     */
    public SpringObjectStoreType createSpringObjectStoreType() {
        return new SpringObjectStoreType();
    }

    /**
     * Create an instance of {@link AbstractGlobalInterceptingMessageProcessorType }
     * 
     */
    public AbstractGlobalInterceptingMessageProcessorType createAbstractGlobalInterceptingMessageProcessorType() {
        return new AbstractGlobalInterceptingMessageProcessorType();
    }

    /**
     * Create an instance of {@link MessageFilterType }
     * 
     */
    public MessageFilterType createMessageFilterType() {
        return new MessageFilterType();
    }

    /**
     * Create an instance of {@link IdempotentMessageFilterType }
     * 
     */
    public IdempotentMessageFilterType createIdempotentMessageFilterType() {
        return new IdempotentMessageFilterType();
    }

    /**
     * Create an instance of {@link AbstractRedeliveryPolicyType }
     * 
     */
    public AbstractRedeliveryPolicyType createAbstractRedeliveryPolicyType() {
        return new AbstractRedeliveryPolicyType();
    }

    /**
     * Create an instance of {@link IdempotentRedeliveryPolicyType }
     * 
     */
    public IdempotentRedeliveryPolicyType createIdempotentRedeliveryPolicyType() {
        return new IdempotentRedeliveryPolicyType();
    }

    /**
     * Create an instance of {@link IdempotentSecureHashMessageFilter }
     * 
     */
    public IdempotentSecureHashMessageFilter createIdempotentSecureHashMessageFilter() {
        return new IdempotentSecureHashMessageFilter();
    }

    /**
     * Create an instance of {@link WireTap }
     * 
     */
    public WireTap createWireTap() {
        return new WireTap();
    }

    /**
     * Create an instance of {@link CombineCollectionsTransformer }
     * 
     */
    public CombineCollectionsTransformer createCombineCollectionsTransformer() {
        return new CombineCollectionsTransformer();
    }

    /**
     * Create an instance of {@link BaseAggregatorType }
     * 
     */
    public BaseAggregatorType createBaseAggregatorType() {
        return new BaseAggregatorType();
    }

    /**
     * Create an instance of {@link CustomAggregator }
     * 
     */
    public CustomAggregator createCustomAggregator() {
        return new CustomAggregator();
    }

    /**
     * Create an instance of {@link Splitter }
     * 
     */
    public Splitter createSplitter() {
        return new Splitter();
    }

    /**
     * Create an instance of {@link CollectionSplitter }
     * 
     */
    public CollectionSplitter createCollectionSplitter() {
        return new CollectionSplitter();
    }

    /**
     * Create an instance of {@link MapSplitter }
     * 
     */
    public MapSplitter createMapSplitter() {
        return new MapSplitter();
    }

    /**
     * Create an instance of {@link MessageChunkSplitter }
     * 
     */
    public MessageChunkSplitter createMessageChunkSplitter() {
        return new MessageChunkSplitter();
    }

    /**
     * Create an instance of {@link CustomSplitter }
     * 
     */
    public CustomSplitter createCustomSplitter() {
        return new CustomSplitter();
    }

    /**
     * Create an instance of {@link ForeachProcessorType }
     * 
     */
    public ForeachProcessorType createForeachProcessorType() {
        return new ForeachProcessorType();
    }

    /**
     * Create an instance of {@link AbstractRoutingMessageProcessorType }
     * 
     */
    public AbstractRoutingMessageProcessorType createAbstractRoutingMessageProcessorType() {
        return new AbstractRoutingMessageProcessorType();
    }

    /**
     * Create an instance of {@link CustomRouter }
     * 
     */
    public CustomRouter createCustomRouter() {
        return new CustomRouter();
    }

    /**
     * Create an instance of {@link SelectiveOutboundRouterType }
     * 
     */
    public SelectiveOutboundRouterType createSelectiveOutboundRouterType() {
        return new SelectiveOutboundRouterType();
    }

    /**
     * Create an instance of {@link BaseMultipleRoutesRoutingMessageProcessorType }
     * 
     */
    public BaseMultipleRoutesRoutingMessageProcessorType createBaseMultipleRoutesRoutingMessageProcessorType() {
        return new BaseMultipleRoutesRoutingMessageProcessorType();
    }

    /**
     * Create an instance of {@link FirstSuccessful }
     * 
     */
    public FirstSuccessful createFirstSuccessful() {
        return new FirstSuccessful();
    }

    /**
     * Create an instance of {@link UntilSuccessful }
     * 
     */
    public UntilSuccessful createUntilSuccessful() {
        return new UntilSuccessful();
    }

    /**
     * Create an instance of {@link ScatterGather }
     * 
     */
    public ScatterGather createScatterGather() {
        return new ScatterGather();
    }

    /**
     * Create an instance of {@link ProcessorWithAtLeastOneTargetType }
     * 
     */
    public ProcessorWithAtLeastOneTargetType createProcessorWithAtLeastOneTargetType() {
        return new ProcessorWithAtLeastOneTargetType();
    }

    /**
     * Create an instance of {@link RecipientList }
     * 
     */
    public RecipientList createRecipientList() {
        return new RecipientList();
    }

    /**
     * Create an instance of {@link DynamicRoundRobin }
     * 
     */
    public DynamicRoundRobin createDynamicRoundRobin() {
        return new DynamicRoundRobin();
    }

    /**
     * Create an instance of {@link DynamicAll }
     * 
     */
    public DynamicAll createDynamicAll() {
        return new DynamicAll();
    }

    /**
     * Create an instance of {@link DynamicFirstSuccessful }
     * 
     */
    public DynamicFirstSuccessful createDynamicFirstSuccessful() {
        return new DynamicFirstSuccessful();
    }

    /**
     * Create an instance of {@link CustomRouterResolverType }
     * 
     */
    public CustomRouterResolverType createCustomRouterResolverType() {
        return new CustomRouterResolverType();
    }

    /**
     * Create an instance of {@link IdempotentReceiverType }
     * 
     */
    public IdempotentReceiverType createIdempotentReceiverType() {
        return new IdempotentReceiverType();
    }

    /**
     * Create an instance of {@link IdempotentSecureHashReceiverType }
     * 
     */
    public IdempotentSecureHashReceiverType createIdempotentSecureHashReceiverType() {
        return new IdempotentSecureHashReceiverType();
    }

    /**
     * Create an instance of {@link WireTapRouterType }
     * 
     */
    public WireTapRouterType createWireTapRouterType() {
        return new WireTapRouterType();
    }

    /**
     * Create an instance of {@link FilteredInboundRouterType }
     * 
     */
    public FilteredInboundRouterType createFilteredInboundRouterType() {
        return new FilteredInboundRouterType();
    }

    /**
     * Create an instance of {@link BaseAggregatorRouterType }
     * 
     */
    public BaseAggregatorRouterType createBaseAggregatorRouterType() {
        return new BaseAggregatorRouterType();
    }

    /**
     * Create an instance of {@link MessageChunkingAggregatorRouterType }
     * 
     */
    public MessageChunkingAggregatorRouterType createMessageChunkingAggregatorRouterType() {
        return new MessageChunkingAggregatorRouterType();
    }

    /**
     * Create an instance of {@link CustomCorrelationAggregatorRouterType }
     * 
     */
    public CustomCorrelationAggregatorRouterType createCustomCorrelationAggregatorRouterType() {
        return new CustomCorrelationAggregatorRouterType();
    }

    /**
     * Create an instance of {@link CustomInboundRouterType }
     * 
     */
    public CustomInboundRouterType createCustomInboundRouterType() {
        return new CustomInboundRouterType();
    }

    /**
     * Create an instance of {@link AsyncReplyRouterType }
     * 
     */
    public AsyncReplyRouterType createAsyncReplyRouterType() {
        return new AsyncReplyRouterType();
    }

    /**
     * Create an instance of {@link CustomAsyncReplyRouterType }
     * 
     */
    public CustomAsyncReplyRouterType createCustomAsyncReplyRouterType() {
        return new CustomAsyncReplyRouterType();
    }

    /**
     * Create an instance of {@link OutboundRouterType }
     * 
     */
    public OutboundRouterType createOutboundRouterType() {
        return new OutboundRouterType();
    }

    /**
     * Create an instance of {@link SingleEndpointFilteringOutboundRouterType }
     * 
     */
    public SingleEndpointFilteringOutboundRouterType createSingleEndpointFilteringOutboundRouterType() {
        return new SingleEndpointFilteringOutboundRouterType();
    }

    /**
     * Create an instance of {@link MultipleEndpointFilteringOutboundRouterType }
     * 
     */
    public MultipleEndpointFilteringOutboundRouterType createMultipleEndpointFilteringOutboundRouterType() {
        return new MultipleEndpointFilteringOutboundRouterType();
    }

    /**
     * Create an instance of {@link ExpressionRecipientListRouterType }
     * 
     */
    public ExpressionRecipientListRouterType createExpressionRecipientListRouterType() {
        return new ExpressionRecipientListRouterType();
    }

    /**
     * Create an instance of {@link EndpointSelectorRouterType }
     * 
     */
    public EndpointSelectorRouterType createEndpointSelectorRouterType() {
        return new EndpointSelectorRouterType();
    }

    /**
     * Create an instance of {@link RoundRobinSplitterType }
     * 
     */
    public RoundRobinSplitterType createRoundRobinSplitterType() {
        return new RoundRobinSplitterType();
    }

    /**
     * Create an instance of {@link ExpressionSplitterOutboundRouterType }
     * 
     */
    public ExpressionSplitterOutboundRouterType createExpressionSplitterOutboundRouterType() {
        return new ExpressionSplitterOutboundRouterType();
    }

    /**
     * Create an instance of {@link ChunkingRouterType }
     * 
     */
    public ChunkingRouterType createChunkingRouterType() {
        return new ChunkingRouterType();
    }

    /**
     * Create an instance of {@link StaticRecipientListRouterType }
     * 
     */
    public StaticRecipientListRouterType createStaticRecipientListRouterType() {
        return new StaticRecipientListRouterType();
    }

    /**
     * Create an instance of {@link ExpressionOrStaticRecipientListRouterType }
     * 
     */
    public ExpressionOrStaticRecipientListRouterType createExpressionOrStaticRecipientListRouterType() {
        return new ExpressionOrStaticRecipientListRouterType();
    }

    /**
     * Create an instance of {@link CustomOutboundRouterType }
     * 
     */
    public CustomOutboundRouterType createCustomOutboundRouterType() {
        return new CustomOutboundRouterType();
    }

    /**
     * Create an instance of {@link LoggingCatchAllStrategyType }
     * 
     */
    public LoggingCatchAllStrategyType createLoggingCatchAllStrategyType() {
        return new LoggingCatchAllStrategyType();
    }

    /**
     * Create an instance of {@link CustomCatchAllStrategyType }
     * 
     */
    public CustomCatchAllStrategyType createCustomCatchAllStrategyType() {
        return new CustomCatchAllStrategyType();
    }

    /**
     * Create an instance of {@link ForwardingCatchAllStrategyType }
     * 
     */
    public ForwardingCatchAllStrategyType createForwardingCatchAllStrategyType() {
        return new ForwardingCatchAllStrategyType();
    }

    /**
     * Create an instance of {@link CustomForwardingCatchAllStrategyType }
     * 
     */
    public CustomForwardingCatchAllStrategyType createCustomForwardingCatchAllStrategyType() {
        return new CustomForwardingCatchAllStrategyType();
    }

    /**
     * Create an instance of {@link AbstractMessageInfoMappingType }
     * 
     */
    public AbstractMessageInfoMappingType createAbstractMessageInfoMappingType() {
        return new AbstractMessageInfoMappingType();
    }

    /**
     * Create an instance of {@link ExpressionMessageInfoMappingType }
     * 
     */
    public ExpressionMessageInfoMappingType createExpressionMessageInfoMappingType() {
        return new ExpressionMessageInfoMappingType();
    }

    /**
     * Create an instance of {@link CustomMessageInfoMappingType }
     * 
     */
    public CustomMessageInfoMappingType createCustomMessageInfoMappingType() {
        return new CustomMessageInfoMappingType();
    }

    /**
     * Create an instance of {@link AbstractLifecycleAdapterFactory }
     * 
     */
    public AbstractLifecycleAdapterFactory createAbstractLifecycleAdapterFactory() {
        return new AbstractLifecycleAdapterFactory();
    }

    /**
     * Create an instance of {@link CustomLifecycleAdapterFactory }
     * 
     */
    public CustomLifecycleAdapterFactory createCustomLifecycleAdapterFactory() {
        return new CustomLifecycleAdapterFactory();
    }

    /**
     * Create an instance of {@link AbstractObjectFactoryType }
     * 
     */
    public AbstractObjectFactoryType createAbstractObjectFactoryType() {
        return new AbstractObjectFactoryType();
    }

    /**
     * Create an instance of {@link SpringBeanLookupType }
     * 
     */
    public SpringBeanLookupType createSpringBeanLookupType() {
        return new SpringBeanLookupType();
    }

    /**
     * Create an instance of {@link SingletonObjectFactoryType }
     * 
     */
    public SingletonObjectFactoryType createSingletonObjectFactoryType() {
        return new SingletonObjectFactoryType();
    }

    /**
     * Create an instance of {@link PrototypeObjectFactoryType }
     * 
     */
    public PrototypeObjectFactoryType createPrototypeObjectFactoryType() {
        return new PrototypeObjectFactoryType();
    }

    /**
     * Create an instance of {@link AbstractPoolingProfileType }
     * 
     */
    public AbstractPoolingProfileType createAbstractPoolingProfileType() {
        return new AbstractPoolingProfileType();
    }

    /**
     * Create an instance of {@link PoolingProfileType }
     * 
     */
    public PoolingProfileType createPoolingProfileType() {
        return new PoolingProfileType();
    }

    /**
     * Create an instance of {@link MapType }
     * 
     */
    public MapType createMapType() {
        return new MapType();
    }

    /**
     * Create an instance of {@link AbstractCachingStrategyType }
     * 
     */
    public AbstractCachingStrategyType createAbstractCachingStrategyType() {
        return new AbstractCachingStrategyType();
    }

    /**
     * Create an instance of {@link AnnotationsType }
     * 
     */
    public AnnotationsType createAnnotationsType() {
        return new AnnotationsType();
    }

    /**
     * Create an instance of {@link DescriptionType }
     * 
     */
    public DescriptionType createDescriptionType() {
        return new DescriptionType();
    }

    /**
     * Create an instance of {@link ExtractEnrichPairType }
     * 
     */
    public ExtractEnrichPairType createExtractEnrichPairType() {
        return new ExtractEnrichPairType();
    }

    /**
     * Create an instance of {@link InboundCollectionType }
     * 
     */
    public InboundCollectionType createInboundCollectionType() {
        return new InboundCollectionType();
    }

    /**
     * Create an instance of {@link AbstractFilterType }
     * 
     */
    public AbstractFilterType createAbstractFilterType() {
        return new AbstractFilterType();
    }

    /**
     * Create an instance of {@link OutboundCollectionType }
     * 
     */
    public OutboundCollectionType createOutboundCollectionType() {
        return new OutboundCollectionType();
    }

    /**
     * Create an instance of {@link DefineNotificationType }
     * 
     */
    public DefineNotificationType createDefineNotificationType() {
        return new DefineNotificationType();
    }

    /**
     * Create an instance of {@link DisableNotificationType }
     * 
     */
    public DisableNotificationType createDisableNotificationType() {
        return new DisableNotificationType();
    }

    /**
     * Create an instance of {@link BaseEntryPointResolverType }
     * 
     */
    public BaseEntryPointResolverType createBaseEntryPointResolverType() {
        return new BaseEntryPointResolverType();
    }

    /**
     * Create an instance of {@link BaseServiceType }
     * 
     */
    public BaseServiceType createBaseServiceType() {
        return new BaseServiceType();
    }

    /**
     * Create an instance of {@link AbstractNotificationExceptionStrategyType }
     * 
     */
    public AbstractNotificationExceptionStrategyType createAbstractNotificationExceptionStrategyType() {
        return new AbstractNotificationExceptionStrategyType();
    }

    /**
     * Create an instance of {@link AbstractExceptionStrategyType }
     * 
     */
    public AbstractExceptionStrategyType createAbstractExceptionStrategyType() {
        return new AbstractExceptionStrategyType();
    }

    /**
     * Create an instance of {@link ExceptionPatternType }
     * 
     */
    public ExceptionPatternType createExceptionPatternType() {
        return new ExceptionPatternType();
    }

    /**
     * Create an instance of {@link AsyncReplyCollectionType }
     * 
     */
    public AsyncReplyCollectionType createAsyncReplyCollectionType() {
        return new AsyncReplyCollectionType();
    }

    /**
     * Create an instance of {@link BaseFlowConstructType }
     * 
     */
    public BaseFlowConstructType createBaseFlowConstructType() {
        return new BaseFlowConstructType();
    }

    /**
     * Create an instance of {@link PojoBindingType }
     * 
     */
    public PojoBindingType createPojoBindingType() {
        return new PojoBindingType();
    }

    /**
     * Create an instance of {@link BaseTransactionType }
     * 
     */
    public BaseTransactionType createBaseTransactionType() {
        return new BaseTransactionType();
    }

    /**
     * Create an instance of {@link GlobalEndpointTypeWithoutExchangePattern }
     * 
     */
    public GlobalEndpointTypeWithoutExchangePattern createGlobalEndpointTypeWithoutExchangePattern() {
        return new GlobalEndpointTypeWithoutExchangePattern();
    }

    /**
     * Create an instance of {@link GlobalEndpointWithXaType }
     * 
     */
    public GlobalEndpointWithXaType createGlobalEndpointWithXaType() {
        return new GlobalEndpointWithXaType();
    }

    /**
     * Create an instance of {@link InboundEndpointTypeWithoutExchangePattern }
     * 
     */
    public InboundEndpointTypeWithoutExchangePattern createInboundEndpointTypeWithoutExchangePattern() {
        return new InboundEndpointTypeWithoutExchangePattern();
    }

    /**
     * Create an instance of {@link InboundEndpointWithXaType }
     * 
     */
    public InboundEndpointWithXaType createInboundEndpointWithXaType() {
        return new InboundEndpointWithXaType();
    }

    /**
     * Create an instance of {@link OutboundEndpointTypeWithoutExchangePattern }
     * 
     */
    public OutboundEndpointTypeWithoutExchangePattern createOutboundEndpointTypeWithoutExchangePattern() {
        return new OutboundEndpointTypeWithoutExchangePattern();
    }

    /**
     * Create an instance of {@link OutboundEndpointWithXaType }
     * 
     */
    public OutboundEndpointWithXaType createOutboundEndpointWithXaType() {
        return new OutboundEndpointWithXaType();
    }

    /**
     * Create an instance of {@link EndpointRefType }
     * 
     */
    public EndpointRefType createEndpointRefType() {
        return new EndpointRefType();
    }

    /**
     * Create an instance of {@link PatternFilterType }
     * 
     */
    public PatternFilterType createPatternFilterType() {
        return new PatternFilterType();
    }

    /**
     * Create an instance of {@link CaseSensitivePatternFilterType }
     * 
     */
    public CaseSensitivePatternFilterType createCaseSensitivePatternFilterType() {
        return new CaseSensitivePatternFilterType();
    }

    /**
     * Create an instance of {@link RefInterceptorStackType }
     * 
     */
    public RefInterceptorStackType createRefInterceptorStackType() {
        return new RefInterceptorStackType();
    }

    /**
     * Create an instance of {@link AbstractAddPropertyTransformerType }
     * 
     */
    public AbstractAddPropertyTransformerType createAbstractAddPropertyTransformerType() {
        return new AbstractAddPropertyTransformerType();
    }

    /**
     * Create an instance of {@link CommonMessagePartTransformerType }
     * 
     */
    public CommonMessagePartTransformerType createCommonMessagePartTransformerType() {
        return new CommonMessagePartTransformerType();
    }

    /**
     * Create an instance of {@link ExtractValueExtractorTemplateType }
     * 
     */
    public ExtractValueExtractorTemplateType createExtractValueExtractorTemplateType() {
        return new ExtractValueExtractorTemplateType();
    }

    /**
     * Create an instance of {@link SecurityProviderType }
     * 
     */
    public SecurityProviderType createSecurityProviderType() {
        return new SecurityProviderType();
    }

    /**
     * Create an instance of {@link EncryptionStrategyType }
     * 
     */
    public EncryptionStrategyType createEncryptionStrategyType() {
        return new EncryptionStrategyType();
    }

    /**
     * Create an instance of {@link CustomSecurityProviderType }
     * 
     */
    public CustomSecurityProviderType createCustomSecurityProviderType() {
        return new CustomSecurityProviderType();
    }

    /**
     * Create an instance of {@link CustomEncryptionStrategyType }
     * 
     */
    public CustomEncryptionStrategyType createCustomEncryptionStrategyType() {
        return new CustomEncryptionStrategyType();
    }

    /**
     * Create an instance of {@link PasswordEncryptionStrategyType }
     * 
     */
    public PasswordEncryptionStrategyType createPasswordEncryptionStrategyType() {
        return new PasswordEncryptionStrategyType();
    }

    /**
     * Create an instance of {@link SecretKeyEncryptionStrategyType }
     * 
     */
    public SecretKeyEncryptionStrategyType createSecretKeyEncryptionStrategyType() {
        return new SecretKeyEncryptionStrategyType();
    }

    /**
     * Create an instance of {@link AsynchronousThreadingProfileType }
     * 
     */
    public AsynchronousThreadingProfileType createAsynchronousThreadingProfileType() {
        return new AsynchronousThreadingProfileType();
    }

    /**
     * Create an instance of {@link BaseMessageFilterType }
     * 
     */
    public BaseMessageFilterType createBaseMessageFilterType() {
        return new BaseMessageFilterType();
    }

    /**
     * Create an instance of {@link SingleTarget }
     * 
     */
    public SingleTarget createSingleTarget() {
        return new SingleTarget();
    }

    /**
     * Create an instance of {@link BaseSplitterType }
     * 
     */
    public BaseSplitterType createBaseSplitterType() {
        return new BaseSplitterType();
    }

    /**
     * Create an instance of {@link WhenMessageProcessorFilterPairType }
     * 
     */
    public WhenMessageProcessorFilterPairType createWhenMessageProcessorFilterPairType() {
        return new WhenMessageProcessorFilterPairType();
    }

    /**
     * Create an instance of {@link OtherwiseMessageProcessorFilterPairType }
     * 
     */
    public OtherwiseMessageProcessorFilterPairType createOtherwiseMessageProcessorFilterPairType() {
        return new OtherwiseMessageProcessorFilterPairType();
    }

    /**
     * Create an instance of {@link BaseSingleRouteRoutingMessageProcessorType }
     * 
     */
    public BaseSingleRouteRoutingMessageProcessorType createBaseSingleRouteRoutingMessageProcessorType() {
        return new BaseSingleRouteRoutingMessageProcessorType();
    }

    /**
     * Create an instance of {@link ProcessorWithExactlyOneTargetType }
     * 
     */
    public ProcessorWithExactlyOneTargetType createProcessorWithExactlyOneTargetType() {
        return new ProcessorWithExactlyOneTargetType();
    }

    /**
     * Create an instance of {@link AggregationStrategyType }
     * 
     */
    public AggregationStrategyType createAggregationStrategyType() {
        return new AggregationStrategyType();
    }

    /**
     * Create an instance of {@link AbstractDynamicRoutingMessageProcessor }
     * 
     */
    public AbstractDynamicRoutingMessageProcessor createAbstractDynamicRoutingMessageProcessor() {
        return new AbstractDynamicRoutingMessageProcessor();
    }

    /**
     * Create an instance of {@link FilteringOutboundRouterType }
     * 
     */
    public FilteringOutboundRouterType createFilteringOutboundRouterType() {
        return new FilteringOutboundRouterType();
    }

    /**
     * Create an instance of {@link MessageSplitterOutboundRouterType }
     * 
     */
    public MessageSplitterOutboundRouterType createMessageSplitterOutboundRouterType() {
        return new MessageSplitterOutboundRouterType();
    }

    /**
     * Create an instance of {@link GenericObjectFactoryType }
     * 
     */
    public GenericObjectFactoryType createGenericObjectFactoryType() {
        return new GenericObjectFactoryType();
    }

    /**
     * Create an instance of {@link TlsClientKeyStoreType }
     * 
     */
    public TlsClientKeyStoreType createTlsClientKeyStoreType() {
        return new TlsClientKeyStoreType();
    }

    /**
     * Create an instance of {@link TlsKeyStoreType }
     * 
     */
    public TlsKeyStoreType createTlsKeyStoreType() {
        return new TlsKeyStoreType();
    }

    /**
     * Create an instance of {@link TlsTrustStoreType }
     * 
     */
    public TlsTrustStoreType createTlsTrustStoreType() {
        return new TlsTrustStoreType();
    }

    /**
     * Create an instance of {@link TlsServerTrustStoreType }
     * 
     */
    public TlsServerTrustStoreType createTlsServerTrustStoreType() {
        return new TlsServerTrustStoreType();
    }

    /**
     * Create an instance of {@link TlsProtocolHandler }
     * 
     */
    public TlsProtocolHandler createTlsProtocolHandler() {
        return new TlsProtocolHandler();
    }

    /**
     * Create an instance of {@link ListOrSetType }
     * 
     */
    public ListOrSetType createListOrSetType() {
        return new ListOrSetType();
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link KeyType }
     * 
     */
    public KeyType createKeyType() {
        return new KeyType();
    }

    /**
     * Create an instance of {@link NameType }
     * 
     */
    public NameType createNameType() {
        return new NameType();
    }

    /**
     * Create an instance of {@link ValueType }
     * 
     */
    public ValueType createValueType() {
        return new ValueType();
    }

    /**
     * Create an instance of {@link KeyValueType }
     * 
     */
    public KeyValueType createKeyValueType() {
        return new KeyValueType();
    }

    /**
     * Create an instance of {@link PropertyWithDataType }
     * 
     */
    public PropertyWithDataType createPropertyWithDataType() {
        return new PropertyWithDataType();
    }

    /**
     * Create an instance of {@link NameValueType }
     * 
     */
    public NameValueType createNameValueType() {
        return new NameValueType();
    }

    /**
     * Create an instance of {@link ConnectorType }
     * 
     */
    public ConnectorType createConnectorType() {
        return new ConnectorType();
    }

    /**
     * Create an instance of {@link ServiceOverridesType }
     * 
     */
    public ServiceOverridesType createServiceOverridesType() {
        return new ServiceOverridesType();
    }

    /**
     * Create an instance of {@link TransactedConnectorType }
     * 
     */
    public TransactedConnectorType createTransactedConnectorType() {
        return new TransactedConnectorType();
    }

    /**
     * Create an instance of {@link JndiConnectorType }
     * 
     */
    public JndiConnectorType createJndiConnectorType() {
        return new JndiConnectorType();
    }

    /**
     * Create an instance of {@link DataReferenceType }
     * 
     */
    public DataReferenceType createDataReferenceType() {
        return new DataReferenceType();
    }

    /**
     * Create an instance of {@link RollbackExceptionStrategyType.OnRedeliveryAttemptsExceeded }
     * 
     */
    public RollbackExceptionStrategyType.OnRedeliveryAttemptsExceeded createRollbackExceptionStrategyTypeOnRedeliveryAttemptsExceeded() {
        return new RollbackExceptionStrategyType.OnRedeliveryAttemptsExceeded();
    }

    /**
     * Create an instance of {@link ExpressionTransformerType.ReturnArgument }
     * 
     */
    public ExpressionTransformerType.ReturnArgument createExpressionTransformerTypeReturnArgument() {
        return new ExpressionTransformerType.ReturnArgument();
    }

    /**
     * Create an instance of {@link BeanBuilderTransformer.BeanProperty }
     * 
     */
    public BeanBuilderTransformer.BeanProperty createBeanBuilderTransformerBeanProperty() {
        return new BeanBuilderTransformer.BeanProperty();
    }

    /**
     * Create an instance of {@link StaticComponentType.ReturnData }
     * 
     */
    public StaticComponentType.ReturnData createStaticComponentTypeReturnData() {
        return new StaticComponentType.ReturnData();
    }

    /**
     * Create an instance of {@link FlowType.Response }
     * 
     */
    public FlowType.Response createFlowTypeResponse() {
        return new FlowType.Response();
    }

    /**
     * Create an instance of {@link NotificationManagerType.NotificationListener }
     * 
     */
    public NotificationManagerType.NotificationListener createNotificationManagerTypeNotificationListener() {
        return new NotificationManagerType.NotificationListener();
    }

    /**
     * Create an instance of {@link ConfigurationType.ExpressionLanguage }
     * 
     */
    public ConfigurationType.ExpressionLanguage createConfigurationTypeExpressionLanguage() {
        return new ConfigurationType.ExpressionLanguage();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-message-processor")
    public JAXBElement<AbstractMessageProcessorType> createAbstractMessageProcessor(AbstractMessageProcessorType value) {
        return new JAXBElement<AbstractMessageProcessorType>(_AbstractMessageProcessor_QNAME, AbstractMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MuleType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MuleType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "mule")
    public JAXBElement<MuleType> createMule(MuleType value) {
        return new JAXBElement<MuleType>(_Mule_QNAME, MuleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlobalPropertyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GlobalPropertyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "global-property")
    public JAXBElement<GlobalPropertyType> createGlobalProperty(GlobalPropertyType value) {
        return new JAXBElement<GlobalPropertyType>(_GlobalProperty_QNAME, GlobalPropertyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfigurationType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConfigurationType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "configuration")
    public JAXBElement<ConfigurationType> createConfiguration(ConfigurationType value) {
        return new JAXBElement<ConfigurationType>(_Configuration_QNAME, ConfigurationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotificationManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link NotificationManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "notifications")
    public JAXBElement<NotificationManagerType> createNotifications(NotificationManagerType value) {
        return new JAXBElement<NotificationManagerType>(_Notifications_QNAME, NotificationManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractExtensionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractExtensionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-shared-extension")
    public JAXBElement<AbstractExtensionType> createAbstractSharedExtension(AbstractExtensionType value) {
        return new JAXBElement<AbstractExtensionType>(_AbstractSharedExtension_QNAME, AbstractExtensionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractExtensionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractExtensionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-extension")
    public JAXBElement<AbstractExtensionType> createAbstractExtension(AbstractExtensionType value) {
        return new JAXBElement<AbstractExtensionType>(_AbstractExtension_QNAME, AbstractExtensionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractMixedContentExtensionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractMixedContentExtensionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-mixed-content-extension")
    public JAXBElement<AbstractMixedContentExtensionType> createAbstractMixedContentExtension(AbstractMixedContentExtensionType value) {
        return new JAXBElement<AbstractMixedContentExtensionType>(_AbstractMixedContentExtension_QNAME, AbstractMixedContentExtensionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExpressionComponent }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExpressionComponent }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "expression-component", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-mixed-content-message-processor")
    public JAXBElement<ExpressionComponent> createExpressionComponent(ExpressionComponent value) {
        return new JAXBElement<ExpressionComponent>(_ExpressionComponent_QNAME, ExpressionComponent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractMixedContentMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractMixedContentMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-mixed-content-message-processor")
    public JAXBElement<AbstractMixedContentMessageProcessorType> createAbstractMixedContentMessageProcessor(AbstractMixedContentMessageProcessorType value) {
        return new JAXBElement<AbstractMixedContentMessageProcessorType>(_AbstractMixedContentMessageProcessor_QNAME, AbstractMixedContentMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-shared-connector")
    public JAXBElement<AbstractConnectorType> createAbstractSharedConnector(AbstractConnectorType value) {
        return new JAXBElement<AbstractConnectorType>(_AbstractSharedConnector_QNAME, AbstractConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-connector")
    public JAXBElement<AbstractConnectorType> createAbstractConnector(AbstractConnectorType value) {
        return new JAXBElement<AbstractConnectorType>(_AbstractConnector_QNAME, AbstractConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractAgentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractAgentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-agent")
    public JAXBElement<AbstractAgentType> createAbstractAgent(AbstractAgentType value) {
        return new JAXBElement<AbstractAgentType>(_AbstractAgent_QNAME, AbstractAgentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomAgentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomAgentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-agent", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-agent")
    public JAXBElement<CustomAgentType> createCustomAgent(CustomAgentType value) {
        return new JAXBElement<CustomAgentType>(_CustomAgent_QNAME, CustomAgentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractQueueStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractQueueStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-queue-store")
    public JAXBElement<AbstractQueueStoreType> createAbstractQueueStore(AbstractQueueStoreType value) {
        return new JAXBElement<AbstractQueueStoreType>(_AbstractQueueStore_QNAME, AbstractQueueStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefQueueStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RefQueueStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "queue-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-queue-store")
    public JAXBElement<RefQueueStoreType> createQueueStore(RefQueueStoreType value) {
        return new JAXBElement<RefQueueStoreType>(_QueueStore_QNAME, RefQueueStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomQueueStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomQueueStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-queue-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-queue-store")
    public JAXBElement<CustomQueueStoreType> createCustomQueueStore(CustomQueueStoreType value) {
        return new JAXBElement<CustomQueueStoreType>(_CustomQueueStore_QNAME, CustomQueueStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "default-in-memory-queue-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-queue-store")
    public JAXBElement<DefaultQueueStoreType> createDefaultInMemoryQueueStore(DefaultQueueStoreType value) {
        return new JAXBElement<DefaultQueueStoreType>(_DefaultInMemoryQueueStore_QNAME, DefaultQueueStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "default-persistent-queue-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-queue-store")
    public JAXBElement<DefaultQueueStoreType> createDefaultPersistentQueueStore(DefaultQueueStoreType value) {
        return new JAXBElement<DefaultQueueStoreType>(_DefaultPersistentQueueStore_QNAME, DefaultQueueStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "simple-in-memory-queue-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-queue-store")
    public JAXBElement<DefaultQueueStoreType> createSimpleInMemoryQueueStore(DefaultQueueStoreType value) {
        return new JAXBElement<DefaultQueueStoreType>(_SimpleInMemoryQueueStore_QNAME, DefaultQueueStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DefaultQueueStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "file-queue-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-queue-store")
    public JAXBElement<DefaultQueueStoreType> createFileQueueStore(DefaultQueueStoreType value) {
        return new JAXBElement<DefaultQueueStoreType>(_FileQueueStore_QNAME, DefaultQueueStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractGlobalEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractGlobalEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-global-endpoint")
    public JAXBElement<AbstractGlobalEndpointType> createAbstractGlobalEndpoint(AbstractGlobalEndpointType value) {
        return new JAXBElement<AbstractGlobalEndpointType>(_AbstractGlobalEndpoint_QNAME, AbstractGlobalEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-inbound-endpoint")
    public JAXBElement<AbstractInboundEndpointType> createAbstractInboundEndpoint(AbstractInboundEndpointType value) {
        return new JAXBElement<AbstractInboundEndpointType>(_AbstractInboundEndpoint_QNAME, AbstractInboundEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-outbound-endpoint")
    public JAXBElement<AbstractOutboundEndpointType> createAbstractOutboundEndpoint(AbstractOutboundEndpointType value) {
        return new JAXBElement<AbstractOutboundEndpointType>(_AbstractOutboundEndpoint_QNAME, AbstractOutboundEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-observer-message-processor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<AbstractObserverMessageProcessorType> createAbstractObserverMessageProcessor(AbstractObserverMessageProcessorType value) {
        return new JAXBElement<AbstractObserverMessageProcessorType>(_AbstractObserverMessageProcessor_QNAME, AbstractObserverMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "processor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<RefMessageProcessorType> createProcessor(RefMessageProcessorType value) {
        return new JAXBElement<RefMessageProcessorType>(_Processor_QNAME, RefMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-processor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<CustomMessageProcessorType> createCustomProcessor(CustomMessageProcessorType value) {
        return new JAXBElement<CustomMessageProcessorType>(_CustomProcessor_QNAME, CustomMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractEmptyMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractEmptyMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-empty-processor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<AbstractEmptyMessageProcessorType> createAbstractEmptyProcessor(AbstractEmptyMessageProcessorType value) {
        return new JAXBElement<AbstractEmptyMessageProcessorType>(_AbstractEmptyProcessor_QNAME, AbstractEmptyMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageProcessorChainType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MessageProcessorChainType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "processor-chain", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<MessageProcessorChainType> createProcessorChain(MessageProcessorChainType value) {
        return new JAXBElement<MessageProcessorChainType>(_ProcessorChain_QNAME, MessageProcessorChainType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubFlowType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubFlowType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "sub-flow")
    public JAXBElement<SubFlowType> createSubFlow(SubFlowType value) {
        return new JAXBElement<SubFlowType>(_SubFlow_QNAME, SubFlowType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvokeType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InvokeType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "invoke", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<InvokeType> createInvoke(InvokeType value) {
        return new JAXBElement<InvokeType>(_Invoke_QNAME, InvokeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageEnricherType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MessageEnricherType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "enricher", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<MessageEnricherType> createEnricher(MessageEnricherType value) {
        return new JAXBElement<MessageEnricherType>(_Enricher_QNAME, MessageEnricherType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AsyncType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AsyncType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "async", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<AsyncType> createAsync(AsyncType value) {
        return new JAXBElement<AsyncType>(_Async_QNAME, AsyncType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestReplyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RequestReplyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "request-reply", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<RequestReplyType> createRequestReply(RequestReplyType value) {
        return new JAXBElement<RequestReplyType>(_RequestReply_QNAME, RequestReplyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractPollOverrideType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractPollOverrideType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-poll-override")
    public JAXBElement<AbstractPollOverrideType> createAbstractPollOverride(AbstractPollOverrideType value) {
        return new JAXBElement<AbstractPollOverrideType>(_AbstractPollOverride_QNAME, AbstractPollOverrideType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WatermarkType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WatermarkType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "watermark", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-poll-override")
    public JAXBElement<WatermarkType> createWatermark(WatermarkType value) {
        return new JAXBElement<WatermarkType>(_Watermark_QNAME, WatermarkType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "logger", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-observer-message-processor")
    public JAXBElement<LoggerType> createLogger(LoggerType value) {
        return new JAXBElement<LoggerType>(_Logger_QNAME, LoggerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransactional }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransactional }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "transactional", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<AbstractTransactional> createTransactional(AbstractTransactional value) {
        return new JAXBElement<AbstractTransactional>(_Transactional_QNAME, AbstractTransactional.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractMessageSourceType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractMessageSourceType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-message-source")
    public JAXBElement<AbstractMessageSourceType> createAbstractMessageSource(AbstractMessageSourceType value) {
        return new JAXBElement<AbstractMessageSourceType>(_AbstractMessageSource_QNAME, AbstractMessageSourceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomMessageSourceType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomMessageSourceType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-source", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-source")
    public JAXBElement<CustomMessageSourceType> createCustomSource(CustomMessageSourceType value) {
        return new JAXBElement<CustomMessageSourceType>(_CustomSource_QNAME, CustomMessageSourceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompositeMessageSourceType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CompositeMessageSourceType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "composite-source", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-source")
    public JAXBElement<CompositeMessageSourceType> createCompositeSource(CompositeMessageSourceType value) {
        return new JAXBElement<CompositeMessageSourceType>(_CompositeSource_QNAME, CompositeMessageSourceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<CommonTransformerType> createAbstractTransformer(CommonTransformerType value) {
        return new JAXBElement<CommonTransformerType>(_AbstractTransformer_QNAME, CommonTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractCatchAllStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractCatchAllStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-catch-all-strategy")
    public JAXBElement<AbstractCatchAllStrategyType> createAbstractCatchAllStrategy(AbstractCatchAllStrategyType value) {
        return new JAXBElement<AbstractCatchAllStrategyType>(_AbstractCatchAllStrategy_QNAME, AbstractCatchAllStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractInboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractInboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-inbound-router")
    public JAXBElement<AbstractInboundRouterType> createAbstractInboundRouter(AbstractInboundRouterType value) {
        return new JAXBElement<AbstractInboundRouterType>(_AbstractInboundRouter_QNAME, AbstractInboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<CommonFilterType> createAbstractFilter(CommonFilterType value) {
        return new JAXBElement<CommonFilterType>(_AbstractFilter_QNAME, CommonFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractOutboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractOutboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-outbound-router")
    public JAXBElement<AbstractOutboundRouterType> createAbstractOutboundRouter(AbstractOutboundRouterType value) {
        return new JAXBElement<AbstractOutboundRouterType>(_AbstractOutboundRouter_QNAME, AbstractOutboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransactionManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransactionManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-transaction-manager")
    public JAXBElement<AbstractTransactionManagerType> createAbstractTransactionManager(AbstractTransactionManagerType value) {
        return new JAXBElement<AbstractTransactionManagerType>(_AbstractTransactionManager_QNAME, AbstractTransactionManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransactionManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransactionManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-shared-transaction-manager")
    public JAXBElement<AbstractTransactionManagerType> createAbstractSharedTransactionManager(AbstractTransactionManagerType value) {
        return new JAXBElement<AbstractTransactionManagerType>(_AbstractSharedTransactionManager_QNAME, AbstractTransactionManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractConfigurationExtensionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractConfigurationExtensionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-configuration-extension")
    public JAXBElement<AbstractConfigurationExtensionType> createAbstractConfigurationExtension(AbstractConfigurationExtensionType value) {
        return new JAXBElement<AbstractConfigurationExtensionType>(_AbstractConfigurationExtension_QNAME, AbstractConfigurationExtensionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractModelType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractModelType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-model")
    public JAXBElement<AbstractModelType> createAbstractModel(AbstractModelType value) {
        return new JAXBElement<AbstractModelType>(_AbstractModel_QNAME, AbstractModelType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SedaModelType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SedaModelType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "seda-model", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-model")
    public JAXBElement<SedaModelType> createSedaModel(SedaModelType value) {
        return new JAXBElement<SedaModelType>(_SedaModel_QNAME, SedaModelType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultModelType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DefaultModelType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "model", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-model")
    public JAXBElement<DefaultModelType> createModel(DefaultModelType value) {
        return new JAXBElement<DefaultModelType>(_Model_QNAME, DefaultModelType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractQueueProfileType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractQueueProfileType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-queue-profile")
    public JAXBElement<AbstractQueueProfileType> createAbstractQueueProfile(AbstractQueueProfileType value) {
        return new JAXBElement<AbstractQueueProfileType>(_AbstractQueueProfile_QNAME, AbstractQueueProfileType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverSetType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverSetType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-entry-point-resolver-set")
    public JAXBElement<AbstractEntryPointResolverSetType> createAbstractEntryPointResolverSet(AbstractEntryPointResolverSetType value) {
        return new JAXBElement<AbstractEntryPointResolverSetType>(_AbstractEntryPointResolverSet_QNAME, AbstractEntryPointResolverSetType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "legacy-entry-point-resolver-set", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-entry-point-resolver-set")
    public JAXBElement<ExtensibleEntryPointResolverSet> createLegacyEntryPointResolverSet(ExtensibleEntryPointResolverSet value) {
        return new JAXBElement<ExtensibleEntryPointResolverSet>(_LegacyEntryPointResolverSet_QNAME, ExtensibleEntryPointResolverSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "entry-point-resolver-set", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-entry-point-resolver-set")
    public JAXBElement<ExtensibleEntryPointResolverSet> createEntryPointResolverSet(ExtensibleEntryPointResolverSet value) {
        return new JAXBElement<ExtensibleEntryPointResolverSet>(_EntryPointResolverSet_QNAME, ExtensibleEntryPointResolverSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomEntryPointResolverSetType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomEntryPointResolverSetType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-entry-point-resolver-set", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-entry-point-resolver-set")
    public JAXBElement<CustomEntryPointResolverSetType> createCustomEntryPointResolverSet(CustomEntryPointResolverSetType value) {
        return new JAXBElement<CustomEntryPointResolverSetType>(_CustomEntryPointResolverSet_QNAME, CustomEntryPointResolverSetType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-entry-point-resolver")
    public JAXBElement<AbstractEntryPointResolverType> createAbstractEntryPointResolver(AbstractEntryPointResolverType value) {
        return new JAXBElement<AbstractEntryPointResolverType>(_AbstractEntryPointResolver_QNAME, AbstractEntryPointResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "callable-entry-point-resolver", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-entry-point-resolver")
    public JAXBElement<AbstractEntryPointResolverType> createCallableEntryPointResolver(AbstractEntryPointResolverType value) {
        return new JAXBElement<AbstractEntryPointResolverType>(_CallableEntryPointResolver_QNAME, AbstractEntryPointResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomEntryPointResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomEntryPointResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-entry-point-resolver", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-entry-point-resolver")
    public JAXBElement<CustomEntryPointResolverType> createCustomEntryPointResolver(CustomEntryPointResolverType value) {
        return new JAXBElement<CustomEntryPointResolverType>(_CustomEntryPointResolver_QNAME, CustomEntryPointResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyEntryPointResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PropertyEntryPointResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "property-entry-point-resolver", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-entry-point-resolver")
    public JAXBElement<PropertyEntryPointResolverType> createPropertyEntryPointResolver(PropertyEntryPointResolverType value) {
        return new JAXBElement<PropertyEntryPointResolverType>(_PropertyEntryPointResolver_QNAME, PropertyEntryPointResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MethodEntryPointResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MethodEntryPointResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "method-entry-point-resolver", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-entry-point-resolver")
    public JAXBElement<MethodEntryPointResolverType> createMethodEntryPointResolver(MethodEntryPointResolverType value) {
        return new JAXBElement<MethodEntryPointResolverType>(_MethodEntryPointResolver_QNAME, MethodEntryPointResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReflectionEntryPointResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReflectionEntryPointResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "reflection-entry-point-resolver", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-entry-point-resolver")
    public JAXBElement<ReflectionEntryPointResolverType> createReflectionEntryPointResolver(ReflectionEntryPointResolverType value) {
        return new JAXBElement<ReflectionEntryPointResolverType>(_ReflectionEntryPointResolver_QNAME, ReflectionEntryPointResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "array-entry-point-resolver", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-entry-point-resolver")
    public JAXBElement<ComplexEntryPointResolverType> createArrayEntryPointResolver(ComplexEntryPointResolverType value) {
        return new JAXBElement<ComplexEntryPointResolverType>(_ArrayEntryPointResolver_QNAME, ComplexEntryPointResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "no-arguments-entry-point-resolver", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-entry-point-resolver")
    public JAXBElement<ComplexEntryPointResolverType> createNoArgumentsEntryPointResolver(ComplexEntryPointResolverType value) {
        return new JAXBElement<ComplexEntryPointResolverType>(_NoArgumentsEntryPointResolver_QNAME, ComplexEntryPointResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MethodType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MethodType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "include-entry-point")
    public JAXBElement<MethodType> createIncludeEntryPoint(MethodType value) {
        return new JAXBElement<MethodType>(_IncludeEntryPoint_QNAME, MethodType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractServiceType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractServiceType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-service")
    public JAXBElement<AbstractServiceType> createAbstractService(AbstractServiceType value) {
        return new JAXBElement<AbstractServiceType>(_AbstractService_QNAME, AbstractServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "legacy-abstract-exception-strategy")
    public JAXBElement<ExceptionStrategyType> createLegacyAbstractExceptionStrategy(ExceptionStrategyType value) {
        return new JAXBElement<ExceptionStrategyType>(_LegacyAbstractExceptionStrategy_QNAME, ExceptionStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-exception-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "legacy-abstract-exception-strategy")
    public JAXBElement<ExceptionStrategyType> createAbstractExceptionStrategy(ExceptionStrategyType value) {
        return new JAXBElement<ExceptionStrategyType>(_AbstractExceptionStrategy_QNAME, ExceptionStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractReconnectionStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractReconnectionStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-reconnection-strategy")
    public JAXBElement<AbstractReconnectionStrategyType> createAbstractReconnectionStrategy(AbstractReconnectionStrategyType value) {
        return new JAXBElement<AbstractReconnectionStrategyType>(_AbstractReconnectionStrategy_QNAME, AbstractReconnectionStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractReconnectNotifierType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractReconnectNotifierType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-reconnect-notifier")
    public JAXBElement<AbstractReconnectNotifierType> createAbstractReconnectNotifier(AbstractReconnectNotifierType value) {
        return new JAXBElement<AbstractReconnectNotifierType>(_AbstractReconnectNotifier_QNAME, AbstractReconnectNotifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReconnectSimpleStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReconnectSimpleStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "reconnect", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-reconnection-strategy")
    public JAXBElement<ReconnectSimpleStrategyType> createReconnect(ReconnectSimpleStrategyType value) {
        return new JAXBElement<ReconnectSimpleStrategyType>(_Reconnect_QNAME, ReconnectSimpleStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReconnectForeverStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReconnectForeverStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "reconnect-forever", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-reconnection-strategy")
    public JAXBElement<ReconnectForeverStrategyType> createReconnectForever(ReconnectForeverStrategyType value) {
        return new JAXBElement<ReconnectForeverStrategyType>(_ReconnectForever_QNAME, ReconnectForeverStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReconnectCustomStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReconnectCustomStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "reconnect-custom-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-reconnection-strategy")
    public JAXBElement<ReconnectCustomStrategyType> createReconnectCustomStrategy(ReconnectCustomStrategyType value) {
        return new JAXBElement<ReconnectCustomStrategyType>(_ReconnectCustomStrategy_QNAME, ReconnectCustomStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReconnectNotifierType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReconnectNotifierType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "reconnect-notifier", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-reconnect-notifier")
    public JAXBElement<ReconnectNotifierType> createReconnectNotifier(ReconnectNotifierType value) {
        return new JAXBElement<ReconnectNotifierType>(_ReconnectNotifier_QNAME, ReconnectNotifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReconnectCustomNotifierType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReconnectCustomNotifierType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "reconnect-custom-notifier", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-reconnect-notifier")
    public JAXBElement<ReconnectCustomNotifierType> createReconnectCustomNotifier(ReconnectCustomNotifierType value) {
        return new JAXBElement<ReconnectCustomNotifierType>(_ReconnectCustomNotifier_QNAME, ReconnectCustomNotifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractAsyncReplyRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractAsyncReplyRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-async-reply-router")
    public JAXBElement<AbstractAsyncReplyRouterType> createAbstractAsyncReplyRouter(AbstractAsyncReplyRouterType value) {
        return new JAXBElement<AbstractAsyncReplyRouterType>(_AbstractAsyncReplyRouter_QNAME, AbstractAsyncReplyRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SedaServiceType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SedaServiceType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "service", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-service")
    public JAXBElement<SedaServiceType> createService(SedaServiceType value) {
        return new JAXBElement<SedaServiceType>(_Service_QNAME, SedaServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomServiceType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomServiceType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-service", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-service")
    public JAXBElement<CustomServiceType> createCustomService(CustomServiceType value) {
        return new JAXBElement<CustomServiceType>(_CustomService_QNAME, CustomServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractServiceThreadingProfileType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractServiceThreadingProfileType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-service-threading-profile")
    public JAXBElement<AbstractServiceThreadingProfileType> createAbstractServiceThreadingProfile(AbstractServiceThreadingProfileType value) {
        return new JAXBElement<AbstractServiceThreadingProfileType>(_AbstractServiceThreadingProfile_QNAME, AbstractServiceThreadingProfileType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractFlowConstructType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractFlowConstructType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-flow-construct")
    public JAXBElement<AbstractFlowConstructType> createAbstractFlowConstruct(AbstractFlowConstructType value) {
        return new JAXBElement<AbstractFlowConstructType>(_AbstractFlowConstruct_QNAME, AbstractFlowConstructType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FlowType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FlowType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "flow")
    public JAXBElement<FlowType> createFlow(FlowType value) {
        return new JAXBElement<FlowType>(_Flow_QNAME, FlowType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessingStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ProcessingStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-processing-strategy")
    public JAXBElement<ProcessingStrategyType> createAbstractProcessingStrategy(ProcessingStrategyType value) {
        return new JAXBElement<ProcessingStrategyType>(_AbstractProcessingStrategy_QNAME, ProcessingStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AsynchronousProcessingStrategy }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AsynchronousProcessingStrategy }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "asynchronous-processing-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-processing-strategy")
    public JAXBElement<AsynchronousProcessingStrategy> createAsynchronousProcessingStrategy(AsynchronousProcessingStrategy value) {
        return new JAXBElement<AsynchronousProcessingStrategy>(_AsynchronousProcessingStrategy_QNAME, AsynchronousProcessingStrategy.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueuedAsynchronousProcessingStrategy }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link QueuedAsynchronousProcessingStrategy }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "queued-asynchronous-processing-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-processing-strategy")
    public JAXBElement<QueuedAsynchronousProcessingStrategy> createQueuedAsynchronousProcessingStrategy(QueuedAsynchronousProcessingStrategy value) {
        return new JAXBElement<QueuedAsynchronousProcessingStrategy>(_QueuedAsynchronousProcessingStrategy_QNAME, QueuedAsynchronousProcessingStrategy.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AsynchronousProcessingStrategy }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AsynchronousProcessingStrategy }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "thread-per-processor-processing-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-processing-strategy")
    public JAXBElement<AsynchronousProcessingStrategy> createThreadPerProcessorProcessingStrategy(AsynchronousProcessingStrategy value) {
        return new JAXBElement<AsynchronousProcessingStrategy>(_ThreadPerProcessorProcessingStrategy_QNAME, AsynchronousProcessingStrategy.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueuedAsynchronousProcessingStrategy }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link QueuedAsynchronousProcessingStrategy }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "queued-thread-per-processor-processing-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-processing-strategy")
    public JAXBElement<QueuedAsynchronousProcessingStrategy> createQueuedThreadPerProcessorProcessingStrategy(QueuedAsynchronousProcessingStrategy value) {
        return new JAXBElement<QueuedAsynchronousProcessingStrategy>(_QueuedThreadPerProcessorProcessingStrategy_QNAME, QueuedAsynchronousProcessingStrategy.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NonBlockingProcessingStrategy }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link NonBlockingProcessingStrategy }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "non-blocking-processing-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-processing-strategy")
    public JAXBElement<NonBlockingProcessingStrategy> createNonBlockingProcessingStrategy(NonBlockingProcessingStrategy value) {
        return new JAXBElement<NonBlockingProcessingStrategy>(_NonBlockingProcessingStrategy_QNAME, NonBlockingProcessingStrategy.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomProcessingStrategy }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomProcessingStrategy }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-processing-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-processing-strategy")
    public JAXBElement<CustomProcessingStrategy> createCustomProcessingStrategy(CustomProcessingStrategy value) {
        return new JAXBElement<CustomProcessingStrategy>(_CustomProcessingStrategy_QNAME, CustomProcessingStrategy.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FlowRef }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FlowRef }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "flow-ref", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<FlowRef> createFlowRef(FlowRef value) {
        return new JAXBElement<FlowRef>(_FlowRef_QNAME, FlowRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleServiceType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SimpleServiceType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "simple-service", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-flow-construct")
    public JAXBElement<SimpleServiceType> createSimpleService(SimpleServiceType value) {
        return new JAXBElement<SimpleServiceType>(_SimpleService_QNAME, SimpleServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BridgeType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BridgeType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "bridge", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-flow-construct")
    public JAXBElement<BridgeType> createBridge(BridgeType value) {
        return new JAXBElement<BridgeType>(_Bridge_QNAME, BridgeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidatorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ValidatorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "validator", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-flow-construct")
    public JAXBElement<ValidatorType> createValidator(ValidatorType value) {
        return new JAXBElement<ValidatorType>(_Validator_QNAME, ValidatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-component", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<AbstractComponentType> createAbstractComponent(AbstractComponentType value) {
        return new JAXBElement<AbstractComponentType>(_AbstractComponent_QNAME, AbstractComponentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "component", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-component")
    public JAXBElement<DefaultJavaComponentType> createComponent(DefaultJavaComponentType value) {
        return new JAXBElement<DefaultJavaComponentType>(_Component_QNAME, DefaultJavaComponentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "pooled-component", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-component")
    public JAXBElement<PooledJavaComponentType> createPooledComponent(PooledJavaComponentType value) {
        return new JAXBElement<PooledJavaComponentType>(_PooledComponent_QNAME, PooledJavaComponentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "echo-component", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-component")
    public JAXBElement<DefaultComponentType> createEchoComponent(DefaultComponentType value) {
        return new JAXBElement<DefaultComponentType>(_EchoComponent_QNAME, DefaultComponentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "log-component", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-component")
    public JAXBElement<DefaultComponentType> createLogComponent(DefaultComponentType value) {
        return new JAXBElement<DefaultComponentType>(_LogComponent_QNAME, DefaultComponentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "null-component", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-component")
    public JAXBElement<DefaultComponentType> createNullComponent(DefaultComponentType value) {
        return new JAXBElement<DefaultComponentType>(_NullComponent_QNAME, DefaultComponentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "static-component", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-component")
    public JAXBElement<StaticComponentType> createStaticComponent(StaticComponentType value) {
        return new JAXBElement<StaticComponentType>(_StaticComponent_QNAME, StaticComponentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-transaction")
    public JAXBElement<AbstractTransactionType> createAbstractTransaction(AbstractTransactionType value) {
        return new JAXBElement<AbstractTransactionType>(_AbstractTransaction_QNAME, AbstractTransactionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-xa-transaction")
    public JAXBElement<AbstractTransactionType> createAbstractXaTransaction(AbstractTransactionType value) {
        return new JAXBElement<AbstractTransactionType>(_AbstractXaTransaction_QNAME, AbstractTransactionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-multi-transaction")
    public JAXBElement<AbstractTransactionType> createAbstractMultiTransaction(AbstractTransactionType value) {
        return new JAXBElement<AbstractTransactionType>(_AbstractMultiTransaction_QNAME, AbstractTransactionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-transaction", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction")
    public JAXBElement<CustomTransactionType> createCustomTransaction(CustomTransactionType value) {
        return new JAXBElement<CustomTransactionType>(_CustomTransaction_QNAME, CustomTransactionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "xa-transaction", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction")
    public JAXBElement<XaTransactionType> createXaTransaction(XaTransactionType value) {
        return new JAXBElement<XaTransactionType>(_XaTransaction_QNAME, XaTransactionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "websphere-transaction-manager", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction-manager")
    public JAXBElement<TransactionManagerType> createWebsphereTransactionManager(TransactionManagerType value) {
        return new JAXBElement<TransactionManagerType>(_WebsphereTransactionManager_QNAME, TransactionManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "jboss-transaction-manager", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction-manager")
    public JAXBElement<TransactionManagerType> createJbossTransactionManager(TransactionManagerType value) {
        return new JAXBElement<TransactionManagerType>(_JbossTransactionManager_QNAME, TransactionManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JndiTransactionManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link JndiTransactionManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "weblogic-transaction-manager", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction-manager")
    public JAXBElement<JndiTransactionManagerType> createWeblogicTransactionManager(JndiTransactionManagerType value) {
        return new JAXBElement<JndiTransactionManagerType>(_WeblogicTransactionManager_QNAME, JndiTransactionManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "jrun-transaction-manager", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction-manager")
    public JAXBElement<TransactionManagerType> createJrunTransactionManager(TransactionManagerType value) {
        return new JAXBElement<TransactionManagerType>(_JrunTransactionManager_QNAME, TransactionManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "resin-transaction-manager", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction-manager")
    public JAXBElement<TransactionManagerType> createResinTransactionManager(TransactionManagerType value) {
        return new JAXBElement<TransactionManagerType>(_ResinTransactionManager_QNAME, TransactionManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JndiTransactionManager }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link JndiTransactionManager }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "jndi-transaction-manager", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction-manager")
    public JAXBElement<JndiTransactionManager> createJndiTransactionManager(JndiTransactionManager value) {
        return new JAXBElement<JndiTransactionManager>(_JndiTransactionManager_QNAME, JndiTransactionManager.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomTransactionManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomTransactionManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-transaction-manager", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction-manager")
    public JAXBElement<CustomTransactionManagerType> createCustomTransactionManager(CustomTransactionManagerType value) {
        return new JAXBElement<CustomTransactionManagerType>(_CustomTransactionManager_QNAME, CustomTransactionManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlobalEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GlobalEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-endpoint")
    public JAXBElement<GlobalEndpointType> createEndpoint(GlobalEndpointType value) {
        return new JAXBElement<GlobalEndpointType>(_Endpoint_QNAME, GlobalEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "inbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-endpoint")
    public JAXBElement<InboundEndpointType> createInboundEndpoint(InboundEndpointType value) {
        return new JAXBElement<InboundEndpointType>(_InboundEndpoint_QNAME, InboundEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractSchedulerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractSchedulerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-scheduler")
    public JAXBElement<AbstractSchedulerType> createAbstractScheduler(AbstractSchedulerType value) {
        return new JAXBElement<AbstractSchedulerType>(_AbstractScheduler_QNAME, AbstractSchedulerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FixedSchedulerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FixedSchedulerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "fixed-frequency-scheduler", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-scheduler")
    public JAXBElement<FixedSchedulerType> createFixedFrequencyScheduler(FixedSchedulerType value) {
        return new JAXBElement<FixedSchedulerType>(_FixedFrequencyScheduler_QNAME, FixedSchedulerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PollInboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PollInboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "poll", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-source")
    public JAXBElement<PollInboundEndpointType> createPoll(PollInboundEndpointType value) {
        return new JAXBElement<PollInboundEndpointType>(_Poll_QNAME, PollInboundEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OutboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "outbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-endpoint")
    public JAXBElement<OutboundEndpointType> createOutboundEndpoint(OutboundEndpointType value) {
        return new JAXBElement<OutboundEndpointType>(_OutboundEndpoint_QNAME, OutboundEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-security-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<AbstractSecurityFilterType> createAbstractSecurityFilter(AbstractSecurityFilterType value) {
        return new JAXBElement<AbstractSecurityFilterType>(_AbstractSecurityFilter_QNAME, AbstractSecurityFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-intercepting-message-processor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<AbstractInterceptingMessageProcessorType> createAbstractInterceptingMessageProcessor(AbstractInterceptingMessageProcessorType value) {
        return new JAXBElement<AbstractInterceptingMessageProcessorType>(_AbstractInterceptingMessageProcessor_QNAME, AbstractInterceptingMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "username-password-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-security-filter")
    public JAXBElement<UsernamePasswordFilterType> createUsernamePasswordFilter(UsernamePasswordFilterType value) {
        return new JAXBElement<UsernamePasswordFilterType>(_UsernamePasswordFilter_QNAME, UsernamePasswordFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-security-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-security-filter")
    public JAXBElement<CustomSecurityFilterType> createCustomSecurityFilter(CustomSecurityFilterType value) {
        return new JAXBElement<CustomSecurityFilterType>(_CustomSecurityFilter_QNAME, CustomSecurityFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<RefFilterType> createFilter(RefFilterType value) {
        return new JAXBElement<RefFilterType>(_Filter_QNAME, RefFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "not-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<UnitaryFilterType> createNotFilter(UnitaryFilterType value) {
        return new JAXBElement<UnitaryFilterType>(_NotFilter_QNAME, UnitaryFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "and-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<CollectionFilterType> createAndFilter(CollectionFilterType value) {
        return new JAXBElement<CollectionFilterType>(_AndFilter_QNAME, CollectionFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "or-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<CollectionFilterType> createOrFilter(CollectionFilterType value) {
        return new JAXBElement<CollectionFilterType>(_OrFilter_QNAME, CollectionFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "wildcard-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<WildcardFilterType> createWildcardFilter(WildcardFilterType value) {
        return new JAXBElement<WildcardFilterType>(_WildcardFilter_QNAME, WildcardFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "expression-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<ExpressionFilterType> createExpressionFilter(ExpressionFilterType value) {
        return new JAXBElement<ExpressionFilterType>(_ExpressionFilter_QNAME, ExpressionFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "regex-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<RegexFilterType> createRegexFilter(RegexFilterType value) {
        return new JAXBElement<RegexFilterType>(_RegexFilter_QNAME, RegexFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "message-property-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<ScopedPropertyFilterType> createMessagePropertyFilter(ScopedPropertyFilterType value) {
        return new JAXBElement<ScopedPropertyFilterType>(_MessagePropertyFilter_QNAME, ScopedPropertyFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "exception-type-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<TypeFilterType> createExceptionTypeFilter(TypeFilterType value) {
        return new JAXBElement<TypeFilterType>(_ExceptionTypeFilter_QNAME, TypeFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "payload-type-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<TypeFilterType> createPayloadTypeFilter(TypeFilterType value) {
        return new JAXBElement<TypeFilterType>(_PayloadTypeFilter_QNAME, TypeFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<CustomFilterType> createCustomFilter(CustomFilterType value) {
        return new JAXBElement<CustomFilterType>(_CustomFilter_QNAME, CustomFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractInterceptorStackType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractInterceptorStackType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-interceptor-stack")
    public JAXBElement<AbstractInterceptorStackType> createAbstractInterceptorStack(AbstractInterceptorStackType value) {
        return new JAXBElement<AbstractInterceptorStackType>(_AbstractInterceptorStack_QNAME, AbstractInterceptorStackType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractInterceptorStackType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractInterceptorStackType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "interceptor-stack", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-interceptor-stack")
    public JAXBElement<AbstractInterceptorStackType> createInterceptorStack(AbstractInterceptorStackType value) {
        return new JAXBElement<AbstractInterceptorStackType>(_InterceptorStack_QNAME, AbstractInterceptorStackType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-interceptor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<AbstractInterceptorType> createAbstractInterceptor(AbstractInterceptorType value) {
        return new JAXBElement<AbstractInterceptorType>(_AbstractInterceptor_QNAME, AbstractInterceptorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "timer-interceptor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-interceptor")
    public JAXBElement<AbstractInterceptorType> createTimerInterceptor(AbstractInterceptorType value) {
        return new JAXBElement<AbstractInterceptorType>(_TimerInterceptor_QNAME, AbstractInterceptorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "logging-interceptor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-interceptor")
    public JAXBElement<AbstractInterceptorType> createLoggingInterceptor(AbstractInterceptorType value) {
        return new JAXBElement<AbstractInterceptorType>(_LoggingInterceptor_QNAME, AbstractInterceptorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomInterceptorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomInterceptorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-interceptor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-interceptor")
    public JAXBElement<CustomInterceptorType> createCustomInterceptor(CustomInterceptorType value) {
        return new JAXBElement<CustomInterceptorType>(_CustomInterceptor_QNAME, CustomInterceptorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "encryption-security-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-security-filter")
    public JAXBElement<EncryptionSecurityFilterType> createEncryptionSecurityFilter(EncryptionSecurityFilterType value) {
        return new JAXBElement<EncryptionSecurityFilterType>(_EncryptionSecurityFilter_QNAME, EncryptionSecurityFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<RefTransformerType> createTransformer(RefTransformerType value) {
        return new JAXBElement<RefTransformerType>(_Transformer_QNAME, RefTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "auto-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createAutoTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_AutoTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<CustomTransformerType> createCustomTransformer(CustomTransformerType value) {
        return new JAXBElement<CustomTransformerType>(_CustomTransformer_QNAME, CustomTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "message-properties-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<MessagePropertiesTransformerType> createMessagePropertiesTransformer(MessagePropertiesTransformerType value) {
        return new JAXBElement<MessagePropertiesTransformerType>(_MessagePropertiesTransformer_QNAME, MessagePropertiesTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "set-property", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<SetPropertyType> createSetProperty(SetPropertyType value) {
        return new JAXBElement<SetPropertyType>(_SetProperty_QNAME, SetPropertyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "remove-property", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<RemovePropertyType> createRemoveProperty(RemovePropertyType value) {
        return new JAXBElement<RemovePropertyType>(_RemoveProperty_QNAME, RemovePropertyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "copy-properties", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<CopyPropertiesType> createCopyProperties(CopyPropertiesType value) {
        return new JAXBElement<CopyPropertiesType>(_CopyProperties_QNAME, CopyPropertiesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "set-variable", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<SetVariableType> createSetVariable(SetVariableType value) {
        return new JAXBElement<SetVariableType>(_SetVariable_QNAME, SetVariableType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "remove-variable", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<RemoveVariableType> createRemoveVariable(RemoveVariableType value) {
        return new JAXBElement<RemoveVariableType>(_RemoveVariable_QNAME, RemoveVariableType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "set-session-variable", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<SetVariableType> createSetSessionVariable(SetVariableType value) {
        return new JAXBElement<SetVariableType>(_SetSessionVariable_QNAME, SetVariableType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "remove-session-variable", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<RemoveVariableType> createRemoveSessionVariable(RemoveVariableType value) {
        return new JAXBElement<RemoveVariableType>(_RemoveSessionVariable_QNAME, RemoveVariableType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "set-attachment", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<SetAttachmentType> createSetAttachment(SetAttachmentType value) {
        return new JAXBElement<SetAttachmentType>(_SetAttachment_QNAME, SetAttachmentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "remove-attachment", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<RemoveAttachmentType> createRemoveAttachment(RemoveAttachmentType value) {
        return new JAXBElement<RemoveAttachmentType>(_RemoveAttachment_QNAME, RemoveAttachmentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "copy-attachments", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<CopyAttachmentType> createCopyAttachments(CopyAttachmentType value) {
        return new JAXBElement<CopyAttachmentType>(_CopyAttachments_QNAME, CopyAttachmentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "base64-encoder-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createBase64EncoderTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_Base64EncoderTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "base64-decoder-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createBase64DecoderTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_Base64DecoderTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "xml-entity-encoder-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createXmlEntityEncoderTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_XmlEntityEncoderTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "xml-entity-decoder-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createXmlEntityDecoderTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_XmlEntityDecoderTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "gzip-compress-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createGzipCompressTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_GzipCompressTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "gzip-uncompress-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createGzipUncompressTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_GzipUncompressTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "byte-array-to-hex-string-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createByteArrayToHexStringTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_ByteArrayToHexStringTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "hex-string-to-byte-array-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createHexStringToByteArrayTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_HexStringToByteArrayTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "byte-array-to-object-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createByteArrayToObjectTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_ByteArrayToObjectTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "object-to-byte-array-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createObjectToByteArrayTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_ObjectToByteArrayTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "object-to-string-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createObjectToStringTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_ObjectToStringTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "byte-array-to-serializable-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createByteArrayToSerializableTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_ByteArrayToSerializableTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "serializable-to-byte-array-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createSerializableToByteArrayTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_SerializableToByteArrayTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "byte-array-to-string-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createByteArrayToStringTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_ByteArrayToStringTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "string-to-byte-array-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createStringToByteArrayTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_StringToByteArrayTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "map-to-bean-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createMapToBeanTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_MapToBeanTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "bean-to-map-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createBeanToMapTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_BeanToMapTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "append-string-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AppendStringTransformerType> createAppendStringTransformer(AppendStringTransformerType value) {
        return new JAXBElement<AppendStringTransformerType>(_AppendStringTransformer_QNAME, AppendStringTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "parse-template", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<ParseTemplateTransformerType> createParseTemplate(ParseTemplateTransformerType value) {
        return new JAXBElement<ParseTemplateTransformerType>(_ParseTemplate_QNAME, ParseTemplateTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetPayloadTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetPayloadTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "set-payload", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<SetPayloadTransformerType> createSetPayload(SetPayloadTransformerType value) {
        return new JAXBElement<SetPayloadTransformerType>(_SetPayload_QNAME, SetPayloadTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "encrypt-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<EncryptionTransformerType> createEncryptTransformer(EncryptionTransformerType value) {
        return new JAXBElement<EncryptionTransformerType>(_EncryptTransformer_QNAME, EncryptionTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "decrypt-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<EncryptionTransformerType> createDecryptTransformer(EncryptionTransformerType value) {
        return new JAXBElement<EncryptionTransformerType>(_DecryptTransformer_QNAME, EncryptionTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "bean-builder-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<BeanBuilderTransformer> createBeanBuilderTransformer(BeanBuilderTransformer value) {
        return new JAXBElement<BeanBuilderTransformer>(_BeanBuilderTransformer_QNAME, BeanBuilderTransformer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "expression-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<ExpressionTransformerType> createExpressionTransformer(ExpressionTransformerType value) {
        return new JAXBElement<ExpressionTransformerType>(_ExpressionTransformer_QNAME, ExpressionTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "value-extractor-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<ValueExtractorTransformerType> createValueExtractorTransformer(ValueExtractorTransformerType value) {
        return new JAXBElement<ValueExtractorTransformerType>(_ValueExtractorTransformer_QNAME, ValueExtractorTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueueProfileType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link QueueProfileType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "queue-profile", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-queue-profile")
    public JAXBElement<QueueProfileType> createQueueProfile(QueueProfileType value) {
        return new JAXBElement<QueueProfileType>(_QueueProfile_QNAME, QueueProfileType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractSecurityManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractSecurityManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-security-manager")
    public JAXBElement<AbstractSecurityManagerType> createAbstractSecurityManager(AbstractSecurityManagerType value) {
        return new JAXBElement<AbstractSecurityManagerType>(_AbstractSecurityManager_QNAME, AbstractSecurityManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecurityManagerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SecurityManagerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "security-manager", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-security-manager")
    public JAXBElement<SecurityManagerType> createSecurityManager(SecurityManagerType value) {
        return new JAXBElement<SecurityManagerType>(_SecurityManager_QNAME, SecurityManagerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ThreadingProfileType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ThreadingProfileType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "threading-profile", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-service-threading-profile")
    public JAXBElement<ThreadingProfileType> createThreadingProfile(ThreadingProfileType value) {
        return new JAXBElement<ThreadingProfileType>(_ThreadingProfile_QNAME, ThreadingProfileType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "default-exception-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "legacy-abstract-exception-strategy")
    public JAXBElement<ServiceExceptionStrategyType> createDefaultExceptionStrategy(ServiceExceptionStrategyType value) {
        return new JAXBElement<ServiceExceptionStrategyType>(_DefaultExceptionStrategy_QNAME, ServiceExceptionStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "catch-exception-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-exception-strategy")
    public JAXBElement<CatchExceptionStrategyType> createCatchExceptionStrategy(CatchExceptionStrategyType value) {
        return new JAXBElement<CatchExceptionStrategyType>(_CatchExceptionStrategy_QNAME, CatchExceptionStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "choice-exception-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-exception-strategy")
    public JAXBElement<ChoiceExceptionStrategyType> createChoiceExceptionStrategy(ChoiceExceptionStrategyType value) {
        return new JAXBElement<ChoiceExceptionStrategyType>(_ChoiceExceptionStrategy_QNAME, ChoiceExceptionStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "rollback-exception-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-exception-strategy")
    public JAXBElement<RollbackExceptionStrategyType> createRollbackExceptionStrategy(RollbackExceptionStrategyType value) {
        return new JAXBElement<RollbackExceptionStrategyType>(_RollbackExceptionStrategy_QNAME, RollbackExceptionStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "exception-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-exception-strategy")
    public JAXBElement<ReferenceExceptionStrategyType> createExceptionStrategy(ReferenceExceptionStrategyType value) {
        return new JAXBElement<ReferenceExceptionStrategyType>(_ExceptionStrategy_QNAME, ReferenceExceptionStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-exception-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-exception-strategy")
    public JAXBElement<CustomExceptionStrategyType> createCustomExceptionStrategy(CustomExceptionStrategyType value) {
        return new JAXBElement<CustomExceptionStrategyType>(_CustomExceptionStrategy_QNAME, CustomExceptionStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "default-service-exception-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "legacy-abstract-exception-strategy")
    public JAXBElement<ServiceExceptionStrategyType> createDefaultServiceExceptionStrategy(ServiceExceptionStrategyType value) {
        return new JAXBElement<ServiceExceptionStrategyType>(_DefaultServiceExceptionStrategy_QNAME, ServiceExceptionStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-connector")
    public JAXBElement<CustomConnectorType> createCustomConnector(CustomConnectorType value) {
        return new JAXBElement<CustomConnectorType>(_CustomConnector_QNAME, CustomConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractObjectStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractObjectStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-object-store")
    public JAXBElement<AbstractObjectStoreType> createAbstractObjectStore(AbstractObjectStoreType value) {
        return new JAXBElement<AbstractObjectStoreType>(_AbstractObjectStore_QNAME, AbstractObjectStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractMonitoredObjectStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractMonitoredObjectStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "in-memory-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-object-store")
    public JAXBElement<AbstractMonitoredObjectStoreType> createInMemoryStore(AbstractMonitoredObjectStoreType value) {
        return new JAXBElement<AbstractMonitoredObjectStoreType>(_InMemoryStore_QNAME, AbstractMonitoredObjectStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TextFileObjectStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TextFileObjectStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "simple-text-file-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-object-store")
    public JAXBElement<TextFileObjectStoreType> createSimpleTextFileStore(TextFileObjectStoreType value) {
        return new JAXBElement<TextFileObjectStoreType>(_SimpleTextFileStore_QNAME, TextFileObjectStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManagedObjectStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ManagedObjectStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "managed-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-object-store")
    public JAXBElement<ManagedObjectStoreType> createManagedStore(ManagedObjectStoreType value) {
        return new JAXBElement<ManagedObjectStoreType>(_ManagedStore_QNAME, ManagedObjectStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomObjectStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomObjectStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-object-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-object-store")
    public JAXBElement<CustomObjectStoreType> createCustomObjectStore(CustomObjectStoreType value) {
        return new JAXBElement<CustomObjectStoreType>(_CustomObjectStore_QNAME, CustomObjectStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SpringObjectStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SpringObjectStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "spring-object-store", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-object-store")
    public JAXBElement<SpringObjectStoreType> createSpringObjectStore(SpringObjectStoreType value) {
        return new JAXBElement<SpringObjectStoreType>(_SpringObjectStore_QNAME, SpringObjectStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-global-intercepting-message-processor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<AbstractGlobalInterceptingMessageProcessorType> createAbstractGlobalInterceptingMessageProcessor(AbstractGlobalInterceptingMessageProcessorType value) {
        return new JAXBElement<AbstractGlobalInterceptingMessageProcessorType>(_AbstractGlobalInterceptingMessageProcessor_QNAME, AbstractGlobalInterceptingMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "message-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-intercepting-message-processor")
    public JAXBElement<MessageFilterType> createMessageFilter(MessageFilterType value) {
        return new JAXBElement<MessageFilterType>(_MessageFilter_QNAME, MessageFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "idempotent-message-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-intercepting-message-processor")
    public JAXBElement<IdempotentMessageFilterType> createIdempotentMessageFilter(IdempotentMessageFilterType value) {
        return new JAXBElement<IdempotentMessageFilterType>(_IdempotentMessageFilter_QNAME, IdempotentMessageFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractRedeliveryPolicyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractRedeliveryPolicyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-redelivery-policy")
    public JAXBElement<AbstractRedeliveryPolicyType> createAbstractRedeliveryPolicy(AbstractRedeliveryPolicyType value) {
        return new JAXBElement<AbstractRedeliveryPolicyType>(_AbstractRedeliveryPolicy_QNAME, AbstractRedeliveryPolicyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdempotentRedeliveryPolicyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IdempotentRedeliveryPolicyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "idempotent-redelivery-policy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-redelivery-policy")
    public JAXBElement<IdempotentRedeliveryPolicyType> createIdempotentRedeliveryPolicy(IdempotentRedeliveryPolicyType value) {
        return new JAXBElement<IdempotentRedeliveryPolicyType>(_IdempotentRedeliveryPolicy_QNAME, IdempotentRedeliveryPolicyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "idempotent-secure-hash-message-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-intercepting-message-processor")
    public JAXBElement<IdempotentSecureHashMessageFilter> createIdempotentSecureHashMessageFilter(IdempotentSecureHashMessageFilter value) {
        return new JAXBElement<IdempotentSecureHashMessageFilter>(_IdempotentSecureHashMessageFilter_QNAME, IdempotentSecureHashMessageFilter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WireTap }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WireTap }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "wire-tap", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<WireTap> createWireTap(WireTap value) {
        return new JAXBElement<WireTap>(_WireTap_QNAME, WireTap.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "combine-collections-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-intercepting-message-processor")
    public JAXBElement<CombineCollectionsTransformer> createCombineCollectionsTransformer(CombineCollectionsTransformer value) {
        return new JAXBElement<CombineCollectionsTransformer>(_CombineCollectionsTransformer_QNAME, CombineCollectionsTransformer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "resequencer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<BaseAggregatorType> createResequencer(BaseAggregatorType value) {
        return new JAXBElement<BaseAggregatorType>(_Resequencer_QNAME, BaseAggregatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "collection-aggregator", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<BaseAggregatorType> createCollectionAggregator(BaseAggregatorType value) {
        return new JAXBElement<BaseAggregatorType>(_CollectionAggregator_QNAME, BaseAggregatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "message-chunk-aggregator", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<BaseAggregatorType> createMessageChunkAggregator(BaseAggregatorType value) {
        return new JAXBElement<BaseAggregatorType>(_MessageChunkAggregator_QNAME, BaseAggregatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-aggregator", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<CustomAggregator> createCustomAggregator(CustomAggregator value) {
        return new JAXBElement<CustomAggregator>(_CustomAggregator_QNAME, CustomAggregator.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "splitter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<Splitter> createSplitter(Splitter value) {
        return new JAXBElement<Splitter>(_Splitter_QNAME, Splitter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "collection-splitter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<CollectionSplitter> createCollectionSplitter(CollectionSplitter value) {
        return new JAXBElement<CollectionSplitter>(_CollectionSplitter_QNAME, CollectionSplitter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "map-splitter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<MapSplitter> createMapSplitter(MapSplitter value) {
        return new JAXBElement<MapSplitter>(_MapSplitter_QNAME, MapSplitter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "message-chunk-splitter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<MessageChunkSplitter> createMessageChunkSplitter(MessageChunkSplitter value) {
        return new JAXBElement<MessageChunkSplitter>(_MessageChunkSplitter_QNAME, MessageChunkSplitter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-splitter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<CustomSplitter> createCustomSplitter(CustomSplitter value) {
        return new JAXBElement<CustomSplitter>(_CustomSplitter_QNAME, CustomSplitter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "foreach", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-intercepting-message-processor")
    public JAXBElement<ForeachProcessorType> createForeach(ForeachProcessorType value) {
        return new JAXBElement<ForeachProcessorType>(_Foreach_QNAME, ForeachProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractRoutingMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractRoutingMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-routing-message-processor", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<AbstractRoutingMessageProcessorType> createAbstractRoutingMessageProcessor(AbstractRoutingMessageProcessorType value) {
        return new JAXBElement<AbstractRoutingMessageProcessorType>(_AbstractRoutingMessageProcessor_QNAME, AbstractRoutingMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomRouter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomRouter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<CustomRouter> createCustomRouter(CustomRouter value) {
        return new JAXBElement<CustomRouter>(_CustomRouter_QNAME, CustomRouter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectiveOutboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SelectiveOutboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "choice", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<SelectiveOutboundRouterType> createChoice(SelectiveOutboundRouterType value) {
        return new JAXBElement<SelectiveOutboundRouterType>(_Choice_QNAME, SelectiveOutboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseMultipleRoutesRoutingMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BaseMultipleRoutesRoutingMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "all", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<BaseMultipleRoutesRoutingMessageProcessorType> createAll(BaseMultipleRoutesRoutingMessageProcessorType value) {
        return new JAXBElement<BaseMultipleRoutesRoutingMessageProcessorType>(_All_QNAME, BaseMultipleRoutesRoutingMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirstSuccessful }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FirstSuccessful }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "first-successful", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<FirstSuccessful> createFirstSuccessful(FirstSuccessful value) {
        return new JAXBElement<FirstSuccessful>(_FirstSuccessful_QNAME, FirstSuccessful.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UntilSuccessful }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UntilSuccessful }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "until-successful", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<UntilSuccessful> createUntilSuccessful(UntilSuccessful value) {
        return new JAXBElement<UntilSuccessful>(_UntilSuccessful_QNAME, UntilSuccessful.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScatterGather }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ScatterGather }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "scatter-gather", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<ScatterGather> createScatterGather(ScatterGather value) {
        return new JAXBElement<ScatterGather>(_ScatterGather_QNAME, ScatterGather.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessorWithAtLeastOneTargetType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ProcessorWithAtLeastOneTargetType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "round-robin", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<ProcessorWithAtLeastOneTargetType> createRoundRobin(ProcessorWithAtLeastOneTargetType value) {
        return new JAXBElement<ProcessorWithAtLeastOneTargetType>(_RoundRobin_QNAME, ProcessorWithAtLeastOneTargetType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecipientList }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RecipientList }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "recipient-list", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<RecipientList> createRecipientList(RecipientList value) {
        return new JAXBElement<RecipientList>(_RecipientList_QNAME, RecipientList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DynamicRoundRobin }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DynamicRoundRobin }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "dynamic-round-robin", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<DynamicRoundRobin> createDynamicRoundRobin(DynamicRoundRobin value) {
        return new JAXBElement<DynamicRoundRobin>(_DynamicRoundRobin_QNAME, DynamicRoundRobin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DynamicAll }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DynamicAll }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "dynamic-all", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<DynamicAll> createDynamicAll(DynamicAll value) {
        return new JAXBElement<DynamicAll>(_DynamicAll_QNAME, DynamicAll.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DynamicFirstSuccessful }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DynamicFirstSuccessful }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "dynamic-first-successful", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-routing-message-processor")
    public JAXBElement<DynamicFirstSuccessful> createDynamicFirstSuccessful(DynamicFirstSuccessful value) {
        return new JAXBElement<DynamicFirstSuccessful>(_DynamicFirstSuccessful_QNAME, DynamicFirstSuccessful.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-dynamic-route-resolver")
    public JAXBElement<Object> createAbstractDynamicRouteResolver(Object value) {
        return new JAXBElement<Object>(_AbstractDynamicRouteResolver_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomRouterResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomRouterResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-route-resolver", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-dynamic-route-resolver")
    public JAXBElement<CustomRouterResolverType> createCustomRouteResolver(CustomRouterResolverType value) {
        return new JAXBElement<CustomRouterResolverType>(_CustomRouteResolver_QNAME, CustomRouterResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdempotentReceiverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IdempotentReceiverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "idempotent-receiver-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-router")
    public JAXBElement<IdempotentReceiverType> createIdempotentReceiverRouter(IdempotentReceiverType value) {
        return new JAXBElement<IdempotentReceiverType>(_IdempotentReceiverRouter_QNAME, IdempotentReceiverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdempotentSecureHashReceiverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IdempotentSecureHashReceiverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "idempotent-secure-hash-receiver-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-router")
    public JAXBElement<IdempotentSecureHashReceiverType> createIdempotentSecureHashReceiverRouter(IdempotentSecureHashReceiverType value) {
        return new JAXBElement<IdempotentSecureHashReceiverType>(_IdempotentSecureHashReceiverRouter_QNAME, IdempotentSecureHashReceiverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WireTapRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WireTapRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "wire-tap-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-router")
    public JAXBElement<WireTapRouterType> createWireTapRouter(WireTapRouterType value) {
        return new JAXBElement<WireTapRouterType>(_WireTapRouter_QNAME, WireTapRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractInboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractInboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "forwarding-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-router")
    public JAXBElement<AbstractInboundRouterType> createForwardingRouter(AbstractInboundRouterType value) {
        return new JAXBElement<AbstractInboundRouterType>(_ForwardingRouter_QNAME, AbstractInboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FilteredInboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FilteredInboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "selective-consumer-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-router")
    public JAXBElement<FilteredInboundRouterType> createSelectiveConsumerRouter(FilteredInboundRouterType value) {
        return new JAXBElement<FilteredInboundRouterType>(_SelectiveConsumerRouter_QNAME, FilteredInboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseAggregatorRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BaseAggregatorRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "correlation-resequencer-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-router")
    public JAXBElement<BaseAggregatorRouterType> createCorrelationResequencerRouter(BaseAggregatorRouterType value) {
        return new JAXBElement<BaseAggregatorRouterType>(_CorrelationResequencerRouter_QNAME, BaseAggregatorRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageChunkingAggregatorRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MessageChunkingAggregatorRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "message-chunking-aggregator-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-router")
    public JAXBElement<MessageChunkingAggregatorRouterType> createMessageChunkingAggregatorRouter(MessageChunkingAggregatorRouterType value) {
        return new JAXBElement<MessageChunkingAggregatorRouterType>(_MessageChunkingAggregatorRouter_QNAME, MessageChunkingAggregatorRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomCorrelationAggregatorRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomCorrelationAggregatorRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-correlation-aggregator-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-router")
    public JAXBElement<CustomCorrelationAggregatorRouterType> createCustomCorrelationAggregatorRouter(CustomCorrelationAggregatorRouterType value) {
        return new JAXBElement<CustomCorrelationAggregatorRouterType>(_CustomCorrelationAggregatorRouter_QNAME, CustomCorrelationAggregatorRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseAggregatorRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BaseAggregatorRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "collection-aggregator-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-router")
    public JAXBElement<BaseAggregatorRouterType> createCollectionAggregatorRouter(BaseAggregatorRouterType value) {
        return new JAXBElement<BaseAggregatorRouterType>(_CollectionAggregatorRouter_QNAME, BaseAggregatorRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomInboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomInboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-inbound-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-router")
    public JAXBElement<CustomInboundRouterType> createCustomInboundRouter(CustomInboundRouterType value) {
        return new JAXBElement<CustomInboundRouterType>(_CustomInboundRouter_QNAME, CustomInboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AsyncReplyRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AsyncReplyRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "single-async-reply-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-async-reply-router")
    public JAXBElement<AsyncReplyRouterType> createSingleAsyncReplyRouter(AsyncReplyRouterType value) {
        return new JAXBElement<AsyncReplyRouterType>(_SingleAsyncReplyRouter_QNAME, AsyncReplyRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AsyncReplyRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AsyncReplyRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "collection-async-reply-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-async-reply-router")
    public JAXBElement<AsyncReplyRouterType> createCollectionAsyncReplyRouter(AsyncReplyRouterType value) {
        return new JAXBElement<AsyncReplyRouterType>(_CollectionAsyncReplyRouter_QNAME, AsyncReplyRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomAsyncReplyRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomAsyncReplyRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-async-reply-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-async-reply-router")
    public JAXBElement<CustomAsyncReplyRouterType> createCustomAsyncReplyRouter(CustomAsyncReplyRouterType value) {
        return new JAXBElement<CustomAsyncReplyRouterType>(_CustomAsyncReplyRouter_QNAME, CustomAsyncReplyRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OutboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "pass-through-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<OutboundRouterType> createPassThroughRouter(OutboundRouterType value) {
        return new JAXBElement<OutboundRouterType>(_PassThroughRouter_QNAME, OutboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SingleEndpointFilteringOutboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SingleEndpointFilteringOutboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "filtering-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<SingleEndpointFilteringOutboundRouterType> createFilteringRouter(SingleEndpointFilteringOutboundRouterType value) {
        return new JAXBElement<SingleEndpointFilteringOutboundRouterType>(_FilteringRouter_QNAME, SingleEndpointFilteringOutboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "chaining-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<MultipleEndpointFilteringOutboundRouterType> createChainingRouter(MultipleEndpointFilteringOutboundRouterType value) {
        return new JAXBElement<MultipleEndpointFilteringOutboundRouterType>(_ChainingRouter_QNAME, MultipleEndpointFilteringOutboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "exception-based-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<MultipleEndpointFilteringOutboundRouterType> createExceptionBasedRouter(MultipleEndpointFilteringOutboundRouterType value) {
        return new JAXBElement<MultipleEndpointFilteringOutboundRouterType>(_ExceptionBasedRouter_QNAME, MultipleEndpointFilteringOutboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExpressionRecipientListRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExpressionRecipientListRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "recipient-list-exception-based-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<ExpressionRecipientListRouterType> createRecipientListExceptionBasedRouter(ExpressionRecipientListRouterType value) {
        return new JAXBElement<ExpressionRecipientListRouterType>(_RecipientListExceptionBasedRouter_QNAME, ExpressionRecipientListRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "multicasting-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<MultipleEndpointFilteringOutboundRouterType> createMulticastingRouter(MultipleEndpointFilteringOutboundRouterType value) {
        return new JAXBElement<MultipleEndpointFilteringOutboundRouterType>(_MulticastingRouter_QNAME, MultipleEndpointFilteringOutboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "sequence-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<MultipleEndpointFilteringOutboundRouterType> createSequenceRouter(MultipleEndpointFilteringOutboundRouterType value) {
        return new JAXBElement<MultipleEndpointFilteringOutboundRouterType>(_SequenceRouter_QNAME, MultipleEndpointFilteringOutboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EndpointSelectorRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EndpointSelectorRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "endpoint-selector-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<EndpointSelectorRouterType> createEndpointSelectorRouter(EndpointSelectorRouterType value) {
        return new JAXBElement<EndpointSelectorRouterType>(_EndpointSelectorRouter_QNAME, EndpointSelectorRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoundRobinSplitterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RoundRobinSplitterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "list-message-splitter-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<RoundRobinSplitterType> createListMessageSplitterRouter(RoundRobinSplitterType value) {
        return new JAXBElement<RoundRobinSplitterType>(_ListMessageSplitterRouter_QNAME, RoundRobinSplitterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExpressionSplitterOutboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExpressionSplitterOutboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "expression-splitter-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<ExpressionSplitterOutboundRouterType> createExpressionSplitterRouter(ExpressionSplitterOutboundRouterType value) {
        return new JAXBElement<ExpressionSplitterOutboundRouterType>(_ExpressionSplitterRouter_QNAME, ExpressionSplitterOutboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChunkingRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ChunkingRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "message-chunking-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<ChunkingRouterType> createMessageChunkingRouter(ChunkingRouterType value) {
        return new JAXBElement<ChunkingRouterType>(_MessageChunkingRouter_QNAME, ChunkingRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StaticRecipientListRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link StaticRecipientListRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "static-recipient-list-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<StaticRecipientListRouterType> createStaticRecipientListRouter(StaticRecipientListRouterType value) {
        return new JAXBElement<StaticRecipientListRouterType>(_StaticRecipientListRouter_QNAME, StaticRecipientListRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExpressionOrStaticRecipientListRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExpressionOrStaticRecipientListRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "expression-recipient-list-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<ExpressionOrStaticRecipientListRouterType> createExpressionRecipientListRouter(ExpressionOrStaticRecipientListRouterType value) {
        return new JAXBElement<ExpressionOrStaticRecipientListRouterType>(_ExpressionRecipientListRouter_QNAME, ExpressionOrStaticRecipientListRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomOutboundRouterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomOutboundRouterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-outbound-router", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-router")
    public JAXBElement<CustomOutboundRouterType> createCustomOutboundRouter(CustomOutboundRouterType value) {
        return new JAXBElement<CustomOutboundRouterType>(_CustomOutboundRouter_QNAME, CustomOutboundRouterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoggingCatchAllStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LoggingCatchAllStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "logging-catch-all-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-catch-all-strategy")
    public JAXBElement<LoggingCatchAllStrategyType> createLoggingCatchAllStrategy(LoggingCatchAllStrategyType value) {
        return new JAXBElement<LoggingCatchAllStrategyType>(_LoggingCatchAllStrategy_QNAME, LoggingCatchAllStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomCatchAllStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomCatchAllStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-catch-all-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-catch-all-strategy")
    public JAXBElement<CustomCatchAllStrategyType> createCustomCatchAllStrategy(CustomCatchAllStrategyType value) {
        return new JAXBElement<CustomCatchAllStrategyType>(_CustomCatchAllStrategy_QNAME, CustomCatchAllStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ForwardingCatchAllStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ForwardingCatchAllStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "forwarding-catch-all-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-catch-all-strategy")
    public JAXBElement<ForwardingCatchAllStrategyType> createForwardingCatchAllStrategy(ForwardingCatchAllStrategyType value) {
        return new JAXBElement<ForwardingCatchAllStrategyType>(_ForwardingCatchAllStrategy_QNAME, ForwardingCatchAllStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomForwardingCatchAllStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomForwardingCatchAllStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-forwarding-catch-all-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-catch-all-strategy")
    public JAXBElement<CustomForwardingCatchAllStrategyType> createCustomForwardingCatchAllStrategy(CustomForwardingCatchAllStrategyType value) {
        return new JAXBElement<CustomForwardingCatchAllStrategyType>(_CustomForwardingCatchAllStrategy_QNAME, CustomForwardingCatchAllStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-message-info-mapping")
    public JAXBElement<AbstractMessageInfoMappingType> createAbstractMessageInfoMapping(AbstractMessageInfoMappingType value) {
        return new JAXBElement<AbstractMessageInfoMappingType>(_AbstractMessageInfoMapping_QNAME, AbstractMessageInfoMappingType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExpressionMessageInfoMappingType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExpressionMessageInfoMappingType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "expression-message-info-mapping", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-info-mapping")
    public JAXBElement<ExpressionMessageInfoMappingType> createExpressionMessageInfoMapping(ExpressionMessageInfoMappingType value) {
        return new JAXBElement<ExpressionMessageInfoMappingType>(_ExpressionMessageInfoMapping_QNAME, ExpressionMessageInfoMappingType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomMessageInfoMappingType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomMessageInfoMappingType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-message-info-mapping", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-info-mapping")
    public JAXBElement<CustomMessageInfoMappingType> createCustomMessageInfoMapping(CustomMessageInfoMappingType value) {
        return new JAXBElement<CustomMessageInfoMappingType>(_CustomMessageInfoMapping_QNAME, CustomMessageInfoMappingType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractLifecycleAdapterFactory }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractLifecycleAdapterFactory }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-lifecycle-adapter-factory")
    public JAXBElement<AbstractLifecycleAdapterFactory> createAbstractLifecycleAdapterFactory(AbstractLifecycleAdapterFactory value) {
        return new JAXBElement<AbstractLifecycleAdapterFactory>(_AbstractLifecycleAdapterFactory_QNAME, AbstractLifecycleAdapterFactory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomLifecycleAdapterFactory }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomLifecycleAdapterFactory }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "custom-lifecycle-adapter-factory", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-lifecycle-adapter-factory")
    public JAXBElement<CustomLifecycleAdapterFactory> createCustomLifecycleAdapterFactory(CustomLifecycleAdapterFactory value) {
        return new JAXBElement<CustomLifecycleAdapterFactory>(_CustomLifecycleAdapterFactory_QNAME, CustomLifecycleAdapterFactory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractObjectFactoryType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractObjectFactoryType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-object-factory")
    public JAXBElement<AbstractObjectFactoryType> createAbstractObjectFactory(AbstractObjectFactoryType value) {
        return new JAXBElement<AbstractObjectFactoryType>(_AbstractObjectFactory_QNAME, AbstractObjectFactoryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SpringBeanLookupType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SpringBeanLookupType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "spring-object", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-object-factory")
    public JAXBElement<SpringBeanLookupType> createSpringObject(SpringBeanLookupType value) {
        return new JAXBElement<SpringBeanLookupType>(_SpringObject_QNAME, SpringBeanLookupType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SingletonObjectFactoryType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SingletonObjectFactoryType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "singleton-object", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-object-factory")
    public JAXBElement<SingletonObjectFactoryType> createSingletonObject(SingletonObjectFactoryType value) {
        return new JAXBElement<SingletonObjectFactoryType>(_SingletonObject_QNAME, SingletonObjectFactoryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrototypeObjectFactoryType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PrototypeObjectFactoryType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "prototype-object", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-object-factory")
    public JAXBElement<PrototypeObjectFactoryType> createPrototypeObject(PrototypeObjectFactoryType value) {
        return new JAXBElement<PrototypeObjectFactoryType>(_PrototypeObject_QNAME, PrototypeObjectFactoryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractPoolingProfileType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractPoolingProfileType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-pooling-profile")
    public JAXBElement<AbstractPoolingProfileType> createAbstractPoolingProfile(AbstractPoolingProfileType value) {
        return new JAXBElement<AbstractPoolingProfileType>(_AbstractPoolingProfile_QNAME, AbstractPoolingProfileType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PoolingProfileType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PoolingProfileType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "pooling-profile", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-pooling-profile")
    public JAXBElement<PoolingProfileType> createPoolingProfile(PoolingProfileType value) {
        return new JAXBElement<PoolingProfileType>(_PoolingProfile_QNAME, PoolingProfileType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MapType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MapType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "properties")
    public JAXBElement<MapType> createProperties(MapType value) {
        return new JAXBElement<MapType>(_Properties_QNAME, MapType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractCachingStrategyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractCachingStrategyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "abstract-caching-strategy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-extension")
    public JAXBElement<AbstractCachingStrategyType> createAbstractCachingStrategy(AbstractCachingStrategyType value) {
        return new JAXBElement<AbstractCachingStrategyType>(_AbstractCachingStrategy_QNAME, AbstractCachingStrategyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnnotationsType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AnnotationsType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "annotations", scope = AnnotatedMixedContentType.class)
    public JAXBElement<AnnotationsType> createAnnotatedMixedContentTypeAnnotations(AnnotationsType value) {
        return new JAXBElement<AnnotationsType>(_AnnotatedMixedContentTypeAnnotations_QNAME, AnnotationsType.class, AnnotatedMixedContentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefInterceptorStackType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RefInterceptorStackType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "interceptor-stack", scope = AbstractComponentType.class)
    public JAXBElement<RefInterceptorStackType> createAbstractComponentTypeInterceptorStack(RefInterceptorStackType value) {
        return new JAXBElement<RefInterceptorStackType>(_InterceptorStack_QNAME, RefInterceptorStackType.class, AbstractComponentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FlowType.Response }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FlowType.Response }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "response", scope = FlowType.class)
    public JAXBElement<FlowType.Response> createFlowTypeResponse(FlowType.Response value) {
        return new JAXBElement<FlowType.Response>(_FlowTypeResponse_QNAME, FlowType.Response.class, FlowType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnnotationsType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AnnotationsType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "annotations", scope = ExpressionComponent.class)
    public JAXBElement<AnnotationsType> createExpressionComponentAnnotations(AnnotationsType value) {
        return new JAXBElement<AnnotationsType>(_AnnotatedMixedContentTypeAnnotations_QNAME, AnnotationsType.class, ExpressionComponent.class, value);
    }

}
