
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for defaultJavaComponentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="defaultJavaComponentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractComponentType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/core}entryPointResolvers"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-object-factory" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-lifecycle-adapter-factory" minOccurs="0"/&gt;
 *         &lt;element name="binding" type="{http://www.mulesoft.org/schema/mule/core}pojoBindingType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="class" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "defaultJavaComponentType", propOrder = {
    "abstractEntryPointResolverSet",
    "abstractEntryPointResolver",
    "abstractObjectFactory",
    "abstractLifecycleAdapterFactory",
    "binding"
})
@XmlSeeAlso({
    PooledJavaComponentType.class
})
public class DefaultJavaComponentType
    extends AbstractComponentType
{

    @XmlElementRef(name = "abstract-entry-point-resolver-set", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractEntryPointResolverSetType> abstractEntryPointResolverSet;
    @XmlElementRef(name = "abstract-entry-point-resolver", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractEntryPointResolverType> abstractEntryPointResolver;
    @XmlElementRef(name = "abstract-object-factory", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractObjectFactoryType> abstractObjectFactory;
    @XmlElementRef(name = "abstract-lifecycle-adapter-factory", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractLifecycleAdapterFactory> abstractLifecycleAdapterFactory;
    protected List<PojoBindingType> binding;
    @XmlAttribute(name = "class")
    protected String clazz;

    /**
     * Gets the value of the abstractEntryPointResolverSet property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomEntryPointResolverSetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverSetType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractEntryPointResolverSetType> getAbstractEntryPointResolverSet() {
        return abstractEntryPointResolverSet;
    }

    /**
     * Sets the value of the abstractEntryPointResolverSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomEntryPointResolverSetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverSetType }{@code >}
     *     
     */
    public void setAbstractEntryPointResolverSet(JAXBElement<? extends AbstractEntryPointResolverSetType> value) {
        this.abstractEntryPointResolverSet = value;
    }

    /**
     * Gets the value of the abstractEntryPointResolver property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MethodEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReflectionEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractEntryPointResolverType> getAbstractEntryPointResolver() {
        return abstractEntryPointResolver;
    }

    /**
     * Sets the value of the abstractEntryPointResolver property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MethodEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReflectionEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     *     
     */
    public void setAbstractEntryPointResolver(JAXBElement<? extends AbstractEntryPointResolverType> value) {
        this.abstractEntryPointResolver = value;
    }

    /**
     * 
     *                                 Object factory used to obtain the object instance that will be used for the component implementation. The object factory is responsible for object creation and may implement different patterns, such as singleton or prototype, or look up an instance from other object containers.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SingletonObjectFactoryType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PrototypeObjectFactoryType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SpringBeanLookupType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObjectFactoryType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractObjectFactoryType> getAbstractObjectFactory() {
        return abstractObjectFactory;
    }

    /**
     * Sets the value of the abstractObjectFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SingletonObjectFactoryType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PrototypeObjectFactoryType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SpringBeanLookupType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractObjectFactoryType }{@code >}
     *     
     */
    public void setAbstractObjectFactory(JAXBElement<? extends AbstractObjectFactoryType> value) {
        this.abstractObjectFactory = value;
    }

    /**
     * Gets the value of the abstractLifecycleAdapterFactory property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CustomLifecycleAdapterFactory }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractLifecycleAdapterFactory }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractLifecycleAdapterFactory> getAbstractLifecycleAdapterFactory() {
        return abstractLifecycleAdapterFactory;
    }

    /**
     * Sets the value of the abstractLifecycleAdapterFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CustomLifecycleAdapterFactory }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractLifecycleAdapterFactory }{@code >}
     *     
     */
    public void setAbstractLifecycleAdapterFactory(JAXBElement<? extends AbstractLifecycleAdapterFactory> value) {
        this.abstractLifecycleAdapterFactory = value;
    }

    /**
     * Gets the value of the binding property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the binding property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBinding().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PojoBindingType }
     * 
     * 
     */
    public List<PojoBindingType> getBinding() {
        if (binding == null) {
            binding = new ArrayList<PojoBindingType>();
        }
        return this.binding;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClazz(String value) {
        this.clazz = value;
    }

}
