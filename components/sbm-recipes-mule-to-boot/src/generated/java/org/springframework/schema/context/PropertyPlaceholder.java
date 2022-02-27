
package org.springframework.schema.context;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.springframework.org/schema/context}propertyLoading"&gt;
 *       &lt;attribute name="system-properties-mode" default="ENVIRONMENT"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="ENVIRONMENT"/&gt;
 *             &lt;enumeration value="NEVER"/&gt;
 *             &lt;enumeration value="FALLBACK"/&gt;
 *             &lt;enumeration value="OVERRIDE"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="value-separator" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default=":" /&gt;
 *       &lt;attribute name="trim-values" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" /&gt;
 *       &lt;attribute name="null-value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "property-placeholder")
public class PropertyPlaceholder
    extends PropertyLoading
{

    @XmlAttribute(name = "system-properties-mode")
    protected String systemPropertiesMode;
    @XmlAttribute(name = "value-separator")
    @XmlSchemaType(name = "anySimpleType")
    protected String valueSeparator;
    @XmlAttribute(name = "trim-values")
    @XmlSchemaType(name = "anySimpleType")
    protected String trimValues;
    @XmlAttribute(name = "null-value")
    @XmlSchemaType(name = "anySimpleType")
    protected String nullValue;

    /**
     * Gets the value of the systemPropertiesMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemPropertiesMode() {
        if (systemPropertiesMode == null) {
            return "ENVIRONMENT";
        } else {
            return systemPropertiesMode;
        }
    }

    /**
     * Sets the value of the systemPropertiesMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemPropertiesMode(String value) {
        this.systemPropertiesMode = value;
    }

    /**
     * Gets the value of the valueSeparator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueSeparator() {
        if (valueSeparator == null) {
            return ":";
        } else {
            return valueSeparator;
        }
    }

    /**
     * Sets the value of the valueSeparator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueSeparator(String value) {
        this.valueSeparator = value;
    }

    /**
     * Gets the value of the trimValues property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrimValues() {
        return trimValues;
    }

    /**
     * Sets the value of the trimValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrimValues(String value) {
        this.trimValues = value;
    }

    /**
     * Gets the value of the nullValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNullValue() {
        return nullValue;
    }

    /**
     * Sets the value of the nullValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNullValue(String value) {
        this.nullValue = value;
    }

}
