
package org.mulesoft.schema.mule.scripting;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.scripting package. 
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

    private final static QName _Component_QNAME = new QName("http://www.mulesoft.org/schema/mule/scripting", "component");
    private final static QName _Script_QNAME = new QName("http://www.mulesoft.org/schema/mule/scripting", "script");
    private final static QName _Transformer_QNAME = new QName("http://www.mulesoft.org/schema/mule/scripting", "transformer");
    private final static QName _Filter_QNAME = new QName("http://www.mulesoft.org/schema/mule/scripting", "filter");
    private final static QName _GroovyRefreshable_QNAME = new QName("http://www.mulesoft.org/schema/mule/scripting", "groovy-refreshable");
    private final static QName _Lang_QNAME = new QName("http://www.mulesoft.org/schema/mule/scripting", "lang");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.scripting
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ScriptComponentType }
     * 
     */
    public ScriptComponentType createScriptComponentType() {
        return new ScriptComponentType();
    }

    /**
     * Create an instance of {@link ScriptType }
     * 
     */
    public ScriptType createScriptType() {
        return new ScriptType();
    }

    /**
     * Create an instance of {@link ScriptTransformerType }
     * 
     */
    public ScriptTransformerType createScriptTransformerType() {
        return new ScriptTransformerType();
    }

    /**
     * Create an instance of {@link ScriptFilterType }
     * 
     */
    public ScriptFilterType createScriptFilterType() {
        return new ScriptFilterType();
    }

    /**
     * Create an instance of {@link GroovyRefreshableType }
     * 
     */
    public GroovyRefreshableType createGroovyRefreshableType() {
        return new GroovyRefreshableType();
    }

    /**
     * Create an instance of {@link LangType }
     * 
     */
    public LangType createLangType() {
        return new LangType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScriptComponentType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ScriptComponentType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/scripting", name = "component", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-component")
    public JAXBElement<ScriptComponentType> createComponent(ScriptComponentType value) {
        return new JAXBElement<ScriptComponentType>(_Component_QNAME, ScriptComponentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScriptType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ScriptType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/scripting", name = "script", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-mixed-content-extension")
    public JAXBElement<ScriptType> createScript(ScriptType value) {
        return new JAXBElement<ScriptType>(_Script_QNAME, ScriptType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScriptTransformerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ScriptTransformerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/scripting", name = "transformer", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-transformer")
    public JAXBElement<ScriptTransformerType> createTransformer(ScriptTransformerType value) {
        return new JAXBElement<ScriptTransformerType>(_Transformer_QNAME, ScriptTransformerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScriptFilterType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ScriptFilterType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/scripting", name = "filter", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-filter")
    public JAXBElement<ScriptFilterType> createFilter(ScriptFilterType value) {
        return new JAXBElement<ScriptFilterType>(_Filter_QNAME, ScriptFilterType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GroovyRefreshableType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GroovyRefreshableType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/scripting", name = "groovy-refreshable", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-extension")
    public JAXBElement<GroovyRefreshableType> createGroovyRefreshable(GroovyRefreshableType value) {
        return new JAXBElement<GroovyRefreshableType>(_GroovyRefreshable_QNAME, GroovyRefreshableType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LangType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LangType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/scripting", name = "lang", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-mixed-content-extension")
    public JAXBElement<LangType> createLang(LangType value) {
        return new JAXBElement<LangType>(_Lang_QNAME, LangType.class, null, value);
    }

}
