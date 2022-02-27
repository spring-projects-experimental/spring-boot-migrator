
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 A pooling profile is used to configure the pooling behaviour of Mule components. Each component can have its own pooling profile.
 *             
 * 
 * <p>Java class for poolingProfileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="poolingProfileType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractPoolingProfileType"&gt;
 *       &lt;attribute name="maxActive" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="maxIdle" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="initialisationPolicy" default="INITIALISE_ONE"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="INITIALISE_NONE"/&gt;
 *             &lt;enumeration value="INITIALISE_ONE"/&gt;
 *             &lt;enumeration value="INITIALISE_ALL"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="exhaustedAction" default="WHEN_EXHAUSTED_GROW"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="WHEN_EXHAUSTED_GROW"/&gt;
 *             &lt;enumeration value="WHEN_EXHAUSTED_WAIT"/&gt;
 *             &lt;enumeration value="WHEN_EXHAUSTED_FAIL"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="maxWait" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="evictionCheckIntervalMillis" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="minEvictionMillis" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "poolingProfileType")
public class PoolingProfileType
    extends AbstractPoolingProfileType
{

    @XmlAttribute(name = "maxActive")
    protected String maxActive;
    @XmlAttribute(name = "maxIdle")
    protected String maxIdle;
    @XmlAttribute(name = "initialisationPolicy")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String initialisationPolicy;
    @XmlAttribute(name = "exhaustedAction")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String exhaustedAction;
    @XmlAttribute(name = "maxWait")
    protected String maxWait;
    @XmlAttribute(name = "evictionCheckIntervalMillis")
    protected String evictionCheckIntervalMillis;
    @XmlAttribute(name = "minEvictionMillis")
    protected String minEvictionMillis;

    /**
     * Gets the value of the maxActive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxActive() {
        return maxActive;
    }

    /**
     * Sets the value of the maxActive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxActive(String value) {
        this.maxActive = value;
    }

    /**
     * Gets the value of the maxIdle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxIdle() {
        return maxIdle;
    }

    /**
     * Sets the value of the maxIdle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxIdle(String value) {
        this.maxIdle = value;
    }

    /**
     * Gets the value of the initialisationPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialisationPolicy() {
        if (initialisationPolicy == null) {
            return "INITIALISE_ONE";
        } else {
            return initialisationPolicy;
        }
    }

    /**
     * Sets the value of the initialisationPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialisationPolicy(String value) {
        this.initialisationPolicy = value;
    }

    /**
     * Gets the value of the exhaustedAction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExhaustedAction() {
        if (exhaustedAction == null) {
            return "WHEN_EXHAUSTED_GROW";
        } else {
            return exhaustedAction;
        }
    }

    /**
     * Sets the value of the exhaustedAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExhaustedAction(String value) {
        this.exhaustedAction = value;
    }

    /**
     * Gets the value of the maxWait property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxWait() {
        return maxWait;
    }

    /**
     * Sets the value of the maxWait property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxWait(String value) {
        this.maxWait = value;
    }

    /**
     * Gets the value of the evictionCheckIntervalMillis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvictionCheckIntervalMillis() {
        return evictionCheckIntervalMillis;
    }

    /**
     * Sets the value of the evictionCheckIntervalMillis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvictionCheckIntervalMillis(String value) {
        this.evictionCheckIntervalMillis = value;
    }

    /**
     * Gets the value of the minEvictionMillis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinEvictionMillis() {
        return minEvictionMillis;
    }

    /**
     * Sets the value of the minEvictionMillis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinEvictionMillis(String value) {
        this.minEvictionMillis = value;
    }

}
