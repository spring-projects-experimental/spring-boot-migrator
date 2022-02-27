
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Configures a trust store. TLS/SSL connections are made to trusted systems. The public certificates of trusted systems are stored in a keystore (called a trust store) and used to verify that the connection made to a remote system is the expected identity.
 *             
 * 
 * <p>Java class for tlsTrustStoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tlsTrustStoreType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="path" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="storePassword" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tlsTrustStoreType")
@XmlSeeAlso({
    TlsServerTrustStoreType.class
})
public class TlsTrustStoreType {

    @XmlAttribute(name = "path")
    protected String path;
    @XmlAttribute(name = "storePassword")
    protected String storePassword;

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * Gets the value of the storePassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStorePassword() {
        return storePassword;
    }

    /**
     * Sets the value of the storePassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStorePassword(String value) {
        this.storePassword = value;
    }

}
