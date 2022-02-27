
package org.mulesoft.schema.mule.schemadoc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.schemadoc package. 
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

    private final static QName _Snippet_QNAME = new QName("http://www.mulesoft.org/schema/mule/schemadoc", "snippet");
    private final static QName _PageTitle_QNAME = new QName("http://www.mulesoft.org/schema/mule/schemadoc", "page-title");
    private final static QName _ShortName_QNAME = new QName("http://www.mulesoft.org/schema/mule/schemadoc", "short-name");
    private final static QName _AdditionalDocumentation_QNAME = new QName("http://www.mulesoft.org/schema/mule/schemadoc", "additional-documentation");
    private final static QName _TransportFeatures_QNAME = new QName("http://www.mulesoft.org/schema/mule/schemadoc", "transport-features");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.schemadoc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SnippetType }
     * 
     */
    public SnippetType createSnippetType() {
        return new SnippetType();
    }

    /**
     * Create an instance of {@link AdditionalDocumentationType }
     * 
     */
    public AdditionalDocumentationType createAdditionalDocumentationType() {
        return new AdditionalDocumentationType();
    }

    /**
     * Create an instance of {@link TransportFeaturesType }
     * 
     */
    public TransportFeaturesType createTransportFeaturesType() {
        return new TransportFeaturesType();
    }

    /**
     * Create an instance of {@link MEPsType }
     * 
     */
    public MEPsType createMEPsType() {
        return new MEPsType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SnippetType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SnippetType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/schemadoc", name = "snippet")
    public JAXBElement<SnippetType> createSnippet(SnippetType value) {
        return new JAXBElement<SnippetType>(_Snippet_QNAME, SnippetType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/schemadoc", name = "page-title")
    public JAXBElement<String> createPageTitle(String value) {
        return new JAXBElement<String>(_PageTitle_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/schemadoc", name = "short-name")
    public JAXBElement<String> createShortName(String value) {
        return new JAXBElement<String>(_ShortName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalDocumentationType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AdditionalDocumentationType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/schemadoc", name = "additional-documentation")
    public JAXBElement<AdditionalDocumentationType> createAdditionalDocumentation(AdditionalDocumentationType value) {
        return new JAXBElement<AdditionalDocumentationType>(_AdditionalDocumentation_QNAME, AdditionalDocumentationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransportFeaturesType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransportFeaturesType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/schemadoc", name = "transport-features")
    public JAXBElement<TransportFeaturesType> createTransportFeatures(TransportFeaturesType value) {
        return new JAXBElement<TransportFeaturesType>(_TransportFeatures_QNAME, TransportFeaturesType.class, null, value);
    }

}
