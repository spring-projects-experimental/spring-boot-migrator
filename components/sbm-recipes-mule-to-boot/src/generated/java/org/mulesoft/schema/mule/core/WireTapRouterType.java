
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wireTapRouterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wireTapRouterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}filteredInboundRouterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-outbound-endpoint"/&gt;
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
@XmlType(name = "wireTapRouterType", propOrder = {
    "abstractOutboundEndpoint"
})
public class WireTapRouterType
    extends FilteredInboundRouterType
{

    @XmlElementRef(name = "abstract-outbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractOutboundEndpointType> abstractOutboundEndpoint;

    /**
     * Gets the value of the abstractOutboundEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractOutboundEndpointType> getAbstractOutboundEndpoint() {
        return abstractOutboundEndpoint;
    }

    /**
     * Sets the value of the abstractOutboundEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     *     
     */
    public void setAbstractOutboundEndpoint(JAXBElement<? extends AbstractOutboundEndpointType> value) {
        this.abstractOutboundEndpoint = value;
    }

}
