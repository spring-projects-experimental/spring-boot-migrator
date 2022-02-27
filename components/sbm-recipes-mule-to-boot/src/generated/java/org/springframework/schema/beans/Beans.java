
package org.springframework.schema.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.springframework.org/schema/beans}description" minOccurs="0"/&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element ref="{http://www.springframework.org/schema/beans}import"/&gt;
 *           &lt;element ref="{http://www.springframework.org/schema/beans}alias"/&gt;
 *           &lt;element ref="{http://www.springframework.org/schema/beans}bean"/&gt;
 *           &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref="{http://www.springframework.org/schema/beans}beans" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="profile" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="default-lazy-init" type="{http://www.springframework.org/schema/beans}defaultable-boolean" default="default" /&gt;
 *       &lt;attribute name="default-merge" type="{http://www.springframework.org/schema/beans}defaultable-boolean" default="default" /&gt;
 *       &lt;attribute name="default-autowire" default="default"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="default"/&gt;
 *             &lt;enumeration value="no"/&gt;
 *             &lt;enumeration value="byName"/&gt;
 *             &lt;enumeration value="byType"/&gt;
 *             &lt;enumeration value="constructor"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="default-autowire-candidates" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="default-init-method" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="default-destroy-method" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "description",
    "importOrAliasOrBean",
    "beans"
})
@XmlRootElement(name = "beans")
public class Beans {

    protected Description description;
    @XmlElementRefs({
        @XmlElementRef(name = "import", namespace = "http://www.springframework.org/schema/beans", type = Import.class, required = false),
        @XmlElementRef(name = "alias", namespace = "http://www.springframework.org/schema/beans", type = Alias.class, required = false),
        @XmlElementRef(name = "bean", namespace = "http://www.springframework.org/schema/beans", type = Bean.class, required = false)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> importOrAliasOrBean;
    protected List<Beans> beans;
    @XmlAttribute(name = "profile")
    protected String profile;
    @XmlAttribute(name = "default-lazy-init")
    protected DefaultableBoolean defaultLazyInit;
    @XmlAttribute(name = "default-merge")
    protected DefaultableBoolean defaultMerge;
    @XmlAttribute(name = "default-autowire")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String defaultAutowire;
    @XmlAttribute(name = "default-autowire-candidates")
    protected String defaultAutowireCandidates;
    @XmlAttribute(name = "default-init-method")
    protected String defaultInitMethod;
    @XmlAttribute(name = "default-destroy-method")
    protected String defaultDestroyMethod;
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
     * Gets the value of the importOrAliasOrBean property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the importOrAliasOrBean property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImportOrAliasOrBean().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Import }
     * {@link Alias }
     * {@link Bean }
     * {@link Object }
     * 
     * 
     */
    public List<Object> getImportOrAliasOrBean() {
        if (importOrAliasOrBean == null) {
            importOrAliasOrBean = new ArrayList<Object>();
        }
        return this.importOrAliasOrBean;
    }

    /**
     * Gets the value of the beans property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the beans property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBeans().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Beans }
     * 
     * 
     */
    public List<Beans> getBeans() {
        if (beans == null) {
            beans = new ArrayList<Beans>();
        }
        return this.beans;
    }

    /**
     * Gets the value of the profile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfile() {
        return profile;
    }

    /**
     * Sets the value of the profile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfile(String value) {
        this.profile = value;
    }

    /**
     * Gets the value of the defaultLazyInit property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultableBoolean }
     *     
     */
    public DefaultableBoolean getDefaultLazyInit() {
        if (defaultLazyInit == null) {
            return DefaultableBoolean.DEFAULT;
        } else {
            return defaultLazyInit;
        }
    }

    /**
     * Sets the value of the defaultLazyInit property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultableBoolean }
     *     
     */
    public void setDefaultLazyInit(DefaultableBoolean value) {
        this.defaultLazyInit = value;
    }

    /**
     * Gets the value of the defaultMerge property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultableBoolean }
     *     
     */
    public DefaultableBoolean getDefaultMerge() {
        if (defaultMerge == null) {
            return DefaultableBoolean.DEFAULT;
        } else {
            return defaultMerge;
        }
    }

    /**
     * Sets the value of the defaultMerge property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultableBoolean }
     *     
     */
    public void setDefaultMerge(DefaultableBoolean value) {
        this.defaultMerge = value;
    }

    /**
     * Gets the value of the defaultAutowire property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultAutowire() {
        if (defaultAutowire == null) {
            return "default";
        } else {
            return defaultAutowire;
        }
    }

    /**
     * Sets the value of the defaultAutowire property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultAutowire(String value) {
        this.defaultAutowire = value;
    }

    /**
     * Gets the value of the defaultAutowireCandidates property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultAutowireCandidates() {
        return defaultAutowireCandidates;
    }

    /**
     * Sets the value of the defaultAutowireCandidates property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultAutowireCandidates(String value) {
        this.defaultAutowireCandidates = value;
    }

    /**
     * Gets the value of the defaultInitMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultInitMethod() {
        return defaultInitMethod;
    }

    /**
     * Sets the value of the defaultInitMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultInitMethod(String value) {
        this.defaultInitMethod = value;
    }

    /**
     * Gets the value of the defaultDestroyMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultDestroyMethod() {
        return defaultDestroyMethod;
    }

    /**
     * Sets the value of the defaultDestroyMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultDestroyMethod(String value) {
        this.defaultDestroyMethod = value;
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
