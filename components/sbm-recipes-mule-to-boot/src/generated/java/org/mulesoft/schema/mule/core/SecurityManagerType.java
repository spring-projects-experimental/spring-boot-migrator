
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 The default security manager (type 'defaultSecurityManagerType') provides basic support for security functions. Other modules (JAAS, PGP, Spring Security) provide more advanced functionality.
 *             
 * 
 * <p>Java class for securityManagerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="securityManagerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractSecurityManagerType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="custom-security-provider" type="{http://www.mulesoft.org/schema/mule/core}customSecurityProviderType" minOccurs="0"/&gt;
 *         &lt;element name="custom-encryption-strategy" type="{http://www.mulesoft.org/schema/mule/core}customEncryptionStrategyType" minOccurs="0"/&gt;
 *         &lt;element name="secret-key-encryption-strategy" type="{http://www.mulesoft.org/schema/mule/core}secretKeyEncryptionStrategyType" minOccurs="0"/&gt;
 *         &lt;element name="password-encryption-strategy" type="{http://www.mulesoft.org/schema/mule/core}passwordEncryptionStrategyType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "securityManagerType", propOrder = {
    "customSecurityProvider",
    "customEncryptionStrategy",
    "secretKeyEncryptionStrategy",
    "passwordEncryptionStrategy"
})
public class SecurityManagerType
    extends AbstractSecurityManagerType
{

    @XmlElement(name = "custom-security-provider")
    protected CustomSecurityProviderType customSecurityProvider;
    @XmlElement(name = "custom-encryption-strategy")
    protected CustomEncryptionStrategyType customEncryptionStrategy;
    @XmlElement(name = "secret-key-encryption-strategy")
    protected SecretKeyEncryptionStrategyType secretKeyEncryptionStrategy;
    @XmlElement(name = "password-encryption-strategy")
    protected PasswordEncryptionStrategyType passwordEncryptionStrategy;

    /**
     * Gets the value of the customSecurityProvider property.
     * 
     * @return
     *     possible object is
     *     {@link CustomSecurityProviderType }
     *     
     */
    public CustomSecurityProviderType getCustomSecurityProvider() {
        return customSecurityProvider;
    }

    /**
     * Sets the value of the customSecurityProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomSecurityProviderType }
     *     
     */
    public void setCustomSecurityProvider(CustomSecurityProviderType value) {
        this.customSecurityProvider = value;
    }

    /**
     * Gets the value of the customEncryptionStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link CustomEncryptionStrategyType }
     *     
     */
    public CustomEncryptionStrategyType getCustomEncryptionStrategy() {
        return customEncryptionStrategy;
    }

    /**
     * Sets the value of the customEncryptionStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomEncryptionStrategyType }
     *     
     */
    public void setCustomEncryptionStrategy(CustomEncryptionStrategyType value) {
        this.customEncryptionStrategy = value;
    }

    /**
     * Gets the value of the secretKeyEncryptionStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link SecretKeyEncryptionStrategyType }
     *     
     */
    public SecretKeyEncryptionStrategyType getSecretKeyEncryptionStrategy() {
        return secretKeyEncryptionStrategy;
    }

    /**
     * Sets the value of the secretKeyEncryptionStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretKeyEncryptionStrategyType }
     *     
     */
    public void setSecretKeyEncryptionStrategy(SecretKeyEncryptionStrategyType value) {
        this.secretKeyEncryptionStrategy = value;
    }

    /**
     * Gets the value of the passwordEncryptionStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link PasswordEncryptionStrategyType }
     *     
     */
    public PasswordEncryptionStrategyType getPasswordEncryptionStrategy() {
        return passwordEncryptionStrategy;
    }

    /**
     * Sets the value of the passwordEncryptionStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link PasswordEncryptionStrategyType }
     *     
     */
    public void setPasswordEncryptionStrategy(PasswordEncryptionStrategyType value) {
        this.passwordEncryptionStrategy = value;
    }

}
