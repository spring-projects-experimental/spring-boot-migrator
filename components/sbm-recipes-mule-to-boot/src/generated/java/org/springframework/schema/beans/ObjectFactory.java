
package org.springframework.schema.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.springframework.schema.beans package. 
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

    private final static QName _Meta_QNAME = new QName("http://www.springframework.org/schema/beans", "meta");
    private final static QName _Entry_QNAME = new QName("http://www.springframework.org/schema/beans", "entry");
    private final static QName _Property_QNAME = new QName("http://www.springframework.org/schema/beans", "property");
    private final static QName _Attribute_QNAME = new QName("http://www.springframework.org/schema/beans", "attribute");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.springframework.schema.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Beans }
     * 
     */
    public Beans createBeans() {
        return new Beans();
    }

    /**
     * Create an instance of {@link Description }
     * 
     */
    public Description createDescription() {
        return new Description();
    }

    /**
     * Create an instance of {@link Import }
     * 
     */
    public Import createImport() {
        return new Import();
    }

    /**
     * Create an instance of {@link Alias }
     * 
     */
    public Alias createAlias() {
        return new Alias();
    }

    /**
     * Create an instance of {@link Bean }
     * 
     */
    public Bean createBean() {
        return new Bean();
    }

    /**
     * Create an instance of {@link MetaType }
     * 
     */
    public MetaType createMetaType() {
        return new MetaType();
    }

    /**
     * Create an instance of {@link ConstructorArg }
     * 
     */
    public ConstructorArg createConstructorArg() {
        return new ConstructorArg();
    }

    /**
     * Create an instance of {@link Ref }
     * 
     */
    public Ref createRef() {
        return new Ref();
    }

    /**
     * Create an instance of {@link Idref }
     * 
     */
    public Idref createIdref() {
        return new Idref();
    }

    /**
     * Create an instance of {@link Value }
     * 
     */
    public Value createValue() {
        return new Value();
    }

    /**
     * Create an instance of {@link Null }
     * 
     */
    public Null createNull() {
        return new Null();
    }

    /**
     * Create an instance of {@link Array }
     * 
     */
    public Array createArray() {
        return new Array();
    }

    /**
     * Create an instance of {@link ListOrSetType }
     * 
     */
    public ListOrSetType createListOrSetType() {
        return new ListOrSetType();
    }

    /**
     * Create an instance of {@link CollectionType }
     * 
     */
    public CollectionType createCollectionType() {
        return new CollectionType();
    }

    /**
     * Create an instance of {@link List }
     * 
     */
    public List createList() {
        return new List();
    }

    /**
     * Create an instance of {@link Set }
     * 
     */
    public Set createSet() {
        return new Set();
    }

    /**
     * Create an instance of {@link Map }
     * 
     */
    public Map createMap() {
        return new Map();
    }

    /**
     * Create an instance of {@link MapType }
     * 
     */
    public MapType createMapType() {
        return new MapType();
    }

    /**
     * Create an instance of {@link EntryType }
     * 
     */
    public EntryType createEntryType() {
        return new EntryType();
    }

    /**
     * Create an instance of {@link Props }
     * 
     */
    public Props createProps() {
        return new Props();
    }

    /**
     * Create an instance of {@link PropsType }
     * 
     */
    public PropsType createPropsType() {
        return new PropsType();
    }

    /**
     * Create an instance of {@link Prop }
     * 
     */
    public Prop createProp() {
        return new Prop();
    }

    /**
     * Create an instance of {@link PropertyType }
     * 
     */
    public PropertyType createPropertyType() {
        return new PropertyType();
    }

    /**
     * Create an instance of {@link Qualifier }
     * 
     */
    public Qualifier createQualifier() {
        return new Qualifier();
    }

    /**
     * Create an instance of {@link LookupMethod }
     * 
     */
    public LookupMethod createLookupMethod() {
        return new LookupMethod();
    }

    /**
     * Create an instance of {@link ReplacedMethod }
     * 
     */
    public ReplacedMethod createReplacedMethod() {
        return new ReplacedMethod();
    }

    /**
     * Create an instance of {@link ArgType }
     * 
     */
    public ArgType createArgType() {
        return new ArgType();
    }

    /**
     * Create an instance of {@link Key }
     * 
     */
    public Key createKey() {
        return new Key();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MetaType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MetaType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.springframework.org/schema/beans", name = "meta")
    public JAXBElement<MetaType> createMeta(MetaType value) {
        return new JAXBElement<MetaType>(_Meta_QNAME, MetaType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntryType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EntryType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.springframework.org/schema/beans", name = "entry")
    public JAXBElement<EntryType> createEntry(EntryType value) {
        return new JAXBElement<EntryType>(_Entry_QNAME, EntryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertyType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PropertyType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.springframework.org/schema/beans", name = "property")
    public JAXBElement<PropertyType> createProperty(PropertyType value) {
        return new JAXBElement<PropertyType>(_Property_QNAME, PropertyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MetaType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MetaType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.springframework.org/schema/beans", name = "attribute")
    public JAXBElement<MetaType> createAttribute(MetaType value) {
        return new JAXBElement<MetaType>(_Attribute_QNAME, MetaType.class, null, value);
    }

}
