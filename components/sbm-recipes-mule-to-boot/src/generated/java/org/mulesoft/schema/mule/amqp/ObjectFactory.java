
package org.mulesoft.schema.mule.amqp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.mulesoft.schema.mule.core.AbstractTransformerType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.amqp package. 
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

    private final static QName _Connector_QNAME = new QName("http://www.mulesoft.org/schema/mule/amqp", "connector");
    private final static QName _InboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/amqp", "inbound-endpoint");
    private final static QName _OutboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/amqp", "outbound-endpoint");
    private final static QName _Endpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/amqp", "endpoint");
    private final static QName _AcknowledgeMessage_QNAME = new QName("http://www.mulesoft.org/schema/mule/amqp", "acknowledge-message");
    private final static QName _RejectMessage_QNAME = new QName("http://www.mulesoft.org/schema/mule/amqp", "reject-message");
    private final static QName _ReturnHandler_QNAME = new QName("http://www.mulesoft.org/schema/mule/amqp", "return-handler");
    private final static QName _AmqpmessageToObjectTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/amqp", "amqpmessage-to-object-transformer");
    private final static QName _ObjectToAmqpmessageTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/amqp", "object-to-amqpmessage-transformer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.amqp
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AmqpConnectorType }
     * 
     */
    public AmqpConnectorType createAmqpConnectorType() {
        return new AmqpConnectorType();
    }

    /**
     * Create an instance of {@link InboundEndpointType }
     * 
     */
    public InboundEndpointType createInboundEndpointType() {
        return new InboundEndpointType();
    }

    /**
     * Create an instance of {@link OutboundEndpointType }
     * 
     */
    public OutboundEndpointType createOutboundEndpointType() {
        return new OutboundEndpointType();
    }

    /**
     * Create an instance of {@link GlobalEndpointType }
     * 
     */
    public GlobalEndpointType createGlobalEndpointType() {
        return new GlobalEndpointType();
    }

    /**
     * Create an instance of {@link BasicAckType }
     * 
     */
    public BasicAckType createBasicAckType() {
        return new BasicAckType();
    }

    /**
     * Create an instance of {@link BasicRejectType }
     * 
     */
    public BasicRejectType createBasicRejectType() {
        return new BasicRejectType();
    }

    /**
     * Create an instance of {@link ReturnHandlerType }
     * 
     */
    public ReturnHandlerType createReturnHandlerType() {
        return new ReturnHandlerType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AmqpConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AmqpConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/amqp", name = "connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-connector")
    public JAXBElement<AmqpConnectorType> createConnector(AmqpConnectorType value) {
        return new JAXBElement<AmqpConnectorType>(_Connector_QNAME, AmqpConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/amqp", name = "inbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-endpoint")
    public JAXBElement<InboundEndpointType> createInboundEndpoint(InboundEndpointType value) {
        return new JAXBElement<InboundEndpointType>(_InboundEndpoint_QNAME, InboundEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OutboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/amqp", name = "outbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-endpoint")
    public JAXBElement<OutboundEndpointType> createOutboundEndpoint(OutboundEndpointType value) {
        return new JAXBElement<OutboundEndpointType>(_OutboundEndpoint_QNAME, OutboundEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlobalEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GlobalEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/amqp", name = "endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-endpoint")
    public JAXBElement<GlobalEndpointType> createEndpoint(GlobalEndpointType value) {
        return new JAXBElement<GlobalEndpointType>(_Endpoint_QNAME, GlobalEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BasicAckType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BasicAckType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/amqp", name = "acknowledge-message", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<BasicAckType> createAcknowledgeMessage(BasicAckType value) {
        return new JAXBElement<BasicAckType>(_AcknowledgeMessage_QNAME, BasicAckType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BasicRejectType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BasicRejectType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/amqp", name = "reject-message", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<BasicRejectType> createRejectMessage(BasicRejectType value) {
        return new JAXBElement<BasicRejectType>(_RejectMessage_QNAME, BasicRejectType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnHandlerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReturnHandlerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/amqp", name = "return-handler", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<ReturnHandlerType> createReturnHandler(ReturnHandlerType value) {
        return new JAXBElement<ReturnHandlerType>(_ReturnHandler_QNAME, ReturnHandlerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/amqp", name = "amqpmessage-to-object-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createAmqpmessageToObjectTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_AmqpmessageToObjectTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/amqp", name = "object-to-amqpmessage-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createObjectToAmqpmessageTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_ObjectToAmqpmessageTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

}
