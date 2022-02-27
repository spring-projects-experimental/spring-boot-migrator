
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractMonitoredObjectStoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractMonitoredObjectStoreType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractObjectStoreType"&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="maxEntries" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="entryTTL" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="expirationInterval" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractMonitoredObjectStoreType")
@XmlSeeAlso({
    TextFileObjectStoreType.class
})
public class AbstractMonitoredObjectStoreType
    extends AbstractObjectStoreType
{

    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "maxEntries")
    protected String maxEntries;
    @XmlAttribute(name = "entryTTL")
    protected String entryTTL;
    @XmlAttribute(name = "expirationInterval")
    protected String expirationInterval;

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
     * Gets the value of the maxEntries property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxEntries() {
        return maxEntries;
    }

    /**
     * Sets the value of the maxEntries property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxEntries(String value) {
        this.maxEntries = value;
    }

    /**
     * Gets the value of the entryTTL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryTTL() {
        return entryTTL;
    }

    /**
     * Sets the value of the entryTTL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryTTL(String value) {
        this.entryTTL = value;
    }

    /**
     * Gets the value of the expirationInterval property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpirationInterval() {
        return expirationInterval;
    }

    /**
     * Sets the value of the expirationInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpirationInterval(String value) {
        this.expirationInterval = value;
    }

}
