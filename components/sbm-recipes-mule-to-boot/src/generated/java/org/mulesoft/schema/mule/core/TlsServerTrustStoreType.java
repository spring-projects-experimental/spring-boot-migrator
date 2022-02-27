
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Configures a direct trust store. TLS/SSL connections are made to trusted systems. The public certificates of trusted systems are stored in a keystore (called a trust store) and used to verify that the connection made to a remote system is the expected identity.
 *             
 * 
 * <p>Java class for tlsServerTrustStoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tlsServerTrustStoreType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}tlsTrustStoreType"&gt;
 *       &lt;attribute name="class" type="{http://www.mulesoft.org/schema/mule/core}keystoreType" /&gt;
 *       &lt;attribute name="type" type="{http://www.mulesoft.org/schema/mule/core}keystoreType" /&gt;
 *       &lt;attribute name="algorithm" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="factory-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="explicitOnly" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="requireClientAuthentication" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tlsServerTrustStoreType")
public class TlsServerTrustStoreType
    extends TlsTrustStoreType
{

    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "algorithm")
    protected String algorithm;
    @XmlAttribute(name = "factory-ref")
    protected String factoryRef;
    @XmlAttribute(name = "explicitOnly")
    protected String explicitOnly;
    @XmlAttribute(name = "requireClientAuthentication")
    protected String requireClientAuthentication;

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

    /**
     * Gets the value of the factoryRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactoryRef() {
        return factoryRef;
    }

    /**
     * Sets the value of the factoryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactoryRef(String value) {
        this.factoryRef = value;
    }

    /**
     * Gets the value of the explicitOnly property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExplicitOnly() {
        return explicitOnly;
    }

    /**
     * Sets the value of the explicitOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExplicitOnly(String value) {
        this.explicitOnly = value;
    }

    /**
     * Gets the value of the requireClientAuthentication property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequireClientAuthentication() {
        return requireClientAuthentication;
    }

    /**
     * Sets the value of the requireClientAuthentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequireClientAuthentication(String value) {
        this.requireClientAuthentication = value;
    }

}
