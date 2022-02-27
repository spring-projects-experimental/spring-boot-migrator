
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for expressionMessageInfoMappingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="expressionMessageInfoMappingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageInfoMappingType"&gt;
 *       &lt;attribute name="messageIdExpression" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="correlationIdExpression" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "expressionMessageInfoMappingType")
public class ExpressionMessageInfoMappingType
    extends AbstractMessageInfoMappingType
{

    @XmlAttribute(name = "messageIdExpression", required = true)
    protected String messageIdExpression;
    @XmlAttribute(name = "correlationIdExpression", required = true)
    protected String correlationIdExpression;

    /**
     * Gets the value of the messageIdExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageIdExpression() {
        return messageIdExpression;
    }

    /**
     * Sets the value of the messageIdExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageIdExpression(String value) {
        this.messageIdExpression = value;
    }

    /**
     * Gets the value of the correlationIdExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrelationIdExpression() {
        return correlationIdExpression;
    }

    /**
     * Sets the value of the correlationIdExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrelationIdExpression(String value) {
        this.correlationIdExpression = value;
    }

}
