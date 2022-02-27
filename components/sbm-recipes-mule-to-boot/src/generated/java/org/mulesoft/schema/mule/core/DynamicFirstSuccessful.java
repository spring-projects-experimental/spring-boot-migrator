
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractDynamicRoutingMessageProcessor"&gt;
 *       &lt;attribute name="failureExpression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class DynamicFirstSuccessful
    extends AbstractDynamicRoutingMessageProcessor
{

    @XmlAttribute(name = "failureExpression")
    protected String failureExpression;

    /**
     * Gets the value of the failureExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailureExpression() {
        return failureExpression;
    }

    /**
     * Sets the value of the failureExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailureExpression(String value) {
        this.failureExpression = value;
    }

}
