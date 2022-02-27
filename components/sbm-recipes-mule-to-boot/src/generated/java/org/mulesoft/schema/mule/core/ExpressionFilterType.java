
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for expressionFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="expressionFilterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractFilterType"&gt;
 *       &lt;attribute name="evaluator" type="{http://www.mulesoft.org/schema/mule/core}expressionFilterEvaluators" /&gt;
 *       &lt;attribute name="expression" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="customEvaluator" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;attribute name="nullReturnsTrue" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "expressionFilterType")
public class ExpressionFilterType
    extends AbstractFilterType
{

    @XmlAttribute(name = "evaluator")
    protected String evaluator;
    @XmlAttribute(name = "expression", required = true)
    protected String expression;
    @XmlAttribute(name = "customEvaluator")
    protected String customEvaluator;
    @XmlAttribute(name = "nullReturnsTrue")
    protected String nullReturnsTrue;

    /**
     * Gets the value of the evaluator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvaluator() {
        return evaluator;
    }

    /**
     * Sets the value of the evaluator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvaluator(String value) {
        this.evaluator = value;
    }

    /**
     * Gets the value of the expression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Sets the value of the expression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * Gets the value of the customEvaluator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomEvaluator() {
        return customEvaluator;
    }

    /**
     * Sets the value of the customEvaluator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomEvaluator(String value) {
        this.customEvaluator = value;
    }

    /**
     * Gets the value of the nullReturnsTrue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNullReturnsTrue() {
        return nullReturnsTrue;
    }

    /**
     * Sets the value of the nullReturnsTrue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNullReturnsTrue(String value) {
        this.nullReturnsTrue = value;
    }

}
