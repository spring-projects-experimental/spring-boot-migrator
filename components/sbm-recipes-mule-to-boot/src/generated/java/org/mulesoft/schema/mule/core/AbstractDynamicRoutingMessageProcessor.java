
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractDynamicRoutingMessageProcessor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractDynamicRoutingMessageProcessor"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractRoutingMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-dynamic-route-resolver"/&gt;
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
@XmlType(name = "abstractDynamicRoutingMessageProcessor", propOrder = {
    "abstractDynamicRouteResolver"
})
@XmlSeeAlso({
    DynamicRoundRobin.class,
    DynamicAll.class,
    DynamicFirstSuccessful.class
})
public class AbstractDynamicRoutingMessageProcessor
    extends AbstractRoutingMessageProcessorType
{

    @XmlElementRef(name = "abstract-dynamic-route-resolver", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class)
    protected JAXBElement<?> abstractDynamicRouteResolver;

    /**
     * Gets the value of the abstractDynamicRouteResolver property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CustomRouterResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public JAXBElement<?> getAbstractDynamicRouteResolver() {
        return abstractDynamicRouteResolver;
    }

    /**
     * Sets the value of the abstractDynamicRouteResolver property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CustomRouterResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public void setAbstractDynamicRouteResolver(JAXBElement<?> value) {
        this.abstractDynamicRouteResolver = value;
    }

}
