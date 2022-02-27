
package org.springframework.schema.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.springframework.org/schema/beans}identifiedType"&gt;
 *       &lt;group ref="{http://www.springframework.org/schema/beans}beanElements"/&gt;
 *       &lt;attGroup ref="{http://www.springframework.org/schema/beans}beanAttributes"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "description",
    "metaOrConstructorArgOrProperty"
})
@XmlRootElement(name = "bean")
public class Bean
    extends IdentifiedType
{

    protected Description description;
    @XmlElementRefs({
        @XmlElementRef(name = "meta", namespace = "http://www.springframework.org/schema/beans", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "constructor-arg", namespace = "http://www.springframework.org/schema/beans", type = ConstructorArg.class, required = false),
        @XmlElementRef(name = "property", namespace = "http://www.springframework.org/schema/beans", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "qualifier", namespace = "http://www.springframework.org/schema/beans", type = Qualifier.class, required = false),
        @XmlElementRef(name = "lookup-method", namespace = "http://www.springframework.org/schema/beans", type = LookupMethod.class, required = false),
        @XmlElementRef(name = "replaced-method", namespace = "http://www.springframework.org/schema/beans", type = ReplacedMethod.class, required = false)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> metaOrConstructorArgOrProperty;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "parent")
    protected String parent;
    @XmlAttribute(name = "scope")
    protected String scope;
    @XmlAttribute(name = "abstract")
    protected Boolean _abstract;
    @XmlAttribute(name = "lazy-init")
    protected DefaultableBoolean lazyInit;
    @XmlAttribute(name = "autowire")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String autowire;
    @XmlAttribute(name = "depends-on")
    protected String dependsOn;
    @XmlAttribute(name = "autowire-candidate")
    protected DefaultableBoolean autowireCandidate;
    @XmlAttribute(name = "primary")
    protected Boolean primary;
    @XmlAttribute(name = "init-method")
    protected String initMethod;
    @XmlAttribute(name = "destroy-method")
    protected String destroyMethod;
    @XmlAttribute(name = "factory-method")
    protected String factoryMethod;
    @XmlAttribute(name = "factory-bean")
    protected String factoryBean;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the metaOrConstructorArgOrProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metaOrConstructorArgOrProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetaOrConstructorArgOrProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link MetaType }{@code >}
     * {@link ConstructorArg }
     * {@link JAXBElement }{@code <}{@link PropertyType }{@code >}
     * {@link Qualifier }
     * {@link LookupMethod }
     * {@link ReplacedMethod }
     * {@link Object }
     * 
     * 
     */
    public List<Object> getMetaOrConstructorArgOrProperty() {
        if (metaOrConstructorArgOrProperty == null) {
            metaOrConstructorArgOrProperty = new ArrayList<Object>();
        }
        return this.metaOrConstructorArgOrProperty;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
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

    /**
     * Gets the value of the parent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParent(String value) {
        this.parent = value;
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScope(String value) {
        this.scope = value;
    }

    /**
     * Gets the value of the abstract property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAbstract() {
        return _abstract;
    }

    /**
     * Sets the value of the abstract property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAbstract(Boolean value) {
        this._abstract = value;
    }

    /**
     * Gets the value of the lazyInit property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultableBoolean }
     *     
     */
    public DefaultableBoolean getLazyInit() {
        if (lazyInit == null) {
            return DefaultableBoolean.DEFAULT;
        } else {
            return lazyInit;
        }
    }

    /**
     * Sets the value of the lazyInit property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultableBoolean }
     *     
     */
    public void setLazyInit(DefaultableBoolean value) {
        this.lazyInit = value;
    }

    /**
     * Gets the value of the autowire property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutowire() {
        if (autowire == null) {
            return "default";
        } else {
            return autowire;
        }
    }

    /**
     * Sets the value of the autowire property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutowire(String value) {
        this.autowire = value;
    }

    /**
     * Gets the value of the dependsOn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependsOn() {
        return dependsOn;
    }

    /**
     * Sets the value of the dependsOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependsOn(String value) {
        this.dependsOn = value;
    }

    /**
     * Gets the value of the autowireCandidate property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultableBoolean }
     *     
     */
    public DefaultableBoolean getAutowireCandidate() {
        if (autowireCandidate == null) {
            return DefaultableBoolean.DEFAULT;
        } else {
            return autowireCandidate;
        }
    }

    /**
     * Sets the value of the autowireCandidate property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultableBoolean }
     *     
     */
    public void setAutowireCandidate(DefaultableBoolean value) {
        this.autowireCandidate = value;
    }

    /**
     * Gets the value of the primary property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPrimary() {
        return primary;
    }

    /**
     * Sets the value of the primary property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrimary(Boolean value) {
        this.primary = value;
    }

    /**
     * Gets the value of the initMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitMethod() {
        return initMethod;
    }

    /**
     * Sets the value of the initMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitMethod(String value) {
        this.initMethod = value;
    }

    /**
     * Gets the value of the destroyMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestroyMethod() {
        return destroyMethod;
    }

    /**
     * Sets the value of the destroyMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestroyMethod(String value) {
        this.destroyMethod = value;
    }

    /**
     * Gets the value of the factoryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactoryMethod() {
        return factoryMethod;
    }

    /**
     * Sets the value of the factoryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactoryMethod(String value) {
        this.factoryMethod = value;
    }

    /**
     * Gets the value of the factoryBean property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactoryBean() {
        return factoryBean;
    }

    /**
     * Sets the value of the factoryBean property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactoryBean(String value) {
        this.factoryBean = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
