
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for choiceExceptionStrategyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="choiceExceptionStrategyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}exceptionStrategyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-exception-strategy" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "choiceExceptionStrategyType", propOrder = {
    "abstractExceptionStrategy"
})
public class ChoiceExceptionStrategyType
    extends ExceptionStrategyType
{

    @XmlElementRef(name = "abstract-exception-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class)
    protected List<JAXBElement<? extends ExceptionStrategyType>> abstractExceptionStrategy;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the abstractExceptionStrategy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractExceptionStrategy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractExceptionStrategy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends ExceptionStrategyType>> getAbstractExceptionStrategy() {
        if (abstractExceptionStrategy == null) {
            abstractExceptionStrategy = new ArrayList<JAXBElement<? extends ExceptionStrategyType>>();
        }
        return this.abstractExceptionStrategy;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
