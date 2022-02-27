
package org.springframework.schema.context;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for propertyLoading complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="propertyLoading"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="location" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="properties-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="file-encoding" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="order" type="{http://www.w3.org/2001/XMLSchema}token" /&gt;
 *       &lt;attribute name="ignore-resource-not-found" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="ignore-unresolvable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="local-override" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "propertyLoading")
@XmlSeeAlso({
    PropertyOverride.class,
    PropertyPlaceholder.class
})
public class PropertyLoading {

    @XmlAttribute(name = "location")
    protected String location;
    @XmlAttribute(name = "properties-ref")
    protected String propertiesRef;
    @XmlAttribute(name = "file-encoding")
    protected String fileEncoding;
    @XmlAttribute(name = "order")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String order;
    @XmlAttribute(name = "ignore-resource-not-found")
    protected Boolean ignoreResourceNotFound;
    @XmlAttribute(name = "ignore-unresolvable")
    protected Boolean ignoreUnresolvable;
    @XmlAttribute(name = "local-override")
    protected Boolean localOverride;

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the propertiesRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertiesRef() {
        return propertiesRef;
    }

    /**
     * Sets the value of the propertiesRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertiesRef(String value) {
        this.propertiesRef = value;
    }

    /**
     * Gets the value of the fileEncoding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileEncoding() {
        return fileEncoding;
    }

    /**
     * Sets the value of the fileEncoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileEncoding(String value) {
        this.fileEncoding = value;
    }

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrder(String value) {
        this.order = value;
    }

    /**
     * Gets the value of the ignoreResourceNotFound property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIgnoreResourceNotFound() {
        if (ignoreResourceNotFound == null) {
            return false;
        } else {
            return ignoreResourceNotFound;
        }
    }

    /**
     * Sets the value of the ignoreResourceNotFound property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIgnoreResourceNotFound(Boolean value) {
        this.ignoreResourceNotFound = value;
    }

    /**
     * Gets the value of the ignoreUnresolvable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIgnoreUnresolvable() {
        if (ignoreUnresolvable == null) {
            return false;
        } else {
            return ignoreUnresolvable;
        }
    }

    /**
     * Sets the value of the ignoreUnresolvable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIgnoreUnresolvable(Boolean value) {
        this.ignoreUnresolvable = value;
    }

    /**
     * Gets the value of the localOverride property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isLocalOverride() {
        if (localOverride == null) {
            return false;
        } else {
            return localOverride;
        }
    }

    /**
     * Sets the value of the localOverride property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLocalOverride(Boolean value) {
        this.localOverride = value;
    }

}
