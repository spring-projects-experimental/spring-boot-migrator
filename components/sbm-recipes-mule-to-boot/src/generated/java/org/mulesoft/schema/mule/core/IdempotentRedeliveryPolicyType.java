
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for idempotentRedeliveryPolicyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="idempotentRedeliveryPolicyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractRedeliveryPolicyType"&gt;
 *       &lt;attribute name="useSecureHash" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="messageDigestAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="idExpression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="object-store-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "idempotentRedeliveryPolicyType")
public class IdempotentRedeliveryPolicyType
    extends AbstractRedeliveryPolicyType
{

    @XmlAttribute(name = "useSecureHash")
    protected Boolean useSecureHash;
    @XmlAttribute(name = "messageDigestAlgorithm")
    protected String messageDigestAlgorithm;
    @XmlAttribute(name = "idExpression")
    protected String idExpression;
    @XmlAttribute(name = "object-store-ref")
    protected String objectStoreRef;

    /**
     * Gets the value of the useSecureHash property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUseSecureHash() {
        if (useSecureHash == null) {
            return true;
        } else {
            return useSecureHash;
        }
    }

    /**
     * Sets the value of the useSecureHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUseSecureHash(Boolean value) {
        this.useSecureHash = value;
    }

    /**
     * Gets the value of the messageDigestAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageDigestAlgorithm() {
        return messageDigestAlgorithm;
    }

    /**
     * Sets the value of the messageDigestAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageDigestAlgorithm(String value) {
        this.messageDigestAlgorithm = value;
    }

    /**
     * Gets the value of the idExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdExpression() {
        return idExpression;
    }

    /**
     * Sets the value of the idExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdExpression(String value) {
        this.idExpression = value;
    }

    /**
     * Gets the value of the objectStoreRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectStoreRef() {
        return objectStoreRef;
    }

    /**
     * Sets the value of the objectStoreRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectStoreRef(String value) {
        this.objectStoreRef = value;
    }

}
