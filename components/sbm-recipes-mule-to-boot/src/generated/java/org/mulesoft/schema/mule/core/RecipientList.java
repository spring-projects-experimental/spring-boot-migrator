
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractRoutingMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-message-info-mapping" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}defaultCorrelationAttributes"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}expressionAttributes"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "abstractMessageInfoMapping"
})
public class RecipientList
    extends AbstractRoutingMessageProcessorType
{

    @XmlElementRef(name = "abstract-message-info-mapping", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractMessageInfoMappingType> abstractMessageInfoMapping;
    @XmlAttribute(name = "enableCorrelation")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String enableCorrelation;
    @XmlAttribute(name = "evaluator")
    protected String evaluator;
    @XmlAttribute(name = "expression", required = true)
    protected String expression;
    @XmlAttribute(name = "custom-evaluator")
    protected String customEvaluator;

    /**
     * Gets the value of the abstractMessageInfoMapping property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractMessageInfoMappingType> getAbstractMessageInfoMapping() {
        return abstractMessageInfoMapping;
    }

    /**
     * Sets the value of the abstractMessageInfoMapping property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
     *     
     */
    public void setAbstractMessageInfoMapping(JAXBElement<? extends AbstractMessageInfoMappingType> value) {
        this.abstractMessageInfoMapping = value;
    }

    /**
     * Gets the value of the enableCorrelation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableCorrelation() {
        if (enableCorrelation == null) {
            return "IF_NOT_SET";
        } else {
            return enableCorrelation;
        }
    }

    /**
     * Sets the value of the enableCorrelation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableCorrelation(String value) {
        this.enableCorrelation = value;
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
