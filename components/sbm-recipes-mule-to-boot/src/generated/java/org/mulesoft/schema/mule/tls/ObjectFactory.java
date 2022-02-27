
package org.mulesoft.schema.mule.tls;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.tls package. 
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

    private final static QName _Connector_QNAME = new QName("http://www.mulesoft.org/schema/mule/tls", "connector");
    private final static QName _InboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/tls", "inbound-endpoint");
    private final static QName _OutboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/tls", "outbound-endpoint");
    private final static QName _Endpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/tls", "endpoint");
    private final static QName _Context_QNAME = new QName("http://www.mulesoft.org/schema/mule/tls", "context");
    private final static QName _TrustStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/tls", "trust-store");
    private final static QName _KeyStore_QNAME = new QName("http://www.mulesoft.org/schema/mule/tls", "key-store");
    private final static QName _TlsContextTypeRevocationCheck_QNAME = new QName("http://www.mulesoft.org/schema/mule/tls", "revocation-check");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.tls
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Connector }
     * 
     */
    public Connector createConnector() {
        return new Connector();
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
     * Create an instance of {@link TlsContextType }
     * 
     */
    public TlsContextType createTlsContextType() {
        return new TlsContextType();
    }

    /**
     * Create an instance of {@link TlsContextTrustStoreType }
     * 
     */
    public TlsContextTrustStoreType createTlsContextTrustStoreType() {
        return new TlsContextTrustStoreType();
    }

    /**
     * Create an instance of {@link TlsContextKeyStoreType }
     * 
     */
    public TlsContextKeyStoreType createTlsContextKeyStoreType() {
        return new TlsContextKeyStoreType();
    }

    /**
     * Create an instance of {@link RcWrapperElement }
     * 
     */
    public RcWrapperElement createRcWrapperElement() {
        return new RcWrapperElement();
    }

    /**
     * Create an instance of {@link RcStandardType }
     * 
     */
    public RcStandardType createRcStandardType() {
        return new RcStandardType();
    }

    /**
     * Create an instance of {@link RcCrlFileType }
     * 
     */
    public RcCrlFileType createRcCrlFileType() {
        return new RcCrlFileType();
    }

    /**
     * Create an instance of {@link RcCustomOcspType }
     * 
     */
    public RcCustomOcspType createRcCustomOcspType() {
        return new RcCustomOcspType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Connector }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Connector }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tls", name = "connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-connector")
    public JAXBElement<Connector> createConnector(Connector value) {
        return new JAXBElement<Connector>(_Connector_QNAME, Connector.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tls", name = "inbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-endpoint")
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
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tls", name = "outbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-endpoint")
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
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tls", name = "endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-endpoint")
    public JAXBElement<GlobalEndpointType> createEndpoint(GlobalEndpointType value) {
        return new JAXBElement<GlobalEndpointType>(_Endpoint_QNAME, GlobalEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TlsContextType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TlsContextType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tls", name = "context", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-extension")
    public JAXBElement<TlsContextType> createContext(TlsContextType value) {
        return new JAXBElement<TlsContextType>(_Context_QNAME, TlsContextType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TlsContextTrustStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TlsContextTrustStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tls", name = "trust-store")
    public JAXBElement<TlsContextTrustStoreType> createTrustStore(TlsContextTrustStoreType value) {
        return new JAXBElement<TlsContextTrustStoreType>(_TrustStore_QNAME, TlsContextTrustStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TlsContextKeyStoreType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TlsContextKeyStoreType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tls", name = "key-store")
    public JAXBElement<TlsContextKeyStoreType> createKeyStore(TlsContextKeyStoreType value) {
        return new JAXBElement<TlsContextKeyStoreType>(_KeyStore_QNAME, TlsContextKeyStoreType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RcWrapperElement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RcWrapperElement }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/tls", name = "revocation-check", scope = TlsContextType.class)
    public JAXBElement<RcWrapperElement> createTlsContextTypeRevocationCheck(RcWrapperElement value) {
        return new JAXBElement<RcWrapperElement>(_TlsContextTypeRevocationCheck_QNAME, RcWrapperElement.class, TlsContextType.class, value);
    }

}
