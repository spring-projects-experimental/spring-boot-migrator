
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Configure client key stores. TLS/SSL connections are made on behalf of an entity, which can be anonymous or identified by a certificate. This interface specifies how a keystore can be used to provide the certificates (and associated private keys) necessary for identification. This is also used as the trust store if no other trust store is specified and the explicitTrustStoreOnly parameter in the server trust store configuration is false. WARNING - due to restrictions in library implementations the values specified here typically apply to all connectors using this transport.
 *             
 * 
 * <p>Java class for tlsClientKeyStoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tlsClientKeyStoreType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="path" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="storePassword" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="class" type="{http://www.mulesoft.org/schema/mule/core}keystoreType" /&gt;
 *       &lt;attribute name="type" type="{http://www.mulesoft.org/schema/mule/core}keystoreType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tlsClientKeyStoreType")
public class TlsClientKeyStoreType {

    @XmlAttribute(name = "path")
    protected String path;
    @XmlAttribute(name = "storePassword")
    protected String storePassword;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "type")
    protected String type;

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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
