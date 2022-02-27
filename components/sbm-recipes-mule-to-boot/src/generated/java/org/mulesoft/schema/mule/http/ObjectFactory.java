
package org.mulesoft.schema.mule.http;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.mulesoft.schema.mule.core.AbstractTransformerType;
import org.mulesoft.schema.mule.core.WildcardFilterType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.http package. 
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

    private final static QName _Listener_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "listener");
    private final static QName _ListenerConfig_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "listener-config");
    private final static QName _ResponseBuilder_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "response-builder");
    private final static QName _Request_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "request");
    private final static QName _RequestBuilder_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "request-builder");
    private final static QName _RequestConfig_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "request-config");
    private final static QName _AbstractHttpRequestAuthenticationProvider_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "abstract-http-request-authentication-provider");
    private final static QName _BasicAuthentication_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "basic-authentication");
    private final static QName _DigestAuthentication_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "digest-authentication");
    private final static QName _NtlmAuthentication_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "ntlm-authentication");
    private final static QName _Proxy_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "proxy");
    private final static QName _NtlmProxy_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "ntlm-proxy");
    private final static QName _Config_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "config");
    private final static QName _Connector_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "connector");
    private final static QName _PollingConnector_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "polling-connector");
    private final static QName _RestServiceComponent_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "rest-service-component");
    private final static QName _HttpResponseToObjectTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "http-response-to-object-transformer");
    private final static QName _HttpResponseToStringTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "http-response-to-string-transformer");
    private final static QName _ObjectToHttpRequestTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "object-to-http-request-transformer");
    private final static QName _MessageToHttpResponseTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "message-to-http-response-transformer");
    private final static QName _BodyToParameterMapTransformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "body-to-parameter-map-transformer");
    private final static QName _BasicSecurityFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "basic-security-filter");
    private final static QName _InboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "inbound-endpoint");
    private final static QName _OutboundEndpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "outbound-endpoint");
    private final static QName _Endpoint_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "endpoint");
    private final static QName _RequestWildcardFilter_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "request-wildcard-filter");
    private final static QName _StaticResourceHandler_QNAME = new QName("http://www.mulesoft.org/schema/mule/http", "static-resource-handler");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.http
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RequestBuilderType }
     * 
     */
    public RequestBuilderType createRequestBuilderType() {
        return new RequestBuilderType();
    }

    /**
     * Create an instance of {@link ResponseBuilderType }
     * 
     */
    public ResponseBuilderType createResponseBuilderType() {
        return new ResponseBuilderType();
    }

    /**
     * Create an instance of {@link RestServiceWrapperType }
     * 
     */
    public RestServiceWrapperType createRestServiceWrapperType() {
        return new RestServiceWrapperType();
    }

    /**
     * Create an instance of {@link RequestConfigType }
     * 
     */
    public RequestConfigType createRequestConfigType() {
        return new RequestConfigType();
    }

    /**
     * Create an instance of {@link RequestType }
     * 
     */
    public RequestType createRequestType() {
        return new RequestType();
    }

    /**
     * Create an instance of {@link ListenerType }
     * 
     */
    public ListenerType createListenerType() {
        return new ListenerType();
    }

    /**
     * Create an instance of {@link ListenerConfigType }
     * 
     */
    public ListenerConfigType createListenerConfigType() {
        return new ListenerConfigType();
    }

    /**
     * Create an instance of {@link GlobalResponseBuilderType }
     * 
     */
    public GlobalResponseBuilderType createGlobalResponseBuilderType() {
        return new GlobalResponseBuilderType();
    }

    /**
     * Create an instance of {@link GlobalRequestBuilderType }
     * 
     */
    public GlobalRequestBuilderType createGlobalRequestBuilderType() {
        return new GlobalRequestBuilderType();
    }

    /**
     * Create an instance of {@link AbstractHttpRequestAuthenticationProvider }
     * 
     */
    public AbstractHttpRequestAuthenticationProvider createAbstractHttpRequestAuthenticationProvider() {
        return new AbstractHttpRequestAuthenticationProvider();
    }

    /**
     * Create an instance of {@link BasicAuthenticationType }
     * 
     */
    public BasicAuthenticationType createBasicAuthenticationType() {
        return new BasicAuthenticationType();
    }

    /**
     * Create an instance of {@link DigestAuthenticationType }
     * 
     */
    public DigestAuthenticationType createDigestAuthenticationType() {
        return new DigestAuthenticationType();
    }

    /**
     * Create an instance of {@link NtlmAuthenticationType }
     * 
     */
    public NtlmAuthenticationType createNtlmAuthenticationType() {
        return new NtlmAuthenticationType();
    }

    /**
     * Create an instance of {@link GlobalProxyType }
     * 
     */
    public GlobalProxyType createGlobalProxyType() {
        return new GlobalProxyType();
    }

    /**
     * Create an instance of {@link GlobalNtlmProxyType }
     * 
     */
    public GlobalNtlmProxyType createGlobalNtlmProxyType() {
        return new GlobalNtlmProxyType();
    }

    /**
     * Create an instance of {@link HttpConfigType }
     * 
     */
    public HttpConfigType createHttpConfigType() {
        return new HttpConfigType();
    }

    /**
     * Create an instance of {@link HttpConnectorType }
     * 
     */
    public HttpConnectorType createHttpConnectorType() {
        return new HttpConnectorType();
    }

    /**
     * Create an instance of {@link HttpPollingConnectorType }
     * 
     */
    public HttpPollingConnectorType createHttpPollingConnectorType() {
        return new HttpPollingConnectorType();
    }

    /**
     * Create an instance of {@link BasicSecurityFilterType }
     * 
     */
    public BasicSecurityFilterType createBasicSecurityFilterType() {
        return new BasicSecurityFilterType();
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
     * Create an instance of {@link StaticResourceHandlerType }
     * 
     */
    public StaticResourceHandlerType createStaticResourceHandlerType() {
        return new StaticResourceHandlerType();
    }

    /**
     * Create an instance of {@link HttpHeaderType }
     * 
     */
    public HttpHeaderType createHttpHeaderType() {
        return new HttpHeaderType();
    }

    /**
     * Create an instance of {@link HeadersType }
     * 
     */
    public HeadersType createHeadersType() {
        return new HeadersType();
    }

    /**
     * Create an instance of {@link ProxyType }
     * 
     */
    public ProxyType createProxyType() {
        return new ProxyType();
    }

    /**
     * Create an instance of {@link NtlmProxyType }
     * 
     */
    public NtlmProxyType createNtlmProxyType() {
        return new NtlmProxyType();
    }

    /**
     * Create an instance of {@link HeaderType }
     * 
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link CookieType }
     * 
     */
    public CookieType createCookieType() {
        return new CookieType();
    }

    /**
     * Create an instance of {@link CacheControlType }
     * 
     */
    public CacheControlType createCacheControlType() {
        return new CacheControlType();
    }

    /**
     * Create an instance of {@link RequestBuilderType.Builder }
     * 
     */
    public RequestBuilderType.Builder createRequestBuilderTypeBuilder() {
        return new RequestBuilderType.Builder();
    }

    /**
     * Create an instance of {@link RequestBuilderType.QueryParam }
     * 
     */
    public RequestBuilderType.QueryParam createRequestBuilderTypeQueryParam() {
        return new RequestBuilderType.QueryParam();
    }

    /**
     * Create an instance of {@link RequestBuilderType.QueryParams }
     * 
     */
    public RequestBuilderType.QueryParams createRequestBuilderTypeQueryParams() {
        return new RequestBuilderType.QueryParams();
    }

    /**
     * Create an instance of {@link RequestBuilderType.UriParam }
     * 
     */
    public RequestBuilderType.UriParam createRequestBuilderTypeUriParam() {
        return new RequestBuilderType.UriParam();
    }

    /**
     * Create an instance of {@link RequestBuilderType.UriParams }
     * 
     */
    public RequestBuilderType.UriParams createRequestBuilderTypeUriParams() {
        return new RequestBuilderType.UriParams();
    }

    /**
     * Create an instance of {@link ResponseBuilderType.Builder }
     * 
     */
    public ResponseBuilderType.Builder createResponseBuilderTypeBuilder() {
        return new ResponseBuilderType.Builder();
    }

    /**
     * Create an instance of {@link RestServiceWrapperType.ErrorFilter }
     * 
     */
    public RestServiceWrapperType.ErrorFilter createRestServiceWrapperTypeErrorFilter() {
        return new RestServiceWrapperType.ErrorFilter();
    }

    /**
     * Create an instance of {@link RequestConfigType.RamlApiConfiguration }
     * 
     */
    public RequestConfigType.RamlApiConfiguration createRequestConfigTypeRamlApiConfiguration() {
        return new RequestConfigType.RamlApiConfiguration();
    }

    /**
     * Create an instance of {@link RequestType.SuccessStatusCodeValidator }
     * 
     */
    public RequestType.SuccessStatusCodeValidator createRequestTypeSuccessStatusCodeValidator() {
        return new RequestType.SuccessStatusCodeValidator();
    }

    /**
     * Create an instance of {@link RequestType.FailureStatusCodeValidator }
     * 
     */
    public RequestType.FailureStatusCodeValidator createRequestTypeFailureStatusCodeValidator() {
        return new RequestType.FailureStatusCodeValidator();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListenerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListenerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "listener", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-source")
    public JAXBElement<ListenerType> createListener(ListenerType value) {
        return new JAXBElement<ListenerType>(_Listener_QNAME, ListenerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListenerConfigType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListenerConfigType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "listener-config", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-extension")
    public JAXBElement<ListenerConfigType> createListenerConfig(ListenerConfigType value) {
        return new JAXBElement<ListenerConfigType>(_ListenerConfig_QNAME, ListenerConfigType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlobalResponseBuilderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GlobalResponseBuilderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "response-builder", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-empty-processor")
    public JAXBElement<GlobalResponseBuilderType> createResponseBuilder(GlobalResponseBuilderType value) {
        return new JAXBElement<GlobalResponseBuilderType>(_ResponseBuilder_QNAME, GlobalResponseBuilderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "request", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<RequestType> createRequest(RequestType value) {
        return new JAXBElement<RequestType>(_Request_QNAME, RequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlobalRequestBuilderType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GlobalRequestBuilderType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "request-builder", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-extension")
    public JAXBElement<GlobalRequestBuilderType> createRequestBuilder(GlobalRequestBuilderType value) {
        return new JAXBElement<GlobalRequestBuilderType>(_RequestBuilder_QNAME, GlobalRequestBuilderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestConfigType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RequestConfigType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "request-config", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-extension")
    public JAXBElement<RequestConfigType> createRequestConfig(RequestConfigType value) {
        return new JAXBElement<RequestConfigType>(_RequestConfig_QNAME, RequestConfigType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractHttpRequestAuthenticationProvider }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractHttpRequestAuthenticationProvider }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "abstract-http-request-authentication-provider")
    public JAXBElement<AbstractHttpRequestAuthenticationProvider> createAbstractHttpRequestAuthenticationProvider(AbstractHttpRequestAuthenticationProvider value) {
        return new JAXBElement<AbstractHttpRequestAuthenticationProvider>(_AbstractHttpRequestAuthenticationProvider_QNAME, AbstractHttpRequestAuthenticationProvider.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BasicAuthenticationType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BasicAuthenticationType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "basic-authentication", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/http", substitutionHeadName = "abstract-http-request-authentication-provider")
    public JAXBElement<BasicAuthenticationType> createBasicAuthentication(BasicAuthenticationType value) {
        return new JAXBElement<BasicAuthenticationType>(_BasicAuthentication_QNAME, BasicAuthenticationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DigestAuthenticationType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DigestAuthenticationType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "digest-authentication", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/http", substitutionHeadName = "abstract-http-request-authentication-provider")
    public JAXBElement<DigestAuthenticationType> createDigestAuthentication(DigestAuthenticationType value) {
        return new JAXBElement<DigestAuthenticationType>(_DigestAuthentication_QNAME, DigestAuthenticationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NtlmAuthenticationType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link NtlmAuthenticationType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "ntlm-authentication", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/http", substitutionHeadName = "abstract-http-request-authentication-provider")
    public JAXBElement<NtlmAuthenticationType> createNtlmAuthentication(NtlmAuthenticationType value) {
        return new JAXBElement<NtlmAuthenticationType>(_NtlmAuthentication_QNAME, NtlmAuthenticationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlobalProxyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GlobalProxyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "proxy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-extension")
    public JAXBElement<GlobalProxyType> createProxy(GlobalProxyType value) {
        return new JAXBElement<GlobalProxyType>(_Proxy_QNAME, GlobalProxyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlobalNtlmProxyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GlobalNtlmProxyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "ntlm-proxy", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-extension")
    public JAXBElement<GlobalNtlmProxyType> createNtlmProxy(GlobalNtlmProxyType value) {
        return new JAXBElement<GlobalNtlmProxyType>(_NtlmProxy_QNAME, GlobalNtlmProxyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HttpConfigType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link HttpConfigType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "config", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-configuration-extension")
    public JAXBElement<HttpConfigType> createConfig(HttpConfigType value) {
        return new JAXBElement<HttpConfigType>(_Config_QNAME, HttpConfigType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HttpConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link HttpConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-connector")
    public JAXBElement<HttpConnectorType> createConnector(HttpConnectorType value) {
        return new JAXBElement<HttpConnectorType>(_Connector_QNAME, HttpConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HttpPollingConnectorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link HttpPollingConnectorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "polling-connector", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-connector")
    public JAXBElement<HttpPollingConnectorType> createPollingConnector(HttpPollingConnectorType value) {
        return new JAXBElement<HttpPollingConnectorType>(_PollingConnector_QNAME, HttpPollingConnectorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "rest-service-component", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-component")
    public JAXBElement<RestServiceWrapperType> createRestServiceComponent(RestServiceWrapperType value) {
        return new JAXBElement<RestServiceWrapperType>(_RestServiceComponent_QNAME, RestServiceWrapperType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "http-response-to-object-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createHttpResponseToObjectTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_HttpResponseToObjectTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "http-response-to-string-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createHttpResponseToStringTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_HttpResponseToStringTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "object-to-http-request-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createObjectToHttpRequestTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_ObjectToHttpRequestTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "message-to-http-response-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createMessageToHttpResponseTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_MessageToHttpResponseTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "body-to-parameter-map-transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<AbstractTransformerType> createBodyToParameterMapTransformer(AbstractTransformerType value) {
        return new JAXBElement<AbstractTransformerType>(_BodyToParameterMapTransformer_QNAME, AbstractTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "basic-security-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-security-filter")
    public JAXBElement<BasicSecurityFilterType> createBasicSecurityFilter(BasicSecurityFilterType value) {
        return new JAXBElement<BasicSecurityFilterType>(_BasicSecurityFilter_QNAME, BasicSecurityFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InboundEndpointType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "inbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-inbound-endpoint")
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
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "outbound-endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-outbound-endpoint")
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
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "endpoint", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-global-endpoint")
    public JAXBElement<GlobalEndpointType> createEndpoint(GlobalEndpointType value) {
        return new JAXBElement<GlobalEndpointType>(_Endpoint_QNAME, GlobalEndpointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "request-wildcard-filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<WildcardFilterType> createRequestWildcardFilter(WildcardFilterType value) {
        return new JAXBElement<WildcardFilterType>(_RequestWildcardFilter_QNAME, WildcardFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StaticResourceHandlerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link StaticResourceHandlerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/http", name = "static-resource-handler", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<StaticResourceHandlerType> createStaticResourceHandler(StaticResourceHandlerType value) {
        return new JAXBElement<StaticResourceHandlerType>(_StaticResourceHandler_QNAME, StaticResourceHandlerType.class, null, value);
    }

}
