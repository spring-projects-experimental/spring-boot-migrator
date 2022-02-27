
package org.springframework.schema.context;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="include-filter" type="{http://www.springframework.org/schema/context}filterType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="exclude-filter" type="{http://www.springframework.org/schema/context}filterType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="base-package" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="resource-pattern" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="use-default-filters" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="annotation-config" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="name-generator" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="scope-resolver" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="scoped-proxy"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="no"/&gt;
 *             &lt;enumeration value="interfaces"/&gt;
 *             &lt;enumeration value="targetClass"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "includeFilter",
    "excludeFilter"
})
@XmlRootElement(name = "component-scan")
public class ComponentScan {

    @XmlElement(name = "include-filter")
    protected List<FilterType> includeFilter;
    @XmlElement(name = "exclude-filter")
    protected List<FilterType> excludeFilter;
    @XmlAttribute(name = "base-package", required = true)
    protected String basePackage;
    @XmlAttribute(name = "resource-pattern")
    protected String resourcePattern;
    @XmlAttribute(name = "use-default-filters")
    protected Boolean useDefaultFilters;
    @XmlAttribute(name = "annotation-config")
    protected Boolean annotationConfig;
    @XmlAttribute(name = "name-generator")
    protected String nameGenerator;
    @XmlAttribute(name = "scope-resolver")
    protected String scopeResolver;
    @XmlAttribute(name = "scoped-proxy")
    protected String scopedProxy;

    /**
     * Gets the value of the includeFilter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the includeFilter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncludeFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FilterType }
     * 
     * 
     */
    public List<FilterType> getIncludeFilter() {
        if (includeFilter == null) {
            includeFilter = new ArrayList<FilterType>();
        }
        return this.includeFilter;
    }

    /**
     * Gets the value of the excludeFilter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the excludeFilter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExcludeFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FilterType }
     * 
     * 
     */
    public List<FilterType> getExcludeFilter() {
        if (excludeFilter == null) {
            excludeFilter = new ArrayList<FilterType>();
        }
        return this.excludeFilter;
    }

    /**
     * Gets the value of the basePackage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBasePackage() {
        return basePackage;
    }

    /**
     * Sets the value of the basePackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBasePackage(String value) {
        this.basePackage = value;
    }

    /**
     * Gets the value of the resourcePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourcePattern() {
        return resourcePattern;
    }

    /**
     * Sets the value of the resourcePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourcePattern(String value) {
        this.resourcePattern = value;
    }

    /**
     * Gets the value of the useDefaultFilters property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUseDefaultFilters() {
        if (useDefaultFilters == null) {
            return true;
        } else {
            return useDefaultFilters;
        }
    }

    /**
     * Sets the value of the useDefaultFilters property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUseDefaultFilters(Boolean value) {
        this.useDefaultFilters = value;
    }

    /**
     * Gets the value of the annotationConfig property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isAnnotationConfig() {
        if (annotationConfig == null) {
            return true;
        } else {
            return annotationConfig;
        }
    }

    /**
     * Sets the value of the annotationConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAnnotationConfig(Boolean value) {
        this.annotationConfig = value;
    }

    /**
     * Gets the value of the nameGenerator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameGenerator() {
        return nameGenerator;
    }

    /**
     * Sets the value of the nameGenerator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameGenerator(String value) {
        this.nameGenerator = value;
    }

    /**
     * Gets the value of the scopeResolver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScopeResolver() {
        return scopeResolver;
    }

    /**
     * Sets the value of the scopeResolver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScopeResolver(String value) {
        this.scopeResolver = value;
    }

    /**
     * Gets the value of the scopedProxy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScopedProxy() {
        return scopedProxy;
    }

    /**
     * Sets the value of the scopedProxy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScopedProxy(String value) {
        this.scopedProxy = value;
    }

}
