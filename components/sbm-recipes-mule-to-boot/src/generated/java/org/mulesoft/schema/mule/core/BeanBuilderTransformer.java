
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractTransformerType"&gt;
 *       &lt;sequence maxOccurs="unbounded"&gt;
 *         &lt;element name="bean-property"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}expressionAttributes"/&gt;
 *                 &lt;attribute name="property-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="optional" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="beanClass" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="beanFactory-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
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
    "beanProperty"
})
public class BeanBuilderTransformer
    extends AbstractTransformerType
{

    @XmlElement(name = "bean-property", required = true)
    protected List<BeanBuilderTransformer.BeanProperty> beanProperty;
    @XmlAttribute(name = "beanClass")
    protected String beanClass;
    @XmlAttribute(name = "beanFactory-ref")
    protected String beanFactoryRef;

    /**
     * Gets the value of the beanProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the beanProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBeanProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BeanBuilderTransformer.BeanProperty }
     * 
     * 
     */
    public List<BeanBuilderTransformer.BeanProperty> getBeanProperty() {
        if (beanProperty == null) {
            beanProperty = new ArrayList<BeanBuilderTransformer.BeanProperty>();
        }
        return this.beanProperty;
    }

    /**
     * Gets the value of the beanClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeanClass() {
        return beanClass;
    }

    /**
     * Sets the value of the beanClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeanClass(String value) {
        this.beanClass = value;
    }

    /**
     * Gets the value of the beanFactoryRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeanFactoryRef() {
        return beanFactoryRef;
    }

    /**
     * Sets the value of the beanFactoryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeanFactoryRef(String value) {
        this.beanFactoryRef = value;
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
     *       &lt;attribute name="property-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
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
    public static class BeanProperty {

        @XmlAttribute(name = "property-name", required = true)
        protected String propertyName;
        @XmlAttribute(name = "optional")
        protected String optional;
        @XmlAttribute(name = "evaluator")
        protected String evaluator;
        @XmlAttribute(name = "expression", required = true)
        protected String expression;
        @XmlAttribute(name = "custom-evaluator")
        protected String customEvaluator;

        /**
         * Gets the value of the propertyName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPropertyName() {
            return propertyName;
        }

        /**
         * Sets the value of the propertyName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPropertyName(String value) {
            this.propertyName = value;
        }

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
