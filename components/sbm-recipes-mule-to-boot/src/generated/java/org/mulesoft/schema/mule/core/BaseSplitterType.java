
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for baseSplitterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="baseSplitterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractInterceptingMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-message-info-mapping" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}defaultCorrelationAttributes"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseSplitterType", propOrder = {
    "abstractMessageInfoMapping"
})
@XmlSeeAlso({
    Splitter.class,
    CollectionSplitter.class,
    MapSplitter.class,
    MessageChunkSplitter.class,
    CustomSplitter.class
})
public class BaseSplitterType
    extends AbstractInterceptingMessageProcessorType
{

    @XmlElementRef(name = "abstract-message-info-mapping", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractMessageInfoMappingType> abstractMessageInfoMapping;
    @XmlAttribute(name = "enableCorrelation")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String enableCorrelation;

    /**
     * Gets the value of the abstractMessageInfoMapping property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CustomMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link CustomMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionMessageInfoMappingType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractMessageInfoMappingType }{@code >}
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

}
