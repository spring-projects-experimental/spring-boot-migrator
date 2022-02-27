
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for expressionTransformerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="expressionTransformerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractTransformerType"&gt;
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element name="return-argument"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}expressionAttributes"/&gt;
 *                 &lt;attribute name="optional" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}optionalExpressionAttributes"/&gt;
 *       &lt;attribute name="returnSourceIfNull" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "expressionTransformerType", propOrder = {
    "returnArgument"
})
public class ExpressionTransformerType
    extends AbstractTransformerType
{

    @XmlElement(name = "return-argument")
    protected List<ExpressionTransformerType.ReturnArgument> returnArgument;
    @XmlAttribute(name = "returnSourceIfNull")
    protected Boolean returnSourceIfNull;
    @XmlAttribute(name = "evaluator")
    protected String evaluator;
    @XmlAttribute(name = "expression")
    protected String expression;
    @XmlAttribute(name = "custom-evaluator")
    protected String customEvaluator;

    /**
     * Gets the value of the returnArgument property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the returnArgument property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReturnArgument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExpressionTransformerType.ReturnArgument }
     * 
     * 
     */
    public List<ExpressionTransformerType.ReturnArgument> getReturnArgument() {
        if (returnArgument == null) {
            returnArgument = new ArrayList<ExpressionTransformerType.ReturnArgument>();
        }
        return this.returnArgument;
    }

    /**
     * Gets the value of the returnSourceIfNull property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReturnSourceIfNull() {
        return returnSourceIfNull;
    }

    /**
     * Sets the value of the returnSourceIfNull property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReturnSourceIfNull(Boolean value) {
        this.returnSourceIfNull = value;
    }

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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}expressionAttributes"/&gt;
     *       &lt;attribute name="optional" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ReturnArgument {

        @XmlAttribute(name = "optional")
        protected String optional;
        @XmlAttribute(name = "evaluator")
        protected String evaluator;
        @XmlAttribute(name = "expression", required = true)
        protected String expression;
        @XmlAttribute(name = "custom-evaluator")
        protected String customEvaluator;

        /**
         * Gets the value of the optional property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOptional() {
            return optional;
        }

        /**
         * Sets the value of the optional property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOptional(String value) {
            this.optional = value;
        }

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

    }

}
