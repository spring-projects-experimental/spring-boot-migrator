
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
 * <p>Java class for outboundCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="outboundCollectionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-outbound-router" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-catch-all-strategy" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="matchAll" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "outboundCollectionType", propOrder = {
    "abstractOutboundRouter",
    "abstractCatchAllStrategy"
})
public class OutboundCollectionType {

    @XmlElementRef(name = "abstract-outbound-router", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractOutboundRouterType>> abstractOutboundRouter;
    @XmlElementRef(name = "abstract-catch-all-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractCatchAllStrategyType> abstractCatchAllStrategy;
    @XmlAttribute(name = "matchAll")
    protected String matchAll;

    /**
     * Gets the value of the abstractOutboundRouter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractOutboundRouter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractOutboundRouter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionOrStaticRecipientListRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ChunkingRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link SingleEndpointFilteringOutboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link StaticRecipientListRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionRecipientListRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomOutboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RoundRobinSplitterType }{@code >}
     * {@link JAXBElement }{@code <}{@link EndpointSelectorRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionSplitterOutboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link MultipleEndpointFilteringOutboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link OutboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractOutboundRouterType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractOutboundRouterType>> getAbstractOutboundRouter() {
        if (abstractOutboundRouter == null) {
            abstractOutboundRouter = new ArrayList<JAXBElement<? extends AbstractOutboundRouterType>>();
        }
        return this.abstractOutboundRouter;
    }

    /**
     * Gets the value of the abstractCatchAllStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CustomForwardingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ForwardingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LoggingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractCatchAllStrategyType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractCatchAllStrategyType> getAbstractCatchAllStrategy() {
        return abstractCatchAllStrategy;
    }

    /**
     * Sets the value of the abstractCatchAllStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CustomForwardingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ForwardingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LoggingCatchAllStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractCatchAllStrategyType }{@code >}
     *     
     */
    public void setAbstractCatchAllStrategy(JAXBElement<? extends AbstractCatchAllStrategyType> value) {
        this.abstractCatchAllStrategy = value;
    }

    /**
     * Gets the value of the matchAll property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatchAll() {
        if (matchAll == null) {
            return "false";
        } else {
            return matchAll;
        }
    }

    /**
     * Sets the value of the matchAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatchAll(String value) {
        this.matchAll = value;
    }

}
