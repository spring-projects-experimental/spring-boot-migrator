
package org.mulesoft.schema.mule.ee.wmq;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.mulesoft.schema.mule.core.AbstractMessageInfoMappingType;
import org.mulesoft.schema.mule.core.AbstractTransformerType;
import org.mulesoft.schema.mule.core.BaseTransactionType;
import org.mulesoft.schema.mule.core.KeyValueType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.ee.wmq package. 
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

    private final static QName _XaConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/wmq", "xa-connector");
    private final static QName _Connector_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/wmq", "connector");
    private final static QName _MessageToObjectTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/wmq", "message-to-object-transformer");
    private final static QName _ObjectToMessageTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/wmq", "object-to-message-transformer");
    private final static QName _InboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/wmq", "inbound-endpoint");
    private final static QName _OutboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/wmq", "outbound-endpoint");
    private final static QName _Endpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/wmq", "endpoint");
    private final static QName _Transaction_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/wmq", "transaction");
    private final static QName _MessageInfoMapping_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/wmq", "message-info-mapping");
    private final static QName _GlobalEndpointTypeSelector_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/wmq", "selector");
    private final static QName _GlobalEndpointTypeProperty_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "property");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.ee.wmq
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link WmqConnectorType }
     * 
     */
    public WmqConnectorType createWmqConnectorType() {
        return new WmqConnectorType();
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
     * Create an instance of {@link SelectorFilter }
     * 
     */
    public SelectorFilter createSelectorFilter() {
        return new SelectorFilter();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WmqConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WmqConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "xa-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-connector")
    public JAXBElement<WmqConnectorType> createXaConnector(WmqConnectorType value) {
        return new JAXBElement<WmqConnectorType>(_XaConnector_QNAME, WmqConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WmqConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WmqConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-connector")
    public JAXBElement<WmqConnectorType> createConnector(WmqConnectorType value) {
        return new JAXBElement<WmqConnectorType>(_Connector_QNAME, WmqConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "message-to-object-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createMessageToObjectTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_MessageToObjectTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "object-to-message-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createObjectToMessageTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_ObjectToMessageTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "inbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-endpoint")
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
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "outbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-endpoint")
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
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-endpoint")
    public JAXBElement<GlobalEndpointType> createEndpoint(GlobalEndpointType value) {
        return new JAXBElement<GlobalEndpointType>(_Endpoint_QNAME, GlobalEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "transaction", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction")
    public JAXBElement<BaseTransactionType> createTransaction(BaseTransactionType value) {
        return new JAXBElement<BaseTransactionType>(_Transaction_QNAME, BaseTransactionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "message-info-mapping", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-info-mapping")
    public JAXBElement<AbstractMessageInfoMappingType> createMessageInfoMapping(AbstractMessageInfoMappingType value) {
        return new JAXBElement<AbstractMessageInfoMappingType>(_MessageInfoMapping_QNAME, AbstractMessageInfoMappingType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectorFilter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SelectorFilter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "selector", scope = GlobalEndpointType.class)
    public JAXBElement<SelectorFilter> createGlobalEndpointTypeSelector(SelectorFilter value) {
        return new JAXBElement<SelectorFilter>(_GlobalEndpointTypeSelector_QNAME, SelectorFilter.class, GlobalEndpointType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link KeyValueType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "property", scope = GlobalEndpointType.class)
    public JAXBElement<KeyValueType> createGlobalEndpointTypeProperty(KeyValueType value) {
        return new JAXBElement<KeyValueType>(_GlobalEndpointTypeProperty_QNAME, KeyValueType.class, GlobalEndpointType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectorFilter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SelectorFilter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "selector", scope = OutboundEndpointType.class)
    public JAXBElement<SelectorFilter> createOutboundEndpointTypeSelector(SelectorFilter value) {
        return new JAXBElement<SelectorFilter>(_GlobalEndpointTypeSelector_QNAME, SelectorFilter.class, OutboundEndpointType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link KeyValueType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "property", scope = OutboundEndpointType.class)
    public JAXBElement<KeyValueType> createOutboundEndpointTypeProperty(KeyValueType value) {
        return new JAXBElement<KeyValueType>(_GlobalEndpointTypeProperty_QNAME, KeyValueType.class, OutboundEndpointType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectorFilter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SelectorFilter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", name = "selector", scope = InboundEndpointType.class)
    public JAXBElement<SelectorFilter> createInboundEndpointTypeSelector(SelectorFilter value) {
        return new JAXBElement<SelectorFilter>(_GlobalEndpointTypeSelector_QNAME, SelectorFilter.class, InboundEndpointType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link KeyValueType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "property", scope = InboundEndpointType.class)
    public JAXBElement<KeyValueType> createInboundEndpointTypeProperty(KeyValueType value) {
        return new JAXBElement<KeyValueType>(_GlobalEndpointTypeProperty_QNAME, KeyValueType.class, InboundEndpointType.class, value);
    }

}
