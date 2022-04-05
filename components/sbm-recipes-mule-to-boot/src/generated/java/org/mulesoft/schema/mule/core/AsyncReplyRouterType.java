
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for asyncReplyRouterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="asyncReplyRouterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractAsyncReplyRouterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-inbound-endpoint" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asyncReplyRouterType", propOrder = {
    "abstractInboundEndpoint"
})
@XmlSeeAlso({
    CustomAsyncReplyRouterType.class
})
public class AsyncReplyRouterType
    extends AbstractAsyncReplyRouterType
{

    @XmlElementRef(name = "abstract-inbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends AbstractInboundEndpointType>> abstractInboundEndpoint;

    /**
     * 
     *                                 The endpoint used to receive the response(s) on.
     *                             Gets the value of the abstractInboundEndpoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractInboundEndpoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractInboundEndpoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractInboundEndpointType>> getAbstractInboundEndpoint() {
        if (abstractInboundEndpoint == null) {
            abstractInboundEndpoint = new ArrayList<JAXBElement<? extends AbstractInboundEndpointType>>();
        }
        return this.abstractInboundEndpoint;
    }

}
