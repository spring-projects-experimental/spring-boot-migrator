
package org.mulesoft.schema.mule.tls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rcStandardType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rcStandardType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="onlyEndEntities" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="preferCrls" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="noFallback" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="softFail" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rcStandardType")
public class RcStandardType {

    @XmlAttribute(name = "onlyEndEntities")
    protected Boolean onlyEndEntities;
    @XmlAttribute(name = "preferCrls")
    protected Boolean preferCrls;
    @XmlAttribute(name = "noFallback")
    protected Boolean noFallback;
    @XmlAttribute(name = "softFail")
    protected Boolean softFail;

    /**
     * Gets the value of the onlyEndEntities property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOnlyEndEntities() {
        return onlyEndEntities;
    }

    /**
     * Sets the value of the onlyEndEntities property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOnlyEndEntities(Boolean value) {
        this.onlyEndEntities = value;
    }

    /**
     * Gets the value of the preferCrls property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPreferCrls() {
        return preferCrls;
    }

    /**
     * Sets the value of the preferCrls property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPreferCrls(Boolean value) {
        this.preferCrls = value;
    }

    /**
     * Gets the value of the noFallback property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNoFallback() {
        return noFallback;
    }

    /**
     * Sets the value of the noFallback property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNoFallback(Boolean value) {
        this.noFallback = value;
    }

    /**
     * Gets the value of the softFail property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSoftFail() {
        return softFail;
    }

    /**
     * Sets the value of the softFail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSoftFail(Boolean value) {
        this.softFail = value;
    }

}
