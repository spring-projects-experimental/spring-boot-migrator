
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for expressionOrStaticRecipientListRouterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="expressionOrStaticRecipientListRouterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}expressionRecipientListRouterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="recipients" type="{http://www.mulesoft.org/schema/mule/core}listOrSetType" minOccurs="0"/&gt;
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
@XmlType(name = "expressionOrStaticRecipientListRouterType", propOrder = {
    "recipients"
})
public class ExpressionOrStaticRecipientListRouterType
    extends ExpressionRecipientListRouterType
{

    protected ListOrSetType recipients;

    /**
     * Gets the value of the recipients property.
     * 
     * @return
     *     possible object is
     *     {@link ListOrSetType }
     *     
     */
    public ListOrSetType getRecipients() {
        return recipients;
    }

    /**
     * Sets the value of the recipients property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOrSetType }
     *     
     */
    public void setRecipients(ListOrSetType value) {
        this.recipients = value;
    }

}
