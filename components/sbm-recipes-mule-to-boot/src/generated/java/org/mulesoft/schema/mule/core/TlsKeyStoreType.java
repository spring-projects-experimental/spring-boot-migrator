
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Configures key stores. TLS/SSL connections are made on behalf of an entity, which can be anonymous or identified by a certificate. This interface specifies how a keystore can be used to provide the certificates (and associated private keys) necessary for identification.
 *             
 * 
 * <p>Java class for tlsKeyStoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tlsKeyStoreType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="path" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="class" type="{http://www.mulesoft.org/schema/mule/core}keystoreType" /&gt;
 *       &lt;attribute name="type" type="{http://www.mulesoft.org/schema/mule/core}keystoreType" /&gt;
 *       &lt;attribute name="keyAlias" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="keyPassword" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="storePassword" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="algorithm" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tlsKeyStoreType")
public class TlsKeyStoreType {

    @XmlAttribute(name = "path")
    protected String path;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "keyAlias")
    protected String keyAlias;
    @XmlAttribute(name = "keyPassword")
    protected String keyPassword;
    @XmlAttribute(name = "storePassword")
    protected String storePassword;
    @XmlAttribute(name = "algorithm")
    protected String algorithm;

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

    /**
     * Gets the value of the keyAlias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyAlias() {
        return keyAlias;
    }

    /**
     * Sets the value of the keyAlias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyAlias(String value) {
        this.keyAlias = value;
    }

    /**
     * Gets the value of the keyPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyPassword() {
        return keyPassword;
    }

    /**
     * Sets the value of the keyPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyPassword(String value) {
        this.keyPassword = value;
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
     * Gets the value of the algorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Sets the value of the algorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgorithm(String value) {
        this.algorithm = value;
    }

}
