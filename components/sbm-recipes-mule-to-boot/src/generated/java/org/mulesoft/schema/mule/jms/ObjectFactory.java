
package org.mulesoft.schema.mule.jms;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.mulesoft.schema.mule.core.AbstractTransformerType;
import org.mulesoft.schema.mule.core.BaseTransactionType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.jms package. 
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

    private final static QName _Connector_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "connector");
    private final static QName _CustomConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "custom-connector");
    private final static QName _ActivemqConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "activemq-connector");
    private final static QName _ActivemqXaConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "activemq-xa-connector");
    private final static QName _MulemqConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "mulemq-connector");
    private final static QName _MulemqXaConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "mulemq-xa-connector");
    private final static QName _WeblogicConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "weblogic-connector");
    private final static QName _WebsphereConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "websphere-connector");
    private final static QName _Transaction_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "transaction");
    private final static QName _ClientAckTransaction_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "client-ack-transaction");
    private final static QName _JmsmessageToObjectTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "jmsmessage-to-object-transformer");
    private final static QName _ObjectToJmsmessageTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "object-to-jmsmessage-transformer");
    private final static QName _InboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "inbound-endpoint");
    private final static QName _OutboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "outbound-endpoint");
    private final static QName _Endpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "endpoint");
    private final static QName _PropertyFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "property-filter");
    private final static QName _AbstractJndiNameResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "abstract-jndi-name-resolver");
    private final static QName _DefaultJndiNameResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "default-jndi-name-resolver");
    private final static QName _CustomJndiNameResolver_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "custom-jndi-name-resolver");
    private final static QName _CachingConnectionFactory_QNAME = new QName("http://www.mulesoft.org/schema/mule/jms", "caching-connection-factory");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.jms
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GenericConnectorType }
     * 
     */
    public GenericConnectorType createGenericConnectorType() {
        return new GenericConnectorType();
    }

    /**
     * Create an instance of {@link CustomConnector }
     * 
     */
    public CustomConnector createCustomConnector() {
        return new CustomConnector();
    }

    /**
     * Create an instance of {@link ActiveMqConnectorType }
     * 
     */
    public ActiveMqConnectorType createActiveMqConnectorType() {
        return new ActiveMqConnectorType();
    }

    /**
     * Create an instance of {@link MuleMqConnectorType }
     * 
     */
    public MuleMqConnectorType createMuleMqConnectorType() {
        return new MuleMqConnectorType();
    }

    /**
     * Create an instance of {@link VendorJmsConnectorType }
     * 
     */
    public VendorJmsConnectorType createVendorJmsConnectorType() {
        return new VendorJmsConnectorType();
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
     * Create an instance of {@link PropertyFilter }
     * 
     */
    public PropertyFilter createPropertyFilter() {
        return new PropertyFilter();
    }

    /**
     * Create an instance of {@link AbstractJndiNameResolverType }
     * 
     */
    public AbstractJndiNameResolverType createAbstractJndiNameResolverType() {
        return new AbstractJndiNameResolverType();
    }

    /**
     * Create an instance of {@link DefaultJndiNameResolverType }
     * 
     */
    public DefaultJndiNameResolverType createDefaultJndiNameResolverType() {
        return new DefaultJndiNameResolverType();
    }

    /**
     * Create an instance of {@link CustomJndiNameResolverType }
     * 
     */
    public CustomJndiNameResolverType createCustomJndiNameResolverType() {
        return new CustomJndiNameResolverType();
    }

    /**
     * Create an instance of {@link ConnectionFactoryPoolType }
     * 
     */
    public ConnectionFactoryPoolType createConnectionFactoryPoolType() {
        return new ConnectionFactoryPoolType();
    }

    /**
     * Create an instance of {@link JmsSelectorFilter }
     * 
     */
    public JmsSelectorFilter createJmsSelectorFilter() {
        return new JmsSelectorFilter();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenericConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GenericConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-connector")
    public JAXBElement<GenericConnectorType> createConnector(GenericConnectorType value) {
        return new JAXBElement<GenericConnectorType>(_Connector_QNAME, GenericConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomConnector }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomConnector }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "custom-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-connector")
    public JAXBElement<CustomConnector> createCustomConnector(CustomConnector value) {
        return new JAXBElement<CustomConnector>(_CustomConnector_QNAME, CustomConnector.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActiveMqConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ActiveMqConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "activemq-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-connector")
    public JAXBElement<ActiveMqConnectorType> createActivemqConnector(ActiveMqConnectorType value) {
        return new JAXBElement<ActiveMqConnectorType>(_ActivemqConnector_QNAME, ActiveMqConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActiveMqConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ActiveMqConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "activemq-xa-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-connector")
    public JAXBElement<ActiveMqConnectorType> createActivemqXaConnector(ActiveMqConnectorType value) {
        return new JAXBElement<ActiveMqConnectorType>(_ActivemqXaConnector_QNAME, ActiveMqConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MuleMqConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MuleMqConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "mulemq-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-connector")
    public JAXBElement<MuleMqConnectorType> createMulemqConnector(MuleMqConnectorType value) {
        return new JAXBElement<MuleMqConnectorType>(_MulemqConnector_QNAME, MuleMqConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MuleMqConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MuleMqConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "mulemq-xa-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-connector")
    public JAXBElement<MuleMqConnectorType> createMulemqXaConnector(MuleMqConnectorType value) {
        return new JAXBElement<MuleMqConnectorType>(_MulemqXaConnector_QNAME, MuleMqConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VendorJmsConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link VendorJmsConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "weblogic-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-connector")
    public JAXBElement<VendorJmsConnectorType> createWeblogicConnector(VendorJmsConnectorType value) {
        return new JAXBElement<VendorJmsConnectorType>(_WeblogicConnector_QNAME, VendorJmsConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VendorJmsConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link VendorJmsConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "websphere-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-connector")
    public JAXBElement<VendorJmsConnectorType> createWebsphereConnector(VendorJmsConnectorType value) {
        return new JAXBElement<VendorJmsConnectorType>(_WebsphereConnector_QNAME, VendorJmsConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "transaction", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction")
    public JAXBElement<BaseTransactionType> createTransaction(BaseTransactionType value) {
        return new JAXBElement<BaseTransactionType>(_Transaction_QNAME, BaseTransactionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "client-ack-transaction", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transaction")
    public JAXBElement<BaseTransactionType> createClientAckTransaction(BaseTransactionType value) {
        return new JAXBElement<BaseTransactionType>(_ClientAckTransaction_QNAME, BaseTransactionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "jmsmessage-to-object-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createJmsmessageToObjectTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_JmsmessageToObjectTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "object-to-jmsmessage-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createObjectToJmsmessageTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_ObjectToJmsmessageTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "inbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-endpoint")
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
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "outbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-endpoint")
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
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-endpoint")
    public JAXBElement<GlobalEndpointType> createEndpoint(GlobalEndpointType value) {
        return new JAXBElement<GlobalEndpointType>(_Endpoint_QNAME, GlobalEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "property-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<PropertyFilter> createPropertyFilter(PropertyFilter value) {
        return new JAXBElement<PropertyFilter>(_PropertyFilter_QNAME, PropertyFilter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractJndiNameResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractJndiNameResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "abstract-jndi-name-resolver")
    public JAXBElement<AbstractJndiNameResolverType> createAbstractJndiNameResolver(AbstractJndiNameResolverType value) {
        return new JAXBElement<AbstractJndiNameResolverType>(_AbstractJndiNameResolver_QNAME, AbstractJndiNameResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultJndiNameResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DefaultJndiNameResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "default-jndi-name-resolver", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/jms", substitutionHeadName = "abstract-jndi-name-resolver")
    public JAXBElement<DefaultJndiNameResolverType> createDefaultJndiNameResolver(DefaultJndiNameResolverType value) {
        return new JAXBElement<DefaultJndiNameResolverType>(_DefaultJndiNameResolver_QNAME, DefaultJndiNameResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomJndiNameResolverType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomJndiNameResolverType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "custom-jndi-name-resolver", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/jms", substitutionHeadName = "abstract-jndi-name-resolver")
    public JAXBElement<CustomJndiNameResolverType> createCustomJndiNameResolver(CustomJndiNameResolverType value) {
        return new JAXBElement<CustomJndiNameResolverType>(_CustomJndiNameResolver_QNAME, CustomJndiNameResolverType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConnectionFactoryPoolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConnectionFactoryPoolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/jms", name = "caching-connection-factory", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-extension")
    public JAXBElement<ConnectionFactoryPoolType> createCachingConnectionFactory(ConnectionFactoryPoolType value) {
        return new JAXBElement<ConnectionFactoryPoolType>(_CachingConnectionFactory_QNAME, ConnectionFactoryPoolType.class, null, value);
    }

}
