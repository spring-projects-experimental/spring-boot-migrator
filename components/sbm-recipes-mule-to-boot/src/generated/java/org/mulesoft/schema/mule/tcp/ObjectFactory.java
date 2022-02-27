
package org.mulesoft.schema.mule.tcp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.tcp package. 
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

    private final static QName _Connector_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "connector");
    private final static QName _PollingConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "polling-connector");
    private final static QName _AbstractProtocol_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "abstract-protocol");
    private final static QName _StreamingProtocol_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "streaming-protocol");
    private final static QName _XmlProtocol_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "xml-protocol");
    private final static QName _XmlEofProtocol_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "xml-eof-protocol");
    private final static QName _EofProtocol_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "eof-protocol");
    private final static QName _DirectProtocol_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "direct-protocol");
    private final static QName _SafeProtocol_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "safe-protocol");
    private final static QName _CustomClassLoadingProtocol_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "custom-class-loading-protocol");
    private final static QName _LengthProtocol_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "length-protocol");
    private final static QName _CustomProtocol_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "custom-protocol");
    private final static QName _InboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "inbound-endpoint");
    private final static QName _OutboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "outbound-endpoint");
    private final static QName _Endpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "endpoint");
    private final static QName _ClientSocketProperties_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "client-socket-properties");
    private final static QName _ServerSocketProperties_QNAME = new QName("http://www.mulesoft.org/schema/mule/tcp", "server-socket-properties");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.tcp
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TcpConnectorType }
     * 
     */
    public TcpConnectorType createTcpConnectorType() {
        return new TcpConnectorType();
    }

    /**
     * Create an instance of {@link PollingTcpConnectorType }
     * 
     */
    public PollingTcpConnectorType createPollingTcpConnectorType() {
        return new PollingTcpConnectorType();
    }

    /**
     * Create an instance of {@link AbstractProtocolType }
     * 
     */
    public AbstractProtocolType createAbstractProtocolType() {
        return new AbstractProtocolType();
    }

    /**
     * Create an instance of {@link ByteOrMessageProtocolType }
     * 
     */
    public ByteOrMessageProtocolType createByteOrMessageProtocolType() {
        return new ByteOrMessageProtocolType();
    }

    /**
     * Create an instance of {@link LengthProtocolType }
     * 
     */
    public LengthProtocolType createLengthProtocolType() {
        return new LengthProtocolType();
    }

    /**
     * Create an instance of {@link CustomClassLoadingProtocolType }
     * 
     */
    public CustomClassLoadingProtocolType createCustomClassLoadingProtocolType() {
        return new CustomClassLoadingProtocolType();
    }

    /**
     * Create an instance of {@link CustomProtocolType }
     * 
     */
    public CustomProtocolType createCustomProtocolType() {
        return new CustomProtocolType();
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
     * Create an instance of {@link TcpClientSocketPropertiesType }
     * 
     */
    public TcpClientSocketPropertiesType createTcpClientSocketPropertiesType() {
        return new TcpClientSocketPropertiesType();
    }

    /**
     * Create an instance of {@link TcpServerSocketPropertiesType }
     * 
     */
    public TcpServerSocketPropertiesType createTcpServerSocketPropertiesType() {
        return new TcpServerSocketPropertiesType();
    }

    /**
     * Create an instance of {@link NoProtocolTcpConnectorType }
     * 
     */
    public NoProtocolTcpConnectorType createNoProtocolTcpConnectorType() {
        return new NoProtocolTcpConnectorType();
    }

    /**
     * Create an instance of {@link TcpAbstractSocketPropertiesType }
     * 
     */
    public TcpAbstractSocketPropertiesType createTcpAbstractSocketPropertiesType() {
        return new TcpAbstractSocketPropertiesType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TcpConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TcpConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-connector")
    public JAXBElement<TcpConnectorType> createConnector(TcpConnectorType value) {
        return new JAXBElement<TcpConnectorType>(_Connector_QNAME, TcpConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PollingTcpConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PollingTcpConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "polling-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-connector")
    public JAXBElement<PollingTcpConnectorType> createPollingConnector(PollingTcpConnectorType value) {
        return new JAXBElement<PollingTcpConnectorType>(_PollingConnector_QNAME, PollingTcpConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "abstract-protocol")
    public JAXBElement<AbstractProtocolType> createAbstractProtocol(AbstractProtocolType value) {
        return new JAXBElement<AbstractProtocolType>(_AbstractProtocol_QNAME, AbstractProtocolType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "streaming-protocol", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/tcp", substitutionHeadName = "abstract-protocol")
    public JAXBElement<AbstractProtocolType> createStreamingProtocol(AbstractProtocolType value) {
        return new JAXBElement<AbstractProtocolType>(_StreamingProtocol_QNAME, AbstractProtocolType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "xml-protocol", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/tcp", substitutionHeadName = "abstract-protocol")
    public JAXBElement<AbstractProtocolType> createXmlProtocol(AbstractProtocolType value) {
        return new JAXBElement<AbstractProtocolType>(_XmlProtocol_QNAME, AbstractProtocolType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "xml-eof-protocol", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/tcp", substitutionHeadName = "abstract-protocol")
    public JAXBElement<AbstractProtocolType> createXmlEofProtocol(AbstractProtocolType value) {
        return new JAXBElement<AbstractProtocolType>(_XmlEofProtocol_QNAME, AbstractProtocolType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ByteOrMessageProtocolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ByteOrMessageProtocolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "eof-protocol", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/tcp", substitutionHeadName = "abstract-protocol")
    public JAXBElement<ByteOrMessageProtocolType> createEofProtocol(ByteOrMessageProtocolType value) {
        return new JAXBElement<ByteOrMessageProtocolType>(_EofProtocol_QNAME, ByteOrMessageProtocolType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ByteOrMessageProtocolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ByteOrMessageProtocolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "direct-protocol", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/tcp", substitutionHeadName = "abstract-protocol")
    public JAXBElement<ByteOrMessageProtocolType> createDirectProtocol(ByteOrMessageProtocolType value) {
        return new JAXBElement<ByteOrMessageProtocolType>(_DirectProtocol_QNAME, ByteOrMessageProtocolType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LengthProtocolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LengthProtocolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "safe-protocol", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/tcp", substitutionHeadName = "abstract-protocol")
    public JAXBElement<LengthProtocolType> createSafeProtocol(LengthProtocolType value) {
        return new JAXBElement<LengthProtocolType>(_SafeProtocol_QNAME, LengthProtocolType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomClassLoadingProtocolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomClassLoadingProtocolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "custom-class-loading-protocol", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/tcp", substitutionHeadName = "abstract-protocol")
    public JAXBElement<CustomClassLoadingProtocolType> createCustomClassLoadingProtocol(CustomClassLoadingProtocolType value) {
        return new JAXBElement<CustomClassLoadingProtocolType>(_CustomClassLoadingProtocol_QNAME, CustomClassLoadingProtocolType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LengthProtocolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LengthProtocolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "length-protocol", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/tcp", substitutionHeadName = "abstract-protocol")
    public JAXBElement<LengthProtocolType> createLengthProtocol(LengthProtocolType value) {
        return new JAXBElement<LengthProtocolType>(_LengthProtocol_QNAME, LengthProtocolType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomProtocolType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomProtocolType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "custom-protocol", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/tcp", substitutionHeadName = "abstract-protocol")
    public JAXBElement<CustomProtocolType> createCustomProtocol(CustomProtocolType value) {
        return new JAXBElement<CustomProtocolType>(_CustomProtocol_QNAME, CustomProtocolType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "inbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-endpoint")
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
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "outbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-endpoint")
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
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-endpoint")
    public JAXBElement<GlobalEndpointType> createEndpoint(GlobalEndpointType value) {
        return new JAXBElement<GlobalEndpointType>(_Endpoint_QNAME, GlobalEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TcpClientSocketPropertiesType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TcpClientSocketPropertiesType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "client-socket-properties", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-extension")
    public JAXBElement<TcpClientSocketPropertiesType> createClientSocketProperties(TcpClientSocketPropertiesType value) {
        return new JAXBElement<TcpClientSocketPropertiesType>(_ClientSocketProperties_QNAME, TcpClientSocketPropertiesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TcpServerSocketPropertiesType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TcpServerSocketPropertiesType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tcp", name = "server-socket-properties", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-extension")
    public JAXBElement<TcpServerSocketPropertiesType> createServerSocketProperties(TcpServerSocketPropertiesType value) {
        return new JAXBElement<TcpServerSocketPropertiesType>(_ServerSocketProperties_QNAME, TcpServerSocketPropertiesType.class, null, value);
    }

}
