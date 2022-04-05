
package org.mulesoft.schema.mule.db;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for databasePoolingProfileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="databasePoolingProfileType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="maxPoolSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="minPoolSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="acquireIncrement" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="preparedStatementCacheSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="maxWaitMillis" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "databasePoolingProfileType")
public class DatabasePoolingProfileType {

    @XmlAttribute(name = "maxPoolSize")
    protected String maxPoolSize;
    @XmlAttribute(name = "minPoolSize")
    protected String minPoolSize;
    @XmlAttribute(name = "acquireIncrement")
    protected String acquireIncrement;
    @XmlAttribute(name = "preparedStatementCacheSize")
    protected String preparedStatementCacheSize;
    @XmlAttribute(name = "maxWaitMillis")
    protected String maxWaitMillis;

    /**
     * Gets the value of the maxPoolSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * Sets the value of the maxPoolSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxPoolSize(String value) {
        this.maxPoolSize = value;
    }

    /**
     * Gets the value of the minPoolSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinPoolSize() {
        return minPoolSize;
    }

    /**
     * Sets the value of the minPoolSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinPoolSize(String value) {
        this.minPoolSize = value;
    }

    /**
     * Gets the value of the acquireIncrement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcquireIncrement() {
        return acquireIncrement;
    }

    /**
     * Sets the value of the acquireIncrement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcquireIncrement(String value) {
        this.acquireIncrement = value;
    }

    /**
     * Gets the value of the preparedStatementCacheSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreparedStatementCacheSize() {
        return preparedStatementCacheSize;
    }

    /**
     * Sets the value of the preparedStatementCacheSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreparedStatementCacheSize(String value) {
        this.preparedStatementCacheSize = value;
    }

    /**
     * Gets the value of the maxWaitMillis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxWaitMillis() {
        return maxWaitMillis;
    }

    /**
     * Sets the value of the maxWaitMillis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxWaitMillis(String value) {
        this.maxWaitMillis = value;
    }

}
