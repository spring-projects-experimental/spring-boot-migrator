
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for passwordEncryptionStrategyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="passwordEncryptionStrategyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}encryptionStrategyType"&gt;
 *       &lt;attribute name="password" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="salt" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="iterationCount" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "passwordEncryptionStrategyType")
public class PasswordEncryptionStrategyType
    extends EncryptionStrategyType
{

    @XmlAttribute(name = "password", required = true)
    protected String password;
    @XmlAttribute(name = "salt")
    protected String salt;
    @XmlAttribute(name = "iterationCount")
    protected String iterationCount;

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the salt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the value of the salt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalt(String value) {
        this.salt = value;
    }

    /**
     * Gets the value of the iterationCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIterationCount() {
        return iterationCount;
    }

    /**
     * Sets the value of the iterationCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIterationCount(String value) {
        this.iterationCount = value;
    }

}
