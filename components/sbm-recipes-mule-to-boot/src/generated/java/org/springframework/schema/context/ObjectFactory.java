
package org.springframework.schema.context;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.springframework.schema.context package. 
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

    private final static QName _AnnotationConfig_QNAME = new QName("http://www.springframework.org/schema/context", "annotation-config");
    private final static QName _SpringConfigured_QNAME = new QName("http://www.springframework.org/schema/context", "spring-configured");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.springframework.schema.context
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PropertyPlaceholder }
     * 
     */
    public PropertyPlaceholder createPropertyPlaceholder() {
        return new PropertyPlaceholder();
    }

    /**
     * Create an instance of {@link PropertyLoading }
     * 
     */
    public PropertyLoading createPropertyLoading() {
        return new PropertyLoading();
    }

    /**
     * Create an instance of {@link PropertyOverride }
     * 
     */
    public PropertyOverride createPropertyOverride() {
        return new PropertyOverride();
    }

    /**
     * Create an instance of {@link ComponentScan }
     * 
     */
    public ComponentScan createComponentScan() {
        return new ComponentScan();
    }

    /**
     * Create an instance of {@link FilterType }
     * 
     */
    public FilterType createFilterType() {
        return new FilterType();
    }

    /**
     * Create an instance of {@link LoadTimeWeaver }
     * 
     */
    public LoadTimeWeaver createLoadTimeWeaver() {
        return new LoadTimeWeaver();
    }

    /**
     * Create an instance of {@link MbeanExport }
     * 
     */
    public MbeanExport createMbeanExport() {
        return new MbeanExport();
    }

    /**
     * Create an instance of {@link MbeanServer }
     * 
     */
    public MbeanServer createMbeanServer() {
        return new MbeanServer();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.springframework.org/schema/context", name = "annotation-config")
    public JAXBElement<Object> createAnnotationConfig(Object value) {
        return new JAXBElement<Object>(_AnnotationConfig_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.springframework.org/schema/context", name = "spring-configured")
    public JAXBElement<String> createSpringConfigured(String value) {
        return new JAXBElement<String>(_SpringConfigured_QNAME, String.class, null, value);
    }

}
